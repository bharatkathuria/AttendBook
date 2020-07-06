package com.example.attendbook;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class DisplayAttendanceAdapter extends RecyclerView.Adapter<DisplayAttendanceAdapter.DisplayAttendanceViewHolder> {


    private ArrayList<DisplayAttendanceModel> modelArrayList = new ArrayList<>();

    public DisplayAttendanceAdapter(ArrayList<DisplayAttendanceModel> modelArrayList) {
        Log.i("ItemClicked", "object created");
        this.modelArrayList = modelArrayList;
    }


    @NonNull
    @Override
    public DisplayAttendanceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.display_list_item_layout, parent, false);
        return new DisplayAttendanceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DisplayAttendanceViewHolder holder, int position) {
        holder.bindDisplayAttendanceModel(modelArrayList.get(position));
    }

    @Override
    public int getItemCount() {
        return modelArrayList.size();
    }

    public class DisplayAttendanceViewHolder extends RecyclerView.ViewHolder {

        public TextView nameTextView;
        public TextView attendanceCountTextView;

        public DisplayAttendanceViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.itemDisplayName);
            attendanceCountTextView = itemView.findViewById(R.id.itemAttendanceCount);
        }

        public void bindDisplayAttendanceModel(DisplayAttendanceModel displayAttendanceModel) {

            nameTextView.setText(displayAttendanceModel.getName());
            attendanceCountTextView.setText(displayAttendanceModel.getAttendanceCount() + "");
        }
    }

}
