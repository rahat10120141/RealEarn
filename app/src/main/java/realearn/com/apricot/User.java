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

    SharedPreferences sharedPreferences;

    public void removeUser(){
        sharedPreferences.edit().clear().commit();
    }


    public boolean isBreaktime() {
        //breaktime=sharedPreferences.getBoolean("breaktime",false);
        //breaktime=sharedPreferences.getBoolean(encrypt("breaktime"),false);
        String hiddenKey=sharedPreferences.getString(encrypt("breaktime"),encrypt("false"));
        if(hiddenKey.equals(encrypt("false"))){
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
        }else{
            hiddenKey="false";
        }
        sharedPreferences.edit().putString(encrypt("breaktime"),hiddenKey);
        //sharedPreferences.edit().putBoolean("breaktime",breaktime).commit();
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

    public String getuId() {
        uId=sharedPreferences.getString(encrypt("userId"),encrypt("1"));
        Log.i("pref_user",encrypt("userId"));
        return decrypt(uId);
    }

    public int getAdcounter() {
        String adc="";
        adc=sharedPreferences.getString(encrypt("adcounter"),encrypt("0"));
        Log.i("pref",adc);
        adcounter=Integer.parseInt(decrypt(adc));
        return adcounter;
    }

    public void setAdcounter(int adcounter) {
        sharedPreferences.edit().putString(encrypt("adcounter"),encrypt(Integer.toString(adcounter))).commit();
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
    public User(Context context){
        this.context=context;
        sharedPreferences=context.getSharedPreferences(encrypt("realEarn"),context.MODE_PRIVATE);
        sharedPreferences.edit().remove("breaktime");
        sharedPreferences.edit().remove("userId");
        sharedPreferences.edit().remove("adcounter");
        sharedPreferences.edit().remove("mobile");
    }

    public String encrypt(String input) {
        // This is base64 encoding, which is not an encryption
        return Base64.encodeToString(input.getBytes(), Base64.DEFAULT);
    }

    public String decrypt(String input) {
        return new String(Base64.decode(input, Base64.DEFAULT));
    }

}
