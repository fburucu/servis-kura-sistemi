package com.service.logic;

import com.service.model.Day;
import com.service.model.Vehicle;

import java.util.*;

public class WeeklyRestEngine {

    private static final int MAX_FRIDAY = 4;

    public void generateNextWeek(List<Vehicle> vehicles) {

        Map<Day, List<Vehicle>> plan = new EnumMap<>(Day.class);
        for (Day day : Day.values()) {
            plan.put(day, new ArrayList<>());
        }

        for (Vehicle v : vehicles) {
            Day nextDay = calculateNextRestDay(v);
            v.setRestDay(nextDay);
            plan.get(nextDay).add(v);
            v.setTransitionWeek(false);
        }

        enforceFridayLimit(plan);
    }

    private Day calculateNextRestDay(Vehicle vehicle) {
        Day current = vehicle.getRestDay();

        if (vehicle.isTransitionWeek()) {
            if (current == Day.SATURDAY || current == Day.SUNDAY) {
                return Day.FRIDAY;
            }
        }

        int nextIndex = current.getIndex() - 1;

        if (nextIndex < 0) {
            return Day.FRIDAY;
        }

        return Day.fromIndex(nextIndex);
    }

    private void enforceFridayLimit(Map<Day, List<Vehicle>> plan) {
        List<Vehicle> fridayList = plan.get(Day.FRIDAY);

        while (fridayList.size() > MAX_FRIDAY) {
            Vehicle v = fridayList.remove(fridayList.size() - 1);
            v.setRestDay(Day.THURSDAY);
            plan.get(Day.THURSDAY).add(v);
        }
    }
}
