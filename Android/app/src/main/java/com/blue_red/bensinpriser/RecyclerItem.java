package com.blue_red.bensinpriser;

import android.graphics.drawable.Drawable;

import java.io.InputStream;
import java.net.URL;

public class RecyclerItem {
    private String  mImageURL;
    private String mText1;
    private String mText2;
    public RecyclerItem (String imageURL, String text1, String text2)
    {
        mImageURL=imageURL;
        mText1=text1;
        mText2=text2;

    }


    public String getmImageResource() {
        return mImageURL;
    }

    public void setmImageResource(String mImageURL) {
        this.mImageURL = mImageURL;
    }

    public String getmText1() {
        return mText1;
    }

    public void setmText1(String mText1) {
        this.mText1 = mText1;
    }

    public String getmText2() {
        return mText2;
    }

    public void setmText2(String mText2) {
        this.mText2 = mText2;
    }
}
