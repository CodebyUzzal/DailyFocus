package com.dailyfocus.core.util

/**
 * Represents a one-shot UI message (e.g. snackbar) consumed by the presentation layer.
 * Using a sealed interface keeps the error-handling strategy extensible but simple.
 */
sealed interface UiMessage {
    val id: Long
    val text: String

    /** Standard transient message shown as a Snackbar. */
    data class Snackbar(
        override val text: String,
        override val id: Long = System.currentTimeMillis()
    ) : UiMessage
}
