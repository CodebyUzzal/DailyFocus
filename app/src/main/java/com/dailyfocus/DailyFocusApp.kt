package com.dailyfocus

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

/**
 * Application class for DailyFocus.
 * Annotated with [HiltAndroidApp] to trigger Hilt's code generation
 * and serve as the application-level dependency container.
 */
@HiltAndroidApp
class DailyFocusApp : Application()
