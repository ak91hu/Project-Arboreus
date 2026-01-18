package com.arboreus.logic;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class BinaryTreeLayoutTest {

    @Test
    public void testRecalculatePositionsRootOnly() {
        BinaryTree tree = new BinaryTree();
        tree.insert(50);

        float startX = 500;
        float startY = 600;
        float hSpacing = 200;
        float vSpacing = 100;

        tree.recalculatePositions(startX, startY, hSpacing, vSpacing);

        assertEquals(startX, tree.root.targetX, 0.001, "Root X shoud be startX");
        assertEquals(startY, tree.root.targetY, 0.001, "Root Y should be startY");
    }

    @Test
    public void testRecalculatePositionsChildren() {
        BinaryTree tree = new BinaryTree();
        tree.insert(50); // Level 0
        tree.insert(25); // Level 1 (Left)
        tree.insert(75); // Level 1 (Right)

        float startX = 500;
        float startY = 600;
        float hSpacing = 200;
        float vSpacing = 100;

        tree.recalculatePositions(startX, startY, hSpacing, vSpacing);

        // Root
        assertEquals(startX, tree.root.targetX, 0.001);
        assertEquals(startY, tree.root.targetY, 0.001);

        // Level 1 Offset: hSpacing / 2^(0+1) = 200 / 2 = 100
        float offsetL1 = 100;

        // Left Child
        assertEquals(startX - offsetL1, tree.root.left.targetX, 0.001, "Left child X incorrect");
        assertEquals(startY - vSpacing, tree.root.left.targetY, 0.001, "Left child Y incorrect");

        // Right Child
        assertEquals(startX + offsetL1, tree.root.right.targetX, 0.001, "Right child X incorrect");
        assertEquals(startY - vSpacing, tree.root.right.targetY, 0.001, "Right child Y incorrect");
    }
}
