package com.example.irontitan_x;



import java.util.List;

public class ReadyPlans {
    private String name;
    private List<Day> days;

    public ReadyPlans(String name, List<Day> days) {
        this.name = name;
        this.days = days;
    }

    public String getName() {
        return name;
    }

    public List<Day> getDays() {
        return days;
    }

    public void addDay(Day day) {
        days.add(day);
    }

    public void removeDay(Day day) {
        days.remove(day);
    }
}
