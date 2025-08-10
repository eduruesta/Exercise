package org.dev.exercises.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.dev.exercises.domain.model.Exercise
import org.dev.exercises.domain.model.BodyPart
import org.dev.exercises.domain.usecase.GetExercisesGroupedByMuscleUseCase

data class ExerciseUiState(
    val isLoading: Boolean = false,
    val exercisesByMuscle: Map<BodyPart, List<Exercise>> = emptyMap(),
    val error: String? = null
)

class ExerciseViewModel(
    private val getExercisesGroupedByMuscleUseCase: GetExercisesGroupedByMuscleUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(ExerciseUiState())
    val uiState: StateFlow<ExerciseUiState> = _uiState.asStateFlow()

    init {
        loadExercises()
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
    }
}
