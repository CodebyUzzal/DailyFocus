# DailyFocus

A local-first, offline-capable daily execution and habit system for Android.

## Architecture
DailyFocus follows Clean Architecture principles with MVVM pattern.

- **Presentation**: Jetpack Compose, ViewModels (Hilt injected).
- **Domain**: UseCases, Models, Repository Interfaces.
- **Data**: Room Database, Repository Implementations, DataStore.

### Key Concepts
- **Routine (formerly Recurring Task)**: Operational tasks that must be done today (e.g., "Check emails"). These appear in the "Today" view and reset daily. They track streaks but do not have the same behavioral weight as Habits.
- **Habit**: Identity-based behaviors (e.g., "Read 30 mins"). These appear in the "Habits" tab and focus on consistency and long-term streaks.
- **One-off Task**: A single instance task for today only.

## Build & Signing
The project uses standard Android Gradle configuration.
Release builds require signing configuration via environment variables:
- `KEYSTORE_FILE`
- `KEYSTORE_PASSWORD`
- `KEY_ALIAS`
- `KEY_PASSWORD`

If these are not set, the build may fail or look for a local `keystore.jks`.

## Database
Uses Room with automatic migrations.
- Version 1: Initial schema
- Version 2: Added streak tracking to Routines (`currentStreak`, `longestStreak`, `lastCompletedDate`)

## License
Private / Proprietary
