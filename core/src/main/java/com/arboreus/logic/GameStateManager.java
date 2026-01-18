package com.arboreus.logic;

public class GameStateManager {
    private int score;
    private int lives;
    private static final int POINTS_PER_SUCCESS = 100;

    private float speedMultiplier;

    public GameStateManager(int initialLives) {
        this.lives = initialLives;
        this.score = 0;
        this.speedMultiplier = 1.0f;
    }

    // --- Methods requested by Prompt ---

    public void registerSuccess() {
        incrementScore(POINTS_PER_SUCCESS);
        speedMultiplier += 0.05f; // Progression: 5% faster per success
    }

    public boolean registerMistake() {
        decrementLife();
        return isGameOver();
    }

    // --- Methods used by GameScreen.java ---

    public void incrementScore(int points) {
        this.score += points;
    }

    public void decrementLife() {
        if (lives > 0) {
            lives--;
        }
    }

    public boolean isGameOver() {
        return lives <= 0;
    }

    public boolean checkWinCondition() {
        return score >= 500;
    }

    public void reset(int initialLives) {
        this.lives = initialLives;
        this.score = 0;
        this.speedMultiplier = 1.0f;
    }

    public int getScore() {
        return score;
    }

    public float getSpeedMultiplier() {
        return speedMultiplier;
    }

    public int getLives() {
        return lives;
    }
}
