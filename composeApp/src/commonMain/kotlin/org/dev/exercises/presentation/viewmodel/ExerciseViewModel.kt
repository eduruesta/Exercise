package org.dev.exercises.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.dev.exercises.domain.model.Exercise
import org.dev.exercises.domain.model.BodyPartData
import org.dev.exercises.domain.usecase.GetExercisesGroupedByMuscleUseCase
import org.dev.exercises.domain.usecase.GetExerciseByIdUseCase
import org.dev.exercises.domain.usecase.GetBodyPartsUseCase

data class ExerciseUiState(
    val isLoading: Boolean = false,
    val exercisesByMuscle: Map<String, List<Exercise>> = emptyMap(),
    val bodyParts: List<BodyPartData> = emptyList(),
    val error: String? = null
)

data class ExerciseDetailUiState(
    val isLoading: Boolean = false,
    val exercise: Exercise? = null,
    val error: String? = null
)

class ExerciseViewModel(
    private val getExercisesGroupedByMuscleUseCase: GetExercisesGroupedByMuscleUseCase,
    private val getExerciseByIdUseCase: GetExerciseByIdUseCase,
    private val getBodyPartsUseCase: GetBodyPartsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(ExerciseUiState())
    val uiState: StateFlow<ExerciseUiState> = _uiState.asStateFlow()

    private val _exerciseDetailUiState = MutableStateFlow(ExerciseDetailUiState())
    val exerciseDetailUiState: StateFlow<ExerciseDetailUiState> = _exerciseDetailUiState.asStateFlow()

    init {
        loadExercises()
        loadBodyParts()
    }

    fun loadExercises() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)

            try {
                val result = getExercisesGroupedByMuscleUseCase()

                if (result.isSuccess) {
                    val exercisesByMuscle = result.getOrNull() ?: emptyMap()
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        exercisesByMuscle = exercisesByMuscle,
                        error = null
                    )
                } else {
                    val error = result.exceptionOrNull()?.message ?: "Unknown error occurred"
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = error
                    )
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = e.message ?: "Unknown error occurred"
                )
            }
        }
    }

    fun retry() {
        loadExercises()
        loadBodyParts()
    }

    fun loadBodyParts() {
        viewModelScope.launch {
            try {
                val result = getBodyPartsUseCase()

                if (result.isSuccess) {
                    val bodyParts = result.getOrNull() ?: emptyList()
                    _uiState.value = _uiState.value.copy(
                        bodyParts = bodyParts
                    )
                } else {
                    // Body parts loading failed, but don't show error as it's not critical
                    println("Failed to load body parts: ${result.exceptionOrNull()?.message}")
                }
            } catch (e: Exception) {
                // Body parts loading failed, but don't show error as it's not critical
                println("Failed to load body parts: ${e.message}")
            }
        }
    }

    fun loadExerciseDetail(exerciseId: String) {
        viewModelScope.launch {
            _exerciseDetailUiState.value = _exerciseDetailUiState.value.copy(isLoading = true, error = null)

            try {
                val result = getExerciseByIdUseCase(exerciseId)

                if (result.isSuccess) {
                    val exercise = result.getOrNull()
                    _exerciseDetailUiState.value = _exerciseDetailUiState.value.copy(
                        isLoading = false,
                        exercise = exercise,
                        error = null
                    )
                } else {
                    val error = result.exceptionOrNull()?.message ?: "Unknown error occurred"
                    _exerciseDetailUiState.value = _exerciseDetailUiState.value.copy(
                        isLoading = false,
                        error = error
                    )
                }
            } catch (e: Exception) {
                _exerciseDetailUiState.value = _exerciseDetailUiState.value.copy(
                    isLoading = false,
                    error = e.message ?: "Unknown error occurred"
                )
            }
        }
    }

    fun retryExerciseDetail(exerciseId: String) {
        loadExerciseDetail(exerciseId)
    }
}
