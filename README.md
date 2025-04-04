# Currency Convert

This is a sample project which demos a good Android app architecture using Kotlin flows and Compose. It also have view implementation. Use `MainActivity.USE_COMPOSE` to switch between compose and view. Note that the view implementation shows only the conversions.

## Architecture components

* Unidirectional flow: The data flows using `Flow` and `StateFlow` from database and network to view model through repository 
* MVVM: MVVM pattern is applied. There are different data models for conversions.
* Hilt Dependency Injection: Dependency injection is done using Hilt so that unit testing various components is easy
* Room database: For local storage so that data is available at cold starts.
* Compose: UI is built using Compose with different components for portrait and landscape modes.
* SharedPreference: Current currency is stored in `SharedPreferences`. It can be stored in database too but using this approach it is demonstrated on how to use multiple data sources in repositories. 
* WorkManager: A worker is scheduled for repeated refresh of data even when the app is not running.

## Unit testing

1. `MainViewModel` is unit tested (more to come..)

## Screenshot

### Portrait with Light theme

<img src="resources/portrait.gif" width=540 height=1200 alt="Portrait Screenshot">

### Landscape with Dark theme

<img src="resources/landscape.gif" width=1000 alt="Landscape Screenshot">
