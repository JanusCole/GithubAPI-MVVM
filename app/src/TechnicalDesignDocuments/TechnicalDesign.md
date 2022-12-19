# Github Organization Viewer

> An Android application which allows a user to enter an organization name and then displays the top 3 most popular (by stars) repositories on GitHub for that organization. A user should be able to click on one of the Repos and navigate to it within a webview or chrome custom tabs.

## API

This app uses the https://api.github.com/ api endpoint to retrieve Github organizations and repos. While it has a lower rate limit than the endpoint at https://developer.github.com/v3/, it doesn't require an API key. So you should be able to run it out of the box with no prior configuration.

## Programming Language
The app is written in Kotlin. In 2019, Google **[announced](https://developer.android.com/kotlin/first)** that Kotlin would be the preferred programming language for Android applications.

> Android development will become increasingly Kotlin-first. Many new Jetpack APIs and features will be offered first in Kotlin. If you’re starting a new project, you should write it in Kotlin; code written in Kotlin often mean much less code for you–less code to type, test, and maintain.

## Single Activity Architecture

Google introduced **[Jetpack Compose Navigation](https://developer.android.com/jetpack/compose/navigation)** as a replacement for the legacy `Intent` driven transitions between screens. This framework implements navigation through the use of a specialized `NavHost` that act as a controller for swappable @Composables. To this end, Google is moving away from the practice of structuring apps with multiple activities in favor of a single activity design hosting multiple @Composables.<br>

> Today we are introducing the Navigation component as a framework for structuring your in-app UI, with a focus on making a single-Activity app the preferred architecture.

### Model-View-ViewModel

As part of the Jetpack framework, Google introduced an architecture component called **[ViewModel](https://developer.android.com/topic/libraries/architecture/viewmodel)**. Previously. Google recommended the Model-View-Presenter pattern for app architecture. But that pattern had a number of problems related to the Android `Activity` lifecycle. This new component holds the data behind the UI and exposes it using the Observer Pattern. And this solves a number of problems with the old MVP pattern:

- Presenters were dependent on their Activities and so their data was lost when the Activity was rotated.
- Activities and Presenters had bidirectional dependencies. All of the dependencies in MVVM flow in one direction.

### UI Framework
The app currently uses **[Jetpack Compose](https://developer.android.com/jetpack/compose)**.

### UI State

The UI state is represented as plain classes that hold the UI state as per **[this](https://developer.android.com/topic/architecture/ui-layer/events#compose_2)** guidance from Google.

### Persistence
Saved search criteria are persisted using **[Protobuf Datastore](https://developer.android.com/codelabs/android-preferences-datastore#0)** as SharedPreferences is no longer the preferred approach.

> DataStore is a new and improved data storage solution aimed at replacing SharedPreferences. Built on Kotlin coroutines and Flow, DataStore provides two different implementations: Proto DataStore, that stores typed objects (backed by protocol buffers) and Preferences DataStore, that stores key-value pairs. Data is stored asynchronously, consistently, and transactionally, overcoming some of the drawbacks of SharedPreferences.

### Dependency Injection

The app uses dependency injection using **[Hilt](https://developer.android.com/jetpack/androidx/releases/hilt)**

## Automated Testing
The app employs both **Espresso** UI testing and **JUnit** unit testing.<br>

### Running tests

Type this command at the command line to run the unit tests suite.<br>

./gradlew createDebugCoverageReport<br>

Find the test results here: app/build/reports/androidTests/connected/index.html<br>
Find the coverage report here: app/build/reports/coverage/androidTest/debug/index.html<br>

or<br>

./gradlew createDebugUnitTestCoverageReport<br>

Find the test results here: app/build/reports/tests/testDebugUnitTest/index.html<br>
Find the coverage report here: app/build/reports/coverage/test/debug/index.html<br>