package com.arboreus.logic;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class NumberGeneratorTest {

    private NumberGenerator numberGenerator;

    @BeforeEach
    public void setUp() {
        // Range 1-10
        numberGenerator = new NumberGenerator(1, 10);
    }

    @Test
    public void testGenerateInRange() {
        for (int i = 0; i < 50; i++) {
            int num = numberGenerator.generateUniqueNumber();
            assertTrue(num >= 1 && num <= 10, "Generated number " + num + " should be between 1 and 10");
        }
    }

    @Test
    public void testUniqueness() {
        Set<Integer> manualSet = new HashSet<>();
        // Should be able to generate 10 unique numbers
        for (int i = 0; i < 10; i++) {
            int num = numberGenerator.generateUniqueNumber();
            assertTrue(manualSet.add(num), "Number " + num + " should be unique in current cycle");
        }

        // After 10, it should reset (based on logic analysis or assumption)
        // Code says: if (usedNumbers.size() >= (max - min + 1)) usedNumbers.clear();
        // So the 11th number is allowed to be a duplicate of previous set, but starts a
        // new cycle
        int num11 = numberGenerator.generateUniqueNumber();
        assertTrue(num11 >= 1 && num11 <= 10);
    }

    @Test
    public void testReset() {
        numberGenerator.generateUniqueNumber();
        numberGenerator.reset();
        // Reset clears used numbers. Hard to test "state cleared" without accessing
        // private field,
        // but we can infer if we essentially restart cycle.
        // Logic: usedNumbers.clear().
        // Not much to verify externally other than no exception.
        assertDoesNotThrow(() -> numberGenerator.generateUniqueNumber());
    }
}
