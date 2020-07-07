package com.example.studely.classes;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Order implements Serializable {
    private LocalDateTime timestamp;
    private String deliverer;
    private String receiver;
    private String canteen;
    private List<Food> foodList;
    private int orderCost;
    private String deliveryTime;
    private String destination;

    public Order() {
        timestamp = LocalDateTime.now();
        foodList = new ArrayList<>();
        orderCost = 0;
    }

    public void addFood(Food food) {
        foodList.add(food);
    }

    public String getDeliverer() {
        return deliverer;
    }

    public void setDeliverer(String deliv) {
        this.deliverer = deliv;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receive) {
        this.receiver = receive;
    }

    public List<Food> getList() {
        return this.foodList;
    }

    public int calcOrderCost() {
        this.orderCost = 0;
        for (Food food : foodList) {
            this.orderCost += food.calcCost();
        }
        return this.orderCost;
    }

    public String getDeliveryTime() {
        return deliveryTime;
    }

    public void setDeliveryTime(String deliTime) {
        // Conversion
        this.deliveryTime = deliTime;
    }

    public String getCanteen() {
        return canteen;
    }

    public void setCanteen(String canteen) {
        this.canteen = canteen;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String dest) {
        this.destination = dest;
    }
}
