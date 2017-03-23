package com.sayarat.interfaces;

import android.view.View;

import com.sayarat.Models.ProductModel;
import com.sayarat.Models.UserModel;

/**
 * Created by sotra on 2/27/2017.
 */
public interface req_listener {
    void start_profile(UserModel mdoel, View view) ;
    void start_detailes(ProductModel mdoel, View view) ;


}
