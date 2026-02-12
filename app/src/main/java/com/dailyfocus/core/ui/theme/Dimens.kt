package com.dailyfocus.core.ui.theme

import androidx.compose.ui.unit.dp

/**
 * DailyFocus strict spatial system.
 * Based on a 4dp grid.
 */
object Spacing {
    val none = 0.dp
    val xxs = 4.dp
    val xs = 8.dp
    val s = 12.dp
    val m = 16.dp
    val l = 24.dp
    val xl = 32.dp
    val xxl = 48.dp
    val maximize = 96.dp // Bottom padding for scroll awareness
}

/**
 * Standard corner radii for DailyFocus.
 * "Soft" and "Touch-friendly".
 */
object Radius {
    val small = 8.dp
    val medium = 12.dp
    val large = 16.dp
    val full = 100.dp
}

/**
 * Elevation levels for tonal surfaces.
 */
object Elevation {
    val Level0 = 0.dp   // Background
    val Level1 = 1.dp   // List Item / Card
    val Level2 = 3.dp   // Floating / Active
    val Level3 = 6.dp   // Dialog
    val Level4 = 8.dp   // Modal
    val Level5 = 12.dp  // Maximum
}

/**
 * Standard shapes using the radius tokens.
 */
object AppShapes {
    val Small = androidx.compose.foundation.shape.RoundedCornerShape(Radius.small)
    val Medium = androidx.compose.foundation.shape.RoundedCornerShape(Radius.medium)
    val Large = androidx.compose.foundation.shape.RoundedCornerShape(Radius.large)
    val ExtraLarge = androidx.compose.foundation.shape.RoundedCornerShape(Radius.full)
}
