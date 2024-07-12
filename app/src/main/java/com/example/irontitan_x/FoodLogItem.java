package com.example.irontitan_x;

public class FoodLogItem {
    private String name;
    private int grams;
    private int energy;

    public FoodLogItem() {
        // Default constructor required for calls to DataSnapshot.getValue(FoodLogItem.class)
    }

    public FoodLogItem(String name, int grams, int energy) {
        this.name = name;
        this.grams = grams;
        this.energy = energy;
    }

    public String getName() {
        return name;
    }

    public int getGrams() {
        return grams;
    }

    public int getEnergy() {
        return energy;
    }
}
