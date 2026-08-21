# Mini Space Invaders

A classic, fast-paced 2D arcade shooter built entirely in Java (Swing/AWT). Defend your spaceship against relentless waves of alien invaders, collect power-ups, and secure your place on the Top 10 Leaderboard.

## Features

- **20 Dynamic Waves:** The game features 20 levels of increasing difficulty. The number of enemies scales up with each wave, creating massive, challenging swarms in the late game.
- **Smooth & Responsive Controls:** Built with continuous key-state tracking for fluid movement and shooting simultaneously, eliminating default OS input delays.
- **Bullet Cooldown System:** Prevents "bullet spamming" by implementing a precise cooldown between shots, keeping the gameplay balanced.
- **Classic Retro Movement:** Enemies move uniformly across a wide horizontal range before aggressively dropping down towards the player.
- **Local Leaderboard:** A fully functional High Score system that saves the Top 10 players to a local `.txt` file, complete with interactive Main and End-game menus.

## Enemy Types

The alien swarm consists of three distinct types of enemies, with stronger variants appearing more frequently as you progress through the waves:
1. **Basic Enemy:** Standard speed, 1 hit point, awards 10 points.
2. **Fast Enemy (Orange/Red):** Moves horizontally at a faster pace, making it harder to hit. 1 hit point, awards 20 points.
3. **Strong Enemy (Purple):** Heavy armor requiring 3 hits to destroy. Awards 30 points.

## Power-Ups

Starting from Wave 7, the game introduces dropping power-ups to help you survive the growing swarms:
- **Shield (Cyan 'S'):** Grants a glowing aura and complete invincibility for 5 seconds. Absorbs all enemy fire and collisions without losing a life.
- **Extra Life (Pink '+'):** Drops dynamically every 3 waves to grant the player an additional life.

## Code Spotlight: Smooth Movement System

To avoid the default keyboard delay caused by the operating system, the game implements a boolean flag system inside the Game Loop. This allows for seamless, continuous movement and shooting simultaneously:

```java
// Inside GamePanel.java
@Override
public void keyPressed(KeyEvent e) {
    int key = e.getKeyCode();
    if (key == KeyEvent.VK_LEFT) leftPressed = true;
    else if (key == KeyEvent.VK_RIGHT) rightPressed = true;
    else if (key == KeyEvent.VK_SPACE) spacePressed = true;
}

// Inside the Game Timer (runs every 30ms)
if (leftPressed) gameManager.movePlayerLeft();
if (rightPressed) gameManager.movePlayerRight();
if (spacePressed) gameManager.playerShoot();
```

## How to Play

- **Left / Right Arrows:** Move the spaceship left and right.
- **Spacebar:** Shoot lasers. You can hold the key for continuous fire, which is regulated by the cooldown system.
- **Objective:** Destroy all enemies to advance to the next wave. 
- **Defeat Conditions:** The game is over if your lives drop to zero, or if any enemy reaches the bottom of the screen.

## How to Run Locally

1. Clone this repository to your local machine:
   ```bash
   git clone https://github.com/YourUsername/Mini-Space-Invaders.git
   ```
2. Open the project in Eclipse (or any other Java IDE).
3. Ensure the files are under a package named `game`.
4. Locate `Main.java`, right-click on it, and select **Run As > Java Application**.

## Screenshots

<p align="center">
  <img src="Screenshot%202026-08-21%20at%2015.30.34.png" width="45%" alt="Wave 16 Gameplay with Shield">
  &nbsp; &nbsp;
  <img src="Screenshot%202026-08-21%20at%2015.31.02.png" width="45%" alt="Wave 12 Gameplay with Life Powerup">
</p>
<br>
