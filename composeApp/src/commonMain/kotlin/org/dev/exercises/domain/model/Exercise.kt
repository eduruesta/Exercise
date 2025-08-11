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
data class ExerciseDetailApiResponse(
    val success: Boolean,
    val data: Exercise
)

@Serializable
data class BodyPartsApiResponse(
    val success: Boolean,
    val data: List<BodyPartData>
)

@Serializable
data class BodyPartData(
    val name: String,
    val imageUrl: String
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
    val keywords: List<String>,
    // Additional fields for detailed exercise information
    val videoUrl: String? = null,
    val overview: String? = null,
    val instructions: List<String>? = null,
    val exerciseTips: List<String>? = null,
    val variations: List<String>? = null,
    val relatedExerciseIds: List<String>? = null
)

