package org.misis.tp.lab2;

public class Turtle extends Animal {
    public Turtle(String name, int age) {
        super(name, age);
    }

    @Override
    public String express() {
        return "  _____     ____\n" +
                " /      \\  |  o |\n" +
                "|        |/ ___\\|\n" +
                "|_________/\n" +
                "|_|_| |_|_|";
    }

    @Override
    public int getAgeInHumanYears() {
        return (getAge() * 5) + 1;
    }
}
