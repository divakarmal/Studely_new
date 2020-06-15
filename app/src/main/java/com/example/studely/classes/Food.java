package com.example.studely.classes;

public class Food {
    private String name;
    private int price;
    private int quantity;

    public Food(String name, int price, int quantity) {
        this.name = name;
        this.price = price;
        this.quantity = quantity;
    }

    public int calcCost() {
        return price * quantity;
    }
}
