package org.misis.tp.lab3.defense;

public class Damage {
    private int amount;
    private boolean evaded = false;
    private int absorbed = 0;

    public Damage(int amount) { this.amount = amount; }
    public int getAmount() { return amount; }
    public boolean isEvaded() { return evaded; }
    public int getAbsorbed() { return absorbed; }
    public void reduceTo(int amount) { this.amount = amount; }
    public void evade() { this.amount = 0; this.evaded = true; }
    public void absorb(int value) { this.amount -= value; this.absorbed += value; }
}
