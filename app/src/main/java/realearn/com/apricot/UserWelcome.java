package realearn.com.apricot;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.tasks.Task;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import realearn.com.apricot.R;

public class UserWelcome extends AppCompatActivity {

    //TextView impressionTxt,fraudTxt,earningsTxt;

    TextView txtTaskIncome,txtRefIncome,txtOtherIncome,txtWithdraw,txtRemain;
    Button reportBtn,withdrawBtn,startTaskBtn,facebokBtn,rulesBtn;

    AdView adView1,adView2,adView3,adView4;

    User user;
    String uid;

    AppSettings appSettings;

    String balance;

    AlertDialog.Builder builder;
    int verCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_user_welcome);
        setContentView(R.layout.user_welcome);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        Toast.makeText(getApplicationContext(), "Please Join Our Facebook Group if you have not joined yet",
                Toast.LENGTH_LONG).show();
        try {
            PackageInfo pInfo = this.getPackageManager().getPackageInfo(getPackageName(), 0);
            String version = pInfo.versionName;
            verCode = pInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        user=new User(UserWelcome.this);
        uid=user.getuId();
        appSettings=new AppSettings(UserWelcome.this);

        startTaskBtn=(Button) findViewById(R.id.btnNewStartTask);
        reportBtn=(Button)findViewById(R.id.btnNewUserInfo);
        withdrawBtn=(Button) findViewById(R.id.btnWithdrawNew);
        facebokBtn=(Button) findViewById(R.id.btnFacebookNew);
        rulesBtn=(Button)findViewById(R.id.btnRulesNew);


        txtTaskIncome=(TextView) findViewById(R.id.txtTaskIncome);
        txtRefIncome=(TextView) findViewById(R.id.txtRefIncome);
        txtOtherIncome=(TextView)findViewById(R.id.txtOtherIncome);
        txtWithdraw=(TextView)findViewById(R.id.txtWithdraw);
        txtRemain=(TextView)findViewById(R.id.txtRemainings);
        builder=new AlertDialog.Builder(UserWelcome.this);
        Log.i("rahat","Mobile : "+user.getMobile());
        Log.i("rahat","I am loaded");

        //impressionTxt.setText("lo");

        getAccountData(new VolleyCallback() {
            @Override
            public void onSuccess(String result) {
                try {
                    //Log.i("result",result);
                    JSONArray jsonArray=new JSONArray(result);
                    JSONObject jsonObject=jsonArray.getJSONObject(0);
                    user.setBreaktime(jsonObject.getString("break_status").toString().equals("1")?true:false);
                    user.setActiveUser(jsonObject.getString("user_status").toString().equals("1")?true:false);
                    if (user.isBreaktime()){
                        user.setAdcounter(0);
                        user.setClickCounter(0);
                    }
                    if(verCode<Integer.parseInt(jsonObject.getString("version_code").toString())){
                        final String appPackageName = getPackageName(); // getPackageName() from Context or Activity object
                        try {
                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                        } catch (android.content.ActivityNotFoundException anfe) {
                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                        }
                        System.exit(0);
                    }
                    txtOtherIncome.setText(jsonObject.getString("other_income").toString());
                    txtTaskIncome.setText(jsonObject.getString("task_income").toString());
                    txtRefIncome.setText(jsonObject.getString("referral_income").toString());
                    txtWithdraw.setText(jsonObject.getString("total_withdraw").toString());
                    txtRemain.setText(jsonObject.getString("balance").toString());
                    user.setDailyFraud(Integer.parseInt(jsonObject.getString("daily_fraud")));
                    user.setMobile(jsonObject.getString("mobile").toString());

                    //earningsTxt.setText(jsonObject.getString("balance").toString());
                    //impressionTxt.setText(jsonObject.getString("impression").toString());
                    //fraudTxt.setText(jsonObject.getString("clicks").toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        // Button Events
        startTaskBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(UserWelcome.this,Task_1.class);
                //intent.putExtra("total_impression",Integer.parseInt(impressionTxt.getText().toString()));
                if (user.isBreaktime()){
                    builder.setTitle("Break Time");
                    builder.setMessage("You are on break time. Please come after few time");
                    AlertDialog alertDialog=builder.create();
                    alertDialog.show();
                }else {
                    if (user.isPrepared() && user.isSettingLoaded()){
                        if (user.isActiveUser()){
                            if (!user.Remember_counters()){
                                user.setAdcounter(0);
                                user.setClickCounter(0);
                            }
                            startActivity(intent);
                            finish();
                        }else{
                            Toast.makeText(getApplicationContext(),"You are Blocked!! Please Contact Admin",
                                    Toast.LENGTH_LONG).show();
                        }
                    }else {
                        Toast.makeText(getApplicationContext(),"Wait App Is not Prepared Yet. Please Check Your Internet Connection",
                                Toast.LENGTH_LONG).show();
                    }

                }
            }
        });
        reportBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(user.isPrepared() && user.isSettingLoaded()){
                    /*
                    Log.i("rahat", "Click Index: " + user.getClickIndexes());
                    Log.i("rahat", "Video Index: " + user.getvideoIndexes());
                    Log.i("rahat", "Content Urls: " + user.getContent_urls());
                    Log.i("rahat", "click Return Time: " + Integer.toString(user.getClickReturnTime()));
                    Log.i("rahat", "App Id: " + user.getAppID());
                    Log.i("rahat", "Counter Delay: " + Integer.toString(user.getCounter_delay()));
                    Log.i("rahat", "Ad Delay: " + Integer.toString(user.getAdd_delay()));
                    Log.i("rahat", "Ad Waiting Time: " + Integer.toString(user.getAd_waiting_time()));
                    Log.i("rahat", "Click Per Session: " + Integer.toString(user.getClick_per_session()));
                    Log.i("rahat", "Ad Per Session: " + Integer.toString(user.getAdd_per_session()));
                    Log.i("rahat", "Is Prepared: " + Boolean.toString(user.isPrepared()));
                    Log.i("rahat", "Video ID: " + user.getVideoID());
                    Log.i("rahat", "Image Ids For Click: " + user.getImage_ids_for_click());
                    Log.i("rahat", "Image Id: " + user.getImageID());
                    Log.i("rahat", "Mobile: " + user.getMobile());
                    Log.i("rahat", "User ID: " + user.getuId());
                    Log.i("rahat", "Adcounter: " + Integer.toString(user.getAdcounter()));
                    Log.i("rahat", "Click Counter: " + Integer.toString(user.getClickCounter()));
                    Log.i("rahat", "Is Break Time: " + Boolean.toString(user.isBreaktime()));
                    Log.i("rahat", "Auto Task: " + Boolean.toString(user.isAutoTask()));
                    Log.i("rahat", "Facebook Ad: " + Boolean.toString(user.isFacebook_ad()));
                    Log.i("rahat", "Remember Counters: " + Boolean.toString(user.Remember_counters()));
                    Log.i("rahat", "Showing Log: " + Boolean.toString(user.isShowingLog()));
                    Log.i("rahat", "Prevent Phone From Sleep: " + Boolean.toString(user.isPrevent_phone_from_sleep()));
                    Log.i("rahat", "Wait For Action: " + Boolean.toString(user.isWait_for_action()));
                    Log.i("rahat", "VPN Allowed: " + Boolean.toString(user.isVpnAllowed()));
                    Log.i("rahat", "Break  Allowed: " + Boolean.toString(user.isBreakAllowed()));
                    Log.i("rahat", "Fraud Per Session: " + Integer.toString(user.getMaximumFraudPerSession()));
                    Log.i("rahat", "Fraud Per Day: " + Integer.toString(user.getMaximumFraudPerDay()));
                    */
                    startActivity(new Intent(getApplicationContext(),UserDetail.class));
                }else {

                }

                /*
                if (user.isPrepared() && user.isSettingLoaded()){
                    if (user.isActiveUser()){
                        startActivity(new Intent(UserWelcome.this,UserDetail.class));
                    }else{
                        Toast.makeText(getApplicationContext(),"You are Blocked!! Please Contact Admin",
                                Toast.LENGTH_LONG).show();
                    }
                }else {
                    Toast.makeText(getApplicationContext(),"Wait App Is not Prepared Yet",
                            Toast.LENGTH_LONG).show();
                }*/
            }
        });

        withdrawBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (user.isPrepared() && user.isSettingLoaded()){
                    if (user.isActiveUser()){
                        startActivity(new Intent(UserWelcome.this,Withdraw.class));
                    }else{
                        Toast.makeText(getApplicationContext(),"You are Blocked!! Please Contact Admin",
                                Toast.LENGTH_LONG).show();
                    }
                }else {
                    Toast.makeText(getApplicationContext(),"Wait App Is not Prepared Yet",
                            Toast.LENGTH_LONG).show();
                }

            }
        });
        facebokBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse("https://www.facebook.com/groups/1549359625184060"); // groups/137668183614933/
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });
        rulesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(UserWelcome.this,AppRules.class));
            }
        });
    }
    public void getAccountData(final VolleyCallback callback){
        StringRequest stringRequest=new StringRequest(Request.Method.POST, Appurls.user_work_data, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                callback.onSuccess(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params=new HashMap<String, String>();
                params.put("uid",uid);
                return params;
            }
        };
        Mysingleton.getmInstance(UserWelcome.this).AddToRequestQue(stringRequest);
    }
    public interface VolleyCallback{
        void onSuccess(String result);
    }

    @Override
    public void onBackPressed() {
        System.exit(0);
        super.onBackPressed();
    }
}
