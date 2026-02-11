package com.dailyfocus.core.ui.theme

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.TweenSpec
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally

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

    fun <T> slowTween() = TweenSpec<T>(
        durationMillis = DurationSlow,
        easing = EasingStandard
    )

    // Spring for bouncy, natural-feeling transitions
    fun <T> gentleSpring() = spring<T>(
        dampingRatio = Spring.DampingRatioLowBouncy,
        stiffness = Spring.StiffnessLow
    )

    // Screen transitions for NavHost
    val screenEnter: EnterTransition = fadeIn(TweenSpec(DurationNormal, easing = EasingStandard)) +
        slideInHorizontally(TweenSpec(DurationNormal, easing = EasingStandard)) { it / 8 }

    val screenExit: ExitTransition = fadeOut(TweenSpec(DurationFast, easing = EasingStandard)) +
        slideOutHorizontally(TweenSpec(DurationFast, easing = EasingStandard)) { -it / 8 }

    val screenPopEnter: EnterTransition = fadeIn(TweenSpec(DurationNormal, easing = EasingStandard)) +
        slideInHorizontally(TweenSpec(DurationNormal, easing = EasingStandard)) { -it / 8 }

    val screenPopExit: ExitTransition = fadeOut(TweenSpec(DurationFast, easing = EasingStandard)) +
        slideOutHorizontally(TweenSpec(DurationFast, easing = EasingStandard)) { it / 8 }
}
