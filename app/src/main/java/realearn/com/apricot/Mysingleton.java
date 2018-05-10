package realearn.com.apricot;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Created by LSE on 4/7/2018.
 */

public class Mysingleton {

    private static Mysingleton mInstance;
    private RequestQueue requestQueue;
    private Context mContext;

    private Mysingleton(Context context){
        mContext=context;
        requestQueue=getRequestQueue();
    }

    public RequestQueue getRequestQueue(){
        if (requestQueue==null){
            requestQueue=Volley.newRequestQueue(mContext.getApplicationContext());
        }
        return requestQueue;
    }
    public static synchronized Mysingleton getmInstance(Context context){
        if (mInstance==null){
            mInstance=new Mysingleton(context);
        }
        return mInstance;
    }
    public<T> void AddToRequestQue(Request<T> request){
        requestQueue.add(request);
    }
}
