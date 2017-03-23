package com.sayarat.Fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.sayarat.app.AppController;
import com.sayarat.interfaces.Add_adv_listener;
import com.sayarat.Models.ProductModel;
import com.sayarat.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class Add_adv_adertiser_info_Fragment extends Fragment {
    View view ;
    Add_adv_listener add_adv_listener ;
    ProductModel prodauctModel = new ProductModel();
    EditText name , phone , email ;
    ProductModel extra_model ;

    public void setExtra_model(ProductModel extra_model) {
        this.extra_model = extra_model;
    }


    public void setAdd_adv_listener(Add_adv_listener add_adv_listener) {
        this.add_adv_listener = add_adv_listener;
    }

    public Add_adv_adertiser_info_Fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (view == null){
            view =inflater.inflate(R.layout.fragment_add_adv_adertiser_info_, container, false);
            name = (EditText) view.findViewById(R.id.input_name) ;
            phone = (EditText) view.findViewById(R.id.input_phone) ;
            email = (EditText) view.findViewById(R.id.input_email) ;
            View continue_btt = view.findViewById(R.id.continue_btt) ;
            continue_btt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                  put_data();
                }
            });

            if (extra_model==null) {
                name.setText(AppController.getInstance().getPrefManager().getUser().getUser_name());
                email.setText(AppController.getInstance().getPrefManager().getUser().getEmail());
                phone.setText(AppController.getInstance().getPrefManager().getUser().getUser_phone());
            }else {
                name.setText( extra_model.getUserModel().getUser_name());
                email.setText( extra_model.getUserModel().getEmail());
                phone.setText( extra_model.getPhone());
            }

        }
        // Inflate the layout for this fragment
        return view ;
    }
    private boolean validate_ed(EditText ed ) {
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




    void put_data() {


     if (!validate_ed(name))
            return;
        if (!validate_ed(email))
            return;
  if (!validate_ed(phone))
            return;
        prodauctModel.setPhone(phone.getText().toString().trim());


        add_adv_listener.advertiser_info(prodauctModel);
    }
}
