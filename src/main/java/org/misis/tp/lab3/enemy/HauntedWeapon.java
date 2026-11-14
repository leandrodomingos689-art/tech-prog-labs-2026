package org.misis.tp.lab3.enemy;

import org.misis.tp.lab3.combat.Hit;
import org.misis.tp.lab3.core.Dice;
import org.misis.tp.lab3.defense.Damage;
import org.misis.tp.lab3.item.Weapon;

public class HauntedWeapon implements Enemy {
    private final Weapon weapon;
    private final int maxHealth;
    private int currentHealth;

    public HauntedWeapon(Weapon weapon) {
        this.weapon = weapon;
        this.maxHealth = weapon.getDamage() * 5;
        this.currentHealth = this.maxHealth;
    }

    @Override public String getCode() { return "haunted-weapon"; }
    @Override public String getName() { return "Одержимый " + weapon.getName(); }
    @Override public int getHealth() { return currentHealth; }
    @Override public int getMaxHealth() { return maxHealth; }
    @Override public boolean isAlive() { return currentHealth > 0; }
    @Override public Hit rollDamage() { return weapon.rollDamage(); }
    @Override public int getExperienceReward() { return weapon.getPrice() / 2; }
    @Override public int rollGoldReward() { return Dice.getInstance().roll(weapon.getPrice() / 4, weapon.getPrice() / 2); }

    @Override
    public Damage receiveHit(int amount) {
        Damage damage = new Damage(amount);
        currentHealth = Math.max(0, currentHealth - damage.getAmount());
        return damage;
    }
}
