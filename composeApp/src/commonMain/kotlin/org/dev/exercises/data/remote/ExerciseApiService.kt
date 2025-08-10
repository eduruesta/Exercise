package org.dev.exercises.data.remote

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import org.dev.exercises.domain.model.ExerciseApiResponse

class ExerciseApiService(
    private val httpClient: HttpClient
) {
    companion object {
        private const val BASE_URL = "https://exercisedb-api1.p.rapidapi.com/api/v1"
        // TODO: Move API key to secure configuration (local.properties or environment variables)
        // For production, never hardcode API keys in source code
        private const val API_KEY = "f37565d2b3mshd71eb862b546083p1b3fdajsn8c803ca1eff6"
        private const val API_HOST = "exercisedb-api1.p.rapidapi.com"
        private const val EXERCISES_ENDPOINT = "$BASE_URL/exercises"
    }

    suspend fun getExercises(
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
    ): ExerciseApiResponse {
        return httpClient.get(EXERCISES_ENDPOINT) {
            header("x-rapidapi-key", API_KEY)
            header("x-rapidapi-host", API_HOST)

            name?.let { parameter("name", it) }
            keywords?.let { parameter("keywords", it) }
            targetMuscles?.let { parameter("targetMuscles", it) }
            secondaryMuscles?.let { parameter("secondaryMuscles", it) }
            exerciseType?.let { parameter("exerciseType", it) }
            bodyParts?.let { parameter("bodyParts", it) }
            equipments?.let { parameter("equipments", it) }
            parameter("limit", limit.coerceIn(1, 25))
            after?.let { parameter("after", it) }
            before?.let { parameter("before", it) }
        }.body()
    }
}
