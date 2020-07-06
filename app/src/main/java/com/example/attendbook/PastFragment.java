package com.example.attendbook;


import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.attendbook.ViewHolder.ViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;


/**
 * A simple {@link Fragment} subclass.
 */
public class PastFragment extends Fragment {


    FirebaseRecyclerOptions<LectureInformation> options;
    FirebaseRecyclerAdapter<LectureInformation, ViewHolder> adapter;
    private View view;
    private DatabaseReference databaseReference;
    private FirebaseAuth firebaseAuth;
    private RecyclerView recyclerView;

    public PastFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_past, container, false);
        }


        recyclerView = view.findViewById(R.id.pastRecyclerView);
        firebaseAuth = FirebaseAuth.getInstance();

        FirebaseUser user = firebaseAuth.getCurrentUser();
        String yesterday = getYesterDay();
        databaseReference = FirebaseDatabase.getInstance().getReference("");
        options = new FirebaseRecyclerOptions.Builder<LectureInformation>().setQuery(databaseReference.child("LectureDetails").child(user.getUid()).orderByChild("day").equalTo(yesterday), LectureInformation.class).build();

        adapter = new FirebaseRecyclerAdapter<LectureInformation, ViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull final ViewHolder holder, int position, @NonNull LectureInformation model) {
                holder.textViewCourse.setTag(model.getCourseId());
                holder.textViewStartTime.setText(model.getStartTime());
                holder.textViewSubject.setTag(model.getSubjectId());
                holder.textViewEndTime.setText(model.getEndTime());
                holder.textViewLocation.setText(model.getLocation());
                DatabaseReference coursePostReference = databaseReference.child("CourseList").child(model.getCourseId());
                coursePostReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        holder.textViewCourse.setText(String.valueOf(dataSnapshot.getValue()));
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
                DatabaseReference subjectPostReference = databaseReference.child("SubjectList").child(model.getSubjectId());
                subjectPostReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        holder.textViewSubject.setText(String.valueOf(dataSnapshot.getValue()));
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }

            @NonNull
            @Override
            public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_row, parent, false);
                final TextView course = view.findViewById(R.id.textViewCourse);
                final TextView subject = view.findViewById(R.id.textViewSubject);
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        GeoFenceServiceStatus geoFenceServiceStatus = GeoFenceServiceStatus.getInstance();
                        boolean isInside = geoFenceServiceStatus.isInside();
                        if (isInside) {
                            Log.i("ItemClicked", "Yes is inside");
                        } else {
                            Log.i("ItemClicked", "No is inside");
                        }
                        Log.i("ItemClicked", course.getText().toString());

                        StudentAttendanceListFragment studentAttendanceListFragment = new StudentAttendanceListFragment();
                        Bundle args = new Bundle();
                        args.putString("courseId", course.getTag().toString());
                        args.putString("subjectId", subject.getTag().toString());
                        studentAttendanceListFragment.setArguments(args);
                        getActivity().getSupportFragmentManager().beginTransaction()
                                .replace(R.id.main_content, studentAttendanceListFragment, "studentAttendanceListFragment")
                                .addToBackStack(null)
                                .commit();


                    }
                });
                return new ViewHolder(view);
            }
        };
        adapter.startListening();
        LinearLayoutManager layout = new LinearLayoutManager(this.getActivity());
        recyclerView.setLayoutManager(layout);
        recyclerView.setAdapter(adapter);
        return view;

    }

    private String getYesterDay() {

        String[] days = {"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};

        Calendar calendar = Calendar.getInstance();
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        if (dayOfWeek == 1) {
            dayOfWeek = 8;
        }
        return days[dayOfWeek - 2];
    }


}

