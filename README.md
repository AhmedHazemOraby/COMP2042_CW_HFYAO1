# CourseworkGame

## Compilation Instructions
- **Step 1:** Ensure that all Java classes (Main, Game Rules, Initialization Board, Initialize, Physics, Updater, Game Engine, Block, Score, and Bonus) are in the same file or package.
- **Step 2:** The application requires JavaFX to run. Ensure that JavaFX is correctly installed and configured in your development environment.
- **Step 3:** Compile and run the code from the `Main` class.

## Implemented and Working Properly
- **Endless Mode:** Functions properly as expected, offering continuous gameplay.
- **Game Visuals:** Themed after the "Pokemon" cartoon, enhancing the overall user experience.
- **Main Menu:** Allows players to choose between "Starting a New Game" in normal mode and "Endless Mode."

## Implemented but Not Working Properly
- **Collision Detection System:** Implemented, but sometimes the collision detection is inconsistent.

## Features Not Implemented
- **Save and Load Game:** These features have been removed as they are not required in this implementation.

## New Java Classes
- **Game Rules Class:** Manages user input, movement, collision flags, game restarts, and level progression.
- **Initialization Board Class:** Responsible for initializing the ball, paddle, and blocks.
- **Initialize Class:** Initializes different game scenes, including the menu and the game itself.
- **Physics Class:** Handles game physics, collision detection, ball physics updates, bonus handling, and block destruction checks.
- **Updater Class:** Updates the game state, level progression, score, and positions of game elements.

## Modified Java Classes
- **Game Engine Class:** Optimized for better performance.
- **Block Class:** Modified to handle different types of blocks and include the collision detection system.
- **Score Class:** Changed the color of pop-ups and added a timer for display duration.
- **Bonus Class:** Modified to change the icons of the bonuses.
- **Main Class:** Updated to include all new and modified classes.

## Unexpected Problems
1. **Game Engine Crashes:** Addressed frequent crashes with try-catch blocks, reducing but not entirely eliminating the issue.
2. **Endless Mode Implementation:** Initially impacted the normal mode. Resolved by treating each mode separately in the code.