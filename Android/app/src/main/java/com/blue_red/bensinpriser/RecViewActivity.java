package com.blue_red.bensinpriser;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.blue_red.bensinpriser.utils.CommonUtils;
import com.blue_red.bensinpriser.utils.DividerItemDecoration;

import java.util.ArrayList;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import butterknife.BindView;
import butterknife.ButterKnife;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

public class RecViewActivity extends AppCompatActivity implements FuelStationAdapter.Callback {

    @BindView(R.id.mRecyclerView)
    RecyclerView mRecyclerView;
    FuelStationAdapter mFuelStationAdapter;
    private String jsondata;
    private SeekBar seekBar;
    private TextView distTV;
    private int distCheck = 10;
    private Spinner spinnerFuel;
    private Spinner spinnerSort;
    private String sort = "Pris Asc";
    private String controlsort;
    private String fuel = "Bensin 95";
    private String controlfuel;
    private final int REQUEST_LOCATION_PERMISSION = 1;


    LinearLayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recview);
        ButterKnife.bind(this);
        setUp();
        Intent intent = getIntent();
        jsondata = intent.getExtras().getString("string");
        seekBar = findViewById(R.id.seekBar);
        distTV = findViewById(R.id.disttextView);
        distTV.setText("Distance: " + distCheck + "km");
        //https://stackoverflow.com/questions/13377361/how-to-create-a-drop-down-list
        spinnerFuel = findViewById(R.id.FuelSpinner);
//create a list of items for the spinner.
        String[] items = new String[]{"Bensin 95", "Bensin 98", "Diesel", "Ethanol 85"};
//create an adapter to describe how the items are displayed, adapters are used in several places in android.
//There are multiple variations of this, but this is the basic variant.
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
//set the spinners adapter to the previously created one.
        spinnerFuel.setAdapter(adapter);
        spinnerFuel.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                controlfuel = (String) spinnerFuel.getSelectedItem();
                if(!controlfuel.equals(fuel))
                {
                    fuel=controlfuel;
                    setUp();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        spinnerSort = findViewById(R.id.SortSpinner);
        String[] sortitems = new String[]{"Pris Asc", "Pris Desc", "Distans Asc", "Distans Desc"};
        ArrayAdapter<String> a = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, sortitems);
        spinnerSort.setAdapter(a);
        spinnerSort.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                controlsort = (String) spinnerSort.getSelectedItem();
                if(!controlsort.equals(sort))
                {
                    sort=controlsort;
                    setUp();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        //https://abhiandroid.com/ui/seekbar
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                distCheck = progress;
            }

            public void onStartTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
                /*Toast.makeText(RecViewActivity.this, "Seek bar progress is :" + progressChangedValue,
                        Toast.LENGTH_SHORT).show();*/
                distTV.setText("Distance: " + distCheck + "km");
                setUp();

            }
        });
    }

    private void setUp() {
        mLayoutManager = new LinearLayoutManager(this);
        mLayoutManager.setOrientation(RecyclerView.VERTICAL);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        Drawable dividerDrawable = ContextCompat.getDrawable(this, R.drawable.divider_drawable);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(dividerDrawable));
        mFuelStationAdapter = new FuelStationAdapter(fuel, new ArrayList<>());

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
                    float dist = distanceCalc(lat, lng);

                    double pricetemp;
                    if (fuel.equals("Bensin 95")) {
                        pricetemp = b95;
                    } else if (fuel.equals("Bensin 98")) {
                        pricetemp = b98;
                    } else if (fuel.equals("Diesel")) {
                        pricetemp = diesel;
                    } else if (fuel.equals("Ethanol 85")) {
                        pricetemp = e85;
                    } else {
                        pricetemp = -1;
                    }
                    if (dist <= distCheck && pricetemp >= 0) {
                        mFuelStations.add(new FuelStation("https://www.st1.se/skin/frontend/st1/st1web/images/logo.png", name, b95, b98, diesel, e85, lat, lng, dist));
                    }
                }
            }

            ArrayList<FuelStation> tempstations = new ArrayList<>();
            if (sort.equals("Pris Asc") || sort.equals("Pris Desc")) {
                int temp = mFuelStations.size();
                for (int i = 0; i < temp; i++) {
                    double low = 999999999;
                    int pos = 0;
                    for (int j = 0; j < mFuelStations.size(); j++) {
                        double pricetemp;
                        if (fuel.equals("Bensin 95")) {
                            pricetemp = mFuelStations.get(j).getmBensin95();
                        } else if (fuel.equals("Bensin 98")) {
                            pricetemp = mFuelStations.get(j).getmBensin98();
                        } else if (fuel.equals("Diesel")) {
                            pricetemp = mFuelStations.get(j).getmDiesel();
                        } else if (fuel.equals("Ethanol 85")) {
                            pricetemp = mFuelStations.get(j).getmEthanol85();
                        } else {
                            pricetemp = -1;
                        }

                        if (pricetemp < low) {
                            low = pricetemp;
                            pos = j;
                        }
                    }
                    tempstations.add(mFuelStations.get(pos));
                    mFuelStations.remove(pos);
                }
                if (sort.equals("Pris Desc")) {
                    ArrayList<FuelStation> tempstation2 = new ArrayList<>(tempstations);
                    tempstations.removeAll(tempstations);
                    for (int i = tempstation2.size() - 1; i >= 0; i--) {
                        System.out.println(i);
                        tempstations.add(tempstation2.get(i));
                    }
                }
            } else {
                int temp = mFuelStations.size();
                for (int i = 0; i < temp; i++) {
                    double high = 0;
                    int pos = 0;
                    for (int j = 0; j < mFuelStations.size(); j++) {
                        if (mFuelStations.get(j).getmDistance()>high) {
                            high = mFuelStations.get(j).getmDistance();
                            pos = j;
                        }
                    }
                    tempstations.add(mFuelStations.get(pos));
                    mFuelStations.remove(pos);
                }
                if(sort.equals("Distans Asc"))
                {
                    ArrayList<FuelStation> tempstation2 = new ArrayList<>(tempstations);
                    tempstations.removeAll(tempstations);
                    for (int i = tempstation2.size() - 1; i >= 0; i--) {
                        System.out.println(i);
                        tempstations.add(tempstation2.get(i));
                    }
                }
            }

            mFuelStations = tempstations;
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


        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    Activity#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for Activity#requestPermissions for more details.
            requestLocationPermission();
        }
        Location current = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        //Should implement a check for permission, has to be granted from settings by user as is

        //https://developer.android.com/reference/android/location/Location
        //https://www.programcreek.com/java-api-examples/?class=android.location.Location&method=distanceBetween
        float[] dist = new float[1];
        Location.distanceBetween(current.getLatitude(), current.getLongitude(), lat1, lon1, dist);


        //System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!DISTANCE between " + lat1 + ", " + lon1 + " and " + current.getLatitude() + ", " + current.getLongitude() + ": " + dist[0]);
        return (dist[0] / 1000);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        // Forward results to EasyPermissions
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @AfterPermissionGranted(REQUEST_LOCATION_PERMISSION)
    public void requestLocationPermission() {
        String[] perms = {Manifest.permission.ACCESS_FINE_LOCATION};
        if (EasyPermissions.hasPermissions(this, perms)) {
            Toast.makeText(this, "Permission already granted", Toast.LENGTH_SHORT).show();
        } else {
            EasyPermissions.requestPermissions(this, "Please grant the location permission", REQUEST_LOCATION_PERMISSION, perms);
        }
    }
}

