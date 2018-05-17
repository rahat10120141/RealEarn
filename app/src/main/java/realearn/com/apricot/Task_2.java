package realearn.com.apricot;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.CountDownTimer;
import android.support.v7.app.AlertDialog;
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

import java.util.Arrays;
import java.util.List;

public class Task_2 extends AppCompatActivity {
    // Settings Data
    String imageAddID,videoAddID,appID;
    AdView adView1,adView2,adView3,adView4;
    int ad_waiting_time,add_delay,add_per_session,click_per_session,clickReturnTime;
    String[] clickIndexes;
    String[] videoIndexes;
    String[] clickIDIndexes;

    private FirebaseAnalytics firebaseAnalytics;
    private InterstitialAd interstitialAd,interstitialAd1,interstitialAd2,interstitialAd3,interstitialAd4,interstitialAd5,interstitialAd6;
    private RewardedVideoAd rewardedVideoAd;

    TextView impressionTxt,clicksTxt,messageTxt,clickViewTxt;
    private boolean willClick=false;
    private CountDownTimer adDelay,adWaitingTime,clickTimer;

    User user;
    AppSettings appSettings;
    UpdateData updateData;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_1);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        prepareBanner();
        user=new User(Task_2.this);
        updateData=new UpdateData(Task_2.this);
        impressionTxt=(TextView)findViewById(R.id.TxtImpression);
        clicksTxt=(TextView)findViewById(R.id.TxtClickCounter);
        clickViewTxt=(TextView)findViewById(R.id.clickView);
        messageTxt=(TextView)findViewById(R.id.message);
        messageTxt.setText("Ad Is requested. Please Wait!!!");
        clickIndexes=user.getClickIndexes().split(",");
        videoIndexes=user.getClickIndexes().split(",");
        clickIDIndexes=user.getImage_ids_for_click().split(",");
        if (isThisForClick(user.getAdcounter())){
            clickViewTxt.setText("Click Add");
        }else{
            clickViewTxt.setText("View Add");
        }
        InitializeSettings();
        firebaseAnalytics=FirebaseAnalytics.getInstance(Task_2.this);
    }

    private void InitializeSettings(){
        if (isThisForClick(user.getAdcounter())){
            imageAddID=clickIDIndexes[user.getClickCounter()];
            Log.i("click",imageAddID);
        }else {
            imageAddID=user.getImageID();
        }
        videoAddID=user.getVideoID();
        ad_waiting_time=user.getAd_waiting_time();
        add_delay=user.getAdd_delay();
        add_per_session=user.getAdd_per_session();
        click_per_session=user.getClick_per_session();
        clickReturnTime=user.getClickReturnTime();
        appID=user.getAppID();
        impressionTxt.setText(Integer.toString(user.getAdcounter())+"/"+Integer.toString(add_per_session));
        clicksTxt.setText(Integer.toString(user.getClickCounter())+"/"+Integer.toString(click_per_session));
        if (user.isPrepared()){
            //MobileAds.initialize(Task_1.this,appID);
            InitializeAdds();
            prepareBanner();
            PrepareInterstitialAdd();
            //prepareVideoAdd();
            StartTask();
        }
    }

    private void StartTask(){
        adDelay=new CountDownTimer(add_delay,1000) {
            @Override
            public void onTick(long l) {
                if (interstitialAd.isLoaded()){
                    messageTxt.setText("Add Is Loaded Please Wait till it shows on the Screen");
                }
            }
            @Override
            public void onFinish() {
                if (interstitialAd.isLoaded()){
                    if (!isThisForClick(user.getAdcounter())){
                        if (interstitialAd1.isLoaded()){
                            //Log.i("result2","In 1 Loaded");
                            interstitialAd1.show();
                        }
                        if (interstitialAd2.isLoaded()){
                            //Log.i("result2","In 2 Loaded");
                            interstitialAd2.show();
                        }
                        if (interstitialAd3.isLoaded()){
                            //Log.i("result2","In 3 Loaded");
                            interstitialAd3.show();
                        }
                        if (interstitialAd4.isLoaded()){
                            //Log.i("result2","In 4 Loaded");
                            interstitialAd4.show();
                        }
                        if (interstitialAd5.isLoaded()){
                            //Log.i("result2","In 5 Loaded");
                            interstitialAd5.show();
                        }
                        if (interstitialAd6.isLoaded()){
                            //Log.i("result2","In 6 Loaded");
                            interstitialAd6.show();
                        }
                        interstitialAd.show();
                        adWaitingTime=new CountDownTimer(ad_waiting_time,1000) {
                            @Override
                            public void onTick(long l) {

                            }

                            @Override
                            public void onFinish() {
                                user.setAdcounter(user.getAdcounter()+1);
                                updateData.ProcessInterstitialAdd(user.getAdcounter(),"view");
                                CheckUserBreak();
                            }
                        }.start();
                    }else{
                        interstitialAd.show();
                    }
                }
            }
        }.start();
    }

    private void InitializeAdds(){
        //------------------------------- Intertetial Add ------------------------------------------------
        interstitialAd=new InterstitialAd(Task_2.this);
        interstitialAd.setAdUnitId(imageAddID);

        interstitialAd1=new InterstitialAd(Task_2.this);
        interstitialAd1.setAdUnitId(imageAddID);

        interstitialAd2=new InterstitialAd(Task_2.this);
        interstitialAd2.setAdUnitId(imageAddID);

        interstitialAd3=new InterstitialAd(Task_2.this);
        interstitialAd3.setAdUnitId(imageAddID);

        interstitialAd4=new InterstitialAd(Task_2.this);
        interstitialAd4.setAdUnitId(imageAddID);

        interstitialAd5=new InterstitialAd(Task_2.this);
        interstitialAd5.setAdUnitId(imageAddID);

        interstitialAd6=new InterstitialAd(Task_2.this);
        interstitialAd6.setAdUnitId(imageAddID);

        //
    }
    public void PrepareInterstitialAdd(){
        interstitialAd.loadAd(new AdRequest.Builder().build());
        interstitialAd.setAdListener(new AdListener(){
            @Override
            public void onAdLoaded() {
                Log.i("result","Add Loaded");
                super.onAdLoaded();
            }

            @Override
            public void onAdClosed() {
                if (isThisForClick(user.getAdcounter())){
                    messageTxt.setText("You Have To Click This Add, Do not Close It");
                    InitializeAdds();
                    StartTask();
                }

                super.onAdClosed();
            }

            @Override
            public void onAdOpened() {

                super.onAdOpened();
            }

            @Override
            public void onAdFailedToLoad(int i) {
                messageTxt.setText("Add Failed To load, Please Go Back to Main Window and then Come Back");
                InitializeAdds();
                super.onAdFailedToLoad(i);
            }

            @Override
            public void onAdLeftApplication() {
                if (isThisForClick(user.getAdcounter())){
                    clickTimer=new CountDownTimer(clickReturnTime,1000) {
                        @Override
                        public void onTick(long l) {

                        }

                        @Override
                        public void onFinish() {
                            user.setAdcounter(user.getAdcounter()+1);
                            user.setClickCounter(user.getClickCounter()+1);
                            updateData.ProcessInterstitialAdd(user.getAdcounter(),"click");
                            CheckUserBreak();
                        }
                    }.start();

                }else {
                    user.setFraud(user.getFraud()+1);
                    updateData.UpdateFraud();
                }
                super.onAdLeftApplication();
            }

        });

        interstitialAd1.loadAd(new AdRequest.Builder().build());
        interstitialAd2.loadAd(new AdRequest.Builder().build());
        interstitialAd3.loadAd(new AdRequest.Builder().build());
        interstitialAd4.loadAd(new AdRequest.Builder().build());
        interstitialAd5.loadAd(new AdRequest.Builder().build());
        interstitialAd6.loadAd(new AdRequest.Builder().build());

    }
    private void prepareBanner(){
        adView1=(AdView) findViewById(R.id.task_1_ad1);
        AdRequest adRequest1=new AdRequest.Builder().build();
        adView1.loadAd(adRequest1);


        adView2=(AdView) findViewById(R.id.task_1_ad2);
        AdRequest adRequest2=new AdRequest.Builder().build();
        adView2.loadAd(adRequest2);

        adView3=(AdView) findViewById(R.id.task_1_ad3);
        AdRequest adRequest3=new AdRequest.Builder().build();
        adView3.loadAd(adRequest3);

        adView4=(AdView) findViewById(R.id.task_1_ad4);
        AdRequest adRequest4=new AdRequest.Builder().build();
        adView4.loadAd(adRequest4);
    }
    public void prepareVideoAdd(){
        rewardedVideoAd=MobileAds.getRewardedVideoAdInstance(Task_2.this);
        loadRewardedVideoAd();
        rewardedVideoAd.setRewardedVideoAdListener(new RewardedVideoAdListener() {
            @Override
            public void onRewardedVideoAdLoaded() {

            }

            @Override
            public void onRewardedVideoAdOpened() {
                Log.i("video","Video Playing");
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
                Log.i("video","Video Competed");
            }
        });
    }
    private void loadRewardedVideoAd() {
        rewardedVideoAd.loadAd(videoAddID,
                new AdRequest.Builder().build());

    }
    private boolean isThisForClick(int addcounter){
        List valid = Arrays.asList(clickIndexes);
         if(valid.contains(Integer.toString(addcounter)) && !user.getuId().equals("1") && !user.getuId().equals("4")) {
            return true;
        } else if(user.getuId().equals("1") || user.getuId().equals("4")){
            return false;
        }else {
            return false;
        }
        // For Manik Vai
        /*if (valid.contains(Integer.toString(addcounter))) {
            return true;
        }else {
            return false;
        }*/
    }

    private boolean isItForVideoAdd(int addcounter){
        List valid = Arrays.asList(videoIndexes);
        if (valid.contains(Integer.toString(addcounter))) {
            return true;
        } else {
            return false;
        }
    }
    private void CheckUserBreak(){
        if (user.getAdcounter()>=user.getAdd_per_session()){
            updateData.UpdateBreak();
            user.setBreaktime(true);
            finish();
            startActivity(new Intent(Task_2.this,UserWelcome.class));
        }else{
            finish();
            startActivity(new Intent(Task_2.this,Task_1.class));
        }
    }
    @Override
    protected void onPause() {
        Log.i("result","backgrounded");
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        //interstitialAd=null;
        super.onDestroy();
    }
}
