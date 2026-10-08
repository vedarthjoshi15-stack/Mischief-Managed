package com.example.model

enum class CommunityChannel(
    val id: String,
    val title: String,
    val iconEmoji: String,
    val subtitle: String
) {
    GREAT_HALL("chan_great_hall", "Great Hall Plaza", "🏰", "Campus-wide student announcements & gossip"),
    HOUSE_COMMON("chan_house", "House Common Lounge", "🦁", "Exclusive hangout for your sorted house"),
    POTIONS_SURVIVORS("chan_potions", "Snapple's Survivors", "🧪", "Cauldron advice & burn ointment trade"),
    DUELLING_CLUB("chan_duel", "Midnight Duel Club", "🪄", "Unofficial hex sparring & wand grip tips")
}

data class CommunityPost(
    val id: String,
    val authorName: String,
    val authorHouse: House,
    val authorTitle: String,
    val messageText: String,
    val timestamp: String,
    val channel: CommunityChannel,
    val likesCount: Int = 0,
    val isLikedByMe: Boolean = false,
    val isUserAuthor: Boolean = false
)

enum class AIPersona(
    val id: String,
    val displayName: String,
    val roleTitle: String,
    val iconEmoji: String,
    val promptTone: String
) {
    NICK_GHOST(
        id = "persona_nick",
        displayName = "Sir Nicholas",
        roleTitle = "Resident Spectral Guide",
        iconEmoji = "👻",
        promptTone = "Melodramatic, polite Tudor ghost whose head tilts at 45 degrees. Complains about the Headless Hunt."
    ),
    SNAPPLE(
        id = "persona_snapple",
        displayName = "Prof. Snapple",
        roleTitle = "Master of Potions & Scowls",
        iconEmoji = "🧪",
        promptTone = "Bitter, sarcastic, highly condescending, talks slowly, threatens detentions and cauldron explosions."
    ),
    DUMBLEDORF(
        id = "persona_dumbledorf",
        displayName = "Albus Dumbledorf",
        roleTitle = "Eccentric Headmaster",
        iconEmoji = "🧙‍♂️",
        promptTone = "Warm, twinkle-eyed, speaks in charming absurdities, offers sherbet lemons, randomly awards points to Gryffindoor."
    ),
    HERMIONE(
        id = "persona_hermione",
        displayName = "Hermione-ish",
        roleTitle = "Overachieving Freshman",
        iconEmoji = "📚",
        promptTone = "Rapid-fire academic, cites Hogwarts: A History, corrects pronunciation of spells, stresses over exams."
    )
}
