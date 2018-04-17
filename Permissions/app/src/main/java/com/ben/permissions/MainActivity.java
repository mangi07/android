package com.ben.permissions;

import android.Manifest;
import android.app.Dialog;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

/* Modified from CS 496 Module 7 material on permissions */
public class MainActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    private final int REQUEST_CODE_ASK_PERMISSION = 10;
    private GoogleApiClient mGoogleApiClient = null;
    private LocationRequest mLocationRequest = null;
    private Location mLastLocation = null;
    private LocationListener mLocationListener = null;

    private SQLiteLocation mSQLiteLocation;

    private boolean checkPermission() {
        // We want to make sure both permissions are granted
        if ((ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) && (
                ContextCompat.checkSelfPermission(this,
                        Manifest.permission.ACCESS_COARSE_LOCATION)
                        == PackageManager.PERMISSION_GRANTED
        )) {
            Log.i("**TEST**", "Permission granted");
            return true;
        } else {
            Log.i("**TEST**", "DENIED");
            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION
            }, REQUEST_CODE_ASK_PERMISSION);
            return false;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_ASK_PERMISSION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted
                    Log.i("**TEST**", "In onRequestPermissionsResult: Permission granted!");
                    updateLocation();
                } else {
                    // permission denied
                    Log.i("**TEST**", "In onRequestPermissionsResult: Permission DENIED!");
                    /* In the event the user denied location access the latitude and longitude
                    should be set to 44.5 and 123.2 respectively, the coordinates of OSU. */
                    manageData(44.5, 123.2);
                }
                return;
            }
            default:
                Log.i("**TEST**", "In onRequestPermissionsResult: switch default");
                //debugToast("In onRequestPermissionsResult: switch default");
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }
        mLocationRequest = LocationRequest.create();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(5000);
        mLocationRequest.setFastestInterval(5000);
        mLocationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                if (location != null) {
                    mLastLocation = location;
                    //mLonText.setText(String.valueOf(location.getLongitude()));
                    Log.i("**TEST**", "in onLocationChanged, Longitude " +
                            String.valueOf(location.getLongitude()));
                    Log.i("**TEST**", "in onLocationChanged, Latitude" +
                            String.valueOf(location.getLatitude()));
                } else {
                    Log.i("**TEST**", "in onLocationChanged, no location available");
                }
            }
        };

        /* DATABASE STUFF */
        mSQLiteLocation = new SQLiteLocation(this);

        Log.i("**TEST**", "end of method onCreate");
    }

    @Override
    protected void onStart() {
        mGoogleApiClient.connect();
        Log.i("**TEST**", "in onStart");
        super.onStart();
        mSQLiteLocation.show();
    }

    @Override
    protected void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        // TODO: test that we are okay without the following line
        // if (checkPermission()) updateLocation();
    }

    public void recordButton(View view) {
        updateLocation();
    }

    private void updateLocation() {
        if (!checkPermission()) {
            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(
                mGoogleApiClient, mLocationRequest, mLocationListener);
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (mLastLocation != null) {
            Log.i("**TEST**", "Lat in updateLocation: " +
                    String.valueOf(mLastLocation.getLatitude()));
            Log.i("**TEST**", "Long in updateLocation: " +
                    String.valueOf(mLastLocation.getLongitude()));
            manageData(mLastLocation.getLatitude(), mLastLocation.getLongitude());
        } else {
            Log.i("**TEST**", "FusedLocationApi");
        }
    }

    /* Gets and saves text and location in database. */
    private void manageData(double latitude, double longitude) {
        TextView latView = (TextView) findViewById(R.id.Latitude);
        latView.setText("Latitude: " + Double.toString(latitude));

        TextView longView = (TextView) findViewById(R.id.Longitude);
        longView.setText("Longitude: " + Double.toString(longitude));

        // get text
        EditText text = (EditText) findViewById(R.id.text);

        // record to database and update view
        SQLiteLocation sqLiteLocation = new SQLiteLocation(this);
        sqLiteLocation.record(text.getText().toString(), latitude, longitude);
        sqLiteLocation.show();

        // inform user
        debugToast("text: " + text.getText().toString() + ", latitude: " + latitude +
                " and longitude: " + longitude + " recorded to database.");
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Dialog errDialog = GoogleApiAvailability.getInstance().getErrorDialog(this,
                connectionResult.getErrorCode(), 0);
        errDialog.show();
    }

    private void debugToast(String s) {
        Toast.makeText(this, s, Toast.LENGTH_LONG).show();
    }


}

