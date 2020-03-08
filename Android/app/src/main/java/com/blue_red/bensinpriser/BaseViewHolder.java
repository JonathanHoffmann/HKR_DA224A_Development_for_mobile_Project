package com.blue_red.bensinpriser;

import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

/**
 * Created on : Jan 26, 2019
 * Author     : AndroidWave
 * Email    : info@androidwave.com
 */
public abstract class BaseViewHolder extends RecyclerView.ViewHolder {

    private int mCurrentPosition;

    public BaseViewHolder(View itemView) {
        super(itemView);
    }

    protected abstract void clear();

    public void onBind(int position) {
        mCurrentPosition = position;
        clear();
    }

    public int getCurrentPosition() {
        return mCurrentPosition;
    }
}
