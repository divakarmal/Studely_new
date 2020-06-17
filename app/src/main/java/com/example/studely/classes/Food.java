package com.example.studely.classes;

import java.io.Serializable;

public class Food implements Serializable  {
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

    @Override
    public String toString() {
        return String.format("%s x %d\t\t%d", name, quantity, price);
    }
}
