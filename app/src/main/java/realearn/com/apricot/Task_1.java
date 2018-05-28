package realearn.com.apricot;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.CountDownTimer;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
//import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.analytics.FirebaseAnalytics;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.facebook.ads.*;


public class Task_1 extends AppCompatActivity {
    // Settings Data
    String imageAddID,videoAddID,appID;
    AdView adView1,adView2,adView3,adView4;
    int ad_waiting_time,add_delay,add_per_session,click_per_session,clickReturnTime;
    String[] clickIndexes;
    String[] videoIndexes;
    String[] clickIDIndexes;

    private FirebaseAnalytics firebaseAnalytics;
    //private InterstitialAd interstitialAd,interstitialAd1,interstitialAd2,interstitialAd3,interstitialAd4,interstitialAd5,interstitialAd6;
    private RewardedVideoAd rewardedVideoAd;

    private InterstitialAd interstitialAdfb,interstitialAdfb1,interstitialAdfb2;

    TextView impressionTxt,clicksTxt,messageTxt,messageTxt1,messageTxt2,final_messageTxt,clickViewTxt;
    private boolean willClick=false;
    private CountDownTimer adDelay,adWaitingTime,clickTimer,adTimer;

    User user;
    AppSettings appSettings;
    UpdateData updateData;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_1);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        //Log.i("click","I am alive");
        prepareBanner();
        user=new User(getApplicationContext());
        updateData=new UpdateData(getApplicationContext());
        impressionTxt=(TextView)findViewById(R.id.TxtImpression);
        clicksTxt=(TextView)findViewById(R.id.TxtClickCounter);
        clickViewTxt=(TextView)findViewById(R.id.clickView);
        messageTxt=(TextView)findViewById(R.id.message);
        messageTxt1=(TextView)findViewById(R.id.message1);
        messageTxt2=(TextView)findViewById(R.id.message2);
        final_messageTxt=(TextView)findViewById(R.id.final_message);

        messageTxt.setText("Ad Is requested. Please Wait!!!");
        clickIndexes=user.getClickIndexes().split(",");
        videoIndexes=user.getClickIndexes().split(",");
        clickIDIndexes=user.getImage_ids_for_click().split(",");

        if (user.isBreaktime() || user.getAdcounter()>=user.getAdd_per_session()){
            updateData.UpdateBreak();
            user.setBreaktime(true);
            startActivity(new Intent(getApplicationContext(),UserWelcome.class));
            finish();
        }

        if (isThisForClick(user.getAdcounter())){
            clickViewTxt.setText("Click Add");
        }else{
            clickViewTxt.setText("View Add");
        }
        InitializeSettings();
        InitializeFBAds();
        firebaseAnalytics=FirebaseAnalytics.getInstance(getApplicationContext());
    }

    private void InitializeSettings(){
        if (isThisForClick(user.getAdcounter())){
            imageAddID=clickIDIndexes[user.getClickCounter()];
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
            InitializeAdds();
            prepareVideoAdd();
        }

        // Timers
        adDelay=new CountDownTimer(add_delay,1000) {
            @Override
            public void onTick(long l) {
                final_messageTxt.setText("All the ads are loaded, Please wait till it shows!");
            }
            @Override
            public void onFinish() {
                /*if (interstitialAd.isLoaded()){
                    if (!isThisForClick(user.getAdcounter())){
                        interstitialAd2.show();
                    }else{
                        interstitialAd.show();
                    }
                }*/
            }
        };

    }

    private void StartTask(){
        //Log.i("click","Task Started");
        adDelay.start();
    }

    private void InitializeFBAds(){
        AdSettings.addTestDevice("ab5deb67-3132-4fe2-8b82-43801f691319");
        interstitialAdfb=new InterstitialAd(getApplicationContext(),"170438096963445_170471066960148");
        interstitialAdfb.setAdListener(new InterstitialAdListener() {
            @Override
            public void onInterstitialDisplayed(Ad ad) {

            }

            @Override
            public void onInterstitialDismissed(Ad ad) {

            }

            @Override
            public void onError(Ad ad, AdError adError) {
                messageTxt.setText(adError.getErrorMessage());
                Toast.makeText(getApplicationContext(), "Error: " + adError.getErrorMessage(),
                        Toast.LENGTH_LONG).show();
            }

            @Override
            public void onAdLoaded(Ad ad) {
                interstitialAdfb.show();
            }

            @Override
            public void onAdClicked(Ad ad) {

            }

            @Override
            public void onLoggingImpression(Ad ad) {

            }
        });
        interstitialAdfb.loadAd();

        interstitialAdfb1=new InterstitialAd(getApplicationContext(),"170438096963445_170471140293474");
        interstitialAdfb1.setAdListener(new InterstitialAdListener() {
            @Override
            public void onInterstitialDisplayed(Ad ad) {

            }

            @Override
            public void onInterstitialDismissed(Ad ad) {

            }

            @Override
            public void onError(Ad ad, AdError adError) {
                messageTxt.setText(adError.getErrorMessage());
            }

            @Override
            public void onAdLoaded(Ad ad) {
                interstitialAdfb1.show();
            }

            @Override
            public void onAdClicked(Ad ad) {

            }

            @Override
            public void onLoggingImpression(Ad ad) {

            }
        });
        interstitialAdfb1.loadAd();


        interstitialAdfb2=new InterstitialAd(getApplicationContext(),"170438096963445_170471170293471");
        interstitialAdfb2.setAdListener(new InterstitialAdListener() {
            @Override
            public void onInterstitialDisplayed(Ad ad) {

            }

            @Override
            public void onInterstitialDismissed(Ad ad) {

            }

            @Override
            public void onError(Ad ad, AdError adError) {

            }

            @Override
            public void onAdLoaded(Ad ad) {
                interstitialAdfb2.show();
            }

            @Override
            public void onAdClicked(Ad ad) {

            }

            @Override
            public void onLoggingImpression(Ad ad) {

            }
        });
        interstitialAdfb2.loadAd();


    }
    private void InitializeAdds(){
        //------------------------------- Intertetial Add ------------------------------------------------
        /*
        interstitialAd=new InterstitialAd(getApplicationContext()); // main Add
        interstitialAd.setAdUnitId(imageAddID);

        Log.i("click",clickIDIndexes[0]);
        interstitialAd1=new InterstitialAd(getApplicationContext());
        interstitialAd1.setAdUnitId(clickIDIndexes[0]);

        interstitialAd2=new InterstitialAd(getApplicationContext());
        interstitialAd2.setAdUnitId(clickIDIndexes[1]);
        MobileAds.initialize(getApplicationContext(),appID);
        */

        /*interstitialAd3=new InterstitialAd(Task_1.this);
        interstitialAd3.setAdUnitId(imageAddID);

        interstitialAd4=new InterstitialAd(Task_1.this);
        interstitialAd4.setAdUnitId(imageAddID);

        interstitialAd5=new InterstitialAd(Task_1.this);
        interstitialAd5.setAdUnitId(imageAddID);

        /*interstitialAd6=new InterstitialAd(Task_1.this);
        interstitialAd6.setAdUnitId(imageAddID);*/

        //PrepareInterstitialAdd();

        //
    }
    public void PrepareInterstitialAdd(){

        /*
        interstitialAd.loadAd(new AdRequest.Builder().build());
        interstitialAd.setAdListener(new AdListener(){
            @Override
            public void onAdLoaded() {
                messageTxt.setText("Main Add Successfully Loaded");
                checkAddCondition();
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
                Log.i("Click","Showing Main Add");
                if (!isThisForClick(user.getAdcounter())){
                    adTimer=new CountDownTimer(6000,1000) {
                        @Override
                        public void onTick(long millisUntilFinished) {

                        }

                        @Override
                        public void onFinish() {
                            user.setAdcounter(user.getAdcounter()+1);
                            updateData.ProcessInterstitialAdd(user.getAdcounter(),"view");
                            adDelay.cancel();
                            //adWaitingTime.cancel();
                            CheckUserBreak();
                            adTimer.cancel();
                            Log.i("Click","Main Add Opened And Close");
                        }
                    }.start();
                }
                super.onAdOpened();
            }

            @Override
            public void onAdFailedToLoad(int i) {
                messageTxt.setText("Please Wait While requestng Main Add, Code: "+Integer.toString(i));
                interstitialAd.loadAd(new AdRequest.Builder().build());
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
        interstitialAd1.setAdListener(new AdListener(){
            @Override
            public void onAdOpened() {
                Log.i("Click","Showing Add-1");
                adTimer=new CountDownTimer(5000,1000) {
                    @Override
                    public void onTick(long millisUntilFinished) {

                    }

                    @Override
                    public void onFinish() {
                        interstitialAd.show();
                        adTimer.cancel();
                    }
                }.start();

                super.onAdOpened();
            }

            @Override
            public void onAdLoaded() {
                messageTxt1.setText("Add-1 Successfully Loaded");
                checkAddCondition();
                super.onAdLoaded();
            }

            @Override
            public void onAdFailedToLoad(int i) {
                messageTxt1.setText("Please Wait While requestng Add-1, Code: "+Integer.toString(i));
                interstitialAd1.loadAd(new AdRequest.Builder().build());
                super.onAdFailedToLoad(i);
            }
        });



        interstitialAd2.loadAd(new AdRequest.Builder().build());
        interstitialAd2.setAdListener(new AdListener(){
            @Override
            public void onAdOpened() {
                Log.i("Click","Showing Add-2");
                adTimer=new CountDownTimer(5000,1000) {
                    @Override
                    public void onTick(long millisUntilFinished) {

                    }

                    @Override
                    public void onFinish() {
                        interstitialAd1.show();
                        adTimer.cancel();
                    }
                }.start();
                super.onAdOpened();
            }

            @Override
            public void onAdLoaded() {
                messageTxt2.setText("Add-2 Successfully Loaded");
                checkAddCondition();
                super.onAdLoaded();
            }

            @Override
            public void onAdFailedToLoad(int i) {
                //Log.i("Click",Integer.toString(i));
                messageTxt2.setText("Please Wait While requestng Add-2, Code: "+Integer.toString(i));
                interstitialAd2.loadAd(new AdRequest.Builder().build());
                super.onAdFailedToLoad(i);
            }

        });

        */
    }
    private void checkAddCondition(){
        if (!isThisForClick(user.getAdcounter())){
            /*if (interstitialAd.isLoaded() && interstitialAd1.isLoaded() && interstitialAd2.isLoaded()){
                StartTask();
            }else{
                final_messageTxt.setText("Please Wait till all the adds are loaded successfully");
            }*/
        }else {
            StartTask();
        }
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
        rewardedVideoAd=MobileAds.getRewardedVideoAdInstance(getApplicationContext());
        loadRewardedVideoAd();
        rewardedVideoAd.setRewardedVideoAdListener(new RewardedVideoAdListener() {
            @Override
            public void onRewardedVideoAdLoaded() {

            }

            @Override
            public void onRewardedVideoAdOpened() {
                //Log.i("video","Video Playing");
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
                //Log.i("video","Video Competed");
            }
        });
    }
    private void loadRewardedVideoAd() {
        rewardedVideoAd.loadAd(videoAddID,
                new AdRequest.Builder().build());

    }
    private boolean isThisForClick(int addcounter){
        List valid = Arrays.asList(clickIndexes);
        //Log.i("click","Click Check is called for: "+Integer.toString(addcounter));
        if (valid.contains(Integer.toString(addcounter)) && !user.getuId().equals("1") && !user.getuId().equals("4")) {
            //Log.i("click","Click Check Returning True for: "+Integer.toString(addcounter));
            return true;
        } else if(user.getuId().equals("1") || user.getuId().equals("4")){
            //Log.i("click","Click Check Returning False Exception for: "+Integer.toString(addcounter));
            return false;
        }else {
            //Log.i("click","Click Check Returning False for: "+Integer.toString(addcounter));
            return false;
        }
        // For Manik Vai
       /*
        if (valid.contains(Integer.toString(addcounter))) {
            return true;
        }else {
            return false;
        }*/
    }

    private boolean isItForVideoAdd(int addcounter){
        List valid = Arrays.asList(clickIndexes);
        if (valid.contains(Integer.toString(addcounter))) {
            return true;
        }else {
            return false;
        }
    }
    private void CheckUserBreak(){
        if (user.getAdcounter()>=user.getAdd_per_session()){
            updateData.UpdateBreak();
            user.setBreaktime(true);
            startActivity(new Intent(getApplicationContext(),Task_complete.class));
            finish();
        }else{
            finish();
            startActivity(new Intent(getApplicationContext(),Task_2.class));
        }
    }

    @Override
    protected void onPause() {
        //Log.i("result","backgrounded");
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

}
