package com.sayarat.Activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.GlideBitmapDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.github.pwittchen.infinitescroll.library.InfiniteScrollListener;
import com.sayarat.Adapter.AdsAdapter;
import com.sayarat.Adapter.MostProdaactsPAgerAdapter;
import com.sayarat.interfaces.Add_listener;
import com.sayarat.interfaces.EndlessScrollListener;
import com.sayarat.Models.ProductModel;
import com.sayarat.Models.UserModel;
import com.sayarat.R;
import com.sayarat.app.Config;
import com.sayarat.app.AppController;
import com.sayarat.interfaces.LikePlus;
import com.sayarat.transition.FabTransform;

import net.lucode.hackware.magicindicator.MagicIndicator;
import net.lucode.hackware.magicindicator.ViewPagerHelper;
import net.lucode.hackware.magicindicator.buildins.circlenavigator.CircleNavigator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class HomeActivity extends AppCompatActivity  implements View.OnClickListener , Add_listener{
    private static final String TAG = "ads clicklistener";
    MostProdaactsPAgerAdapter mostProdaactsPAgerAdapter ;
    ViewPager mostProductsViewPager ;
    MagicIndicator mostProductsIndicator ;
    ArrayList<ArrayList<ProductModel>> mostProductsImagesUrls = new ArrayList<>() ;
    private Boolean isFabOpen = false;
    private ImageView fab,fab1,fab2;
    private Animation fab_open,fab_close,rotate_forward,rotate_backward;
    View fab1_container , fab2_container ;
    private View rootLayoutEmulat;
    private NavigationView navigationView;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private Toolbar toolbar;
    RecyclerView ads ;
    AdsAdapter ads_adapter ;
    ArrayList<ProductModel> ads_data = new ArrayList<>() ;
    ImageView profile_img  , profile_logo;
    TextView profile_name ;
    private SwipeRefreshLayout swipeRefresh;
    private boolean loading = true;
    int pastVisiblesItems, visibleItemCount, totalItemCount;
    LinearLayoutManager mLayoutManager;
    private int gpage=0;
    EndlessScrollListener endlessScrollListener ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        //start init
        rootLayoutEmulat = findViewById(R.id.reval_bg);
        toolbar_action_setup(getString(R.string.title_sayrat));
        //end inti
        navigationView = (NavigationView) findViewById(R.id.nav_view) ;
        View headerLayout = navigationView.getHeaderView(0); // 0-index header
        profile_img = (ImageView) headerLayout.findViewById(R.id.nav_img);
        profile_name = (TextView) headerLayout.findViewById(R.id.nav_name);
        profile_logo = (ImageView) headerLayout.findViewById(R.id.nav_logo);


        SimpleTarget target = new SimpleTarget<GlideBitmapDrawable>() {
            @Override
            public void onResourceReady(GlideBitmapDrawable bitmap, GlideAnimation glideAnimation) {
                // do something with the bitmap
                // for demonstration purposes, let's just set it to an ImageView

                RoundedBitmapDrawable circularBitmapDrawable =
                        RoundedBitmapDrawableFactory.create(getApplication().getResources(), bitmap.getBitmap());
                circularBitmapDrawable.setCircular(true);
                profile_img.setImageDrawable( circularBitmapDrawable );
                try{
                    profile_name.setText(AppController.getInstance().getPrefManager().getUser().getUser_name());
                }catch (Exception e){}
                profile_logo.setVisibility(View.GONE);

            }
        };

        try{
            Glide.with(getApplicationContext())
                    .load( AppController.getInstance().getPrefManager().getUser().getImg())
                    .centerCrop()
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .into(target);
        Log.e("pp" , AppController.getInstance().getPrefManager().getUser().getImg());

        }catch (Exception e){}

        setupNavigtionDrawer();
      //  mostProducts_setup();
        setup_fabs();
        setup_ads();
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


    private void setup_fabs() {
        fab = (ImageButton) findViewById(R.id.fab);
        fab1 = (ImageButton)findViewById(R.id.req_car);
        fab2 = (ImageButton)findViewById(R.id.add_car);
        fab1_container = findViewById(R.id.fab1_container);
        fab2_container = findViewById(R.id.fab2_container);

         rotate_forward = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.rotate_forward);
        rotate_backward = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.rotate_backward);
        fab.setOnClickListener(this);
        fab1.setOnClickListener(this);
        fab2.setOnClickListener(this);
    }

//    void mostProducts_setup(){
//        getMostProductsUrls();
//        mostProdaactsPAgerAdapter  = new MostProdaactsPAgerAdapter(getApplicationContext(),mostProductsImagesUrls) ;
//        mostProductsViewPager = (WrapContentViewPager) findViewById(R.id.most_products_view_pager);
//        mostProductsIndicator = (MagicIndicator) findViewById(R.id.most_products_view_pager_indicator);
//        mostProductsViewPager.setAdapter(mostProdaactsPAgerAdapter);
//        setupIndecator(mostProductsViewPager , mostProductsImagesUrls.size());
//    }
    protected void setupIndecator(final ViewPager mViewPager ,int size){
        final CircleNavigator circleNavigator = new CircleNavigator(this);
        circleNavigator.setCircleCount(size);
        circleNavigator.setRadius(8);
        circleNavigator.setCircleSpacing(18);
        circleNavigator.setCircleColor(ContextCompat.getColor(getApplicationContext(),R.color.colorAccent));
        mostProductsIndicator.setNavigator(circleNavigator);
        ViewPagerHelper.bind(mostProductsIndicator,mViewPager);
    }
    void getMostProductsUrls(){
        // max 3 sectors
//        mostProductsImagesUrls = new ArrayList<>() ;
//        for (int i=0 ; i<3 ; i++){
//          ArrayList<ProdauctModel>  urlsgroup = new ArrayList<>() ;
//            ProdauctModel prodauctModel1 = new ProdauctModel() ;
//            ProdauctModel prodauctModel2 = new ProdauctModel() ;
//            ProdauctModel prodauctModel3 = new ProdauctModel() ;
//            prodauctModel1.setImg(Config.img_test);
//            prodauctModel2.setImg(Config.img_test);
//            prodauctModel3.setImg(Config.img_test);
//
//          prodauctModel1.setLikes("123");
//          prodauctModel2.setLikes("123");
//          prodauctModel3.setLikes("123");
//           prodauctModel1.setViews("555");
//           prodauctModel3.setViews("555");
//           prodauctModel2.setViews("555");
//
//            urlsgroup.add(prodauctModel1);
//            urlsgroup.add(prodauctModel2);
//            urlsgroup.add(prodauctModel3);
//            mostProductsImagesUrls.add(urlsgroup) ;
//        }

    }

    public void animateFAB(){
        final int dp48 = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 48, getResources().getDisplayMetrics());

        if(isFabOpen){
            exit_reveal();
            fab.startAnimation(rotate_backward);
            fab1_container.animate().alpha(0).setInterpolator(new AccelerateDecelerateInterpolator()) . setDuration(50).start();
            fab2_container.animate().alpha(0).setInterpolator(new AccelerateDecelerateInterpolator()) . setDuration(50).start();

            fab2_container.animate().translationY(dp48).setInterpolator(new AccelerateInterpolator()) . setDuration(50).start();
            fab1_container.animate().translationY((dp48*2)).setInterpolator(new AccelerateInterpolator()) . setDuration(50).start();

            fab1.setClickable(false);
            fab2.setClickable(false);
            isFabOpen = false;
            Log.d("Raj", "close");

        } else {
            circularRevealActivity();
            fab.startAnimation(rotate_forward);
            fab1_container.animate().alpha(1).setInterpolator(new AccelerateDecelerateInterpolator()) . setDuration(80).start();
            fab2_container.animate().alpha(1).setInterpolator(new AccelerateDecelerateInterpolator()) . setDuration(80).start();

            fab1_container.animate().translationY(0).setInterpolator(new AccelerateDecelerateInterpolator()) . setDuration(100).start();
            fab2_container.animate().translationY((0)).setInterpolator(new AccelerateDecelerateInterpolator()) . setDuration(100).start();

            fab1.setClickable(true);
            fab2.setClickable(true);
            isFabOpen = true;
            Log.d("Raj","open");

        }
    }

    private void setupRefreshLayout(){
        swipeRefresh = (SwipeRefreshLayout) findViewById(R.id.swipeRefresh);
        swipeRefresh.setColorSchemeColors(ContextCompat.getColor(getApplicationContext(),R.color.colorAccent));
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                get_ads(gpage);
            }
        });
    }

    private void endLoading() {
        swipeRefresh.post(new Runnable() {
            @Override
            public void run() {
                swipeRefresh.setRefreshing(false);
            }
        });
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR1) {
            ads.setAlpha(0);
            ads.animate().alpha(1);
        }else {
            ads.setVisibility(View.VISIBLE);
        }
    }

    void show_loading(){
        swipeRefresh.post(new Runnable() {
            @Override
            public void run() {
                swipeRefresh.setRefreshing(true);
            }
        });

    }
    @Override
    public void onClick(View v) {
        if (AppController.getInstance().getPrefManager().getUser() != null ){
            int id = v.getId();
            switch (id){
                case R.id.fab:
                    animateFAB();
                    break;
                case R.id.req_car:
                    startUploadActivity(fab1,"req_car");
                    break;
                case R.id.add_car:
                    startUploadActivity(fab2,"add_car");
                    break;
            }
        }else  startActivity(new Intent(getApplicationContext(),LoginActivity.class));
    }
    private void exit_reveal() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
        int point[] = new int[2] ;
        fab.getLocationOnScreen(point);
//        point.x = (int) fab.getX() + (fab.getMeasuredWidth()/2);
//        point.y = (int) fab.getY() + (fab.getMeasuredHeight()/2);
//        Log.e("point" , "x" +point.x +"  y"+point.y);
        float finalRadius = Math.max(rootLayoutEmulat.getWidth(), rootLayoutEmulat.getHeight());
        final int dp48 = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 48, getResources().getDisplayMetrics());

        // create the animator for this view (the start radius is zero)
        Animator circularReveal = null;

            circularReveal = ViewAnimationUtils.createCircularReveal(rootLayoutEmulat,(point[0]+(fab.getMeasuredWidth()/2)), point[1], finalRadius,(dp48/2) );
        circularReveal.setDuration(300);
        circularReveal.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                rootLayoutEmulat.setVisibility(View.GONE);

            }

            @Override
            public void onAnimationCancel(Animator animation) {
                super.onAnimationCancel(animation);
            }
        });
        // make the view visible and start the animation

        circularReveal.start();
        }else {
            rootLayoutEmulat.animate().alpha(0).setDuration(100);

        }

    }

    void toolbar_action_setup(String title){
        toolbar = (Toolbar) findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);
        View search_action = findViewById(R.id.main_toolbar_search);
        View menu_action = findViewById(R.id.main_toolbar_inbox);
        TextView page_title = (TextView) findViewById(R.id.main_toolbar_title);
        page_title.setText(title);
        search_action.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               startSearchActivity(view);
            }
        });
        menu_action.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (AppController.getInstance().getPrefManager().getUser() != null)
                    startMessageActivity(view);
                else
                    startActivity(new Intent(getApplicationContext(), LoginActivity.class));

            }
        });

    }



    private void circularRevealActivity() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP){

//            Point point = new Point();
//            point.x = (int) fab.getX() + (fab.getMeasuredWidth()/2);
//            point.y = (int) fab.getY() + (fab.getMeasuredHeight()/2);
//            Log.e("point" , "x" +point.x +"  y"+point.y);

            int point[] = new int[2] ;
            fab.getLocationOnScreen(point);

            float finalRadius = Math.max(rootLayoutEmulat.getWidth(), rootLayoutEmulat.getHeight());

            // create the animator for this view (the start radius is zero)
            Animator circularReveal = ViewAnimationUtils.createCircularReveal(rootLayoutEmulat, (point[0]+(fab.getMeasuredWidth()/2)), point[1] ,  0, finalRadius);
            circularReveal.setDuration(350);
            circularReveal.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                }

                @Override
                public void onAnimationCancel(Animator animation) {
                    super.onAnimationCancel(animation);
                }

                @Override
                public void onAnimationStart(Animator animation) {
                    rootLayoutEmulat.setVisibility(View.VISIBLE);
                }
            });
            // make the view visible and start the animation

            circularReveal.start();

        } else{
            rootLayoutEmulat.setAlpha(0);
            rootLayoutEmulat.setVisibility(View.VISIBLE);
            rootLayoutEmulat.animate().alpha(1).setDuration(150);

        }
    }

    private void setupNavigtionDrawer() {
//        replaceFragment(new CategoriesFragment());
        setUpDrawerToggel();
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        if(AppController.getInstance().getPrefManager().getUser() != null)
        {
            navigationView.getMenu().clear();
            navigationView.inflateMenu(R.menu.main_menu);
        } else
        {
            navigationView.getMenu().clear();
            navigationView.inflateMenu(R.menu.main_menu2);
        }
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                if (AppController.getInstance().getPrefManager().getUser() != null) {

                    switch (item.getItemId()) {
                        case R.id.logout_action:
                            mDrawerLayout.closeDrawers();
                            AppController.getInstance().getPrefManager().clear();
                            break;
                        case R.id.home_action:
                            mDrawerLayout.closeDrawers();
                            break;
                        case R.id.add_car_action:
                            Intent intent = new Intent(getApplicationContext(), UploadActivity.class);
                            intent.putExtra("type", "add_car");
                            startActivity(intent);
                            break;
                        case R.id.profile_action:
                            startActivity(new Intent(getApplicationContext(), Profile_Activity.class));

                            break;
                        case R.id.req_action:
                            startActivity(new Intent(getApplicationContext(), ReqsActivity.class));

                            break;

                        case R.id.messages_action:
                            startActivity(new Intent(getApplicationContext(), MessActivity.class));

                            break;
                        case R.id.ta7weel_action:
                            startActivity(new Intent(getApplicationContext(), CommActivity.class));

                            break;
                        case R.id.about_action:
                            startActivity(new Intent(getApplicationContext(), About_usActivity.class));

                            break;
                        case R.id.contact_action:
                            startActivity(new Intent(getApplicationContext(), Contact_usActivity.class));

                            break;
                        case R.id.settings_action:
                            startActivity(new Intent(getApplicationContext(), SettingsActivity.class));

                            break;

                    }

                }
                else {

                    switch (item.getItemId()) {

                        case R.id.req_action:
                            startActivity(new Intent(getApplicationContext(), ReqsActivity.class));

                            break;
                        case R.id.about_action:
                            startActivity(new Intent(getApplicationContext(), About_usActivity.class));

                            break;
                        case R.id.contact_action:
                            startActivity(new Intent(getApplicationContext(), Contact_usActivity.class));

                            break;
                        case R.id.sign_up_action:
                            startActivity(new Intent(getApplicationContext(), SignUPActivity.class));

                            break;
                        case R.id.sign_in_action:
                            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                            break;

                    }

                }
                item.setCheckable(true);
                mDrawerLayout.closeDrawers();
                return true;
            }
        });

    }

    private void setUpDrawerToggel() {
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                toolbar, R.string.drawer_open, R.string.drawer_close){

        } ;

        // Set the drawer toggle as the DrawerListener
        mDrawerLayout.addDrawerListener(mDrawerToggle);

    }
    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    void startUploadActivity(View view , String type ){
        Intent intent = null;
            intent = new Intent(getApplicationContext(), UploadActivity.class);
        intent.putExtra("type" , type) ;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(HomeActivity.this,
                    view, getApplication().getString(R.string.add_adv));
            FabTransform.addExtras(intent,
                    ContextCompat.getColor(getApplicationContext(), R.color.white),R.drawable.ic_add_white_24dp);

            startActivity(intent,options.toBundle());
        }else {
            startActivity(intent);
        }
    }


    void startMessageActivity(View view ){
        Intent intent = null;
        intent = new Intent(getApplicationContext(), MessActivity.class);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(HomeActivity.this,
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
            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(HomeActivity.this,
                    view, getApplication().getString(R.string.search_transtion));
            FabTransform.addExtras(intent,
                    ContextCompat.getColor(getApplicationContext(), R.color.colorPrimary),R.drawable.ic_search_white_24dp);

            startActivity(intent,options.toBundle());
        }else {
            startActivity(intent);
        }
    }
    void  setup_ads(){
        mLayoutManager = new LinearLayoutManager(this);
        ads = (RecyclerView) findViewById(R.id.ads_recycler) ;
        ads.setHasFixedSize(true);

        // Attach the listener to the AdapterView onCreate
        endlessScrollListener = new EndlessScrollListener(mLayoutManager) {

            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                Log.e("ads page",page + "");
                gpage = page ;
                get_ads(page);
            }
        };
//        endlessScrollListener =new EndlessScrollListener(mLayoutManager) {
//            @Override
//            public void onLoadMore(int current_page) {
//                Log.e("ads page",current_page + "");
//                gpage = 0 ;
//                get_ads(current_page);
//            }
//        };

        ads.setLayoutManager(mLayoutManager);

        ads_adapter = new AdsAdapter(ads_data , this , this);
        ads.setAdapter(ads_adapter);
        ads.addOnScrollListener(endlessScrollListener);
        setupRefreshLayout();
        get_ads(0);
    }

    void get_ads (final int page) {
        show_loading();

        StringRequest requesturl = new StringRequest(Request.Method.POST, Config.BASE_URL+"ads.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("get ads", "respone "+response.toString()) ;

                findViewById(R.id.progressBar).setVisibility(View.GONE);
                try {
                    JSONObject response_obj = new JSONObject(response);
                    if (response_obj.getString("response").equals("success")){
                        JSONArray  feed_arr = response_obj.getJSONArray("data") ;
                        for (int i=ads_data.size(); i<feed_arr.length();i++){
                            JSONObject add = feed_arr.getJSONObject(i) ;

                            JSONArray images = add.getJSONArray("images") ;
                            ArrayList<String> imgs = new ArrayList<>();
                            for (int j=0; j<images.length();j++){
                                imgs.add(images.getJSONObject(j).getString("image").trim());
                            }

                            ProductModel prodauctModel = new ProductModel() ;
                            prodauctModel.setImgs(imgs);
                            prodauctModel.setType(add.getString("type"));
                            prodauctModel.setTitle(add.getString("title"));
                            prodauctModel.setMake(add.getString("make"));
                            prodauctModel.setYear(add.getString("year"));
                            prodauctModel.setAdvertisertype(add.getString("advertisertype"));
                            prodauctModel.setUsingstatus(add.getString("usingstatus"));
                            prodauctModel.setPricetype(add.getString("pricetype"));

                            String price ;
                            try{
                                 price = add.getString("pricevalue") ;
                                if (price.isEmpty()|| price.trim().equals("على السوم"))
                                    price = prodauctModel.getPricetype();
                                else  price = price.concat(getString(R.string.ryal));
                            }catch (NullPointerException e){
                                price = prodauctModel.getPricetype();
                            }
                            prodauctModel.setPricevalue(price);

                            prodauctModel.setDetails(add.getString("details"));
                            prodauctModel.setOdemetertype(add.getString("odemetertype"));
                            prodauctModel.setOdemetervaue(add.getString("odemetervaue"));
                            prodauctModel.setGuarantee(add.getString("Guarantee"));
                            prodauctModel.setAdvertiserID(add.getString("advertiserID"));
                            prodauctModel.setDate(add.getString("date"));
                            prodauctModel.setAutomatic(add.getString("automatic"));
                            prodauctModel.setProduct_id(add.getString("Id"));
                            prodauctModel.setAddress(add.getString("Address"));
                            prodauctModel.setActive(add.getString("Active"));
                            prodauctModel.setPhone(add.getString("phone"));
                            prodauctModel.setVehiclestatus(add.getString("vehiclestatus"));
                            prodauctModel.setVehicleset(add.getString("vehicleset"));
                            prodauctModel.setLikes(add.getString("likes"));
                            try{

                                JSONObject temp = add.getJSONObject("user");
                                UserModel user = new UserModel(temp.getString("ID"),
                                        temp.getString("Name"),
                                        temp.getString("Phone"),
                                        temp.getString("Email"),
                                        temp.getString("picture")
                                );

                                prodauctModel.setUserModel(user);
                                ads_data.add(prodauctModel);
                            }catch (Exception e){

                            }
                            Log.e("data", prodauctModel.getImgs().size()+"");
                        }
                        endLoading();

                        ads_adapter.notifyDataSetChanged();
                       // endlessScrollListener.resetState();


                    }else {
                        Toast.makeText(getApplicationContext(),getString(R.string.no_data) , Toast.LENGTH_LONG).show();
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e("get ads", "parsing err "+e.toString()) ;
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("ads  response  err" , error.toString()) ;
            }
        }
        ){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String , String> params = new HashMap<>();
                params.put("page", String.valueOf(gpage));
                return params;
            }
        };
        int socketTimeout = 5000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout,
                12,
                1);

        requesturl.setRetryPolicy(policy);
        AppController.getInstance().addToRequestQueue(requesturl);



    }


    @Override
    public void share(ProductModel prodauctModel, View img) {

        img.setDrawingCacheEnabled(true);
        Bitmap bitmap = Bitmap.createBitmap(img.getDrawingCache());
        share_ch(prodauctModel,bitmap);
    }

    @Override
    public void fav(String prodauctModel) {
        fav_req(prodauctModel);

    }

    @Override
    public void like(String prodauctModel, LikePlus likePlus) {
        if (AppController.getInstance().getPrefManager().getUser() != null)
        like_req(prodauctModel,likePlus);
        else
            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
    }

    @Override
    public void start_detailes(ProductModel prodauctModel) {
        startDetailACtivity(prodauctModel);
    }


    void like_req(final String id, final LikePlus likePlus){

        StringRequest requesturl = new StringRequest(Request.Method.POST, Config.BASE_URL +"add_ads_likes.php" , new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("get ads", "respone " + response.toString());

                try {
                    JSONObject response_obj = new JSONObject(response);
                    if (response_obj.getString("response").equals("success")){
                        String message = "تم اضافة الاعجاب";
                        Toast.makeText(getApplicationContext(), "" + message, Toast.LENGTH_LONG).show();
                        int edited_pos =    likePlus.plusOne();
                        ProductModel edited_model = ads_data.get(edited_pos);
                        int prv_likes = Integer.valueOf(edited_model.getLikes());
                        edited_model.setLikes(String.valueOf(++prv_likes));
                        ads_data.set(edited_pos,edited_model);
                    }


                    else {
                        String message = response_obj.getString("error");
                        Toast.makeText(getApplicationContext(), "" + message, Toast.LENGTH_LONG).show();
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e("get ads", "parsing err " + e.toString());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("ads  response  err", error.toString());
                if (error instanceof NoConnectionError) {
                    String message =   "يرجى التاكد من الانترنت و اعادة المحاوله";
                    Toast.makeText(getApplicationContext(), "" + message, Toast.LENGTH_LONG).show();
                }
            }
        }
        ){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> params = new HashMap<>();
                params.put("user_id",AppController.getInstance().getPrefManager().getUser().getUser_id());
                params.put("ad_id",id);


                Log.e("add add params", params.toString());

                return params;
            }
        };
        int socketTimeout = 5000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout,
                12,
                1);

        requesturl.setRetryPolicy(policy);
        AppController.getInstance().addToRequestQueue(requesturl);

    }
    void fav_req(final String id){

        StringRequest requesturl = new StringRequest(Request.Method.POST, Config.BASE_URL +"add_ads_likes.php" , new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("get ads", "respone " + response.toString());

                try {
                    JSONObject response_obj = new JSONObject(response);
                    if (response_obj.getString("response").equals("success")){
                        String message = "تم الاضافة للمفضلة";
                        Toast.makeText(getApplicationContext(), "" + message, Toast.LENGTH_LONG).show();
                    }


                    else {
                        String message = "لم يتم الاضافة للمفضلة";
                        Toast.makeText(getApplicationContext(), "" + message, Toast.LENGTH_LONG).show();
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e("get ads", "parsing err " + e.toString());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("ads  response  err", error.toString());
                if (error instanceof NoConnectionError) {
                    String message =   "يرجى التاكد من الانترنت و اعادة المحاوله";
                    Toast.makeText(getApplicationContext(), "" + message, Toast.LENGTH_LONG).show();
                }
            }
        }
        ){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> params = new HashMap<>();
                params.put("user_id",AppController.getInstance().getPrefManager().getUser().getUser_id());
                params.put("ad_id",id);


                Log.e("add add params", params.toString());

                return params;
            }
        };
        int socketTimeout = 5000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout,
                12,
                1);

        requesturl.setRetryPolicy(policy);
        AppController.getInstance().addToRequestQueue(requesturl);

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


    private  void startDetailACtivity(  ProductModel selected){
        Intent intent = new Intent(getApplicationContext(),ProductDetailsActivity.class);
        intent.putExtra("extra_model",selected);

        startActivityForResult(intent,500);


    }

    void makescreenshot(Bitmap bitmap , String id){
        File outputDir = Environment.getExternalStorageDirectory(); // context being the Activity pointer

        File outputFile = new File(outputDir, id+".jpg" );
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
    }



    private InfiniteScrollListener createInfiniteScrollListener() {
        return new InfiniteScrollListener(20, mLayoutManager) {
            @Override public void onScrolledToEnd(final int firstVisibleItemPosition) {
                gpage ++;
                get_ads(gpage);
                // load your items here
                // logic of loading items will be different depending on your specific use case

                // when new items are loaded, combine old and new items, pass them to your adapter
                // and call refreshView(...) method from InfiniteScrollListener class to refresh RecyclerView
            }
        };
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.e("codes results" , requestCode +"   "+resultCode);
        if (resultCode == 200){
          ads_data.remove((ProductModel) data.getExtras().get("model"));
            ads_data.clear();
            get_ads(0);
        }


    }
}
