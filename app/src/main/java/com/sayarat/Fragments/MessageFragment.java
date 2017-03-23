package com.sayarat.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.sayarat.Adapter.Messages_Adapter;
import com.sayarat.Models.Message_model;
import com.sayarat.Models.UserModel;
import com.sayarat.R;
import com.sayarat.app.AppController;
import com.sayarat.app.Config;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by lenovo on 2/22/2017.
 */

public class MessageFragment extends Fragment {

    RecyclerView recyclerView ;
    public Messages_Adapter adapter;
    ArrayList<Message_model> item_model=new ArrayList<>();
    View layout , layoutmsg;
    TextView textView ;
    View noMessages ;
    String type ;

    public void setType(String type) {
        this.type = type;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(layout==null) {
            layout = inflater.inflate(R.layout.fragment_message, container, false);

            recyclerView = (RecyclerView) layout.findViewById(R.id.msg_view);
            adapter = new Messages_Adapter(getActivity(), item_model);
            recyclerView.setAdapter(adapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

            noMessages = layout.findViewById(R.id.no_mess);
        }
        geMessages();
        return layout;
    }
    void geMessages(){

        StringRequest user_req = new StringRequest(Request.Method.POST, Config.BASE_URL+type+".php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("mess response" , response) ;
                try {
                    layout.findViewById(R.id.no_ads_loading).setVisibility(View.GONE);
                    JSONObject obj = new JSONObject(response);
                    String res =   obj.getString("response");
                    if (res.equals("success") ) {
                        noMessages.setVisibility(View.GONE);
                        JSONArray fedarr = obj.getJSONArray("data") ;
                        for (int i= (0); i<fedarr.length() ; i++){
                            noMessages.setVisibility(View.GONE);
                            JSONObject temp = fedarr.getJSONObject(i) ;
                            JSONObject message = temp.getJSONObject("message");
                            Message_model message_model = new Message_model() ;
                            message_model.setMessage(message.getString("text"));
                            message_model.setDate(message.getString("date"));

                            JSONObject userObj = temp.getJSONObject("user");
                            UserModel user = new UserModel(userObj.getString("ID"),
                                    userObj.getString("Name"),
                                    userObj.getString("Phone"),
                                    userObj.getString("Email"),
                                    userObj.getString("picture")
                            );
                            message_model.setUser_model(user);


                            item_model.add(message_model);
                        }

                        adapter.notifyDataSetChanged();

                    }else {
                        noMessages.setVisibility(View.VISIBLE);
                        layout.findViewById(R.id.no_mess_txt).setVisibility(View.VISIBLE);
                    }
                }catch (Exception e) {
                    Log.e("login response  err" , e.toString()) ;
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("login err" , error.toString()) ;
                    }
                }
        ){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String , String> parmas = new HashMap<>() ;
                parmas.put("user_id" , AppController.getInstance().getPrefManager().getUser().getUser_id()) ;
                Log.e("params" , parmas.toString());
                return parmas;
            }

        };
        int socketTimeout = 5000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout,
                12,
                1);
            user_req.setRetryPolicy(policy);

        AppController.getInstance().getRequestQueue().add(user_req) ;

    }
}
