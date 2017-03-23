package com.sayarat.Activity;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
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
import com.sayarat.Adapter.Title_Pager_Adapter;
import com.sayarat.Fragments.DetailsDescriptionFragment;
import com.sayarat.Fragments.DetailsImagesFragment;
import com.sayarat.Models.ProductModel;
import com.sayarat.Models.UserModel;
import com.sayarat.R;
import com.sayarat.app.AppController;
import com.sayarat.transition.FabTransform;
import com.sayarat.widget.WrapContentViewPager;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;
import  com.sayarat.app.Config;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ProductDetailsActivity extends AppCompatActivity {
        ProductModel extra_model  ;
    ImageButton send_message_fab , call_fab , share_fab1, share_fab2 , repost_fab , delete_fab , edit_fab ;
    private String TAG="pd";
    Bitmap bitmap_sh ;
    private Toolbar toolbar;
    Bitmap screenshot ;
    private HashMap<String, String> params;
    String title ;
    boolean isorder = false ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);
        title = getString(R.string.add_details);
           // byte[] byteArray = getIntent().getByteArrayExtra("screenshot");
            extra_model = (ProductModel) getIntent().getExtras().get("extra_model");

           try {
               if (extra_model.getAdvertiserID().equals(AppController.getInstance().getPrefManager().getUser().getUser_id())) {
                   findViewById(R.id.users_fab).setVisibility(View.VISIBLE);
                   findViewById(R.id.not_users_fab).setVisibility(View.GONE);
               } else {
                   findViewById(R.id.users_fab).setVisibility(View.GONE);
                   findViewById(R.id.not_users_fab).setVisibility(View.VISIBLE);
               }


           }catch (NullPointerException e){
               findViewById(R.id.users_fab).setVisibility(View.GONE);
               findViewById(R.id.not_users_fab).setVisibility(View.VISIBLE);
           }


        if (getIntent().getExtras().getString("from") != null) {
            findViewById(R.id.users_fab).setVisibility(View.GONE);
            findViewById(R.id.not_users_fab).setVisibility(View.VISIBLE);
            title = getString(R.string.requests);
            isorder = true ;
        }
            start_bind();

        //  screenshot = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);


        toolbar_action_setup(title) ;


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
    void toolbar_action_setup(String title){
        toolbar = (Toolbar) findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
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
                if (AppController.getInstance().getPrefManager().getUser() !=null)
                startMessageActivity(view);
                else
                    startActivity(new Intent(getApplicationContext(),LoginActivity.class));
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
                    ContextCompat.getColor(getApplicationContext(), R.color.blue_gray),R.drawable.ic_share_white_24dp);

            startActivity(intent,options.toBundle());
        }else {
            startActivity(intent);
        }
    }

    void startShare(View view ){
        Intent intent = null;
        intent = new Intent(getApplicationContext(), ShareActivty.class);
        intent.putExtra("extra_product",extra_model);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(this,
                    view, getApplication().getString(R.string.search_transtion));
            FabTransform.addExtras(intent,
                    ContextCompat.getColor(getApplicationContext(), R.color.blue_gray),R.drawable.ic_search_white_24dp);

            startActivity(intent,options.toBundle());
        }else {
            startActivity(intent);
        }
    }
 void start_send_message(View view ){
        Intent intent = null;
     intent = new Intent(getApplicationContext(), SendMessageActivity.class);
     intent.putExtra("extra_model",extra_model.getAdvertiserID());
     intent.putExtra("ads_id",extra_model.getProduct_id());

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(this,
                    view, getApplication().getString(R.string.search_transtion));
            FabTransform.addExtras(intent,
                    ContextCompat.getColor(getApplicationContext(), R.color.blue_gray),R.drawable.ic_search_white_24dp);

            startActivity(intent,options.toBundle());
        }else {
            startActivity(intent);
        }
    }

    void  start_bind(){


        final WrapContentViewPager viewPager = (WrapContentViewPager) findViewById(R.id.pager);

        send_message_fab = (ImageButton) findViewById(R.id.send_message_fab);
        delete_fab = (ImageButton) findViewById(R.id.delete_fab);
        repost_fab = (ImageButton) findViewById(R.id.reshare_fab);
        edit_fab = (ImageButton) findViewById(R.id.edit_fab);

        delete_fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               delete_ad();
            }
        });
        edit_fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               update_ad(view);
            }
        });
        repost_fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               repost_ad();
            }
        });




        call_fab = (ImageButton) findViewById(R.id.call);
        call_fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri call = Uri.parse("tel:" + extra_model.getPhone());
                Intent surf = new Intent(Intent.ACTION_DIAL, call);
                startActivity(surf);
            }
        });
        share_fab1 = (ImageButton) findViewById(R.id.share);
        share_fab2 = (ImageButton) findViewById(R.id.share_fab_user);
        share_fab1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startShare(view);
            }
        });
        share_fab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startShare(view);
            }
        });
        send_message_fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (AppController.getInstance().getPrefManager().getUser()!= null)
                start_send_message(view);
                else
                    startActivity(new Intent(getApplicationContext() , LoginActivity.class));
            }
        });

        share_fab2 = (ImageButton) findViewById(R.id.share);
        share_fab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startShare(view);
            }
        });


        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);



        Title_Pager_Adapter viewPagerAdapter = new Title_Pager_Adapter(getSupportFragmentManager());

        DetailsImagesFragment productDescriptionAdDetails  = new DetailsImagesFragment();
        productDescriptionAdDetails.setExtra_product(extra_model);

        DetailsDescriptionFragment detailsDescriptionFragment = new DetailsDescriptionFragment();
        detailsDescriptionFragment.setExtra_model(extra_model);
        detailsDescriptionFragment.setOrder(isorder);

        if (!isorder)
        viewPagerAdapter.addFragment(productDescriptionAdDetails,getString(R.string.add_details));
        else
            viewPagerAdapter.addFragment(productDescriptionAdDetails,getString(R.string.request_details));

        viewPagerAdapter.addFragment(detailsDescriptionFragment,getString(R.string.car_dsc));

        viewPager.setAdapter(viewPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);




    }





    protected void share_ch(ProductModel current) {
        // Get access to the URI for the bitmap
        File outputDir = Environment.getExternalStorageDirectory(); // context being the Activity pointer

        Uri bmpUri = null;
        try {
            bmpUri = Uri.fromFile(new File(outputDir+current.getProduct_id()+".jpg"));
        } catch (Exception e) {
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
        File outputDir = Environment.getExternalStorageDirectory(); // context being the Activity pointer

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


    public void getAds() {


        StringRequest requesturl = new StringRequest(Request.Method.POST, Config.BASE_URL+"ads_details.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("get ads", "respone "+response.toString()) ;

                try {
                    findViewById(R.id.loadingSpinner).setVisibility(View.GONE);
                    JSONObject response_obj = new JSONObject(response);
                    if (response_obj.getString("response").equals("success")){
                        JSONArray feed_arr = response_obj.getJSONArray("data") ;
                        for (int i=0; i<feed_arr.length();i++){
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

                            extra_model = (prodauctModel);
                            Log.e("data", prodauctModel.getImgs().size()+"");
                            start_bind();
                        }


                    }else {
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
                HashMap hashMap = new HashMap();
                hashMap.put("add_id",getIntent().getExtras().getString("extra_id"));
                return hashMap ;
            }
        };
        int socketTimeout = 5000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout,
                12,
                1);

        requesturl.setRetryPolicy(policy);
        AppController.getInstance().addToRequestQueue(requesturl);



    }


    void  repost_ad(){
        Date date = new Date() ;
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss") ;
        String   timestamp = dateFormat.format(date);

        try {
            Date parsed = new Date();

            DateFormat format =
                    new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            parsed = format.parse(extra_model.getDate());



            Calendar c1 = Calendar.getInstance();
            Calendar c2 = Calendar.getInstance();

            c1.setTime(dateFormat.parse(timestamp));
            c2.setTime(parsed);

            int monthDiff = c1.get(Calendar.MONTH) - c2.get(Calendar.MONTH);


            if (monthDiff<1){

                within_mon();
            }else {
                send_data();
            }
        }
        catch(ParseException pe) {
            throw new IllegalArgumentException();
        }




    }
    void  update_ad(View view){
        startUploadActivity(view,"add_car");

    }

    void startUploadActivity(View view , String type ){
        Intent intent = null;
        intent = new Intent(getApplicationContext(), UploadActivity.class);
        intent.putExtra("type" , type) ;
        intent.putExtra("model" , extra_model) ;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(this,
                    view, getApplication().getString(R.string.add_adv));
            FabTransform.addExtras(intent,
                    ContextCompat.getColor(getApplicationContext(), R.color.blue_gray),R.drawable.ic_edit_white_36dp);

            startActivity(intent,options.toBundle());
        }else {
            startActivity(intent);
        }
    }


    void  delete_ad(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder
                .setMessage(getString(R.string.delete_confirm))
                .setPositiveButton("نعم", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        delete_ad_req();
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

    void  within_mon(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder
                .setMessage(getString(R.string.within_month))
                .setNegativeButton("موافق", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
        builder.create().show();

    }
    private void delete_ad_req() {
        findViewById(R.id.loadingSpinner).setVisibility(View.VISIBLE);
        StringRequest user_req = new StringRequest(Request.Method.POST, Config.BASE_URL+"delete_ads.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("login response" , response) ;
                try {

                    JSONObject obj = new JSONObject(response);
                    String res =   obj.getString("response");
                    if (res.equals("success") ) {
                        Intent intent = new Intent();
                        intent.putExtra("model",extra_model);
                        setResult(200,intent);
                        findViewById(R.id.loadingSpinner).setVisibility(View.GONE);
               supportFinishAfterTransition();
                    }
                }catch (Exception e) {
                    Log.e("login response  err" , e.toString()) ;
                    findViewById(R.id.loadingSpinner).setVisibility(View.GONE);
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("login err" , error.toString()) ;
                        findViewById(R.id.loadingSpinner).setVisibility(View.GONE);
                    }
                }
        ){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String , String> parmas = new HashMap<>() ;
                parmas.put("ad_id" , extra_model.getProduct_id()) ;
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



    void send_data() {


        findViewById(R.id.loadingSpinner).setVisibility(View.VISIBLE);

        StringRequest requesturl = new StringRequest(Request.Method.POST, Config.BASE_URL +"repost_add.php" , new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("get ads", "respone " + response.toString());
                ProductModel prodauctModel = new ProductModel() ;
                try {
                    findViewById(R.id.loadingSpinner).setVisibility(View.GONE);
                    JSONObject response_obj = new JSONObject(response);
                    if (response_obj.getString("response").equals("success")) {


                            Toast.makeText(getApplicationContext(),"تم اعادة النشر",Toast.LENGTH_LONG).show();
                            Log.e("data", prodauctModel.getImgs().size()+"");




                    } else {
                        Toast.makeText(getApplicationContext(),getString(R.string.opp_fail) , Toast.LENGTH_LONG).show();
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
              HashMap<String,String> params = new HashMap<>();
                params.put("add_id",extra_model.getProduct_id());
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


}
