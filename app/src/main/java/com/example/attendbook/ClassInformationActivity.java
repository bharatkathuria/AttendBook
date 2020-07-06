package com.example.attendbook;

import android.Manifest;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.text.TextUtils;
import android.util.Log;
import android.view.ViewParent;
import android.widget.*;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingClient;
import com.google.android.gms.location.GeofencingEvent;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import androidx.core.app.ActivityCompat;
import androidx.core.view.MenuCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;

public class ClassInformationActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, View.OnClickListener {

    private static final String TAG = "MainActivity";
    private static final int REQUEST_LOCATION_PERMISSION_CODE = 101;
    /**
     * The {@link androidx.viewpager.widget.PagerAdapter} that will provide
     * fragments for e/ach of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * androidx.fragment.app.FragmentStatePagerAdapter.
     */

    private Button buttonSignin;
    private EditText editTextEmail;
    private EditText editTextPassword;
    private ProgressDialog progressDialog;
    private String tag = "LogIn";
    private FirebaseUser user;
    private GeofencingRequest geofencingRequest;
    private GoogleApiClient googleApiClient;
    private boolean isMonitoring = false;
    private GeofencingClient geofencingClient;
    private PendingIntent pendingIntent;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;

    /**
     * The {@link ViewPager} that will host the section contents.
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        firebaseAuth = FirebaseAuth.getInstance();

        if (firebaseAuth.getCurrentUser() == null) {
            setContentView(R.layout.activity_main);
            Log.d(tag, "checking Firebase Connection....");
            Log.d(tag, "Connected To firebase");

            progressDialog = new ProgressDialog(this);
            buttonSignin = findViewById(R.id.buttonLogin);
            editTextEmail = findViewById(R.id.editTextUserEmail);
            editTextPassword = findViewById(R.id.editTextUserPassword);

            buttonSignin.setOnClickListener(this);
        } else {
            setContentView(R.layout.activity_class_information);


            firebaseAuth = FirebaseAuth.getInstance();
            user = firebaseAuth.getCurrentUser();
            databaseReference = FirebaseDatabase.getInstance().getReference("");
            RelativeLayout relativeLayout = findViewById(R.id.main_content);
            FragmentTransaction t = getSupportFragmentManager().beginTransaction();
            TabsFragment tabsFragment = new TabsFragment();
            t.add(relativeLayout.getId(), tabsFragment, "myTabsFragment");
            t.commit();

        }


        googleApiClient = new GoogleApiClient.Builder(this).addApi(LocationServices.API).addConnectionCallbacks(this).addOnConnectionFailedListener(this).build();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, REQUEST_LOCATION_PERMISSION_CODE);
        }
        geofencingClient = LocationServices.getGeofencingClient(this);
    }

    @Override
    public void onClick(View v) {

        if (v == buttonSignin) {
            Log.d(tag, "Sign in button Clicked");
            userLogin();
        }
    }


    private void userLogin() {
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();
        Log.d(tag, "getting email and password");

        if (TextUtils.isEmpty(email)) {
            //email empty
            Log.d(tag, "Email is Empty");
            Toast.makeText(this, "Please Enter Email..", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(password)) {
            //ppassword empty
            Log.d(tag, "Password is Empty");
            Toast.makeText(this, "Please Enter Password..", Toast.LENGTH_SHORT).show();
            return;
        }

        progressDialog.setMessage("Signing In......");
        progressDialog.show();
        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                progressDialog.dismiss();
                if (task.isSuccessful()) {

                    //start profle activity
                    Log.d(tag, "Successfully Signin");
                    Toast.makeText(getApplicationContext(), "Success! SignIn Successfully", Toast.LENGTH_SHORT).show();
                    finish();
                    startActivity(new Intent(getApplicationContext(), ClassInformationActivity.class));

                } else {
                    Log.d(tag, "Log in Failed");
                    Toast.makeText(getApplicationContext(), "Failed! Please check your login info..", Toast.LENGTH_SHORT).show();

                }

            }
        });

    }

    private void startGeofencing() {
        Log.d(TAG, "Start geofencing monitoring call");
        pendingIntent = getGeofencePendingIntent();
        geofencingRequest = new GeofencingRequest.Builder()
                .setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER)
                .addGeofence(getGeofence())
                .build();

        if (!googleApiClient.isConnected()) {
            Log.d(TAG, "Google API client not connected");
        } else {
            geofencingClient.addGeofences(geofencingRequest, pendingIntent).addOnSuccessListener(this, new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Log.d(TAG, "Successfully Geofencing Connected");
                }
            })
                    .addOnFailureListener(this, new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            // Failed to add geofences
                            Log.d(TAG, "Failed to add Geofencing " + e.getMessage());
                        }
                    });

        }
        isMonitoring = true;
        invalidateOptionsMenu();
    }


    @NonNull
    private Geofence getGeofence() {


        return new Geofence.Builder()
                .setRequestId("IP_UNI")
                .setExpirationDuration(Geofence.NEVER_EXPIRE)
                .setCircularRegion(28.5946, 77.0184, 250)
                .setNotificationResponsiveness(30000)
                .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER | Geofence.GEOFENCE_TRANSITION_EXIT)
                .build();
    }

    private PendingIntent getGeofencePendingIntent() {
        if (pendingIntent != null) {
            return pendingIntent;
        }
        Intent intent = new Intent(this, GeofenceRegistrationService.class);
        return PendingIntent.getService(this, 0, intent, PendingIntent.
                FLAG_UPDATE_CURRENT);
    }

    private void stopGeoFencing() {
        pendingIntent = getGeofencePendingIntent();
        geofencingClient.removeGeofences(pendingIntent)
                .addOnSuccessListener(this, new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // Geofences removed
                        Log.d(TAG, "Stop geofencing");
                        // ...
                    }
                })
                .addOnFailureListener(this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Failed to remove geofences
                        Log.d(TAG, "Not stop geofencing");
                        // ...
                    }
                });
        isMonitoring = false;
        invalidateOptionsMenu();
    }


    @Override
    protected void onResume() {
        super.onResume();
        int response = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(ClassInformationActivity.this);
        if (response != ConnectionResult.SUCCESS) {
            Log.d(TAG, "Google Play Service Not Available");
            GoogleApiAvailability.getInstance().getErrorDialog(ClassInformationActivity.this, response, 1).show();
        } else {
            Log.d(TAG, "Google play service available");
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        googleApiClient.reconnect();
    }

    @Override
    protected void onStop() {
        super.onStop();
        googleApiClient.disconnect();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.d(TAG, "Google Api Client Connected");
        isMonitoring = true;
        startGeofencing();
        startLocationMonitor();

    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.d(TAG, "Google Connection Suspended");

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        isMonitoring = false;
        Log.e(TAG, "Connection Failed:" + connectionResult.getErrorMessage());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_class_information, menu);
        MenuCompat.setGroupDividerEnabled(menu, true);
        return true;
    }

    private void startLocationMonitor() {

        Log.d(TAG, "start Location Monitor");

        final LocationRequest locationRequest = new LocationRequest().create().setInterval(20000).setFastestInterval(10000).setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);


        try {

            LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {

                    Log.d(TAG, "Location Change Lat Lng " + location.getLatitude() + " " + location.getLongitude());


                }
            });

        } catch (SecurityException e) {
            Log.d(TAG, e.getMessage());
        }


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {

            SettingsFragment settingsFragment = new SettingsFragment();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.main_content, settingsFragment, "settingFragment")
                    .addToBackStack(null)
                    .commit();
            return true;
        }
        if (id == R.id.dailyreport) {

            Bundle args = new Bundle();
            args.putBoolean("total", false);
            SelectCourseFragment selectCourseFragment = new SelectCourseFragment();
            selectCourseFragment.setArguments(args);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.main_content, selectCourseFragment, "attendanceChartFragment")
                    .addToBackStack(null)
                    .commit();
            return true;


        }
        if (id == R.id.monthlyreport) {

            Bundle args = new Bundle();
            args.putBoolean("total", true);
            SelectCourseFragment selectCourseFragment = new SelectCourseFragment();
            selectCourseFragment.setArguments(args);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.main_content, selectCourseFragment, "attendanceChartFragment")
                    .addToBackStack(null)
                    .commit();
            return true;

//            DisplayTotalAttendance displayTotalAttendance = new DisplayTotalAttendance();
////            displayTotalAttendance.setArguments(args);
//
//
//            getSupportFragmentManager()
//                    .beginTransaction()
//                    .replace(R.id.main_content, displayTotalAttendance, "displayTotalAttendanceFragment")
//                    .addToBackStack(null).commit();
        }

        return super.onOptionsItemSelected(item);
    }


    public void onClickCurrentListItem(View view) {


    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onBackPressed() {
        StudentAttendanceListFragment studentAttendanceListFragment = (StudentAttendanceListFragment) getSupportFragmentManager().findFragmentByTag("studentAttendanceListFragment");
        if (studentAttendanceListFragment != null && studentAttendanceListFragment.isVisible() && !studentAttendanceListFragment.isSaveAttendance) {
            new AlertDialog.Builder(this)
                    .setTitle(R.string.dialogalerttitle)
                    .setMessage(R.string.doyou)
                    .setCancelable(false)
                    .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            ClassInformationActivity.super.onBackPressed();
                            Log.i("ItemClicked", "YES");
                        }
                    })
                    .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            Log.i("ItemClicked", "Cancel");
                        }
                    })
                    .show();
        } else {
            super.onBackPressed();
        }
    }


}
