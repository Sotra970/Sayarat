package com.sayarat.Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.GlideBitmapDrawable;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.sayarat.R;
import com.sayarat.app.Config;

import java.io.File;
import java.util.ArrayList;


public class ImageAdapter  extends RecyclerView.Adapter<ImageAdapter.ImageAdapterViewHolder>{
    ArrayList<String> data  ;
    ArrayList<Bitmap> upload_imgs  ;
    LayoutInflater layoutInflater ;
    Context context ;
    int type ;

    public ImageAdapter(ArrayList<String> dta, Context context) {
        this.data = dta;
        this.context = context;
        layoutInflater= LayoutInflater.from(context)  ;
        type = Config.DOWNLOAD ;
    }

    public ImageAdapter(ArrayList<Bitmap> upload_imgs, Context context, String s) {
        this.upload_imgs = upload_imgs ;
        this.context = context ;
        layoutInflater= LayoutInflater.from(context)  ;
        type = Config.UPLOAD ;

    }

    @Override
    public ImageAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View item_view = layoutInflater.inflate(R.layout.product_description_ad_details_recycler_item ,parent , false ) ;
        final ImageAdapterViewHolder  viewHolder = new ImageAdapterViewHolder(item_view);
        viewHolder.img.post(new Runnable() {
            @Override
            public void run() {
                ViewGroup.LayoutParams params = viewHolder.img.getLayoutParams();
                params.width = (viewHolder.img.getMeasuredHeight()*4)/3 ;
                viewHolder.img.setLayoutParams(params);
            }
        });

        return viewHolder;
    }



    @Override
    public void onBindViewHolder(final ImageAdapterViewHolder holder, int position) {
//            Log.e("img adapter data size " ,data.size() +"");
        if (type == Config.DOWNLOAD){
            SimpleTarget target = new SimpleTarget<GlideBitmapDrawable>() {
                @Override
                public void onResourceReady(GlideBitmapDrawable bitmap, GlideAnimation glideAnimation) {
                    // do something with the bitmap
                    // for demonstration purposes, let's just set it to an ImageView
                    Log.e("slider","endLoading");

                    holder.img.setImageBitmap( bitmap.getBitmap() );
                    holder.spinner.setVisibility(View.GONE);
                }
            };
            Log.e("rescycler slider","ini" + position);
            Log.e("slider","img url " +Config.img_url + data.get(position));
            Glide.with(context)
                    .load(Config.img_url + data.get(position))
                    .centerCrop()
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .override(300,250)
                    .crossFade()
                    .listener(new RequestListener<String, GlideDrawable>() {
                        @Override
                        public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                            holder.spinner.setVisibility(View.GONE);
                            return false;
                        }
                    })
                    .into(holder.img);

        }else {
            holder.spinner.setVisibility(View.GONE);
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inPreferredConfig = Bitmap.Config.ARGB_8888;
            holder.img.setImageBitmap(upload_imgs.get(position));
        }
    }

    @Override
    public int getItemCount() {
        if (type == Config.DOWNLOAD)
        return data.size();
        else return upload_imgs.size();
    }

    class  ImageAdapterViewHolder extends RecyclerView.ViewHolder{
       ImageView img;
        View spinner ;
        public ImageAdapterViewHolder(View itemView) {
            super(itemView);
            img = (ImageView) itemView.findViewById(R.id.product_description_ad_details_recyclle_item_img);
            spinner = itemView.findViewById(R.id.loadingSpinner);

        }
    }


}
