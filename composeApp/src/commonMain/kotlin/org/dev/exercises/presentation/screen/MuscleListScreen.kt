package org.dev.exercises.presentation.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import org.dev.exercises.domain.model.BodyPart
import org.dev.exercises.presentation.viewmodel.ExerciseUiState
import org.dev.exercises.presentation.viewmodel.ExerciseViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun MuscleListScreen(
    onMuscleClick: (BodyPart) -> Unit
) {
    val viewModel: ExerciseViewModel = koinViewModel()
    val uiState by viewModel.uiState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Header
        Text(
            text = "💪 Exercise Library",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        Text(
            text = "Select a muscle group to view exercises",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        when {
            uiState.isLoading -> {
                LoadingContent()
            }

            uiState.error != null -> {
                ErrorContent(
                    error = uiState.error!!,
                    onRetry = { viewModel.retry() }
                )
            }

            uiState.exercisesByMuscle.isEmpty() -> {
                EmptyContent(onRetry = { viewModel.retry() })
            }

            else -> {
                MuscleGroupList(
                    bodyParts = uiState.exercisesByMuscle.keys.toList(),
                    exercisesByMuscle = uiState.exercisesByMuscle,
                    onMuscleClick = onMuscleClick
                )
            }
        }
    }
}

@Composable
private fun LoadingContent() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CircularProgressIndicator()
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Loading muscle groups...",
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}

@Composable
private fun ErrorContent(
    error: String,
    onRetry: () -> Unit
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "❌ Error",
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.error
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = error,
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 32.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = onRetry) {
                Text("Retry")
            }
        }
    }
}

@Composable
private fun EmptyContent(onRetry: () -> Unit) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "🏃‍♂️ No muscle groups found",
                style = MaterialTheme.typography.headlineSmall
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Try refreshing to load muscle groups",
                style = MaterialTheme.typography.bodyMedium
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = onRetry) {
                Text("Refresh")
            }
        }
    }
}

@Composable
private fun MuscleGroupList(
    bodyParts: List<BodyPart>,
    exercisesByMuscle: Map<BodyPart, List<org.dev.exercises.domain.model.Exercise>>,
    onMuscleClick: (BodyPart) -> Unit
) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(bodyParts.sortedBy { it.displayName }) { bodyPart ->
            MuscleGroupCard(
                bodyPart = bodyPart,
                exerciseCount = exercisesByMuscle[bodyPart]?.size ?: 0,
                onClick = { onMuscleClick(bodyPart) }
            )
        }
    }
}

@Composable
private fun MuscleGroupCard(
    bodyPart: BodyPart,
    exerciseCount: Int,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = getBodyPartEmoji(bodyPart) + " " + bodyPart.displayName,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "$exerciseCount exercises available",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Text(
                text = "→",
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}

private fun getBodyPartEmoji(bodyPart: BodyPart): String {
    return when (bodyPart) {
        BodyPart.WAIST -> "🔥"
        BodyPart.QUADRICEPS -> "🦵"
        BodyPart.THIGHS -> "🦵"
        BodyPart.BACK -> "🔙"
        BodyPart.TRICEPS -> "🦾"
        BodyPart.UPPER_ARMS -> "💪"
        BodyPart.CALVES -> "🦶"
        BodyPart.BICEPS -> "💪"
        BodyPart.CHEST -> "🫁"
        BodyPart.SHOULDERS -> "🤲"
        BodyPart.FOREARMS -> "🤏"
        BodyPart.GLUTES -> "🍑"
        BodyPart.HAMSTRINGS -> "🦵"
        BodyPart.LATS -> "🪶"
        BodyPart.LOWER_BACK -> "⬇️"
        BodyPart.MIDDLE_BACK -> "🔙"
        BodyPart.NECK -> "🦒"
        BodyPart.TRAPS -> "🔺"
        BodyPart.ABDOMINALS -> "🔥"
        BodyPart.CORE -> "🔥"
    }
}
