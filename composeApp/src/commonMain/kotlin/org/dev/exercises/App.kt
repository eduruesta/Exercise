package org.dev.exercises

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import org.dev.exercises.di.appModule
import org.dev.exercises.domain.model.BodyPart
import org.dev.exercises.presentation.screen.ExercisesScreen
import org.dev.exercises.presentation.screen.MuscleListScreen
import org.dev.exercises.theme.AppTheme
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.KoinApplication

sealed class Screen {
    object MuscleList : Screen()
    data class ExerciseList(val bodyPart: BodyPart) : Screen()
}

@Preview
@Composable
internal fun App() = AppTheme {
    KoinApplication(application = {
        modules(appModule)
    }) {
        var currentScreen by remember { mutableStateOf<Screen>(Screen.MuscleList) }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .windowInsetsPadding(WindowInsets.safeDrawing)
        ) {
            val screen = currentScreen
            when (screen) {
                is Screen.MuscleList -> {
                    MuscleListScreen(
                        onMuscleClick = { bodyPart ->
                            currentScreen = Screen.ExerciseList(bodyPart)
                        }
                    )
                }

                is Screen.ExerciseList -> {
                    ExercisesScreen(
                        bodyPart = screen.bodyPart,
                        onBackClick = {
                            currentScreen = Screen.MuscleList
                        }
                    )
                }
            }
        }
    }
}
