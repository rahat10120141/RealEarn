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
                //Log.i("rahat",result);
                try {
                    //user.removeSettings();
                    JSONArray jsonArray=new JSONArray(result);
                    JSONObject jsonObject=jsonArray.getJSONObject(0);
                    user.setImageID(jsonObject.getString("iai"));
                    user.setVideoID(jsonObject.getString("vai"));
                    user.setAd_waiting_time(jsonObject.getString("awt"));
                    user.setAdd_delay(jsonObject.getString("ad"));
                    user.setAdd_per_session(jsonObject.getString("aps"));
                    user.setClick_per_session(jsonObject.getString("cps"));
                    user.setClickIndexes(jsonObject.getString("ci"));
                    user.setvideoIndexes(jsonObject.getString("vi"));
                    user.setImage_ids_for_click(jsonObject.getString("iifc"));
                    //user.setvideoIndexes("0222");
                    user.setClickReturnTime(jsonObject.getString("crt"));
                    user.setAppID(jsonObject.getString("ai"));
                    user.setContent_urls(jsonObject.getString("content_urls"));
                    user.setPrepared(true);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        GetAppSettings(new VolleyCallback() {
            @Override
            public void onSuccess(String result) {
                //Log.i("rahat",result);
                JSONArray jsonArray= null;
                try {
                    jsonArray = new JSONArray(result);
                    JSONObject jsonObject=jsonArray.getJSONObject(0);
                    if(jsonObject.getString("at").equals("1")){
                        user.setAutoTask(true);
                        //Log.i("rahat","autotask: "+Boolean.toString(user.isAutoTask()));
                    }else{
                        user.setAutoTask(false);
                        //Log.i("rahat","autotask: "+Boolean.toString(user.isAutoTask()));
                    }

                    if(jsonObject.getString("fa").equals("1")){
                        user.setFacebook_ad(true);
                        //Log.i("rahat","Facebook: "+Boolean.toString(user.isFacebook_ad()));
                    }else{
                        user.setFacebook_ad(false);
                        //Log.i("rahat","Facebook: "+Boolean.toString(user.isFacebook_ad()));
                    }

                    if(jsonObject.getString("rc").equals("1")){
                        user.setRemember_counters(true);
                        //Log.i("rahat","remember_counters: "+Boolean.toString(user.Remember_counters()));
                    }else{
                        user.setRemember_counters(false);
                        //Log.i("rahat","remember_counters: "+Boolean.toString(user.Remember_counters()));
                    }

                    if(jsonObject.getString("sl").equals("1")){
                        user.setShowingLog(true);
                        //Log.i("rahat","show_log: "+Boolean.toString(user.isShowingLog()));
                    }else{
                        user.setShowingLog(false);
                        //Log.i("rahat","show_log: "+Boolean.toString(user.isShowingLog()));
                    }
                    if(jsonObject.getString("ppfs").equals("1")){
                        user.setPrevent_phone_from_sleep(true);
                        //Log.i("rahat","prevent_phone_from_sleep: "+Boolean.toString(user.isPrevent_phone_from_sleep()));
                    }else{
                        user.setPrevent_phone_from_sleep(false);
                        //Log.i("rahat","prevent_phone_from_sleep: "+Boolean.toString(user.isPrevent_phone_from_sleep()));
                    }
                    if(jsonObject.getString("wfa").equals("1")){
                        user.setWait_for_action(true);
                        //Log.i("rahat","wait_for_action: "+Boolean.toString(user.isWait_for_action()));
                    }else{
                        user.setWait_for_action(false);
                        //Log.i("rahat","wait_for_action: "+Boolean.toString(user.isWait_for_action()));
                    }
                    if(jsonObject.getString("avpn").equals("1")){
                        user.setVpnAllowed(true);
                    }else{
                        user.setVpnAllowed(false);
                    }
                    if(jsonObject.getString("ba").equals("1")){
                        user.setBreakAllowed(true);
                    }else{
                        user.setBreakAllowed(false);
                    }
                    user.setMaximumFraudPerSession(jsonObject.getString("mfps"));
                    user.setMaximumFraudPerDay(jsonObject.getString("mfpd"));
                    user.setSettingLoaded(true);

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

    public void GetAppSettings(final VolleyCallback callback){
        StringRequest stringRequest=new StringRequest(Request.Method.POST, Appurls.appSettings, new Response.Listener<String>() {
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

}
