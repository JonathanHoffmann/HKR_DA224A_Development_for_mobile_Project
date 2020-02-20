package com.blue_red.bensinpriser;

import android.graphics.drawable.Drawable;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;

//https://www.youtube.com/watch?v=17NbUcEts9c
public class recyclerAdapter extends RecyclerView.Adapter<recyclerAdapter.recyclerViewHoler> {
    private ArrayList<RecyclerItem> mItemList;

    public static class recyclerViewHoler extends RecyclerView.ViewHolder {
        public ImageView mImageView;
        public TextView mTextView1;
        public  TextView mTextView2;

        public recyclerViewHoler(@NonNull View itemView) {
            super(itemView);
            mImageView = itemView.findViewById(R.id.imageView);
            mTextView1 = itemView.findViewById(R.id.textView);
            mTextView2 = itemView.findViewById(R.id.textView2);
        }
    }

    public recyclerAdapter(ArrayList<RecyclerItem> recyclerlist)
    {
        mItemList=recyclerlist;
    }

    @NonNull
    @Override
    public recyclerViewHoler onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_price_view, parent, false);
        recyclerViewHoler rvh = new recyclerViewHoler(v);
        return rvh;    }

    @Override
    public void onBindViewHolder(@NonNull recyclerViewHoler holder, int position) {
        RecyclerItem currentitem = mItemList.get(position);
        //holder.mImageView.setImageDrawable(LoadImageFromWebOperations(currentitem.getmImageResource()));
        holder.mTextView1.setText(currentitem.getmText1());
        holder.mTextView2.setText(currentitem.getmText2());

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

    @Override
    public int getItemCount() {
        return mItemList.size();
    }
}
