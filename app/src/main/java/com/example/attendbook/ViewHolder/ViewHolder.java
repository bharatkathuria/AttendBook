package com.example.attendbook.ViewHolder;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.attendbook.R;

public class ViewHolder extends RecyclerView.ViewHolder {

    public TextView textViewCourse;
    public TextView textViewSubject;
    public TextView textViewStartTime;
    public TextView textViewEndTime;
    public TextView textViewLocation;

    public ViewHolder(@NonNull View itemView) {
        super(itemView);
        textViewCourse = itemView.findViewById(R.id.textViewCourse);
        textViewSubject = itemView.findViewById(R.id.textViewSubject);
        textViewStartTime = itemView.findViewById(R.id.textViewStartTime);
        textViewEndTime = itemView.findViewById(R.id.textViewEndTime);
        textViewLocation = itemView.findViewById(R.id.textViewLocation);
    }
}
