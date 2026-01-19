package com.arboreus.logic;

public class TreeNode {
    public int value;
    public TreeNode left;
    public TreeNode right;
    public TreeNode parent;

    // Visual metadata
    public float targetX;
    public float targetY;

    // Red-Black Tree Field
    public boolean isRed = true; // New nodes are RED by default

    public TreeNode(int value) {
        this.value = value;
    }
}
