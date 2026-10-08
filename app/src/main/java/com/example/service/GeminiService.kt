package com.example.service

import com.example.BuildConfig
import com.example.model.ChatMessage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import java.util.concurrent.TimeUnit

enum class ChatbotRole(
    val roleName: String,
    val title: String,
    val modelName: String,
    val emoji: String,
    val systemInstruction: String,
    val description: String
) {
    GENERAL_GUIDE(
        roleName = "Sir Nick-ish",
        title = "Nearly Headless Nick-ish",
        modelName = "gemini-3.5-flash",
        emoji = "👻",
        systemInstruction = """
            You are "Nearly Headless Nick-ish", the resident ghostly guide at Hogwash School of Witchcraft and Wizardry.
            Your tone is goofy, melodramatic, self-aware, British historical parody. Your head is tilted 45 degrees.
            You help first-year students with general castle guidance, moving staircases, house rivalries, and lore.
            Keep responses witty, 2-4 sentences, full of Harry Potter parody puns (Hairy Porter, Albus Dumbledorf, Professor Snapple).
        """.trimIndent(),
        description = "General castle guidance & witty lore (gemini-3.5-flash)"
    ),
    COMPLEX_SCHOLAR(
        roleName = "Prof. Snapple",
        title = "Professor Severus Snapple",
        modelName = "gemini-3.1-pro-preview",
        emoji = "🧪",
        systemInstruction = """
            You are Professor Severus Snapple, master of Potions and Severe Logic at Hogwash School.
            You solve complex magical dilemmas, intricate brewing equations, and deep academic reasoning.
            Your tone is sarcastic, intellectually arrogant, highly articulate, yet thoroughly accurate and helpful.
            You occasionally threaten to deduct points from Gryffindoor, but provide comprehensive, clever analysis.
        """.trimIndent(),
        description = "Complex magical theory, reasoning & brewing logic (gemini-3.1-pro-preview)"
    ),
    FAST_PRANKSTER(
        roleName = "Peeves Fast",
        title = "Peeves the Speedster",
        modelName = "gemini-3.1-flash-lite-preview",
        emoji = "⚡",
        systemInstruction = """
            You are Peeves the Poltergeist at Hogwash School.
            You are lightning-fast, mischievous, snappy, and chaotic!
            Answer in 1-2 rapid-fire sentences with funny rhymes or speedy zingers, while still giving the user the exact answer they need immediately.
        """.trimIndent(),
        description = "Lightning-fast answers & snappy roasts (gemini-3.1-flash-lite)"
    )
}

object GeminiService {

    private val client = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(60, TimeUnit.SECONDS)
        .writeTimeout(60, TimeUnit.SECONDS)
        .build()

    /**
     * Multi-turn chat interface maintaining conversation history and system instructions.
     */
    suspend fun askGeminiChat(
        history: List<ChatMessage>,
        userQuestion: String,
        role: ChatbotRole = ChatbotRole.GENERAL_GUIDE
    ): String = withContext(Dispatchers.IO) {
        val apiKey = try {
            BuildConfig.GEMINI_API_KEY
        } catch (e: Throwable) {
            ""
        }

        if (apiKey.isBlank() || apiKey == "MY_GEMINI_API_KEY") {
            return@withContext getLocalFallbackAnswer(userQuestion, role)
        }

        val url = "https://generativelanguage.googleapis.com/v1beta/models/${role.modelName}:generateContent?key=$apiKey"

        try {
            val root = JSONObject()

            // System instruction
            val sysContent = JSONObject()
            val sysParts = JSONArray()
            sysParts.put(JSONObject().put("text", role.systemInstruction))
            sysContent.put("parts", sysParts)
            root.put("systemInstruction", sysContent)

            // Multi-turn contents array preserving past conversation history
            val contents = JSONArray()

            // Include last 6 turns to keep context window fresh and responsive
            val recentHistory = history.takeLast(6)
            for (msg in recentHistory) {
                val turn = JSONObject()
                turn.put("role", if (msg.isFromUser) "user" else "model")
                val parts = JSONArray()
                parts.put(JSONObject().put("text", msg.text))
                turn.put("parts", parts)
                contents.put(turn)
            }

            // Append current user message
            val currentTurn = JSONObject()
            currentTurn.put("role", "user")
            val currentParts = JSONArray()
            currentParts.put(JSONObject().put("text", userQuestion))
            currentTurn.put("parts", currentParts)
            contents.put(currentTurn)

            root.put("contents", contents)

            // Generation config
            val config = JSONObject()
            config.put("temperature", if (role == ChatbotRole.COMPLEX_SCHOLAR) 0.4 else 0.7)
            config.put("maxOutputTokens", 350)
            root.put("generationConfig", config)

            val mediaType = "application/json; charset=utf-8".toMediaType()
            val requestBody = root.toString().toRequestBody(mediaType)

            val request = Request.Builder()
                .url(url)
                .post(requestBody)
                .build()

            val response = client.newCall(request).execute()
            val responseBody = response.body?.string()

            if (response.isSuccessful && responseBody != null) {
                val json = JSONObject(responseBody)
                val candidates = json.optJSONArray("candidates")
                if (candidates != null && candidates.length() > 0) {
                    val candidate = candidates.getJSONObject(0)
                    val contentObj = candidate.optJSONObject("content")
                    val parts = contentObj?.optJSONArray("parts")
                    if (parts != null && parts.length() > 0) {
                        val replyText = parts.getJSONObject(0).optString("text")
                        if (replyText.isNotBlank()) {
                            return@withContext replyText.trim()
                        }
                    }
                }
            }

            getLocalFallbackAnswer(userQuestion, role)
        } catch (e: Exception) {
            getLocalFallbackAnswer(userQuestion, role)
        }
    }

    suspend fun askGhostGuide(userQuestion: String): String {
        return askGeminiChat(emptyList(), userQuestion, ChatbotRole.GENERAL_GUIDE)
    }

    fun getLocalFallbackAnswer(query: String, role: ChatbotRole = ChatbotRole.GENERAL_GUIDE): String {
        val q = query.lowercase()

        if (role == ChatbotRole.COMPLEX_SCHOLAR) {
            return when {
                q.contains("potion") || q.contains("brew") ->
                    "Professor Snapple: 'Clearly, you have never consulted Advanced Potion Making. To prevent a catastrophic cauldron eruption, crush the Sopophorous bean with a silver blade rather than slicing it. Ten points from Gryffindoor for asking what should have been obvious.'"
                q.contains("transfig") || q.contains("spell") ->
                    "Professor Snapple: 'Transfiguration requires absolute molecular discipline. Intention without mental fortitude yields nothing more than half-transmuted teacups with rodent tails. Study harder.'"
                else ->
                    "Professor Snapple: 'A curious inquiry, though your lack of preparation is painfully evident. The answer lies in strict adherence to magical logic and silent wand movement. Do not waste my dungeon hours with trivialities.'"
            }
        }

        if (role == ChatbotRole.FAST_PRANKSTER) {
            return when {
                q.contains("where") || q.contains("how") ->
                    "Peeves: 'Down the chute, past the boot, drop a water balloon on Snapple's suit! Zoom!'"
                else ->
                    "Peeves: 'Tickle the pear, run up the stair, Peevesie is throwing chalk in your hair! Wheeeee!'"
            }
        }

        return when {
            q.contains("potion") || q.contains("snapple") || q.contains("cauldron") ->
                "Gadzooks! Down to the Dungeons you must go! Turn past the chilly flagstones, hold your breath to avoid the pickled slug vapor, and prepare your face for Professor Snapple's immediate disapproval. He deducted ten points from a ghost yesterday just for floating too enthusiastically!"

            q.contains("library") || q.contains("archive") || q.contains("book") || q.contains("pinch") ->
                "The Dusty Archives are on the second floor! Tread lightly, young scholar—Madam Pinch's ears can detect the crumple of parchment from forty yards. And do not, under any circumstances, tickle the dragon-hide volumes in the Restricted Section!"

            q.contains("staircase") || q.contains("moving") || q.contains("late") || q.contains("lost") ->
                "Ah! The 142 shifting staircases! They move entirely out of personal malice, usually right when you have four minutes before Transfiguration. If you find yourself suspended over the Great Chasm, simply wave politely at the portraits and wait for the four-minute swing!"

            q.contains("forest") || q.contains("wood") || q.contains("foliage") || q.contains("creature") || q.contains("greg") || q.contains("web") ->
                "Great Scott! The Forbidding Foliage is strictly out-of-bounds, unless of course you are in detention with Haggard! Deep inside lies Webby Hollow, where Aragog's Cousin Greg weaves macramé and complains of sensitive retinas. Take some peppermint drops if you dare go!"

            q.contains("haggard") || q.contains("hut") || q.contains("shack") || q.contains("scone") ->
                "Down near the forest fringe! Haggard is the dearest soul, but mind his hospitality! His rock cakes can dent goblin steel, and his latest pet—the Blast-Ended Screwtop—has a habit of launching itself rear-first into innocent bystanders!"

            q.contains("ravenclue") || q.contains("wit") ->
                "Ravenclue! Ah, high up in the windy attic. Brilliant minds, but terribly exhausting to converse with. Their bronze eagle knocker will demand you solve a paradoxical haiku just to grab your spare socks from your trunk!"

            q.contains("gryffindoor") || q.contains("brave") || q.contains("lion") ->
                "Gryffindoor Tower! Roaring fires and reckless bravado. They charge headfirst into trouble, rescue three first-years, set fire to the drapes, and Headmaster Dumbledorf awards them fifty points at dinner for it!"

            q.contains("slitherin") || q.contains("snake") || q.contains("dungeon") ->
                "Slitherin VIP Crypt! Underneath the Black Lake with murky green lighting. Shrewd, ambitious, and they get bulk discounts on silver cauldron cleaners. Never make a wager with a Slitherin unless you have a notary present!"

            q.contains("hufflefluff") || q.contains("badger") || q.contains("snack") ->
                "Hufflefluff! Tucked right beside the kitchens behind the vinegar barrel. The sweetest folk in the entire castle. They have never produced a dark wizard, but their lemon drizzle cake has caused three peace treaties!"

            q.contains("headless hunt") || q.contains("neck") || q.contains("head") ->
                "*Crack!* Ah, pardon my vertebrae! Forty-five degrees, you see! Sir Patrick Delaney-Podmore still insists forty-five degrees does not qualify as 'headless'! The elitism of the Headless Hunt is positively scandalous!"

            q.contains("spell") || q.contains("expelli") || q.contains("lumos") || q.contains("wand") ->
                "Always remember your wrist movement! Swish with dignity, flick with purpose! If you mumble 'Expelli-oops', your wand will end up inside your own left boot. I saw a lad duel his own shoe for three days in 1704!"

            else ->
                "By Merlin's spectacles! In all my five centuries floating through these drafty corridors, I have rarely heard such a curious question. My recommendation? Check your Marauder's Map, dodge the moving staircases, and whatever you do, do not accept confectionery from Peeves-ish!"
        }
    }

    /**
     * Transcribe spoken audio using model gemini-3.5-transcribe.
     */
    suspend fun transcribeAudio(audioBytes: ByteArray): String = withContext(Dispatchers.IO) {
        val apiKey = try {
            com.example.BuildConfig.GEMINI_API_KEY
        } catch (e: Throwable) {
            ""
        }
        if (apiKey.isBlank() || apiKey == "MY_GEMINI_API_KEY") {
            return@withContext "API Key missing. Please set GEMINI_API_KEY in Secrets panel."
        }

        val url = "https://generativelanguage.googleapis.com/v1beta/models/gemini-3.5-transcribe:generateContent?key=$apiKey"

        try {
            val root = JSONObject()
            val contents = JSONArray()
            val turn = JSONObject()
            turn.put("role", "user")
            
            val parts = JSONArray()
            parts.put(JSONObject().put("text", "Accurately transcribe this spoken spell incantation or voice note:"))
            
            val audioBase64 = android.util.Base64.encodeToString(audioBytes, android.util.Base64.NO_WRAP)
            val inlineData = JSONObject()
            inlineData.put("mimeType", "audio/mp4")
            inlineData.put("data", audioBase64)
            
            val filePart = JSONObject()
            filePart.put("inlineData", inlineData)
            parts.put(filePart)
            
            turn.put("parts", parts)
            contents.put(turn)
            root.put("contents", contents)

            val mediaType = "application/json; charset=utf-8".toMediaType()
            val requestBody = root.toString().toRequestBody(mediaType)
            val request = Request.Builder()
                .url(url)
                .post(requestBody)
                .build()

            val response = client.newCall(request).execute()
            val responseBody = response.body?.string()
            if (response.isSuccessful && responseBody != null) {
                val json = JSONObject(responseBody)
                val candidates = json.optJSONArray("candidates")
                if (candidates != null && candidates.length() > 0) {
                    val candidate = candidates.getJSONObject(0)
                    val contentObj = candidate.optJSONObject("content")
                    val partsArr = contentObj?.optJSONArray("parts")
                    if (partsArr != null && partsArr.length() > 0) {
                        val text = partsArr.getJSONObject(0).optString("text")
                        if (text.isNotBlank()) {
                            return@withContext text.trim()
                        }
                    }
                }
            }
            "Audio transcription returned no text."
        } catch (e: Exception) {
            "Transcription error: ${e.localizedMessage}"
        }
    }

}