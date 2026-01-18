package com.arboreus.algo;

/**
 * Represents a node in the Binary Search Tree.
 * Contains data and metadata for UI positioning.
 *
 * @param <T> The type of data stored in the node, must be Comparable.
 */
public class TreeNode<T extends Comparable<T>> {
    private T value;
    private TreeNode<T> left;
    private TreeNode<T> right;
    private TreeNode<T> parent;

    // UI Metadata - Logic only, no graphics libraries
    private float targetX;
    private float targetY;

    public TreeNode(T value) {
        this.value = value;
    }

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }

    public TreeNode<T> getLeft() {
        return left;
    }

    public void setLeft(TreeNode<T> left) {
        this.left = left;
    }

    public TreeNode<T> getRight() {
        return right;
    }

    public void setRight(TreeNode<T> right) {
        this.right = right;
    }

    public TreeNode<T> getParent() {
        return parent;
    }

    public void setParent(TreeNode<T> parent) {
        this.parent = parent;
    }

    public float getTargetX() {
        return targetX;
    }

    public void setTargetX(float targetX) {
        this.targetX = targetX;
    }

    public float getTargetY() {
        return targetY;
    }

    public void setTargetY(float targetY) {
        this.targetY = targetY;
    }
}
