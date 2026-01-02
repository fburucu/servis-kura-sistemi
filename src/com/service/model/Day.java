package com.service.model;

public enum Day {
    MONDAY(0),
    TUESDAY(1),
    WEDNESDAY(2),
    THURSDAY(3),
    FRIDAY(4),
    SATURDAY(5),
    SUNDAY(6);

    private final int index;

    Day(int index) {
        this.index = index;
    }

    public int getIndex() {
        return index;
    }

    public static Day fromIndex(int index) {
        for (Day d : values()) {
            if (d.index == index) return d;
        }
        throw new IllegalArgumentException("Geçersiz gün indexi");
    }
}
