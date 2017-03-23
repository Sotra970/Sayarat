package com.sayarat.Activity;

import android.os.Build;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.sayarat.Models.UserModel;
import com.sayarat.R;
import com.sayarat.app.AppController;
import com.sayarat.app.Config;
import com.sayarat.transition.FabTransform;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class SendMessageActivity extends AppCompatActivity {
    TextInputLayout desc_layout ;
    EditText description ;
    View send , cancel ;
    String extra_id ;
    private String extra_ads_id="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_message);
        extra_id =  getIntent().getExtras().getString("extra_model");
        extra_ads_id =  getIntent().getExtras().getString("ads_id");
        View main_container = findViewById(R.id.search_main_container);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            FabTransform.setup(this, main_container);}
        
        desc_layout = (TextInputLayout) findViewById(R.id.desc_layout);
        description = (EditText) findViewById(R.id.desc_input);

        send =  findViewById(R.id.send_btn);
        cancel = findViewById(R.id.cancel_btn);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                send_mess_req() ;
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                supportFinishAfterTransition();
            }
        });

    }

    private void send_mess_req() {
        if (!validate_description())
            return;
        findViewById(R.id.loadingSpinner).setVisibility(View.VISIBLE);
        StringRequest user_req = new StringRequest(Request.Method.POST, Config.BASE_URL+"send_message.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("login response" , response) ;
                try {

                    JSONObject obj = new JSONObject(response);
                    String res =   obj.getString("response");
                    if (res.equals("success") ) {
                        findViewById(R.id.loadingSpinner).setVisibility(View.GONE);
                        supportFinishAfterTransition();
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
                parmas.put("sender_id" , AppController.getInstance().getPrefManager().getUser().getUser_id()) ;
                parmas.put("receiver_id" ,extra_id) ;
                parmas.put("message" ,description.getText().toString().trim() ) ;
                parmas.put("mob" ,"android" ) ;
                parmas.put("ads_id" ,extra_ads_id) ;
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

    boolean validate_description (){
        return !(description.getText().toString().trim().equals(null) || description.getText().toString().trim().isEmpty());
    }

}
