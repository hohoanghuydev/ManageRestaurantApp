package com.example.managerestaurantapp.services;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class volley {
    private static volley mInstance;
    private RequestQueue requestQueue;
    private static Context mcontext;
    private volley(Context context){
        mcontext = context;
        requestQueue = getRequestqueue();
    }

    private RequestQueue getRequestqueue() {
        if(requestQueue == null)
            requestQueue = Volley.newRequestQueue(mcontext.getApplicationContext());
        return requestQueue;
    }
    public static synchronized volley getInstance(Context context){
        if(mInstance == null){
            mInstance = new volley(context);
        }
        return  mInstance;
    }
    public<T> void addToRequestQue(Request<T> request){
        getRequestqueue().add(request);
    }


}
