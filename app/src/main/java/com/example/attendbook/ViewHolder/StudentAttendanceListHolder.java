package com.example.attendbook.ViewHolder;

import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.attendbook.R;

public class StudentAttendanceListHolder extends RecyclerView.ViewHolder {

    public Button incrementCountAttendance;
    public Button decrementCountAttendance;
    public TextView textViewCountAttendance;
    public TextView textViewName;
    public TextView textViewRollNo;
    public CheckBox checkBoxIsAttended;

    public StudentAttendanceListHolder(@NonNull View itemView) {
        super(itemView);
        textViewName = itemView.findViewById(R.id.itemName);
        textViewRollNo = itemView.findViewById(R.id.itemRollNo);
        checkBoxIsAttended = itemView.findViewById(R.id.itemCheckBox);
        textViewCountAttendance = itemView.findViewById(R.id.textViewCount);
        incrementCountAttendance = itemView.findViewById(R.id.plusButton);
        decrementCountAttendance = itemView.findViewById(R.id.minusButton);


    }
}
