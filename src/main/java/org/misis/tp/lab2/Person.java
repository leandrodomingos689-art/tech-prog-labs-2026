package org.misis.tp.lab2;

public class Person {
    private String name;
    private Pet pet;

    public Person(String name) {
        this.name = name;
        this.pet = null;
    }

    public Person(String name, Pet pet) {
        this.name = name;
        this.pet = pet;
    }

    public void getPetInfo() {
        System.out.println("--------------------");
        if (this.pet == null) {
            System.out.println(this.name + " has no pet yet");
        } else {
            System.out.println("Pet for " + this.name + " is named " + this.pet.getName());
            System.out.println("Age: " + this.pet.getAge() + " year(s), about " + this.pet.getAgeInHumanYears() + " human years");
            System.out.println("And it expresses itself like this:");
            System.out.println(this.pet.express());
        }
        System.out.println("--------------------");
    }

    public void setPet(Pet pet) {
        this.pet = pet;
    }
}
