package com.blue_red.bensinpriser;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;

import com.blue_red.bensinpriser.utils.CommonUtils;
import com.blue_red.bensinpriser.utils.DividerItemDecoration;

import java.util.ArrayList;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecViewActivity extends AppCompatActivity implements FuelStationAdapter.Callback {

    @BindView(R.id.mRecyclerView)
    RecyclerView mRecyclerView;
    FuelStationAdapter mFuelStationAdapter;
    private String jsondata;

    LinearLayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recview);
        ButterKnife.bind(this);
        setUp();
        Intent intent = getIntent();
        jsondata = intent.getExtras().getString("string");
    }

    private void setUp() {
        mLayoutManager = new LinearLayoutManager(this);
        mLayoutManager.setOrientation(RecyclerView.VERTICAL);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        Drawable dividerDrawable = ContextCompat.getDrawable(this, R.drawable.divider_drawable);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(dividerDrawable));
        mFuelStationAdapter = new FuelStationAdapter(new ArrayList<>());

        prepareContent();
    }

    private void prepareContent() {
        CommonUtils.showLoading(RecViewActivity.this);
        new Handler().postDelayed(() -> {
            //prepare data and show loading
            CommonUtils.hideLoading();
            ArrayList<FuelStation> mFuelStations = new ArrayList<>();
            StringBuilder sb = new StringBuilder(jsondata);
            for (int i = 0; i < sb.length(); i++) {
                if (sb.charAt(i) == '(') {
                    int counter = 1;
                    int j;
                    for (j = i + 1; counter != 0; j++) {
                        if (sb.charAt(j) == '(') {
                            counter++;
                        } else if (sb.charAt(j) == ')') {
                            counter--;
                        }
                    }

                    String qb98 = "type=bensin98, price=";
                    String qb95 = "type=bensin95, price=";
                    String qe85 = "type=ethanol85, price=";
                    String qdiesel = "type=diesel, price=";
                    String qlat = "latitude=";
                    String qlng = "longitude=";
                    String qname = "stationName=";
                    double b98 = -1;
                    double b95 = -1;
                    double diesel = -1;
                    double e85 = -1;
                    double lat = -1;
                    double lng = -1;
                    String url = "";
                    String name = "";
                    String tempvalue = "";

                    int pos;
                    //Bensin98
                    //http://www.java2s.com/Tutorials/Java/String/How_to_search_a_StringBuilder_from_start_or_from_the_end.htm
                    pos = sb.indexOf(qb98) + qb98.length();
                    for (int k = pos; sb.charAt(k) != ')'; k++) {
                        tempvalue += sb.charAt(k);
                    }
                    try {
                        b98 = Double.parseDouble(tempvalue);
                    } catch (Exception e) {
                        System.err.println(e);
                    }
                    tempvalue = "";
                    //Bensin 95
                    pos = sb.indexOf(qb95) + qb95.length();
                    for (int k = pos; sb.charAt(k) != ')'; k++) {
                        tempvalue += sb.charAt(k);
                    }
                    try {
                        b95 = Double.parseDouble(tempvalue);
                    } catch (Exception e) {
                        System.err.println(e);
                    }
                    tempvalue = "";
                    //Diesel
                    pos = sb.indexOf(qdiesel) + qdiesel.length();
                    for (int k = pos; sb.charAt(k) != ')'; k++) {
                        tempvalue += sb.charAt(k);
                    }
                    try {
                        diesel = Double.parseDouble(tempvalue);
                    } catch (Exception e) {
                        System.err.println(e);
                    }
                    tempvalue = "";
                    //E85
                    pos = sb.indexOf(qe85) + qe85.length();
                    for (int k = pos; sb.charAt(k) != ')'; k++) {
                        tempvalue += sb.charAt(k);
                    }
                    try {
                        e85 = Double.parseDouble(tempvalue);
                    } catch (Exception e) {
                        System.err.println(e);
                    }
                    tempvalue = "";
                    //Latitude
                    pos = sb.indexOf(qlat) + qlat.length();
                    for (int k = pos; sb.charAt(k) != ','; k++) {
                        tempvalue += sb.charAt(k);
                    }
                    try {
                        lat = Double.parseDouble(tempvalue);
                    } catch (Exception e) {
                        System.err.println(e);
                    }
                    tempvalue = "";
                    //longitude
                    pos = sb.indexOf(qlng) + qlng.length();
                    for (int k = pos; sb.charAt(k) != ','; k++) {
                        tempvalue += sb.charAt(k);
                    }
                    try {
                        lng = Double.parseDouble(tempvalue);
                    } catch (Exception e) {
                        System.err.println(e);
                    }
                    tempvalue = "";
                    //name
                    pos = sb.indexOf(qname) + qname.length();
                    for (int k = pos; sb.charAt(k) != ')'; k++) {
                        tempvalue += sb.charAt(k);
                    }
                    name = tempvalue;

                    //Testing via console outputs
                    /*
                    System.out.println("!!!!!!!!!!!!!!!!!!!ENTRY!!!!!!!!!!!!!!!!!");
                    System.out.println("NAME: " + name);
                    System.out.println("98: " + b98);
                    System.out.println("95: " + b95);
                    System.out.println("e85: " + e85);
                    System.out.println("diesel: " + diesel);
                    System.out.println("Lat: " + lat);
                    System.out.println("long: " + lng);
                    System.out.println("!!!!!!!!!!!!!!!!!!!OVER!!!!!!!!!!!!!!!!!");
*/
                    sb.delete(0, j);
                    i = 0;
                    String dist = String.valueOf(distanceCalc(lat, lng));
                    mFuelStations.add(new FuelStation("https://www.st1.se/skin/frontend/st1/st1web/images/logo.png", name, dist, name, b95, b98, diesel, e85, lat, lng));
                }
            }
            mFuelStationAdapter.addItems(mFuelStations);
            mRecyclerView.setAdapter(mFuelStationAdapter);
        }, 2000);
    }

    @Override
    public void onEmptyViewRetryClick() {
        prepareContent();
    }

    private float distanceCalc(double lat1, double lon1) {
        //https://stackoverflow.com/questions/2227292/how-to-get-latitude-and-longitude-of-the-mobile-device-in-android
        LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Location current = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        //Should implement a check for permission, has to be granted from settings by user as is

        //https://developer.android.com/reference/android/location/Location
        //https://www.programcreek.com/java-api-examples/?class=android.location.Location&method=distanceBetween
        float[] dist =new float[1];
        Location.distanceBetween(current.getLatitude(), current.getLongitude(), lat1, lon1, dist);


        System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!DISTANCE between " + lat1 + ", " + lon1 + " and " + current.getLatitude() + ", " + current.getLongitude() + ": " + dist[0]);
        return (dist[0]/1000);
    }
}