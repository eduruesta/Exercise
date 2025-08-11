package org.dev.exercises.domain.usecase

import org.dev.exercises.domain.model.Exercise
import org.dev.exercises.domain.repository.ExerciseRepository

class GetExercisesGroupedByMuscleUseCase(
    private val repository: ExerciseRepository
) {
    suspend operator fun invoke(): Result<Map<String, List<Exercise>>> {
        return try {
            val allExercisesResult = repository.getAllExercises()

            if (allExercisesResult.isFailure) {
                return Result.failure(allExercisesResult.exceptionOrNull() ?: Exception("Unknown error"))
            }

            val exercises = allExercisesResult.getOrNull() ?: emptyList()

            // Create a map to group exercises by body parts
            val groupedExercises = mutableMapOf<String, MutableList<Exercise>>()

            exercises.forEach { exercise ->
                exercise.bodyParts.forEach { bodyPartString ->
                    groupedExercises.getOrPut(bodyPartString) { mutableListOf() }.add(exercise)
                }
            }

            // Convert to immutable map and sort exercises by name
            val sortedGroupedExercises = groupedExercises
                .mapValues { (_, exerciseList) ->
                    exerciseList.distinctBy { it.exerciseId }.sortedBy { it.name }
                }

            Result.success(sortedGroupedExercises)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
