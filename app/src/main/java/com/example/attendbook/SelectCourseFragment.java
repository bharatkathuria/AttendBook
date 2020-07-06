package com.example.attendbook;


import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.attendbook.ViewHolder.StudentAttendanceListHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;


/**
 * A simple {@link Fragment} subclass.
 */
public class SelectCourseFragment extends Fragment {


    private View view;
    private DatabaseReference databaseReference;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser user;
    private RecyclerView recyclerView;
    private AppCompatActivity appCompatActivity;
    private DatabaseReference coursePostReference;
    private ArrayList<String> courseList;
    private ArrayList<String> courseIdList;
    private TextView selectLabel;

    public SelectCourseFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_select_spinner, container, false);
        }

        courseList = new ArrayList<>();
        courseIdList = new ArrayList<>();

        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();

        Toolbar toolbar = view.findViewById(R.id.backToolbarSelectFragment);
        appCompatActivity = ((AppCompatActivity) getActivity());
        appCompatActivity.setSupportActionBar(toolbar);
        appCompatActivity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        appCompatActivity.getSupportActionBar().setDisplayShowHomeEnabled(true);

        selectLabel = view.findViewById(R.id.listLabel);
        selectLabel.setText("Select Course");
        databaseReference = FirebaseDatabase.getInstance().getReference("");

        recyclerView = view.findViewById(R.id.listRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getActivity()));
        final CustomArrayListAdapter customArrayListAdapter = new CustomArrayListAdapter(courseList, courseIdList, new CustomArrayListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view) {

                TextView v = (TextView) view;
                Bundle args = getArguments();
                args.putString("courseName", v.getText().toString());
                args.putString("courseId", v.getTag().toString());
                SelectSubjectFragment selectSubjectFragment = new SelectSubjectFragment();
                selectSubjectFragment.setArguments(args);
                getActivity()
                        .getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.main_content, selectSubjectFragment, "selectSubjectFragment")
                        .addToBackStack(null).commit();
            }
        });


        recyclerView.setAdapter(customArrayListAdapter);


        coursePostReference = databaseReference.child("CourseSubjectDetails");

        coursePostReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    DatabaseReference coursePostReference = databaseReference.child("CourseList").child(String.valueOf(postSnapshot.getKey()));
                    courseIdList.add(postSnapshot.getKey());
                    coursePostReference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            courseList.add(String.valueOf(dataSnapshot.getValue()));
                            customArrayListAdapter.notifyDataSetChanged();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                    Log.e("ItemClicked", "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        return view;
    }
}
