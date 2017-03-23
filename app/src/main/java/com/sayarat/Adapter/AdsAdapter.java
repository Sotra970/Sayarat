package com.sayarat.Adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.GlideBitmapDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.sayarat.interfaces.Add_listener;
import com.sayarat.Models.ProductModel;
import com.sayarat.R;
import com.sayarat.app.Config;
import com.sayarat.interfaces.LikePlus;

import java.util.ArrayList;


public class AdsAdapter extends RecyclerView.Adapter<AdsAdapter.ImageAdapterViewHolder>{
    private static final String TAG ="Ads adapter" ;
    ArrayList<ProductModel> data  ;
    LayoutInflater layoutInflater ;
    Activity context ;
    Add_listener add_listener;
    public AdsAdapter(ArrayList<ProductModel> dta, Activity context , Add_listener add_listener) {
        this.data = dta;
        this.add_listener=add_listener;
        this.context = context;
        layoutInflater= LayoutInflater.from(context)  ;
    }

    @Override
    public ImageAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View item_view = layoutInflater.inflate(R.layout.product_item ,parent , false ) ;
        final ImageAdapterViewHolder  viewHolder = new ImageAdapterViewHolder(item_view);
        viewHolder.cover.post(new Runnable() {
            @Override
            public void run() {
                ViewGroup.LayoutParams params = viewHolder.cover.getLayoutParams();
                params.height = (viewHolder.cover.getMeasuredWidth()*9)/16 ;
                viewHolder.cover.setLayoutParams(params);
            }
        });

        return viewHolder;
    }



    @Override
    public void onBindViewHolder(final ImageAdapterViewHolder holder, final int position) {
        holder.cover.setImageBitmap(null);
        holder.cover.setImageResource(0);
        holder.cover.setImageDrawable(null);
        holder.cover.setImageURI(null);

        ProductModel current = data.get(position);
            SimpleTarget target = new SimpleTarget<GlideBitmapDrawable>() {
                @Override
                public void onResourceReady(GlideBitmapDrawable bitmap, GlideAnimation glideAnimation) {
                    // do something with the bitmap
                    // for demonstration purposes, let's just set it to an ImageView
                    Log.e("slider","endLoading");

                    holder.cover.setImageBitmap( bitmap.getBitmap() );
                    holder.spinner.setVisibility(View.GONE);
                }
            };
            Log.e("rescycler slider","ini" + position);
//            Log.e("ads adapter","img url " +Config.img_url + data.get(position).getImgs().get(0));


        try {
            holder.type.setText(current.getType());

        }catch (Exception e){

        }

        try {
            holder.make.setText(current.getMake());

        }catch (Exception e){

        }

        try {
            holder.price.setText(current.getPricevalue());

        }catch (Exception e){

        }

        try {
            holder.likes.setText(current.getLikes());

        }catch (Exception e){

        }
        try {
            Log.e("ads adapterimg " , current.getImgs().get(0) + "" ) ;
            if (!current.getImgs().get(0).equals(null) && !current.getImgs().get(0).trim().isEmpty()){
                Glide.with(context)
                        .load(Config.img_url +current.getImgs().get(0) )
                        .override(1080,640)
                        .centerCrop()
                        .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                        .into(holder.cover);
            }else {
                holder.cover.setImageBitmap(null);
                holder.cover.setImageResource(0);
                holder.cover.setImageDrawable(null);
                holder.cover.setImageURI(null);

            }


        }catch (Exception e){

        }


        holder.like_v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ProductModel current = data.get(position);
                // start add like
                add_listener.like(current.getProduct_id(), new LikePlus() {
                    @Override
                    public int plusOne() {
                        int likes = Integer.parseInt(holder.likes.getText().toString());
                        holder.likes.setText(String.valueOf(++likes));
                        return position ;
                    }
                });
            }
        });

        holder.share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ProductModel current = data.get(position);
                add_listener.share(current,holder.itemView);
            }
        });




    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class  ImageAdapterViewHolder extends RecyclerView.ViewHolder{
       ImageView cover ;
        TextView make, type , price , likes ;
        View spinner ,  container , like_v , fav , share ;
        public ImageAdapterViewHolder(final View itemView) {
            super(itemView);
            cover = (ImageView) itemView.findViewById(R.id.product_item_img);
            make = (TextView) itemView.findViewById(R.id.product_item_make);
            type = (TextView) itemView.findViewById(R.id.product_item_car_type);
            price = (TextView) itemView.findViewById(R.id.product_item_car_price);
            likes = (TextView) itemView.findViewById(R.id.product_item_likes);
            spinner = itemView.findViewById(R.id.loadingSpinner);
            container = itemView.findViewById(R.id.add_container);
            like_v = itemView.findViewById(R.id.add_like_v);
            share = itemView.findViewById(R.id.add_share);
//            fav = itemView.findViewById(R.id.add_fav);


//            fav.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    ProductModel current = data.get(getAdapterPosition());
//                    // start detailes
//                    add_listener.fav(current.getProduct_id());
//
//                }
//            });



            cover.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ProductModel current = data.get(getAdapterPosition());
                    // start detailes
                    Log.e("ads adpter","cover on click" + "model id " + current.getProduct_id());
                    add_listener.start_detailes(current);
                }
            });


        }
    }





}
