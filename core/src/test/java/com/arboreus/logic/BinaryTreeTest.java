package com.arboreus.logic;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class BinaryTreeTest {

    @Test
    public void testInsert() {
        BinaryTree tree = new BinaryTree();
        tree.insert(50);
        tree.insert(30);
        tree.insert(70);
        tree.insert(20);
        tree.insert(40);
        tree.insert(60);
        tree.insert(80);

        TreeNode root = tree.root;
        assertNotNull(root);
        assertEquals(50, root.value);
        assertEquals(0, root.targetX, 0.001);
        assertEquals(0, root.targetY, 0.001);

        // Left subtree
        assertNotNull(root.left);
        assertEquals(30, root.left.value);
        assertEquals(root, root.left.parent);

        assertNotNull(root.left.left);
        assertEquals(20, root.left.left.value);
        assertEquals(root.left, root.left.left.parent);

        assertNotNull(root.left.right);
        assertEquals(40, root.left.right.value);
        assertEquals(root.left, root.left.right.parent);

        // Right subtree
        assertNotNull(root.right);
        assertEquals(70, root.right.value);
        assertEquals(root, root.right.parent);

        assertNotNull(root.right.left);
        assertEquals(60, root.right.left.value);
        assertEquals(root.right, root.right.left.parent);

        assertNotNull(root.right.right);
        assertEquals(80, root.right.right.value);
        assertEquals(root.right, root.right.right.parent);
    }
}
