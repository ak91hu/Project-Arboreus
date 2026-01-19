package com.arboreus.logic;

public class RBTOperation {
    public enum Type {
        RECOLOR, ROTATE_LEFT, ROTATE_RIGHT, INSERT
    }

    public Type type;
    public TreeNode affectedNode;
    public String description;

    public RBTOperation(Type type, TreeNode affectedNode, String description) {
        this.type = type;
        this.affectedNode = affectedNode;
        this.description = description;
    }
}
