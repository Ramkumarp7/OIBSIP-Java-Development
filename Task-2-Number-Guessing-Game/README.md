# OASIS INFOBYTE — Task 2: Number Guessing Game

A Java Swing desktop game with a dark Cards-Against-Humanity-inspired table aesthetic.

## Requirements
- JDK 17 or newer
- IntelliJ IDEA (or any Java IDE)

## Run in IntelliJ IDEA
1. Extract the ZIP.
2. Open the extracted project folder in IntelliJ IDEA.
3. Set the project SDK to JDK 17 or newer.
4. Open `src/main/java/com/oasis/guessing/Main.java`.
5. Run `Main.main()`.

## Run on Windows
Double-click `run.bat` from the extracted project folder.

## Run on macOS / Linux
Run `./run.sh` from the project folder.

## Gameplay
- Choose Easy (1–50, 10 attempts), Medium (1–100, 7 attempts), or Hard (1–200, 5 attempts).
- Enter a whole number and press Enter or click **LOCK IN YOUR GUESS**.
- Receive Too Low / Too High feedback and a proximity temperature hint.
- The possible range narrows after each valid guess.
- Current-round guess chips show each previous guess and its direction.
- Win a round to earn points. Points = (attempts remaining after the winning guess + 1) × 100 × difficulty multiplier × streak multiplier.
- Difficulty multipliers: Easy 1×, Medium 2×, Hard 3×. Win-streak multiplier increases by 1× per consecutive win, capped at 5×. A loss resets the current streak.
- The table scoreboard tracks rounds, wins, win rate, current streak, and total points.
- Session statistics reset when the application is closed.

## Project structure
`src/main/java/com/oasis/guessing/` contains the game engine, difficulty model, round result model, and Swing interface.
