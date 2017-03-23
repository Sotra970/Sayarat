package com.sayarat.Activity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.GlideBitmapDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.sayarat.Models.UserModel;
import com.sayarat.R;
import com.sayarat.app.AndroidMultiPartEntity;
import com.sayarat.app.AppController;
import com.sayarat.app.Config;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;


public class SettingsActivity extends AppCompatActivity {
    UserModel userModel ;
    String uploaded_img_name;
    private EditText inputName, inputPass,inputEmail,inputPhone , check_pass_ed;
    private TextInputLayout inputLayoutName,inputLayoutPhone, inputLayoutPass,inputLayoutEmail , check_pass_ed_layout;
    View pahse_1 ,pahse_2 ,pahse_3;
    View pahse_1_layout ,pahse_2_layout ,pahse_3_layout , scess_layout ;
    View scess_txt ;
    View phase_1_continue,phase_2_continue,phase_3_continue , resend_vevication;
    EditText pass_input , con_pass_input ,user_name_input , vervication_code_input ;
    TextInputLayout pass_input_layout , con_pass_input_layout ,user_name_input_layout , vervication_code_input_layout;
    String user_name ;
    ImageView profile_img ;
    private String TAG= "settings activity";
    TextView txtPercentage;
    ProgressBar progressBar ;
    View progressBarView ;
    ImageButton  upload_img;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null)no_storage_permission();
        setContentView(R.layout.activity_settings);
        userModel = AppController.getInstance().getPrefManager().getUser() ;
        uploaded_img_name = userModel.getImg();
        inputLayoutName = (TextInputLayout) findViewById(R.id.input_layout_name);
        inputLayoutPhone = (TextInputLayout) findViewById(R.id.input_layout_phone);
        inputLayoutPass = (TextInputLayout) findViewById(R.id.input_layout_pass);
        inputLayoutEmail = (TextInputLayout) findViewById(R.id.input_layout_email);
        check_pass_ed_layout = (TextInputLayout) findViewById(R.id.check_pass_ed_layout);

        inputName = (EditText) findViewById(R.id.input_name);
        inputName.setText(userModel.getUser_name());

        inputPass = (EditText) findViewById(R.id.input_pass);
        inputPass.setText(userModel.getPass());

        inputPhone = (EditText) findViewById(R.id.input_phone);
        inputPhone.setText(userModel.getUser_phone());

        inputEmail = (EditText) findViewById(R.id.input_email);
        inputEmail.setText(userModel.getEmail());



        txtPercentage = (TextView) findViewById(R.id.txtPercentage);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBarView = findViewById(R.id.progressBarView);


        check_pass_ed = (EditText) findViewById(R.id.check_pass_ed);
        profile_img = (ImageView) findViewById(R.id.profile_img);
        SimpleTarget target = new SimpleTarget<GlideBitmapDrawable>() {
            @Override
            public void onResourceReady(GlideBitmapDrawable bitmap, GlideAnimation glideAnimation) {
                // do something with the bitmap
                // for demonstration purposes, let's just set it to an ImageView
                Log.e("slider","endLoading");
                RoundedBitmapDrawable circularBitmapDrawable =
                        RoundedBitmapDrawableFactory.create(getResources(), bitmap.getBitmap());
                circularBitmapDrawable.setCircular(true);
                profile_img.setImageDrawable( circularBitmapDrawable );
            }
        };
        Glide.with(getApplicationContext())
                .load( userModel.getImg())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .centerCrop()
                .into(target);

        View btn_register = findViewById(R.id.normal_signup);
        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                register();
            }
        });
        final View check_pass =  findViewById(R.id.check_pass);

        check_pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (check_pass_ed.getText().toString().equals(userModel.getPass())){
                    check_pass_ed_layout.setErrorEnabled(false);
                    collabse(findViewById(R.id.check_pass_layout),findViewById(R.id.check_pass_container));
                    expand(findViewById(R.id.sign_up_layout),findViewById(R.id.sign_up_container));
                }else {
                    check_pass_ed_layout.setError(getString(R.string.Enter_valid_PASS));
                }
            }
        });

        upload_img = (ImageButton) findViewById(R.id.upload_img);
        upload_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                get_img();
            }
        });


    }
    void get_img(){
        Intent i = new Intent(
                Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        startActivityForResult(i, Config.RESULT_LOAD_IMAGE);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        /// select from gallery section
        if (requestCode == Config.RESULT_LOAD_IMAGE && resultCode == RESULT_OK && data!=null){

            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};
            Log.e("Uri",selectedImage + "");
            Log.e("filePathColumn",MediaStore.Images.Media.DATA + "");

            Cursor cursor = getContentResolver().query(selectedImage,
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

        // Decode image in background.
        @Override
        protected String doInBackground(String... params) {
            filePath = params[0];
            File uploadFile = null ;
            String response = null;
            try {
                resized_bitmap =  decodeSampledBitmapFromPath(filePath, 1080*2, 1920*2) ;

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

        // Once complete, see if ImageView is still around and set bitmap.
        @Override
        protected void onPostExecute(String result) {
            showAlert(result);
            progressBarView.setVisibility(View.GONE);
        }

        protected File  bitmaptofile(Bitmap bitmap) throws IOException {
            File outputDir = getApplicationContext().getCacheDir(); // context being the Activity pointer
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
        /**
         * Method to show alert dialog
         * */
        protected void showAlert(String message) {
            if (ok){
                String filePathName = new File(filePath).getName();
                Log.e("category","filename  "+fileName +"file path   " + filePathName);
                uploaded_img_name = fileName;
                validateImg();
            }
            AlertDialog.Builder builder = new AlertDialog.Builder(SettingsActivity.this);
            builder.setMessage("تم رفق الصورة بنجاح").setTitle("نتيجو العمليه")
                    .setCancelable(false)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // do nothing
                        }
                    });
            AlertDialog alert = builder.create();
            alert.show();
          //  alert.getWindow().getDecorView().setBackgroundColor(ContextCompat.getColor(getApplicationContext(),R.color.colorPrimary));




        }

        @SuppressWarnings("deprecation")
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


                totalSize = entity.getContentLength();
                httppost.setEntity(entity);

                // Making server call
                HttpResponse response = httpclient.execute(httppost);
                HttpEntity r_entity = response.getEntity();

                int statusCode = response.getStatusLine().getStatusCode();
                if (statusCode == 200) {
                    // Server response
                    responseString = EntityUtils.toString(r_entity);
                    ok=true ;
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
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss" , Locale.ENGLISH);
        String  currentTimeStamp = dateFormat.format(new Date());
        return currentTimeStamp;
    }
    public static Bitmap decodeSampledBitmapFromPath( String FilePath,
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
        if ( uploaded_img_name.equals("null") || uploaded_img_name.isEmpty() ){
//            findViewById(R.id.img_err).setVisibility(View.VISIBLE);
            return false ;
        }else {
//            findViewById(R.id.img_err).setVisibility(View.GONE);
        }
        return true ;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case Config.MY_PERMISSIONS_REQUEST_STORAGE: {

                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                } else {
                    //  no_gps_permition();
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(SettingsActivity.this, "Permission needed to run your app", Toast.LENGTH_SHORT).show();
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

            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, Config.MY_PERMISSIONS_REQUEST_STORAGE);


        }
    }

    // fade , scale
    void collabse(View layout , final View container ){
        layout.animate().alpha(0).setDuration(100).setInterpolator(new AccelerateInterpolator());
        container.animate().scaleY(0).setDuration(150).setStartDelay(50).setInterpolator(new AccelerateInterpolator());
        container.postDelayed(new Runnable() {
            @Override
            public void run() {
                container.setVisibility(View.GONE);
                findViewById(R.id.check_pass_scrim).setVisibility(View.GONE);
            }
        },200);

    }


    // fade , scale
    void expand(View layout , View container ){
        findViewById(R.id.mainContainer).setVisibility(View.VISIBLE);
        container.setVisibility(View.VISIBLE);
        layout.animate().alpha(1).setDuration(250).setStartDelay(200).setInterpolator(new AccelerateInterpolator());
        container.animate().scaleY(1).setDuration(300).setStartDelay(180).setInterpolator(new AccelerateInterpolator());
        profile_img.animate().scaleY(1).scaleX(1).setDuration(300).setStartDelay(400).setInterpolator(new FastOutSlowInInterpolator());
        upload_img.animate().scaleY(1).scaleX(1).setDuration(150).setStartDelay(700).setInterpolator(new FastOutSlowInInterpolator());

    }

    private void register() {
        if (!validateName()) {
            return;
        }
        if (!validatePhone()) {
            return;
        }
        if (!validateEmail()) {
            return;
        }

        if (!validatePass()) {
            return;
        }

        findViewById(R.id.loadingSpinner).setVisibility(View.VISIBLE);
        final String name = inputName.getText().toString();
        final String pass = inputPass.getText().toString();
        final String phone = inputPhone.getText().toString();

        user_name= phone ;

        final String email = inputEmail.getText().toString();
        StringRequest strReq = new StringRequest(Request.Method.POST,
                Config.BASE_URL+"update_profile.php", new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                findViewById(R.id.loadingSpinner).setVisibility(View.GONE);
                Log.e(TAG, "REGISTER response: " + response);

                try {
                    JSONObject obj = new JSONObject(response);
                    String res =   obj.getString("response");
                    if (res.equals("success") ){
                        //GO TO LOGIN
//                        findViewById(R.id.code_confirm_container).setVisibility(View.VISIBLE);

                        userModel.setUser_name(name);
                        userModel.setPass(pass);
                        userModel.setUser_phone(phone);
                        userModel.setEmail(email);
                        userModel.setImg(Config.img_url + uploaded_img_name);
                        AppController.getInstance().getPrefManager().storeUser(userModel);
                        finish();
                        Toast.makeText(getApplicationContext(), "" + "تم التسجيل بنجاح", Toast.LENGTH_LONG).show();
                    }else if(res.equals("error")) {
                        String message="";
                        if ( obj.getString("error").contains("Email"))
                            message = getString(R.string.dublicate_email);
                        if ( obj.getString("error").contains("Phone"))
                            message = getString(R.string.dublicate_phone);
                        else message="oop failed";
                        Toast.makeText(getApplicationContext(), "" + message, Toast.LENGTH_LONG).show();
                    }

                } catch (JSONException e) {
                    Log.e(TAG, "json parsing error: " + e.getMessage());
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                if (error instanceof TimeoutError){
                }
                if (error instanceof NoConnectionError) {
                    findViewById(R.id.loadingSpinner).setVisibility(View.GONE);
                    String message =   "يرجى التاكد من الانترنت و اعادة المحاوله";
                    Toast.makeText(getApplicationContext(), "" + message, Toast.LENGTH_LONG).show();
                }
                NetworkResponse networkResponse = error.networkResponse;
                Log.e(TAG, "Volley error: " + error.getMessage() + ", code: " + networkResponse);
            }
        }) {
            //sending your email and pass
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("name", name);
                params.put("pass", pass);
                params.put("phone", phone);
                params.put("email", email);
                params.put("image", uploaded_img_name);
                params.put("user_id", userModel.getUser_id());

                return params;
            }
        };

        //Adding request to request queue
        int socketTimeout = 5000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout,
                12,
                1);

        strReq.setRetryPolicy(policy);
        AppController.getInstance().addToRequestQueue(strReq);

    }

    // Validating name
    private boolean validateName() {
        if (inputName.getText().toString().trim().isEmpty()) {
            incorrectUserName();
            return false;
        } else {
            inputLayoutName.setErrorEnabled(false);
        }

        return true;
    }

    private void incorrectUserName() {
        inputLayoutName.setError(getString(R.string.Enter_valid_Username));
        requestFocus(inputName);
    }

    // Validating phone
    private boolean validatePhone() {
        if (inputPhone.getText().toString().trim().isEmpty()) {
            incorrectPhone();
            return false;
        } else {
            inputLayoutPhone.setErrorEnabled(false);
        }

        return true;
    }

    private void incorrectPhone() {
        inputLayoutPhone.setError(getString(R.string.Enter_valid_PHONE));
        requestFocus(inputPhone);
    }

    // Validating Pass
    private boolean validatePass() {
        String Pass = inputPass.getText().toString().trim();

        if (Pass.isEmpty()) {
            incorrectPass();
            return false;
        } else {
            inputLayoutPass.setErrorEnabled(false);
        }

        return true;
    }
    // Validating Pass
    private boolean validateEmail() {
        String Email = inputEmail.getText().toString().trim();

        if (Email.isEmpty()) {
            incorrectEmail();
            return false;
        } else {
            inputLayoutEmail.setErrorEnabled(false);
        }

        return true;
    }

    private void incorrectPass() {
        inputLayoutPass.setError(getString(R.string.Enter_valid_PASS));
        requestFocus(inputPass);
    }

    private void incorrectEmail() {
        inputLayoutEmail.setError(getString(R.string.Enter_valid_Email));
        requestFocus(inputEmail);
    }



    private void requestFocus(View view) {
        Log.d("requestFocus",view.requestFocus()+"");
        //foucus on view
        if (view.requestFocus()) {
            /*m7taga sho3'l
          im = (InputMethodManager)getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            Log.d("inputmetod", im.isAcceptingText() + "");
            im.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
*/
        }
    }


}
