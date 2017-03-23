package com.sayarat.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.GlideBitmapDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.sayarat.Activity.LoginActivity;
import com.sayarat.Activity.ProductDetailsActivity;
import com.sayarat.Activity.SettingsActivity;
import com.sayarat.Adapter.FAvAdapter;
import com.sayarat.interfaces.Add_listener;
import com.sayarat.Models.ProductModel;
import com.sayarat.Models.UserModel;
import com.sayarat.R;
import com.sayarat.app.AppController;
import com.sayarat.app.Config;
import com.sayarat.interfaces.LikePlus;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by ahmed on 2/24/2017.
 */

public class ProfileAdsFragment extends Fragment implements Add_listener {
    View view , settings , send_message;
    ArrayList<ProductModel> ads_data = new ArrayList<>()  ;
    FAvAdapter ads_adapter  ;
    ImageView proifle_img  ;
    TextView profile_name ;
    TextView user_id , user_email , user_phone ;

    UserModel extra_model ;
    TextInputLayout desc_layout ;
    EditText description ;
    View dialog ;
    View send , cancel;

    public void setExtra_model(UserModel extra_model) {
        this.extra_model = extra_model;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (view == null){
            view = inflater.inflate(R.layout.myads_user_profile,container,false);
            profile_data_setup();
            ad_recycler_setup();
        }

        return view;
    }
    public void opendialog(){
        dialog.setVisibility(View.VISIBLE);
    }
    void profile_data_setup(){
        desc_layout = (TextInputLayout) view.findViewById(R.id.desc_layout);
        description = (EditText) view.findViewById(R.id.desc_input);
        dialog = view.findViewById(R.id.layout_dashboard);
        send_message = view.findViewById(R.id.profile_send_msg);
        settings = view.findViewById(R.id.profile_settings);
        try {
            if (extra_model.getUser_id().equals(AppController.getInstance().getPrefManager().getUser().getUser_id())) {
                send_message.setVisibility(View.GONE);
            }else {
                settings.setVisibility(View.GONE);

            }
        }catch (NullPointerException e){
            send_message.setVisibility(View.GONE);

        }


        send =  view.findViewById(R.id.send_btn);
        cancel = view.findViewById(R.id.cancel_btn);
        send_message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (AppController.getInstance().getPrefManager().getUser() !=null)
                opendialog();
                else
                    startActivity(new Intent(getContext() , LoginActivity.class));
            }
        });
        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), SettingsActivity.class));
            }
        });
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (AppController.getInstance().getPrefManager().getUser() != null)
                send_mess_req() ;
                else startActivity(new Intent(getContext() , LoginActivity.class));
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.setVisibility(View.GONE);
            }
        });
        profile_name = (TextView) view.findViewById(R.id.profile_name) ;
        user_phone = (TextView) view.findViewById(R.id.phone) ;
        user_email = (TextView) view.findViewById(R.id.email) ;
        user_id = (TextView) view.findViewById(R.id._user_id) ;

        profile_name.setText(extra_model.getUser_name());
        try{
            user_phone.setText(extra_model.getUser_phone());

        }catch (Exception e){

        }
        try{
            user_phone.setText(extra_model.getUser_phone());

        }catch (Exception e){

        }
        try{
            user_email.setText(extra_model.getEmail());

        }catch (Exception e){

        }
        user_id.setText(extra_model.getUser_id());

        proifle_img = (ImageView) view.findViewById(R.id.profile_img) ;
        SimpleTarget target = new SimpleTarget<GlideBitmapDrawable>() {
            @Override
            public void onResourceReady(GlideBitmapDrawable bitmap, GlideAnimation glideAnimation) {
                // do something with the bitmap
                // for demonstration purposes, let's just set it to an ImageView
                Log.e("slider","endLoading");
                RoundedBitmapDrawable circularBitmapDrawable =
                        RoundedBitmapDrawableFactory.create(getContext().getResources(), bitmap.getBitmap());
                circularBitmapDrawable.setCircular(true);
                proifle_img.setImageDrawable( circularBitmapDrawable );
            }
        };
        Glide.with(getContext())
                .load( extra_model.getImg())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .centerCrop()
                .into(target);
    }

    void ad_recycler_setup(){
        RecyclerView pager_RecyclerView = (RecyclerView) view.findViewById(R.id.myad_profile_recycler_view);
        pager_RecyclerView.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL , false));
         ads_adapter = new FAvAdapter(ads_data , getActivity(),this) ;
        pager_RecyclerView.setAdapter(ads_adapter);
        get_ads();

    }

    void get_ads () {


        StringRequest requesturl = new StringRequest(Request.Method.POST, Config.BASE_URL+"myads.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("get ads", "respone "+response.toString()) ;
                try {
                    JSONObject response_obj = new JSONObject(response);
                    if (response_obj.getString("response").equals("success")){
                        JSONArray feed_arr = response_obj.getJSONArray("data") ;
                        for (int i=0; i<feed_arr.length();i++){
                            JSONObject add = feed_arr.getJSONObject(i) ;

                            JSONArray images = add.getJSONArray("images") ;
                            ArrayList<String> imgs = new ArrayList<>();
                            for (int j=0; j<images.length();j++){
                                imgs.add(images.getJSONObject(0).getString("image"));
                            }

                            ProductModel prodauctModel = new ProductModel() ;
                            prodauctModel.setImgs(imgs);
                            prodauctModel.setType(add.getString("type"));
                            prodauctModel.setTitle(add.getString("title"));
                            prodauctModel.setMake(add.getString("make"));
                            prodauctModel.setYear(add.getString("year"));
                            prodauctModel.setAdvertisertype(add.getString("advertisertype"));
                            prodauctModel.setUsingstatus(add.getString("usingstatus"));
                            prodauctModel.setPricetype(add.getString("pricetype"));
                            prodauctModel.setPricevalue(add.getString("pricevalue"));
                            prodauctModel.setDetails(add.getString("details"));
                            prodauctModel.setOdemetertype(add.getString("odemetertype"));
                            prodauctModel.setOdemetervaue(add.getString("odemetervaue"));
                            prodauctModel.setGuarantee(add.getString("Guarantee"));
                            prodauctModel.setAdvertiserID(add.getString("advertiserID"));
                            prodauctModel.setDate(add.getString("date"));
                            prodauctModel.setAutomatic(add.getString("automatic"));
                            prodauctModel.setProduct_id(add.getString("Id"));
                            prodauctModel.setAddress(add.getString("Address"));
                            prodauctModel.setActive(add.getString("Active"));
                            prodauctModel.setPhone(add.getString("phone"));
                            prodauctModel.setVehiclestatus(add.getString("vehiclestatus"));
                            prodauctModel.setVehicleset(add.getString("vehicleset"));
                            prodauctModel.setLikes(add.getString("likes"));


                            JSONObject temp = add.getJSONObject("user");
                            UserModel user = new UserModel(temp.getString("ID"),
                                    temp.getString("Name"),
                                    temp.getString("Phone"),
                                    temp.getString("Email"),
                                    temp.getString("picture")
                            );
                            prodauctModel.setUserModel(user);

                            ads_data.add(prodauctModel);
                            Log.e("data", prodauctModel.getImgs().size()+"");
                        }

                        ads_adapter.notifyDataSetChanged();
                        view.findViewById(R.id.no_ads).setVisibility(View.GONE);
                        view.findViewById(R.id.no_ads_loading).setVisibility(View.GONE);


                    }else {
                        view.findViewById(R.id.no_ads).setVisibility(View.VISIBLE);
                        view.findViewById(R.id.no_ads_loading).setVisibility(View.GONE);
//                        Toast.makeText(getApplicationContext(),getString(R.string.no_data) , Toast.LENGTH_LONG).show();
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e("get ads", "parsing err "+e.toString()) ;
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("ads  response  err" , error.toString()) ;
            }
        }
        ){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> params = new HashMap<>();
                params.put("user_id" , extra_model.getUser_id()) ;
                return  params ;
            }
        };
        int socketTimeout = 5000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout,
                12,
                1);

        requesturl.setRetryPolicy(policy);
        AppController.getInstance().addToRequestQueue(requesturl);



    }

    private void send_mess_req() {
        if (!validate_description())
            return;
        view.findViewById(R.id.loadingSpinner).setVisibility(View.VISIBLE);
        StringRequest user_req = new StringRequest(Request.Method.POST, Config.BASE_URL+"send_message.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("login response" , response) ;
                try {

                    JSONObject obj = new JSONObject(response);
                    String res =   obj.getString("response");
                    if (res.equals("success") ) {
                        view.findViewById(R.id.loadingSpinner).setVisibility(View.GONE);
                        dialog.setVisibility(View.GONE);

                    }
                }catch (Exception e) {
                    Log.e("login response  err" , e.toString()) ;
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("login err" , error.toString()) ;
                    }
                }
        ){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String , String> parmas = new HashMap<>() ;
                parmas.put("sender_id" , AppController.getInstance().getPrefManager().getUser().getUser_id()) ;
                parmas.put("receiver_id" , extra_model.getUser_id()) ;
                parmas.put("message" ,description.getText().toString().trim() ) ;
                parmas.put("mob" ,"android" ) ;
                parmas.put("ads_id" ,"profile" ) ;
                Log.e("params" , parmas.toString());
                return parmas;
            }

        };

        int socketTimeout = 10000; // 10 seconds. You can change it
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout,
                10,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);

        user_req.setRetryPolicy(policy);
        AppController.getInstance().getRequestQueue().add(user_req) ;

    }

    boolean validate_description (){
        return !(description.getText().toString().trim().equals(null) || description.getText().toString().trim().isEmpty());
    }

    @Override
    public void share(ProductModel prodauctModel, View img) {

    }

    @Override
    public void fav(String prodauctModel) {

    }

    @Override
    public void like(String prodauctModel, LikePlus likePlus) {

    }

    @Override
    public void start_detailes(ProductModel prodauctModel) {
        Intent intent = new Intent(getContext(),ProductDetailsActivity.class);
        intent.putExtra("extra_model",prodauctModel);
        startActivityForResult(intent,600);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == 200){
            int postion = ads_data.indexOf((ProductModel)data.getExtras().get("model") );
            ads_data.remove(postion);
            ads_adapter.notifyItemRemoved(postion);
            Log.e("codes results" , requestCode +"   "+resultCode + "  "+postion );

        }


    }
}
