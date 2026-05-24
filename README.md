# Contexta

A personal intelligence and productivity assistant for Android that helps you manage priorities, tasks, and context through a clean, voice-ready interface.

---

## Features

- **Authentication** — Email/password sign-in and Google OAuth via Supabase
- **Strategy Screen** — Daily priority dashboard with a live clock and time-aware greeting
- **Quick Actions** — One-tap access to Voice, Note, Task, and Queue inputs
- **Execution Queue** — Track and manage ongoing tasks
- **Context Capture** — Quickly log information as context
- **Session Feedback** — Record session-level reflections
- **Theme Preferences** — Light, Dark, or System theme saved to DataStore
- **Profile Management** — View and manage your user profile
- **Smooth Transitions** — Custom circular reveal animation on logout

---

## Tech Stack

| Layer | Technology |
|-------|-----------|
| Language | Kotlin 2.2.10 |
| UI | Jetpack Compose + Material Design 3 |
| Navigation | Jetpack Navigation Compose |
| Backend | Supabase (Auth, PostgREST, Realtime) |
| HTTP Client | Ktor (OkHttp engine) |
| DI | Dagger Hilt |
| State | StateFlow + MVVM |
| Storage | DataStore Preferences |
| Serialization | Kotlinx Serialization |
| Logging | Timber |
| Min SDK | 24 |
| Target SDK | 36 |

---

## Architecture

The app follows **MVVM** with a **Repository pattern**, organized by feature.

```
app/src/main/java/com/example/contexta/
├── auth/               # Sign-in, sign-up, session state, validation
├── home/               # Strategy screen, ViewModel, UI state
├── execution/          # Execution queue screen
├── context/            # Context capture screen
├── feedback/           # Session feedback screen
├── moreoptions/        # Settings, theme, logout
├── navigation/         # App-level + auth NavGraphs
├── data/               # Theme and profile local storage
├── di/                 # Hilt modules
├── core/network/       # Network configuration
└── ui/                 # Shared composables, theme, transitions
```

- **ViewModels** expose UI state via `StateFlow` and navigation events via `Channel`
- **Repositories** abstract data access (Supabase, DataStore, local storage)
- **Hilt** wires everything together via `@HiltViewModel` and `@HiltAndroidApp`
- **Navigation** uses nested NavGraphs: an auth graph and a main app graph with bottom tabs

---

## Getting Started

### Prerequisites

- Android Studio Hedgehog or newer
- JDK 17
- A [Supabase](https://supabase.com) project with email/password auth and Google OAuth enabled

### Setup

1. Clone the repository:
   ```bash
   git clone https://github.com/SamruddhaChavan007/Contexta.git
   cd Contexta
   ```

2. Create a `local.properties` file (if not already present) and add your Supabase credentials:
   ```properties
   SUPABASE_URL=https://your-project.supabase.co
   SUPABASE_ANON_KEY=your-anon-key
   ```

3. Configure the Google OAuth redirect in your Supabase dashboard to use:
   ```
   com.example.contexta://callback
   ```

4. Open the project in Android Studio, sync Gradle, and run on a device or emulator (API 24+).

---

## Screenshots

> Coming soon

---

## License

This project is for personal/educational use. License TBD.
