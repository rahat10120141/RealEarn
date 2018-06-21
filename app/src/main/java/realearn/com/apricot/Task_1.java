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
import android.webkit.WebView;
import android.webkit.WebViewClient;
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

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class Task_1 extends AppCompatActivity {
    // Settings Data
    String imageAddID,videoAddID,appID;
    AdView adView1,adView2,adView3,adView4;
    WebView webView;

    int ad_waiting_time,add_delay,add_per_session,click_per_session,clickReturnTime;
    String[] clickIndexes;
    String[] videoIndexes;
    String[] clickIDIndexes;
    String[] content_urls;

    private FirebaseAnalytics firebaseAnalytics;
    private InterstitialAd interstitialAd,interstitialAd1,interstitialAd2,interstitialAd3,interstitialAd4,interstitialAd5,interstitialAd6;
    private RewardedVideoAd rewardedVideoAd;

    TextView impressionTxt,clicksTxt,messageTxt,messageTxt1,messageTxt2,final_messageTxt,clickViewTxt;
    private boolean int_opened=false,int_1_opened=false,int_2_opened=false;
    private CountDownTimer adDelay,adWaitingTime,clickTimer,adTimer;

    User user;
    AppSettings appSettings;
    UpdateData updateData;

    Button nextArticle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_1);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        webView=(WebView)findViewById(R.id.webView);
        webView.setWebViewClient(new WebViewClient());
        webView.getSettings().setJavaScriptEnabled(true);
        nextArticle=(Button)findViewById(R.id.nxtArticleMain);
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

        if (!user.isActiveUser()){
            startActivity(new Intent(getApplicationContext(),UserWelcome.class));
            finish();
        }
        if (user.isShowingLog()){
            messageTxt.setText("Ad Is requested. Please Wait!!!");
        }
        if (user.isPrevent_phone_from_sleep()){
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        }
        clickIndexes=user.getClickIndexes().split(",");
        videoIndexes=user.getClickIndexes().split(",");
        clickIDIndexes=user.getImage_ids_for_click().split(",");
        content_urls=user.getContent_urls().split(",");
        /*for (int i=0;i<content_urls.length;i++){
            Log.i("urls "+Integer.toString(i)+": ",content_urls[i]);
        }*/
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
        if (user.isAutoTask()){
            nextArticle.setEnabled(false);
        }else{
            nextArticle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (isThisForClick(user.getAdcounter())){
                        if (int_opened){
                            user.setAdcounter(user.getAdcounter()+1);
                            updateData.ProcessInterstitialAdd(user.getAdcounter(),"view");
                            CheckUserBreak();
                        }else{
                            startActivity(new Intent(getApplicationContext(),Task_2.class));
                        }
                    }else{
                        if(int_opened && int_1_opened && int_2_opened){
                            user.setAdcounter(user.getAdcounter()+1);
                            updateData.ProcessInterstitialAdd(user.getAdcounter(),"view");
                            CheckUserBreak();
                        }else{
                            startActivity(new Intent(getApplicationContext(),Task_2.class));
                        }
                    }

                }
            });
        }
        if (isThisForClick(user.getAdcounter())){
            clickViewTxt.setText("Get Gift");
        }else{
            clickViewTxt.setText("Wait For Gift");
        }

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
        ad_waiting_time=user.getAd_waiting_time()*1000;
        add_delay=user.getAdd_delay()*1000;
        add_per_session=user.getAdd_per_session();
        click_per_session=user.getClick_per_session();
        clickReturnTime=user.getClickReturnTime()*1000;
        appID=user.getAppID();
        impressionTxt.setText(Integer.toString(user.getAdcounter())+"/"+Integer.toString(add_per_session));
        clicksTxt.setText(Integer.toString(user.getClickCounter())+"/"+Integer.toString(click_per_session));
        if (user.isPrepared() && user.isSettingLoaded()){
            InitializeAdds();
            prepareVideoAdd();
        }

        // Timers
        adDelay=new CountDownTimer(add_delay,1000) {
            @Override
            public void onTick(long l) {
                if (user.isShowingLog()){
                    final_messageTxt.setText("All the ads are loaded, Please wait till it shows!");
                }

            }
            @Override
            public void onFinish() {
                if (interstitialAd.isLoaded()){
                    if (!isThisForClick(user.getAdcounter())){
                        interstitialAd2.show();
                    }else{
                        interstitialAd.show();
                    }
                }
            }
        };

    }

    private void StartTask(){
        //Log.i("click","Task Started");
        adDelay.start();
    }

    private void InitializeAdds(){
        //------------------------------- Intertetial Add ------------------------------------------------
        interstitialAd=new InterstitialAd(getApplicationContext()); // main Add
        interstitialAd.setAdUnitId(imageAddID);
        interstitialAd1=new InterstitialAd(getApplicationContext());
        interstitialAd1.setAdUnitId(clickIDIndexes[0]);

        interstitialAd2=new InterstitialAd(getApplicationContext());
        interstitialAd2.setAdUnitId(clickIDIndexes[1]);
        MobileAds.initialize(getApplicationContext(),appID);

        /*interstitialAd3=new InterstitialAd(Task_1.this);
        interstitialAd3.setAdUnitId(imageAddID);

        interstitialAd4=new InterstitialAd(Task_1.this);
        interstitialAd4.setAdUnitId(imageAddID);

        interstitialAd5=new InterstitialAd(Task_1.this);
        interstitialAd5.setAdUnitId(imageAddID);

        /*interstitialAd6=new InterstitialAd(Task_1.this);
        interstitialAd6.setAdUnitId(imageAddID);*/

        PrepareInterstitialAdd();

        //
    }

    public void PrepareInterstitialAdd(){
        interstitialAd.loadAd(new AdRequest.Builder().build());
        interstitialAd.setAdListener(new AdListener(){
            @Override
            public void onAdLoaded() {
                if (user.isShowingLog()){
                    messageTxt.setText("Main Add Successfully Loaded");
                }
                checkAddCondition();
                super.onAdLoaded();
            }

            @Override
            public void onAdClosed() {
                if (isThisForClick(user.getAdcounter())){
                    if (user.isWait_for_action()){
                        if (user.isShowingLog()){
                            messageTxt.setText("You Have To Click This Add, Do not Close It");
                        }
                        InitializeAdds();
                        StartTask();
                    }else{
                        user.setAdcounter(user.getAdcounter()+1);
                        updateData.ProcessInterstitialAdd(user.getAdcounter(),"view");
                        CheckUserBreak();
                    }

                }else{
                    clickViewTxt.setText("Next Article");
                }
                super.onAdClosed();
            }

            @Override
            public void onAdOpened() {
                int_opened=true;
                Log.i("rahat","int_opened: "+Boolean.toString(int_opened));
                if (user.isAutoTask()){
                    if (!isThisForClick(user.getAdcounter())){
                        adTimer=new CountDownTimer(ad_waiting_time,1000) {
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
                }
                super.onAdOpened();
            }

            @Override
            public void onAdFailedToLoad(int i) {
                if (user.isShowingLog()){
                    messageTxt.setText("Please Wait While requestng Main Add, Code: "+Integer.toString(i));
                }
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
                   UpdateFraudStatus();
                }
                super.onAdLeftApplication();
            }

        });

        interstitialAd1.loadAd(new AdRequest.Builder().build());
        interstitialAd1.setAdListener(new AdListener(){
            @Override
            public void onAdOpened() {
                int_1_opened=true;
                Log.i("rahat","int_1_opened: "+Boolean.toString(int_1_opened));
                adTimer=new CountDownTimer(ad_waiting_time,1000) {
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
                if (user.isShowingLog()){
                    messageTxt1.setText("Add-1 Successfully Loaded");
                }
                checkAddCondition();
                super.onAdLoaded();
            }

            @Override
            public void onAdFailedToLoad(int i) {
                if (user.isShowingLog()){
                    messageTxt1.setText("Please Wait While requestng Add-1, Code: "+Integer.toString(i));
                }
                interstitialAd1.loadAd(new AdRequest.Builder().build());
                super.onAdFailedToLoad(i);
            }

            @Override
            public void onAdLeftApplication() {
                UpdateFraudStatus();
                super.onAdLeftApplication();
            }
        });



        interstitialAd2.loadAd(new AdRequest.Builder().build());
        interstitialAd2.setAdListener(new AdListener(){
            @Override
            public void onAdOpened() {
                int_2_opened=true;
                Log.i("rahat","int_2_opened: "+Boolean.toString(int_2_opened));
                adTimer=new CountDownTimer(ad_waiting_time,1000) {
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
                if (user.isShowingLog()){
                    messageTxt2.setText("Add-2 Successfully Loaded");
                }
                checkAddCondition();
                super.onAdLoaded();
            }

            @Override
            public void onAdFailedToLoad(int i) {
                if (user.isShowingLog()){
                    messageTxt2.setText("Please Wait While requestng Add-2, Code: "+Integer.toString(i));
                }
                interstitialAd2.loadAd(new AdRequest.Builder().build());
                super.onAdFailedToLoad(i);
            }

            @Override
            public void onAdLeftApplication() {
                UpdateFraudStatus();
                super.onAdLeftApplication();
            }
        });
    }
    private void checkAddCondition(){
        if (!isThisForClick(user.getAdcounter())){
            if (interstitialAd.isLoaded() && interstitialAd1.isLoaded() && interstitialAd2.isLoaded()){
                StartTask();
            }else{
                if (user.isShowingLog()){
                    final_messageTxt.setText("Please Wait till all the adds are loaded successfully");
                }
            }
        }else {
            StartTask();
        }
    }
    private void prepareBanner(){
        adView1=(AdView) findViewById(R.id.task_1_ad1);
        AdRequest adRequest1=new AdRequest.Builder().build();
        adView1.loadAd(adRequest1);
        adView1.setAdListener(new AdListener(){
            @Override
            public void onAdLeftApplication() {
                UpdateFraudStatus();
                super.onAdLeftApplication();
            }
        });

        adView2=(AdView) findViewById(R.id.task_1_ad2);
        AdRequest adRequest2=new AdRequest.Builder().build();
        adView2.loadAd(adRequest2);
        adView2.setAdListener(new AdListener(){
            @Override
            public void onAdLeftApplication() {
                UpdateFraudStatus();
                super.onAdLeftApplication();
            }
        });

        adView3=(AdView) findViewById(R.id.task_1_ad3);
        AdRequest adRequest3=new AdRequest.Builder().build();
        adView3.loadAd(adRequest3);
        adView3.setAdListener(new AdListener(){
            @Override
            public void onAdLeftApplication() {
                UpdateFraudStatus();
                super.onAdLeftApplication();
            }
        });

        adView4=(AdView) findViewById(R.id.task_1_ad4);
        AdRequest adRequest4=new AdRequest.Builder().build();
        adView4.loadAd(adRequest4);
        adView4.setAdListener(new AdListener(){
            @Override
            public void onAdLeftApplication() {
                UpdateFraudStatus();
                super.onAdLeftApplication();
            }
        });
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
                UpdateFraudStatus();
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
            if (user.isBreakAllowed()){
                updateData.UpdateBreak();
                user.setBreaktime(true);
            }
            startActivity(new Intent(getApplicationContext(),Task_complete.class));
            finish();
        }else {
            finish();
            startActivity(new Intent(getApplicationContext(), Task_2.class));
        }
    }
    private void CheckFraudStatus(){
        Log.i("fraud","Session: "+Integer.toString(user.getFraud()));
        Log.i("fraud","Daily: "+Integer.toString(user.getDailyFraud()));
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
