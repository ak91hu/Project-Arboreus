package com.arboreus.logic;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class NumberGenerator {
    private Set<Integer> usedNumbers;
    private Random random;
    private int min;
    private int max;

    public NumberGenerator(int min, int max) {
        this.min = min;
        this.max = max;
        this.usedNumbers = new HashSet<>();
        this.random = new Random();
    }

    public int generateUniqueNumber() {
        if (usedNumbers.size() >= (max - min + 1)) {
            // All numbers used, reset or throw?
            // For this game, let's reset to allow infinite play
            usedNumbers.clear();
        }

        int nextVal;
        do {
            nextVal = random.nextInt(max - min + 1) + min;
        } while (usedNumbers.contains(nextVal));

        usedNumbers.add(nextVal);
        return nextVal;
    }

    public void reset() {
        usedNumbers.clear();
    }
}
