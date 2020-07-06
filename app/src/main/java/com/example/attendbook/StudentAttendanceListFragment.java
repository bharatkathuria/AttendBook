package com.example.attendbook;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
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
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.attendbook.ViewHolder.StudentAttendanceListHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;
import java.util.PriorityQueue;
import java.util.Timer;
import java.util.TimerTask;

import cn.pedant.SweetAlert.SweetAlertDialog;


/**
 * A simple {@link Fragment} subclass.
 */
public class StudentAttendanceListFragment extends Fragment {


    boolean isSaveAttendance;
    FirebaseRecyclerOptions<AttendanceListInformation> options;
    FirebaseRecyclerAdapter<AttendanceListInformation, StudentAttendanceListHolder> adapter;
    private String courseId;
    private String subjectId;
    private View view;
    private DatabaseReference databaseReference;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser user;
    private RecyclerView recyclerView;
    private AppCompatActivity appCompatActivity;
    private HashMap<String, Integer> map;
    private DatabaseReference postReference;

    public StudentAttendanceListFragment() {

        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             final Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_student_attendance_list, container, false);
        }


        courseId = getArguments().getString("courseId");
        subjectId = getArguments().getString("subjectId");
        Log.i("ItemClicked", courseId);
        recyclerView = view.findViewById(R.id.studentListRecyclerView);
        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
        map = new HashMap<String, Integer>();
        Toolbar toolbar = view.findViewById(R.id.backtoolbar);
        appCompatActivity = ((AppCompatActivity) getActivity());


        appCompatActivity.setSupportActionBar(toolbar);
        appCompatActivity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        appCompatActivity.getSupportActionBar().setDisplayShowHomeEnabled(true);


        databaseReference = FirebaseDatabase.getInstance().getReference("");

        postReference = databaseReference.child("CourseStudentDetails").child("CourseID").child(courseId);
        options = new FirebaseRecyclerOptions.Builder<AttendanceListInformation>().setQuery(postReference, AttendanceListInformation.class).build();

        adapter = new FirebaseRecyclerAdapter<AttendanceListInformation, StudentAttendanceListHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull final StudentAttendanceListHolder holder, final int position, @NonNull final AttendanceListInformation model) {

                holder.checkBoxIsAttended.setTag(model.getEnrollmentNo());
                holder.textViewName.setText(model.getStudentName());
                holder.textViewRollNo.setText(model.getEnrollmentNo());
                holder.textViewCountAttendance.setText("1");

            }

            @NonNull
            @Override
            public StudentAttendanceListHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                View view;
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_layout, parent, false);
                Button plusButton = view.findViewById(R.id.plusButton);
                Button minusButton = view.findViewById(R.id.minusButton);
                final TextView count = view.findViewById(R.id.textViewCount);
                CheckBox cb = view.findViewById(R.id.itemCheckBox);
                cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        String s = (String) buttonView.getTag();
                        Log.i("ItemClicked", s);
                        if (isChecked) {
                            int countA = Integer.parseInt(count.getText().toString());
                            map.put(s, countA);
                        } else {
                            map.remove(s);
                        }

                    }
                });

                plusButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        int t = Integer.parseInt(count.getText().toString());
                        if (t < 3) {
                            t++;
                            count.setText("" + t);
                        } else {
                            Toast.makeText(getActivity(), "Maximum value 3 reached!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                minusButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        int t = Integer.parseInt(count.getText().toString());
                        if (t > 1) {
                            t--;
                            count.setText("" + t);
                        } else {
                            Toast.makeText(getActivity(), "Miniimum value 1 reached!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                return new StudentAttendanceListHolder(view);

            }


        };

        Button saveAttendanceButton = view.findViewById(R.id.buttonSaveAttendance);
        saveAttendanceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                GeoFenceServiceStatus geoFenceServiceStatus = GeoFenceServiceStatus.getInstance();
                if (geoFenceServiceStatus.isLocationEnable()) {
                    Log.i("ItemClicked", "Location Enalbe: Yes");
                    if (!geoFenceServiceStatus.isInside()) {

                        Log.i("ItemClicked", "Not Inside University Premesis");
                        AlertDialog.Builder alertBox = new AlertDialog.Builder(getActivity());
                        alertBox.setTitle("Location Error!")
                                .setMessage("Not Inside University Premises !!")
                                .setPositiveButton("OK", null).setCancelable(true).create().show();
                        return;
                    } else {
                        Log.i("ItemClicked", "Inside");
                    }


                } else {
                    Log.i("ItemClicked", "Location Enalbe: No");
                }
                Log.i("ItemClicked", "Inside University Premesis");
                Log.i("ItemClicked", courseId);
                Log.i("ItemClicked", subjectId);
                DatabaseReference savePostReference = databaseReference.child("AttendanceDetails").child(courseId).child(subjectId).child(getMonth()).child(getDate());
                savePostReference.setValue(map);
                if (map.isEmpty()) {
                    Log.i("ItemClicked", "Empty");
                } else {
                    Log.i("ItemClicked", map.toString());
                }

                isSaveAttendance = true;
                Log.i("ItemClicked", "Attendance Saved Successfully");
                final SweetAlertDialog sweetAlertDialog = new SweetAlertDialog(getActivity(), SweetAlertDialog.SUCCESS_TYPE);
                sweetAlertDialog.setTitleText("Success !")
                        .setContentText("Attendance Saved Successfully")
                        .show();

                final Timer t = new Timer();
                t.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        sweetAlertDialog.dismiss();
                        t.cancel();
                    }
                }, 3000);

                getActivity().onBackPressed();
            }
        });
        adapter.startListening();
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getActivity()));
        recyclerView.setAdapter(adapter);
        return view;
    }

    public String getDate() {

        String date = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
        return date;
    }

    public String getMonth() {
        Calendar cal = Calendar.getInstance();
        String month = new SimpleDateFormat("MMM").format(cal.getTime());
        return month;
    }

}
