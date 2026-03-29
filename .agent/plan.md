# Project Plan

SlidePuzzle: A slide puzzle game with various grid sizes (3x3, 4x4, 5x5, custom up to 10x10). Features include predefined images, user-uploaded images, timer, move counter, random mode, cozy layout with smooth animations, music (home and game screens), SFX, settings for audio control, and player profiles. Refinements include a mini preview image, integrated audio assets, and an animated "blurred beams" background.


## Project Brief: SlidePuzzle

SlidePuzzle is a modern, immersive take on the classic sliding tile puzzle, featuring high customization, dynamic visuals, and a rich audio
 experience.

## Features
*   **Dynamic Grid Engine**: Supports grid sizes from 3x3 up to 10x10 with smooth, physics-based tile animations and a real-time mini-preview of the solved state for reference.
*   **Personalized Puzzles**: Allows players to use high
-quality predefined images or upload their own photos via gallery/camera to create custom challenges.
*   **"Alive" Visual Design**: Features a cozy Material 3 layout with an animated "blurred beams" flowing background and full edge-to-edge display support.
*   **Immersive Audio System**: Context
-aware background music for home and game screens, paired with responsive SFX for tile movements and victory states.
*   **Player Profiles & Tracking**: Local tracking of move counts, solving times, and user preferences stored within persistent player profiles.

## High-Level Technical Stack
*   **Language**: Kotlin
*   
**UI Framework**: Jetpack Compose (Material 3)
*   **Architecture**: MVVM (Model-View-ViewModel)
*   **Concurrency**: Kotlin Coroutines & Flow for game logic and reactive UI updates.
*   **Image Handling**: Coil for efficient loading and transformation of internal and user-sourced images.

*   **Code Generation**: **KSP (Kotlin Symbol Processing)** for Room and Moshi.
*   **Persistence**: Room Database for managing player profiles and high scores.
*   **Audio**: Jetpack Media3 for high-performance background music and SFX playback.

## Implementation Steps

### Task_1_Foundation_Logic: Initialize Room database for player profiles and high scores. Implement the core sliding puzzle logic including grid management (3x3 to 10x10), shuffling algorithm (ensuring solvability), move validation, and tile-splitting logic. Add Media3 dependencies for audio support.
- **Status:** COMPLETED
- **Updates:** Initialized Room database for player profiles and high scores. Implemented the core sliding puzzle logic including grid management (3x3 to 10x10), shuffling algorithm (ensuring solvability), move validation, and tile-splitting logic. Added Media3 dependencies for audio support.
- **Acceptance Criteria:**
  - Room database is correctly initialized
  - Puzzle engine handles grid generation and move validation
  - Image splitting logic correctly partitions images into tiles
  - Project builds successfully with new dependencies

### Task_2_Navigation_Settings_Audio: Set up Jetpack Compose Navigation. Implement the Material 3 theme with a vibrant, energetic color scheme and Edge-to-Edge support. Create the Home, Player Profile, and Settings screens. Integrate Media3 for background music (BGM) and sound effects (SFX) with persistent user controls.
- **Status:** COMPLETED
- **Updates:** Set up Jetpack Compose Navigation. Implement the Material 3 theme with a vibrant, energetic color scheme and Edge-to-Edge support. Create the Home, Player Profile, and Settings screens. Integrate Media3 for background music (BGM) and sound effects (SFX) with persistent user controls.
- **Acceptance Criteria:**
  - Navigation between Home, Profile, and Settings works
  - Vibrant M3 theme and Edge-to-Edge are applied
  - Audio (BGM/SFX) plays correctly and responds to Settings toggles
  - App does not crash during navigation or audio playback

### Task_3_Game_Experience: Develop the main Game screen. Implement the dynamic tile grid with smooth, physics-based animations. Integrate the real-time timer and move counter. Connect the puzzle engine to the UI to handle user interactions and detect win states.
- **Status:** COMPLETED
- **Updates:** Develop the main Game screen. Implement the dynamic tile grid with smooth, physics-based animations. Integrate the real-time timer and move counter. Connect the puzzle engine to the UI to handle user interactions and detect win states.
- **Acceptance Criteria:**
  - Game screen displays interactive grids for various sizes
  - Tiles animate smoothly when moved
  - Timer and move counter update accurately
  - Win condition is detected and handled with UI feedback

### Task_4_Image_Integration_Random: Implement image selection from a predefined library using Coil. Add support for user-uploaded images via Gallery and Camera permissions. Implement 'Random Mode' for unpredictable gameplay sessions.
- **Status:** COMPLETED
- **Updates:** Implement image selection from a predefined library using Coil. Add support for user-uploaded images via Gallery and Camera permissions. Implement 'Random Mode' for unpredictable gameplay sessions.
- **Acceptance Criteria:**
  - Predefined image library is accessible
  - User can successfully start a puzzle with a Gallery or Camera photo
  - Random Mode generates valid puzzles with random grids/images
  - All existing tests pass

### Task_5_Polish_Icon_Verify: Perform final UI polish to ensure a 'strong Android and Material design aesthetic'. Create an adaptive app icon matching the game theme. Conduct a final 'Run and Verify' to ensure application stability, performance, and alignment with all project requirements.
- **Status:** COMPLETED
- **Updates:** Performed final UI polish and adaptive icon implementation.
- **Acceptance Criteria:**
  - Adaptive app icon is implemented and visible
  - Vibrant theme is consistent across all screens
  - App is stable and crash-free
  - Final build pass and requirements are fully met

### Task_6_Alive_UI_Refinements: Implement the 'Alive' visual design refinements. Develop the 'blurred beams' animated background and add the mini-preview image of the solved state to the Game screen. Adjust the layout to achieve a 'cozy' Material 3 aesthetic.
- **Status:** COMPLETED
- **Updates:** Implemented the 'Alive' visual design refinements. Develop the 'blurred beams' animated background and add the mini-preview image of the solved state to the Game screen. Adjust the layout to achieve a 'cozy' Material 3 aesthetic.
- **Acceptance Criteria:**
  - Blurred beams background is animated and visible
  - Mini-preview image correctly shows the target solved state
  - UI follows the updated 'cozy' Material 3 design aesthetic
  - Project builds successfully

### Task_7_Audio_Integration_Final_Verify: Integrate final audio assets for context-aware background music (Home vs. Game screens) and responsive sound effects. Perform a final 'Run and Verify' to ensure stability, performance, and alignment with the refined project brief.
- **Status:** COMPLETED
- **Updates:** Integrated final audio assets for context-aware background music (Home vs. Game screens) and responsive sound effects. Perform a final 'Run and Verify' to ensure stability, performance, and alignment with the refined project brief.
Context-aware BGM transitions correctly between Home and Game screens.
High-quality SFX are integrated for movements and victory.
Build pass, all tests pass, and app does not crash.
Critic_agent confirms alignment with all user requirements.
Verified the implementation with a successful clean build and unit tests for the PuzzleEngine logic (4 passed).
The app is confirmed to be stable, crash-free, and fully aligned with all project requirements.
- **Acceptance Criteria:**
  - Context-aware BGM transitions correctly between Home and Game screens
  - High-quality SFX are integrated for movements and victory
  - Build pass, all tests pass, and app does not crash
  - Critic_agent confirms alignment with all user requirements
- **Duration:** N/A

