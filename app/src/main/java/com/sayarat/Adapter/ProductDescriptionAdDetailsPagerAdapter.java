package com.sayarat.Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.view.PagerAdapter;
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

/**
 * Created by ahmed on 2/22/2017.
 */

public class ProductDescriptionAdDetailsPagerAdapter extends android.support.v4.view.PagerAdapter {
    LayoutInflater mLayoutInflater ;
    private Context mContext;
    ArrayList<String> data ;
    ArrayList<String> odlddata ;
    ArrayList<Bitmap> file_data ;
    int type ;
    private View itemView;
    int w , h ;
    public ProductDescriptionAdDetailsPagerAdapter(Context context , ArrayList<String> data) {
        mContext = context;
        mLayoutInflater = LayoutInflater.from(context);
        this.data = data ;
        type = Config.DOWNLOAD ;
    }

    public ProductDescriptionAdDetailsPagerAdapter(Context context, ArrayList<String> odlddata, ArrayList<Bitmap> data ,String grap) {
        mContext = context;
        this.odlddata = odlddata ;
        mLayoutInflater = LayoutInflater.from(context);
        this.file_data = data ;
        type = Config.UPLOAD ;
    }

    public ProductDescriptionAdDetailsPagerAdapter(Context context, ArrayList<Bitmap> data ,String grap) {
        mContext = context;
        mLayoutInflater = LayoutInflater.from(context);
        this.file_data = data ;
        type = Config.UPLOAD ;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position)  {
         itemView = mLayoutInflater.inflate(R.layout.product_description_ad_details_pager_item, container, false);

        final View spinner = itemView.findViewById(R.id.loadingSpinner);
        final ImageView imageView = (ImageView) itemView.findViewById(R.id.product_description_ad_details_pager_item_img);
        imageView.post(new Runnable() {
            @Override
            public void run() {
                ViewGroup.LayoutParams params = imageView.getLayoutParams();
                params.height =(imageView.getMeasuredWidth()*9)/16 ;
                imageView.setLayoutParams(params);

            }
        });

        if (type == Config.DOWNLOAD){



            Log.e("slider","ini" + position);
            Log.e("slider","img url " +Config.img_url + data.get(position));
            Glide.with(mContext)
                    .load(Config.img_url + data.get(position))
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .fitCenter()
                    .override(1080,640)
                    .crossFade(300)
                    .listener(new RequestListener<String, GlideDrawable>() {
                        @Override
                        public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                            spinner.setVisibility(View.GONE);
                            return false;
                        }
                    })
                    .into(imageView);

        }
//
//        else {
//            spinner.setVisibility(View.GONE);
//            BitmapFactory.Options options = new BitmapFactory.Options();
//            options.inPreferredConfig = Bitmap.Config.ARGB_8888;
//            imageView.setImageBitmap(file_data.get(position));
//        }
        container.addView(itemView);
        return itemView;
    }


    @Override
    public void destroyItem(ViewGroup collection, int position, Object view) {
        collection.removeView((View) view);
    }

    @Override
    public int getCount() {
        if (type == Config.DOWNLOAD)
        return data.size();
        else
            return file_data.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public int getItemPosition(Object object) {
        return PagerAdapter.POSITION_NONE;
    }
}