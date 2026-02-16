package com.dailyfocus.core.ui.theme

import androidx.compose.ui.graphics.Color

// Primary - Deep Indigo (Calm, Professional)
val Primary80 = Color(0xFFBAC3FF)
val Primary40 = Color(0xFF3F51B5)
val Primary30 = Color(0xFF283896)

// Secondary - Teal (Actionable, Clear)
val Secondary80 = Color(0xFF8CF3E6)
val Secondary40 = Color(0xFF006A61)

// Tertiary - Warm Accent (Habits/Streaks)
val Tertiary80 = Color(0xFFFFB690)
val Tertiary40 = Color(0xFF8D4F2B)

// Tonal Surfaces (DailyFocus Depth System)
// Replaces pure black with layered dark greys.
val Surface1 = Color(0xFF161618) // App Background (Deepest)
val Surface2 = Color(0xFF1C1C1E) // List Background
val Surface3 = Color(0xFF252528) // Card / Task Item
val Surface4 = Color(0xFF2C2C2E) // Elevated Card
val Surface5 = Color(0xFF3A3A3C) // Modal / Menu

// Light Mode Surfaces (Soft Off-Whites)
val LightSurface1 = Color(0xFFF2F2F7) // App Background
val LightSurface2 = Color(0xFFFFFFFF) // Card Background
val LightSurface3 = Color(0xFFFFFFFF) // Elevated

// Text / Content
val Neutral10 = Color(0xFF191C22)
val Neutral90 = Color(0xFFE0E2EC)
val Neutral99 = Color(0xFFFDFCFF)

// Semantic
val Error80 = Color(0xFFFFB4AB)
val Error40 = Color(0xFFBA1A1A)

// Gradients (Subtle bottom fade)
val GradientFadeStart = Color(0x00161618)
val GradientFadeEnd = Surface1

// Streak Colors (TickTick inspired)
val StreakFlame = Color(0xFFFF7043)
val StreakRingSuccess = Color(0xFF26A69A)
val StreakRingMissed = Color(0xFFFFB4AB)
val StreakRingEmpty = Color(0xFF46464F)

// Missing tokens
val GradientHeaderStart = Color(0xCC161618) // Slightly transparent start
val GradientHeaderEnd = Color(0x00161618)
val StreakHot = Color(0xFFFF5722) // Hotter flame

// Heatmap
val HeatmapDone = StreakRingSuccess
val HeatmapMissed = StreakRingMissed
val HeatmapEmpty = StreakRingEmpty
val HeatmapToday = Primary80

// Premium Heatmap Colors (Soft)
val HeatmapDoneSoft = Color(0xD926A69A) // 85% opacity
val HeatmapMissedSoft = Color(0x66FFB4AB) // 40% opacity

// Momentum Card Gradient
val MomentumGradientStart = Surface2
val MomentumGradientEnd = Surface3

// Summary Card Gradient
val GradientSummaryStart = Color(0xFF3F51B5) // Primary40
val GradientSummaryEnd = Color(0xFF283896)   // Primary30
