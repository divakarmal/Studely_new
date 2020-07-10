package com.example.studely.misc;

import java.io.Serializable;

public class Food implements Serializable {
    public String name;
    public int price;
    public int quantity;

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
        int p = this.price;
        int q = this.quantity;
        int c = this.calcCost();
        return String.format("%s " + "  $" + q + "    x" + p + "      $" + c, name);
    }
}
