package org.misis.tp.lab3;

import java.util.List;
import java.util.Random;

public class Dice {
    private static Dice instance;
    private final Random random;

    private Dice() {
        this.random = new Random();
    }

    public static Dice getInstance() {
        if (instance == null) {
            instance = new Dice();
        }
        return instance;
    }

    public void reseed(long seed) {
        this.random.setSeed(seed);
    }

    public int nextSeed() {
        return this.random.nextInt(1_000_000);
    }

    public int roll(int low, int high) {
        return this.random.nextInt(high - low + 1) + low;
    }

    public boolean chance(double probability) {
        return this.random.nextDouble() < probability;
    }

    public <T> T pick(List<T> options) {
        return options.get(this.random.nextInt(options.size()));
    }
}
