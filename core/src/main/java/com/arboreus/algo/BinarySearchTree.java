package com.arboreus.algo;

import java.util.Objects;

/**
 * A pure Java 17 implementation of a Binary Search Tree.
 * Includes support for Rotation, Insertion, Deletion, and Search.
 *
 * @param <T> The type of element held in the tree.
 */
public class BinarySearchTree<T extends Comparable<T>> {

    private TreeNode<T> root;

    public BinarySearchTree() {
        this.root = null;
    }

    public TreeNode<T> getRoot() {
        return root;
    }

    /**
     * Inserts a value into the BST.
     * 
     * @param value The value to insert.
     */
    public void insert(T value) {
        Objects.requireNonNull(value, "Value cannot be null");
        if (root == null) {
            root = new TreeNode<>(value);
            return;
        }
        insertRecursive(root, value);
    }

    private void insertRecursive(TreeNode<T> current, T value) {
        int cmp = value.compareTo(current.getValue());
        if (cmp < 0) {
            if (current.getLeft() == null) {
                TreeNode<T> newNode = new TreeNode<>(value);
                current.setLeft(newNode);
                newNode.setParent(current);
            } else {
                insertRecursive(current.getLeft(), value);
            }
        } else if (cmp > 0) {
            if (current.getRight() == null) {
                TreeNode<T> newNode = new TreeNode<>(value);
                current.setRight(newNode);
                newNode.setParent(current);
            } else {
                insertRecursive(current.getRight(), value);
            }
        }
        // Duplicate values are ignored in this implementation
    }

    /**
     * Searches for a value in the BST.
     * 
     * @param value The value to search for.
     * @return The node containing the value, or null if not found.
     */
    public TreeNode<T> search(T value) {
        Objects.requireNonNull(value, "Value cannot be null");
        return searchRecursive(root, value);
    }

    private TreeNode<T> searchRecursive(TreeNode<T> current, T value) {
        if (current == null) {
            return null;
        }
        int cmp = value.compareTo(current.getValue());
        if (cmp == 0) {
            return current;
        } else if (cmp < 0) {
            return searchRecursive(current.getLeft(), value);
        } else {
            return searchRecursive(current.getRight(), value);
        }
    }

    /**
     * Deletes a value from the BST.
     * 
     * @param value The value to delete.
     */
    public void delete(T value) {
        Objects.requireNonNull(value, "Value cannot be null");
        root = deleteRecursive(root, value);
    }

    private TreeNode<T> deleteRecursive(TreeNode<T> current, T value) {
        if (current == null) {
            return null;
        }

        int cmp = value.compareTo(current.getValue());
        if (cmp < 0) {
            current.setLeft(deleteRecursive(current.getLeft(), value));
        } else if (cmp > 0) {
            current.setRight(deleteRecursive(current.getRight(), value));
        } else {
            // Node to delete found

            // Case 1: Leaf node or one child
            if (current.getLeft() == null) {
                TreeNode<T> right = current.getRight();
                if (right != null)
                    right.setParent(current.getParent());
                return right;
            } else if (current.getRight() == null) {
                TreeNode<T> left = current.getLeft();
                if (left != null)
                    left.setParent(current.getParent());
                return left;
            }

            // Case 2: Two children
            // Find in-order successor (smallest in the right subtree)
            TreeNode<T> successor = findMin(current.getRight());
            current.setValue(successor.getValue());
            current.setRight(deleteRecursive(current.getRight(), successor.getValue()));
        }
        return current;
    }

    private TreeNode<T> findMin(TreeNode<T> node) {
        while (node.getLeft() != null) {
            node = node.getLeft();
        }
        return node;
    }

    /**
     * Rotates the tree left around the given node.
     * 
     * @param node The pivot node for the rotation.
     */
    public void rotateLeft(TreeNode<T> node) {
        if (node == null || node.getRight() == null) {
            return; // Cannot rotate left if no right child
        }

        TreeNode<T> rightChild = node.getRight();
        node.setRight(rightChild.getLeft());

        if (rightChild.getLeft() != null) {
            rightChild.getLeft().setParent(node);
        }

        rightChild.setParent(node.getParent());

        if (node.getParent() == null) {
            root = rightChild;
        } else if (node == node.getParent().getLeft()) {
            node.getParent().setLeft(rightChild);
        } else {
            node.getParent().setRight(rightChild);
        }

        rightChild.setLeft(node);
        node.setParent(rightChild);
    }

    /**
     * Rotates the tree right around the given node.
     * 
     * @param node The pivot node for the rotation.
     */
    public void rotateRight(TreeNode<T> node) {
        if (node == null || node.getLeft() == null) {
            return; // Cannot rotate right if no left child
        }

        TreeNode<T> leftChild = node.getLeft();
        node.setLeft(leftChild.getRight());

        if (leftChild.getRight() != null) {
            leftChild.getRight().setParent(node);
        }

        leftChild.setParent(node.getParent());

        if (node.getParent() == null) {
            root = leftChild;
        } else if (node == node.getParent().getRight()) {
            node.getParent().setRight(leftChild);
        } else {
            node.getParent().setLeft(leftChild);
        }

        leftChild.setRight(node);
        node.setParent(leftChild);
    }
}
