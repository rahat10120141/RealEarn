package realearn.com.apricot;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
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
        if (!user.isActiveUser()){
            startActivity(new Intent(getApplicationContext(),UserWelcome.class));
            finish();
        }
        rewardedVideoAd=MobileAds.getRewardedVideoAdInstance(getApplicationContext());
        rewardedVideoAd.setRewardedVideoAdListener(new RewardedVideoAdListener() {
            @Override
            public void onRewardedVideoAdLoaded() {
                Log.i("rahat","Video Add Loaded");
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
                if (user.getClickCounter()<user.getClick_per_session()){
                    startActivity(new Intent(getApplicationContext(),UserWelcome.class));
                    finish();
                }else{
                    Log.i("rahat","user click the full add");
                    updateData.ProcessCompleteTask();
                    startActivity(new Intent(getApplicationContext(),UserWelcome.class));
                    finish();
                }
            }

            @Override
            public void onRewardedVideoAdLeftApplication() {

            }

            @Override
            public void onRewardedVideoAdFailedToLoad(int i) {
                Log.i("rahat","Failed To Load: "+Integer.toString(i));
            }

            @Override
            public void onRewardedVideoCompleted() {
                Log.i("rewarded","Completed");
            }
        });

        //loadRewardedVideo();
        completeTask=(Button) findViewById(R.id.claim_bonus);
        completeTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("rahat","I am clicked");
                //startActivity(new Intent(getApplicationContext(),UserWelcome.class));
                //finish();
                if (rewardedVideoAd.isLoaded()){
                    Log.i("rahat","Rewarded Loaded");
                    rewardedVideoAd.show();
                }else{
                    if (user.getClickCounter()<user.getClick_per_session()){
                        startActivity(new Intent(getApplicationContext(),UserWelcome.class));
                        finish();
                    }else{
                        updateData.ProcessCompleteTask();
                        startActivity(new Intent(getApplicationContext(),UserWelcome.class));
                        finish();
                    }

                    //loadRewardedVideo();
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
        adView1.setAdListener(new AdListener(){
            @Override
            public void onAdLeftApplication() {
                UpdateFraudStatus();
                super.onAdLeftApplication();
            }
        });
        AdRequest adRequest1=new AdRequest.Builder().build();
        adView1.loadAd(adRequest1);


        adView2=(AdView) findViewById(R.id.comp_ad2);
        adView2.setAdListener(new AdListener(){
            @Override
            public void onAdLeftApplication() {
                UpdateFraudStatus();
                super.onAdLeftApplication();
            }
        });
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
