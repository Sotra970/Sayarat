package com.sayarat.Activity;

import android.app.ActivityOptions;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

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

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class CommActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    EditText comm_amount ,comm_calc , user_name , transfer_amount,transfer_name ,  add_num , notes;
    String bank , time ;
    TextView transfer_time ;
    Calendar myCalendar;
    Spinner bank_spinner ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comm);

        comm_amount = (EditText) findViewById(R.id.comm_amount);
        comm_calc = (EditText) findViewById(R.id.comm_calc);
        comm_calc.addTextChangedListener(new MyTextWatcher(comm_calc));



        user_name = (EditText) findViewById(R.id.user_name);
        transfer_amount = (EditText) findViewById(R.id.transfer_amount);
        transfer_name = (EditText) findViewById(R.id.transfer_name);
        add_num = (EditText) findViewById(R.id.add_num);
        notes = (EditText) findViewById(R.id.notes);


        transfer_time = (TextView)findViewById(R.id.transfer_time);


         myCalendar = Calendar.getInstance();



        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }

        };

        findViewById(R.id.transfer_time_v).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(CommActivity.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        findViewById(R.id.send_comm_btt).setOnClickListener(new View.OnClickListener() {
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

        bank_spinner = (Spinner) findViewById(R.id.transfer_bank_spinner);

        bank_spinner.setOnItemSelectedListener(this);

        toolbar_action_setup();

        Resources res = getResources();
        String[] banks = res.getStringArray(R.array.bank_names);
        ArrayAdapter adapter9 = new ArrayAdapter(this.getBaseContext(), R.layout.simple_spinner_item, banks);
        adapter9.setDropDownViewResource(R.layout.simple_spinner_item);
        bank_spinner.setAdapter(adapter9);
        bank_spinner.setOnItemSelectedListener(this);

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

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    private void updateLabel() {

        String myFormat = "MM/dd/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat);
        time = sdf.format(myCalendar.getTime()) ;
        transfer_time.setText(time);
    }


    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {


                bank = adapterView.getSelectedItem().toString();

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }


    private void send_mess_req() {
        if (!validate(user_name))
            return;
        if (!validate(transfer_amount))
            return;
        if (!validate(transfer_name))
            return;
        if (!validate_sp(bank_spinner, bank)) return;

        if (!validate_tv(time))
            return;

        if (!validate(add_num))
            return;
        if (!validate(notes))
            return;
        findViewById(R.id.loadingSpinner).setVisibility(View.VISIBLE);
        StringRequest user_req = new StringRequest(Request.Method.POST, Config.BASE_URL+"commotion.php", new Response.Listener<String>() {
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
                parmas.put("notes" ,notes.getText().toString().trim()) ;
                parmas.put("ad_number" ,add_num.getText().toString().trim() ) ;
                parmas.put("commission" ,transfer_amount.getText().toString().trim() ) ;
                parmas.put("transfer_name" ,transfer_name.getText().toString().trim() ) ;
                parmas.put("user_name" ,user_name.getText().toString().trim() ) ;
                parmas.put("message" ,user_name.getText().toString().trim() ) ;
                parmas.put("bank" ,bank) ;
                parmas.put("time" ,time) ;
                parmas.put("email" ,AppController.getInstance().getPrefManager().getUser().getEmail()) ;
                parmas.put("number" ,AppController.getInstance().getPrefManager().getUser().getUser_phone()) ;
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
   private boolean validate_tv(String txt ) {


        if (txt.isEmpty()) {
            transfer_time.setError(getString(R.string.no_data_err));
            transfer_time.requestFocus();
            return false;
        } else {
            transfer_time.setError(null);
        }

        return true;
    }



    private void requestFocus(View view) {
        Log.d("requestFocus", view.requestFocus() + "");
        //foucus on view
        if (view.requestFocus()) {

        }
    }

    boolean validate_sp(Spinner spinner, String txt ) {
        TextView textView = ((TextView) spinner.getChildAt(0));
        Log.e("txtview" , textView.getText().toString());
        Log.e("txt" , txt+"");
        String default_txt=getString(R.string.bank_name);
        Log.e("default_txt" , default_txt);
        if ( txt.trim().isEmpty() || txt.trim().equals(default_txt.trim()) ){
            textView.setError(getString(R.string.no_ch_err));
            requestFocus(textView);
            Log.e("err" , "true");
            return false;
        } else {
            textView.setError(null);
        }

        return true;
    }


    private class MyTextWatcher implements TextWatcher {

        private View view;
        private MyTextWatcher(View view) {
            this.view = view;
        }

        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void afterTextChanged(Editable editable) {
            switch (view.getId()) {
                case R.id.comm_calc:
                    if (!comm_calc.getText().toString().isEmpty()) {
                        Double amount = Double.valueOf(comm_calc.getText().toString()) * (0.5 / 100);
                        comm_amount.setText(String.valueOf(amount));
                    }
                    break;

            }
        }


    }
}
