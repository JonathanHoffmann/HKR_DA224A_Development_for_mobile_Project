package com.blue_red.bensinpriser;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import java.util.ArrayList;

public class PriceView extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_price_view);

        ArrayList<RecyclerItem> exampleList = new ArrayList<>();
        exampleList.add(new RecyclerItem("https://www.din-x.se/wp-content/themes/dinx/images/dinx_logo.jpg", "Line 1", "Line 2"));
        exampleList.add(new RecyclerItem("https://resources.mynewsdesk.com/image/upload/c_limit,dpr_1.0,f_auto,h_700,q_auto,w_600/pt1kjlgibwc1foix728o.jpg", "Line 3", "Line 4"));
        exampleList.add(new RecyclerItem("https://139886-434170-raikfcquaxqncofqfm.stackpathdns.com/wp-content/uploads/2017/02/okq8-logo-2-1024x285.png", "Line 5", "Line 6"));
        mRecyclerView = findViewById(R.id.RecyclerView);
        mLayoutManager = new LinearLayoutManager(this);
        mAdapter = new recyclerAdapter(exampleList);

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
    }
}
