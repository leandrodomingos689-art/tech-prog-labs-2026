package org.misis.tp.lab3;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DiceTest {

    private Dice dice;

    @BeforeEach
    void resetDice() {
        dice = Dice.getInstance();
        dice.reseed(2026);
    }

    @Test
    void returnsSameInstance() {
        assertSame(Dice.getInstance(), Dice.getInstance());
    }

    @Test
    void rollsStayWithinBounds() {
        int min = 1;
        int max = 6;
        for (int i = 0; i < 100; i++) {
            int roll = dice.roll(min, max);
            assertTrue(roll >= min && roll <= max);
        }
    }

    @Test
    void equalBoundsReturnOnlyValue() {
        int value = 7;
        assertEquals(value, dice.roll(value, value));
    }

    @Test
    void handlesCertainOutcomes() {
        double impossible = 0.0;
        double certain = 1.0;
        for (int i = 0; i < 100; i++) {
            assertFalse(dice.chance(impossible));
            assertTrue(dice.chance(certain));
        }
    }

    @Test
    void picksOnlyElement() {
        Object element = new Object();
        assertSame(element, dice.pick(List.of(element)));
    }

    @Test
    void picksElementFromList() {
        List<String> options = List.of("меч", "щит", "посох");
        for (int i = 0; i < 100; i++) {
            assertTrue(options.contains(dice.pick(options)));
        }
    }

    @Test
    void reseedRepeatsSequence() {
        long seed = 42L;
        int count = 10;

        dice.reseed(seed);
        List<Integer> first = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            first.add(dice.roll(1, 100));
        }

        dice.reseed(seed);
        List<Integer> repeated = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            repeated.add(dice.roll(1, 100));
        }

        assertEquals(first, repeated);
    }
}
