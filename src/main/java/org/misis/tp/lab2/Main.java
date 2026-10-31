package org.misis.tp.lab2;

public class Main {
    public static void main(String[] args) {
        Pet kesha = new Parrot("Kesha", 3);
        Person jack = new Person("Jack", kesha);
        jack.getPetInfo();

        Person john = new Person("John");
        john.getPetInfo();

        Pet zavala = new Turtle("Zavala", 4);
        john.setPet(zavala);
        john.getPetInfo();
    }
}
