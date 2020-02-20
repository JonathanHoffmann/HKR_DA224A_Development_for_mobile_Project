package com.blue_red.bensinpriser;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.widget.ImageView;

import java.io.InputStream;
import java.net.URL;

public class testurlpic extends AppCompatActivity {

    private String url = "https://139886-434170-raikfcquaxqncofqfm.stackpathdns.com/wp-content/uploads/2017/02/okq8-logo-2-1024x285.png";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_testurlpic);
        ImageView iv = findViewById(R.id.iv);
        iv.setImageDrawable(LoadImageFromWebOperations(url));

    }
    //https://stackoverflow.com/questions/6407324/how-to-display-image-from-url-on-android
    public static Drawable LoadImageFromWebOperations(String url) {
        try {
            InputStream is = (InputStream) new URL(url).getContent();
            Drawable d = Drawable.createFromStream(is, "src name");
            return d;
        } catch (Exception e) {
            return null;
        }
    }
}
