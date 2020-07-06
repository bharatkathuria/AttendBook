package com.example.attendbook;


import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class LectureInformation{

    public String day;
    public String startTime;
    public String endTime;
    public String courseId;
    public String subjectId;
    public String location;


    public void setLocation(String location) {
        this.location = location;
    }


    public LectureInformation()
    {


    }

    public void setDay(String day) {
        this.day = day;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    public void setSubjectId(String subjectId) {
        this.subjectId = subjectId;
    }

    public String getDay() {
        return day;
    }

    public String getStartTime() {

        int intStartTime = Integer.parseInt(startTime);
        String timeMode ="";
        timeMode = intStartTime>=12?"PM":"AM";

        intStartTime =intStartTime%12;
        if(intStartTime==0){
            intStartTime=12;
        }
        String temp="";
        if(intStartTime<10)
        {
            temp="0";
        }

        String stringStartTime = temp+intStartTime+" : 00 "+timeMode;

        return stringStartTime;
    }

    public String getEndTime() {

        int intEndTime = Integer.parseInt(endTime);
        String timeMode ="";
        timeMode = intEndTime>=12?"PM":"AM";

        intEndTime =intEndTime%12;
        if(intEndTime==0){
            intEndTime=12;
        }
        String temp="";
        if(intEndTime<10)
        {
            temp="0";
        }

        String stringEndTime = temp+intEndTime+" : 00 "+timeMode;

        return stringEndTime;
    }

    public String getCourseId() {
        return courseId;
    }

    public String getSubjectId() {
        return subjectId;
    }
    public String getLocation() {
        return location;
    }


    public LectureInformation(String day, String startTime, String endTime, String courseId, String subjectId,String location) {
        this.day = day;
        this.startTime = startTime;
        this.endTime = endTime;
        this.courseId = courseId;
        this.subjectId = subjectId;
        this.location  = location;
    }
}
