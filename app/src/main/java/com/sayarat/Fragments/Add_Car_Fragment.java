package com.sayarat.Fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.sayarat.interfaces.Add_adv_listener;
import com.sayarat.Models.ProductModel;
import com.sayarat.R;
import com.sayarat.app.AppController;
import com.sayarat.app.Config;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class Add_Car_Fragment extends Fragment  implements AdapterView.OnItemSelectedListener {
    View view;
    Spinner city_spinner, com_spinner, use_type_spinner, status_spinner, model_spinner, autamatic_spinner, set_spinner, counter_type_spinner, price_type_spinner , make_spinner;
    ArrayList<String> city = new ArrayList<>(), com = new ArrayList<>(), use_type = new ArrayList<>() ,make = new ArrayList<>() , make_id=new ArrayList<>() ,make_com_id=new ArrayList<>() ,make_for_ad=new ArrayList<>();
    ArrayList<String>status = new ArrayList<>(), model = new ArrayList<>(), sets = new ArrayList<>(), autamatic = new ArrayList<>(), counter_type = new ArrayList<>(), price_type= new ArrayList<>();
    ArrayList<String> city_id = new ArrayList<>(), model_id = new ArrayList<>(), com_id = new ArrayList<>();
    String city_txt = "", com_txt = "", use_type_txt = "", status_txt = "", model_txt = "", sets_txt = "", autamatic_txt = "", counter_type_txt = "", price_type_txt = "";
    Add_adv_listener add_adv_listener;
    ProductModel prodauctModel = new ProductModel();
    ArrayAdapter adapter ,adapter5 ,adapter4 , adapter_make ;
    EditText price , counter , title , detailes ;
    CheckBox under_worenty ;
    private String make_txt = "";
    View price_hover ;
    ProductModel extra_model ;

    public void setExtra_model(ProductModel extra_model) {
        this.extra_model = extra_model;
    }

    public Add_Car_Fragment() {
        // Required empty public constructor
    }

    public void setAdd_adv_listener(Add_adv_listener add_adv_listener) {
        this.add_adv_listener = add_adv_listener;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        get_data();
        view = inflater.inflate(R.layout.fragment_add__car, container, false);
        View continue_btt = view.findViewById(R.id.continue_btt);
        continue_btt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              put_data();
            }
        });

        price_hover= this.view.findViewById(R.id.price_type_hover) ;
        title = (EditText) view.findViewById(R.id.add_title_input) ;
        price = (EditText) view.findViewById(R.id.add_price_input) ;
        counter = (EditText) view.findViewById(R.id.add_counter_input) ;
        detailes = (EditText) view.findViewById(R.id.more_details) ;
        under_worenty = (CheckBox) view.findViewById(R.id.under_worenty) ;
        //city spinner
        city_spinner = (Spinner) view.findViewById(R.id.city_spiner);
        city.add("المدينة");
        city_id.add("المدينة");
        adapter = new ArrayAdapter(getActivity().getBaseContext(), R.layout.simple_spinner_item, city);
        adapter.setDropDownViewResource(R.layout.simple_spinner_item);
        city_spinner.setAdapter(adapter);
        city_spinner.setOnItemSelectedListener(this);


        make_spinner = (Spinner) view.findViewById(R.id.make_spiner);
        make_for_ad.add("النوع");
        make.add("النوع");
        make_com_id.add("النوع");
        make_id.add("النوع");

        adapter_make = new ArrayAdapter(getActivity().getBaseContext(), R.layout.simple_spinner_item, make_for_ad);
        adapter_make.setDropDownViewResource(R.layout.simple_spinner_item);
        make_spinner.setAdapter(adapter_make);
        make_spinner.setOnItemSelectedListener(this);

        //autamatic_spinner
        autamatic_spinner = (Spinner) view.findViewById(R.id.automatic_spiner);
        autamatic.add("نوع القير");
        autamatic.add("عادي");
        autamatic.add("اتامتيك");
        ArrayAdapter adapter3 = new ArrayAdapter(getActivity().getBaseContext(), R.layout.simple_spinner_item, autamatic);
        adapter3.setDropDownViewResource(R.layout.simple_spinner_item);
        autamatic_spinner.setAdapter(adapter3);
        autamatic_spinner.setOnItemSelectedListener(this);


        //use_type_spiner
        use_type_spinner = (Spinner) view.findViewById(R.id.use_type_spiner);
        use_type.add("نوع الاستخدام ");
        use_type.add("خليجي");
        use_type.add("سعودي");
        use_type.add("امريكي");
        ArrayAdapter adapter2 = new ArrayAdapter(getActivity().getBaseContext(), R.layout.simple_spinner_item, use_type);
        adapter2.setDropDownViewResource(R.layout.simple_spinner_item);
        use_type_spinner.setAdapter(adapter2);
        use_type_spinner.setOnItemSelectedListener(this);

        //com_spinner
        com_spinner = (Spinner) view.findViewById(R.id.com_spiner);
        com.add("الشركة");
        adapter4 = new ArrayAdapter(getActivity().getBaseContext(), R.layout.simple_spinner_item, com);
        adapter4.setDropDownViewResource(R.layout.simple_spinner_item);
        com_spinner.setAdapter(adapter4);
        com_spinner.setOnItemSelectedListener(this);

        //model spinner
        model_spinner = (Spinner) view.findViewById(R.id.model_spiner);
        model.add("الموديل");
        adapter5 = new ArrayAdapter(getActivity().getBaseContext(), R.layout.simple_spinner_item, model);
        adapter5.setDropDownViewResource(R.layout.simple_spinner_item);
        model_spinner.setAdapter(adapter5);
        model_spinner.setOnItemSelectedListener(this);


        //status spinner
        status_spinner = (Spinner) view.findViewById(R.id.car_status_spiner);
        status.add("حالة المركبه");
        status.add("ممتازة - لاتوجد ملاحظات");
        status.add("جيدة جدا - ملاحظات بسيطة لاتعيب المركبة");
        status.add("جيد على الفحص");
        ArrayAdapter adapter6 = new ArrayAdapter(getActivity().getBaseContext(), R.layout.simple_spinner_item, status);
        adapter6.setDropDownViewResource(R.layout.simple_spinner_item);
        status_spinner.setAdapter(adapter6);
        status_spinner.setOnItemSelectedListener(this);

        counter_type_spinner = (Spinner) view.findViewById(R.id.counter_type_spiner);
        counter_type.add("العداد");
        counter_type.add("الكيلومتر");
        counter_type.add("ميل");
        ArrayAdapter adapter7 = new ArrayAdapter(getActivity().getBaseContext(), R.layout.simple_spinner_item, counter_type);
        adapter7.setDropDownViewResource(R.layout.simple_spinner_item);
        counter_type_spinner.setAdapter(adapter7);
        counter_type_spinner.setOnItemSelectedListener(this);


        set_spinner = (Spinner) view.findViewById(R.id.set_spinner);
        sets.add("وضع المركبة");
        sets.add("اقساط - للتنازل");
        sets.add("مستعملة - استعمال حشمة");
        sets.add("جديدة - للبيع معارض");
        ArrayAdapter adapter8 = new ArrayAdapter(getActivity().getBaseContext(), R.layout.simple_spinner_item, sets);
        adapter8.setDropDownViewResource(R.layout.simple_spinner_item);
        set_spinner.setAdapter(adapter8);
        set_spinner.setOnItemSelectedListener(this);

        price_type_spinner = (Spinner) view.findViewById(R.id.price_type_spinner);
        price_type.add("السعر");
        price_type.add("محدد رقماً");
        price_type.add("على السوم");
        ArrayAdapter adapter9 = new ArrayAdapter(getActivity().getBaseContext(), R.layout.simple_spinner_item, price_type);
        adapter9.setDropDownViewResource(R.layout.simple_spinner_item);
        price_type_spinner.setAdapter(adapter9);
        price_type_spinner.setOnItemSelectedListener(this);

         prodauctModel.setGuarantee("لا");
        under_worenty.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b){
                    prodauctModel.setGuarantee("نعم");
                }else prodauctModel.setGuarantee("لا");
            }
        });
        if (extra_model !=null){

            use_type_spinner.setSelection(use_type.indexOf(extra_model.getUsingstatus()));

            status_spinner.setSelection(status.indexOf(extra_model.getVehiclestatus()));

            autamatic_spinner.setSelection(autamatic.indexOf(extra_model.getAutomatic()));

            set_spinner.setSelection(sets.indexOf(extra_model.getVehicleset()));

            counter_type_spinner.setSelection(counter_type.indexOf(extra_model.getOdemetertype()));


            price_type_spinner.setSelection(price_type.indexOf(extra_model.getPricetype()));



            title.setText(extra_model.getTitle());
            Log.e("price",extra_model.getPricevalue());
            price.setText(extra_model.getPricevalue().replace("ريال",""));
            counter.setText(extra_model.getOdemetervaue());
            detailes.setText(extra_model.getDetails());

            if (extra_model.getGuarantee().equals("نعم"))
                under_worenty.setChecked(true);

        }
        return view;

    }

    // city , com , model
    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        switch (adapterView.getId()) {

            case R.id.city_spiner:
                city_txt = adapterView.getSelectedItem().toString();
                break;
            case R.id.com_spiner:
                com_txt = adapterView.getSelectedItem().toString();
                try {
                    int x = Integer.parseInt(com_id.get((com.indexOf(com_txt))));
                    x--;
                    make_filter(String.valueOf(x));
                }catch (Exception e){}
                break;
            case R.id.make_spiner:
                make_txt = String.valueOf(make_id.get(make.indexOf(adapterView.getSelectedItem().toString())));
                Log.e("make",make_txt +"  "+make.indexOf(adapterView.getSelectedItem().toString()) +"  "+ adapterView.getSelectedItem().toString() );
                break;

            case R.id.use_type_spiner:
                use_type_txt = adapterView.getSelectedItem().toString();
                break;
            case R.id.car_status_spiner:
                status_txt = adapterView.getSelectedItem().toString();
                break;
            case R.id.model_spiner:
                model_txt = adapterView.getSelectedItem().toString();
                break;
            case R.id.automatic_spiner:
                autamatic_txt = adapterView.getSelectedItem().toString();
                break;
            case R.id.set_spinner:
                sets_txt = adapterView.getSelectedItem().toString();
                break;
            case R.id.counter_type_spiner:
                counter_type_txt = adapterView.getSelectedItem().toString();
                break;
            case R.id.price_type_spinner:
                price_type_txt = adapterView.getSelectedItem().toString();
                if (price_type_txt.equals(price_type.get(2))){

                    price_hover.setVisibility(View.VISIBLE);
                    price.setText(price_type_txt);
                }else {
                    price_hover.setVisibility(View.GONE);
                    if (price.getText().equals(price_type.get(2)))
                    price.setText("");

                }
                break;

        }
    }


    // Validating Pass
    private boolean validate_ed_mu(EditText editText) {
        String txt = editText.getText().toString().trim();

        if (txt.isEmpty()) {
            editText.setError(getString(R.string.no_data_err));
            requestFocus(editText);
            return false;
        } else {
            editText.setError(null);
        }

        return true;
    }

    private boolean validate_ed(EditText editText) {
        String txt = editText.getText().toString().trim();

        if (txt.isEmpty()) {
            return false;
        }

        return true;
    }

    private void requestFocus(View view) {
        Log.d("requestFocus", view.requestFocus() + "");
        //foucus on view
        if (view.requestFocus()) {

        }
    }

    boolean validate_sp(Spinner spinner, String txt , String default_txt) {
        TextView textView = ((TextView) spinner.getChildAt(0));
        Log.e("txtview" , textView.getText().toString());
        Log.e("txt" , txt);
        Log.e("default_txt" , default_txt);
        if ( txt.trim().isEmpty() || txt.trim().equals(default_txt.trim()) ){
            return false;
        }
        return true;
    }


    boolean validate_sp_mu(Spinner spinner, String txt , String default_txt) {
        TextView textView = ((TextView) spinner.getChildAt(0));
        Log.e("txtview" , textView.getText().toString());
        Log.e("txt" , txt);
        Log.e("default_txt" , default_txt);
        if ( txt.trim().isEmpty() || txt.trim().equals(default_txt.trim()) ){
            textView.setError(getString(R.string.no_ch_err));
            requestFocus(textView);
            Log.e("err" , "true");
            return false;
        } else {
            textView.setError(null);
        }

        return true;
    }

    boolean validate_make(Spinner spinner, String txt , String default_txt) {
        TextView textView = ((TextView) spinner.getChildAt(0));
        Log.e("txtview" , textView.getText().toString());
        Log.e("txt" , txt);
        Log.e("default_txt" , default_txt);
        if (make_for_ad.size() == 1 )
        {
            make_txt = "  ";
            return true ;
        }
        if ((txt.trim().isEmpty() || txt.trim().equals(default_txt.trim())) ){
            textView.setError(getString(R.string.no_ch_err));
            requestFocus(textView);
            Log.e("err" , "true");
            return false;
        } else {
            textView.setError(null);
        }

        return true;
    }



    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    void put_data() {

        if (!validate_sp_mu(city_spinner, city_txt , city.get(0))) return;
        prodauctModel.setAddress(city_txt);

        if (!validate_sp_mu(com_spinner, com_txt , com.get(0) )) return;

       int index= com.indexOf(com_txt) ;
        index--;
        prodauctModel.setType(com_id.get((index)));

        if (!validate_sp(use_type_spinner, use_type_txt, use_type.get(0)))
            prodauctModel.setUsingstatus("");
        else
        prodauctModel.setUsingstatus(use_type_txt);

        if (!validate_sp(status_spinner, status_txt ,  status.get(0)))
            prodauctModel.setVehiclestatus("");
        else
        prodauctModel.setVehiclestatus(status_txt);

        if (!validate_sp_mu(model_spinner, model_txt ,  model.get(0))) return;
        prodauctModel.setYear(model_txt);

        if (!validate_sp(autamatic_spinner, autamatic_txt, autamatic.get(0)))
        prodauctModel.setAutomatic("");
        else
        prodauctModel.setAutomatic(autamatic_txt);

        if (!validate_sp(set_spinner, sets_txt, sets.get(0)))
        prodauctModel.setVehicleset("");
       else
        prodauctModel.setVehicleset(sets_txt);

        if (!validate_sp(counter_type_spinner, counter_type_txt, counter_type.get(0)))
            prodauctModel.setOdemetertype("");
            else
        prodauctModel.setOdemetertype(counter_type_txt);

        if (!validate_sp_mu(price_type_spinner, price_type_txt , price_type.get(0))) return;
        prodauctModel.setPricetype(price_type_txt);

        if (!validate_make(make_spinner, make_txt , make_for_ad.get(0))) return;
        prodauctModel.setMake(make_txt);

        if (!validate_ed_mu(title))
            return;
        prodauctModel.setTitle(title.getText().toString().trim());

    if (!validate_ed(price))
        prodauctModel.setPricevalue("");
        else
        prodauctModel.setPricevalue(price.getText().toString().trim());

     if (!validate_ed(counter))
         prodauctModel.setOdemetervaue("");
         else
        prodauctModel.setOdemetervaue(counter.getText().toString().trim());


     if (!validate_ed(detailes))
         prodauctModel.setDetails("");
        else
        prodauctModel.setDetails(detailes.getText().toString().trim());


        add_adv_listener.baisc_info(prodauctModel);
    }


    void get_data() {


        StringRequest requesturl = new StringRequest(Request.Method.POST, Config.BASE_URL + "spinner_detials.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("get ads", "respone " + response.toString());

                try {
                    JSONObject response_obj = new JSONObject(response);
                    if (response_obj.getString("response").equals("success")) {
                        JSONObject data = response_obj.getJSONObject("data");
                        JSONArray city_obj = data.getJSONArray("city");
                        JSONArray model_obl = data.getJSONArray("model");
                        JSONArray company_obj = data.getJSONArray("company");
                        JSONArray make_obj = data.getJSONArray("make");
                        for (int i = 0; i < city_obj.length(); i++) {
                            JSONObject current = city_obj.getJSONObject(i);
                            city.add(current.getString("city_name"));
                            city_id.add(current.getString("city_id"));
                        }
                            adapter.notifyDataSetChanged();
                        for (int i = 0; i < model_obl.length(); i++) {
                            JSONObject current = model_obl.getJSONObject(i);
                            model.add(current.getString("model_name"));
                            model_id.add(current.getString("model_id"));
                        }
                            adapter5.notifyDataSetChanged();
                        for (int i = 0; i < company_obj.length(); i++) {
                            JSONObject current = company_obj.getJSONObject(i);
                            com.add(current.getString("com_name"));
                            com_id.add(current.getString("com_id"));
                        }
                        adapter4.notifyDataSetChanged();

                        for (int i = 0; i < make_obj.length(); i++) {
                            JSONObject current = make_obj.getJSONObject(i);
                            make.add(current.getString("make_name"));
                            make_id.add(current.getString("make_id"));
                            make_com_id.add(current.getString("com_id"));
                        }
                        adapter_make.notifyDataSetChanged();



                        if (extra_model !=null){
                            city_spinner.setSelection(city.indexOf(extra_model.getAddress()));

/////////////////////////////////////////
                            com_spinner.setSelection(com.indexOf(extra_model.getType()));

                            int index= com.indexOf(extra_model.getType()) ;
                            index--;

                            make_filter(String.valueOf(index));

///////////////////////////////////////////////

                            model_spinner.setSelection(model.indexOf(extra_model.getYear()));

 /////////////////////////////////////////////////



                        }

                    } else {
//                        Toast.makeText(getApplicationContext(),getString(R.string.no_data) , Toast.LENGTH_LONG).show();
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
            }
        }
        );
        int socketTimeout = 5000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout,
                12,
                1);

        requesturl.setRetryPolicy(policy);
        AppController.getInstance().addToRequestQueue(requesturl);


    }

    void make_filter(String com_id){
        make_for_ad.clear();
        adapter_make.clear();
        make_for_ad.add("النوع");
        for (int i = 1; i < make.size()-1; i++) {
            if (make_com_id.get(i).equals(com_id))
            make_for_ad.add(make.get(i));
        }
        adapter_make.notifyDataSetChanged();
        if (extra_model!=null && make_txt.equals(make_for_ad.get(0)))
            make_spinner.setSelection(make_for_ad.indexOf(extra_model.getMake()));


    }
}