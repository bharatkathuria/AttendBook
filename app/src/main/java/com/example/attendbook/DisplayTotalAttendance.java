package com.example.attendbook;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;


/**
 * A simple {@link Fragment} subclass.
 */
public class DisplayTotalAttendance extends Fragment {

    private View view;
    private String courseName;
    private String subjectName;
    private String monthId;
    private String courseId;
    private String subjectId;
    private String monthName;
    private DatabaseReference databaseReference;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser user;
    private ArrayList<DisplayAttendanceModel> modelList;
    private RecyclerView recyclerView;
    private TextView dateTextView;
    private TextView subjectTextView;
    private TextView courseTextView;
    private TreeMap<String, DisplayAttendanceModel> studentMap;
    private AppCompatActivity appCompatActivity;

    public DisplayTotalAttendance() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if (view == null) {
            view = inflater.inflate(R.layout.fragment_display__total_attendance, container, false);

        }
        studentMap = new TreeMap<>(new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {

                if (o1.length() == o2.length()) {
                    return o1.compareTo(o2);
                } else {
                    if (o1.length() > o2.length()) {
                        return 1;
                    } else {
                        return -1;
                    }
                }
            }
        });
        modelList = new ArrayList<>();
        Toolbar toolbar = view.findViewById(R.id.backToolbarDisplayAttendanceFragment);

        appCompatActivity = ((AppCompatActivity) getActivity());
        appCompatActivity.setSupportActionBar(toolbar);
        appCompatActivity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        appCompatActivity.getSupportActionBar().setDisplayShowHomeEnabled(true);

        courseName = getArguments().getString("courseName");
        subjectName = getArguments().getString("subjectName");
        courseId = getArguments().getString("courseId");
        subjectId = getArguments().getString("subjectId");
        monthId = getArguments().getString("monthId");
        monthName = getArguments().getString("monthName");

        modelList = new ArrayList<>();

        dateTextView = view.findViewById(R.id.dateCell);
        subjectTextView = view.findViewById(R.id.subjectCell);
        courseTextView = view.findViewById(R.id.courseCell);
        dateTextView.setText(monthName);
        subjectTextView.setText(subjectName);
        courseTextView.setText(courseName);


        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();

        databaseReference = FirebaseDatabase.getInstance().getReference("");

        recyclerView = view.findViewById(R.id.displayAttendanceRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getActivity()));
        final DisplayAttendanceAdapter displayAttendanceAdapter = new DisplayAttendanceAdapter(modelList);
        recyclerView.setAdapter(displayAttendanceAdapter);

        DatabaseReference studentData = databaseReference.child("CourseStudentDetails").child("CourseID").child(courseId);
        Log.i("ItemClicked", "--------------------");


        studentData.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {

                    AttendanceListInformation attendanceListInformation = postSnapshot.getValue(AttendanceListInformation.class);


                    studentMap.put(attendanceListInformation.enrollmentNo, new DisplayAttendanceModel(attendanceListInformation.enrollmentNo, "0"));
                    Log.i("ItemClicked", attendanceListInformation.studentName);
                    Log.i("ItemClicked", attendanceListInformation.enrollmentNo);
                    Log.i("ItemClicked", "--------------------");
                }
                DatabaseReference savedPostReference = databaseReference.child("AttendanceDetails").child(courseId).child(subjectId).child(monthId);

                savedPostReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {

                            for (DataSnapshot childSnapshot : postSnapshot.getChildren()) {

                                int x = ((Long) childSnapshot.getValue()).intValue();
                                studentMap.get(childSnapshot.getKey()).increaseAttendanceCount(x);

                            }

                            HashMap<String, String> map = (HashMap<String, String>) postSnapshot.getValue();

                            Log.i("ItemClicked", map.toString());
                            Log.i("ItemClicked", studentMap.keySet().toString());

                        }

                        modelList.addAll(studentMap.values());
                        displayAttendanceAdapter.notifyDataSetChanged();

                        Log.i("ItemClicked", "aaaaaaaaaaaaa");

                    }


                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        return view;
    }

}
