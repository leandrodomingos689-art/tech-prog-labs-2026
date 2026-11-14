package org.misis.tp.lab3.defense;

public abstract class DamageHandler {
    private DamageHandler next;
    public DamageHandler setNext(DamageHandler next) { this.next = next; return next; }
    public abstract void handle(Damage damage);
    protected void passToNext(Damage damage) { if (next != null) next.handle(damage); }
}
