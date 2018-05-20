package realearn.com.apricot;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.reward.RewardedVideoAd;

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


        completeTask=(Button) findViewById(R.id.claim_bonus);
        completeTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),UserWelcome.class));
                finish();
            }
        });
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
