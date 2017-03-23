package com.sayarat.Activity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.sayarat.R;
import com.sayarat.transition.FabTransform;

public class About_usActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);
        toolbar_action_setup();
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
