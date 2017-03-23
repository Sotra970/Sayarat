package com.sayarat.Fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.sayarat.Adapter.ImageAdapter;
import com.sayarat.Adapter.ProductDescriptionAdDetailsPagerAdapter;
import com.sayarat.interfaces.Add_adv_listener;
import com.sayarat.Models.ProductModel;
import com.sayarat.R;
import com.sayarat.app.AndroidMultiPartEntity;
import com.sayarat.app.Config;
import com.sayarat.interfaces.RecyclerViewTouchHelper;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

/**
 * Created by ahmed on 2/22/2017.
 */

public class Add_car_pic_Fragment extends Fragment {
    View view , add_img;
    ProductModel prodauctModel =new ProductModel();
    protected int RESULT_LOAD_IMAGE=1;
    private static final int MY_PERMISSIONS_REQUEST_STORAGE = 262 ;
    ArrayList<Bitmap> upload_imgs = new ArrayList<>()  ;
    ArrayList<String> imgs =new ArrayList<>();
    private ImageAdapter imageAdapter;
    Add_adv_listener add_adv_listener ;
    TextView txtPercentage;
    ProgressBar progressBar ;
    View progressBarView ;
    ProductModel extra_model ;
    private ViewPager viewPager;

    public void setExtra_model(ProductModel extra_model) {
        this.extra_model = extra_model;
    }

    private ProductDescriptionAdDetailsPagerAdapter productDescriptionAdDetailsPagerAdapter;


    public void setAdd_adv_listener(Add_adv_listener add_adv_listener) {
        this.add_adv_listener = add_adv_listener;
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.add_car_pic_fragment,container,false) ;

        txtPercentage = (TextView) view.findViewById(R.id.txtPercentage);
        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        progressBarView = view.findViewById(R.id.progressBarView);


        if (extra_model!=null){

            imgs = extra_model.getImgs();

        }
        View continue_btt = view.findViewById(R.id.continue_btt) ;
        continue_btt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//               if (! validateImg())
//                   return;
                prodauctModel.setImgs(imgs);
                add_adv_listener.Imgs_info(prodauctModel);
            }
        });
        pager_setup();
        pager_recycler_setup();

        add_img = view.findViewById(R.id.add_img);
        add_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(
                        Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                startActivityForResult(i, RESULT_LOAD_IMAGE);
            }
        });


        return view;

    }


    void pager_setup(){
         viewPager = (ViewPager) view.findViewById(R.id.viewpager);
         productDescriptionAdDetailsPagerAdapter =
                new ProductDescriptionAdDetailsPagerAdapter(getContext() ,imgs) ;
        viewPager.setAdapter(productDescriptionAdDetailsPagerAdapter);

    }



    void pager_recycler_setup(){
        RecyclerView pager_RecyclerView = (RecyclerView) view.findViewById(R.id.pager_recycler_view);
        pager_RecyclerView.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL , false));
        SnapHelper snapHelper = new LinearSnapHelper() ;
        snapHelper.attachToRecyclerView(pager_RecyclerView);
         imageAdapter = new ImageAdapter(imgs , getContext()) ;
        pager_RecyclerView.setAdapter(imageAdapter);
        pager_RecyclerView.addOnItemTouchListener(new RecyclerViewTouchHelper(getContext(), pager_RecyclerView, new RecyclerViewTouchHelper.recyclerViewTouchListner() {
            @Override
            public void onclick(View child, int postion) {
                viewPager.setCurrentItem(postion,true);
            }

            @Override
            public void onLongClick(View child, int postion) {
                delete_ad(postion);
            }
        }));

    }

    void  delete_ad(final int postion){
        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(getActivity());
        builder
                .setMessage(getString(R.string.delete_img))
                .setPositiveButton("نعم", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        imgs.remove(postion);
                        imageAdapter.notifyDataSetChanged();
                        productDescriptionAdDetailsPagerAdapter.notifyDataSetChanged();

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
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        /// select from gallery section
        if (requestCode == RESULT_LOAD_IMAGE && resultCode == getActivity().RESULT_OK && data!=null){

            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};
            Log.e("Uri",selectedImage + "");
            Log.e("filePathColumn",MediaStore.Images.Media.DATA + "");

            Cursor cursor = getActivity().getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);

            cursor.close();

            Log.e("picturePath",picturePath + "");
            new ResizeImage().execute(picturePath);

        }
    }

    public class ResizeImage extends AsyncTask<String, Integer, String> {
        protected String  filePath,fileName ;
        long totalSize = 0;
        Boolean ok = false ;
        Bitmap resized_bitmap ;


        // Decode image in background.
        @Override
        protected String doInBackground(String... params) {
            filePath = params[0];
            File uploadFile = null ;
            String response = null;
            try {
                    resized_bitmap =  decodeSampledBitmapFromPath(filePath, (int) (1080), (int) (720)) ;

                uploadFile = bitmaptofile(resized_bitmap);

            } catch (IOException e) {
                e.printStackTrace();
            }

            if (uploadFile == null){
                Log.e("category","file"+"null");
                uploadFile = new File(filePath);
            }
            fileName = uploadFile.getName();
            response = uploadFile(uploadFile);



            return    response ;
        }

        @Override
        protected void onPreExecute() {
            // setting progress bar to zero
            progressBar.setProgress(0);
            super.onPreExecute();
        }

        @Override
        protected void onProgressUpdate(Integer... progress) {
            // Making progress bar visible
            progressBar.setVisibility(View.VISIBLE);
            progressBarView.setVisibility(View.VISIBLE);
            // updating progress bar value
            progressBar.setProgress(progress[0]);

            // updating percentage value
            txtPercentage.setText(String.valueOf(progress[0]) + "%");
        }


        // Once complete, see if ImageView is still around and set bitmap.
        @Override
        protected void onPostExecute(String message) {
            progressBarView.setVisibility(View.GONE);

            if (ok){
                String filePathName = new File(filePath).getName();
                Log.e("category","filename  "+fileName +"file path   " + filePathName);
                 imgs.add(fileName);
                upload_imgs.add(resized_bitmap);
                productDescriptionAdDetailsPagerAdapter.notifyDataSetChanged();
                imageAdapter.notifyDataSetChanged();
                validateImg();
                message = "تم اضافة الصورة";
            }

            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setMessage(message).setTitle("Response from Servers")
                    .setCancelable(false)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // do nothing
                        }
                    });
            AlertDialog alert = builder.create();
            alert.show();
            alert.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(ContextCompat.getColor(getContext(),R.color.colorPrimary));


        }


        protected File  bitmaptofile(Bitmap bitmap) throws IOException {
            File outputDir = getActivity().getCacheDir(); // context being the Activity pointer
            File outputFile = File.createTempFile(currentDateFormat(),".jpg" , outputDir);
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

        protected String uploadFile(File sourceFile) {
            String responseString = null;

            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(Config.BASE_URL0+"upload_img.php");
            Log.e("category","url"+httppost.getURI());
            try {
                AndroidMultiPartEntity entity = new AndroidMultiPartEntity(
                        new AndroidMultiPartEntity.ProgressListener() {

                            @Override
                            public void transferred(long num) {
                                Log.e("transferd" , num +"");
                                int precentage = (int) ((num / (float) totalSize) * 100);
                                if (precentage <=99){
                                    publishProgress(precentage);
                                }else publishProgress(99);
                            }
                        });


                // Adding file data to http body
                entity.addPart("image", new FileBody(sourceFile));
                entity.addPart("request", new StringBody(Config.BASE_URL0+"upload_img.php"));


                totalSize = entity.getContentLength();
                httppost.setEntity(entity);

                // Making server call
                HttpResponse response = httpclient.execute(httppost);
                HttpEntity r_entity = response.getEntity();
                Log.e("response",response.toString());
                int statusCode = response.getStatusLine().getStatusCode();
                if (statusCode == 200) {
                    // Server response
                    responseString = EntityUtils.toString(r_entity);
                    Log.e("response",responseString.toString());
                    try {
                        JSONObject res_json = new JSONObject(responseString);
                        if (res_json.getString("response").equals("success")){
                            ok=true ;

                        }else {
                            ok = false ;
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    responseString = "Error occurred! Http Status Code: "
                            + statusCode;
                }

            } catch (ClientProtocolException e) {
                responseString = e.toString();
            } catch (IOException e) {
                responseString = e.toString();
            }

            return responseString;

        }

    }

    public static String currentDateFormat(){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss", Locale.ENGLISH);
        String  currentTimeStamp = dateFormat.format(new Date());
        return currentTimeStamp;
    }

    public static Bitmap decodeSampledBitmapFromPath(String FilePath,
                                                     int reqWidth, int reqHeight) {


        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile( FilePath, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile( FilePath, options);
    }
    public static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) >= reqHeight
                    && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2;
            }
        }
        Log.e("sapleSize" , inSampleSize + "");
        return inSampleSize;
    }
    protected boolean validateImg() {
        if (  imgs.size()<1 ){
            view.findViewById(R.id.img_err).setVisibility(View.VISIBLE);
            return false ;
        }else {
            view.findViewById(R.id.img_err).setVisibility(View.GONE);
        }
        return true ;
    }


}
