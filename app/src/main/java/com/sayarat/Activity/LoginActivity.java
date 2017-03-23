package com.sayarat.Activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v4.content.IntentCompat;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.ScaleAnimation;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
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
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookActivity;
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
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;
import com.sayarat.Models.UserModel;
import com.sayarat.R;
import com.sayarat.app.AppController;
import com.sayarat.app.Config;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterAuthToken;
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
import java.util.concurrent.Callable;

import io.fabric.sdk.android.Fabric;
import retrofit2.Call;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.common.server.converter.StringToIntConverter;

public class LoginActivity extends AppCompatActivity implements  GoogleApiClient.OnConnectionFailedListener {
    View normal_login , facebook_login , twitter_login , google_login , reqister , forget_password ;
    private EditText inputName, inputPass;
    private TextInputLayout inputLayoutName, inputLayoutPass;
    private String TAG = LoginActivity.class.getSimpleName();
    CallbackManager mFacebookCallbackManager;
    AccessToken accessToken;
    private static final int RC_SIGN_IN = 007;

    private static final String TWITTER_KEY = "uah4xLk9t7e9yWn7ls3zDwhW2";
    private static final String TWITTER_SECRET = "QijoDAzR3XzGNxppMuUGzboF69C1g5riQsAwCIS2yfSsoquTan";

    TwitterAuthClient mTwitterAuthClient ;
    private LoginButton loginButton;
    private String id;
    private TwitterLoginButton TwitterloginButton;
    private GoogleApiClient mGoogleApiClient;
    private SignInButton google_sn;
    private String uid;
    String pname,ppass;
    View login_activity_view , login_bg ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        TwitterSession session = Twitter.getSessionManager().getActiveSession();
//        TwitterAuthToken authToken = session.getAuthToken();
//        String token = authToken.token;
//        String secret = authToken.secret;


        overridePendingTransition(0,0);

        TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
        Fabric.with(this, new Twitter(authConfig));

        FacebookSdk.sdkInitialize(getApplicationContext());
        mFacebookCallbackManager = CallbackManager.Factory.create();


        setContentView(R.layout.activity_login);
        login_activity_view = findViewById(R.id.login_activity_view);
        login_bg = findViewById(R.id.login_bg);
        start_activty_anim();

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
        // start inti
        inputLayoutName = (TextInputLayout) findViewById(R.id.input_layout_email);
        inputLayoutPass = (TextInputLayout) findViewById(R.id.input_layout_pass);
        inputName = (EditText) findViewById(R.id.input_email);
        inputPass = (EditText) findViewById(R.id.input_pass);

        inputName.addTextChangedListener(new MyTextWatcher(inputName));
        inputPass.addTextChangedListener(new MyTextWatcher(inputPass));

        normal_login = findViewById(R.id.normal_login);
        google_login = findViewById(R.id.google_sign_in_button);
        twitter_login = findViewById(R.id.twitter_sign_in_button);
        facebook_login = findViewById(R.id.facebook_sign_in_button);
        forget_password = findViewById(R.id.forget_password);
        reqister = findViewById(R.id.reqister);
        //end inti

        normal_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login(1);
            }
        });
        forget_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                startActivity(new Intent( getApplicationContext() , HomeActivity.class));
            }
        });
        reqister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent( getApplicationContext() , SignUPActivity.class));
            }
        });
         mTwitterAuthClient = new TwitterAuthClient();
        findViewById(R.id.twitter_sign_in_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                findViewById(R.id.loadingSpinner).setVisibility(View.VISIBLE);
                Twitter();
                TwitterloginButton.performClick();
            }
        });
        findViewById(R.id.forget_password).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                forget_pass(view);
            }
        });
        google_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                findViewById(R.id.loadingSpinner).setVisibility(View.VISIBLE);
                google_login();
            }
        });
        findViewById(R.id.facebook_sign_in_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
             findViewById(R.id.loadingSpinner).setVisibility(View.VISIBLE);
                try{
                    LoginManager.getInstance().logOut();
                }catch (Exception e){}
                Facebook();
                loginButton.performClick();
            }
        });

//
//        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
//                .setDefaultFontPath("fonts/HelveticaNeueLT.otf")
//                .setFontAttrId(R.attr.fontPath)
//                .build()
//        );
    }

    private void start_activty_anim() {

//
//        ScaleAnimation scaleAnimation = new ScaleAnimation(1,1,0,1,Animation.RELATIVE_TO_SELF,0);
//        scaleAnimation.setDuration(350);
//        scaleAnimation.setStartOffset(20);
//        scaleAnimation.setInterpolator(new FastOutSlowInInterpolator());
//        login_bg.startAnimation(scaleAnimation);
        login_bg.setPivotY(0);
        login_bg.animate().scaleY(1).setDuration(300).setStartDelay(20).setInterpolator(new AccelerateInterpolator()).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationCancel(Animator animation) {
                super.onAnimationCancel(animation);
               login_bg.setScaleY(1);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);

            }
        });

        login_activity_view.setVisibility(View.VISIBLE);
        login_activity_view.animate().alpha(1).setDuration(700).setStartDelay(350).setInterpolator(new FastOutSlowInInterpolator()).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationCancel(Animator animation) {
                super.onAnimationCancel(animation);
                login_activity_view.setAlpha(1);
                login_activity_view.setVisibility(View.VISIBLE);
            }
        });
    }

//    @Override
//    protected void attachBaseContext(Context newBase) {
//        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
//    }




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
        findViewById(R.id.loadingSpinner).setVisibility(View.GONE);

        Log.d(TAG, "handleSignInResult:" + result.isSuccess());
        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount acct = result.getSignInAccount();

            Log.e(TAG, "display name: " + acct.getDisplayName() + acct.getId());
            uid = acct.getId() ;
            login(0);

            String personName = acct.getDisplayName();
            String personPhotoUrl = "";
            if(acct.getPhotoUrl() != null) {
                personPhotoUrl = acct.getPhotoUrl().toString();

            }
            String email = acct.getEmail();

            Log.e(TAG, "Name: " + personName + ", email: " + email
                    + ", Image: " + personPhotoUrl);

            signOut();

        } else {
            // Signed out, show unauthenticated UI.
        }
    }

    private void login(final int check) {
        if (check == 1) {
            if (!validateName()) {
                return;
            }

            if (!validatePass()) {
                return;
            }
             pname = inputName.getText().toString();
             ppass = inputPass.getText().toString();
        }

        else{
             pname = uid;
             ppass = "";
        }
        findViewById(R.id.loadingSpinner).setVisibility(View.VISIBLE);

        StringRequest strReq = new StringRequest(Request.Method.POST,
                Config.BASE_URL+"login.php", new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.e(TAG, "Login response: " + response);
                try {
                    JSONObject obj = new JSONObject(response);
                    String res =   obj.getString("response");
                    if (res.equals("success") ) {
                        JSONObject userObj = obj.getJSONObject("data");
                        UserModel user = new UserModel(userObj.getString("ID"),
                                userObj.getString("Name"),
                                userObj.getString("Phone"),
                                userObj.getString("Email"),
                                userObj.getString("picture"),
                                userObj.getString("Password")
                        );
                        user.setToken(FirebaseInstanceId.getInstance().getToken());
                        // storing user in shared preferences
                        AppController.getInstance().getPrefManager().storeUser(user);
                        findViewById(R.id.loadingSpinner).setVisibility(View.GONE);
                        Intent intent = new Intent(new Intent(getApplicationContext(),HomeActivity.class));
                        ComponentName cn = intent.getComponent();
                        Intent mainIntent = IntentCompat.makeRestartActivityTask(cn);
                        startActivity(mainIntent);
                        finish();


                    }else {
                        findViewById(R.id.loadingSpinner).setVisibility(View.GONE);
                        Toast.makeText(getApplicationContext(),getString(R.string.login_err),Toast.LENGTH_LONG).show();
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
                params.put("username", pname);
                params.put("password",ppass);
                if (check==1)
                    params.put("check","true");
                else
                    params.put("check","false");

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






    private void clear(){
        inputPass.setText(null);
        inputName.setText(null);
        inputLayoutName.setErrorEnabled(false);
        inputLayoutPass.setErrorEnabled(false);
    }

    public void forget_pass(View view) {
        startActivity(new Intent(getApplicationContext(),Forget_Password_Activity.class));
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
                case R.id.input_pass:
                    validatePass();
                    break;
            }
        }


    }
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

    private void incorrectPass() {
        inputLayoutPass.setError(getString(R.string.Enter_valid_PASS));
        requestFocus(inputPass);
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
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Make sure that the loginButton hears the result from any
        // Activity that it triggered.
        Log.e("data result",requestCode+"");

        if (FacebookSdk.isFacebookRequestCode(requestCode))
        mFacebookCallbackManager.onActivityResult(requestCode, resultCode, data);

      else   if(requestCode == TwitterAuthConfig.DEFAULT_AUTH_REQUEST_CODE)
            TwitterloginButton.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
     else   if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
        else
        findViewById(R.id.loadingSpinner).setVisibility(View.GONE);



    }

    public void Facebook(){
        // If the access token is available already assign it.
        accessToken = AccessToken.getCurrentAccessToken();

        List< String > permissionNeeds = Arrays.asList("user_photos", "email",
                "user_birthday", "public_profile", "AccessToken");
        loginButton.registerCallback(mFacebookCallbackManager,
                new FacebookCallback < LoginResult > () {@Override
                public void onSuccess(LoginResult loginResult) {
                    findViewById(R.id.loadingSpinner).setVisibility(View.GONE);

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
                                    id = object.getString("id");
                                    uid = String.valueOf(id);
                                    login(0);
                                    try {
                                        URL profile_pic = new URL(
                                                "http://graph.facebook.com/" + id + "/picture?type=large");
                                        Log.i("profile_pic",
                                                profile_pic + "");

                                    } catch (MalformedURLException e) {
                                        e.printStackTrace();
                                        findViewById(R.id.loadingSpinner).setVisibility(View.GONE);

                                    }

//                                    name = object.getString("name");
//                                    email = object.getString("email");
//                                    gender = object.getString("gender");
//                                    birthday = object.getString("birthday");
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    findViewById(R.id.loadingSpinner).setVisibility(View.GONE);

                                }

                                findViewById(R.id.loadingSpinner).setVisibility(View.GONE);


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
                        findViewById(R.id.loadingSpinner).setVisibility(View.GONE);
                        System.out.println("onCancel");
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        findViewById(R.id.loadingSpinner).setVisibility(View.GONE);
                        System.out.println("onError");
                        Log.e("LoginActivity", exception.toString());
                    }

                });


    }
    //en face book
    public void Twitter(){

        TwitterloginButton.setCallback(new Callback<TwitterSession>() {
            @Override
            public void success(Result<TwitterSession> result) {
                findViewById(R.id.loadingSpinner).setVisibility(View.GONE);
                // The TwitterSession is also available through:
                // Twitter.getInstance().core.getSessionManager().getActiveSession()
                TwitterSession session = result.data;

                Call<User> user = TwitterCore.getInstance().getApiClient().getAccountService().verifyCredentials(false, false);
                user.enqueue(new Callback<User>() {
                    @Override
                    public void success(Result<User> userResult) {
                        String name = userResult.data.name;
                        long id = userResult.data.id;
                        String photo = userResult.data.profileImageUrlHttps;
                        uid = String.valueOf(id);
                        login(0);
                        Log.d(photo + name + id,"photo");


                    }

                    @Override
                    public void failure(TwitterException exc) {
                        Log.d("TwitterKit", "Verify Credentials Failure", exc);
                    }
                });
                // with your app's user model
                String msg = "@" + session.getUserName() + " logged in! (#" + session.getUserId() + ")";
                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
                CookieSyncManager.createInstance(LoginActivity.this);
                CookieManager cookieManager = CookieManager.getInstance();
                cookieManager.removeSessionCookie();
                Twitter.getSessionManager().clearActiveSession();
                Twitter.logOut();
            }
            @Override
            public void failure(TwitterException exception) {
                findViewById(R.id.loadingSpinner).setVisibility(View.GONE);
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
            findViewById(R.id.loadingSpinner).setVisibility(View.GONE);
            // If the user's cached credentials are valid, the OptionalPendingResult will be "done"
            // and the GoogleSignInResult will be available instantly.
            Log.d(TAG, "Got cached sign-in");
            GoogleSignInResult result = opr.get();
            handleSignInResult(result);


        } else {
            findViewById(R.id.loadingSpinner).setVisibility(View.GONE);
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
        findViewById(R.id.loadingSpinner).setVisibility(View.GONE);
    }

    @Override
    public void onBackPressed() {
        LoginManager.getInstance().logOut();
        super.onBackPressed();
    }
}
