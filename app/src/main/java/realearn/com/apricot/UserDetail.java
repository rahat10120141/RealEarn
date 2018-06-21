package realearn.com.apricot;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import realearn.com.apricot.R;

public class UserDetail extends AppCompatActivity {

    TextView userID,userName,userMobile,userMother,myReferID,txtMyFraud;
    ImageView userImage;
    String imageUrl;
    StringRequest stringRequest;
    User user;
    UpdateData updateData;
    String uid;
    AdView adView1,adView2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_detail);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        user=new User(UserDetail.this);
        updateData=new UpdateData(getApplicationContext());
        uid=user.getuId();
        //userID=(TextView)findViewById(R.id.txtUserId);
        userImage=(ImageView)findViewById(R.id.img);
        userName=(TextView) findViewById(R.id.txtName);
        userMobile=(TextView)findViewById(R.id.txtMobile);
        userMother=(TextView)findViewById(R.id.txtMother);
        myReferID=(TextView)findViewById(R.id.txtMyRefer);
        txtMyFraud=(TextView)findViewById(R.id.txtMyFraud);
        userImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("rahat","Image is clicked");
            }
        });
        prepareAdd();
        setUserInfo(new VolleyCallback() {
            @Override
            public void onSuccess(String result) {
                JSONArray jsonArray= null;
                try {
                    jsonArray = new JSONArray(result);
                    JSONObject jsonObject=jsonArray.getJSONObject(0);
                    userName.setText(jsonObject.getString("name"));
                    userMobile.setText(jsonObject.getString("mobile"));
                    userMother.setText(jsonObject.getString("mother_id"));
                    myReferID.setText(jsonObject.getString("mobile"));
                    imageUrl=jsonObject.getString("image");
                    txtMyFraud.setText(jsonObject.getString("my_fraud"));
                    SetImage();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
        //Log.d("UserID",userData.get("uid").toString());
    }
    public interface VolleyCallback{
        void onSuccess(String result);
    }
    public void setUserInfo(final VolleyCallback callback){
        stringRequest=new StringRequest(Request.Method.POST,Appurls.fetch_user_date, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                callback.onSuccess(response);
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
        adView1.setAdListener(new AdListener(){
            @Override
            public void onAdLeftApplication() {
                UpdateFraudStatus();
                super.onAdLeftApplication();
            }
        });
        AdRequest adRequest1 = new AdRequest.Builder().build();
        adView1.loadAd(adRequest1);

        adView2 = findViewById(R.id.detailAdd2);
        adView2.setAdListener(new AdListener(){
            @Override
            public void onAdLeftApplication() {
                UpdateFraudStatus();
                super.onAdLeftApplication();
            }
        });
        AdRequest adRequest2 = new AdRequest.Builder().build();
        adView1.loadAd(adRequest2);
    }
    private void SetImage(){
        ImageRequest imageRequest=new ImageRequest(Appurls.image_url+imageUrl, new Response.Listener<Bitmap>() {
            @Override
            public void onResponse(Bitmap response) {
                RoundedBitmapDrawable roundedBitmapDrawable= RoundedBitmapDrawableFactory.create(getResources(),response);
                roundedBitmapDrawable.setCircular(true);
                userImage.setImageDrawable(roundedBitmapDrawable);
                //userImage.setImageBitmap(response);
            }
        }, 0, 0, ImageView.ScaleType.CENTER_CROP, null, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("rahat","i got error");
                Log.i("rahat",error.getMessage());
            }
        });
        Mysingleton.getmInstance(UserDetail.this).AddToRequestQue(imageRequest);
    }
    private void CheckFraudStatus(){
        if (user.getDailyFraud()>user.getMaximumFraudPerDay()){
            user.setActiveUser(false);
            updateData.BlockMyself();
            user.setFraud(0);
            Toast.makeText(getApplicationContext(),"You are blocked for Clicking Too Many Ads",
                    Toast.LENGTH_LONG).show();
            startActivity(new Intent(getApplicationContext(),UserWelcome.class));
            finish();
        }else{
            if (user.getFraud()>user.getMaximumFraudPerSession()){
                updateData.UpdateBreak();
                user.setBreaktime(true);
                user.setFraud(0);
                Toast.makeText(getApplicationContext(),"You have Clicked Too Many Ads",
                        Toast.LENGTH_LONG).show();
                startActivity(new Intent(getApplicationContext(),UserWelcome.class));
                finish();
            }
        }
    }
    private void UpdateFraudStatus(){
        user.setFraud(user.getFraud()+1);
        user.setDailyFraud(user.getDailyFraud()+1);
        updateData.UpdateFraud();
        CheckFraudStatus();
    }
    @Override
    public void onBackPressed() {
        startActivity(new Intent(getApplicationContext(),UserWelcome.class));
        super.onBackPressed();
    }
}
