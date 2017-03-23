package com.sayarat.interfaces;

import android.view.View;
import android.widget.ImageView;

import com.sayarat.Models.ProductModel;

/**
 * Created by sotra on 2/27/2017.
 */
public interface Add_listener {
    void share(ProductModel prodauctModel , View img) ;
    void fav(String prodauctModel) ;
    void like(String prodauctModel, LikePlus likePlus) ;
    void start_detailes(ProductModel prodauctModel) ;

}
