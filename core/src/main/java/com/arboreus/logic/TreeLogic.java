package com.arboreus.logic;

public interface TreeLogic {
    boolean insert(int value);

    TreeNode getRoot();

    void recalculatePositions(float startX, float startY, float hSpacing, float vSpacing);

    boolean shouldGoLeft(int currentValue, int targetNodeValue);

    boolean shouldGoRight(int currentValue, int targetNodeValue);

    void reset();

    java.util.List<RBTOperation> flushOperations();
}
