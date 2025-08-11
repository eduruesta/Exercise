package org.dev.exercises.presentation.utils

/**
 * Converts uppercase text to lowercase with proper capitalization
 */
fun String.toDisplayText(): String {
    return this.lowercase().split(" ").joinToString(" ") { word ->
        word.replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() }
    }
}

/**
 * Converts a list of uppercase strings to display format
 */
fun List<String>.toDisplayTextList(): List<String> {
    return this.map { it.toDisplayText() }
}