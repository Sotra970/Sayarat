package com.sayarat.Activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.sayarat.Adapter.AdsAdapter;
import com.sayarat.Models.UserModel;
import com.sayarat.interfaces.Add_adv_listener;
import com.sayarat.interfaces.Add_listener;
import com.sayarat.Models.ProductModel;
import com.sayarat.R;
import com.sayarat.app.AppController;
import com.sayarat.app.Config;
import com.sayarat.interfaces.EndlessScrollListener;
import com.sayarat.interfaces.LikePlus;
import com.sayarat.transition.FabTransform;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class SearchActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener ,Add_listener{
    Spinner city_spinner, com_spinner, use_type_spinner, status_spinner, model_spinner, autamatic_spinner, set_spinner, counter_type_spinner, price_type_spinner;
    ArrayList<String> city = new ArrayList<>(), com = new ArrayList<>(), use_type = new ArrayList<>();
    ArrayList<String>status = new ArrayList<>(), model = new ArrayList<>(), sets = new ArrayList<>(), autamatic = new ArrayList<>(), counter_type = new ArrayList<>(), price_type= new ArrayList<>();
    ArrayList<String> city_id = new ArrayList<>(), model_id = new ArrayList<>(), com_id = new ArrayList<>();
    String city_txt = "", com_txt = "", use_type_txt = "", status_txt = "", model_txt = "", sets_txt = "", autamatic_txt = "", counter_type_txt = "", price_type_txt = "";
    Add_adv_listener add_adv_listener;
    ProductModel prodauctModel = new ProductModel();
    ArrayAdapter adapter ,adapter5 ,adapter4 ;
    EditText price , counter  ;
    CheckBox under_worenty ;
    HashMap<String,String> paramsg ;
    private Toolbar toolbar;
    RecyclerView ads ;
    AdsAdapter ads_adapter ;
    ArrayList<ProductModel> ads_data = new ArrayList<>() ;
    View search_btt , close;
    private String TAG = "search ac";
    EndlessScrollListener endlessScrollListener ;
    private LinearLayoutManager mLayoutManager;
    private int gpage = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        paramsg=new HashMap<>();

       View main_container = findViewById(R.id.search_main_container);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            FabTransform.setup(this, main_container);}

        setup_ads();
        search_btt = findViewById(R.id.search_btt);
        search_btt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gpage=0;
                search(0);
            }
        });
        close = findViewById(R.id.close);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        city_spinner = (Spinner) findViewById(R.id.city_spiner);
        city.add("المدينة");
        adapter = new ArrayAdapter(this.getBaseContext(), R.layout.simple_spinner_item, city);
        adapter.setDropDownViewResource(R.layout.simple_spinner_item);
        city_spinner.setAdapter(adapter);
        city_spinner.setOnItemSelectedListener(this);
        
        //com_spinner
        com_spinner = (Spinner) findViewById(R.id.com_spiner);
        com.add("الشركة");
        adapter4 = new ArrayAdapter(this.getBaseContext(), R.layout.simple_spinner_item, com);
        adapter4.setDropDownViewResource(R.layout.simple_spinner_item);
        com_spinner.setAdapter(adapter4);
        com_spinner.setOnItemSelectedListener(this);

        //model spinner
        model_spinner = (Spinner) findViewById(R.id.model_spiner);
        model.add("الموديل");
        adapter5 = new ArrayAdapter(this.getBaseContext(), R.layout.simple_spinner_item, model);
        adapter5.setDropDownViewResource(R.layout.simple_spinner_item);
        model_spinner.setAdapter(adapter5);
        model_spinner.setOnItemSelectedListener(this);
        //autamatic_spinner
        autamatic_spinner = (Spinner) findViewById(R.id.automatic_spiner);
        autamatic.add("نوع القير");
        autamatic.add("عادي");
        autamatic.add("اتامتيك");
        ArrayAdapter adapter3 = new ArrayAdapter(this.getBaseContext(), R.layout.simple_spinner_item, autamatic);
        adapter3.setDropDownViewResource(R.layout.simple_spinner_item);
        autamatic_spinner.setAdapter(adapter3);
        autamatic_spinner.setOnItemSelectedListener(this);


        //use_type_spiner
        use_type_spinner = (Spinner) findViewById(R.id.use_type_spiner);
        use_type.add("نوع الاستخدام ");
        use_type.add("خليجي");
        use_type.add("سعودي");
        use_type.add("امريكي");
        ArrayAdapter adapter2 = new ArrayAdapter(this.getBaseContext(), R.layout.simple_spinner_item, use_type);
        adapter2.setDropDownViewResource(R.layout.simple_spinner_item);
        use_type_spinner.setAdapter(adapter2);
        use_type_spinner.setOnItemSelectedListener(this);




        counter = (EditText) findViewById(R.id.add_counter_input) ;
        under_worenty = (CheckBox) findViewById(R.id.under_worenty) ;
        prodauctModel.setGuarantee("لا");
        under_worenty.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b){
                    prodauctModel.setGuarantee("نعم");
                    paramsg.put("Guarantee", "نعم");
                }
            }
        });

        get_data();
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/HelveticaNeueLT.otf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );

    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        if (i==0)
            return;
        switch (adapterView.getId()) {

            case R.id.city_spiner:
                city_txt = adapterView.getSelectedItem().toString();
                paramsg.put("Address", city_txt);

                break;
            case R.id.com_spiner:
                com_txt = adapterView.getSelectedItem().toString();
                int index= com.indexOf(com_txt) ;
                index--;
                paramsg.put("type",com_id.get((index)));
                break;
            case R.id.model_spiner:
                model_txt = adapterView.getSelectedItem().toString();
                paramsg.put("year", model_txt);
                break;
            case R.id.automatic_spiner:
                autamatic_txt = adapterView.getSelectedItem().toString();
                paramsg.put("automatic", autamatic_txt);
                break;
            case R.id.use_type_spiner:
                use_type_txt = adapterView.getSelectedItem().toString();
                paramsg.put("usingstatus", use_type_txt);
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        
    }


    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    void search(final int page){



//        prodauctModel.setAddress(address.getText().toString().trim());

        //prodauctModel.setOdemetervaue(counter.getText().toString().trim());

        if (page ==0){

            String counter_txt = counter.getText().toString().trim() ;
            if (!counter_txt.equals(null) && !counter_txt.isEmpty()){
                paramsg.put("odemetervalue",counter_txt);
            }

        }



        findViewById(R.id.loadingSpinner).setVisibility(View.VISIBLE);

        StringRequest requesturl = new StringRequest(Request.Method.POST, Config.BASE_URL +"search_car.php" , new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("get ads", "respone " + response.toString());

                try {
                    JSONObject response_obj = new JSONObject(response);
                    if (response_obj.getString("response").equals("success")){
                        JSONArray  feed_arr = response_obj.getJSONArray("data") ;
                        for (int i=ads_data.size(); i<feed_arr.length();i++){
                            JSONObject add = feed_arr.getJSONObject(i) ;


                            JSONArray images = add.getJSONArray("images") ;
                            ArrayList<String> imgs = new ArrayList<>();
                            for (int j=0; j<images.length();j++){
                                imgs.add(images.getJSONObject(j).getString("image").trim());
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

                            String price ;
                            try{
                                price = add.getString("pricevalue") ;
                                if (price.isEmpty()|| price.trim().equals("على السوم"))
                                    price = prodauctModel.getPricetype();
                                else  price = price.concat(getString(R.string.ryal));
                            }catch (Exception e){
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
                                ads_data.add(prodauctModel);
                            }catch (Exception e){

                            }
                            Log.e("data", prodauctModel.getImgs().size()+"");
                        }

                        ads_adapter.notifyDataSetChanged();




                        findViewById(R.id.loadingSpinner).setVisibility(View.GONE);
                        sucess_anim();
                    } else {
                        findViewById(R.id.loadingSpinner).setVisibility(View.GONE);
                        unsucess_anim();
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e("get ads", "parsing err " + e.toString());
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
                }
            }
        }
        ){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Log.e("add add params", paramsg.toString());
                paramsg.put("page", String.valueOf(page));

                return paramsg;
            }
        };
        int socketTimeout = 5000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout,
                12,
                1);

        requesturl.setRetryPolicy(policy);
        AppController.getInstance().addToRequestQueue(requesturl);


    }



    void get_data() {


        StringRequest requesturl = new StringRequest(Request.Method.POST, Config.BASE_URL + "spinner_detials.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("get ads", "respone " + response.toString());

                try {
                    JSONObject response_obj = new JSONObject(response);
                    if (response_obj.getString("response").equals("success")) {
                        JSONObject data = response_obj.getJSONObject("data");
                        JSONArray city_obj = data.getJSONArray("city");
                        JSONArray model_obl = data.getJSONArray("model");
                        JSONArray company_obj = data.getJSONArray("company");
                        for (int i = 0; i < city_obj.length(); i++) {
                            JSONObject current = city_obj.getJSONObject(i);
                            city.add(current.getString("city_name"));
                            city_id.add(current.getString("city_id"));
                        }
                        adapter.notifyDataSetChanged();
                        for (int i = 0; i < model_obl.length(); i++) {
                            JSONObject current = model_obl.getJSONObject(i);
                            model.add(current.getString("model_name"));
                            model_id.add(current.getString("model_id"));
                        }
                        adapter5.notifyDataSetChanged();
                        for (int i = 0; i < company_obj.length(); i++) {
                            JSONObject current = company_obj.getJSONObject(i);
                            com.add(current.getString("com_name"));
                            com_id.add(current.getString("com_id"));
                        }
                        adapter4.notifyDataSetChanged();

                    } else {
//                        Toast.makeText(getApplicationContext(),getString(R.string.no_data) , Toast.LENGTH_LONG).show();
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e("get ads", "parsing err " + e.toString());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("ads  response  err", error.toString());
            }
        }
        );
        int socketTimeout = 5000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout,
                12,
                1);

        requesturl.setRetryPolicy(policy);
        AppController.getInstance().addToRequestQueue(requesturl);


    }
    void  setup_ads(){
        ads = (RecyclerView) findViewById(R.id.res_list_view) ;
        ads_adapter = new AdsAdapter(ads_data , this,this);
        mLayoutManager = new LinearLayoutManager(getApplicationContext());
        endlessScrollListener = new EndlessScrollListener(mLayoutManager) {

            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                Log.e("ads page",page + "");
                if (gpage !=-1){
                    gpage = page ;
                    search(page);
                }

            }
        };
        ads.addOnScrollListener(endlessScrollListener);
        ads.setLayoutManager(mLayoutManager);
        ads.setAdapter(ads_adapter);

    }
    void unsucess_anim (){


        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                final View sucess_con = (findViewById(R.id.unsucess_con)) ;
                View sucess_txt= (findViewById(R.id.unsucess_text)) ;
                sucess_con.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        sucess_con.setVisibility(View.VISIBLE);
                    }
                },50);
                findViewById(R.id.search_content).animate().alpha(0).setDuration(200).setInterpolator(new AccelerateInterpolator()) ;
                sucess_con.animate().alpha(1).setStartDelay(100).setDuration(100).setInterpolator(new AccelerateInterpolator());
                findViewById(R.id.search_main_container).animate().scaleY(0).setDuration(200).setStartDelay(50).setInterpolator(new AccelerateDecelerateInterpolator());
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    sucess_con.animate().translationZ(6).setInterpolator(new AccelerateDecelerateInterpolator());
                }

                sucess_txt.animate().alpha(1).setStartDelay(150).setDuration(100).setInterpolator(new AccelerateInterpolator());
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        supportFinishAfterTransition();
                    }
                },1000);
            }
        }, 300);



    }
    private void sucess_anim() {

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                findViewById(R.id.search_res).setVisibility(View.VISIBLE);
                findViewById(R.id.search_content).animate().alpha(0).setDuration(200).setInterpolator(new AccelerateInterpolator()) ;
                findViewById(R.id.search_main_container).animate().scaleY(0).setDuration(200).setStartDelay(50).setInterpolator(new AccelerateDecelerateInterpolator());

                findViewById(R.id.res_list_view).animate().alpha(1).setDuration(300).setStartDelay(400).setInterpolator(new AccelerateInterpolator()) ;
                findViewById(R.id.search_res).animate().scaleY(1).setDuration(300).setStartDelay(240).setInterpolator(new AccelerateDecelerateInterpolator());



            }
        }, 300);

    }

    @Override
    public void onBackPressed() {
        if (findViewById(R.id.search_res).getScaleY()>0.5){
            findViewById(R.id.res_list_view).animate().alpha(0).setDuration(300).setInterpolator(new AccelerateInterpolator()) ;
            findViewById(R.id.search_res).animate().scaleY(0).setDuration(200).setStartDelay(150).setInterpolator(new AccelerateDecelerateInterpolator());
            supportFinishAfterTransition();

        }else {
            supportFinishAfterTransition();
        }

    }




    @Override
    public void share(ProductModel prodauctModel, View img) {
        img.setDrawingCacheEnabled(true);
        Bitmap bitmap = Bitmap.createBitmap(img.getDrawingCache());
        share_ch(prodauctModel,bitmap);    }

    @Override
    public void fav(String prodauctModel) {
        fav_req(prodauctModel);

    }

    @Override
    public void like(String prodauctModel, LikePlus likePlus) {
        if (AppController.getInstance().getPrefManager().getUser() != null)
            like_req(prodauctModel,likePlus);
        else
            startActivity(new Intent(getApplicationContext(), LoginActivity.class));


    }

    @Override
    public void start_detailes(ProductModel prodauctModel) {
        Intent intent = new Intent(getApplicationContext(),ProductDetailsActivity.class);
        intent.putExtra("extra_model",prodauctModel);
        startActivity(intent);
    }




    void like_req(final String id, final LikePlus likePlus){

        StringRequest requesturl = new StringRequest(Request.Method.POST, Config.BASE_URL +"add_ads_likes.php" , new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("get ads", "respone " + response.toString());

                try {
                    JSONObject response_obj = new JSONObject(response);
                    if (response_obj.getString("response").equals("success")){
                        String message = "تم اضافة الاعجاب";
                        Toast.makeText(getApplicationContext(), "" + message, Toast.LENGTH_LONG).show();
                        int edited_pos =    likePlus.plusOne();
                        ProductModel edited_model = ads_data.get(edited_pos);
                        int prv_likes = Integer.valueOf(edited_model.getLikes());
                        edited_model.setLikes(String.valueOf(++prv_likes));
                        ads_data.set(edited_pos,edited_model);
                    }


                    else {
                        String message = response_obj.getString("error");
                        Toast.makeText(getApplicationContext(), "" + message, Toast.LENGTH_LONG).show();
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e("get ads", "parsing err " + e.toString());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("ads  response  err", error.toString());
                if (error instanceof NoConnectionError) {
                    String message =   "يرجى التاكد من الانترنت و اعادة المحاوله";
                    Toast.makeText(getApplicationContext(), "" + message, Toast.LENGTH_LONG).show();
                }
            }
        }
        ){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> params = new HashMap<>();
                params.put("user_id",AppController.getInstance().getPrefManager().getUser().getUser_id());
                params.put("ad_id",id);


                Log.e("add add params", params.toString());

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

    void fav_req(final String id){

        StringRequest requesturl = new StringRequest(Request.Method.POST, Config.BASE_URL +"add_ads_likes.php" , new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("get ads", "respone " + response.toString());

                try {
                    JSONObject response_obj = new JSONObject(response);
                    if (response_obj.getString("response").equals("success")){
                        String message = "تم الاضافة للمفضلة";
                        Toast.makeText(getApplicationContext(), "" + message, Toast.LENGTH_LONG).show();
                    }


                    else {
                        String message = "لم يتم الاضافة للمفضلة";
                        Toast.makeText(getApplicationContext(), "" + message, Toast.LENGTH_LONG).show();
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e("get ads", "parsing err " + e.toString());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("ads  response  err", error.toString());
                if (error instanceof NoConnectionError) {
                    String message =   "يرجى التاكد من الانترنت و اعادة المحاوله";
                    Toast.makeText(getApplicationContext(), "" + message, Toast.LENGTH_LONG).show();
                }
            }
        }
        ){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> params = new HashMap<>();
                params.put("user_id",AppController.getInstance().getPrefManager().getUser().getUser_id());
                params.put("ad_id",id);


                Log.e("add add params", params.toString());

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


    protected void share_ch(ProductModel current , Bitmap img) {
        // Get access to the URI for the bitmap
        Uri bmpUri = null;
        try {
//            Bitmap bitmap = ( (BitmapDrawable) img.getDrawable() ) .getBitmap();
            bmpUri = Uri.fromFile(bitmaptofile(img));
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (bmpUri != null) {
            Log.e(TAG,"image ," + bmpUri);
            // set up an intent to share the image
            Intent share_intent = new Intent();
            share_intent.setAction(Intent.ACTION_SEND);
            share_intent.setType("*/*");

            share_intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            share_intent.putExtra(Intent.EXTRA_SUBJECT,
                    "اعلان من سيارات");
            share_intent.putExtra(Intent.EXTRA_TEXT,getCaption(current));
            share_intent.putExtra(Intent.EXTRA_STREAM,bmpUri);
            // start the intent
            try {
                startActivity(Intent.createChooser(share_intent,
                        "سيارات"));
            } catch (android.content.ActivityNotFoundException ex) {
                (new AlertDialog.Builder(this)
                        .setMessage("Share failed")
                        .setPositiveButton("OK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,
                                                        int whichButton) {
                                    }
                                }).create()).show();
            }
        }else {
            Log.e(TAG,"image , null");
        }
    }

    protected File bitmaptofile(Bitmap bitmap) throws IOException {
        File outputDir = getCacheDir(); // context being the Activity pointer

        File outputFile = new File(outputDir, currentDateFormat()+".jpg" );
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(outputFile);

            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream);
            fileOutputStream.flush();
            fileOutputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return outputFile;
    }

    public static String currentDateFormat(){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy_MMdd_HH_mm_ss");
        String  currentTimeStamp = dateFormat.format(new Date());
        return currentTimeStamp;
    }

        private  void startDetailACtivity( View view, ProductModel selected , View screenshot){


            Intent intent = new Intent(getApplicationContext(),ProductDetailsActivity.class);
            intent.putExtra("extra_model",selected);


            screenshot.setDrawingCacheEnabled(true);
            Bitmap bitmap = Bitmap.createBitmap(screenshot.getDrawingCache());

            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
            byte[] byteArray = stream.toByteArray();
            intent.putExtra("screenshot",byteArray);
//        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK) ;
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
//
//            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(this,
//                    view, getString(R.string.news_Image_shared_transition_name));
//            startActivity(intent,options.toBundle());
//        }else {
//            startActivity(intent);
//
//        }
            startActivity(intent);


        }


    protected  String getCaption(ProductModel feed){
        String caption = "";
        if (!feed.getTitle().equals(null)){
            caption = caption + getString(R.string.title) + " : " +  feed.getTitle()+ "\r\n" ;
        }
        if (!feed.getDetails().equals(null)){
            caption = caption +  feed.getDetails()+ "\r\n" ;
        }
        if (!feed.getPricevalue().equals(null)){
            caption = caption + getString(R.string.price) + " : " + feed.getPricevalue() + "\r\n" ;
        }

        caption = caption +getString(R.string.download_android_app) +" : " +getString(R.string.android_app_link) + "\r\n" ;
        caption = caption +getString(R.string.download_ios_app) +" : " +getString(R.string.ios_link) + "\r\n" ;
        caption = caption +getString(R.string.browseWeb) +" : " +getString(R.string.website_link) + "\r\n" ;
        Log.e("caption" , caption);
        return caption ;
    }
}
