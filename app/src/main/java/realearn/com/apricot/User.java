package realearn.com.apricot;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Base64;
import android.util.Log;

/**
 * Created by Developer on 4/7/2018.
 */

public class User {
    Context context;
    private String uId;
    private String mobile;

    private boolean breaktime;

    private boolean adclicked;


    private int adcounter;

    private int clickCounter;

    // Apsettings Variables

    public String imageID;
    public String videoID;

    public String image_ids_for_click;

    public boolean prepared=false;
    int fraud=0;
    int totImoression;
    int add_per_session;
    int click_per_session;
    int ad_waiting_time;
    int add_delay;
    int counter_delay;

    int clickReturnTime;
    String clickIndexes;
    String videoIndexes;
    String appID;

    SharedPreferences sharedPreferences;
    public User(Context context){
        this.context=context;
        sharedPreferences=context.getSharedPreferences(encrypt("realEarn"),context.MODE_PRIVATE);
    }
    public void removeUser(){
        //sharedPreferences.edit().clear().commit();
        context.getSharedPreferences("realEarn", 0).edit().clear().commit();
        //context.getSharedPreferences("realEarn",context.MODE_PRIVATE).edit().clear().commit();
    }
    public boolean isBreaktime() {
        String hiddenKey=sharedPreferences.getString(encrypt("breaktime"),encrypt("false"));
        Log.i("result","Get Result Encrypted: "+hiddenKey);
        Log.i("result","Get Result Decrypted: "+decrypt(hiddenKey));
        if(decrypt(hiddenKey).equals("false")){
            breaktime=false;
        }else{
            breaktime=true;
        }
        return breaktime;
    }

    public void setBreaktime(boolean breaktime) {
        String hiddenKey="";
        if(breaktime){
            hiddenKey="true";
            Log.i("result","Value Tobe Encrypted: "+hiddenKey);
        }else{
            hiddenKey="false";
            Log.i("result","Value Tobe Encrypted: "+hiddenKey);
        }
        sharedPreferences.edit().putString(encrypt("breaktime"),encrypt(hiddenKey)).commit();
    }

    public boolean isAdclicked() {
        adclicked=sharedPreferences.getBoolean("adClicked",false);
        return adclicked;
    }

    public void setAdclicked(boolean adclicked) {
        sharedPreferences.edit().putBoolean("adClicked",adclicked).commit();
    }

    public int getClickCounter() {
        String adc="";
        adc=sharedPreferences.getString(encrypt("clickCounter"),encrypt("0"));
        clickCounter=Integer.parseInt(decrypt(adc));
        return clickCounter;
    }

    public void setClickCounter(int clickCounter) {
        sharedPreferences.edit().putString(encrypt("clickCounter"),encrypt(Integer.toString(clickCounter))).commit();
    }
    public int getAdcounter() {
        String adc="";
        adc=sharedPreferences.getString(encrypt("adcounter"),encrypt("0"));
        adcounter=Integer.parseInt(decrypt(adc));
        return adcounter;
    }

    public void setAdcounter(int adcounter) {
        sharedPreferences.edit().putString(encrypt("adcounter"),encrypt(Integer.toString(adcounter))).commit();
    }
    public String getuId() {
        //context.getSharedPreferences("realEarn", 0).edit().clear().commit();
        uId=sharedPreferences.getString(encrypt("userId"),encrypt("0"));
        if (decrypt(uId).equals("0")){
            return null;
        }else {
            return decrypt(uId);
        }
    }
    public void setuId(String uId) {
        //this.uId = uId;
        sharedPreferences.edit().putString(encrypt("userId"),encrypt(uId)).commit();
    }
    public String getMobile() {
        mobile=sharedPreferences.getString(encrypt("mobile"),encrypt("0"));
        return decrypt(mobile);
    }

    public void setMobile(String mobile) {
        sharedPreferences.edit().putString(encrypt("mobile"),encrypt(mobile)).commit();
    }
    public int getFraud() {
        return fraud;
    }

    public void setFraud(int fraud) {
        this.fraud = fraud;
    }

    public int getTotImoression() {
        return totImoression;
    }

    public void setTotImoression(int totImoression) {
        this.totImoression = totImoression;
    }


    // App Setting Methods

    public String getImageID() {
        imageID=sharedPreferences.getString(encrypt("imageID"),encrypt(""));
        return decrypt(imageID);
    }

    public void setImageID(String imageID) {
        sharedPreferences.edit().putString(encrypt("imageID"),encrypt(imageID)).commit();
    }

    public String getImage_ids_for_click() {
        image_ids_for_click=sharedPreferences.getString(encrypt("image_ids_for_click"),encrypt(""));
        return decrypt(image_ids_for_click);
    }

    public void setImage_ids_for_click(String image_ids_for_click) {
        sharedPreferences.edit().putString(encrypt("image_ids_for_click"),encrypt(image_ids_for_click)).commit();
    }
    public String getVideoID() {
        videoID=sharedPreferences.getString(encrypt("videoID"),encrypt(""));
        return decrypt(videoID);
    }

    public void setVideoID(String videoID) {
        sharedPreferences.edit().putString(encrypt("videoID"),encrypt(videoID)).commit();
    }

    public boolean isPrepared() {
        String hiddenKey=sharedPreferences.getString(encrypt("prepared"),encrypt("false"));
        //Log.i("result","Get Result Encrypted: "+hiddenKey);
        //Log.i("result","Get Result Decrypted: "+decrypt(hiddenKey));
        if(decrypt(hiddenKey).equals("false")){
            prepared=false;
        }else{
            prepared=true;
        }
        return prepared;
    }

    public void setPrepared(boolean prepared) {
        String hiddenKey="";
        if(prepared){
            hiddenKey="true";
            //Log.i("result","Value Tobe Encrypted: "+hiddenKey);
        }else{
            hiddenKey="false";
            //Log.i("result","Value Tobe Encrypted: "+hiddenKey);
        }
        sharedPreferences.edit().putString(encrypt("prepared"),encrypt(hiddenKey)).commit();
        //Log.i("result","Encrypted: "+sharedPreferences.getString(encrypt("prepared"),encrypt("false")));
        //Log.i("result",Boolean.toString(isPrepared()));
    }



    public int getAdd_per_session() {
        return Integer.parseInt(decrypt(sharedPreferences.getString(encrypt("add_per_session"),encrypt("12"))));
    }

    public void setAdd_per_session(String add_per_session) {
        sharedPreferences.edit().putString(encrypt("add_per_session"),encrypt(add_per_session)).commit();
    }

    public int getClick_per_session() {
        return Integer.parseInt(decrypt(sharedPreferences.getString(encrypt("click_per_session"),encrypt("3"))));
    }

    public void setClick_per_session(String click_per_session) {
        sharedPreferences.edit().putString(encrypt("click_per_session"),encrypt(click_per_session)).commit();
    }

    public int getAd_waiting_time() {
        return Integer.parseInt(decrypt(sharedPreferences.getString(encrypt("ad_waiting_time"),encrypt("5000"))));
    }

    public void setAd_waiting_time(String ad_waiting_time) {
        sharedPreferences.edit().putString(encrypt("ad_waiting_time"),encrypt(ad_waiting_time)).commit();
    }

    public int getAdd_delay() {
        return Integer.parseInt(decrypt(sharedPreferences.getString(encrypt("add_delay"),encrypt("50000"))));
    }

    public void setAdd_delay(String add_delay) {
        sharedPreferences.edit().putString(encrypt("add_delay"),encrypt(add_delay)).commit();
    }

    public int getCounter_delay() {
        return Integer.parseInt(decrypt(sharedPreferences.getString(encrypt("counter_delay"),encrypt("15000"))));
    }

    public void setCounter_delay(String counter_delay) {
        sharedPreferences.edit().putString(encrypt("counter_delay"),encrypt(counter_delay)).commit();
    }

    public String getAppID() {
        appID=sharedPreferences.getString(encrypt("appID"),encrypt(""));
        return decrypt(appID);
    }

    public void setAppID(String appID) {
        sharedPreferences.edit().putString(encrypt("appID"),encrypt(appID)).commit();
    }

    public int getClickReturnTime() {
        return Integer.parseInt(decrypt(sharedPreferences.getString(encrypt("clickReturnTime"),encrypt("30000"))));
    }

    public void setClickReturnTime(String clickReturnTime) {
        sharedPreferences.edit().putString(encrypt("clickReturnTime"),encrypt(clickReturnTime)).commit();
    }

    public String getClickIndexes() {
        clickIndexes=sharedPreferences.getString(encrypt("clickIndexes"),encrypt("1,5,7"));
        return decrypt(clickIndexes);
    }

    public void setClickIndexes(String clickIndexes) {
        sharedPreferences.edit().putString(encrypt("clickIndexes"),encrypt(clickIndexes)).commit();
    }

    public String getvideoIndexes() {
        videoIndexes=sharedPreferences.getString(encrypt("videoIndexes"),encrypt("9"));
        return decrypt(videoIndexes);
    }

    public void setvideoIndexes(String videoIndexes) {
        sharedPreferences.edit().putString(encrypt("videoIndexes"),encrypt(videoIndexes)).commit();
    }

    // Enf Of App Setting
    public String encrypt(String input) {
        // This is base64 encoding, which is not an encryption
        return Base64.encodeToString(input.getBytes(), Base64.DEFAULT);
    }

    public String decrypt(String input) {
        return new String(Base64.decode(input, Base64.DEFAULT));
    }

}
