package com.example.attendbook;

public class DisplayAttendanceModel {

    private String name;
    private String attendanceCount;

    public DisplayAttendanceModel(String name, String attendanceCount) {
        this.name = name;
        this.attendanceCount = attendanceCount;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAttendanceCount() {
        return attendanceCount;
    }

    public void setAttendanceCount(String attendanceCount) {
        this.attendanceCount = attendanceCount;
    }

    public void increaseAttendanceCount(int x) {

        this.attendanceCount = String.valueOf(Integer.parseInt(this.attendanceCount)+x);

    }
}
