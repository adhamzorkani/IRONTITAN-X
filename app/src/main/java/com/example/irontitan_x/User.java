package com.example.irontitan_x;

import java.util.Map;

public class User {
    private final String name;
    private final String email;
    private final String weight_goal;
    private final String activity_level;
    private final String gender;
    private final String age;
    private final String height;
    private final String weight;
    private final String plan;
    private final int streak;
    private final int longest_streak;
    private final Map<String, Object> workout_plans;
    private final String calories_goal;
    private final String calories_input;
    private final String water_goal;
    private final String water_input;


    public User(String name, String email, String weight_goal, String activity_level, String gender,
                String age, String height, String weight, String plan,
                int streak, int longest_streak, Map<String, Object> workout_plans,
                String calories_goal, String calories_input, String water_goal, String water_input) {
        this.name = name;
        this.email = email;
        this.weight_goal = weight_goal;
        this.activity_level = activity_level;
        this.gender = gender;
        this.age = age;
        this.height = height;
        this.weight = weight;
        this.plan = plan;
        this.streak = streak;
        this.longest_streak = longest_streak;
        this.workout_plans = workout_plans;
        this.calories_goal = calories_goal;
        this.calories_input = calories_input;
        this.water_goal = water_goal;
        this.water_input = water_input;
    }

    public String getName() {
        return name;
    }
    public String getEmail() {
        return email;
    }
    public String getWeight_goal() {
        return weight_goal;
    }
    public String getActivity_level() {
        return activity_level;
    }
    public String getGender() {
        return gender;
    }
    public String getAge() {
        return age;
    }
    public String getHeight() {
        return height;
    }
    public String getWeight() {
        return weight;
    }
    public String getPlan() {
        return plan;
    }
    public int getStreak() {
        return streak;
    }
    public int getLongest_streak() {
        return longest_streak;
    }
    public Map<String, Object> getWorkout_plans() {
        return workout_plans;
    }
    public String getCalories_goal() {
        return calories_goal;
    }
    public String getCalories_input() {
        return calories_input;
    }
    public String getWater_goal() {
        return water_goal;
    }
    public String getWater_input() {
        return water_input;
    }
}
