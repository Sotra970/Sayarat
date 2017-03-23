package com.sayarat.Activity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v4.content.IntentCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.widget.Button;
import android.widget.EditText;
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
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.sayarat.R;
import com.sayarat.app.AppController;
import com.sayarat.app.Config;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterAuthClient;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;
import com.twitter.sdk.android.core.models.User;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.fabric.sdk.android.Fabric;
import retrofit2.Call;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class SignUPActivity extends AppCompatActivity implements  GoogleApiClient.OnConnectionFailedListener{
    private String TAG = SignUPActivity.class.getSimpleName();

    private EditText inputName, inputPass,inputEmail,inputPhone;
    private TextInputLayout inputLayoutName,inputLayoutPhone, inputLayoutPass,inputLayoutEmail;
    private View btn_register;
    View pahse_1 ,pahse_2 ,pahse_3;
    View pahse_1_layout ,pahse_2_layout ,pahse_3_layout , scess_layout ;
    View scess_txt ;
    View phase_1_continue,phase_2_continue,phase_3_continue , resend_vevication;
    EditText pass_input , con_pass_input ,user_name_input , vervication_code_input ;
    TextInputLayout pass_input_layout , con_pass_input_layout ,user_name_input_layout , vervication_code_input_layout;
    String user_name ;
    HashMap<String,String> social_prams = new HashMap<>();

    CallbackManager mFacebookCallbackManager;
    AccessToken accessToken;
    private static final int RC_SIGN_IN = 007;

    private static final String TWITTER_KEY = "uEIRsrgpo51W9qr4BAD5Xqe26";
    private static final String TWITTER_SECRET = "DEulTPlpLiHQIPHWPv9AkyCni8leqegvJnJqQnJwOB7kCd0K0J";

    TwitterAuthClient mTwitterAuthClient ;
    private LoginButton loginButton;
    private String id;
    private TwitterLoginButton TwitterloginButton;
    private GoogleApiClient mGoogleApiClient;
    private SignInButton google_sn;
    String img ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
        Fabric.with(this, new Twitter(authConfig));

        FacebookSdk.sdkInitialize(getApplicationContext());
        mFacebookCallbackManager = CallbackManager.Factory.create();



        setContentView(R.layout.activity_sign_up);

        TwitterloginButton = (TwitterLoginButton) findViewById(R.id.twitter_login_button);
        loginButton = (LoginButton) findViewById(R.id.login_button);
        google_sn = (SignInButton) findViewById(R.id.google_sign_in);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();


        Toolbar toolbar= (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        inputLayoutName = (TextInputLayout) findViewById(R.id.input_layout_name);
        inputLayoutPhone = (TextInputLayout) findViewById(R.id.input_layout_phone);
        inputLayoutPass = (TextInputLayout) findViewById(R.id.input_layout_pass);
        inputLayoutEmail = (TextInputLayout) findViewById(R.id.input_layout_email);
        inputName = (EditText) findViewById(R.id.input_name);
        inputPass = (EditText) findViewById(R.id.input_pass);
        inputPhone = (EditText) findViewById(R.id.input_phone);
        inputEmail = (EditText) findViewById(R.id.input_email);
        btn_register =  findViewById(R.id.normal_signup);

        inputName.addTextChangedListener(new MyTextWatcher(inputName));
        inputPass.addTextChangedListener(new MyTextWatcher(inputPass));
        inputPhone.addTextChangedListener(new MyTextWatcher(inputPhone));
        inputEmail.addTextChangedListener(new MyTextWatcher(inputEmail));


        vervication_code_input =(EditText) findViewById(R.id.input_code);
        vervication_code_input_layout  =(TextInputLayout) findViewById(R.id.input_layout_code);
        resend_vevication = findViewById(R.id.resend_vervication_code);
        phase_2_continue = findViewById(R.id.phase_2_continue);
        pahse_2 = findViewById(R.id.phase_2);
        pahse_2_layout = findViewById(R.id.phase_2_linear_layout);
        scess_layout = findViewById(R.id.sucess_con);
        scess_txt = findViewById(R.id.sucess_text);

        phase_2_continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                check_ver_code();
            }
        });
        resend_vevication.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                send_ver_code(2);
            }
        });

        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                register();

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

    public void google_sign_up(View view) {
        google_login();
    }

  public void face_sign_up(View view) {
      try{
          LoginManager.getInstance().logOut();
      }catch (Exception e){}

      Facebook();
      loginButton.performClick();
    }

  public void twitter_sign_up(View view) {
      twitter_login(view);

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
                case R.id.input_name:
                    validateName();
                    break;
                case R.id.input_phone:
                    validatePhone();
                    break;
                case R.id.input_pass:
                    validatePass();
                    break;
                case R.id.input_email:
                    validateEmail();
                    break;
            }
        }


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
                Config.BASE_URL+"register.php", new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
               findViewById(R.id.loadingSpinner).setVisibility(View.GONE);
                Log.e(TAG, "REGISTER response: " + response);

                try {
                    JSONObject obj = new JSONObject(response);
                    String res =   obj.getString("response");
                    if (res.equals("success") ){
                        //GO TO LOGIN
                        findViewById(R.id.code_confirm_container).setVisibility(View.VISIBLE);
                        collabse(findViewById(R.id.sign_up_layout),findViewById(R.id.sign_up_layout));
                        expand(pahse_2_layout,pahse_2);
                        Toast.makeText(getApplicationContext(), "" + "تم التسجيل بنجاح", Toast.LENGTH_LONG).show();
                    }else if(res.equals("error")) {
                        String message="";
                        if ( obj.getString("error").contains("Email"))
                         message = getString(R.string.dublicate_email);
                        if ( obj.getString("error").contains("Phone"))
                         message = getString(R.string.dublicate_phone);
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
               try {
                   Log.e("social prams ","size" +social_prams.size());
                   if (social_prams.size() >1){
                       params.put("img",social_prams.get("img"));
                       params.put("uid",social_prams.get("id"));
                   }
               }catch (NullPointerException e){
                   e.printStackTrace();
               }
                Log.e(TAG, "signup prams"+params.toString());
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
    void send_ver_code(final int phase){



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
                        expand(scess_txt,scess_layout);
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                Intent intent = new Intent(new Intent(getApplicationContext(),HomeActivity.class));
                                ComponentName cn = intent.getComponent();
                                Intent mainIntent = IntentCompat.makeRestartActivityTask(cn);
                                startActivity(mainIntent);
                                finish();

                            }
                        },700);

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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Make sure that the loginButton hears the result from any
        // Activity that it triggered.
        Log.e("data result",requestCode+"");

        if (FacebookSdk.isFacebookRequestCode(requestCode))
            mFacebookCallbackManager.onActivityResult(requestCode, resultCode, data);
        if(requestCode == TwitterAuthConfig.DEFAULT_AUTH_REQUEST_CODE)
            TwitterloginButton.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }


    }


    void facebook_login (View view){
        Facebook();
        loginButton.performClick();

    }


    void  twitter_login(View view){
        Twitter();
        TwitterloginButton.performClick();

    }
    void google_login(){
        signIn();
    }
    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }


    public void logoutTwitter() {
        TwitterSession twitterSession = TwitterCore.getInstance().getSessionManager().getActiveSession();
        if (twitterSession != null) {
            ClearCookies(getApplicationContext());
            Twitter.getSessionManager().clearActiveSession();
            Twitter.logOut();
        }
    }
    public static void ClearCookies(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
            CookieManager.getInstance().removeAllCookies(null);
            CookieManager.getInstance().flush();
        } else {
            CookieSyncManager cookieSyncMngr=CookieSyncManager.createInstance(context);
            cookieSyncMngr.startSync();
            CookieManager cookieManager=CookieManager.getInstance();
            cookieManager.removeAllCookie();
            cookieManager.removeSessionCookie();
            cookieSyncMngr.stopSync();
            cookieSyncMngr.sync();
        }
    }
    private void revokeAccess() {
        Auth.GoogleSignInApi.revokeAccess(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                    }
                });
    }
    private void signOut() {
        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                    }
                });
    }

    private void handleSignInResult(GoogleSignInResult result) {
        Log.d(TAG, "handleSignInResult:" + result.isSuccess());
        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount acct = result.getSignInAccount();

            Log.e(TAG, "display name: " + acct.getDisplayName() + acct.getId());
            String id = acct.getId() ;
            String personName = acct.getDisplayName();
            String personPhotoUrl = "";
            if(acct.getPhotoUrl() != null) {
                personPhotoUrl = acct.getPhotoUrl().toString();

            }
            String email = acct.getEmail();

            Log.e(TAG, "Name: " + personName + ", email: " + email
                    + ", Image: " + personPhotoUrl);

            social_prams = new HashMap<>();
            inputName.setText(personName);
            inputEmail.setText(email);

            social_prams.put("name",personName);
            social_prams.put("email",email);
            social_prams.put("id",id);
            social_prams.put("img",personPhotoUrl);

            signOut();
        } else {
            // Signed out, show unauthenticated UI.
        }
    }


    public void Facebook(){
        // If the access token is available already assign it.
        accessToken = AccessToken.getCurrentAccessToken();

        List< String > permissionNeeds = Arrays.asList("user_photos", "email",
                "user_birthday", "public_profile", "AccessToken");
        loginButton.registerCallback(mFacebookCallbackManager,
                new FacebookCallback< LoginResult >() {@Override
                public void onSuccess(LoginResult loginResult) {

                    System.out.println("onSuccess");

                    String accessToken = loginResult.getAccessToken()
                            .getToken();
                    Log.i("accessToken", accessToken);

                    GraphRequest request = GraphRequest.newMeRequest(
                            loginResult.getAccessToken(),
                            new GraphRequest.GraphJSONObjectCallback() {@Override
                            public void onCompleted(JSONObject object,
                                                    GraphResponse response) {

                                Log.i("LoginActivity",
                                        response.toString());
                                try {
                                    social_prams = new HashMap<>();
                                    id = object.getString("id");
                                 String   name = object.getString("name");
                                    inputName.setText(name);

                                    social_prams.put("name",name);
                                    social_prams.put("id",id);
                                    try {
                                        URL profile_pic = new URL(
                                                "http://graph.facebook.com/" + id + "/picture?type=large");
                                        Log.e("profile_pic",
                                                profile_pic + "");

                                        social_prams.put("img", String.valueOf(profile_pic));


                                    } catch (MalformedURLException e) {
                                        e.printStackTrace();
                                    }




                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }


                            }
                            });
                    Bundle parameters = new Bundle();
                    parameters.putString("fields",
                            "id,name,email,gender, birthday");
                    request.setParameters(parameters);
                    request.executeAsync();
                }

                    @Override
                    public void onCancel() {
                        System.out.println("onCancel");
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        System.out.println("onError");
                        Log.v("LoginActivity", exception.getCause().toString());
                    }
                });


    }
    //en face book
    public void Twitter(){

        TwitterloginButton.setCallback(new Callback<TwitterSession>() {
            @Override
            public void success(Result<TwitterSession> result) {
                // The TwitterSession is also available through:
                // Twitter.getInstance().core.getSessionManager().getActiveSession()
                TwitterSession session = result.data;

                Call<User> user = TwitterCore.getInstance().getApiClient().getAccountService().verifyCredentials(false, false);
                user.enqueue(new Callback<User>() {
                    @Override
                    public void success(Result<User> userResult) {
                        social_prams = new HashMap<>();

                        String name = userResult.data.name;
                        long tw_id = userResult.data.id;
                        String photo = userResult.data.profileImageUrlHttps;

                        inputName.setText(name);

                        social_prams.put("name",name);
                        social_prams.put("id", String.valueOf(tw_id));
                        social_prams.put("img",img);


                        Log.d(photo + name + id,"photo");
                    }

                    @Override
                    public void failure(TwitterException exc) {
                        Log.d("TwitterKit", "Verify Credentials Failure", exc);
                    }
                });
                // with your app's user model
                String msg = "@" + session.getUserName() + " logged in! (#" + session.getUserId() + ")";
            //    Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
            }
            @Override
            public void failure(TwitterException exception) {
                Log.d("TwitterKit", "Login with Twitter failure", exception);
            }
        });
    }
    //end twitter


    @Override
    public void onStart() {
        super.onStart();

        OptionalPendingResult<GoogleSignInResult> opr = Auth.GoogleSignInApi.silentSignIn(mGoogleApiClient);
        if (opr.isDone()) {
            // If the user's cached credentials are valid, the OptionalPendingResult will be "done"
            // and the GoogleSignInResult will be available instantly.
            Log.d(TAG, "Got cached sign-in");
            GoogleSignInResult result = opr.get();
            handleSignInResult(result);

        } else {
            // If the user has not previously signed in on this device or the sign-in has expired,
            // this asynchronous branch will attempt to sign in the user silently.  Cross-device
            // single sign-on will occur in this branch.
            opr.setResultCallback(new ResultCallback<GoogleSignInResult>() {
                @Override
                public void onResult(GoogleSignInResult googleSignInResult) {
                    handleSignInResult(googleSignInResult);
                }
            });
        }

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        // An unresolvable error has occurred and Google APIs (including Sign-In) will not
        // be available.
        Log.d(TAG, "onConnectionFailed:" + connectionResult);
    }

    @Override
    protected void onStop() {




        super.onStop();
    }
}
