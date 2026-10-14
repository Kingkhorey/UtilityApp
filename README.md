# 🛠️ UtilityApp

**UtilityApp** is a modern Android utility application built with Jetpack Compose and designed around clean architecture principles. It provides quick access to daily features, live quotes, customizable settings, and a sleek user interface.

---

## 📱 Features & Highlights

- **Dynamic Quote Integration:** Fetches and displays daily motivational/tech quotes from local repositories or remote APIs.
- **Interactive UI Components:** Custom-built choice chips, dynamic greeting headers, and responsive layout sections.
- **Theme Customization:** Seamless light/dark backdrop theme switching handled via custom UI states.
- **Settings Management:** Dedicated user settings screen to toggle application preferences and view configuration details.
- **Modular Architecture:** Clean separation of concerns featuring dependency injection containers and repository patterns.

---

## 🏛️ Architecture & Tech Stack

This project follows **Android Architecture Guidelines** and modern declarative UI paradigms:

- **UI Framework:** [Jetpack Compose](https://developer.android.com/jetpack/compose) (Declarative UI)
- **Architecture Pattern:** MVVM (Model-View-ViewModel) with unidirectional data flow (UDF).
- **Dependency Management:** Custom lightweight `AppContainer` pattern for manual dependency injection.
- **Data Layer:** 
  - Repository pattern handling data sources.
  - Remote Data Transfer Objects (DTOs) and REST API service interfaces.
  - Local fallback and static quote providers.
- **Testing:** Comprehensive unit tests for repositories, view models, and UI components using JUnit.

---

## 📂 Project Structure

```text
app/src/main/java/au/edu/jcu/assessment/utilityapp/
│
├── data/
│   ├── local/          # Local data sources and static datasets
│   ├── model/          # Data models and DTOs (Quote, QuoteSource)
│   ├── remote/         # Retrofit/API interfaces and network DTOs
│   └── QuoteRepository.kt # Single source of truth for data handling
│
├── di/
│   └── AppContainer.kt # Dependency injection container
│
├── ui/
│   ├── components/     # Reusable UI widgets (ChoiceChips, Headers, etc.)
│   ├── theme/          # Custom theming, backdrops, and styling
│   ├── QuoteViewModel.kt # UI state management and business logic
│   ├── QuoteUiState.kt # Immutable UI state definitions
│   ├── SettingsScreen.kt # Application settings screen
│   └── UtilityApp.kt   # Main navigation/scaffold entry point
│
└── QuoteApplication.kt # Application-level initialization
