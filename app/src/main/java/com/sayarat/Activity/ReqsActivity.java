package com.sayarat.Activity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.sayarat.Adapter.AdsAdapter;
import com.sayarat.Adapter.ReqAdapter;
import com.sayarat.Models.ProductModel;
import com.sayarat.Models.UserModel;
import com.sayarat.R;
import com.sayarat.app.AppController;
import com.sayarat.app.Config;
import com.sayarat.interfaces.EndlessScrollListener;
import com.sayarat.interfaces.req_listener;
import com.sayarat.transition.FabTransform;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ReqsActivity extends AppCompatActivity implements req_listener {
    private SwipeRefreshLayout swipeRefresh;
    RecyclerView ads ;
    ReqAdapter ads_adapter ;
    private LinearLayoutManager mLayoutManager;
    private EndlessScrollListener endlessScrollListener;
    private int gpage=0;
    private ArrayList<ProductModel> ads_data = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reqs);
        toolbar_action_setup();
        setup_ads();

    }



    void toolbar_action_setup(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        View search_action = findViewById(R.id.main_toolbar_search);
        View menu_action = findViewById(R.id.main_toolbar_inbox);
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() ==android.R.id.home )
            onBackPressed();
        return true;
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

    void  setup_ads(){
        mLayoutManager = new LinearLayoutManager(this);
        ads = (RecyclerView) findViewById(R.id.ads_recycler) ;
        ads.setHasFixedSize(true);

        // Attach the listener to the AdapterView onCreate
        endlessScrollListener = new EndlessScrollListener(mLayoutManager) {

            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                Log.e("ads page",page + "");
                gpage = page ;
                get_ads(page);
            }
        };
//        endlessScrollListener =new EndlessScrollListener(mLayoutManager) {
//            @Override
//            public void onLoadMore(int current_page) {
//                Log.e("ads page",current_page + "");
//                gpage = 0 ;
//                get_ads(current_page);
//            }
//        };

        ads.setLayoutManager(mLayoutManager);

        ads_adapter = new ReqAdapter(ads_data , this , this);
        ads.setAdapter(ads_adapter);
        ads.addOnScrollListener(endlessScrollListener);
        setupRefreshLayout();
        get_ads(0);
    }
    private void setupRefreshLayout(){
        swipeRefresh = (SwipeRefreshLayout) findViewById(R.id.swipeRefresh);
        swipeRefresh.setColorSchemeColors(ContextCompat.getColor(getApplicationContext(),R.color.colorAccent));
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                get_ads(gpage);
            }
        });
    }

    private void endLoading() {
        swipeRefresh.post(new Runnable() {
            @Override
            public void run() {
                swipeRefresh.setRefreshing(false);
            }
        });
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR1) {
            ads.setAlpha(0);
            ads.animate().alpha(1);
        }else {
            ads.setVisibility(View.VISIBLE);
        }
    }

    void show_loading(){
        swipeRefresh.post(new Runnable() {
            @Override
            public void run() {
                swipeRefresh.setRefreshing(true);
            }
        });

    }
    void get_ads (final int page) {
        show_loading();

        StringRequest requesturl = new StringRequest(Request.Method.POST, Config.BASE_URL+"requests.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("get ads", "respone "+response.toString()) ;

                findViewById(R.id.progressBar).setVisibility(View.GONE);
                try {
                    JSONObject response_obj = new JSONObject(response);
                    if (response_obj.getString("response").equals("success")){
                        JSONArray feed_arr = response_obj.getJSONArray("data") ;
                        for (int i=ads_data.size(); i<feed_arr.length();i++){
                            JSONObject add = feed_arr.getJSONObject(i) ;


                            ProductModel prodauctModel = new ProductModel() ;
                            try{
                                prodauctModel.setMake(add.getString("make"));

                            }catch (Exception e){

                            }
                            try{
                                prodauctModel.setYear(add.getString("year"));

                            }catch (Exception e){

                            }

                            try{
                                prodauctModel.setType(add.getString("type"));

                            }catch (Exception e){

                            }


                            try{

                                prodauctModel.setTitle(add.getString("title"));

                            }catch (Exception e){

                            }


                            try{
                                prodauctModel.setUsingstatus(add.getString("usingstatus"));


                            }catch (Exception e){

                            }

                            try{

                                prodauctModel.setAdvertisertype(add.getString("advertisertype"));

                            }catch (Exception e){

                            }

                            try{
                                prodauctModel.setPricetype(add.getString("pricetype"));

                            }catch (Exception e){

                            }



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

                            try{
                                prodauctModel.setDetails(add.getString("details"));

                            }catch (Exception e){

                            }


                            try{
                                prodauctModel.setOdemetertype(add.getString("odemetertype"));

                            }catch (Exception e){

                            }

                            try{
                                prodauctModel.setOdemetervaue(add.getString("odemetervaue"));

                            }catch (Exception e){

                            }

                            try{

                                prodauctModel.setGuarantee(add.getString("Guarantee"));

                            }catch (Exception e){

                            }

                            try{
                                prodauctModel.setAdvertiserID(add.getString("RequsterID"));

                            }catch (Exception e){

                            }

                            try{

                                prodauctModel.setDate(add.getString("date"));

                            }catch (Exception e){

                            }

                            try{

                                prodauctModel.setAutomatic(add.getString("automatic"));

                            }catch (Exception e){

                            }

                            try{

                                prodauctModel.setProduct_id(add.getString("Id"));

                            }catch (Exception e){

                            }

                            try{
                                prodauctModel.setAddress(add.getString("Address"));

                            }catch (Exception e){

                            }

                            try{
                                prodauctModel.setActive(add.getString("Active"));

                            }catch (Exception e){

                            }

                            try{

                                prodauctModel.setPhone(add.getString("phone"));

                            }catch (Exception e){

                            }
                            try{

                                prodauctModel.setVehiclestatus(add.getString("vehiclestatus"));

                            }catch (Exception e){

                            }
                            try{

                                prodauctModel.setVehicleset(add.getString("vehicleset"));


                            }catch (Exception e){

                            }
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

                        ads_data.add(prodauctModel);
                            Log.e("data", prodauctModel.getImgs().size()+"");
                        }
                        endLoading();

                        ads_adapter.notifyDataSetChanged();
                        // endlessScrollListener.resetState();


                    }else {
                        endLoading();

                        Toast.makeText(getApplicationContext(),getString(R.string.no_data) , Toast.LENGTH_LONG).show();
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
                HashMap<String , String> params = new HashMap<>();
                params.put("page", String.valueOf(gpage));
                return params;
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
    public void start_profile(UserModel mdoel, View view) {
        startDetailACtivity(  view  , mdoel);

    }
    @Override
    public void start_detailes(ProductModel mdoel, View view) {
            startDetailACtivity(mdoel);
    }


    private  void startDetailACtivity( View view, UserModel selected){
        View sharedView = view.findViewById(R.id.comment_img);
        if (sharedView==null)
            sharedView=view;


        Intent intent = new Intent(getApplicationContext(),Profile_Activity.class);
        intent.putExtra("user_model",selected);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK) ;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){

            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(this,
                    sharedView, getString(R.string.profile_shared_transition_name));
            startActivity(intent,options.toBundle());
        }else {
            startActivity(intent);

        }

    }


    private  void startDetailACtivity(  ProductModel selected){
        Intent intent = new Intent(getApplicationContext(),ProductDetailsActivity.class);
        intent.putExtra("extra_model",selected);
        intent.putExtra("from","reqs");

        startActivity(intent);


    }

}
