package org.misis.tp.lab2;

public class Parrot extends Animal {
    public Parrot(String name, int age) {
        super(name, age);
    }

    @Override
    public String express() {
        return getName() + ": \"Hello!\"\n" +
                "   \\\n" +
                "   (o>\n" +
                "\\\\_//)\n" +
                " \\_/_)\n" +
                "  _|_";
    }

    @Override
    public int getAgeInHumanYears() {
        return getAge() * 5;
    }
}
