package com.blue_red.bensinpriser;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import org.json.JSONException;

import java.io.Serializable;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnItemClick;


public class FuelStationAdapter extends RecyclerView.Adapter<BaseViewHolder> {
    private static final String TAG = "FuelStationAdapter";
    public static final int VIEW_TYPE_EMPTY = 0;
    public static final int VIEW_TYPE_NORMAL = 1;

    private Callback mCallback;
    private List<FuelStation> mFuelStationList;
    private String fuel;

    public FuelStationAdapter(String fuel, List<FuelStation> fuelStationList) {
        mFuelStationList = fuelStationList;
        this.fuel = fuel;
    }

    public String getFuel() {
        return fuel;
    }

    public void setFuel(String fuel) {
        this.fuel = fuel;
    }

    public void setCallback(Callback callback) {
        mCallback = callback;
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {
        holder.onBind(position);
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        switch (viewType) {
            case VIEW_TYPE_NORMAL:
                return new ViewHolder(
                        LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false));
            case VIEW_TYPE_EMPTY:
            default:
                return new EmptyViewHolder(
                        LayoutInflater.from(parent.getContext()).inflate(R.layout.item_empty_view, parent, false));
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (mFuelStationList != null && mFuelStationList.size() > 0) {
            return VIEW_TYPE_NORMAL;
        } else {
            return VIEW_TYPE_EMPTY;
        }
    }

    @Override
    public int getItemCount() {
        if (mFuelStationList != null && mFuelStationList.size() > 0) {
            return mFuelStationList.size();
        } else {
            return 1;
        }
    }

    public void addItems(List<FuelStation> fuelStationList) {
        mFuelStationList.addAll(fuelStationList);
        notifyDataSetChanged();
    }

    public interface Callback {
        void onEmptyViewRetryClick() throws JSONException;
    }

    public class ViewHolder extends BaseViewHolder {

        @BindView(R.id.thumbnail)
        ImageView coverImageView;

        @BindView(R.id.title)
        TextView titleTextView;

        @BindView(R.id.distance)
        TextView distTextView;

        @BindView(R.id.price)
        TextView priceTextView;

        @BindView(R.id.textViewCompanyName)
        TextView companyNameTextView;


        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        protected void clear() {
            coverImageView.setImageDrawable(null);
            titleTextView.setText("");
            distTextView.setText("");
            priceTextView.setText("");
        }

        public void onBind(int position) {
            super.onBind(position);

            final FuelStation mFuelStation = mFuelStationList.get(position);
            double pricetemp;
            if (fuel.equals("Bensin 95")) {
                pricetemp = mFuelStation.getmBensin95();
            } else if (fuel.equals("Bensin 98")) {
                pricetemp = mFuelStation.getmBensin98();
            } else if (fuel.equals("Diesel")) {
                pricetemp = mFuelStation.getmDiesel();
            } else if (fuel.equals("Ethanol 85")) {
                pricetemp = mFuelStation.getmEthanol85();
            } else {
                pricetemp = -1;
            }

            if (mFuelStation.getImageUrl() != null) {
                Glide.with(itemView.getContext())
                        .load(mFuelStation.getImageUrl())
                        .into(coverImageView);
                //https://stackoverflow.com/questions/9113895/how-to-check-if-an-imageview-is-attached-with-image-in-android
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (coverImageView.getDrawable() == null) {
                            //Image doesn´t exist.
                            companyNameTextView.setVisibility(View.VISIBLE);
                            companyNameTextView.setText(mFuelStation.getmCompanyName());
                            coverImageView.setVisibility(View.INVISIBLE);
                        } else {
                            //Image Exists!.
                            companyNameTextView.setVisibility(View.GONE);
                        }
                    }
                }, 500);
            }


            if (mFuelStation.getTitle() != null) {
                titleTextView.setText(mFuelStation.getTitle());
            }

            //https://www.java67.com/2015/10/how-to-convert-float-to-int-in-java-example.html
            distTextView.setText("Distance: " + Math.round(mFuelStation.getmDistance()) + "km");


            priceTextView.setText(fuel + " pris: " + pricetemp + "kr");


            itemView.setOnClickListener(v -> {
                //https://stackoverflow.com/questions/20241857/android-intent-cannot-resolve-constructor/20241921
                Intent intent = new Intent(v.getContext(), DetailViewActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("Station", mFuelStation);
                intent.putExtras(bundle);
                //https://stackoverflow.com/questions/33173043/cannot-resolve-method-startactivity
                v.getContext().startActivity(intent);

            }
        );
    }
}

public class EmptyViewHolder extends BaseViewHolder {

    @BindView(R.id.tv_message)
    TextView messageTextView;
    @BindView(R.id.buttonRetry)
    TextView buttonRetry;

    EmptyViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        buttonRetry.setOnClickListener(v -> {
            try {
                mCallback.onEmptyViewRetryClick();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    protected void clear() {

    }
}

}
