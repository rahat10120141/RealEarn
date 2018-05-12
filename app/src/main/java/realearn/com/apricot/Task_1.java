package realearn.com.apricot;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.CountDownTimer;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.analytics.FirebaseAnalytics;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

public class Task_1 extends AppCompatActivity {

    Button bn,bn2;


    // Settings Data
    String imageAddID,videoAddID,appID;
    AdView adView1,adView2,adView3;
    int ad_waiting_time,add_delay,add_per_session,click_per_session;

    private FirebaseAnalytics firebaseAnalytics;
    private InterstitialAd interstitialAd;
    private RewardedVideoAd rewardedVideoAd;

    private CountDownTimer adDelay;

    User user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_1);
        InitializeSettings();
        InitializeAdds();
        PrepareInterstitialAdd();
        prepareVideoAdd();
        prepareBanner();
        bn=(Button)findViewById(R.id.button2);
        bn2=(Button)findViewById(R.id.button3);
        user=new User(Task_1.this);

        firebaseAnalytics=FirebaseAnalytics.getInstance(Task_1.this);
        bn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (interstitialAd.isLoaded()){
                    interstitialAd.show();
                    adDelay=new CountDownTimer(5000,1000) {
                        @Override
                        public void onTick(long millisUntilFinished) {

                        }

                        @Override
                        public void onFinish() {
                            startActivity(new Intent(Task_1.this,Task_2.class));
                        }
                    }.start();
                }
            }
        });
        bn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Task_1.this,Task_2.class));
            }
        });
        MobileAds.initialize(Task_1.this,appID);
        Log.i("result","Video ID : "+videoAddID);
        Log.i("result","Image ID : "+imageAddID);
        Log.i("result","Ad Waiting Time: "+Integer.toString(ad_waiting_time));
        Log.i("result","add_delay: "+Integer.toString(add_delay));
        Log.i("result","add_per_session: "+Integer.toString(add_per_session));
        Log.i("result","click_per_session: "+Integer.toString(click_per_session));
        Log.i("result","appID: "+appID);
        // Setting Initilizing


    }
    private void InitializeSettings(){
        imageAddID=getIntent().getStringExtra("image_add");
        videoAddID=getIntent().getStringExtra("video_add");
        ad_waiting_time=getIntent().getIntExtra("ad_waiting_time",5000);
        add_delay=getIntent().getIntExtra("add_delay",6000);
        add_per_session=getIntent().getIntExtra("add_per_session",12);
        click_per_session=getIntent().getIntExtra("click_per_session",3);
        appID=getIntent().getStringExtra("appID");
    }
    private void InitializeAdds(){
       //------------------------------- Intertetial Add ------------------------------------------------
        interstitialAd=new InterstitialAd(Task_1.this);
        interstitialAd.setAdUnitId(imageAddID);
        PrepareInterstitialAdd();

        //
    }

    public void PrepareInterstitialAdd(){
        interstitialAd.loadAd(new AdRequest.Builder().addTestDevice(AdRequest.DEVICE_ID_EMULATOR).build());
        interstitialAd.setAdListener(new AdListener(){
            @Override
            public void onAdLoaded() {
                Log.i("result","Add Loaded");
                super.onAdLoaded();
            }

            @Override
            public void onAdClosed() {


                super.onAdClosed();
            }

            @Override
            public void onAdOpened() {

                super.onAdOpened();
            }

            @Override
            public void onAdFailedToLoad(int i) {
                super.onAdFailedToLoad(i);
            }

            @Override
            public void onAdLeftApplication() {
                super.onAdLeftApplication();
            }

        });
    }
    private void prepareBanner(){
        /*adView1=(AdView) findViewById(R.id.taskAd1);
        AdRequest adRequest1=new AdRequest.Builder().addTestDevice(AdRequest.DEVICE_ID_EMULATOR).build();
        adView1.loadAd(adRequest1);*/
    }
    public void prepareVideoAdd(){
        rewardedVideoAd=MobileAds.getRewardedVideoAdInstance(Task_1.this);
        loadRewardedVideoAd();
        rewardedVideoAd.setRewardedVideoAdListener(new RewardedVideoAdListener() {
            @Override
            public void onRewardedVideoAdLoaded() {

            }

            @Override
            public void onRewardedVideoAdOpened() {

            }

            @Override
            public void onRewardedVideoStarted() {

            }

            @Override
            public void onRewardedVideoAdClosed() {

            }

            @Override
            public void onRewarded(RewardItem rewardItem) {

            }

            @Override
            public void onRewardedVideoAdLeftApplication() {

            }

            @Override
            public void onRewardedVideoAdFailedToLoad(int i) {

            }

            @Override
            public void onRewardedVideoCompleted() {

            }
        });
    }
    private void loadRewardedVideoAd() {
        rewardedVideoAd.loadAd(videoAddID,
                new AdRequest.Builder().build());

    }

}
