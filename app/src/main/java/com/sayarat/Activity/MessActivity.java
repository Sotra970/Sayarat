package com.sayarat.Activity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;

import com.sayarat.Adapter.Title_Pager_Adapter;
import com.sayarat.Fragments.DetailsDescriptionFragment;
import com.sayarat.Fragments.DetailsImagesFragment;
import com.sayarat.Fragments.MessageFragment;
import com.sayarat.R;
import com.sayarat.transition.FabTransform;

public class MessActivity extends AppCompatActivity {
    ViewPager viewPager ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mess);
        View main_container = findViewById(R.id.viewPager_mess);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
//            FabTransform.setup(this, main_container);}
        toolbar_action_setup();

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
         viewPager = (ViewPager) findViewById(R.id.viewPager_mess);

        viewPager.post(new Runnable() {
            @Override
            public void run() {
                viewPager.setTranslationY(1200);
                viewPager.animate()
                        .translationY(0f)
                        .setDuration(600)
                        .setInterpolator(new FastOutSlowInInterpolator());
            }
        });


        Title_Pager_Adapter viewPagerAdapter = new Title_Pager_Adapter(getSupportFragmentManager());

         MessageFragment inbox  = new MessageFragment();
        inbox.setType("inbox");

         MessageFragment outbox  = new MessageFragment();
        outbox.setType("outbox");

        viewPagerAdapter.addFragment(inbox,getString(R.string.inbox));
        viewPagerAdapter.addFragment(outbox,getString(R.string.outbox));

        viewPager.setAdapter(viewPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);

    }



    void toolbar_action_setup(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        View search_action = findViewById(R.id.main_toolbar_search);
        search_action.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startSearchActivity(view);
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() ==android.R.id.home )
            onBackPressed();
        return true;
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
