package com.sayarat.Fragments;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.sayarat.Activity.ProductDetailsActivity;
import com.sayarat.Adapter.FAvAdapter;
import com.sayarat.interfaces.Add_listener;
import com.sayarat.Models.ProductModel;
import com.sayarat.Models.UserModel;
import com.sayarat.R;
import com.sayarat.app.AppController;
import com.sayarat.app.Config;
import com.sayarat.interfaces.LikePlus;
import com.sayarat.interfaces.RecyclerViewTouchHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.security.spec.ECField;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by ahmed on 2/24/2017.
 */

public class MyFavoritesFragment extends Fragment implements Add_listener {
    View view;
    ArrayList<ProductModel> ads_data = new ArrayList<>()  ;
    ArrayList<String> productModel_imgs;
    UserModel extra_model ;
    private FAvAdapter ads_adapter;

    public void setExtra_model(UserModel extra_model) {
        this.extra_model = extra_model;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
         view = inflater.inflate(R.layout.myfavorites_user_profile,container,false);

        ad_recycler_setup();
        return view;
    }


    void ad_recycler_setup(){
        RecyclerView pager_RecyclerView = (RecyclerView) view.findViewById(R.id.myfavorites_recycler_view);
        pager_RecyclerView.addOnItemTouchListener(new RecyclerViewTouchHelper(getContext(), pager_RecyclerView, new RecyclerViewTouchHelper.recyclerViewTouchListner() {
            @Override
            public void onclick(View child, int postion) {

            }

            @Override
            public void onLongClick(View child, int postion) {
                if (AppController.getInstance().getPrefManager().getUser().getUser_id().equals(extra_model.getUser_id()))
                delete_ad(postion);
            }
        }));
        pager_RecyclerView.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL , false));
        ads_adapter = new FAvAdapter(ads_data , getActivity(),this) ;
        pager_RecyclerView.setAdapter(ads_adapter);
        get_ads();

    }

    void get_ads () {


        StringRequest requesturl = new StringRequest(Request.Method.POST, Config.BASE_URL+"favorites.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("get ads", "respone "+response.toString()) ;

                try {
                    JSONObject response_obj = new JSONObject(response);
                    if (response_obj.getString("response").equals("success")){
                        view.findViewById(R.id.no_ads_loading).setVisibility(View.GONE);
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


                            UserModel user  = new UserModel();
                         try{
                             JSONObject temp = add.getJSONObject("user");
                              user = new UserModel(temp.getString("ID"),
                                     temp.getString("Name"),
                                     temp.getString("Phone"),
                                     temp.getString("Email"),
                                     temp.getString("picture")
                             );
                         }catch (Exception e){}
                            prodauctModel.setUserModel(user);
                            ads_data.add(prodauctModel);
                            Log.e("data", prodauctModel.getImgs().size()+"");
                        }

                        ads_adapter.notifyDataSetChanged();

                    }else {
                        view.findViewById(R.id.no_ads_loading).setVisibility(View.GONE);
                        view.findViewById(R.id.no_ads).setVisibility(View.VISIBLE);

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
        startActivity(intent);
    }


    void  delete_ad(final int postion){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder
                .setMessage(getString(R.string.delet_fav))
                .setPositiveButton("نعم", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        delete_ad_req(postion);
                    }
                })
                .setNegativeButton("لا", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
        builder.create().show();

    }
    private void delete_ad_req(final int postion) {
        view. findViewById(R.id.loadingSpinner).setVisibility(View.VISIBLE);
        StringRequest user_req = new StringRequest(Request.Method.POST, Config.BASE_URL+"delete_fav.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("login response" , response) ;
                try {

                    JSONObject obj = new JSONObject(response);
                    String res =   obj.getString("response");
                    if (res.equals("success") ) {
                        view.  findViewById(R.id.loadingSpinner).setVisibility(View.GONE);
                        ads_data.remove(postion);
                        ads_adapter.notifyDataSetChanged();
                    }
                }catch (Exception e) {
                    Log.e("login response  err" , e.toString()) ;
                    view. findViewById(R.id.loadingSpinner).setVisibility(View.GONE);
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("login err" , error.toString()) ;
                        view.findViewById(R.id.loadingSpinner).setVisibility(View.GONE);
                    }
                }
        ){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String , String> parmas = new HashMap<>() ;
                parmas.put("ad_id" , ads_data.get(postion).getProduct_id()) ;
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

}
