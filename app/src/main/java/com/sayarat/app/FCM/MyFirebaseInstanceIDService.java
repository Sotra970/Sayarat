package com.sayarat.app.FCM;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.sayarat.Activity.HomeActivity;
import com.sayarat.Models.UserModel;
import com.sayarat.R;
import com.sayarat.app.AppController;
import com.sayarat.app.Config;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Sotraa on 5/27/2016.
 */
public class MyFirebaseInstanceIDService  extends FirebaseInstanceIdService {
    private static final String TAG = FirebaseInstanceIdService.class.getSimpleName();

    @Override
    public void onTokenRefresh() {
        // Get updated InstanceID token.
        String token = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "Refreshed token: " + token);
        try{
            update_token();
        }catch (Exception e){}
        update_token();
        // // TODO: 9/25/2016 subscripe  
    }

    private void update_token() {


        StringRequest strReq = new StringRequest(Request.Method.POST,
                Config.BASE_URL+"update_token.php", new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.e(TAG, "Login response: " + response);
                try {
                    JSONObject obj = new JSONObject(response);
                    String res =   obj.getString("response");
                    if (res.equals("success") ) {
                        UserModel userModel = AppController.getInstance().getPrefManager().getUser();
                        userModel.setToken(FirebaseInstanceId.getInstance().getToken());
                        AppController.getInstance().getPrefManager().storeUser(userModel);
                    }else {

                    }

                } catch (JSONException e) {
                    Log.e(TAG, "json parsing error: " + e.getMessage());
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                if (error instanceof TimeoutError) {
                }
                if (error instanceof NoConnectionError) {

                }
            }
        }) {
            //sending your email and pass
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("use_id", AppController.getInstance().getPrefManager().getUser().getUser_id());
                params.put("token", FirebaseInstanceId.getInstance().getToken());

                Log.e(TAG, "params: " + params.toString());
                return params;
            }
        };
        int socketTimeout = 10000; // 10 seconds. You can change it
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout,
                10,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);

        strReq.setRetryPolicy(policy);
        //Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq);
    }


}
