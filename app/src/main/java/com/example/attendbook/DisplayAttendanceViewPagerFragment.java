package com.example.attendbook;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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


/**
 * A simple {@link Fragment} subclass.
 */
public class DisplayAttendanceViewPagerFragment extends Fragment {


    public int NUM_PAGES;
    private ViewPager mPager;
    private String courseId;
    private String subjectId;
    private View view;
    private DatabaseReference databaseReference;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser user;
    private SectionsPagerAdapter mSectionsPagerAdapter;
    private PagerAdapter pagerAdapter;
    private AppCompatActivity appCompatActivity;
    private ArrayList<String> date;
    private String monthId;

    public DisplayAttendanceViewPagerFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_display_attendance_view_pager, container, false);
        }


        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
        date = new ArrayList<>();

        subjectId = getArguments().getString("subjectId");
        courseId = getArguments().getString("courseId");
        monthId = getArguments().getString("monthId");
        Toolbar toolbar = view.findViewById(R.id.backToolbarDisplayAttendanceFragment);

        appCompatActivity = ((AppCompatActivity) getActivity());
        appCompatActivity.setSupportActionBar(toolbar);
        appCompatActivity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        appCompatActivity.getSupportActionBar().setDisplayShowHomeEnabled(true);


        databaseReference = FirebaseDatabase.getInstance().getReference("");


        DatabaseReference savedPostReference = databaseReference.child("AttendanceDetails").child(courseId).child(subjectId).child(monthId);


        savedPostReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                NUM_PAGES = (int) dataSnapshot.getChildrenCount();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Log.e("ItemClicked", "" + postSnapshot.getKey());
                    date.add(postSnapshot.getKey());
                    HashMap<String, String> map = (HashMap<String, String>) postSnapshot.getValue();
                    Log.i("ItemClicked", map.toString());
                }

                mPager = view.findViewById(R.id.pager);
                pagerAdapter = new ScreenSlidePagerAdapter(getActivity().getSupportFragmentManager());
                mPager.setAdapter(pagerAdapter);
                mPager.setPageTransformer(true, new DepthPageTransformer());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        return view;
    }

    public String getMonth() {
        Calendar cal = Calendar.getInstance();
        String month = new SimpleDateFormat("MMM").format(cal.getTime());
        return month;
    }

    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
        public ScreenSlidePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            DisplayAttendances displayAttendances = new DisplayAttendances();
            Bundle args = getArguments();
            displayAttendances.setDate(date.get(position));
            displayAttendances.setArguments(args);
            return displayAttendances;

        }

        @Override
        public int getCount() {
            return NUM_PAGES;
        }
    }

}
