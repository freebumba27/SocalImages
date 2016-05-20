package com.aj.socialImages.widgets;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.aj.socialImages.R;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by anirban on 5/20/16.
 */
public class GalleryRecyclerViewAdapter extends RecyclerView.Adapter<GalleryRecyclerViewAdapter.MyViewHolder> {

    private ArrayList<String> imageLists;
    private Context context;

    public GalleryRecyclerViewAdapter(Context context) {
        this.context = context;
        imageLists = new ArrayList<>();
    }

    public void addAll(ArrayList<String> images) {
        this.imageLists = images;
        notifyDataSetChanged();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.gallery_image_row, null, false);
        MyViewHolder myViewHolder = new MyViewHolder(view);

        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Glide.with(context)
                .load(imageLists.get(position))
                .centerCrop()
                .placeholder(R.drawable.placeholder)
                .crossFade()
                .into(holder.singleGalleryImage);
    }

    @Override
    public int getItemCount() {
        return imageLists.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.singleGalleryImage)
        ImageView singleGalleryImage;

        public MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
