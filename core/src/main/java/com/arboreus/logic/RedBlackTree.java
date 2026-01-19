package com.arboreus.logic;

public class RedBlackTree extends BinaryTree {
    private java.util.List<RBTOperation> lastOperations = new java.util.ArrayList<>();

    @Override
    public java.util.List<RBTOperation> flushOperations() {
        java.util.List<RBTOperation> ops = new java.util.ArrayList<>(lastOperations);
        lastOperations.clear();
        return ops;
    }

    private void log(RBTOperation.Type type, TreeNode node, String desc) {
        lastOperations.add(new RBTOperation(type, node, desc));
    }

    @Override
    public boolean insert(int value) {
        // Depth Check (Inherited from BinaryTree)
        if (!canInsert(root, value, 0))
            return false;

        // Standard BST Insert with Parent pointer maintenance
        TreeNode newNode = new TreeNode(value);
        newNode.isRed = true; // Always RED at start
        log(RBTOperation.Type.INSERT, newNode, "Inserting " + value + " (RED)");

        if (root == null) {
            root = newNode;
            root.isRed = false; // Root is always BLACK
            log(RBTOperation.Type.RECOLOR, root, "Root " + value + " becomes BLACK");
            return true;
        }

        TreeNode current = root;
        TreeNode parent = null;

        while (current != null) {
            parent = current;
            if (value < current.value) {
                current = current.left;
            } else if (value > current.value) {
                current = current.right;
            } else {
                return false; // Duplicate
            }
        }

        newNode.parent = parent;
        if (value < parent.value) {
            parent.left = newNode;
        } else {
            parent.right = newNode;
        }

        fixInsert(newNode);
        return true;
    }

    private void fixInsert(TreeNode k) {
        TreeNode u; // Uncle

        while (k.parent != null && k.parent.isRed) {
            if (k.parent == k.parent.parent.right) {
                u = k.parent.parent.left;
                if (u != null && u.isRed) {
                    // Case 1: Uncle is RED -> Recolor
                    log(RBTOperation.Type.RECOLOR, u, "Recolor Uncle " + u.value + " to BLACK");
                    u.isRed = false;

                    log(RBTOperation.Type.RECOLOR, k.parent, "Recolor Parent " + k.parent.value + " to BLACK");
                    k.parent.isRed = false;

                    log(RBTOperation.Type.RECOLOR, k.parent.parent,
                            "Recolor Grandparent " + k.parent.parent.value + " to RED");
                    k.parent.parent.isRed = true;
                    k = k.parent.parent;
                } else {
                    if (k == k.parent.left) {
                        // Case 2: Uncle is BLACK, Triangle -> Rotate Parent
                        k = k.parent;
                        rotateRight(k);
                    }
                    // Case 3: Uncle is BLACK, Line -> Rotate Grandparent + Recolor
                    log(RBTOperation.Type.RECOLOR, k.parent, "Recolor Parent " + k.parent.value + " to BLACK");
                    k.parent.isRed = false;

                    log(RBTOperation.Type.RECOLOR, k.parent.parent,
                            "Recolor Grandparent " + k.parent.parent.value + " to RED");
                    k.parent.parent.isRed = true;
                    rotateLeft(k.parent.parent);
                }
            } else {
                // Mirror case (Parent is Left Child)
                u = k.parent.parent.right;
                if (u != null && u.isRed) {
                    log(RBTOperation.Type.RECOLOR, u, "Recolor Uncle " + u.value + " to BLACK");
                    u.isRed = false;

                    log(RBTOperation.Type.RECOLOR, k.parent, "Recolor Parent " + k.parent.value + " to BLACK");
                    k.parent.isRed = false;

                    log(RBTOperation.Type.RECOLOR, k.parent.parent,
                            "Recolor Grandparent " + k.parent.parent.value + " to RED");
                    k.parent.parent.isRed = true;
                    k = k.parent.parent;
                } else {
                    if (k == k.parent.right) {
                        k = k.parent;
                        rotateLeft(k);
                    }
                    log(RBTOperation.Type.RECOLOR, k.parent, "Recolor Parent " + k.parent.value + " to BLACK");
                    k.parent.isRed = false;

                    log(RBTOperation.Type.RECOLOR, k.parent.parent,
                            "Recolor Grandparent " + k.parent.parent.value + " to RED");
                    k.parent.parent.isRed = true;
                    rotateRight(k.parent.parent);
                }
            }
            if (k == root)
                break;
        }
        if (root.isRed) {
            log(RBTOperation.Type.RECOLOR, root, "Repaint Root " + root.value + " BLACK");
            root.isRed = false;
        }
    }

    private void rotateLeft(TreeNode x) {
        log(RBTOperation.Type.ROTATE_LEFT, x, "Rotate Left on " + x.value);
        TreeNode y = x.right;
        x.right = y.left;
        if (y.left != null) {
            y.left.parent = x;
        }
        y.parent = x.parent;
        if (x.parent == null) {
            root = y;
        } else if (x == x.parent.left) {
            x.parent.left = y;
        } else {
            x.parent.right = y;
        }
        y.left = x;
        x.parent = y;
    }

    private void rotateRight(TreeNode x) {
        log(RBTOperation.Type.ROTATE_RIGHT, x, "Rotate Right on " + x.value);
        TreeNode y = x.left;
        x.left = y.right;
        if (y.right != null) {
            y.right.parent = x;
        }
        y.parent = x.parent;
        if (x.parent == null) {
            root = y;
        } else if (x == x.parent.right) {
            x.parent.right = y;
        } else {
            x.parent.left = y;
        }
        y.right = x;
        x.parent = y;
    }
}
