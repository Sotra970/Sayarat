package com.sayarat.Activity;

import android.Manifest;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.PorterDuff;
import android.os.Build;
import android.os.Handler;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.sayarat.Adapter.No_Title_Pager_Adapter;
import com.sayarat.Models.UserModel;
import com.sayarat.interfaces.Add_adv_listener;
import com.sayarat.Fragments.Add_Car_Fragment;
import com.sayarat.Fragments.Add_adv_adertiser_info_Fragment;
import com.sayarat.Fragments.Add_car_pic_Fragment;
import com.sayarat.Models.ProductModel;
import com.sayarat.R;
import com.sayarat.app.AppController;
import com.sayarat.app.Config;
import com.sayarat.tabHolder.TabIconHolder;
import com.sayarat.transition.FabTransform;
import com.sayarat.widget.NonSwipeableViewPager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class UploadActivity extends AppCompatActivity  implements Add_adv_listener{
    private static final int MY_PERMISSIONS_REQUEST_STORAGE = 262 ;
    Toolbar toolbar ;
    View search_action , inbox_action;
    TextView page_title  ;
    ViewPager view_pager ;
    TabLayout tablayout ;
    View main_container ;
    HashMap<String , String> params;
    String type ;
    ProductModel model ;
    private String request;
    boolean wait = false ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (savedInstanceState == null) no_storage_permission();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);
        // start ini
        main_container = findViewById(R.id.upload_main_container);
type=getIntent().getExtras().getString("type");

        // end ini
        toolbar_action_setup(getString(R.string.add_adv)) ;
        setupViewPager();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            FabTransform.setup(this, main_container);}


        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/HelveticaNeueLT.otf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );

    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }


    void setupViewPager(){
        view_pager = (NonSwipeableViewPager) findViewById(R.id.addproduct_viewpager) ;
        tablayout = (TabLayout) findViewById(R.id.addproduct_tabLayout) ;
        No_Title_Pager_Adapter viewPagerAdapter = new No_Title_Pager_Adapter(getSupportFragmentManager());
        Add_Car_Fragment add_car_fragment = new Add_Car_Fragment() ;

        Add_car_pic_Fragment addCarPicFragment = new Add_car_pic_Fragment() ;

        Add_adv_adertiser_info_Fragment add_adv_adertiser_info_fragment = new Add_adv_adertiser_info_Fragment() ;
        model = (ProductModel) getIntent().getExtras().get("model");
        if (model!=null) {
            add_car_fragment.setExtra_model(model);
            addCarPicFragment.setExtra_model(model);
            add_adv_adertiser_info_fragment.setExtra_model(model);
            request = "update_ad";
        }


        add_car_fragment.setAdd_adv_listener(this);
        addCarPicFragment.setAdd_adv_listener(this);
        add_adv_adertiser_info_fragment.setAdd_adv_listener(this);

        viewPagerAdapter.addFragment(add_car_fragment);
        if (type.equals("add_car"))
            viewPagerAdapter.addFragment(addCarPicFragment);

        viewPagerAdapter.addFragment(add_adv_adertiser_info_fragment);

        view_pager.setAdapter(viewPagerAdapter);
        tablayout.setupWithViewPager(view_pager);





                tablayout.getTabAt(0).setCustomView(new TabIconHolder(R.drawable.ic_add_white_24dp , getString(R.string.add_adv) , this).getView() ) ;

                if (type.equals("add_car"))
                tablayout.getTabAt(1).setCustomView(new TabIconHolder(R.drawable.ic_add_a_photo_white_24dp , getString(R.string.add_pic) , this).getView() ) ;

        if (type.equals("add_car"))

            tablayout.getTabAt(2).setCustomView(new TabIconHolder(R.drawable.ic_person_white_24dp , getString(R.string.add_buyer_info) , this).getView() ) ;
        else
            tablayout.getTabAt(1).setCustomView(new TabIconHolder(R.drawable.ic_person_white_24dp , getString(R.string.moshtary) , this).getView() ) ;


         tablayout.getTabAt(0).getCustomView().setAlpha(1);
        tablayout.post(new Runnable() {
            @Override
            public void run() {
                View view =  tablayout.getTabAt(0).getCustomView() ;
                ((ImageView)view.findViewById(R.id.holderTabIcon)).setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.colorAccent), PorterDuff.Mode.SRC_ATOP);
                ((TextView) view.findViewById(R.id.holderTabTitle)).setTextColor(ContextCompat.getColor(getApplicationContext(),R.color.colorAccent));
            }
            });


        tablayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
//                    tab.getCustomView().animate().alpha(1).setDuration(300).setInterpolator(new AccelerateInterpolator());
                    View view = tab.getCustomView() ;
                    ((ImageView)view.findViewById(R.id.holderTabIcon)).setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.colorAccent), PorterDuff.Mode.SRC_ATOP);
                    ((TextView) view.findViewById(R.id.holderTabTitle)).setTextColor(ContextCompat.getColor(getApplicationContext(),R.color.colorAccent));
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                View view = tab.getCustomView() ;
                    ((ImageView)view.findViewById(R.id.holderTabIcon)).clearColorFilter();
                ((TextView) view.findViewById(R.id.holderTabTitle)).setTextColor(ContextCompat.getColor(getApplicationContext(),R.color.tabs_black));
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    }

    void toolbar_action_setup(String title){
        toolbar = (Toolbar) findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);
        View search_action = findViewById(R.id.main_toolbar_search);
        View menu_action = findViewById(R.id.main_toolbar_inbox);
        TextView page_title = (TextView) findViewById(R.id.main_toolbar_title);
        page_title.setText(title);
        search_action.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startSearchActivity(view);
            }
        });
        menu_action.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startMessageActivity(view);
            }
        });

    }


    void startMessageActivity(View view ){
        Intent intent = null;
        intent = new Intent(getApplicationContext(), MessActivity.class);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(this,
                    view, getApplication().getString(R.string.Mess_transtion));
            FabTransform.addExtras(intent,
                    ContextCompat.getColor(getApplicationContext(), R.color.colorPrimary),R.drawable.ic_mail_white_24dp);

            startActivity(intent,options.toBundle());
        }else {
            startActivity(intent);
        }
    }

    void startSearchActivity(View view ){
        Intent intent = null;
        intent = new Intent(getApplicationContext(), SearchActivity.class);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(this,
                    view, getApplication().getString(R.string.search_transtion));
            FabTransform.addExtras(intent,
                    ContextCompat.getColor(getApplicationContext(), R.color.colorPrimary),R.drawable.ic_search_white_24dp);

            startActivity(intent,options.toBundle());
        }else {
            startActivity(intent);
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_STORAGE: {

                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                } else {
                    //  no_gps_permition();
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(this, "الإذن لازم لتشغيل التطبيق الخاص بك", Toast.LENGTH_SHORT).show();
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }
    private void no_storage_permission() {
        // Here, thisActivity is the current activity
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ) {



            // No explanation needed, we can request the permission.

            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_STORAGE);


        }
    }


    @Override
    public void baisc_info(ProductModel prodauctModel) {
        try {

            params=new HashMap<>();
            tablayout.getTabAt(1).select();
            params.put("type", prodauctModel.getType());
            params.put("user_id", AppController.getInstance().getPrefManager().getUser().getUser_id());
            params.put("make", prodauctModel.getMake());
            params.put("year", prodauctModel.getYear());
            params.put("using_status", prodauctModel.getUsingstatus());
            params.put("pricetype", prodauctModel.getPricetype());
            params.put("pricevalue", prodauctModel.getPricevalue());
            params.put("odemetertype", prodauctModel.getOdemetertype());
            params.put("odemetervalue", prodauctModel.getOdemetervaue());
            params.put("guarantee", prodauctModel.getGuarantee());
            params.put("automatic", prodauctModel.getAutomatic());
            params.put("vehicleset", prodauctModel.getVehicleset());
            params.put("details", prodauctModel.getDetails());
            params.put("address", prodauctModel.getAddress());
            params.put("vehiclestatus", prodauctModel.getVehiclestatus());
            params.put("title", prodauctModel.getTitle());
        }catch (NullPointerException e){

            Log.e("basi ads info", "null err " + e.toString());

        }

    }

    @Override
    public void Imgs_info(ProductModel prodauctModel) {

        tablayout.getTabAt(2).select() ;
        if (prodauctModel.getImgs().size()>0)
        params.put("imgs",prodauctModel.getImgs().toString().trim() );

    }

    @Override
    public void advertiser_info(ProductModel prodauctModel) {
        params.put("phone",prodauctModel.getPhone() );
        if (!wait)
        send_data();

//        tablayout.getTabAt(3).select() ;

    }
    void sucess_anim (final ProductModel model){


        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                final View sucess_con = (findViewById(R.id.sucess_con)) ;
                View sucess_txt= (findViewById(R.id.sucess_text)) ;
                sucess_con.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        sucess_con.setVisibility(View.VISIBLE);
                    }
                },50);
                view_pager.animate().alpha(0).setDuration(200).setInterpolator(new AccelerateInterpolator()) ;
                sucess_con.animate().alpha(1).setStartDelay(100).setDuration(100).setInterpolator(new AccelerateInterpolator());
                main_container.animate().scaleY(0).setDuration(200).setStartDelay(50).setInterpolator(new AccelerateDecelerateInterpolator());
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    sucess_con.animate().translationZ(6).setInterpolator(new AccelerateDecelerateInterpolator());
                }

                sucess_txt.animate().alpha(1).setStartDelay(150).setDuration(100).setInterpolator(new AccelerateInterpolator());
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        startDetailACtivity(model);
                        finish();
                    }
                },1000);
            }
        }, 300);



    }


    void send_data() {
        wait = true ;
        String url ;
        if (!type.equals("add_car"))
            url = "add_request_car.php";
        else url = "add_ads.php";


       try{
           if (request.equals("update_ad")){
               url = "update_ad.php" ;
           }

       }catch (NullPointerException e){}

        findViewById(R.id.loadingSpinner).setVisibility(View.VISIBLE);

        StringRequest requesturl = new StringRequest(Request.Method.POST, Config.BASE_URL +url , new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("get ads", "respone " + response.toString());
                ProductModel prodauctModel = new ProductModel() ;
                try {
                    JSONObject response_obj = new JSONObject(response);
                    if (response_obj.getString("response").equals("success")) {

                        JSONArray feed_arr = response_obj.getJSONArray("data") ;
                        for (int i=0; i<feed_arr.length();i++){
                            JSONObject add = feed_arr.getJSONObject(i) ;

                            JSONArray images = add.getJSONArray("images") ;
                            ArrayList<String> imgs = new ArrayList<>();
                            for (int j=0; j<images.length();j++){
                                imgs.add(images.getJSONObject(j).getString("image").trim());
                            }

                            prodauctModel.setImgs(imgs);
                            prodauctModel.setType(add.getString("type"));
                            prodauctModel.setTitle(add.getString("title"));
                            prodauctModel.setMake(add.getString("make"));
                            prodauctModel.setYear(add.getString("year"));
                            prodauctModel.setAdvertisertype(add.getString("advertisertype"));
                            prodauctModel.setUsingstatus(add.getString("usingstatus"));
                            prodauctModel.setPricetype(add.getString("pricetype"));

                            String price ;
                            try{
                                price = add.getString("pricevalue") ;
                                if (price.isEmpty()|| price.trim().equals("على السوم"))
                                    price = prodauctModel.getPricetype();
                                else  price = price.concat(getString(R.string.ryal));
                            }catch (NullPointerException e){
                                price = prodauctModel.getPricetype();
                            }
                            prodauctModel.setPricevalue(price);

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
                            try{

                                JSONObject temp = add.getJSONObject("user");
                                UserModel user = new UserModel(temp.getString("ID"),
                                        temp.getString("Name"),
                                        temp.getString("Phone"),
                                        temp.getString("Email"),
                                        temp.getString("picture")
                                );

                                prodauctModel.setUserModel(user);
                            }catch (Exception e){

                            }
                            Log.e("data", prodauctModel.getImgs().size()+"");
                        }


                        findViewById(R.id.loadingSpinner).setVisibility(View.GONE);
                        sucess_anim(prodauctModel);
                    } else {
                        wait = false;
                       Toast.makeText(getApplicationContext(),getString(R.string.opp_fail) , Toast.LENGTH_LONG).show();
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e("get ads", "parsing err " + e.toString());
                    wait = false ;
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("ads  response  err", error.toString());
                if (error instanceof NoConnectionError) {
                    findViewById(R.id.loadingSpinner).setVisibility(View.GONE);
                    String message =   "يرجى التاكد من الانترنت و اعادة المحاوله";
                    Toast.makeText(getApplicationContext(), "" + message, Toast.LENGTH_LONG).show();
                    wait = false ;
                }
            }
        }
        ){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Log.e("add add params", params.toString());

                try{
                    if (request.equals("update_ad")){
                        params.put("add_id",model.getProduct_id());
                    }

                }catch (NullPointerException e){}
                return params;
            }
        };
        int socketTimeout = 1000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout,
                12,
                1);

        requesturl.setRetryPolicy(policy);
        AppController.getInstance().addToRequestQueue(requesturl);


    }


    private  void startDetailACtivity(  ProductModel selected){
        Intent intent = new Intent(getApplicationContext(),ProductDetailsActivity.class);
        intent.putExtra("extra_model",selected);

        startActivity(intent);


    }

}
