package com.example.attendbook;


import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
public class UpcomingFragment extends Fragment {


    FirebaseRecyclerOptions<LectureInformation> options;
    FirebaseRecyclerAdapter<LectureInformation, ViewHolder> adapter;
    private View view;
    private DatabaseReference databaseReference;
    private FirebaseAuth firebaseAuth;
    private RecyclerView recyclerView;

    public UpcomingFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_upcoming, container, false);
        }


        recyclerView = view.findViewById(R.id.upcomingRecyclerView);
        firebaseAuth = FirebaseAuth.getInstance();

        FirebaseUser user = firebaseAuth.getCurrentUser();
        String tomorrow = getTomorrow();
        databaseReference = FirebaseDatabase.getInstance().getReference("");
        options = new FirebaseRecyclerOptions.Builder<LectureInformation>().setQuery(databaseReference.child("LectureDetails").child(user.getUid()).orderByChild("day").equalTo(tomorrow), LectureInformation.class).build();
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
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_row, parent, false);
                return new ViewHolder(view);
            }
        };
        adapter.startListening();
        LinearLayoutManager layout = new LinearLayoutManager(this.getActivity());
        recyclerView.setLayoutManager(layout);
        recyclerView.setAdapter(adapter);
        return view;
    }

    private String getTomorrow() {

        String[] days = {"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};

        Calendar calendar = Calendar.getInstance();
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        if (dayOfWeek == 7) {
            dayOfWeek = 0;
        }
        return days[dayOfWeek];
    }


}
