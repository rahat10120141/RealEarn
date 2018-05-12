package realearn.com.apricot;

import android.content.Intent;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;
import com.google.firebase.analytics.FirebaseAnalytics;

public class Task_2 extends AppCompatActivity {
    // Settings Data
    String imageAddID,videoAddID,appID;
    AdView adView1,adView2;
    int ad_waiting_time,add_delay,add_per_session,click_per_session;

    private FirebaseAnalytics firebaseAnalytics;
    private InterstitialAd interstitialAd;
    private RewardedVideoAd rewardedVideoAd;

    TextView impressionTxt,clicksTxt,messageTxt,clickViewTxt;
    private boolean willClick=false;
    private CountDownTimer adDelay,adWaitingTime;

    User user;
    AppSettings appSettings;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_1);
        Log.i("result",appSettings.getImageID());
        //InitializeSettings();
        user=new User(Task_2.this);
        firebaseAnalytics=FirebaseAnalytics.getInstance(Task_2.this);
    }

}
