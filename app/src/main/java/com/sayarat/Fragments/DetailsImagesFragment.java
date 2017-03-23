package com.sayarat.Fragments;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.sayarat.Activity.LoginActivity;
import com.sayarat.Activity.Profile_Activity;
import com.sayarat.Adapter.ImageAdapter;
import com.sayarat.Adapter.ProductDescriptionAdDetailsPagerAdapter;
import com.sayarat.Adapter.CommentsAdapter;
import com.sayarat.Models.ProductModel;
import com.sayarat.Models.UserModel;
import com.sayarat.R;
import com.sayarat.interfaces.RecyclerViewTouchHelper;
import com.sayarat.app.AppController;
import com.sayarat.app.CommentModel;
import com.sayarat.app.Config;
import com.sayarat.interfaces.comments_listener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by ahmed on 2/22/2017.
 */

public class DetailsImagesFragment extends Fragment implements comments_listener {
    View view;
    ImageButton send_comment ;
    EditText comment ;
    CommentsAdapter commentsAdapter;
    ProductModel extra_model,profile_extra_product ;
    ArrayList<String> productModel_imgs,profilePhoto_img  ;
    ArrayList<CommentModel> commentModels = new ArrayList<>();
    ViewPager viewPager ;
    TextView id , price_type , price_value , using_status , status , make , type ,model , city , autmatic , car_set ,add_time,name, title;

    public void setExtra_product(ProductModel extra_product) {
        this.extra_model = extra_product;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        if (view == null) {
            view = inflater.inflate(R.layout.fragment_product_description_ad_details, container, false);



            title = (TextView) view.findViewById(R.id.title);
            name = (TextView) view.findViewById(R.id.name);
            add_time = (TextView) view.findViewById(R.id.add_time);
                price_type = (TextView) view.findViewById(R.id.car_price_type);
                price_value = (TextView) view.findViewById(R.id.car_price_value);
                using_status = (TextView) view.findViewById(R.id.car_using_status);
                status = (TextView) view.findViewById(R.id.car_status);
                make = (TextView) view.findViewById(R.id.car_make);
                type = (TextView) view.findViewById(R.id.car_type);
                model = (TextView) view.findViewById(R.id.car_model);
                city = (TextView) view.findViewById(R.id.car_city);
                autmatic = (TextView) view.findViewById(R.id.car_auto);
                car_set = (TextView) view.findViewById(R.id.car_sset);


            name.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startDetailACtivity(name,extra_model.getUserModel());
                }
            });


            try {
                title      .setText(extra_model.getTitle());
            }catch (NullPointerException e){

            }
            try {
                add_time      .setText(extra_model.getDate());
            }catch (NullPointerException e){

            }
            try {
                name      .setText(extra_model.getUserModel().getUser_name());
            }catch (NullPointerException e){

            }
            try {
                autmatic      .setText(extra_model.getAutomatic());
            }catch (NullPointerException e){

            }

            try {
                city          .setText(extra_model.getAddress());

            }catch (NullPointerException e){

            }

            try {
                model         .setText(extra_model.getYear());

            }catch (NullPointerException e){

            }

            try {
                type          .setText(extra_model.getType());

            }catch (NullPointerException e){

            }

            try {
                car_set       .setText(extra_model.getVehicleset());

            }catch (NullPointerException e){

            }


            try {
                make          .setText(extra_model.getMake());

            }catch (NullPointerException e){

            }


            try {
                status        .setText(extra_model.getVehiclestatus());

            }catch (NullPointerException e){

            }

            try {
                using_status  .setText(extra_model.getUsingstatus());

            }catch (NullPointerException e){

            }

            try {
                price_value   .setText(extra_model.getPricevalue());

            }catch (NullPointerException e){

            }
            try {
                price_type    .setText(extra_model.getPricetype());

            }catch (NullPointerException e){

            }


              pager_setup();
              pager_recycler_setup();
              profile_recycler_setup();
              send_comment = (ImageButton) view.findViewById(R.id.send_comment);

              comment = (EditText) view.findViewById(R.id.write_comment)  ;

              send_comment.setOnClickListener(new View.OnClickListener() {
                  @Override
                  public void onClick(View view) {
                      if (AppController.getInstance().getPrefManager().getUser() !=null) {
                          if (!comment.getText().toString().trim().isEmpty())
                              send_comment_req();
                      }
                      else
                          startActivity(new Intent(getContext(),LoginActivity.class));

                  }
              });




        }
        return view;

    }


    void pager_setup(){
        viewPager = (ViewPager) view.findViewById(R.id.viewpager);
        if (extra_model.getImgs().isEmpty() || extra_model.getImgs().equals(null))
        {
            viewPager.setVisibility(View.GONE);
            return;
        }
        ProductDescriptionAdDetailsPagerAdapter ProductDescriptionAdDetailsPagerAdapter =
                new ProductDescriptionAdDetailsPagerAdapter(getContext() ,extra_model.getImgs() ) ;
        viewPager.setAdapter(ProductDescriptionAdDetailsPagerAdapter);


    }



    void pager_recycler_setup(){
        RecyclerView pager_RecyclerView = (RecyclerView) view.findViewById(R.id.pager_recycler_view);
        if (extra_model.getImgs().isEmpty() || extra_model.getImgs().equals(null))
        {
            pager_RecyclerView.setVisibility(View.GONE);
            return;
        }
        pager_RecyclerView.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL , false));
        SnapHelper snapHelper = new LinearSnapHelper() ;
        snapHelper.attachToRecyclerView(pager_RecyclerView);
        ImageAdapter imageAdapter = new ImageAdapter(extra_model.getImgs() , getContext()) ;
        pager_RecyclerView.setAdapter(imageAdapter);

        pager_RecyclerView.addOnItemTouchListener(new RecyclerViewTouchHelper(getContext(), pager_RecyclerView, new RecyclerViewTouchHelper.recyclerViewTouchListner() {
            @Override
            public void onclick(View child, int postion) {
                viewPager.setCurrentItem(postion,true);
            }

            @Override
            public void onLongClick(View child, int postion) {

            }
        }));


    }

    void profile_recycler_setup(){
        RecyclerView pager_RecyclerView = (RecyclerView) view.findViewById(R.id.comments);
        pager_RecyclerView.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL , false));
         commentsAdapter = new CommentsAdapter( commentModels, getActivity(),this) ;
        pager_RecyclerView.setAdapter(commentsAdapter);
        getComments();


    }

    void getComments(){
        Log.e("get comments " , "strat") ;
        StringRequest user_req = new StringRequest(Request.Method.POST, Config.BASE_URL+"comments.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("get comments  response" , response) ;
                try {
                    JSONObject response_obj = new JSONObject(response) ;


                    if (response_obj.getString("response").equals("empty")){
                        view.findViewById(R.id.loadingSpinner_comm).setVisibility(View.GONE);
                        view.findViewById(R.id.no_ads_loading).setVisibility(View.GONE);
                        view.findViewById(R.id.no_comments).setVisibility(View.VISIBLE);

                    }else {
                        view.findViewById(R.id.loadingSpinner_comm).setVisibility(View.GONE);
                        view.findViewById(R.id.no_ads_loading).setVisibility(View.GONE);
                        JSONArray fedarr = response_obj.getJSONArray("data") ;
                        for (int i= (commentModels.size()); i<fedarr.length() ; i++){
                            JSONObject temp = fedarr.getJSONObject(i) ;

                            CommentModel comements_model = new CommentModel() ;
                            comements_model.setComment(temp.getString("comment"));
                            comements_model.setDate(temp.getString("date"));

                            UserModel user = new UserModel(temp.getString("ID"),
                                    temp.getString("Name"),
                                    temp.getString("Phone"),
                                    temp.getString("Email"),
                                    temp.getString("picture")
                            );

                            comements_model.setUser(user);
                            commentModels.add(comements_model);
                        }

                        commentsAdapter.notifyDataSetChanged();

                    }
                }catch (Exception e) {
                    Log.e("comments response  err" , e.toString()) ;
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("comments err" , error.toString()) ;
                    }
                }
        ){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String , String> parmas = new HashMap<>() ;
                parmas.put("ad_id" ,extra_model.getProduct_id()) ;
                Log.e("params" , parmas.toString());
                Log.e("comments params" , parmas.toString()) ;
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

    private void send_comment_req() {
        if (comment.getText().toString().trim().trim().isEmpty())
            return;

        view.findViewById(R.id.loadingSpinner_comm).setVisibility(View.VISIBLE);
        StringRequest user_req = new StringRequest(Request.Method.POST, Config.BASE_URL+"add_comment.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("login response" , response) ;
                try {
                        JSONObject obj = new JSONObject(response.trim());
                    if (obj.getString("response").equals("success")){
                        Toast.makeText(getContext() , "تم اضافة التعليق ",Toast.LENGTH_LONG).show();
                        comment.setText("");
                        getComments();
                    }else {
                        view.findViewById(R.id.loadingSpinner_comm).setVisibility(View.GONE);
                        Toast.makeText(getContext() , "لم يتم اضافة التعليق ",Toast.LENGTH_LONG).show();
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
                parmas.put("user_id" , AppController.getInstance().getPrefManager().getUser().getUser_id()) ;
                parmas.put("ad_id" , extra_model.getProduct_id()) ;
                parmas.put("mob" ,"android" );
                parmas.put("comment" ,comment.getText().toString().trim() ) ;
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

    private  void startDetailACtivity( View view, UserModel selected){
        View sharedView = view.findViewById(R.id.comment_img);
        if (sharedView==null)
            sharedView=view;


        Intent intent = new Intent(getContext(),Profile_Activity.class);
        intent.putExtra("user_model",selected);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK) ;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){

            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(getActivity(),
                    sharedView, getString(R.string.profile_shared_transition_name));
            startActivity(intent,options.toBundle());
        }else {
            startActivity(intent);

        }

    }

    @Override
    public void start_profile(UserModel mdoel, View view) {
        startDetailACtivity(view,mdoel);
    }
}
