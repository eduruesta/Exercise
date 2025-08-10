package org.dev.exercises.di

import io.ktor.client.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module
import org.dev.exercises.data.remote.ExerciseApiService
import org.dev.exercises.data.repository.ExerciseRepositoryImpl
import org.dev.exercises.domain.repository.ExerciseRepository
import org.dev.exercises.domain.usecase.GetExercisesGroupedByMuscleUseCase
import org.dev.exercises.presentation.viewmodel.ExerciseViewModel

val appModule = module {
    
    // HTTP Client
    single {
        HttpClient {
            install(ContentNegotiation) {
                json(Json {
                    ignoreUnknownKeys = true
                    isLenient = true
                })
            }
            install(Logging) {
                level = LogLevel.INFO
            }
        }
    }
    
    // API Service
    single { ExerciseApiService(get()) }
    
    // Repository
    single<ExerciseRepository> { ExerciseRepositoryImpl(get()) }
    
    // Use Cases
    single { GetExercisesGroupedByMuscleUseCase(get()) }
    
    // ViewModels
    viewModel { ExerciseViewModel(get()) }
}