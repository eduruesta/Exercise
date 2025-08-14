package org.dev.exercises

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import kotlinx.serialization.Serializable
import org.dev.exercises.di.appModule
import org.dev.exercises.presentation.screen.ExerciseDetailScreen
import org.dev.exercises.presentation.screen.ExercisesScreen
import org.dev.exercises.presentation.screen.MuscleListScreen
import org.dev.exercises.presentation.subscription.RevenueCatPaywallScreen
import org.dev.exercises.theme.AppTheme
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.KoinApplication
import org.koin.compose.koinInject
import org.dev.exercises.data.subscription.RevenueCatManager
import org.dev.exercises.data.subscription.RevenueCatConfig

// Navigation routes
@Serializable
object MuscleList

@Serializable
data class ExerciseList(val bodyPart: String)

@Serializable
data class ExerciseDetail(val exerciseId: String, val bodyPart: String)

@Serializable
object SubscriptionPage


@Preview
@Composable
internal fun App() = AppTheme {
    KoinApplication(application = {
        modules(appModule)
    }) {
        val navController = rememberNavController()
        val revenueCatManager: RevenueCatManager = koinInject()

        // Initialize RevenueCat when the app starts
        LaunchedEffect(Unit) {
            try {
                revenueCatManager.initialize(RevenueCatConfig.API_KEY)
            } catch (e: Exception) {
                println("Failed to initialize RevenueCat: ${e.message}")
            }
        }

        Scaffold(
            modifier = Modifier
                .fillMaxSize()
                .windowInsetsPadding(WindowInsets.safeDrawing)
        ) { paddingValues ->
            NavHost(
                navController = navController,
                startDestination = MuscleList,
                modifier = Modifier.padding(paddingValues)
            ) {
                composable<MuscleList> {
                    MuscleListScreen(
                        onMuscleClick = { bodyPart ->
                            navController.navigate(ExerciseList(bodyPart.name))
                        },
                        onDirectExerciseClick = { exerciseId, bodyPart ->
                            navController.navigate(ExerciseDetail(exerciseId, bodyPart))
                        },
                        onSubscriptionClick = {
                            navController.navigate(SubscriptionPage)
                        }
                    )
                }


                composable<SubscriptionPage> {
                    RevenueCatPaywallScreen(
                        onBackClick = {
                            navController.popBackStack()
                        },
                        onSubscriptionSuccess = {
                            navController.popBackStack()
                        }
                    )
                }

                composable<ExerciseList> { backStackEntry ->
                    val exerciseList = backStackEntry.toRoute<ExerciseList>()

                    ExercisesScreen(
                        bodyPartName = exerciseList.bodyPart,
                        onBackClick = {
                            navController.popBackStack()
                        },
                        onExerciseClick = { exerciseId ->
                            navController.navigate(ExerciseDetail(exerciseId, exerciseList.bodyPart))
                        }
                    )
                }

                composable<ExerciseDetail> { backStackEntry ->
                    val exerciseDetail = backStackEntry.toRoute<ExerciseDetail>()

                    ExerciseDetailScreen(
                        exerciseId = exerciseDetail.exerciseId,
                        onBackClick = {
                            navController.popBackStack()
                        }
                    )
                }
            }
        }
    }
}

