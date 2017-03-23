package com.sayarat.Adapter;

import android.content.Context;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.GlideBitmapDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.sayarat.R;
import com.sayarat.app.Config;

import java.util.ArrayList;

/**
 * Created by ahmed on 2/23/2017.
 */

public class MyfavoritesImageAdapter extends RecyclerView.Adapter<MyfavoritesImageAdapter.ImageAdapterViewHolder>{
    ArrayList<String> data  ;
    LayoutInflater layoutInflater ;
    Context context ;

    public MyfavoritesImageAdapter(ArrayList<String> dta, Context context) {
        this.data = dta;
        this.context = context;
        layoutInflater= LayoutInflater.from(context)  ;
    }

    @Override
    public ImageAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View item_view = layoutInflater.inflate(R.layout.myfavorite_user_profile_recycler_item,parent , false ) ;
        final ImageAdapterViewHolder  viewHolder = new ImageAdapterViewHolder(item_view);
        viewHolder.profileImg.post(new Runnable() {
            @Override
            public void run() {
                ViewGroup.LayoutParams params = viewHolder.profileImg.getLayoutParams();
                params.width = (viewHolder.profileImg.getMeasuredHeight()*16)/9 ;
                viewHolder.profileImg.setLayoutParams(params);
            }
        });

        return viewHolder;
    }



    @Override
    public void onBindViewHolder(final ImageAdapterViewHolder holder, int position) {
            Log.e("img adapter data size " ,data.size() +"");
        SimpleTarget target = new SimpleTarget<GlideBitmapDrawable>() {
            @Override
            public void onResourceReady(GlideBitmapDrawable bitmap, GlideAnimation glideAnimation) {
                // do something with the bitmap
                // for demonstration purposes, let's just set it to an ImageView
                Log.e("slider","endLoading");
                RoundedBitmapDrawable circularBitmapDrawable =
                        RoundedBitmapDrawableFactory.create(context.getResources(), bitmap.getBitmap());
                circularBitmapDrawable.setCircular(true);
                holder.profileImg.setImageDrawable( circularBitmapDrawable );
            }
        };
        Log.e("rescycler slider","ini" + position);
        Log.e("slider","img url " + Config.img_url + data.get(position));
        Glide.with(context)
                .load(Config.img_url + data.get(position))
                .centerCrop()
                .into(target);

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class  ImageAdapterViewHolder extends RecyclerView.ViewHolder{
       ImageView profileImg ;
        public ImageAdapterViewHolder(View itemView) {
            super(itemView);
            profileImg = (ImageView) itemView.findViewById(R.id.myfavorite_user_profile_recycler_img);
        }
    }


}
