package com.sayarat.Activity;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.design.widget.TextInputLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.sayarat.R;
import com.sayarat.app.AppController;
import com.sayarat.app.Config;
import com.sayarat.transition.FabTransform;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class Contact_usActivity extends AppCompatActivity {
        View contact_us_btt ;
    EditText name, email , mass ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_us);


        name =(EditText) findViewById(R.id.input_name);
        email =(EditText) findViewById(R.id.input_email);
        mass =(EditText) findViewById(R.id.input_mess);

toolbar_action_setup();
        contact_us_btt = findViewById(R.id.contact_us_btt);
        contact_us_btt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                send_mess_req();
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

    private void send_mess_req() {
        if (!validate(name))
            return;
        if (!validate(email))
            return;
        if (!validate(mass))
            return;
        findViewById(R.id.loadingSpinner).setVisibility(View.VISIBLE);
        StringRequest user_req = new StringRequest(Request.Method.POST, Config.BASE_URL+"contact_us.php", new Response.Listener<String>() {
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
                parmas.put("user_id" , AppController.getInstance().getPrefManager().getUser().getUser_id()) ;
                parmas.put("email" ,email.getText().toString().trim()) ;
                parmas.put("name" ,name.getText().toString().trim()) ;
                parmas.put("message" ,mass.getText().toString().trim() ) ;
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


    private boolean validate(EditText ed ) {
        String txt = ed.getText().toString().trim();

        if (txt.isEmpty()) {
            ed.setError(getString(R.string.no_data_err));
            ed.requestFocus();
            return false;
        } else {
            ed.setError(null);
        }

        return true;
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

}
