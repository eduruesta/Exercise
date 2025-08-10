package org.dev.exercises.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class ExerciseApiResponse(
    val success: Boolean,
    val meta: ExerciseMeta,
    val data: List<Exercise>
)

@Serializable
data class ExerciseMeta(
    val total: Int,
    val hasNextPage: Boolean,
    val hasPreviousPage: Boolean,
    val nextCursor: String? = null
)

@Serializable
data class Exercise(
    val exerciseId: String,
    val name: String,
    val imageUrl: String,
    val bodyParts: List<String>,
    val equipments: List<String>,
    val exerciseType: String,
    val targetMuscles: List<String>,
    val secondaryMuscles: List<String>,
    val keywords: List<String>
)

enum class ExerciseType(val value: String) {
    CARDIO("CARDIO"),
    STRENGTH("STRENGTH"),
    STRETCHING("STRETCHING"),
    FLEXIBILITY("FLEXIBILITY")
}

enum class BodyPart(val value: String, val displayName: String) {
    WAIST("WAIST", "Waist"),
    QUADRICEPS("QUADRICEPS", "Quadriceps"),
    THIGHS("THIGHS", "Thighs"),
    BACK("BACK", "Back"),
    TRICEPS("TRICEPS", "Triceps"),
    UPPER_ARMS("UPPER ARMS", "Upper Arms"),
    CALVES("CALVES", "Calves"),
    BICEPS("BICEPS", "Biceps"),
    CHEST("CHEST", "Chest"),
    SHOULDERS("SHOULDERS", "Shoulders"),
    FOREARMS("FOREARMS", "Forearms"),
    GLUTES("GLUTES", "Glutes"),
    HAMSTRINGS("HAMSTRINGS", "Hamstrings"),
    LATS("LATS", "Lats"),
    LOWER_BACK("LOWER BACK", "Lower Back"),
    MIDDLE_BACK("MIDDLE BACK", "Middle Back"),
    NECK("NECK", "Neck"),
    TRAPS("TRAPS", "Traps"),
    ABDOMINALS("ABDOMINALS", "Abdominals"),
    CORE("CORE", "Core")
}

enum class Equipment(val value: String, val displayName: String) {
    BODY_WEIGHT("BODY WEIGHT", "Body Weight"),
    DUMBBELL("DUMBBELL", "Dumbbell"),
    BARBELL("BARBELL", "Barbell"),
    CABLE("CABLE", "Cable"),
    MACHINE("MACHINE", "Machine"),
    KETTLEBELL("KETTLEBELL", "Kettlebell"),
    RESISTANCE_BAND("RESISTANCE BAND", "Resistance Band"),
    MEDICINE_BALL("MEDICINE BALL", "Medicine Ball"),
    STABILITY_BALL("STABILITY BALL", "Stability Ball")
}
