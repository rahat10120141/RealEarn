package realearn.com.apricot;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Base64;
import android.util.Log;

import java.security.MessageDigest;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by Developer on 4/7/2018.
 */

public class User {
    Context context;
    private String uId;
    private String mobile;

    private boolean breaktime;
    private boolean autoTask;
    private boolean facebook_ad;
    private boolean remember_counters;
    private boolean settingLoaded;
    private boolean showingLog;
    private boolean prevent_phone_from_sleep;
    private boolean wait_for_action;
    private boolean activeUser;
    private boolean vpnAllowed;
    private boolean breakAllowed;


    private int adcounter;

    private int clickCounter;

    // Apsettings Variables

    public String imageID;
    public String videoID;

    public String image_ids_for_click;

    public boolean prepared;
    int fraud;
    int dailyFraud;
    int add_per_session;
    int click_per_session;
    int ad_waiting_time;
    int add_delay;
    int counter_delay;
    int maximumFraudPerSession;
    int maximumFraudPerDay;

    int clickReturnTime;
    String clickIndexes;
    String videoIndexes;
    String appID;

    String content_urls;

    SharedPreferences sharedPreferences;

    private static String EncryptedPassword="R@h@^o1830415206";
    String AES="AES";
    public User(Context context){
        this.context=context;
        try {
            sharedPreferences=context.getSharedPreferences(encrypt("realEarn"),context.MODE_PRIVATE);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //sharedPreferences= PreferenceManager.getDefaultSharedPreferences(context);      // for fixing login prompt
    }
    public void removeUser(){
        //sharedPreferences.edit().clear().commit();
        try {
            context.getSharedPreferences(encrypt("realEarn"), 0).edit().clear().commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
        //context.getSharedPreferences("realEarn",context.MODE_PRIVATE).edit().clear().commit();
    }
    public boolean isBreakAllowed() {
        String result="";
        try {
            result=decrypt(sharedPreferences.getString(encrypt("breakAllowed"),encrypt("true")));
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (result.equals("true")){
            breakAllowed=true;
        }else{
            breakAllowed=false;
        }
        return breakAllowed;
    }

    public void setBreakAllowed(boolean breakAllowed) {
        String hiddenKey="";
        if(breakAllowed){
            hiddenKey="true";
        }else{
            hiddenKey="false";
        }
        try {
            sharedPreferences.edit().putString(encrypt("breakAllowed"),encrypt(hiddenKey)).commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int getMaximumFraudPerDay() {
        try {
            maximumFraudPerDay= Integer.parseInt(decrypt(sharedPreferences.getString(encrypt("maximumFraudPerDay"),encrypt("80"))));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return maximumFraudPerDay;
    }

    public void setMaximumFraudPerDay(String maximumFraudPerDay) {
        try {
            sharedPreferences.edit().putString(encrypt("maximumFraudPerDay"),encrypt(maximumFraudPerDay)).commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int getMaximumFraudPerSession() {
        try {
            maximumFraudPerSession= Integer.parseInt(decrypt(sharedPreferences.getString(encrypt("maximumFraudPerSession"),encrypt("3"))));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return maximumFraudPerSession;
    }
    public void setMaximumFraudPerSession(String maximumFraudPerSession) {
        try {
            sharedPreferences.edit().putString(encrypt("maximumFraudPerSession"),encrypt(maximumFraudPerSession)).commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean isVpnAllowed() {
        String result="";
        try {
            result=decrypt(sharedPreferences.getString(encrypt("vpnAllowed"),encrypt("false")));
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (result.equals("true")){
            vpnAllowed=true;
        }else{
            vpnAllowed=false;
        }
        return vpnAllowed;
    }

    public void setVpnAllowed(boolean vpnAllowed) {
        String hiddenKey="";
        if(vpnAllowed){
            hiddenKey="true";
        }else{
            hiddenKey="false";
        }
        try {
            sharedPreferences.edit().putString(encrypt("vpnAllowed"),encrypt(hiddenKey)).commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean isActiveUser() {
        String result="";
        try {
            result=decrypt(sharedPreferences.getString(encrypt("activeUser"),encrypt("true")));
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (result.equals("true")){
            activeUser=true;
        }else{
            activeUser=false;
        }
        return activeUser;
    }
    public void setActiveUser(boolean activeUser) {
        String hiddenKey="";
        if(activeUser){
            hiddenKey="true";
        }else{
            hiddenKey="false";
        }
        try {
            sharedPreferences.edit().putString(encrypt("activeUser"),encrypt(hiddenKey)).commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean isWait_for_action() {
        String result="";
        try {
            result=decrypt(sharedPreferences.getString(encrypt("wait_for_action"),encrypt("false")));
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (result.equals("true")){
            wait_for_action=true;
        }else{
            wait_for_action=false;
        }
        return wait_for_action;
    }

    public void setWait_for_action(boolean wait_for_action) {
        String hiddenKey="";
        if(wait_for_action){
            hiddenKey="true";
        }else{
            hiddenKey="false";
        }
        try {
            sharedPreferences.edit().putString(encrypt("wait_for_action"),encrypt(hiddenKey)).commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean isPrevent_phone_from_sleep() {
        String result="";
        try {
            result=decrypt(sharedPreferences.getString(encrypt("prevent_phone_from_sleep"),encrypt("true")));


        } catch (Exception e) {
            e.printStackTrace();
        }
        if (result.equals("true")){
            prevent_phone_from_sleep=true;
        }else{
            prevent_phone_from_sleep=false;
        }
        return prevent_phone_from_sleep;
    }

    public void setPrevent_phone_from_sleep(boolean prevent_phone_from_sleep) {
        String hiddenKey="";
        if(prevent_phone_from_sleep){
            hiddenKey="true";
        }else{
            hiddenKey="false";
        }
        try {
            sharedPreferences.edit().putString(encrypt("prevent_phone_from_sleep"),encrypt(hiddenKey)).commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean isShowingLog() {
        String result="";
        try {
            result=decrypt(sharedPreferences.getString(encrypt("showingLog"),encrypt("true")));


        } catch (Exception e) {
            e.printStackTrace();
        }
        if (result.equals("true")){
            showingLog=true;
        }else{
            showingLog=false;
        }
        return showingLog;
    }

    public void setShowingLog(boolean showingLog) {
        String hiddenKey="";
        if(showingLog){
            hiddenKey="true";
        }else{
            hiddenKey="false";
        }
        try {
            sharedPreferences.edit().putString(encrypt("showingLog"),encrypt(hiddenKey)).commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public boolean isSettingLoaded() {
        String result="";
        try {
            result=decrypt(sharedPreferences.getString(encrypt("settingLoaded"),encrypt("false")));


        } catch (Exception e) {
            e.printStackTrace();
        }
        if (result.equals("true")){
            settingLoaded=true;
        }else{
            settingLoaded=false;
        }
        return settingLoaded;
    }

    public void setSettingLoaded(boolean settingLoaded) {
        String hiddenKey="";
        if(settingLoaded){
            hiddenKey="true";
        }else{
            hiddenKey="false";
        }
        try {
            sharedPreferences.edit().putString(encrypt("settingLoaded"),encrypt(hiddenKey)).commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public boolean Remember_counters() {
        String result="";
        try {
            result=decrypt(sharedPreferences.getString(encrypt("remember_counters"),encrypt("true")));


        } catch (Exception e) {
            e.printStackTrace();
        }
        if (result.equals("true")){
            remember_counters=true;
        }else{
            remember_counters=false;
        }
        return remember_counters;
    }

    public void setRemember_counters(boolean remember_counters) {
        String hiddenKey="";
        if(remember_counters){
            hiddenKey="true";
        }else{
            hiddenKey="false";
        }
        try {
            sharedPreferences.edit().putString(encrypt("remember_counters"),encrypt(hiddenKey)).commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean isFacebook_ad() {
        String result="";
        try {
            result=decrypt(sharedPreferences.getString(encrypt("facebook_ad"),encrypt("false")));


        } catch (Exception e) {
            e.printStackTrace();
        }
        if (result.equals("true")){
            facebook_ad=true;
        }else{
            facebook_ad=false;
        }
        return facebook_ad;
    }

    public void setFacebook_ad(boolean facebook_ad) {
        String hiddenKey="";
        if(facebook_ad){
            hiddenKey="true";
        }else{
            hiddenKey="false";
        }
        try {
            sharedPreferences.edit().putString(encrypt("facebook_ad"),encrypt(hiddenKey)).commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public boolean isAutoTask() {
        String result="";
        try {
            result=decrypt(sharedPreferences.getString(encrypt("autoTask"),encrypt("true")));


        } catch (Exception e) {
            e.printStackTrace();
        }
        if (result.equals("true")){
            autoTask=true;
        }else{
            autoTask=false;
        }
        return autoTask;
    }

    public void setAutoTask(boolean autoTask) {
        String hiddenKey="";
        if(autoTask){
            hiddenKey="true";
        }else{
            hiddenKey="false";
        }
        try {
            sharedPreferences.edit().putString(encrypt("autoTask"),encrypt(hiddenKey)).commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public boolean isBreaktime() {
        String hiddenKey= null;
        try {
            hiddenKey = sharedPreferences.getString(encrypt("breaktime"),encrypt("false"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            if(decrypt(hiddenKey).equals("false")){
                breaktime=false;
            }else{
                breaktime=true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return breaktime;
    }

    public void setBreaktime(boolean breaktime) {
        String hiddenKey="";
        if(breaktime){
            hiddenKey="true";
        }else{
            hiddenKey="false";
        }
        try {
            sharedPreferences.edit().putString(encrypt("breaktime"),encrypt(hiddenKey)).commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int getClickCounter() {
        try {
            clickCounter= Integer.parseInt(decrypt(sharedPreferences.getString(encrypt("clickCounter"),encrypt("0"))));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return clickCounter;
    }

    public void setClickCounter(int clickCounter) {
        try {
            sharedPreferences.edit().putString(encrypt("clickCounter"),encrypt(Integer.toString(clickCounter))).commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public int getAdcounter() {
        try {
            adcounter= Integer.parseInt(decrypt(sharedPreferences.getString(encrypt("adcounter"),encrypt("0"))));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return adcounter;
    }

    public void setAdcounter(int adcounter) {
        try {
            sharedPreferences.edit().putString(encrypt("adcounter"),encrypt(Integer.toString(adcounter))).commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public String getuId() {
        try {
            uId=sharedPreferences.getString("uId",null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return uId;
    }
    public void setuId(String uId) {
        try {
            sharedPreferences.edit().putString("uId",uId).commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public String getMobile() {
        try {
            mobile=decrypt(sharedPreferences.getString(encrypt("mobile"),encrypt("100")));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mobile;
    }

    public void setMobile(String mobile) {
        try {
            sharedPreferences.edit().putString(encrypt("mobile"),encrypt(mobile)).commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public int getFraud() {
        try {
            fraud= Integer.parseInt(decrypt(sharedPreferences.getString(encrypt("fraud"),encrypt("0"))));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return fraud;
    }

    public void setFraud(int fraud) {
        try {
            sharedPreferences.edit().putString(encrypt("fraud"),encrypt(Integer.toString(fraud))).commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public int getDailyFraud() {
        try {
            dailyFraud= Integer.parseInt(decrypt(sharedPreferences.getString(encrypt("dailyFraud"),encrypt("0"))));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dailyFraud;
    }

    public void setDailyFraud(int dailyFraud) {
        try {
            sharedPreferences.edit().putString(encrypt("dailyFraud"),encrypt(Integer.toString(dailyFraud))).commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // App Setting Methods

    public String getImageID() {
        try {
            imageID=decrypt(sharedPreferences.getString(encrypt("imageID"),encrypt("100")));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return imageID;
    }

    public void setImageID(String imageID) {
        try {
            sharedPreferences.edit().putString(encrypt("imageID"),encrypt(imageID)).commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getImage_ids_for_click() {
        try {
            image_ids_for_click=decrypt(sharedPreferences.getString(encrypt("image_ids_for_click"),encrypt("100")));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return image_ids_for_click;
    }

    public void setImage_ids_for_click(String image_ids_for_click) {
        try {
            sharedPreferences.edit().putString(encrypt("image_ids_for_click"),encrypt(image_ids_for_click)).commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public String getVideoID() {
        try {
            videoID=decrypt(sharedPreferences.getString(encrypt("videoID"),encrypt("100")));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return videoID;
    }

    public void setVideoID(String videoID) {
        try {
            sharedPreferences.edit().putString(encrypt("videoID"),encrypt(videoID)).commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean isPrepared() {
        String result="";
        try {
            result=decrypt(sharedPreferences.getString(encrypt("prepared"),encrypt("false")));


        } catch (Exception e) {
            e.printStackTrace();
        }
        if (result.equals("true")){
            prepared=true;
        }else{
            prepared=false;
        }
        return prepared;
    }

    public void setPrepared(boolean prepared) {
        String hiddenKey="";
        if(prepared){
            hiddenKey="true";
        }else{
            hiddenKey="false";
        }
        try {
            sharedPreferences.edit().putString(encrypt("prepared"),encrypt(hiddenKey)).commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int getAdd_per_session() {
        try {
            add_per_session= Integer.parseInt(decrypt(sharedPreferences.getString(encrypt("add_per_session"),encrypt("10"))));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return add_per_session;
    }

    public void setAdd_per_session(String add_per_session) {
        try {
            sharedPreferences.edit().putString(encrypt("add_per_session"),encrypt(add_per_session)).commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int getClick_per_session() {
        try {
            click_per_session= Integer.parseInt(decrypt(sharedPreferences.getString(encrypt("click_per_session"),encrypt("1"))));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return click_per_session;
    }

    public void setClick_per_session(String click_per_session) {
        try {
            sharedPreferences.edit().putString(encrypt("click_per_session"),encrypt(click_per_session)).commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int getAd_waiting_time() {
        try {
            ad_waiting_time= Integer.parseInt(decrypt(sharedPreferences.getString(encrypt("ad_waiting_time"),encrypt("60000"))));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ad_waiting_time;
    }   // Depricated

    public void setAd_waiting_time(String ad_waiting_time) {
        try {
            sharedPreferences.edit().putString(encrypt("ad_waiting_time"),encrypt(ad_waiting_time)).commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }   // Depricated

    public int getAdd_delay() {
        try {
            add_delay= Integer.parseInt(decrypt(sharedPreferences.getString(encrypt("add_delay"),encrypt("60000"))));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return add_delay;
    }

    public void setAdd_delay(String add_delay) {
        try {
            sharedPreferences.edit().putString(encrypt("add_delay"),encrypt(add_delay)).commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int getCounter_delay() {
        try {
            counter_delay= Integer.parseInt(decrypt(sharedPreferences.getString(encrypt("counter_delay"),encrypt("15000"))));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return counter_delay;
    }       // Depricated

    public void setCounter_delay(String counter_delay) {
        try {
            sharedPreferences.edit().putString(encrypt("counter_delay"),encrypt(counter_delay)).commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }   // Depricated

    public String getAppID() {
        try {
            appID=decrypt(sharedPreferences.getString(encrypt("appID"),encrypt("0")));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return appID;
    }

    public void setAppID(String appID) {
        try {
            sharedPreferences.edit().putString(encrypt("appID"),encrypt(appID)).commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int getClickReturnTime() {
        try {
            clickReturnTime= Integer.parseInt(decrypt(sharedPreferences.getString(encrypt("clickReturnTime"),encrypt("30000"))));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return clickReturnTime;
    }

    public void setClickReturnTime(String clickReturnTime) {
        try {
            sharedPreferences.edit().putString(encrypt("clickReturnTime"),encrypt(clickReturnTime)).commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getClickIndexes() {
        try {
            clickIndexes=decrypt(sharedPreferences.getString(encrypt("clickIndexes"),encrypt("100")));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return clickIndexes;
    }

    public void setClickIndexes(String clickIndexes) {
        try {
            sharedPreferences.edit().putString(encrypt("clickIndexes"),encrypt(clickIndexes)).commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getvideoIndexes() {
        try {
            videoIndexes=decrypt(sharedPreferences.getString(encrypt("videoIndexes"),encrypt("100")));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return videoIndexes;
    }

    public void setvideoIndexes(String videoIndexes) {
        try {
            sharedPreferences.edit().putString(encrypt("videoIndexes"),encrypt(videoIndexes)).commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getContent_urls() {
        try {
            content_urls=decrypt(sharedPreferences.getString(encrypt("content_urls"),encrypt("#")));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return content_urls;
    }

    public void setContent_urls(String content_urls) {
        try {
            sharedPreferences.edit().putString(encrypt("content_urls"),encrypt(content_urls)).commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }





    // Enf Of App Setting
    public String encrypt(String input) throws Exception{
        // This is base64 encoding, which is not an encryption
        // For Login Problem Encrytion is turned off
        //return Base64.encodeToString(input.getBytes(), Base64.DEFAULT);


        SecretKeySpec key=generateKey(EncryptedPassword);       // EncryptedPassword is the key for nencryption
        Cipher c=Cipher.getInstance(AES);
        c.init(Cipher.ENCRYPT_MODE,key);
        byte[] encVal=c.doFinal(input.getBytes());
        String encryptedValue=Base64.encodeToString(encVal,Base64.DEFAULT);
        return encryptedValue;
    }

    public String decrypt(String input) throws Exception{

        SecretKeySpec key=generateKey(EncryptedPassword);
        Cipher c=Cipher.getInstance(AES);
        c.init(Cipher.DECRYPT_MODE,key);
        byte[] decodVal=Base64.decode(input,Base64.DEFAULT);
        byte[] decVal=c.doFinal(decodVal);
        String decryptedValue=new String(decVal);
        return decryptedValue;


       // return input;
        //return new String(Base64.decode(input, Base64.DEFAULT));
    }

    private SecretKeySpec generateKey(String EncryptedPassKey) throws Exception{
        final MessageDigest digest=MessageDigest.getInstance("SHA-256");
        byte[] bytes=EncryptedPassKey.getBytes("UTF-8");
        digest.update(bytes,0,bytes.length);
        byte[] key=digest.digest();
        SecretKeySpec secretKeySpec=new SecretKeySpec(key,"AES");
        return secretKeySpec;
    }


}
