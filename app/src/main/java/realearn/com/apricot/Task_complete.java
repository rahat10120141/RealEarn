package realearn.com.apricot;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;

public class Task_complete extends AppCompatActivity {

    Button completeTask;
    AdView adView1,adView2,adView3,adView4,adView5;
    private RewardedVideoAd rewardedVideoAd;
    User user;
    UpdateData updateData;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_complete);
        PrepareBanner();

        user=new User(getApplicationContext());
        updateData=new UpdateData(getApplicationContext());

        rewardedVideoAd=MobileAds.getRewardedVideoAdInstance(getApplicationContext());
        rewardedVideoAd.setRewardedVideoAdListener(new RewardedVideoAdListener() {
            @Override
            public void onRewardedVideoAdLoaded() {
                Log.i("rewarded","Loaded");
            }

            @Override
            public void onRewardedVideoAdOpened() {
                Log.i("rewarded","Opened");
            }

            @Override
            public void onRewardedVideoStarted() {
                Log.i("rewarded","Started");
            }

            @Override
            public void onRewardedVideoAdClosed() {
                Log.i("rewarded","Closed");
            }

            @Override
            public void onRewarded(RewardItem rewardItem) {
                updateData.ProcessCompleteTask();
                startActivity(new Intent(getApplicationContext(),UserWelcome.class));
                finish();
            }

            @Override
            public void onRewardedVideoAdLeftApplication() {

            }

            @Override
            public void onRewardedVideoAdFailedToLoad(int i) {
                Log.i("rewarded","Failed To Load Eeeor: "+Integer.toString(i));
            }

            @Override
            public void onRewardedVideoCompleted() {
                Log.i("rewarded","Completed");
            }
        });

        loadRewardedVideo();
        completeTask=(Button) findViewById(R.id.claim_bonus);
        completeTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //startActivity(new Intent(getApplicationContext(),UserWelcome.class));
                //finish();
                if (rewardedVideoAd.isLoaded()){
                    rewardedVideoAd.show();
                }else{
                    loadRewardedVideo();
                }
            }
        });
    }

    private void loadRewardedVideo(){
        if (!rewardedVideoAd.isLoaded()){
            rewardedVideoAd.loadAd(user.getVideoID(),new AdRequest.Builder().build());
        }
    }
    private void PrepareBanner(){
        adView1=(AdView) findViewById(R.id.comp_ad1);
        AdRequest adRequest1=new AdRequest.Builder().build();
        adView1.loadAd(adRequest1);


        adView2=(AdView) findViewById(R.id.comp_ad2);
        AdRequest adRequest2=new AdRequest.Builder().build();
        adView2.loadAd(adRequest2);

        adView3=(AdView) findViewById(R.id.comp_ad3);
        AdRequest adRequest3=new AdRequest.Builder().build();
        adView3.loadAd(adRequest3);

        adView4=(AdView) findViewById(R.id.comp_ad4);
        AdRequest adRequest4=new AdRequest.Builder().build();
        adView4.loadAd(adRequest4);

        adView5=(AdView) findViewById(R.id.comp_ad5);
        AdRequest adRequest5=new AdRequest.Builder().build();
        adView5.loadAd(adRequest5);
    }
}
