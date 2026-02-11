package com.dailyfocus.core.ui.theme

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.TweenSpec

object AnimationConstants {
    const val DurationFast = 150
    const val DurationNormal = 250
    const val DurationSlow = 400

    val EasingStandard = FastOutSlowInEasing

    fun <T> fastTween() = TweenSpec<T>(
        durationMillis = DurationFast,
        easing = EasingStandard
    )

    fun <T> normalTween() = TweenSpec<T>(
        durationMillis = DurationNormal,
        easing = EasingStandard
    )
}
