package org.dev.exercises.domain.usecase

import org.dev.exercises.domain.model.BodyPartData
import org.dev.exercises.domain.repository.ExerciseRepository

class GetBodyPartsUseCase(
    private val repository: ExerciseRepository
) {
    suspend operator fun invoke(): Result<List<BodyPartData>> {
        return try {
            repository.getBodyParts()
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}