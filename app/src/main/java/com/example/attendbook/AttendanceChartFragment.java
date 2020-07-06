package com.example.attendbook;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.google.firebase.FirebaseError;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class AttendanceChartFragment extends Fragment {


    private String courseId;
    private String subjectId;
    private View view;
    private DatabaseReference databaseReference;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser user;
    private TableLayout tableLayout;
    private AppCompatActivity appCompatActivity;
    //  private List<HashMap<String, String>> listOfMaps;

    public AttendanceChartFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_attendance_chart, container, false);
        }

        tableLayout = view.findViewById(R.id.AttendanceDisplayTable);


        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();

//        listOfMaps = new ArrayList<HashMap<String, String>>();


        databaseReference = FirebaseDatabase.getInstance().getReference("");

//        DatabaseReference postReference= databaseReference.child("CourseStudentDetails").child("CourseID").child(courseId);


        DatabaseReference savedPostReference = databaseReference.child("AttendanceDetails").child("MCA_3").child("IT-106").child(user.getUid());


        savedPostReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Log.e("ItemClicked", "" + postSnapshot.getKey());
                    HashMap<String, String> map = (HashMap<String, String>) postSnapshot.getValue();
                    Log.i("ItemClicked", map.toString());
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        return view;
    }

}
