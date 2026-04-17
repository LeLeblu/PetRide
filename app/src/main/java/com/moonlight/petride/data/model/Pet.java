package com.moonlight.petride.data.model;

/**
 * Clase Modelo (DTO) que representa una Mascota en el sistema.
 */
public class Pet {
    private long id;
    private long ownerId;
    private String name;
    private String breed;
    private int age;
    private String careTips;
    private String imagePath;

    public Pet() {}

    public Pet(long id, long ownerId, String name, String breed, int age, String careTips, String imagePath) {
        this.id = id;
        this.ownerId = ownerId;
        this.name = name;
        this.breed = breed;
        this.age = age;
        this.careTips = careTips;
        this.imagePath = imagePath;
    }

    // Getters y Setters
    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

    public long getOwnerId() { return ownerId; }
    public void setOwnerId(long ownerId) { this.ownerId = ownerId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getBreed() { return breed; }
    public void setBreed(String breed) { this.breed = breed; }

    public int getAge() { return age; }
    public void setAge(int age) { this.age = age; }

    public String getCareTips() { return careTips; }
    public void setCareTips(String careTips) { this.careTips = careTips; }

    public String getImagePath() { return imagePath; }
    public void setImagePath(String imagePath) { this.imagePath = imagePath; }
}
