package com.sayarat.Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.GlideBitmapDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.sayarat.Models.ProductModel;
import com.sayarat.R;
import com.sayarat.app.Config;

import java.util.ArrayList;

/**
 * Created by sotra on 9/7/2016.
 */
public class MostProdaactsPAgerAdapter extends PagerAdapter {
    Context mContext;
    LayoutInflater mLayoutInflater;
    private ArrayList<ArrayList<ProductModel>> mResources;
    public MostProdaactsPAgerAdapter(Context mContext , ArrayList<ArrayList<ProductModel>> data   ) {
        this.mContext = mContext;
       mLayoutInflater = LayoutInflater.from(mContext);
        mResources = data ;
    }

    /**
     * Return the number of views available.
     */
    @Override
    public int getCount() {
        return mResources.size();
    }
    public void addItem(ArrayList<ProductModel> ResList){
        mResources.add(ResList);
        notifyDataSetChanged();
    }

    /**
     * Create the page for the given position.  The adapter is responsible
     * for adding the view to the container given here, although it only
     * must ensure this is done by the time it returns from
     * {@link #finishUpdate(ViewGroup)}.
     *
     * @param container The containing View in which the page will be shown.
     * @param position  The page position to be instantiated.
     * @return Returns an Object representing the new page.  This does not
     * need to be a View, but can be some other container of the page.
     *
     */

    @Override
    public Object instantiateItem(ViewGroup container,  int position)  {
        ProductModel ProductModel1 = mResources.get(position).get(0);
        ProductModel ProductModel2 = mResources.get(position).get(1);
        ProductModel ProductModel3 = mResources.get(position).get(2);
        View itemView = mLayoutInflater.inflate(R.layout.most_viewd_pager_item, container, false);
        final View spinner1 = itemView.findViewById(R.id.loadingSpinner1);
        final View spinner2 = itemView.findViewById(R.id.loadingSpinner2);
        final View spinner3 = itemView.findViewById(R.id.loadingSpinner3);

         final ImageView most_image_1 = (ImageView) itemView.findViewById(R.id.most_image_1);
         final ImageView most_image_2 = (ImageView) itemView.findViewById(R.id.most_image_2);
         final ImageView most_image_3 = (ImageView) itemView.findViewById(R.id.most_image_3);
        final TextView most_liks_1 = (TextView) itemView.findViewById(R.id.most_likes);
        final TextView most_liks_2 = (TextView) itemView.findViewById(R.id.most_likes2);
        final TextView most_liks_3 = (TextView) itemView.findViewById(R.id.most_likes3);

        final TextView most_views_1 = (TextView) itemView.findViewById(R.id.most_views1);
        final TextView most_views_2 = (TextView) itemView.findViewById(R.id.most_views2);
        final TextView most_views_3 = (TextView) itemView.findViewById(R.id.most_views3);
        most_image_1.post(new Runnable() {
            @Override
            public void run() {
                ViewGroup.LayoutParams params = most_image_1.getLayoutParams();
                params.height = (most_image_1.getMeasuredWidth()*9)/16 ;
                most_image_1.setLayoutParams(params);
                most_image_2.setLayoutParams(params);
                most_image_3.setLayoutParams(params);
                Log.e("slider","img he" + params.height + (most_image_1.getMeasuredWidth()*9)/16 );
            }
        });

            most_liks_1.setText(ProductModel1.getLikes());
            most_liks_2.setText(ProductModel2.getLikes());
            most_liks_3.setText(ProductModel3.getLikes());
            most_views_1.setText(ProductModel1.getViews());
            most_views_2.setText(ProductModel2.getViews());
            most_views_3.setText(ProductModel3.getViews());
            SimpleTarget target1 = new SimpleTarget<GlideBitmapDrawable>() {

                @Override
                public void onLoadFailed(Exception e, Drawable errorDrawable) {

                }

                @Override
            public void onResourceReady(GlideBitmapDrawable bitmap, GlideAnimation glideAnimation) {
                 Log.e("slider","endLoading1");
                 most_image_1.setImageBitmap( bitmap.getBitmap() );
                 spinner1.setVisibility(View.GONE);
            }
        };
        Glide.with(mContext)
                .load(Config.img_url + ProductModel1.getImgs().get(0))
                .fitCenter()
                .into(target1);

        SimpleTarget target2 = new SimpleTarget<GlideBitmapDrawable>() {

                @Override
                public void onLoadFailed(Exception e, Drawable errorDrawable) {

                }

                @Override
            public void onResourceReady(GlideBitmapDrawable bitmap, GlideAnimation glideAnimation) {
                 Log.e("slider","endLoading2");
                 most_image_2.setImageBitmap( bitmap.getBitmap() );
                 spinner2.setVisibility(View.GONE);
            }
        };
        Glide.with(mContext)
                .load(Config.img_url + ProductModel2.getImgs().get(0))
                .fitCenter()
                .into(target2);

        SimpleTarget target3 = new SimpleTarget<GlideBitmapDrawable>() {

            @Override
            public void onLoadFailed(Exception e, Drawable errorDrawable) {

            }

            @Override
            public void onResourceReady(GlideBitmapDrawable bitmap, GlideAnimation glideAnimation) {
                Log.e("slider","endLoading3");
                most_image_3.setImageBitmap( bitmap.getBitmap() );
                spinner3.setVisibility(View.GONE);
            }
        };
        Glide.with(mContext)
                .load(Config.img_url + ProductModel3.getImgs().get(0))
                .fitCenter()
                .into(target3);

        Log.e("img url" ,(Config.img_url +ProductModel3.getImgs().get(0)));
        container.addView(itemView);
        return itemView;
    }



    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((FrameLayout) object);
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
          return view == object;
    }


}
