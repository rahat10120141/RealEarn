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
    AdView adView1,adView2;
    int ad_waiting_time,add_delay,add_per_session,click_per_session,clickReturnTime;
    String[] clickIndexes;
    String[] videoIndexes;

    private FirebaseAnalytics firebaseAnalytics;
    private InterstitialAd interstitialAd;
    private RewardedVideoAd rewardedVideoAd;

    TextView impressionTxt,clicksTxt,messageTxt,clickViewTxt;
    private boolean willClick=false;
    private CountDownTimer adDelay,adWaitingTime,clickTimer;

    User user;
    AppSettings appSettings;
    UpdateData updateData;

    AlertDialog.Builder builder;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_1);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        user=new User(Task_2.this);
        updateData=new UpdateData(Task_2.this);

        impressionTxt=(TextView)findViewById(R.id.TxtImpression);
        clicksTxt=(TextView)findViewById(R.id.TxtClickCounter);
        clickViewTxt=(TextView)findViewById(R.id.clickView);
        messageTxt=(TextView)findViewById(R.id.message);
        messageTxt.setText("Task Window 2");
        clickIndexes=user.getClickIndexes().split(",");
        videoIndexes=user.getClickIndexes().split(",");
        isThisForClick(2);
        //Log.i("result","ad_waiting_time:"+user.getAd_waiting_time());
        //Log.i("result","click Indexes:"+user.getClickIndexes());
        //Log.i("result","Video Indexes:"+user.getvideoIndexes());
        //Log.i("result","click_return_time:"+user.getClickReturnTime());

        String[] parts = user.getClickIndexes().split("-");
        if (isThisForClick(user.getAdcounter())){
            clickViewTxt.setText("Click Add");
        }else{
            clickViewTxt.setText("View Add");
        }
        InitializeSettings();
        firebaseAnalytics=FirebaseAnalytics.getInstance(Task_2.this);

        builder=new AlertDialog.Builder(Task_2.this);

    }

    private void InitializeSettings(){
        /*imageAddID=getIntent().getStringExtra("image_add");
        videoAddID=getIntent().getStringExtra("video_add");
        ad_waiting_time=getIntent().getIntExtra("ad_waiting_time",5000);
        add_delay=getIntent().getIntExtra("add_delay",6000);
        add_per_session=getIntent().getIntExtra("add_per_session",12);
        click_per_session=getIntent().getIntExtra("click_per_session",3);
        appID=getIntent().getStringExtra("appID");*/

        imageAddID=user.getImageID();
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
            InitializeAdds();
            prepareBanner();
            PrepareInterstitialAdd();
            prepareVideoAdd();
            MobileAds.initialize(Task_2.this,appID);
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
                if (isItForVideoAdd(user.getAdcounter())){
                    if (rewardedVideoAd.isLoaded()){
                        rewardedVideoAd.show();
                    }else {
                        if (!isThisForClick(user.getAdcounter())){
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
                if (interstitialAd.isLoaded()){
                    if (!isThisForClick(user.getAdcounter())){
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

                }else{
                    builder.setTitle("Wrong!!");
                    builder.setMessage("You have Clicked The Wrong Add");
                    AlertDialog alertDialog=builder.create();
                    alertDialog.show();
                }
                super.onAdLeftApplication();
            }

        });
    }
    private void prepareBanner(){
        adView1=(AdView) findViewById(R.id.task_1_ad1);
        AdRequest adRequest1=new AdRequest.Builder().addTestDevice(AdRequest.DEVICE_ID_EMULATOR).build();
        adView1.loadAd(adRequest1);


        adView2=(AdView) findViewById(R.id.task_1_ad2);
        AdRequest adRequest2=new AdRequest.Builder().addTestDevice(AdRequest.DEVICE_ID_EMULATOR).build();
        adView2.loadAd(adRequest2);
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
        if (valid.contains(Integer.toString(addcounter))) {
            return true;
        } else {
            return false;
        }
    }

    private boolean isItForVideoAdd(int addcounter){
        List valid = Arrays.asList(clickIndexes);
        if (valid.contains(Integer.toString(addcounter))) {
            return true;
        } else {
            return false;
        }
    }
    private void CheckUserBreak(){
        if (user.getAdcounter()==user.getAdd_per_session()){
            user.setBreaktime(true);
            user.setAdcounter(0);
            finish();
            startActivity(new Intent(Task_2.this,UserWelcome.class));
        }else{
            finish();
            startActivity(new Intent(Task_2.this,Task_1.class));
        }
    }
}
