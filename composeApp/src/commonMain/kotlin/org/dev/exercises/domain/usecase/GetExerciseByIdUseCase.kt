package org.dev.exercises.domain.usecase

import org.dev.exercises.domain.model.Exercise
import org.dev.exercises.domain.repository.ExerciseRepository

class GetExerciseByIdUseCase(
    private val repository: ExerciseRepository
) {
    suspend operator fun invoke(exerciseId: String): Result<Exercise> {
        return try {
            repository.getExerciseById(exerciseId)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}