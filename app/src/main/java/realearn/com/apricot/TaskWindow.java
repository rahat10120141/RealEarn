package realearn.com.apricot;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.CountDownTimer;
import android.os.Debug;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;
import android.widget.TextView;

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
import com.google.firebase.analytics.FirebaseAnalytics;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

import realearn.com.apricot.R;

public class TaskWindow extends AppCompatActivity {

    Thread adDelay;
    AdView adView1,adView2,adView3;
    private InterstitialAd interstitialAd;
    private RewardedVideoAd rewardedVideoAd;

    private FirebaseAnalytics firebaseAnalytics;

    String bannerID;
    String imageID;//="ca-app-pub-3940256099942544/1033173712";
    String videoID;//="ca-app-pub-3940256099942544/5224354917";

    TextView countdownText,message,impression,fraudCounter,clickCounter,vc;
    int adCount=0;
    int fraud=0;
    int totImoression;
    int add_per_session;
    int click_per_session;
    int ad_waiting_time;
    int add_delay;
    int counter_delay;
    private boolean lastAdClosed=true;
    private boolean isItSafeToCloseAd=false;
    private boolean clickAdd=false;

    UpdateData updateData;
    CountDownTimer countDownTimer;
    CountDownTimer safeToClose;
    CountDownTimer returnToTaskWindow;

    AlertDialog.Builder builder;

    User user;


    //String addUrls=Appurls.add_urls;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_window);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        user=new User(TaskWindow.this);
        builder=new AlertDialog.Builder(TaskWindow.this);

        //MobileAds.initialize(TaskWindow.this,bannerID);
        final Intent intent=new Intent();
        totImoression=intent.getIntExtra("total_impression",0);
        //updateData.UpdateDeviceInfoLog(deviceId,"test","1");

        firebaseAnalytics=FirebaseAnalytics.getInstance(TaskWindow.this);
       /* if(user.isBreaktime()){
            DisplayAlert("breaktime");
        }
*/
        countdownText=(TextView) findViewById(R.id.ct);
        message=(TextView)findViewById(R.id.messageTxt);
        impression=(TextView)findViewById(R.id.impressionTxt);
        fraudCounter=(TextView)findViewById(R.id.FraudTxt);
        clickCounter=(TextView)findViewById(R.id.clickText);
        vc=(TextView) findViewById(R.id.vc);

        message.setText("Please Wait Ad Will be loaded !!");
        impression.setText(Integer.toString(user.getAdcounter())+"/"+add_per_session);

        getAddId(new VolleyCallback(){
            @Override
            public void onSuccess(String result){
                try{
                    JSONArray jsonArray=new JSONArray(result);
                    JSONObject jsonObject=jsonArray.getJSONObject(0);
                    bannerID=jsonObject.getString("banner_id");
                    imageID=jsonObject.getString("image_add");
                    videoID=jsonObject.getString("video_add");
                    add_per_session=Integer.parseInt(jsonObject.getString("add_per_session"));
                    add_delay=Integer.parseInt(jsonObject.getString("add_delay"));
                    counter_delay=Integer.parseInt(jsonObject.getString("counter_delay"));
                    click_per_session=Integer.parseInt(jsonObject.getString("click_per_session"));
                    ad_waiting_time=Integer.parseInt(jsonObject.getString("ad_waiting_time"));
                    MobileAds.initialize(TaskWindow.this,jsonObject.getString("app_id"));


                    impression.setText(Integer.toString(user.getAdcounter())+"/"+add_per_session);
                    clickCounter.setText(Integer.toString(user.getClickCounter())+"/"+click_per_session);
                    if (user.getAdcounter()%click_per_session==0){
                        clickAdd=true;
                        SetCommand();
                    }else{
                        clickAdd=false;
                        SetCommand();
                    }

                    if(imageID!=null){
                        Log.i("ImageAdd",imageID);
                        interstitialAd=new InterstitialAd(TaskWindow.this);
                        interstitialAd.setAdUnitId(imageID);
                        PrepareInterstitialAdd();
                    }
                    if (bannerID !=null){
                        Log.i("BannerAdd",bannerID);
                        prepareBanner();
                    }
                    if (videoID!=null){
                        prepareVideoAdd();
                        //prepareVideoAdd();
                    }
                    Log.i("BreakTime","Thread is going to be started");
                    adDelay.start();
                    countDownTimer=new CountDownTimer(counter_delay,1000) {
                        @Override
                        public void onTick(long l) {
                            countdownText.setText(""+l/1000);
                        }

                        @Override
                        public void onFinish() {
                            if (user.getAdcounter()==4 ||user.getAdcounter()==9 && lastAdClosed){
                                rewardedVideoAd.show();
                                countDownTimer.cancel();
                                lastAdClosed=false;
                            }else{
                                interstitialAd.show();
                                lastAdClosed=false;
                                countdownText.setText("");
                                countDownTimer.cancel();
                                message.setText("Ad is Opening...");
                            }

                            //AssignText(message,"Ad is Opening...");
                        }
                    };
                    safeToClose=new CountDownTimer(ad_waiting_time,1000) {
                        @Override
                        public void onTick(long l) {
                            isItSafeToCloseAd=false;
                        }

                        @Override
                        public void onFinish() {
                            isItSafeToCloseAd=true;
                            safeToClose.cancel();
                        }
                    };
                    returnToTaskWindow=new CountDownTimer(5000,1000) {
                        @Override
                        public void onTick(long millisUntilFinished) {
                            onRestart();
                        }

                        @Override
                        public void onFinish() {

                        }
                    };
                }catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
       /* countDownTimer=new CountDownTimer(counter_delay,1000) {
            @Override
            public void onTick(long l) {
                countdownText.setText(""+l/1000);
            }

            @Override
            public void onFinish() {
                if (user.getAdcounter()==4 ||user.getAdcounter()==9 && lastAdClosed){
                    rewardedVideoAd.show();
                    countDownTimer.cancel();
                    lastAdClosed=false;
                }else{
                    interstitialAd.show();
                    lastAdClosed=false;
                    countdownText.setText("");
                    countDownTimer.cancel();
                    message.setText("Ad is Opening...");
                }

                //AssignText(message,"Ad is Opening...");
            }
        };

        safeToClose=new CountDownTimer(ad_waiting_time,1000) {
            @Override
            public void onTick(long l) {
                isItSafeToCloseAd=false;
            }

            @Override
            public void onFinish() {
                isItSafeToCloseAd=true;
            }
        };*/
        updateData=new UpdateData(TaskWindow.this);
        adDelay=new Thread(){
            @Override
            public void run() {
                while (!isInterrupted()){
                    try{
                        Thread.sleep(add_delay);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                /*Log.i("BreakTime",Boolean.toString(user.isBreaktime()));
                                Log.i("BreakTime",Integer.toString(add_per_session));
                                Log.i("BreakTime",Integer.toString(user.getAdcounter()));*/
                                if (user.getAdcounter()>=add_per_session || user.isBreaktime()){
                                    safeToClose.cancel();
                                    countDownTimer.cancel();
                                    adDelay.interrupt();
                                    updateData.UpdateBreak();
                                    user.setBreaktime(true);
                                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            finish();
                                            startActivity(new Intent(TaskWindow.this,UserWelcome.class));
                                        }
                                    });
                                    builder.setTitle("Task Complete");
                                    builder.setMessage("Your Task is complete for this session. Use this App after 1 hour");
                                    if (!isFinishing()){
                                        AlertDialog alertDialog=builder.create();
                                        alertDialog.show();
                                    }
                                    //DisplayAlert("task_finished");
                                }else{
                                    if (user.getAdcounter()==4 ||user.getAdcounter()==9 && lastAdClosed){
                                        if (rewardedVideoAd.isLoaded()){
                                            message.setText("Video Add will be loaded in ....");
                                            countDownTimer.start();
                                            lastAdClosed=false;
                                        }else{
                                            if (interstitialAd.isLoaded()&& lastAdClosed){
                                                //interstitialAd.show();
                                                message.setText("Add will be loaded in ....");
                                                //AssignText(message,"Add will be loaded in ....");
                                                countDownTimer.start();
                                                adDelay.interrupt();
                                                lastAdClosed=false;
                                            }else {
                                                PrepareInterstitialAdd();
                                            }
                                            loadRewardedVideoAd();
                                        }
                                    }else{
                                        if (interstitialAd.isLoaded()&& lastAdClosed){
                                            //interstitialAd.show();
                                            message.setText("Add will be loaded in ....");
                                            //AssignText(message,"Add will be loaded in ....");
                                            countDownTimer.start();
                                            adDelay.interrupt();
                                            lastAdClosed=false;
                                        }else {
                                            PrepareInterstitialAdd();
                                        }
                                    }
                                }
                            }
                        });
                    }catch (InterruptedException e){
                        e.printStackTrace();
                    }
                }
                super.run();
            }
        };
    }
    private void prepareBanner(){
        adView1=(AdView) findViewById(R.id.taskAd1);
        AdRequest adRequest1=new AdRequest.Builder().addTestDevice(AdRequest.DEVICE_ID_EMULATOR).build();
        adView1.loadAd(adRequest1);
    }
    public void PrepareInterstitialAdd(){
        interstitialAd.loadAd(new AdRequest.Builder().addTestDevice(AdRequest.DEVICE_ID_EMULATOR).build());
        interstitialAd.setAdListener(new AdListener(){
            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
            }

            @Override
            public void onAdClosed() {
                if (!isItSafeToCloseAd){
                    fraud+=1;
                    fraudCounter.setText(Integer.toString(fraud));
                    lastAdClosed=true;
                    message.setText("Please Wait Ad Will be loaded !!");
                    updateData.UpdateFraud();
                    safeToClose.cancel();
                    DisplayAlert("illegal_view");
                }else{
                    lastAdClosed=true;
                    PrepareInterstitialAdd();
                    user.setAdcounter(user.getAdcounter()+1);
                    totImoression+=1;
                    updateData.ProcessInterstitialAdd(user.getAdcounter(),"view");
                    impression.setText(Integer.toString(user.getAdcounter())+"/"+add_per_session);
                    message.setText("Please Wait Ad Will be loaded !!");
                    safeToClose.cancel();
                }

                super.onAdClosed();
            }

            @Override
            public void onAdOpened() {
                safeToClose.start();
                Log.i("IntAdd","Add Opened");
                //interetitialAdDelay.isInterrupted();
                super.onAdOpened();
            }

            @Override
            public void onAdFailedToLoad(int i) {
                //interetitialAdDelay.start();
                Log.i("IntAdd","Add Failed To Load");
                super.onAdFailedToLoad(i);
            }

            @Override
            public void onAdLeftApplication() {
                user.setClickCounter(user.getClickCounter()+1);
                clickCounter.setText(Integer.toString(user.getClickCounter())+"/"+click_per_session);
                returnToTaskWindow.start();
                //Log.i("IntAdd","Add Left Application");
                super.onAdLeftApplication();
            }

            @Override
            public void onAdImpression() {
                Log.i("IntAdd","Add Impression");
                super.onAdImpression();
            }
        });
    }
    public void prepareVideoAdd(){
        rewardedVideoAd=MobileAds.getRewardedVideoAdInstance(TaskWindow.this);
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
                lastAdClosed=true;
                message.setText("Please Wait Ad Will be loaded !!");
                updateData.UpdateFraud();
                DisplayAlert("video_illegal_view");
            }

            @Override
            public void onRewarded(RewardItem rewardItem) {
                lastAdClosed=true;
                loadRewardedVideoAd();
                user.setAdcounter(user.getAdcounter()+1);
                updateData.ProcessVideoAdd(user.getAdcounter());
                impression.setText(Integer.toString(user.getAdcounter())+"/"+add_per_session);
                message.setText("Please Wait Ad Will be loaded !!");
                if (user.getAdcounter()>=add_per_session){
                    safeToClose.cancel();
                    countDownTimer.cancel();
                    adDelay.interrupt();
                    updateData.UpdateBreak();
                    user.setBreaktime(true);
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                                finish();
                                startActivity(new Intent(TaskWindow.this,UserWelcome.class));
                        }
                    });
                    builder.setTitle("Task Complete");
                    builder.setMessage("Your Task is complete for this session. Use this App after 1 hour");

                    if (!isFinishing()){
                        AlertDialog alertDialog=builder.create();
                        alertDialog.show();
                    }

                }
            }

            @Override
            public void onRewardedVideoAdLeftApplication() {
                updateData.UpdateFraud();
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
        rewardedVideoAd.loadAd(videoID,
                new AdRequest.Builder().build());

    }
    public void DisplayAlert(final String code){
        if(code.equals("illegal_view")){
            builder.setTitle("Illegal View");
            builder.setMessage("Please Open The Add At least 5 Second");
        }
        if (code.equals("video_illegal_view")){
            builder.setTitle("Illegal View");
            builder.setMessage("Please View the full video Add");
        }
        if (code.equals("click_add")){
            builder.setTitle("Click an Add");
            builder.setMessage("Please Click Any Add, And wait untill it loaded");
        }
        AlertDialog alertDialog=builder.create();
        alertDialog.show();
    }
    public void getAddId(final VolleyCallback callback){
        StringRequest stringRequest=new StringRequest(Request.Method.POST, Appurls.add_urls, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray=new JSONArray(response);
                    JSONObject jsonObject=jsonArray.getJSONObject(0);
                    callback.onSuccess(response);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return super.getParams();
            }
        };
        Mysingleton.getmInstance(TaskWindow.this).AddToRequestQue(stringRequest);
    }

    private void SetCommand(){
        if (clickAdd){
            vc.setText("Click");
        }else{
            vc.setText("View");
        }
    }
    public interface VolleyCallback{
        void onSuccess(String result);
    }
    @Override
    public void onBackPressed() {
        //interetitialAdDelay.interrupt();
        Log.i("Test","Back Button Pressed");
        safeToClose.cancel();
        countDownTimer.cancel();
        adDelay.interrupt();
        finish();
        startActivity(new Intent(TaskWindow.this,UserWelcome.class));
        super.onBackPressed();
    }

    @Override
    protected void onPause() {
        Log.i("Test","Paused");
        super.onPause();
    }

    @Override
    protected void onResume() {
        Log.i("Test","Resume");
        super.onResume();
    }

    @Override
    protected void onRestart() {
        Log.i("Test","Restart");
        super.onRestart();
    }
}