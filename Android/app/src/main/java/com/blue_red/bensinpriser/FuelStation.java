
package com.blue_red.bensinpriser;


import com.google.gson.annotations.SerializedName;

public class FuelStation {

    @SerializedName("imageUrl")
    private String mImageUrl;
    @SerializedName("info")
    private String mInfo;
    @SerializedName("subTitle")
    private String mSubTitle;
    @SerializedName("title")
    private String mTitle;
    @SerializedName("bensin98")
    private double mBensin98;
    @SerializedName("bensin95")
    private double mBensin95;
    @SerializedName("diesel")
    private double mDiesel;
    @SerializedName("ethanol85")
    private double mEthanol85;
    @SerializedName("latitude")
    private double mLatitude;
    @SerializedName("longitude")
    private double mLongitude;

    public double getmLatitude() {
        return mLatitude;
    }

    public void setmLatitude(double mLatitude) {
        this.mLatitude = mLatitude;
    }

    public double getmLongitude() {
        return mLongitude;
    }

    public void setmLongitude(double mLongitude) {
        this.mLongitude = mLongitude;
    }

    public double getmBensin98() {
        return mBensin98;
    }

    public void setmBensin98(double mBensin98) {
        this.mBensin98 = mBensin98;
    }

    public double getmBensin95() {
        return mBensin95;
    }

    public void setmBensin95(double mBensin95) {
        this.mBensin95 = mBensin95;
    }

    public double getmDiesel() {
        return mDiesel;
    }

    public void setmDiesel(double mDiesel) {
        this.mDiesel = mDiesel;
    }

    public double getmEthanol85() {
        return mEthanol85;
    }

    public void setmEthanol85(double mEthanol85) {
        this.mEthanol85 = mEthanol85;
    }

    public FuelStation(String mImageUrl, String mInfo, String mSubTitle, String mTitle, double mBensin95, double mBensin98, double mDiesel, double mEthanol85, double mLatitude, double mLongitude) {
        this.mImageUrl = mImageUrl;
        this.mInfo = mInfo;
        this.mSubTitle = mSubTitle;
        this.mTitle = mTitle;
        this.mBensin95 = mBensin95;
        this.mBensin98 = mBensin98;
        this.mDiesel = mDiesel;
        this.mEthanol85 = mEthanol85;
        this.mLatitude = mLatitude;
        this.mLongitude = mLongitude;
    }

    public String getImageUrl() {
        return mImageUrl;
    }

    public void setImageUrl(String imageUrl) {
        mImageUrl = imageUrl;
    }

    public String getInfo() {
        return mInfo;
    }

    public void setInfo(String info) {
        mInfo = info;
    }

    public String getSubTitle() {
        return mSubTitle;
    }

    public void setSubTitle(String subTitle) {
        mSubTitle = subTitle;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

}
