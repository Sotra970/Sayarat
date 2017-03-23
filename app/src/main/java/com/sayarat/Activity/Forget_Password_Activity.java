package com.sayarat.Activity;

import android.content.Intent;
import android.os.Handler;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.sayarat.Models.UserModel;
import com.sayarat.R;
import com.sayarat.app.AppController;
import com.sayarat.app.Config;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Forget_Password_Activity extends AppCompatActivity {
    private static final String TAG = "forget pass";
    View pahse_1 ,pahse_2 ,pahse_3;
    View pahse_1_layout ,pahse_2_layout ,pahse_3_layout , scess_layout ;
    View scess_txt ;
    View phase_1_continue,phase_2_continue,phase_3_continue , resend_vevication;
    EditText pass_input , con_pass_input ,user_name_input , vervication_code_input ;
    TextInputLayout pass_input_layout , con_pass_input_layout ,user_name_input_layout , vervication_code_input_layout;
     String user_name ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);

        pahse_1 = findViewById(R.id.phase_1);
        pahse_2 = findViewById(R.id.phase_2);
        pahse_3 = findViewById(R.id.phase_3);

        phase_1_continue = findViewById(R.id.phase_1_continue);
        phase_2_continue = findViewById(R.id.phase_2_continue);
        phase_3_continue = findViewById(R.id.phase_2_continue);
        resend_vevication = findViewById(R.id.resend_vervication_code);

        pahse_1_layout = findViewById(R.id.phase_1_linear_layout);
        pahse_2_layout = findViewById(R.id.phase_2_linear_layout);
        pahse_3_layout = findViewById(R.id.phase_3_linear_layout);


        scess_layout = findViewById(R.id.sucess_con);
        scess_txt = findViewById(R.id.sucess_text);

        user_name_input =(EditText) findViewById(R.id.input_name);
        vervication_code_input =(EditText) findViewById(R.id.input_code);
        pass_input =(EditText) findViewById(R.id.input_pass);
        con_pass_input =(EditText) findViewById(R.id.input_pass_confirm);


        user_name_input_layout  =(TextInputLayout) findViewById(R.id.input_layout_name);
        vervication_code_input_layout  =(TextInputLayout) findViewById(R.id.input_layout_code);
        pass_input_layout  =(TextInputLayout) findViewById(R.id.input_layout_pass);
        con_pass_input_layout  =(TextInputLayout) findViewById(R.id.input_layout_pass_confirm);




        phase_1_continue = findViewById(R.id.phase_1_continue);
        phase_2_continue = findViewById(R.id.phase_2_continue);
        phase_3_continue = findViewById(R.id.phase_3_continue);
        resend_vevication = findViewById(R.id.resend_vervication_code);



        resend_vevication.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                send_ver_code(2);
            }
        });

        phase_1_continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                send_ver_code(1);
            }
        });
        phase_2_continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                check_ver_code();
            }
        });
        phase_3_continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                udapte_password();
            }
        });

    }

    void send_ver_code(final int phase){
            if (!validate(user_name_input,user_name_input_layout)) {
                return;
            }

             user_name = user_name_input.getText().toString();

            findViewById(R.id.loadingSpinner).setVisibility(View.VISIBLE);

            StringRequest strReq = new StringRequest(Request.Method.POST,
                    Config.BASE_URL+"forget_pass_send_code.php", new Response.Listener<String>() {

                @Override
                public void onResponse(String response) {
                    Log.e(TAG, "Login response: " + response);
                    try {
                        JSONObject obj = new JSONObject(response);
                        String res =   obj.getString("response");
                        if (res.equals("success") ) {
                            findViewById(R.id.loadingSpinner).setVisibility(View.GONE);
                            if (phase ==  1)
                            collabse(pahse_1_layout,pahse_1);
                            expand(pahse_2_layout,pahse_2);

                        }else {
                            findViewById(R.id.loadingSpinner).setVisibility(View.GONE);
                            Toast.makeText(getApplicationContext(),getString(R.string.user_name_404),Toast.LENGTH_LONG).show();
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
                        findViewById(R.id.loadingSpinner).setVisibility(View.GONE);
                        String message =   "يرجى التاكد من الانترنت و اعادة المحاوله";
                        Toast.makeText(getApplicationContext(), "" + message, Toast.LENGTH_LONG).show();
                    }
                }
            }) {
                //sending your email and pass
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<>();
                    params.put("username", user_name);

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


    void check_ver_code(){
        if (!validate(vervication_code_input,vervication_code_input_layout)) {
            Log.e("verfiy ","faild");
            return;
        }


        final String code = vervication_code_input.getText().toString();

        findViewById(R.id.loadingSpinner).setVisibility(View.VISIBLE);

        StringRequest strReq = new StringRequest(Request.Method.POST,
                Config.BASE_URL+"verification.php", new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.e(TAG, "Login response: " + response);
                try {
                    JSONObject obj = new JSONObject(response);
                    String res =   obj.getString("response");
                    if (res.equals("success") ) {
                        findViewById(R.id.loadingSpinner).setVisibility(View.GONE);
                            collabse(pahse_2_layout,pahse_2);
                        expand(pahse_3_layout,pahse_3);

                    }else {
                        findViewById(R.id.loadingSpinner).setVisibility(View.GONE);
                        Toast.makeText(getApplicationContext(),getString(R.string.code_not_match),Toast.LENGTH_LONG).show();
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
                    findViewById(R.id.loadingSpinner).setVisibility(View.GONE);
                    String message =   "يرجى التاكد من الانترنت و اعادة المحاوله";
                    Toast.makeText(getApplicationContext(), "" + message, Toast.LENGTH_LONG).show();
                }
            }
        }) {
            //sending your email and pass
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("code", code);
                params.put("username", user_name);

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
    void udapte_password(){
        if (!validate(pass_input,pass_input_layout)) {
            return;
        }
        if (!validate_con_pass()) {
            return;
        }


        final String txt = pass_input.getText().toString().trim();

        findViewById(R.id.loadingSpinner).setVisibility(View.VISIBLE);

        StringRequest strReq = new StringRequest(Request.Method.POST,
                Config.BASE_URL+"update_password.php", new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.e(TAG, "Login response: " + response);
                try {
                    JSONObject obj = new JSONObject(response);
                    String res =   obj.getString("response");
                    if (res.equals("success") ) {
                        findViewById(R.id.loadingSpinner).setVisibility(View.GONE);
                            collabse(pahse_3_layout,pahse_3);
                        expand(scess_txt,scess_layout);
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                finish();
                            }
                        },700);

                    }else {
                        findViewById(R.id.loadingSpinner).setVisibility(View.GONE);
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
                    findViewById(R.id.loadingSpinner).setVisibility(View.GONE);
                    String message =   "يرجى التاكد من الانترنت و اعادة المحاوله";
                    Toast.makeText(getApplicationContext(), "" + message, Toast.LENGTH_LONG).show();
                }
            }
        }) {
            //sending your email and pass
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("pass", txt);
                params.put("username", user_name);

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


    private boolean validate(EditText ed , TextInputLayout layout) {
            String txt = ed.getText().toString().trim();

            if (txt.isEmpty()) {
                layout.setError(getString(R.string.no_data_err));
                ed.requestFocus();
                return false;
            } else {
                layout.setError(null);
            }

            return true;
    }
    private boolean validate_con_pass() {
            String txt = pass_input.getText().toString().trim();
            String txt2 = con_pass_input.getText().toString().trim();

            if (txt.isEmpty() || !txt.equals(txt2)) {
                con_pass_input_layout.setError(getString(R.string.no_data_err));
                con_pass_input.requestFocus();
                return false;
            } else {
                con_pass_input_layout.setError(null);
            }

            return true;
    }


    // fade , scale
    void collabse(View layout , final View container ){
        layout.animate().alpha(0).setDuration(100).setInterpolator(new AccelerateInterpolator());
        container.animate().scaleY(0).setDuration(150).setStartDelay(50).setInterpolator(new AccelerateInterpolator());
        container.postDelayed(new Runnable() {
            @Override
            public void run() {
                container.setVisibility(View.GONE);
            }
        },200);
    }


    // fade , scale
    void expand(View layout , View container ){
        container.setVisibility(View.VISIBLE);
        layout.animate().alpha(1).setDuration(150).setStartDelay(240).setInterpolator(new AccelerateInterpolator());
        container.animate().scaleY(1).setDuration(200).setStartDelay(180).setInterpolator(new AccelerateInterpolator());
    }



}
