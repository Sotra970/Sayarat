package com.sayarat.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sayarat.Models.ProductModel;
import com.sayarat.R;

/**
 * Created by ahmed on 2/22/2017.
 */

public class DetailsDescriptionFragment extends Fragment {
    ProductModel extra_model ;
    boolean isOrder = false ;

    public void setOrder(boolean order) {
        isOrder = order;
    }

    View view;
    TextView id , price_type , price_value , using_status , status , make , type ,model , city , autmatic , car_set  , const_prod_num;
    public void setExtra_model(ProductModel extra_model) {
        this.extra_model = extra_model;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (view == null){
            view = inflater.inflate(R.layout.product_description_car_description,container,false) ;
            id = (TextView) view.findViewById(R.id.add_num);
            price_type = (TextView) view.findViewById(R.id.car_price_type);
            price_value = (TextView) view.findViewById(R.id.car_price_value);
            using_status = (TextView) view.findViewById(R.id.car_using_status);
            status = (TextView) view.findViewById(R.id.car_status);
            make = (TextView) view.findViewById(R.id.car_make);
            type = (TextView) view.findViewById(R.id.car_type);
            model = (TextView) view.findViewById(R.id.car_model);
            city = (TextView) view.findViewById(R.id.car_city);
            autmatic = (TextView) view.findViewById(R.id.car_auto);
            car_set = (TextView) view.findViewById(R.id.car_set);
            const_prod_num = (TextView) view.findViewById(R.id.const_prod_num);

            if(isOrder)
                const_prod_num.setText(getString(R.string.request_num));


            id            .setText(extra_model.getProduct_id());
            price_type    .setText(extra_model.getPricetype());
            price_value   .setText(extra_model.getPricevalue());
            using_status  .setText(extra_model.getUsingstatus());
            status        .setText(extra_model.getVehiclestatus());
            make          .setText(extra_model.getMake());
            type          .setText(extra_model.getType());
            model         .setText(extra_model.getYear());
            city          .setText(extra_model.getAddress());
            autmatic      .setText(extra_model.getAutomatic());
            Log.e("extra model ", "car set " + extra_model.getVehicleset());
            car_set       .setText(extra_model.getVehicleset());

        }
        return view;
    }
}
