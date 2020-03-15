package com.blue_red.bensinpriser;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;


public class DetailViewActivity extends AppCompatActivity {
    private FuelStation fuelStation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_view);
        Intent intent = this.getIntent();
        Bundle bundle = intent.getExtras();
        fuelStation = (FuelStation) bundle.getSerializable("Station");
        TextView TVtitle = findViewById(R.id.TVtitle);
        TVtitle.setText(fuelStation.getTitle());
    }
}
