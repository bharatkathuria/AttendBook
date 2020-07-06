package com.example.attendbook;


import android.os.Bundle;

import androidx.annotation.NonNull;
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
import java.util.HashMap;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class DisplayAttendances extends Fragment {

    private View view;
    private String courseName;
    private String subjectName;
    private String monthId;
    private String courseId;
    private String subjectId;
    private String date;
    private DatabaseReference databaseReference;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser user;
    private ArrayList<DisplayAttendanceModel> modelList;
    private RecyclerView recyclerView;
    private TextView dateTextView;
    private TextView subjectTextView;
    private TextView courseTextView;

    public DisplayAttendances() {
        // Required empty public constructor
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if (view == null) {
            view = inflater.inflate(R.layout.fragment_display_attendances, container, false);

        }

        courseName = getArguments().getString("courseName");
        subjectName = getArguments().getString("subjectName");
        courseId = getArguments().getString("courseId");
        subjectId = getArguments().getString("subjectId");
        date = getDate();
        monthId = getArguments().getString("monthId");


        modelList = new ArrayList<>();

        dateTextView = view.findViewById(R.id.dateCell);
        subjectTextView = view.findViewById(R.id.subjectCell);
        courseTextView = view.findViewById(R.id.courseCell);
        dateTextView.setText(date);
        subjectTextView.setText(subjectName);
        courseTextView.setText(courseName);


        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();

        databaseReference = FirebaseDatabase.getInstance().getReference("");

        recyclerView = view.findViewById(R.id.displayAttendanceRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getActivity()));
        final DisplayAttendanceAdapter displayAttendanceAdapter = new DisplayAttendanceAdapter(modelList);
        recyclerView.setAdapter(displayAttendanceAdapter);

        DatabaseReference savedPostReference = databaseReference.child("AttendanceDetails").child(courseId).child(subjectId).child(monthId).child(date);


        savedPostReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {


                    modelList.add(new DisplayAttendanceModel(postSnapshot.getKey(), String.valueOf(postSnapshot.getValue())));
                    Log.e("ItemClicked", "" + postSnapshot.getKey());
                    String temp = postSnapshot.getKey();
//                    HashMap<String,String> map = ( HashMap<String,String>)postSnapshot.getValue();
                    Log.i("ItemClicked", temp);
                }

                displayAttendanceAdapter.notifyDataSetChanged();
                Log.i("ItemClicked", "aaaaaaaaaaaaa");

            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        // Inflate the layout for this fragment
        return view;
    }

}
