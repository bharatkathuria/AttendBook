package com.example.attendbook;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

public class SettingsFragment extends Fragment {

    private View view;
    private AppCompatActivity appCompatActivity;

    public SettingsFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_settings, container, false);
        }


        Toolbar toolbar = view.findViewById(R.id.backToolbarSelectFragment);
        appCompatActivity = ((AppCompatActivity) getActivity());
        appCompatActivity.setSupportActionBar(toolbar);
        appCompatActivity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        appCompatActivity.getSupportActionBar().setDisplayShowHomeEnabled(true);
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("com.example.attendbook", MODE_PRIVATE);
        Switch disableLocationSwitch = view.findViewById(R.id.disableLocationSwitch);
        disableLocationSwitch.setChecked(sharedPreferences.getBoolean("location", false));
        disableLocationSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                GeoFenceServiceStatus geoFenceServiceStatus = GeoFenceServiceStatus.getInstance();

                if (isChecked) {
                    SharedPreferences.Editor editor = getActivity().getSharedPreferences("com.example.attendbook", MODE_PRIVATE).edit();
                    editor.putBoolean("location", true);
                    editor.commit();
                    Log.i("ItemClicked", "true");
                    geoFenceServiceStatus.setLocationEnable(false);
                } else {
                    SharedPreferences.Editor editor = getActivity().getSharedPreferences("com.example.attendbook", MODE_PRIVATE).edit();
                    editor.putBoolean("location", false);
                    editor.commit();
                    Log.i("ItemClicked", "false");
                    geoFenceServiceStatus.setLocationEnable(true);
                }

            }
        });


        return view;
    }
}