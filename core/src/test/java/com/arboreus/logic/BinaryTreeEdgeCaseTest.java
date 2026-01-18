package com.arboreus.logic;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class BinaryTreeEdgeCaseTest {

    @Test
    public void testDuplicateInsertionIgnored() {
        BinaryTree tree = new BinaryTree();
        tree.insert(50);
        tree.insert(50); // Duplicate

        assertEquals(50, tree.root.value);
        assertNull(tree.root.left, "Duplicate 50 should not create a left child");
        assertNull(tree.root.right, "Duplicate 50 should not create a right child");
    }

    @Test
    public void testDegenerateTreeRight() {
        BinaryTree tree = new BinaryTree();
        tree.insert(10);
        tree.insert(20);
        tree.insert(30);
        tree.insert(40);

        // 10 -> 20 -> 30 -> 40 (Rights only)
        assertNotNull(tree.root.right);
        assertEquals(20, tree.root.right.value);

        assertNotNull(tree.root.right.right);
        assertEquals(30, tree.root.right.right.value);

        assertNotNull(tree.root.right.right.right);
        assertEquals(40, tree.root.right.right.right.value);

        // Ensure no left children
        assertNull(tree.root.left);
        assertNull(tree.root.right.left);
        assertNull(tree.root.right.right.left);
    }

    @Test
    public void testDegenerateTreeLeft() {
        BinaryTree tree = new BinaryTree();
        tree.insert(50);
        tree.insert(40);
        tree.insert(30);
        tree.insert(20);

        // 50 -> 40 -> 30 -> 20 (Lefts only)
        assertNotNull(tree.root.left);
        assertEquals(40, tree.root.left.value);

        assertNotNull(tree.root.left.left);
        assertEquals(30, tree.root.left.left.value);

        assertNotNull(tree.root.left.left.left);
        assertEquals(20, tree.root.left.left.left.value);
    }
}
