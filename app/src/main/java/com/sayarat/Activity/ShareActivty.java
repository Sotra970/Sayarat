package com.sayarat.Activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.sayarat.Adapter.AdsAdapter;
import com.sayarat.interfaces.Add_listener;
import com.sayarat.Models.ProductModel;
import com.sayarat.R;
import com.sayarat.interfaces.LikePlus;
import com.sayarat.transition.FabTransform;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class ShareActivty extends AppCompatActivity {

    private AdsAdapter ads_adapter;
    private RecyclerView ads;
    private ArrayList<ProductModel> ads_data = new ArrayList<>();
    private String TAG= "share actvity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_activty);

        View main_container = findViewById(R.id.search_main_container);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            FabTransform.setup(this, main_container);}

        try{
            ads_data.add((ProductModel) getIntent().getExtras().get("extra_product"));

        }catch (Exception e){
            finish();
        }
        ads = (RecyclerView) findViewById(R.id.ads_recycler) ;
        ads_adapter = new AdsAdapter(ads_data, this, new Add_listener() {
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

            }
        });
        ads.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        ads.setAdapter(ads_adapter);
        findViewById(R.id.share_ok).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                share(ads.getLayoutManager().findViewByPosition(0) );
            }
        });
        findViewById(R.id.share_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                supportFinishAfterTransition();
            }
        });
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


    void share(View img ){
        img.setDrawingCacheEnabled(true);
        Bitmap bitmap = Bitmap.createBitmap(img.getDrawingCache());
        share_ch(ads_data.get(0),bitmap);
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


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        new Handler().postDelayed(new Runnable() {

            /*
             * Showing splash screen with a timer. This will be useful when you
             * want to show case your app logo / company
             */

            @Override
            public void run() {

                supportFinishAfterTransition();
            }
        }, 300);

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
}
