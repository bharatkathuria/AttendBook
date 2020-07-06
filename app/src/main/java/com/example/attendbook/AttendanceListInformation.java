package com.example.attendbook;

public class AttendanceListInformation {

    public String enrollmentNo;

    public String getEnrollmentNo() {
        return enrollmentNo;
    }

    public AttendanceListInformation(String enrollmentNo, String studentName) {
        this.enrollmentNo = enrollmentNo;
        this.studentName = studentName;
    }

    public void setEnrollmentNo(String enrollmentNo) {
        this.enrollmentNo = enrollmentNo;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String studentName;

    public AttendanceListInformation() {

    }


}
