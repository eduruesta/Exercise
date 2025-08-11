package org.dev.exercises.data.repository

import org.dev.exercises.data.remote.ExerciseApiService
import org.dev.exercises.domain.model.BodyPartData
import org.dev.exercises.domain.model.Exercise
import org.dev.exercises.domain.repository.ExerciseRepository

class ExerciseRepositoryImpl(
    private val apiService: ExerciseApiService
) : ExerciseRepository {

    override suspend fun getExercises(
        name: String?,
        type: String?,
        muscle: String?,
        difficulty: String?
    ): Result<List<Exercise>> {
        return try {
            val response = apiService.getExercises(
                name = name,
                exerciseType = type,
                bodyParts = muscle,
                limit = 25
            )
            Result.success(response.data)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getExercisesByMuscle(muscle: String): Result<List<Exercise>> {
        return try {
            val response = apiService.getExercises(
                bodyParts = muscle,
                limit = 25
            )
            Result.success(response.data)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getAllExercises(): Result<List<Exercise>> {
        return try {
            // Fetch exercises with a higher limit to get more variety
            val response = apiService.getExercises(limit = 25)
            Result.success(response.data)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getExerciseById(exerciseId: String): Result<Exercise> {
        return try {
            val response = apiService.getExerciseById(exerciseId)
            Result.success(response.data)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getBodyParts(): Result<List<BodyPartData>> {
        return try {
            val response = apiService.getBodyParts()
            Result.success(response.data)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // Additional method to get exercises with pagination support
    suspend fun getExercisesWithPagination(
        name: String? = null,
        keywords: String? = null,
        targetMuscles: String? = null,
        secondaryMuscles: String? = null,
        exerciseType: String? = null,
        bodyParts: String? = null,
        equipments: String? = null,
        limit: Int = 10,
        after: String? = null,
        before: String? = null
    ): Result<org.dev.exercises.domain.model.ExerciseApiResponse> {
        return try {
            val response = apiService.getExercises(
                name = name,
                keywords = keywords,
                targetMuscles = targetMuscles,
                secondaryMuscles = secondaryMuscles,
                exerciseType = exerciseType,
                bodyParts = bodyParts,
                equipments = equipments,
                limit = limit,
                after = after,
                before = before
            )
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
