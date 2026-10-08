package com.example.model

data class ClassScheduleItem(
    val id: String,
    val dayOfWeek: String, // "Monday", "Tuesday", etc.
    val timeSlot: String,
    val subjectName: String,
    val professor: String,
    val locationId: String,
    val locationName: String,
    val comedicNote: String,
    val requiredSupply: String
)

data class SubjectItem(
    val id: String,
    val name: String,
    val professor: String,
    val locationId: String,
    val description: String,
    val humorousWarning: String,
    val recommendedForHouse: House,
    val masteryLevel: Int = 1
)
