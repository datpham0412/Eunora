# Eunora — An adaptive mood reflection app

Eunora is a mood reflection app where the experience adapts to emotional intensity, not just emotional labels.

The app is built with Kotlin Multiplatform, sharing core logic and UI across Android and iOS.

## Demo

A short walkthrough demonstrating Eunora’s adaptive emotional flow across different mood inputs.

▶️ Watch the demo video:  
https://www.youtube.com/watch?v=_78pzntFU9I&t=19s


## Screenshots

| Adaptive Flow | iOS | Android |
|--------------|-----|---------|
| Positive / Calm mood | ![iOS calm](screenshots/ios_calm.png) | ![Android calm](screenshots/android_calm.png) |
| Intense / Overwhelmed mood | ![iOS intense](screenshots/ios_intense.png) | ![Android intense](screenshots/android_intense.png) |


## Problem Statement

Most mood-tracking apps focus on recording emotions, but treat all moods the same after they are logged.

In reality, different emotional states require different kinds of support — from slowing down intense reactions, to resting during low-energy moments, or preserving positive experiences.

Without emotional context, mood tracking remains passive and limited.


## Core Idea

Eunora adapts its experience based on both **emotional type and activation level**.

Instead of a single static flow, the app responds differently depending on how the user feels:

-   **Positive emotions** → preserve and reflect
    
-   **High-activation emotions** → pause and slow down
    
-   **Low-energy emotions** → rest or continue gently
    
-   **Neutral states** → minimal, uninterrupted flow
    

This adaptation is supported by gentle guidance, personal reflection, emotion-consistent visuals, and long-term emotional patterns.


## Why This Is Different

Most mood trackers focus on *what* is logged.  
Eunora focuses on *how interaction happens*.

By adapting pacing, tone, and flow to emotional state, Eunora becomes an emotionally responsive system — not just a mood log.

## User Flow Overview

**High-level flow (experience sequence)**

-   **Welcome & emotional context**  
    Users begin on a calm overview showing recent moods and patterns, providing emotional continuity before starting a new entry.
    
-   **Mood input**  
    Users express how they feel through free writing or a quick mood selection, without pressure to be precise or analytical.
    
-   **Emotional reflection sequence**  
    After submission, Eunora reflects the moment through a progressive sequence:
    
    -   mood vibe & quick overview
        
    -   mood explanation
        
    -   emotional spectrum
        
    -   personal reflection
        
    -   gentle guidance
        
    -   overall summary
        
-   **History & recall**  
    Each entry is saved as a complete emotional moment and can be revisited from history at any time.

## System Architecture

The project follows a **Kotlin Multiplatform (KMP)** architecture, maximizing code sharing between Android and iOS while maintaining platform-native integration where necessary.

### High-level Architecture

The codebase is structured into three main layers:

-   **Shared Domain (`shared`)**: Contains the core business logic, domain models, data repositories, and ViewModels. This layer is platform-agnostic (pure Kotlin) and drives the application state.
-   **Shared UI (`composeApp`)**: Implements the user interface using **Compose Multiplatform**. This allows for a single declarative UI definition that renders natively on both Android and iOS.
-   **Platform Specific**:
    -   **Android App**: A thin wrapper around the shared Compose App.
    -   **iOS App**: A thin wrapper that hosts the shared Compose View Controller.

### Kotlin Multiplatform Usage

-   **Shared Business Logic**: All application logic, including data fetching, AI analysis models, and local data persistence, resides in the `shared` module.
-   **Shared State Management**: The app uses `StateFlow` for reactive state management. `ViewModel` classes (e.g., `WelcomeViewModel`) in the `shared` module expose UI state to the Compose UI.
-   **ViewModel**: ViewModels are shared components located in the `shared` module, managing pure business logic and state, decoupled from the Android-specific ViewModel implementation where possible, or using multiplatform equivalents.
-   **Shared Compose UI**: The entire screen hierarchy and navigation are implemented in `composeApp/src/commonMain/kotlin` using Jetpack Compose, ensuring a consistent experience across platforms.