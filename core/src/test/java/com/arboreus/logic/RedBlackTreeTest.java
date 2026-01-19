package com.arboreus.logic;

import org.junit.jupiter.api.Test;
import java.util.Random;
import static org.junit.jupiter.api.Assertions.*;

public class RedBlackTreeTest {

    @Test
    public void testRootIsAlwaysBlack() {
        RedBlackTree tree = new RedBlackTree();
        tree.insert(10);
        assertFalse(tree.root.isRed, "Root must be BLACK after first insert");

        tree.insert(20);
        assertFalse(tree.root.isRed, "Root must remain BLACK after second insert");

        tree.insert(5);
        assertFalse(tree.root.isRed, "Root must remain BLACK after third insert");
    }

    @Test
    public void testNoDoubleRed() {
        RedBlackTree tree = new RedBlackTree();
        // Insert causing rotations/recoloring
        int[] input = { 10, 20, 30, 40, 50, 25, 60, 80 };
        for (int val : input) {
            tree.insert(val);
            validateNoDoubleRed(tree.root);
        }
    }

    @Test
    public void testBlackHeightBalance() {
        RedBlackTree tree = new RedBlackTree();
        int[] input = { 10, 20, 30, 15, 25, 5, 1 };
        for (int val : input) {
            tree.insert(val);
            validateBlackHeight(tree.root);
        }
    }

    @Test
    public void testStressTestRandomInsertions() {
        RedBlackTree tree = new RedBlackTree();
        Random random = new Random(42); // Seed for reproducibility
        for (int i = 0; i < 1000; i++) {
            tree.insert(random.nextInt(10000));
            validateRedBlackProperties(tree);
        }
    }

    @Test
    public void testLeftRotationCase() {
        // RR Case: Insert 10, 20, 30 -> Should rotate left
        RedBlackTree tree = new RedBlackTree();
        tree.insert(10);
        tree.insert(20);
        tree.insert(30);

        // Expected structure after rotation:
        // 20(B)
        // / \
        // 10(R) 30(R)

        assertEquals(20, tree.root.value);
        assertFalse(tree.root.isRed);

        assertEquals(10, tree.root.left.value);
        assertTrue(tree.root.left.isRed);

        assertEquals(30, tree.root.right.value);
        assertTrue(tree.root.right.isRed);
    }

    @Test
    public void testRightRotationCase() {
        // LL Case: Insert 30, 20, 10 -> Should rotate right
        RedBlackTree tree = new RedBlackTree();
        tree.insert(30);
        tree.insert(20);
        tree.insert(10);

        // Expected structure:
        // 20(B)
        // / \
        // 10(R) 30(R)

        assertEquals(20, tree.root.value);
        assertFalse(tree.root.isRed);

        assertEquals(10, tree.root.left.value);
        assertTrue(tree.root.left.isRed);

        assertEquals(30, tree.root.right.value);
        assertTrue(tree.root.right.isRed);
    }

    // --- Helper Validators ---

    private void validateRedBlackProperties(RedBlackTree tree) {
        if (tree.root == null)
            return;
        assertFalse(tree.root.isRed, "Root property violated: Root is RED");
        validateNoDoubleRed(tree.root);
        validateBlackHeight(tree.root);
        validateBSTProperty(tree.root, Integer.MIN_VALUE, Integer.MAX_VALUE);
    }

    private void validateNoDoubleRed(TreeNode node) {
        if (node == null)
            return;
        if (node.isRed) {
            if (node.left != null) {
                assertFalse(node.left.isRed,
                        "Red violation at node " + node.value + " (Left child is RED)");
            }
            if (node.right != null) {
                assertFalse(node.right.isRed,
                        "Red violation at node " + node.value + " (Right child is RED)");
            }
            if (node.parent != null) {
                assertFalse(node.parent.isRed,
                        "Red violation at node " + node.value + " (Parent is RED)");
            }
        }
        validateNoDoubleRed(node.left);
        validateNoDoubleRed(node.right);
    }

    private int validateBlackHeight(TreeNode node) {
        if (node == null)
            return 1; // Null nodes count as BLACK

        int leftHeight = validateBlackHeight(node.left);
        int rightHeight = validateBlackHeight(node.right);

        assertEquals(leftHeight, rightHeight, "Black height mismatch at node " + node.value);

        // If current node is black, add 1 to height
        return leftHeight + (!node.isRed ? 1 : 0);
    }

    private void validateBSTProperty(TreeNode node, int min, int max) {
        if (node == null)
            return;
        assertTrue(node.value > min && node.value < max, "BST Property violated at " + node.value);
        validateBSTProperty(node.left, min, node.value);
        validateBSTProperty(node.right, node.value, max);
    }
}
