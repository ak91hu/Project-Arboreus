package com.arboreus.logic;

public class BinaryTree {
    public TreeNode root;

    private static final int MAX_DEPTH = 6;

    public boolean insert(int value) {
        if (!canInsert(root, value, 0))
            return false;
        root = insertRec(root, value);
        return true;
    }

    private boolean canInsert(TreeNode node, int value, int depth) {
        if (depth >= MAX_DEPTH)
            return false;
        if (node == null)
            return true;
        if (value < node.value)
            return canInsert(node.left, value, depth + 1);
        if (value > node.value)
            return canInsert(node.right, value, depth + 1);
        return false; // Duplicate or equal
    }

    private TreeNode insertRec(TreeNode root, int value) {
        if (root == null) {
            root = new TreeNode(value);
            return root;
        }

        if (value < root.value)
            root.left = insertRec(root.left, value);
        else if (value > root.value)
            root.right = insertRec(root.right, value);

        return root;
    }

    // Simple layout calculation (can be expanded)
    public void recalculatePositions(float startX, float startY, float hSpacing, float vSpacing) {
        recalculateRec(root, startX, startY, hSpacing, vSpacing, 0);
    }

    private void recalculateRec(TreeNode node, float x, float y, float hSpacing, float vSpacing, int level) {
        if (node == null)
            return;

        node.targetX = x;
        node.targetY = y;

        // Simple strategy: width decreases as we go down
        float currentSpacing = hSpacing / (level + 1);
        // Or static spacing for simplicity in visualization start
        // Let's use a simpler approach for now: spacing reduces per level
        float offset = hSpacing / (float) Math.pow(2, level + 1);

        recalculateRec(node.left, x - offset, y - vSpacing, hSpacing, vSpacing, level + 1);
        recalculateRec(node.right, x + offset, y - vSpacing, hSpacing, vSpacing, level + 1);
    }

    public boolean shouldGoRight(int currentValue, int targetNodeValue) {
        return currentValue > targetNodeValue;
    }

    public boolean shouldGoLeft(int currentValue, int targetNodeValue) {
        return currentValue < targetNodeValue;
    }
}
