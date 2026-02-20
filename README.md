# Eventorias 🎉

An Android event management application built with modern Android development practices, featuring Firebase authentication, real-time data synchronization, and a clean architecture approach.

## 📱 Features

- **User Authentication**
  - Google Sign-In integration
  - Email/Password authentication
  - User profile management with photo upload
  - Persistent authentication state

- **Event Management**
  - Browse and discover events
  - Create new events with details
  - Real-time event updates
  - Event filtering and search

- **User Profile**
  - Update display name with debounced API calls
  - Upload and manage profile photos
  - Toggle notification preferences
  - View and edit user information

## 🏗️ Architecture

This project follows **Clean Architecture** principles with a multi-module structure:

```
Eventorias/
├── app/                    # Main application module
├── core-kt/               # Core Kotlin utilities and extensions
├── core-ui/               # Shared UI components and resources
├── data/                  # Data layer (repositories, data sources)
├── domain/                # Domain layer (use cases, models)
└── gradle/                # Gradle configuration
```

### Architecture Layers

- **Presentation Layer** (`app` module)
  - MVVM pattern with ViewModels
  - Jetpack Compose UI
  - StateFlow for reactive state management

- **Domain Layer** (`domain` module)
  - Business logic and use cases
  - Domain models
  - Repository interfaces

- **Data Layer** (`data` module)
  - Repository implementations
  - Firebase integration
  - Local data sources

## 🛠️ Tech Stack

### Core Technologies
- **Language:** Kotlin 100%
- **UI Framework:** Jetpack Compose
- **Minimum SDK:** 26 (Android 8.0)
- **Target SDK:** 35 (Android 15)

### Libraries & Frameworks

#### Android Jetpack
- **Lifecycle & ViewModel** - Lifecycle-aware components
- **Navigation Compose** - Type-safe navigation
- **Material3** - Modern Material Design components

#### Dependency Injection
- **Koin** - Lightweight dependency injection

#### Networking & Backend
- **Firebase Authentication** - User authentication
- **Firebase Firestore** - Real-time database
- **Firebase Storage** - File storage for profile photos
- **Retrofit** - Type-safe HTTP client
- **OkHttp** - HTTP client with logging interceptor

#### Image Loading
- **Coil** - Image loading for Compose

#### Asynchronous Programming
- **Kotlin Coroutines** - Asynchronous programming
- **Flow** - Reactive streams

#### Testing
- **JUnit 4** - Unit testing framework
- **MockK** - Mocking library for Kotlin
- **Turbine** - Flow testing library
- **Robolectric** - Android unit testing

#### Code Quality
- **Kotlin Serialization** - JSON serialization
- **Detekt** - Static code analysis

## 🚀 Getting Started

### Prerequisites

- Android Studio Ladybug or newer
- JDK 11 or higher
- Android SDK with API 35
- Firebase account for backend services

### Firebase Setup

1. Create a new Firebase project at [Firebase Console](https://console.firebase.google.com/)
2. Enable the following services:
   - Authentication (Google Sign-In and Email/Password)
   - Cloud Firestore
   - Cloud Storage
3. Download `google-services.json` and place it in the `app/` directory
4. Update the package name to match your application ID

### Installation

1. Clone the repository:
```bash
git clone https://github.com/MGAxiom/Eventorias-OC-15.git
cd Eventorias-OC-15
```

2. Open the project in Android Studio

3. Sync Gradle and build the project

4. Run the app on an emulator or physical device
