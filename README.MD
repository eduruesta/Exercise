# Xercise - Compose Multiplatform Application

A comprehensive exercise application built with Kotlin Multiplatform and Compose Multiplatform, providing access to a vast database of exercises organized by muscle groups and body parts.

## 🏋️‍♂️ Features

- **Exercise Database**: Access to thousands of exercises from ExerciseDB API
- **Muscle Group Organization**: Browse exercises by specific body parts and muscle groups
- **Detailed Exercise Information**: View exercise types, required equipment, target muscles, and secondary muscles
- **Cross-Platform**: Runs on Android, iOS, and Desktop
- **Modern UI**: Built with Material 3 design system
- **Clean Architecture**: MVVM pattern with Repository and Use Case layers
- **Dependency Injection**: Using Koin for dependency management

## 🎯 Supported Body Parts

- Waist, Quadriceps, Thighs, Back, Triceps, Upper Arms
- Calves, Biceps, Chest, Shoulders, Forearms, Glutes
- Hamstrings, Lats, Lower Back, Middle Back, Neck, Traps
- Abdominals, Core

## 🛠️ Tech Stack

- **Kotlin Multiplatform**: Shared business logic across platforms
- **Compose Multiplatform**: Modern declarative UI framework
- **Ktor**: HTTP client for API communication
- **Koin**: Dependency injection framework
- **Kotlinx Serialization**: JSON serialization/deserialization
- **Coroutines**: Asynchronous programming
- **Material 3**: Modern design system

## 🚀 API Integration

The app integrates with ExerciseDB API (RapidAPI) to fetch exercise data:
- **Base URL**: `https://exercisedb-api1.p.rapidapi.com/api/v1`
- **Endpoint**: `/exercises`
- **Features**: Filtering by name, keywords, target muscles, body parts, equipment, and exercise type
- **Pagination**: Support for forward and backward pagination

### API Parameters
- `name`: Filter exercises by name (fuzzy matching)
- `keywords`: Filter by keywords (comma-separated)
- `targetMuscles`: Filter by primary target muscles
- `secondaryMuscles`: Filter by secondary muscles
- `exerciseType`: Filter by type (STRENGTH, CARDIO, etc.)
- `bodyParts`: Filter by body parts
- `equipments`: Filter by required equipment
- `limit`: Maximum results (1-25, default: 10)
- `after`/`before`: Pagination cursors

## 📱 Screenshots & Usage

1. **Muscle Group Selection**: Browse available muscle groups with exercise counts
2. **Exercise List**: View exercises for selected muscle groups
3. **Exercise Details**: See comprehensive information about each exercise

## 🔧 Setup & Installation

### Prerequisites
- Check your system with [KDoctor](https://github.com/Kotlin/kdoctor)
- Install JDK 17 or higher on your machine
- Add `local.properties` file to the project root and set a path to Android SDK there

### API Key Configuration
⚠️ **Important**: This app requires an API key from RapidAPI to access the ExerciseDB API.

1. **Get an API Key**:
   - Visit [ExerciseDB API on RapidAPI](https://rapidapi.com/justin-WFnsXH_t6/api/exercisedb)
   - Subscribe to the API (free tier available)
   - Copy your API key from the dashboard

2. **Configure the API Key** (Choose one method):

   **Method 1: Local Properties (Recommended for development)**
   - Add your API key to `local.properties` file:
   ```properties
   rapidapi.key=YOUR_API_KEY_HERE
   ```

   **Method 2: Environment Variables (Recommended for production)**
   - Set environment variable: `RAPIDAPI_KEY=YOUR_API_KEY_HERE`

   **Method 3: Temporary Testing**
   - For quick testing, you can temporarily replace the API key in `ExerciseApiService.kt`
   - ⚠️ **Never commit API keys to version control!**

3. **Update the Code** (if using Method 1 or 2):
   - Modify `ExerciseApiService.kt` to read from your chosen configuration method
   - The current implementation has a placeholder API key for demonstration purposes

### Android
To run the application on android device/emulator:
- Open project in Android Studio and run imported android run configuration

To build the application bundle:
- Run `./gradlew :composeApp:assembleDebug`
- Find `.apk` file in `composeApp/build/outputs/apk/debug/composeApp-debug.apk`

Run android UI tests: `./gradlew :composeApp:connectedDebugAndroidTest`

### Desktop
- Run the desktop application: `./gradlew :composeApp:run`
- Run the desktop **hot reload** application: `./gradlew :composeApp:hotRunJvm`
- Run desktop UI tests: `./gradlew :composeApp:jvmTest`

### iOS
To run the application on iPhone device/simulator:
- Open `iosApp/iosApp.xcproject` in Xcode and run standard configuration
- Or use [Kotlin Multiplatform Mobile plugin](https://plugins.jetbrains.com/plugin/14936-kotlin-multiplatform-mobile) for Android Studio

Run iOS simulator UI tests: `./gradlew :composeApp:iosSimulatorArm64Test`

## 🏗️ Project Structure

```
composeApp/
├── src/
│   ├── commonMain/kotlin/org/dev/exercises/
│   │   ├── data/
│   │   │   ├── remote/ExerciseApiService.kt
│   │   │   └── repository/ExerciseRepositoryImpl.kt
│   │   ├── domain/
│   │   │   ├── model/Exercise.kt
│   │   │   ├── repository/ExerciseRepository.kt
│   │   │   └── usecase/GetExercisesGroupedByMuscleUseCase.kt
│   │   ├── presentation/
│   │   │   ├── screen/
│   │   │   │   ├── ExercisesScreen.kt
│   │   │   │   └── MuscleListScreen.kt
│   │   │   └── viewmodel/ExerciseViewModel.kt
│   │   ├── di/AppModule.kt
│   │   └── App.kt
│   ├── androidMain/
│   ├── iosMain/
│   └── jvmMain/
```

## 🔐 Security

- API keys and sensitive information are excluded from version control via `.gitignore`
- Environment-specific configurations should be stored in `local.properties`

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Add tests if applicable
5. Submit a pull request

## 📄 License

This project is licensed under the MIT License - see the LICENSE file for details.

## 🙏 Acknowledgments

- [ExerciseDB API](https://rapidapi.com/justin-WFnsXH_t6/api/exercisedb) for providing comprehensive exercise data
- [Kotlin Multiplatform](https://kotlinlang.org/docs/multiplatform.html) team for the amazing framework
- [Compose Multiplatform](https://www.jetbrains.com/lp/compose-multiplatform/) team for the UI toolkit
