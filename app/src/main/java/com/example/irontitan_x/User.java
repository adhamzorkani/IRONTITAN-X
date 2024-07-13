package com.example.irontitan_x;

import androidx.annotation.NonNull;

import java.util.Map;

public class User {
    private String name;
    private String email;
    private String weight_goal;
    private String activity_level;
    private String gender;
    private String age;
    private String height;
    private String weight;
    private String plan;
    private int streak;
    private int longest_streak;
    private Map<String, Object> workout_plans;
    private int calories_goal;
    private double calories_input;
    private int water_goal;
    private double water_input;

    public User() {
    }

    public User(String name, String email, String weight_goal, String activity_level, String gender,
                String age, String height, String weight, String plan,
                int streak, int longest_streak, Map<String, Object> workout_plans,
                int calories_goal, double calories_input, int water_goal, double water_input) {
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
    public int getCalories_goal() {
        return calories_goal;
    }
    public double getCalories_input() {
        return calories_input;
    }
    public int getWater_goal() {
        return water_goal;
    }
    public double getWater_input() {
        return water_input;
    }

    @NonNull
    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", weight_goal='" + weight_goal + '\'' +
                ", activity_level='" + activity_level + '\'' +
                ", gender='" + gender + '\'' +
                ", age='" + age + '\'' +
                ", height='" + height + '\'' +
                ", weight='" + weight + '\'' +
                ", plan='" + plan + '\'' +
                ", streak=" + streak +
                ", longest_streak=" + longest_streak +
                ", workout_plans=" + workout_plans +
                ", calories_goal=" + calories_goal +
                ", calories_input=" + calories_input +
                ", water_goal=" + water_goal +
                ", water_input=" + water_input +
                '}';
    }
}
