# Mood Painter – AI Emotional Art Journal
*A Kotlin Multiplatform app that transforms your daily emotions into abstract artwork, reflective journaling, and personalized advice.*

## Overview
Mood Painter is a cross-platform emotional diary that uses AI to convert your daily mood into:
- A reflective micro-journal  
- Personalized well-being suggestions  
- A generated abstract painting representing your emotional state  

The project is built specifically for the Kotlin Student Coding Competition 2025 and demonstrates how Kotlin Multiplatform can deliver a unified emotional experience across Android and Desktop using shared code.

---

## Tech Stack
- **Kotlin Multiplatform**
- **Compose Multiplatform UI**
- **Ktor client (shared)**
- **Gemini Flash (text)**  
- **Gemini Image Generation (image)**  
- **kotlinx.serialization**
- **SQLDelight / JSON storage** (TBD)

---

## Project Structure
```

root  
├─ shared/ # shared business logic  
│ ├─ commonMain/  
│ ├─ androidMain/  
│ ├─ desktopMain/  
│ └─ build.gradle.kts  
├─ androidApp/  
└─ desktopApp/

```

## How to Run

### Android
1. Open project in Android Studio  
2. Select `androidApp`  
3. Run on emulator or device  

### Desktop
```

./gradlew :desktopApp:run

```
---

## AI Usage

### 1. Generate Journal + Advice
- Gemini Flash 1.5  
- Input: mood text  
- Output:
  - 3–5 sentence reflective journal  
  - short actionable suggestion  
  - abstract art direction  

### 2. Generate Image
- Gemini Image Model  
- Uses the art direction generated above

---

## Screencast (to be added)
A 3–5 minute demonstration of the main features.

---

## License
MIT License (see `LICENSE` file)

---

## Author
Tien Dat Pham  
Swinburne University of Technology  
2025
