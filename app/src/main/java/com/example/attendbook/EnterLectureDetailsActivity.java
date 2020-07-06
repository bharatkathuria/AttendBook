package com.example.attendbook;

import android.app.ProgressDialog;
import android.content.Intent;
import android.view.View;
import android.widget.*;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class EnterLectureDetailsActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    private Button buttonCreateLecture;
    private Spinner spinnerDay;
    private Spinner spinnerStartTime;
    private Spinner spinnerEndTime;
    private EditText editTextSubjectId;
    private Spinner spinnerCourseId;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;
    private ProgressDialog progressDialog;
    private String userEmail;

    @Override
    public void onBackPressed() {

        if (getFragmentManager().getBackStackEntryCount() > 0) {


            getFragmentManager().popBackStack();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_lecture_details);

        firebaseAuth = FirebaseAuth.getInstance();


        databaseReference = FirebaseDatabase.getInstance().getReference("");
        FirebaseUser user = firebaseAuth.getCurrentUser();

        spinnerDay = findViewById(R.id.spinnerDay);
        ArrayAdapter<CharSequence> adapterDay = ArrayAdapter.createFromResource(this, R.array.days, android.R.layout.simple_spinner_item);
        adapterDay.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerDay.setAdapter(adapterDay);
        spinnerDay.setOnItemSelectedListener(this);
        spinnerDay.setPrompt("Select Day");

        spinnerStartTime = findViewById(R.id.spinnerStartTime);
        ArrayAdapter<CharSequence> adapterStartTime = ArrayAdapter.createFromResource(this, R.array.starttime, android.R.layout.simple_spinner_item);
        adapterStartTime.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerStartTime.setAdapter(adapterStartTime);
        spinnerStartTime.setOnItemSelectedListener(this);
        spinnerStartTime.setPrompt("Select Start Time");

        spinnerEndTime = findViewById(R.id.spinnerEndTime);
        ArrayAdapter<CharSequence> adapterEndTime = ArrayAdapter.createFromResource(this, R.array.endtime, android.R.layout.simple_spinner_item);
        adapterEndTime.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerEndTime.setAdapter(adapterEndTime);
        spinnerEndTime.setOnItemSelectedListener(this);
        spinnerEndTime.setPrompt("Select End Time");

        editTextSubjectId = findViewById(R.id.editTextSubjectId);
        spinnerCourseId = findViewById(R.id.spinnerCourseId);
        ArrayAdapter<CharSequence> adapterCourseId = ArrayAdapter.createFromResource(this, R.array.courseid, android.R.layout.simple_spinner_item);
        adapterCourseId.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCourseId.setAdapter(adapterCourseId);
        spinnerCourseId.setOnItemSelectedListener(this);
        spinnerEndTime.setPrompt("Select CourseId");

        userEmail = "test1@gmail.com";
        buttonCreateLecture = findViewById(R.id.buttonCreateLecture);

        buttonCreateLecture.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {

        if (v == buttonCreateLecture) {
            createLecture();
        }

    }

    private void createLecture() {

        LectureInformation lectureInformation = new LectureInformation();
        lectureInformation.day = spinnerDay.getSelectedItem().toString().trim();
        lectureInformation.courseId = spinnerCourseId.getSelectedItem().toString().trim();
        lectureInformation.startTime = spinnerStartTime.getSelectedItem().toString().trim();
        lectureInformation.endTime = spinnerEndTime.getSelectedItem().toString().trim();
        lectureInformation.subjectId = editTextSubjectId.getText().toString().trim();

        FirebaseUser user = firebaseAuth.getCurrentUser();

        DatabaseReference postReference = databaseReference.child("LectureDetails").child(user.getUid()).push();

        postReference.setValue(lectureInformation);

        Toast.makeText(this, "Information Saved", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
