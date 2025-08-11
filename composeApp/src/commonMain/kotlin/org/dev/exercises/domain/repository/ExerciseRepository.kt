package org.dev.exercises.domain.repository

import org.dev.exercises.domain.model.Exercise
import org.dev.exercises.domain.model.BodyPartData

interface ExerciseRepository {
    suspend fun getExercises(
        name: String? = null,
        type: String? = null,
        muscle: String? = null,
        difficulty: String? = null
    ): Result<List<Exercise>>

    suspend fun getExercisesByMuscle(muscle: String): Result<List<Exercise>>

    suspend fun getAllExercises(): Result<List<Exercise>>

    suspend fun getExerciseById(exerciseId: String): Result<Exercise>

    suspend fun getBodyParts(): Result<List<BodyPartData>>
}
