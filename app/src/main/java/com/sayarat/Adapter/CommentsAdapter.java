package com.sayarat.Adapter;

import android.app.Activity;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
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
import com.sayarat.Models.UserModel;
import com.sayarat.R;
import com.sayarat.app.CommentModel;
import com.sayarat.interfaces.comments_listener;

import java.util.ArrayList;

/**
 * Created by ahmed on 2/23/2017.
 */

public class CommentsAdapter extends RecyclerView.Adapter<CommentsAdapter.ImageAdapterViewHolder>{
    ArrayList<CommentModel> data  ;
    LayoutInflater layoutInflater ;
    Activity context ;
    comments_listener  comments_listener ;

    public CommentsAdapter(ArrayList<CommentModel> dta, Activity context , comments_listener comments_listener) {
        this.data = dta;
        this.comments_listener = comments_listener ;
        this.context = context;
        layoutInflater= LayoutInflater.from(context)  ;
    }

    @Override
    public ImageAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View item_view = layoutInflater.inflate(R.layout.comment_item,parent , false ) ;
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
        Log.e("slider","img url " +data.get(position).getUser().getImg());
        int dp60 = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 60, context.getResources().getDisplayMetrics());

        Glide.with(context)
                .load(data.get(position).getUser().getImg())
                .centerCrop()
                .override(dp60,dp60)
                .into(target);
        holder.name.setText(data.get(position).getUser().getUser_name());
        holder.time.setText(data.get(position).getDate());
        holder.comment.setText(data.get(position).getComment());

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class  ImageAdapterViewHolder extends RecyclerView.ViewHolder{
       ImageView profileImg ;
        View profileSpinner  ;
        TextView name , time , comment ;
        public ImageAdapterViewHolder(final View itemView) {
            super(itemView);
            profileImg = (ImageView) itemView.findViewById(R.id.comment_img);
            name = (TextView) itemView.findViewById(R.id.comment_name);
            time = (TextView) itemView.findViewById(R.id.comment_time);
            comment = (TextView) itemView.findViewById(R.id.comment_text);

            profileImg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    UserModel current = data.get(getAdapterPosition()).getUser();
                    // start detailes
                    comments_listener.start_profile(current,itemView);
                }
            });


            name.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    UserModel current = data.get(getAdapterPosition()).getUser();
                    comments_listener.start_profile(current,profileImg);
                }
            });


        }

    }



}
