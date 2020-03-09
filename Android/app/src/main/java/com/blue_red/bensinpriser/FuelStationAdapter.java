package com.blue_red.bensinpriser;

import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import org.json.JSONException;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class FuelStationAdapter extends RecyclerView.Adapter<BaseViewHolder> {
    private static final String TAG = "FuelStationAdapter";
    public static final int VIEW_TYPE_EMPTY = 0;
    public static final int VIEW_TYPE_NORMAL = 1;

    private Callback mCallback;
    private List<FuelStation> mFuelStationList;

    public FuelStationAdapter(List<FuelStation> fuelStationList) {
        mFuelStationList = fuelStationList;
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

            if (mFuelStation.getImageUrl() != null) {
                Glide.with(itemView.getContext())
                        .load(mFuelStation.getImageUrl())
                        .into(coverImageView);
            }

            if (mFuelStation.getTitle() != null) {
                titleTextView.setText(mFuelStation.getTitle());
            }

            distTextView.setText("Distance: " + Math.round(mFuelStation.getmDistance()) + "km");

            priceTextView.setText("Bensin pris: " + mFuelStation.getmBensin95() + "kr");

            itemView.setOnClickListener(v -> {
                if (mFuelStation.getImageUrl() != null) {
                    try {
                        Intent intent = new Intent();
                        intent.setAction(Intent.ACTION_VIEW);
                        intent.addCategory(Intent.CATEGORY_BROWSABLE);
                        intent.setData(Uri.parse(mFuelStation.getImageUrl()));
                        itemView.getContext().startActivity(intent);
                    } catch (Exception e) {
                        Log.e(TAG, "onClick: Image url is not correct");
                    }
                }
            });
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
