package realearn.com.apricot;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

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
import java.util.logging.Logger;

import realearn.com.apricot.R;

public class UserWelcome extends AppCompatActivity {

    TextView impressionTxt,fraudTxt,earningsTxt;

    Button reportBtn,withdrawBtn,startTaskBtn,facebokBtn,rulesBtn;

    AdView adView1,adView2,adView3;

    User user;
    String uid;

    AppSettings appSettings;

    String balance;

    AlertDialog.Builder builder;
    int verCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_welcome);
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

        Log.i("result",Integer.toString(user.getAdcounter()));


        appSettings=new AppSettings(UserWelcome.this);

        startTaskBtn=(Button) findViewById(R.id.btnStartTask);
        reportBtn=(Button)findViewById(R.id.btnReport);
        withdrawBtn=(Button) findViewById(R.id.btnWithdraw);
        facebokBtn=(Button) findViewById(R.id.btn_facebook);
        rulesBtn=(Button)findViewById(R.id.btn_rules);

        impressionTxt=(TextView)findViewById(R.id.impression);
        fraudTxt=(TextView)findViewById(R.id.fraud);
        earningsTxt=(TextView)findViewById(R.id.balance);

        // Initializing Banner Ads

        adView3 = findViewById(R.id.adView3);
        AdRequest adRequest3 = new AdRequest.Builder().build();
        adView3.loadAd(adRequest3);

        builder=new AlertDialog.Builder(UserWelcome.this);


        //impressionTxt.setText("lo");

        getAccountData(new VolleyCallback() {
            @Override
            public void onSuccess(String result) {
                try {
                    JSONArray jsonArray=new JSONArray(result);
                    JSONObject jsonObject=jsonArray.getJSONObject(0);
                    user.setBreaktime(jsonObject.getString("break_status").toString().equals("1")?true:false);
                    /*if (jsonObject.getString("break_status").equals("1")){
                        user.setBreaktime(true);
                    }else {
                        user.setBreaktime(false);
                    }*/
                    Log.i("breakS","Pref File: "+Boolean.toString(user.isBreaktime()));
                    Log.i("breakS","Json File: "+jsonObject.getString("break_status"));
                    if(verCode<Integer.parseInt(jsonObject.getString("version_code").toString())){
                        final String appPackageName = getPackageName(); // getPackageName() from Context or Activity object
                        try {
                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                        } catch (android.content.ActivityNotFoundException anfe) {
                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                        }
                        System.exit(0);
                    }

                    earningsTxt.setText(jsonObject.getString("balance").toString());
                    impressionTxt.setText(jsonObject.getString("impression").toString());
                    fraudTxt.setText(jsonObject.getString("fraud").toString());
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
                intent.putExtra("total_impression",Integer.parseInt(impressionTxt.getText().toString()));
                if (user.isBreaktime()){
                    builder.setTitle("Break Time");
                    builder.setMessage("You are on break time. Please come after few time");
                    AlertDialog alertDialog=builder.create();
                    alertDialog.show();
                }else {
                    if (user.isPrepared()){
                        // Must Delete Before Upload
                        //user.setClickCounter(0);    //************
                        //user.setAdcounter(0);       //***********
                        finish();
                        startActivity(intent);
                    }else {
                        Toast.makeText(getApplicationContext(),"Wait App Is not Prepared Yet",
                                Toast.LENGTH_LONG).show();
                    }

                }
            }
        });
        reportBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(UserWelcome.this,UserDetail.class));
            }
        });

        withdrawBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(UserWelcome.this,Withdraw.class));
            }
        });
        facebokBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse("https://www.facebook.com/"); // missing 'http://' will cause crashed
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
        finish();
        super.onBackPressed();
    }
}
