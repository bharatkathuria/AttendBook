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
public class SelectSubjectFragment extends Fragment {


    private View view;
    private DatabaseReference databaseReference;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser user;
    private RecyclerView recyclerView;
    private AppCompatActivity appCompatActivity;
    private DatabaseReference subjectPostReference;
    private ArrayList<String> subjectList;
    private ArrayList<String> subjectIdList;
    private TextView selectLabel;
    private String courseId;

    public SelectSubjectFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_select_spinner, container, false);
        }

        courseId = getArguments().getString("courseId");
        subjectList = new ArrayList<>();
        subjectIdList = new ArrayList<>();
        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();

        Toolbar toolbar = view.findViewById(R.id.backToolbarSelectFragment);
        appCompatActivity = ((AppCompatActivity) getActivity());
        appCompatActivity.setSupportActionBar(toolbar);
        appCompatActivity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        appCompatActivity.getSupportActionBar().setDisplayShowHomeEnabled(true);

        selectLabel = view.findViewById(R.id.listLabel);
        selectLabel.setText("Select Subject");

        databaseReference = FirebaseDatabase.getInstance().getReference("");

        recyclerView = view.findViewById(R.id.listRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getActivity()));

        final CustomArrayListAdapter customArrayListAdapter = new CustomArrayListAdapter(subjectList, subjectIdList, new CustomArrayListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view) {

                TextView v = (TextView) view;

                Bundle args = getArguments();
                args.putString("subjectName", v.getText().toString());
                args.putString("subjectId", v.getTag().toString());
                SelectMonthFragment selectMonthFragment = new SelectMonthFragment();
                selectMonthFragment.setArguments(args);

                getActivity()
                        .getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.main_content, selectMonthFragment, "displayAttendanceFragment")
                        .addToBackStack(null).commit();
            }
        });
        recyclerView.setAdapter(customArrayListAdapter);


        Log.e("ItemClicked", "" + courseId);
        subjectPostReference = databaseReference.child("CourseSubjectDetails").child(courseId);

        subjectPostReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    subjectIdList.add(postSnapshot.getKey());
                    subjectList.add(String.valueOf(postSnapshot.getValue()));
                    customArrayListAdapter.notifyDataSetChanged();
                    Log.e("ItemClicked", "" + postSnapshot.getKey());
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        return view;
    }


}
