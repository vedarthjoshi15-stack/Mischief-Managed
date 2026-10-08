package com.example.data

import com.example.model.*

object HogwashData {

    val LOCATIONS: List<LocationNode> = listOf(
        LocationNode(
            id = "loc_great_hall",
            name = "The Moderately Great Hall",
            parodySubtitle = "Where Food Magically Appears Cold",
            zone = Zone.CASTLE,
            xPct = 0.50f,
            yPct = 0.45f,
            dangerLevel = "Peaceful-ish",
            description = "A cavernous room with an enchanted ceiling displaying whatever the Scottish weather is (mostly drizzling misery). Watch out for flying roast chickens and Dumbledorf's rambling speeches."
        ),
        LocationNode(
            id = "loc_staircase",
            name = "Discombobulating Staircases",
            parodySubtitle = "Changing Directions Purely Out of Spite",
            zone = Zone.CASTLE,
            xPct = 0.50f,
            yPct = 0.30f,
            dangerLevel = "Mildly Perilous",
            description = "142 staircases that move whenever you are exactly three minutes late for Transfiguration. One has a trick step that swallows your left boot every Tuesday."
        ),
        LocationNode(
            id = "loc_library",
            name = "The Dusty Archives",
            parodySubtitle = "Whispering Only, Screaming Books Prohibited",
            zone = Zone.CASTLE,
            xPct = 0.28f,
            yPct = 0.24f,
            dangerLevel = "Safe but Intimidating",
            description = "Endless towering bookshelves watched by Madam Pinch, who can hear a page turn from three corridors away. The Restricted Section contains books that bite, curse, and file nuisance lawsuits."
        ),
        LocationNode(
            id = "loc_astronomy_tower",
            name = "Stargazer's Dizzy Tower",
            parodySubtitle = "Highest Point in Castle, Draftiest Wind",
            zone = Zone.CASTLE,
            xPct = 0.76f,
            yPct = 0.16f,
            dangerLevel = "Vertigo Warning",
            description = "Used for midnight astronomy lessons where everyone just guesses which blurry dot is Jupiter while freezing their wand fingers off."
        ),
        LocationNode(
            id = "loc_potions",
            name = "Snapple's Dungeon of Bubbles",
            parodySubtitle = "Where Professor Snapple Deducts 50 Points",
            zone = Zone.DUNGEONS,
            xPct = 0.25f,
            yPct = 0.65f,
            dangerLevel = "Toxic Fumes",
            description = "A dank, slimy cellar smelling of boiled cabbage and crushed dreams. Professor Snapple glares at anyone attempting to stir clockwise instead of counter-clockwise."
        ),
        LocationNode(
            id = "loc_slitherin_dungeon",
            name = "Slitherin VIP Crypt",
            parodySubtitle = "Green Lighting & Lake Monster Views",
            zone = Zone.DUNGEONS,
            xPct = 0.15f,
            yPct = 0.78f,
            dangerLevel = "Condescending",
            description = "Carved underneath the Black Lake. The windows look out onto gloomy waters where the Giant Squid occasionally presses its face against the glass during naps."
        ),
        LocationNode(
            id = "loc_hufflefluff_cellar",
            name = "Hufflefluff Snack Cellar",
            parodySubtitle = "Behind the Big Vinegar Barrel",
            zone = Zone.DUNGEONS,
            xPct = 0.40f,
            yPct = 0.72f,
            dangerLevel = "Dangerous Pastry Surplus",
            description = "Coziest room in the castle, smelling perpetually of warm sourdough and cinnamon. Tap the barrel to the rhythm of 'Helga Hufflefluff' or get drenched in vinegar."
        ),
        LocationNode(
            id = "loc_gryffindoor_tower",
            name = "Gryffindoor Roaring Tower",
            parodySubtitle = "Fat Portrait Password Challenge",
            zone = Zone.CASTLE,
            xPct = 0.75f,
            yPct = 0.38f,
            dangerLevel = "Chintz Armchairs",
            description = "Guarded by the Fat Soprano Portrait who refuses entry until you listen to her screech an entire Italian aria. Roaring fires and endless board games."
        ),
        LocationNode(
            id = "loc_ravenclue_attic",
            name = "Ravenclue High Attic",
            parodySubtitle = "Eagle Door Knocker with Unfair Philosophy",
            zone = Zone.CASTLE,
            xPct = 0.70f,
            yPct = 0.58f,
            dangerLevel = "Existential Crisis",
            description = "The door has no keyhole, only a bronze beak that asks questions like: 'If an owl hoots in the woods and nobody flinches, does it still poop on your letter?'"
        ),
        LocationNode(
            id = "loc_haggard_hut",
            name = "Haggard's Shaky Shack",
            parodySubtitle = "Home of Rock-Hard Scones & Illegal Pets",
            zone = Zone.GROUNDS,
            xPct = 0.62f,
            yPct = 0.78f,
            dangerLevel = "Canine Drool",
            description = "A wooden cottage on the boundary of the woods. Keeper of Keys Haggard will offer you rock cakes capable of shattering diamonds, alongside a cauldron of mystery tea.",
            canTriggerCreature = true
        ),
        LocationNode(
            id = "loc_forbidden_forest",
            name = "The Forbidding Foliage",
            parodySubtitle = "Strictly Out-of-Bounds (Unless in Detention)",
            zone = Zone.FORBIDDING_WOODS,
            xPct = 0.85f,
            yPct = 0.75f,
            dangerLevel = "Severe Monster Risk",
            description = "A dense ancient wood where the trees grow sideways, the shadows speak in riddles, and school rules declare it completely forbidden—right before sending 11-year-olds there for detention.",
            canTriggerCreature = true
        ),
        LocationNode(
            id = "loc_webby_hollow",
            name = "Webby Hollow of Regret",
            parodySubtitle = "DENSE Cobwebs & Aragog's Cousin Greg",
            zone = Zone.FORBIDDING_WOODS,
            xPct = 0.90f,
            yPct = 0.90f,
            dangerLevel = "Arachnid Overlord Territory",
            description = "Deep in the woods where giant carnivorous spiders convene book club meetings. The trees are draped in sticky silk sheets and discarded butterbeer caps.",
            isQuestTarget = true,
            canTriggerCreature = true
        ),
        LocationNode(
            id = "loc_sparkle_clearing",
            name = "Sparkle Unicorn Glade",
            parodySubtitle = "Silver Hoofprints & High-Strung Centaurs",
            zone = Zone.FORBIDDING_WOODS,
            xPct = 0.74f,
            yPct = 0.92f,
            dangerLevel = "Philosophical Hecklers",
            description = "A luminous mossy clearing where centaurs look at stars and complain about Mars being unusually bright tonight.",
            canTriggerCreature = true
        )
    )

    val EDGES: List<MapEdge> = listOf(
        // Great Hall connections
        MapEdge("loc_great_hall", "loc_staircase", weight = 4, isStaircase = true),
        MapEdge("loc_great_hall", "loc_hufflefluff_cellar", weight = 3),
        MapEdge("loc_great_hall", "loc_potions", weight = 5),
        MapEdge("loc_great_hall", "loc_haggard_hut", weight = 6),

        // Moving Staircases hub
        MapEdge("loc_staircase", "loc_library", weight = 3),
        MapEdge("loc_staircase", "loc_astronomy_tower", weight = 7, isStaircase = true),
        MapEdge("loc_staircase", "loc_gryffindoor_tower", weight = 5, isStaircase = true),
        MapEdge("loc_staircase", "loc_ravenclue_attic", weight = 6, isStaircase = true),

        // Dungeon routes
        MapEdge("loc_potions", "loc_slitherin_dungeon", weight = 2),
        MapEdge("loc_potions", "loc_hufflefluff_cellar", weight = 4),

        // Grounds routes
        MapEdge("loc_haggard_hut", "loc_forbidden_forest", weight = 4),
        MapEdge("loc_forbidden_forest", "loc_webby_hollow", weight = 5),
        MapEdge("loc_forbidden_forest", "loc_sparkle_clearing", weight = 3),
        MapEdge("loc_sparkle_clearing", "loc_webby_hollow", weight = 4)
    )

    val SORTING_QUESTIONS: List<SortingQuestion> = listOf(
        SortingQuestion(
            id = 1,
            hatRoastComment = "Ah, look what stumbled into my brim. Smells like nervous sweat and damp socks...",
            questionText = "You encounter a three-headed dog guarding a trapdoor. Your immediate instinct is to:",
            options = listOf(
                SortingOption("Charge it with a butter knife yelling a battle cry!", House.GRYFFINDOOR, "Ah, bravado with zero survival instincts. Classic."),
                SortingOption("Check if the trapdoor contains negotiable assets or rare bullion.", House.SLITHERIN, "Resourceful. Ethical? Highly questionable."),
                SortingOption("Analyze the acoustic resonance of canine sleep cycles.", House.RAVENCLUE, "You'd bore the beast to sleep with your thesis."),
                SortingOption("Offer each head a scratch behind the ears and a scone.", House.HUFFLEFLUFF, "Hopelessly adorable. You'll probably be eaten while smiling.")
            )
        ),
        SortingQuestion(
            id = 2,
            hatRoastComment = "Hmm, your cranial cavity echoes quite nicely. Let's test your moral spine...",
            questionText = "A moving staircase shifts while you're on it, stranding you between floors. You:",
            options = listOf(
                SortingOption("Leap across the 30-foot chasm to catch the balustrade!", House.GRYFFINDOOR, "Hospital wing frequent flyer points incoming."),
                SortingOption("Bribe a passing ghost to float you down or blame a rival.", House.SLITHERIN, "Delegation through extortion. Impressive."),
                SortingOption("Calculate the trajectory angles and wait for the 4-minute cycle.", House.RAVENCLUE, "Always calculating while missing lunchtime."),
                SortingOption("Sit down comfortably and share your pocket sandwich with a gargoyle.", House.HUFFLEFLUFF, "Never agitated, always snacking.")
            )
        ),
        SortingQuestion(
            id = 3,
            hatRoastComment = "Oof, let's see which authority figure you are most likely to exasperate...",
            questionText = "Professor Snapple accuses you of ruining a cauldron of Draught of Living Naptime. You:",
            options = listOf(
                SortingOption("Demand trial by magical combat in the courtyard!", House.GRYFFINDOOR, "Subtlety is truly dead in that thick skull."),
                SortingOption("Point out that the cauldron belonged to the student next to you.", House.SLITHERIN, "Snakes don't sweat; they redirect blame."),
                SortingOption("Cite page 412 of 'Advanced Draughts' where Snapple was actually wrong.", House.RAVENCLUE, "Correct, but now you have detention until retirement."),
                SortingOption("Apologize sincerely and offer to scrub all the cauldrons with lemon soap.", House.HUFFLEFLUFF, "The staff literally doesn't deserve your wholesome soul.")
            )
        ),
        SortingQuestion(
            id = 4,
            hatRoastComment = "Let's inspect your taste in illegal magical contraband...",
            questionText = "You find an ancient unlabelled glowing potion bottle in the corridor. You:",
            options = listOf(
                SortingOption("Chug it immediately to see what superpowers it gives!", House.GRYFFINDOOR, "Poison control keeps a cot permanently reserved for you."),
                SortingOption("Hide it in your robes to sell to second-years for 5 Galleons.", House.SLITHERIN, "Unregulated apothecary capitalism at its finest."),
                SortingOption("Perform a spectral chromatogram and catalog its vapor density.", House.RAVENCLUE, "Nerds gonna nerd, even when hazardous."),
                SortingOption("Hand it gently to the caretaker and ask if he needs a hot tea.", House.HUFFLEFLUFF, "The only sane person in this entire cursed castle.")
            )
        ),
        SortingQuestion(
            id = 5,
            hatRoastComment = "Such a jumble of petty desires and midnight snack cravings...",
            questionText = "What would you like people to say behind your back?",
            options = listOf(
                SortingOption("'That mad wizard jumped off the astronomy tower on a dare!'", House.GRYFFINDOOR, "Ego bigger than the Great Hall's ceiling."),
                SortingOption("'Do not cross them, they know where the bodies—and tax loopholes—are.'", House.SLITHERIN, "Menacing ambition, check."),
                SortingOption("'They corrected the headmaster's Latin grammar three times.'", House.RAVENCLUE, "Pedantic to the bitter end."),
                SortingOption("'Best person alive, never forgot anyone's birthday or biscuit preference.'", House.HUFFLEFLUFF, "Pure sunshine in human form.")
            )
        ),
        SortingQuestion(
            id = 6,
            hatRoastComment = "Getting closer to sealing your doom... I mean, sorting your house...",
            questionText = "Choose your preferred magical companion:",
            options = listOf(
                SortingOption("A lion that constantly sets fire to curtains.", House.GRYFFINDOOR, "Dramatic, loud, and expensive to insure."),
                SortingOption("A silver viper that whispers malicious stock tips.", House.SLITHERIN, "Insider trading with scales."),
                SortingOption("An owl that only delivers cryptic riddles and peer-reviewed journals.", House.RAVENCLUE, "Peak pretentiousness."),
                SortingOption("A chunky badger wearing a tiny hand-knitted scarf.", House.HUFFLEFLUFF, "Irresistible fluffiness.")
            )
        ),
        SortingQuestion(
            id = 7,
            hatRoastComment = "Final question! Don't sneeze on my lining...",
            questionText = "Your final exam is in 10 minutes and you forgot to study. What is your play?",
            options = listOf(
                SortingOption("Cast random hexes with intense confidence and dramatic flourish!", House.GRYFFINDOOR, "Confidence without competence: the classic duo."),
                SortingOption("Blackmail the exam proctor with photos of their secret toupee.", House.SLITHERIN, "Ruthless efficiency."),
                SortingOption("Stay up speed-reading all 12 volumes of 'Encyclopaedia Arcana'.", House.RAVENCLUE, "Panic-induced hyper-retention."),
                SortingOption("Bake apology cookies for the teacher and hope for mercy.", House.HUFFLEFLUFF, "It works 90% of the time, honestly.")
            )
        )
    )

    val RIDDLES: List<LocationRiddle> = listOf(
        LocationRiddle(
            id = "riddle_library",
            locationId = "loc_library",
            prompt = "I have a spine but no bones, hundreds of leaves but no branches, and Madam Pinch will banish you to the dungeon if you dog-ear me. What am I?",
            options = listOf("A magical fern", "A spell book", "A skeletal snake", "A sleeping gargoyle"),
            correctIndex = 1,
            explanation = "A spell book! And remember: dog-earing is punishable by cauldron scrubbing."
        ),
        LocationRiddle(
            id = "riddle_potions",
            locationId = "loc_potions",
            prompt = "What happens if you add powdered Bicorn horn BEFORE crushed beetle eyes in Professor Snapple's cauldron?",
            options = listOf("Your eyebrows turn permanently orange", "Instant cauldron explosion", "It turns into tasty pumpkin juice", "Snapple smiles (impossible)"),
            correctIndex = 0,
            explanation = "Your eyebrows glow neon orange for a fortnight! Snapple deducted 20 points just for thinking about it."
        ),
        LocationRiddle(
            id = "riddle_staircase",
            locationId = "loc_staircase",
            prompt = "If the 4th staircase swings left on Mondays and right on Wednesdays, which way does it swing when you're 2 minutes late for class?",
            options = listOf("Right", "Left", "Completely upside down into a broom closet", "It stays still"),
            correctIndex = 2,
            explanation = "It dumps you directly into a pile of smelly dusters. The castle lives on student tears."
        ),
        LocationRiddle(
            id = "riddle_webby_hollow",
            locationId = "loc_webby_hollow",
            prompt = "Aragog's Cousin Greg says: 'Eight hairy legs, eight beady eyes, but what do I truly desire from wandering first-years?'",
            options = listOf("Their fresh tender marrow", "Compliments on my silky web crochet", "Directions to the nearest bakery", "A tiny pair of eight earmuffs"),
            correctIndex = 1,
            explanation = "Greg considers himself an artisanal textile weaver! Praise his silk mandala and he lets you pass."
        )
    )

    val CREATURE_ENCOUNTERS: List<CreatureEncounter> = listOf(
        CreatureEncounter(
            id = "enc_blast_screwtop",
            locationId = "loc_haggard_hut",
            creatureName = "Blast-Ended Screwtop",
            dangerRating = "Volatile / Spicy",
            introLore = "A monstrous hybrid of a manticore and a fire crab that resembles a deformed, shell-less lobster with sparks shooting out of its posterior.",
            dilemma = "Haggard yells: 'Don't fret! Little Sparky just wants a hug! Mind the exploding rear end though!' Sparky is charging toward your shins.",
            choices = listOf(
                EncounterChoice(
                    choiceText = "Throw a stale rock-cake straight into its firing nozzle",
                    consequenceText = "The rock-cake corks the spark nozzle! Sparky sneezes a plume of purple glitter and burps happily.",
                    xpDelta = 40,
                    pointsDelta = 20
                ),
                EncounterChoice(
                    choiceText = "Attempt to pet its shiny armored flank while shouting 'Good puppy!'",
                    consequenceText = "Sparky blasts backward at Mach 2, singeing your robes and knocking Haggard into a pumpkin patch. Haggard chuckles warmly.",
                    xpDelta = 25,
                    pointsDelta = 10
                ),
                EncounterChoice(
                    choiceText = "Dive behind Haggard's water barrel and play dead",
                    consequenceText = "Sparky sniffs your boots, finds them unappetizing, and wanders off to chew an iron anvil.",
                    xpDelta = 15,
                    pointsDelta = 5
                )
            )
        ),
        CreatureEncounter(
            id = "enc_aragog_greg",
            locationId = "loc_webby_hollow",
            creatureName = "Aragog's Cousin Greg",
            dangerRating = "Tremendously Leggy",
            introLore = "A house-sized Acromantula with a mild lisp who complains incessantly that Aragog got all the fame in the history books.",
            dilemma = "Greg drops down on a thick silk thread, clicking his massive pincer mandibles: 'Visitors! Tell me honestly, does this silk weave make my thorax look bulbous?'",
            choices = listOf(
                EncounterChoice(
                    choiceText = "Praise the avant-garde symmetry of his cobweb macramé",
                    consequenceText = "Greg blushes in arachnid (turning slight mauve) and gifts you a spool of rare unbreakable Acromantula thread! Quest complete!",
                    xpDelta = 60,
                    pointsDelta = 30
                ),
                EncounterChoice(
                    choiceText = "Cast 'Lumos Maximum Watts' right into all eight eyes",
                    consequenceText = "Greg screams 'My delicate retinas!' and scrambles up a spruce tree, dropping a sparkly forest gem in his panic.",
                    xpDelta = 45,
                    pointsDelta = 15
                ),
                EncounterChoice(
                    choiceText = "Offer him a slice of Hufflefluff lemon pound cake",
                    consequenceText = "Greg eats the cake in one gulp: 'Exquisite crumb structure! Tell the badgers I send my highest compliments.'",
                    xpDelta = 50,
                    pointsDelta = 25
                )
            )
        ),
        CreatureEncounter(
            id = "enc_sparkle_hippogriff",
            locationId = "loc_sparkle_clearing",
            creatureName = "The Hippogriff-ter",
            dangerRating = "Proud & Easily Offended",
            introLore = "Half giant eagle, half draft horse, with talons as long as meat cleavers and the sensitive ego of a prima donna opera star.",
            dilemma = "The creature glares at you with piercing orange eyes, waiting for the customary etiquette test before permitting you into the glade.",
            choices = listOf(
                EncounterChoice(
                    choiceText = "Maintain eye contact without blinking and bow until your nose touches moss",
                    consequenceText = "The Hippogriff bows gracefully in return, allowing you to stroke its silver feathers and earn immense respect.",
                    xpDelta = 50,
                    pointsDelta = 25
                ),
                EncounterChoice(
                    choiceText = "Flap your arms and make screeching eagle noises",
                    consequenceText = "The beast looks at you with profound pity, shakes its head, and flies off in second-hand embarrassment.",
                    xpDelta = 20,
                    pointsDelta = -5
                ),
                EncounterChoice(
                    choiceText = "Toss a raw ferret with golden glitter sprinkled on top",
                    consequenceText = "The Hippogriff catches the ferret mid-air with an audible snap and lets out a regal screech of approval.",
                    xpDelta = 40,
                    pointsDelta = 15
                )
            )
        ),
        CreatureEncounter(
            id = "enc_fluffy_allergies",
            locationId = "loc_forbidden_forest",
            creatureName = "Fluffy's Nephew: Sneezy",
            dangerRating = "Three-Headed Sneeze Hazard",
            introLore = "A miniature three-headed Cerberus pup who unfortunately suffers from chronic hay fever when near pine needles.",
            dilemma = "All three heads inhale deeply at the exact same moment. A hurricane-force triple sneeze is imminent!",
            choices = listOf(
                EncounterChoice(
                    choiceText = "Play a soothing melody on a wooden kazoo",
                    consequenceText = "At the first note, all three heads yawn in harmony and curl into a snoring pile of fur.",
                    xpDelta = 45,
                    pointsDelta = 20
                ),
                EncounterChoice(
                    choiceText = "Hand it a bedsheet-sized handkerchief",
                    consequenceText = "The middle head blows its nose with a trumpeting honk that alerts every owl within 5 miles. It wags its tail gratefully.",
                    xpDelta = 35,
                    pointsDelta = 15
                ),
                EncounterChoice(
                    choiceText = "Cast 'Aguamenti' to clear the pine pollen",
                    consequenceText = "You turn the pup into a soaking wet angry mutt. It shakes water all over your pristine wizard robe.",
                    xpDelta = 20,
                    pointsDelta = 5
                )
            )
        )
    )

    val SUBJECTS: List<SubjectItem> = listOf(
        SubjectItem(
            id = "sub_potions",
            name = "Potions & Cauldron Catastrophes",
            professor = "Professor Severus Snapple",
            locationId = "loc_potions",
            description = "Learn how to brew glory, bottle fame, and stopple death, but mostly learn how to avoid having your cauldron dissolve through three floors of stone.",
            humorousWarning = "Do NOT breathe the lilac fumes. That's how we lost the Class of '94.",
            recommendedForHouse = House.SLITHERIN
        ),
        SubjectItem(
            id = "sub_charms",
            name = "Charms & Ergonomic Flicking",
            professor = "Professor Filius Swish-and-Flick",
            locationId = "loc_great_hall",
            description = "Master everyday enchantments from making tea kettles whistle Beethoven to levitating feather dusters without eye gouging.",
            humorousWarning = "Remember: It's Levi-OH-sa, not Levi-oh-SHOVE-IT-INTO-THE-FAN.",
            recommendedForHouse = House.RAVENCLUE
        ),
        SubjectItem(
            id = "sub_transfig",
            name = "Transfiguration & Animal Accidents",
            professor = "Professor Minerva Meowgonagall",
            locationId = "loc_staircase",
            description = "Turn matches into needles, teacups into gerbils, and yourself into detention if you attempt human transfiguration before Year 5.",
            humorousWarning = "If your gerbil still has a spout, keep it away from boiling water.",
            recommendedForHouse = House.GRYFFINDOOR
        ),
        SubjectItem(
            id = "sub_dada",
            name = "Defence Against Mild Inconveniences",
            professor = "Professor Quirky-Substitute #7",
            locationId = "loc_astronomy_tower",
            description = "A position cursed to have a different instructor every term. Learn how to counter boggarts, red caps, and aggressively rude poltergeists.",
            humorousWarning = "Do not shake hands with the professor if their turban smells of garlic.",
            recommendedForHouse = House.GRYFFINDOOR
        ),
        SubjectItem(
            id = "sub_herbology",
            name = "Herbology & Screaming Vegetables",
            professor = "Professor Sprout-ish",
            locationId = "loc_haggard_hut",
            description = "Gardening with teeth. Repot baby Mandrakes, harvest Bubotuber pus for acne, and dodge Devil's Snare tentacles.",
            humorousWarning = "Earmuffs on AT ALL TIMES. Mandrake screams cause acute ear leakage.",
            recommendedForHouse = House.HUFFLEFLUFF
        ),
        SubjectItem(
            id = "sub_astronomy",
            name = "Astronomy & Cold Toes",
            professor = "Professor Aurora Sinistra-ish",
            locationId = "loc_astronomy_tower",
            description = "Mapping constellations at 2:00 AM while shivering in woollen long-johns and hoping the wind doesn't blow your star charts to Scotland.",
            humorousWarning = "Do not point your brass telescope at the Gryffindoor common room windows.",
            recommendedForHouse = House.RAVENCLUE
        )
    )

    val TIMETABLE: List<ClassScheduleItem> = listOf(
        ClassScheduleItem(
            id = "tt_1",
            dayOfWeek = "Monday",
            timeSlot = "09:00 - 10:30",
            subjectName = "Potions with Prof. Snapple",
            professor = "Professor Snapple",
            locationId = "loc_potions",
            locationName = "Snapple's Dungeon of Bubbles",
            comedicNote = "Bring burn ointment and an apologetic facial expression.",
            requiredSupply = "Pewter Cauldron size 2"
        ),
        ClassScheduleItem(
            id = "tt_2",
            dayOfWeek = "Monday",
            timeSlot = "11:00 - 12:30",
            subjectName = "Charms: Swish & Flick",
            professor = "Prof. Swish-and-Flick",
            locationId = "loc_great_hall",
            locationName = "The Moderately Great Hall Annex",
            comedicNote = "Feather levitation. Wear goggles for stray quills.",
            requiredSupply = "Standard Wand & Feather"
        ),
        ClassScheduleItem(
            id = "tt_3",
            dayOfWeek = "Tuesday",
            timeSlot = "10:00 - 11:30",
            subjectName = "Transfiguration",
            professor = "Prof. Meowgonagall",
            locationId = "loc_staircase",
            locationName = "Moving Staircases Classroom 1B",
            comedicNote = "Turning toothpicks into walruses. No sneezing allowed.",
            requiredSupply = "Wand & Wooden Matchstick"
        ),
        ClassScheduleItem(
            id = "tt_4",
            dayOfWeek = "Wednesday",
            timeSlot = "13:30 - 15:00",
            subjectName = "Herbology with Screaming Roots",
            professor = "Prof. Sprout-ish",
            locationId = "loc_haggard_hut",
            locationName = "Greenhouse Three near Haggard's",
            comedicNote = "Mandrake repotting. Test earmuff seal by yelling 'Pumpernickel!'",
            requiredSupply = "Dragonhide Gloves & Pink Earmuffs"
        ),
        ClassScheduleItem(
            id = "tt_5",
            dayOfWeek = "Thursday",
            timeSlot = "14:00 - 15:30",
            subjectName = "Defence Against Mild Inconveniences",
            professor = "Prof. Quirky-Substitute",
            locationId = "loc_astronomy_tower",
            locationName = "Defence Classroom Tower",
            comedicNote = "Boggart practice. Practice imagining Snapple in your grandma's bonnet.",
            requiredSupply = "Courage & Clean Handkerchief"
        ),
        ClassScheduleItem(
            id = "tt_6",
            dayOfWeek = "Friday",
            timeSlot = "23:00 - 00:30",
            subjectName = "Midnight Astronomy",
            professor = "Prof. Sinistra-ish",
            locationId = "loc_astronomy_tower",
            locationName = "Stargazer's Dizzy Tower",
            comedicNote = "Locating Venus while clutching thermal flasks of Butterbeer-ish.",
            requiredSupply = "Brass Telescope & Thick Mittens"
        )
    )

    val ENCYCLOPEDIA_ENTRIES: List<EncyclopediaEntry> = listOf(
        // Spells
        EncyclopediaEntry(
            id = "sp_expellioops",
            title = "Expelli-oops",
            originalParodyRef = "Expelliarmus",
            category = EntryCategory.SPELLS,
            pronunciationOrType = "ex-PEL-lee-OOPS",
            effectOrDiet = "Disarms the opponent, but also drops your own wand into your shoe",
            humorousExplanation = "A signature duel spell popularized by clumsy first-years. Upon casting, a scarlet flash erupts and both duellists scramble around on their knees looking for their sticks.",
            snarkyTip = "Never cast on wet cobblestones unless you enjoy bruised kneecaps.",
            dangerLevel = "Mildly Embarrassing"
        ),
        EncyclopediaEntry(
            id = "sp_alohomoron",
            title = "Alohomoron",
            originalParodyRef = "Alohomora",
            category = EntryCategory.SPELLS,
            pronunciationOrType = "uh-LOH-hoh-MOR-on",
            effectOrDiet = "Unlocks any standard wooden door, but insults the lock out loud",
            humorousExplanation = "Invented by an impatient wizard who hated key rings. The door unlocks with a soft click and the knocker loudly mutters: 'Really? That flimsy deadbolt? Pathetic.'",
            snarkyTip = "Useless against high-tech muggle combination bike locks.",
            dangerLevel = "Harmless"
        ),
        EncyclopediaEntry(
            id = "sp_lumos_watts",
            title = "Lumos Maximum Watts",
            originalParodyRef = "Lumos Maxima",
            category = EntryCategory.SPELLS,
            pronunciationOrType = "LOO-mos MAX-i-mum WATTS",
            effectOrDiet = "Blinds everyone in a 50-foot radius including yourself",
            humorousExplanation = "Rather than a soft gentle orb, this spell produces the luminescent equivalent of an industrial stadium halogen floodlight.",
            snarkyTip = "Causes temporary retina burn in nearby ghosts.",
            dangerLevel = "Eye Hazard"
        ),
        EncyclopediaEntry(
            id = "sp_wingardium",
            title = "Wingardium Levi-so-so",
            originalParodyRef = "Wingardium Leviosa",
            category = EntryCategory.SPELLS,
            pronunciationOrType = "win-GAR-dee-um LEV-ee-so-so",
            effectOrDiet = "Levitates objects approximately 2 inches before dropping them",
            humorousExplanation = "The result of failing to enunciate the crisp 'GAR'. Items hover momentarily with an uncertain wobble, then plunge directly onto your toe.",
            snarkyTip = "Practice on feathers, not heavy iron skillets.",
            dangerLevel = "Toe Bruiser"
        ),
        EncyclopediaEntry(
            id = "sp_expecto_patron",
            title = "Expecto Patronizing",
            originalParodyRef = "Expecto Patronum",
            category = EntryCategory.SPELLS,
            pronunciationOrType = "ex-PEK-toe puh-TRON-eye-zing",
            effectOrDiet = "Conjures a silvery spectral animal that passive-aggressively critiques your life choices",
            humorousExplanation = "Requires your happiest memory to cast, but upon arrival, your silver otter patronus simply sighs: 'Really? That's your happiest memory? That's rather depressing.'",
            snarkyTip = "Dementors leave not out of fear, but sheer awkwardness.",
            dangerLevel = "Emotional Damage"
        ),

        // Creatures
        EncyclopediaEntry(
            id = "cr_blast_screwtop",
            title = "Blast-Ended Screwtop",
            originalParodyRef = "Blast-Ended Skrewt",
            category = EntryCategory.CREATURES,
            pronunciationOrType = "Magical Beast (XXXY Danger)",
            effectOrDiet = "Diet: Fresh lettuce, scrap iron, student socks",
            humorousExplanation = "An illegal cross-breed by Groundskeeper Haggard. They have no visible faces, stingers that ignite without warning, and a personality comparable to an angry wasp in a megaphone.",
            snarkyTip = "Feed them from behind a reinforced brick wall.",
            dangerLevel = "High Explosive"
        ),
        EncyclopediaEntry(
            id = "cr_aragog_greg",
            title = "Aragog's Cousin Greg",
            originalParodyRef = "Acromantula",
            category = EntryCategory.CREATURES,
            pronunciationOrType = "Giant Carnivorous Arachnid",
            effectOrDiet = "Diet: Stale scones, deer, gossip magazines",
            humorousExplanation = "Lurks in Webby Hollow of Eight-Legged Regret. Unlike his antisocial cousin Aragog, Greg appreciates artisanal macramé and will trade secrets for good gossip about the Ministry.",
            snarkyTip = "Never mention insecticide or raid spray in his hearing.",
            dangerLevel = "Venomous & Chatty"
        ),
        EncyclopediaEntry(
            id = "cr_fluffy_allergies",
            title = "Fluffy the Allergies (Sneezy)",
            originalParodyRef = "Fluffy (Cerberus)",
            category = EntryCategory.CREATURES,
            pronunciationOrType = "Three-Headed Hound",
            effectOrDiet = "Diet: Meat pies, kibble, antihistamines",
            humorousExplanation = "Vicious guardian beast who immediately falls dead asleep if anyone hums a tune, plays a harp, or plays smooth jazz on Spotify.",
            snarkyTip = "Keep a kazoo in your robes at all times.",
            dangerLevel = "Sleepy Giant"
        ),
        EncyclopediaEntry(
            id = "cr_hippogriffter",
            title = "The Hippogriff-ter",
            originalParodyRef = "Hippogriff",
            category = EntryCategory.CREATURES,
            pronunciationOrType = "Avian-Equine Hybrid",
            effectOrDiet = "Diet: Raw ferrets, glitter, flattery",
            humorousExplanation = "Immensely regal and hypersensitive. If you insult its plumage, it will file a formal grievance before mauling your robes.",
            snarkyTip = "Bow low and do NOT blink.",
            dangerLevel = "Sharp Talons"
        ),

        // Objects
        EncyclopediaEntry(
            id = "ob_invis_poncho",
            title = "Invisibility Poncho",
            originalParodyRef = "Invisibility Cloak",
            category = EntryCategory.OBJECTS,
            pronunciationOrType = "Relic of Dubious Quality",
            effectOrDiet = "Turns you invisible, except for your bright yellow sneakers",
            humorousExplanation = "A discount version of the legendary Deathly Hallow bought off a goblin clearance stall. It smells strongly of mothballs and is slightly too short.",
            snarkyTip = "Keep your knees bent while sneaking through the corridors.",
            dangerLevel = "Fashion Crime"
        ),
        EncyclopediaEntry(
            id = "ob_remembrall_forgot",
            title = "Remembrall-Forgot",
            originalParodyRef = "Remembrall",
            category = EntryCategory.OBJECTS,
            pronunciationOrType = "Glass Smoke Bauble",
            effectOrDiet = "Turns scarlet when you've forgotten something, but NEVER tells you what",
            humorousExplanation = "A cruel invention that fills with angry red smoke to remind you that your brain has failed, leaving you to guess in panic for the rest of the day.",
            snarkyTip = "You probably forgot your Herbology earmuffs. Again.",
            dangerLevel = "Stress Inducer"
        ),
        EncyclopediaEntry(
            id = "ob_time_turner",
            title = "Time-Turner-Upper",
            originalParodyRef = "Time-Turner",
            category = EntryCategory.OBJECTS,
            pronunciationOrType = "Hourglass Pendant",
            effectOrDiet = "Sends you back 1 hour, but you arrive with a terrible hangover",
            humorousExplanation = "Used by overachieving students to attend three lectures simultaneously while visibly decaying from sleep deprivation.",
            snarkyTip = "Do NOT run into your past self; they will steal your sandwich.",
            dangerLevel = "Paradox Risk"
        ),

        // Traditions
        EncyclopediaEntry(
            id = "tr_great_feast",
            title = "The Feast of Beige Food",
            originalParodyRef = "Start-of-Term Feast",
            category = EntryCategory.TRADITIONS,
            pronunciationOrType = "Hogwash Culinary Custom",
            effectOrDiet = "Potatoes in 14 distinct states of matter",
            humorousExplanation = "Every term begins with mountains of roast beef, Yorkshire puddings, mashed potatoes, and treacle tart appearing on golden platters.",
            snarkyTip = "Grab the roast chicken before the Gryffindoors inhale it.",
            dangerLevel = "Carb Coma"
        ),
        EncyclopediaEntry(
            id = "tr_house_cup",
            title = "The House Cup Shenanigans",
            originalParodyRef = "The House Cup",
            category = EntryCategory.TRADITIONS,
            pronunciationOrType = "Annual Point Robbery",
            effectOrDiet = "Hourglasses filled with rubies, emeralds, sapphires, and yellow marbles",
            humorousExplanation = "A year-long competition where Slitherin leads all year until Headmaster Dumbledorf awards 400 points to Gryffindoor at dessert for breathing bravely.",
            snarkyTip = "Never get attached to the scoreboard until the last minute.",
            dangerLevel = "Bitter Feuds"
        )
    )

    val BADGES: List<BadgeItem> = listOf(
        BadgeItem(
            id = "badge_sorting_survivor",
            name = "Hat Roast Survivor",
            iconEmoji = "🎩",
            description = "Endured 7 questions of unprovoked sass from the ancient enchanted Sorting Hat."
        ),
        BadgeItem(
            id = "badge_staircase_survivor",
            name = "Staircase Survivor",
            iconEmoji = "🪜",
            description = "Successfully recalculated your route after the Moving Staircases shifted out of spite."
        ),
        BadgeItem(
            id = "badge_library_lurker",
            name = "Library Lurker",
            iconEmoji = "📚",
            description = "Solved a riddle in the Dusty Archives without getting chased out by Madam Pinch."
        ),
        BadgeItem(
            id = "badge_forest_forager",
            name = "Forest Forager",
            iconEmoji = "🌲",
            description = "Braved the deep Forbidding Foliage to find the legendary Webby Hollow!"
        ),
        BadgeItem(
            id = "badge_creature_companion",
            name = "Creature Companion",
            iconEmoji = "🐾",
            description = "Completed an interactive encounter with one of Hogwash's terrifying or goofy beasts!"
        ),
        BadgeItem(
            id = "badge_snapples_least_fav",
            name = "Snapple's Despised",
            iconEmoji = "🧪",
            description = "Survived a visit to the Potions Dungeon without detonating your pewter cauldron."
        ),
        BadgeItem(
            id = "badge_ghost_whisperer",
            name = "Ghost Whisperer",
            iconEmoji = "👻",
            description = "Had an enlightening chat with Sir Nearly Headless Nick-ish."
        )
    )

    val QUESTS: List<QuestItem> = listOf(
        QuestItem(
            id = "quest_forest_forager",
            title = "The Forbidding Foliage Expedition",
            punSubtitle = "Where No Sane First-Year Hath Trodden",
            questType = QuestType.SIDE_EXPEDITION,
            description = "Venture deep past Haggard's Shaky Shack into the treacherous Forbidding Foliage using the Marauder's Map. Reach 'Webby Hollow of Regret', solve Greg's riddle, and survive!",
            targetLocationId = "loc_webby_hollow",
            rewardXp = 80,
            rewardPoints = 40,
            rewardBadgeId = "badge_forest_forager"
        ),
        QuestItem(
            id = "quest_attend_class",
            title = "Punctual Prodigy (Almost)",
            punSubtitle = "Beat the Moving Staircase Clock",
            questType = QuestType.DAILY,
            description = "Check your timetable, click 'Guide Me There', and arrive at your scheduled classroom without falling into a vanishing step.",
            targetLocationId = "loc_potions",
            rewardXp = 40,
            rewardPoints = 20,
            rewardBadgeId = "badge_snapples_least_fav"
        ),
        QuestItem(
            id = "quest_creature_cuddle",
            title = "Beast Befriender Challenge",
            punSubtitle = "Keep All Ten Wand Fingers",
            questType = QuestType.SIDE_EXPEDITION,
            description = "Trigger a Creature Encounter at Haggard's Shack or in the woods, make a humorous choice, and earn the Creature Companion badge.",
            targetLocationId = "loc_haggard_hut",
            rewardXp = 50,
            rewardPoints = 25,
            rewardBadgeId = "badge_creature_companion"
        ),
        QuestItem(
            id = "quest_ghost_consult",
            title = "Nearly Headless Inquiries",
            punSubtitle = "Mind the 45-Degree Neck Tilt",
            questType = QuestType.DAILY,
            description = "Ask Nick-ish about castle locations, potions, or ghosts to receive his melodramatic Renaissance wisdom.",
            rewardXp = 30,
            rewardPoints = 15,
            rewardBadgeId = "badge_ghost_whisperer"
        )
    )
}
