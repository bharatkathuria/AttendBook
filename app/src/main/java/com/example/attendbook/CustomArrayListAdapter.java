package com.example.attendbook;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class CustomArrayListAdapter extends RecyclerView.Adapter<CustomArrayListAdapter.ViewHolder> {

    private OnItemClickListener listener;
    private ArrayList<String> stringList = new ArrayList<>();
    private ArrayList<String> stringTagList = new ArrayList<>();

    public CustomArrayListAdapter(ArrayList<String> stringList, ArrayList<String> stringTagList, OnItemClickListener listener) {

        this.stringList = stringList;
        this.stringTagList = stringTagList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull final ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_string_row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.bind(stringList.get(position), stringTagList.get(position), listener);

    }

    @Override
    public int getItemCount() {

        return stringList.size();
    }

    public interface OnItemClickListener {
        void onItemClick(View view);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView textViewString;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewString = itemView.findViewById(R.id.textViewString);
        }

        public void bind(String s, String tag, OnItemClickListener onClickListener) {
            textViewString.setText(s);
            textViewString.setTag(tag);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(textViewString);
                }
            });

        }


    }
}
