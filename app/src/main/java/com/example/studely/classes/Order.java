package com.example.studely.classes;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Order implements Serializable {
    private LocalDateTime timestamp;
    private User deliverer;
    private User receiver;
    private String canteen;
    private List<Food> foodList;
    private int orderCost;
    private LocalDateTime deliveryTime;
    private String destination;

    public Order() {
        timestamp = LocalDateTime.now();
        foodList = new ArrayList<>();
        orderCost = 0;
    }

    public void setDeliverer(User deliv) { this.deliverer = deliv; }

    public void setReceiver(User receive) { this.receiver = receive; }

    public void setCanteen(String canteen) { this.canteen = canteen; }

    public void addFood(Food food) { foodList.add(food); }

    public List<Food> getList() { return this.foodList; }

    public void calcOrderCost() {
        for (Food food : foodList) {
            this.orderCost += food.calcCost();
        }
    }

    public void setDeliveryTime(LocalDateTime deliTime) { this.deliveryTime = deliTime; }

    public void setDestination(String dest) { this.destination = dest; }
}
