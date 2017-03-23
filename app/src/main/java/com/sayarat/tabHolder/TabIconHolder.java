package com.sayarat.tabHolder;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.sayarat.R;


/**
 * Created by Sotraa on 6/13/2016.
 */
public class TabIconHolder {
    private final View view;
    ImageView tabImage ;
    TextView title ;
    LayoutInflater inflater ;
    public TabIconHolder(int icon  , String title , Activity context){
        inflater = LayoutInflater.from(context);
        view = inflater.inflate(R.layout.tab_icon, null);
//        view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        this.title = (TextView) view.findViewById(R.id.holderTabTitle);
        this.title.setText(title);
        tabImage = (ImageView) view.findViewById(R.id.holderTabIcon);
        tabImage.setImageResource(icon);
     //   RefrechNotfication();
    }
    public View getView (){
        return view;
    }
}
