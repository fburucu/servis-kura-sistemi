package com.service.model;

public class Vehicle {

    private String plate;
    private Day restDay;
    private boolean transitionWeek;

    public Vehicle(String plate, Day restDay, boolean transitionWeek) {
        this.plate = plate;
        this.restDay = restDay;
        this.transitionWeek = transitionWeek;
    }

    public String getPlate() {
        return plate;
    }

    public Day getRestDay() {
        return restDay;
    }

    public void setRestDay(Day restDay) {
        this.restDay = restDay;
    }

    public boolean isTransitionWeek() {
        return transitionWeek;
    }

    public void setTransitionWeek(boolean transitionWeek) {
        this.transitionWeek = transitionWeek;
    }
}
