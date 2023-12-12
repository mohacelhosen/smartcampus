package com.smartcampus.Student.model;
public enum Semester {
    WINTER,
    SUMMER,
    FALL;

    public static Semester fromString(String text) {
        for (Semester semester : Semester.values()) {
            if (semester.toString().equalsIgnoreCase(text)) {
                return semester;
            }
        }
        throw new IllegalArgumentException("No constant with text " + text + " found in enum Semester");
    }
}
