package realearn.com.apricot;

import android.content.Context;

import com.android.volley.toolbox.StringRequest;

/**
 * Created by LSE on 4/9/2018.
 */

public class FetchData {

    StringRequest stringRequest;
    String workData;
    String uid;
    User user;
    Context context;

    public FetchData(Context context){
        this.context=context;
        user=new User(context);
        uid=user.getuId();
    }
}
