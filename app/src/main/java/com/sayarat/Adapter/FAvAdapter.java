package com.sayarat.Adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.GlideBitmapDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.sayarat.interfaces.Add_listener;
import com.sayarat.Models.ProductModel;
import com.sayarat.R;
import com.sayarat.app.Config;

import java.util.ArrayList;

/**
 * Created by ahmed on 2/23/2017.
 */

public class FAvAdapter extends RecyclerView.Adapter<FAvAdapter.ImageAdapterViewHolder>{
    ArrayList<ProductModel> data  ;
    LayoutInflater layoutInflater ;
    Context context ;
    Add_listener add_listener;
    public FAvAdapter(ArrayList<ProductModel> dta, Activity context, Add_listener add_listener) {
        this.data = dta;
        this.context = context;
        this.add_listener = add_listener ;
        layoutInflater= LayoutInflater.from(context)  ;
    }

    @Override
    public ImageAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View item_view = layoutInflater.inflate(R.layout.myad_user_profile_recycler_item,parent , false ) ;
         ImageAdapterViewHolder  viewHolder = new ImageAdapterViewHolder(item_view);


        return viewHolder;
    }



    @Override
    public void onBindViewHolder(final ImageAdapterViewHolder holder, int position) {
            Log.e("img adapter data size " ,data.size() +"");
        ProductModel current = data.get(position);
     
        try{
            Log.e("rescycler slider","ini" + position);
            Log.e("slider","img url " + Config.img_url + data.get(position).getImgs().get(0));
            int dp60 = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 96, context.getResources().getDisplayMetrics());

            Glide.with(context)
                    .load(Config.img_url + current.getImgs().get(0).trim())
                    .centerCrop()
                    .centerCrop()
                    .override(dp60,dp60)
                    .into(holder.profileImg);

        }catch (Exception e){

        }
        holder.date.setText(current.getDate());
        holder.title.setText(current.getTitle());


    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class  ImageAdapterViewHolder extends RecyclerView.ViewHolder{
       ImageView profileImg ;
        TextView title , date  ;
        public ImageAdapterViewHolder(final View itemView) {
            super(itemView);
            profileImg = (ImageView) itemView.findViewById(R.id.add_img);
            title = (TextView) itemView.findViewById(R.id.add_title);
            date = (TextView) itemView.findViewById(R.id.add_time);


            itemView.setOnClickListener(new View.OnClickListener() {
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
