package realearn.com.apricot;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Base64;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

public class AppSettings {

    /*public String imageID;
    public String videoID;

    int fraud=0;
    int totImoression;
    int add_per_session;
    int click_per_session;
    int ad_waiting_time;
    int add_delay;
    int counter_delay;
    String appID;*/

    User user;

    public boolean prepared=false;
    Context context;
    //SharedPreferences sharedPreferences;


    AppSettings(Context context){
        this.context=context;
        user=new User(context);
        //sharedPreferences=context.getSharedPreferences(encrypt("AppSetting"),context.MODE_PRIVATE);

        GetTaskData(new VolleyCallback(){
            @Override
            public void onSuccess(String result) {
                try {
                    //user.removeSettings();
                    JSONArray jsonArray=new JSONArray(result);
                    JSONObject jsonObject=jsonArray.getJSONObject(0);
                    user.setImageID(jsonObject.getString("image_add"));
                    user.setVideoID(jsonObject.getString("video_add"));
                    user.setAd_waiting_time(jsonObject.getString("ad_waiting_time"));
                    user.setAdd_delay(jsonObject.getString("add_delay"));
                    user.setAdd_per_session(jsonObject.getString("add_per_session"));
                    user.setClick_per_session(jsonObject.getString("click_per_session"));
                    user.setClickIndexes(jsonObject.getString("click_indexes"));
                    user.setvideoIndexes(jsonObject.getString("video_indexes"));
                    //user.setvideoIndexes("0222");
                    user.setClickReturnTime(jsonObject.getString("click_return_time"));
                    user.setAppID(jsonObject.getString("app_id"));
                    if (!user.getImageID().equals("") && !user.getVideoID().equals("")){
                        user.setPrepared(true);
                    }else {
                        user.setPrepared(false);
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }


    public interface VolleyCallback{
        void onSuccess(String result);
    }
    public void GetTaskData(final VolleyCallback callback){
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
        Mysingleton.getmInstance(context).AddToRequestQue(stringRequest);
    }
    /*
    public String getImageID() {
        imageID=sharedPreferences.getString(encrypt("imageID"),encrypt(""));
        return decrypt(imageID);
    }

    public void setImageID(String imageID) {
        sharedPreferences.edit().putString(encrypt("imageID"),encrypt(imageID)).commit();
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


    public String encrypt(String input) {
        // This is base64 encoding, which is not an encryption
        return Base64.encodeToString(input.getBytes(),Base64.DEFAULT);
    }

    public String decrypt(String input) {
        return new String(Base64.decode(input, Base64.DEFAULT));
    }
    */
}
