package com.arboreus.logic;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class GameStateManagerTest {

    private GameStateManager gameStateManager;

    @BeforeEach
    public void setUp() {
        gameStateManager = new GameStateManager(3);
    }

    @Test
    public void testInitialState() {
        assertEquals(3, gameStateManager.getLives(), "Initial lives should be 3");
        assertEquals(0, gameStateManager.getScore(), "Initial score should be 0");
        assertEquals(1.0f, gameStateManager.getSpeedMultiplier(), 0.001, "Initial speed multiplier should be 1.0");
        assertFalse(gameStateManager.isGameOver(), "Game should not be over initially");
    }

    @Test
    public void testRegisterSuccess() {
        gameStateManager.registerSuccess();
        assertEquals(100, gameStateManager.getScore(), "Score should increase by 100");
        assertEquals(1.05f, gameStateManager.getSpeedMultiplier(), 0.001, "Speed multiplier should increase by 0.05");
    }

    @Test
    public void testIncrementScoreCustom() {
        gameStateManager.incrementScore(50);
        assertEquals(50, gameStateManager.getScore());
        gameStateManager.incrementScore(20);
        assertEquals(70, gameStateManager.getScore());
    }

    @Test
    public void testRegisterMistake() {
        boolean isGameOver = gameStateManager.registerMistake();
        assertEquals(2, gameStateManager.getLives(), "Lives should decrement");
        assertFalse(isGameOver, "Game should not be over with 2 lives");

        gameStateManager.registerMistake(); // 1 life
        isGameOver = gameStateManager.registerMistake(); // 0 lives

        assertEquals(0, gameStateManager.getLives());
        assertTrue(isGameOver, "Game should be over when lives reach 0");
        assertTrue(gameStateManager.isGameOver());
    }

    @Test
    public void testReset() {
        gameStateManager.registerSuccess();
        gameStateManager.registerMistake();

        gameStateManager.reset(5);

        assertEquals(5, gameStateManager.getLives());
        assertEquals(0, gameStateManager.getScore());
        assertEquals(1.0f, gameStateManager.getSpeedMultiplier(), 0.001);
        assertFalse(gameStateManager.isGameOver());
    }
}
