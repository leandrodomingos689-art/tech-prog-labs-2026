package org.misis.tp.lab2;

public abstract class Animal implements Pet {
    private String name;
    private int age;

    public Animal(String name, int age) {
        this.name = name;
        this.age = age;
    }

    public void happyBirthday() {
        this.age++;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public int getAge() {
        return age;
    }

    @Override
    public abstract String express();

    @Override
    public abstract int getAgeInHumanYears();
}
