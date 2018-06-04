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



        // These textviews are depricated

        //impressionTxt=(TextView)findViewById(R.id.impression);
        //fraudTxt=(TextView)findViewById(R.id.fraud);
        //earningsTxt=(TextView)findViewById(R.id.balance);

        // Initializing Banner Ads


        // No Ad will be shown on new user welcome screen
        /*

        adView1 = findViewById(R.id.adView1);
        AdRequest adRequest1 = new AdRequest.Builder().build();
        adView1.loadAd(adRequest1);

        adView2 = findViewById(R.id.adView2);
        AdRequest adRequest2 = new AdRequest.Builder().build();
        adView2.loadAd(adRequest2);

        adView3 = findViewById(R.id.adView3);
        AdRequest adRequest3 = new AdRequest.Builder().build();
        adView3.loadAd(adRequest3);
        */

        builder=new AlertDialog.Builder(UserWelcome.this);


        //impressionTxt.setText("lo");

        getAccountData(new VolleyCallback() {
            @Override
            public void onSuccess(String result) {
                try {
                    Log.i("result",result);
                    JSONArray jsonArray=new JSONArray(result);
                    JSONObject jsonObject=jsonArray.getJSONObject(0);
                    user.setBreaktime(jsonObject.getString("break_status").toString().equals("1")?true:false);
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
                //startActivity(new Intent(UserWelcome.this,Task_complete.class));
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
        finish();
        System.exit(0);
        super.onBackPressed();
    }
}
