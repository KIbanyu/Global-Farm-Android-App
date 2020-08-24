package com.kibzdev.globalfarm.utils;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Created by Riverbank on 29/07/2017.
 */

public class MySingleton {

    private static MySingleton mInstance;
    private RequestQueue requestQueue;
    private static Context ctx;

    private MySingleton(Context context)
    {
        ctx = context;
        requestQueue = getRequestQueue();
    }

    private RequestQueue getRequestQueue()
    {
        if (requestQueue == null)
            requestQueue = Volley.newRequestQueue(ctx.getApplicationContext());
            return requestQueue;
    }
    public static synchronized MySingleton getInstance(Context context)
    {
        if(mInstance ==null)
        {
            mInstance = new MySingleton(context);
        }
        return mInstance;
    }

    public<T> void addRoRequestQue(Request<T> request)
    {
        getRequestQueue().add(request);
    }

}
