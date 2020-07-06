package com.example.attendbook;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.text.DateFormatSymbols;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;


/**
 * A simple {@link Fragment} subclass.
 */
public class SelectMonthFragment extends Fragment {


    private View view;
    private DatabaseReference databaseReference;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser user;
    private RecyclerView recyclerView;
    private AppCompatActivity appCompatActivity;
    private DatabaseReference subjectPostReference;
    private ArrayList<String> monthList;
    private ArrayList<String> monthIdList;
    private TextView selectLabel;
    private String courseId;

    public SelectMonthFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_select_spinner, container, false);
        }

        monthIdList = new ArrayList<>();
        monthList = new ArrayList<>();

        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();

        Toolbar toolbar = view.findViewById(R.id.backToolbarSelectFragment);
        appCompatActivity = ((AppCompatActivity) getActivity());
        appCompatActivity.setSupportActionBar(toolbar);
        appCompatActivity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        appCompatActivity.getSupportActionBar().setDisplayShowHomeEnabled(true);

        selectLabel = view.findViewById(R.id.listLabel);
        selectLabel.setText("Select Month");

        databaseReference = FirebaseDatabase.getInstance().getReference("");


        DateFormatSymbols dateFormatSymbols = new DateFormatSymbols();
        String[] months = dateFormatSymbols.getMonths();
        String[] shortMonths = dateFormatSymbols.getShortMonths();
        monthList = new ArrayList<>();
        Collections.addAll(monthList, months);
        monthIdList = new ArrayList<>();
        Collections.addAll(monthIdList, shortMonths);


        recyclerView = view.findViewById(R.id.listRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getActivity()));


        final CustomArrayListAdapter customArrayListAdapter = new CustomArrayListAdapter(monthList, monthIdList, new CustomArrayListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view) {

                TextView v = (TextView) view;

                Bundle args = getArguments();
                args.putString("monthName", v.getText().toString());
                args.putString("monthId", v.getTag().toString());

                if (getArguments().getBoolean("total")) {

                    DisplayTotalAttendance displayTotalAttendance = new DisplayTotalAttendance();
                    displayTotalAttendance.setArguments(args);

                    getActivity()
                            .getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.main_content, displayTotalAttendance, "displayTotalAttendanceFragment")
                            .addToBackStack(null).commit();
                } else {

                    DisplayAttendanceViewPagerFragment displayAttendanceViewPagerFragment = new DisplayAttendanceViewPagerFragment();
                    displayAttendanceViewPagerFragment.setArguments(args);

                    getActivity()
                            .getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.main_content, displayAttendanceViewPagerFragment, "displayAttendanceFragment")
                            .addToBackStack(null).commit();

                }


            }
        });
        recyclerView.setAdapter(customArrayListAdapter);


        return view;
    }

}
