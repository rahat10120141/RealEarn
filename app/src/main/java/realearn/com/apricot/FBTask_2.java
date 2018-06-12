package realearn.com.apricot;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.AdSettings;
import com.facebook.ads.AdSize;
import com.facebook.ads.InterstitialAdListener;
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

public class FBTask_2 extends AppCompatActivity {
    // Settings Data
    String imageAddID,videoAddID,appID;
    com.facebook.ads.AdView adView1,adView2,adView3,adView4;
    WebView webView;

    int ad_waiting_time,add_delay,add_per_session,click_per_session,clickReturnTime;
    String[] clickIndexes;
    String[] videoIndexes;
    String[] clickIDIndexes;
    String[] content_urls;

    private FirebaseAnalytics firebaseAnalytics;
    private com.facebook.ads.InterstitialAd interstitialAd,interstitialAd1,interstitialAd2,interstitialAd3,interstitialAd4,interstitialAd5,interstitialAd6;
    private com.facebook.ads.RewardedVideoAd rewardedVideoAd;

    TextView impressionTxt,clicksTxt,messageTxt,messageTxt1,messageTxt2,final_messageTxt,clickViewTxt;
    //TextView impressionTxt;

    private boolean willClick=false;
    private CountDownTimer adDelay,adWaitingTime,clickTimer,adTimer;

    User user;
    AppSettings appSettings;
    UpdateData updateData;


    Button nxtArticle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fbtask);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        webView=(WebView)findViewById(R.id.FBwebView);
        webView.setWebViewClient(new WebViewClient());
        webView.getSettings().setJavaScriptEnabled(true);
        user=new User(getApplicationContext());
        updateData=new UpdateData(getApplicationContext());
        impressionTxt=(TextView)findViewById(R.id.FBTxtImpression);

        // Must Unncomment

        nxtArticle=(Button) findViewById(R.id.nxtArticle);
        nxtArticle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (interstitialAd.isAdLoaded()){
                    interstitialAd.show();
                }else {
                    user.setAdcounter(user.getAdcounter()+1);
                    startActivity(new Intent(getApplicationContext(),FBTask_1.class));
                }
            }
        });

        /*
        clicksTxt=(TextView)findViewById(R.id.FBTxtClickCounter);
        clickViewTxt=(TextView)findViewById(R.id.FBclickView);
        messageTxt=(TextView)findViewById(R.id.FBmessage);
        messageTxt1=(TextView)findViewById(R.id.FBmessage1);
        messageTxt2=(TextView)findViewById(R.id.FBmessage2);
        final_messageTxt=(TextView)findViewById(R.id.FBfinal_message);
        messageTxt.setText("Ad Is requested. Please Wait!!!");
        */
        clickIndexes=user.getClickIndexes().split(",");
        videoIndexes=user.getClickIndexes().split(",");
        clickIDIndexes=user.getImage_ids_for_click().split(",");
        content_urls=user.getContent_urls().split(",");
        if (user.getAdcounter()<=1){
            webView.loadUrl(content_urls[0]);
        }else {
            webView.loadUrl(content_urls[user.getAdcounter()-1]);
        }
        if (user.isBreaktime() || user.getAdcounter()>=user.getAdd_per_session()){
            updateData.UpdateBreak();
            user.setBreaktime(true);
            startActivity(new Intent(getApplicationContext(),UserWelcome.class));
            finish();
        }


        /*
        if (isThisForClick(user.getAdcounter())){
            clickViewTxt.setText("Click Add");
        }else{
            clickViewTxt.setText("View Add");
        }
        */
        //  prepareBanner();
        InitializeSettings();
        firebaseAnalytics=FirebaseAnalytics.getInstance(getApplicationContext());
    }

    private void InitializeSettings(){
        if (isThisForClick(user.getAdcounter())){
            imageAddID=clickIDIndexes[user.getClickCounter()];
        }else {
            imageAddID=user.getImageID();
        }
        videoAddID=user.getVideoID();
        // ad_waiting_time=user.getAd_waiting_time();
        // add_delay=user.getAdd_delay();
        add_per_session=user.getAdd_per_session();
        // click_per_session=user.getClick_per_session();
        // clickReturnTime=user.getClickReturnTime();
        appID=user.getAppID();
        impressionTxt.setText(Integer.toString(user.getAdcounter())+"/"+Integer.toString(add_per_session));
        // clicksTxt.setText(Integer.toString(user.getClickCounter())+"/"+Integer.toString(click_per_session));
        if (user.isPrepared()){
            InitializeAdds();
            prepareVideoAdd();
        }

        // Timers
        /*
        adDelay=new CountDownTimer(add_delay,1000) {
            @Override
            public void onTick(long l) {
                //final_messageTxt.setText("All the ads are loaded, Please wait till it shows!");
            }
            @Override
            public void onFinish() {
                if (interstitialAd.isAdLoaded()){
                    if (!isThisForClick(user.getAdcounter())){
                        interstitialAd2.show();
                    }else{
                        interstitialAd.show();
                    }
                }
            }
        };
        */

    }

    private void StartTask(){
        //Log.i("click","Task Started");
        // adDelay.start();
    }

    private void InitializeAdds(){
        AdSettings.addTestDevice("66289b7f-adb1-4357-b3c8-f6f6336307f5");
        interstitialAd=new com.facebook.ads.InterstitialAd(getApplicationContext(),imageAddID);
        // interstitialAd1=new InterstitialAd(getApplicationContext(),clickIDIndexes[0]);
        // interstitialAd2=new InterstitialAd(getApplicationContext(),clickIDIndexes[1]);
        /*interstitialAd3=new InterstitialAd(getApplicationContext(),"170438096963445_170471170293471");
        interstitialAd3.setAdListener(new InterstitialAdListener() {
            @Override
            public void onInterstitialDisplayed(Ad ad) {

            }

            @Override
            public void onInterstitialDismissed(Ad ad) {

            }

            @Override
            public void onError(Ad ad, AdError adError) {
                Toast.makeText(getApplicationContext(), "Error: " + adError.getErrorMessage(),
                        Toast.LENGTH_LONG).show();
            }

            @Override
            public void onAdLoaded(Ad ad) {
                interstitialAd3.show();
            }

            @Override
            public void onAdClicked(Ad ad) {

            }

            @Override
            public void onLoggingImpression(Ad ad) {

            }
        });
        interstitialAd3.loadAd();*/

        PrepareInterstitialAdd();

        //
    }

    public void PrepareInterstitialAdd(){
        interstitialAd.setAdListener(new InterstitialAdListener() {
            @Override
            public void onInterstitialDisplayed(Ad ad) {

            }

            @Override
            public void onInterstitialDismissed(Ad ad) {
                user.setAdcounter(user.getAdcounter()+1);
                startActivity(new Intent(getApplicationContext(),FBTask_1.class));
                /*
                if (isThisForClick(user.getAdcounter())){
                    messageTxt.setText("You Have To Click This Add, Do not Close It");
                    InitializeAdds();
                    StartTask();
                }
                */
            }

            @Override
            public void onError(Ad ad, AdError adError) {
                //messageTxt.setText(adError.getErrorMessage());
                interstitialAd.loadAd();
            }

            @Override
            public void onAdLoaded(Ad ad) {
                // messageTxt.setText("Main Add Successfully Loaded");
                checkAddCondition();
            }

            @Override
            public void onAdClicked(Ad ad) {

                /*
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
                */
            }

            @Override
            public void onLoggingImpression(Ad ad) {
                /*
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
                        }
                    }.start();
                }
                */
            }
        });
        /*
        interstitialAd1.setAdListener(new InterstitialAdListener() {
            @Override
            public void onInterstitialDisplayed(Ad ad) {
            }

            @Override
            public void onInterstitialDismissed(Ad ad) {

            }

            @Override
            public void onError(Ad ad, AdError adError) {
                messageTxt1.setText(adError.getErrorMessage());
                interstitialAd1.loadAd();
            }

            @Override
            public void onAdLoaded(Ad ad) {
                messageTxt1.setText("Add-1 Successfully Loaded");
                checkAddCondition();
            }

            @Override
            public void onAdClicked(Ad ad) {

            }

            @Override
            public void onLoggingImpression(Ad ad) {
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
            }
        });
        interstitialAd2.setAdListener(new InterstitialAdListener() {
            @Override
            public void onInterstitialDisplayed(Ad ad) {
            }

            @Override
            public void onInterstitialDismissed(Ad ad) {

            }

            @Override
            public void onError(Ad ad, AdError adError) {
                messageTxt2.setText(adError.getErrorMessage());
                interstitialAd2.loadAd();
            }

            @Override
            public void onAdLoaded(Ad ad) {
                messageTxt2.setText("Add-2 Successfully Loaded");
                checkAddCondition();
            }

            @Override
            public void onAdClicked(Ad ad) {

            }

            @Override
            public void onLoggingImpression(Ad ad) {
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
            }
        });
        */
        interstitialAd.loadAd();
        // interstitialAd1.loadAd();
        // interstitialAd2.loadAd();


    }
    private void checkAddCondition(){
/*
        if (!isThisForClick(user.getAdcounter())){
            if (interstitialAd.isAdLoaded() && interstitialAd1.isAdLoaded() && interstitialAd2.isAdLoaded()){
              //  StartTask();
            }else{
             //   final_messageTxt.setText("Please Wait till all the adds are loaded successfully");
            }
        }else {
            StartTask();
        }
        */
    }
    private void prepareBanner(){
        /*
        adView1 = new AdView(this, "170438096963445_170469570293631", AdSize.BANNER_HEIGHT_50);
        LinearLayout adContainer1 = (LinearLayout) findViewById(R.id.FBbanner_1);
        adContainer1.addView(adView1);
        adView1.loadAd();

        adView2 = new AdView(this, "170438096963445_170676000272988", AdSize.BANNER_HEIGHT_50);
        LinearLayout adContainer2 = (LinearLayout) findViewById(R.id.FBbanner_2);
        adContainer2.addView(adView2);
        adView2.loadAd();

        adView3 = new AdView(this, "170438096963445_172660600074528", AdSize.BANNER_HEIGHT_50);
        LinearLayout adContainer3 = (LinearLayout) findViewById(R.id.FBBanner_3);
        adContainer3.addView(adView3);
        adView3.loadAd();

        adView4 = new AdView(this, "170438096963445_172660806741174", AdSize.BANNER_HEIGHT_50);
        LinearLayout adContainer4 = (LinearLayout) findViewById(R.id.FBBanner_4);
        adContainer4.addView(adView4);
        adView4.loadAd();
        */

    }
    public void prepareVideoAdd(){
    }
    private void loadRewardedVideoAd() {
    }
    private boolean isThisForClick(int addcounter){
        List valid = Arrays.asList(clickIndexes);
        //Log.i("click","Click Check is called for: "+Integer.toString(addcounter));
        /*if (valid.contains(Integer.toString(addcounter)) && !user.getuId().equals("1") && !user.getuId().equals("4")) {
            //Log.i("click","Click Check Returning True for: "+Integer.toString(addcounter));
            return true;
        } else if(user.getuId().equals("1") || user.getuId().equals("4")){
            //Log.i("click","Click Check Returning False Exception for: "+Integer.toString(addcounter));
            return false;
        }else {
            //Log.i("click","Click Check Returning False for: "+Integer.toString(addcounter));
            return false;
        }*/
        // For Manik Vai

        if (valid.contains(Integer.toString(addcounter))) {
            return true;
        }else {
            return false;
        }
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
            startActivity(new Intent(getApplicationContext(),FBTask_2.class));
        }
    }

    @Override
    protected void onDestroy() {
        if (adView1 != null || adView2 != null || adView3 != null || adView4 != null) {
            adView1.destroy();adView2.destroy();adView3.destroy();adView4.destroy();
        }
        if (interstitialAd3 != null) {
            interstitialAd3.destroy();
        }
        super.onDestroy();
    }
    @Override
    public void onBackPressed() {
        startActivity(new Intent(getApplicationContext(),UserWelcome.class));
        super.onBackPressed();
    }
}
