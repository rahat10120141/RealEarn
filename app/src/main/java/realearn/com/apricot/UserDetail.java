package realearn.com.apricot;

import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import realearn.com.apricot.R;

public class UserDetail extends AppCompatActivity {

    TextView userID,userName,userMobile,userMother,myReferID;
    StringRequest stringRequest;
    User user;
    String uid;
    AdView adView1,adView2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_detail);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        user=new User(UserDetail.this);
        uid=user.getuId();
        userID=(TextView)findViewById(R.id.txtUserId);
        userName=(TextView) findViewById(R.id.txtName);
        userMobile=(TextView)findViewById(R.id.txtMobile);
        userMother=(TextView)findViewById(R.id.txtMother);
        myReferID=(TextView)findViewById(R.id.txtMyRefer);
        prepareAdd();
        setUserInfo();
        //Log.d("UserID",userData.get("uid").toString());
    }

    public void setUserInfo(){
        stringRequest=new StringRequest(Request.Method.POST,Appurls.fetch_user_date, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray=new JSONArray(response);
                    JSONObject jsonObject=jsonArray.getJSONObject(0);
                    userID.setText(jsonObject.getString("uid").toString());
                    userName.setText(jsonObject.getString("name"));
                    userMobile.setText(jsonObject.getString("mobile"));
                    userMother.setText(jsonObject.getString("mother_id"));
                    myReferID.setText(jsonObject.getString("mobile"));
                }catch (JSONException e){
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params=new HashMap<String, String>();
                params.put("uid",uid);
                return params;
            }
        };
        Mysingleton.getmInstance(UserDetail.this).AddToRequestQue(stringRequest);
    }
    void prepareAdd(){
        adView1 = findViewById(R.id.detailAdd1);
        AdRequest adRequest1 = new AdRequest.Builder().build();
        adView1.loadAd(adRequest1);

        adView2 = findViewById(R.id.detailAdd2);
        AdRequest adRequest2 = new AdRequest.Builder().build();
        adView1.loadAd(adRequest2);
    }
}
