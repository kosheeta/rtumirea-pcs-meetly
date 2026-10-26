package ru.rtumirea.meetly.model;

public class Room {
    private Integer id;
    private String name;
    private int capacity;
    private String address;

    public Room() {
    }

    public Room(Integer id, String name, int capacity, String address) {
        this.id = id;
        this.name = name;
        this.capacity = capacity;
        this.address = address;
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getCapacity() {
        return capacity;
    }

    public String getAddress() {
        return address;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
