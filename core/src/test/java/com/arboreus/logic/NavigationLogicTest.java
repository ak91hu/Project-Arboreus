package com.arboreus.logic;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class NavigationLogicTest {

    @Test
    public void testShouldGoLeft() {
        BinaryTree tree = new BinaryTree();
        // shouldGoLeft(val, target) -> true if val < target
        assertTrue(tree.shouldGoLeft(30, 50), "30 < 50, should go left");
        assertFalse(tree.shouldGoLeft(70, 50), "70 > 50, should not go left");
        assertFalse(tree.shouldGoLeft(50, 50), "50 == 50, should not go left");
    }

    @Test
    public void testShouldGoRight() {
        BinaryTree tree = new BinaryTree();
        // shouldGoRight(val, target) -> true if val > target
        assertTrue(tree.shouldGoRight(70, 50), "70 > 50, should go right");
        assertFalse(tree.shouldGoRight(30, 50), "30 < 50, should not go right");
        assertFalse(tree.shouldGoRight(50, 50), "50 == 50, should not go right");
    }
}
