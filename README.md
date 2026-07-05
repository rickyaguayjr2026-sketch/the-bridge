# The Bridge

Christian/spiritual app that acts as a bridge for the user, a bridge to your local church, build your sanctuary with everything saved on device locally.

The Bridge is a Kotlin + Jetpack Compose Android app with an offline-first architecture.

## Current foundation

This repository currently implements only the local encrypted Room database foundation:

- device-local Room database with SQLCipher encryption
- onboarding profile storage for the selected user mode (Follower, Caregiver, Abide)
- separate persistence boundaries for **The Word** and **The Conversation**
- local event logging and WordForge decision storage for future filtering workflows
- no runtime network dependencies

## Build

```bash
./gradlew assembleDebug
```

## Verify

```bash
./gradlew testDebugUnitTest
```
