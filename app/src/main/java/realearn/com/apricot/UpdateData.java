package realearn.com.apricot;

import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by LSE on 4/9/2018.
 */

public class UpdateData {

    StringRequest stringRequest;
    Context context;
    String uid;
    User user;

    //AlertDialog.Builder builder;
    public UpdateData(Context context){
        this.context=context;
        user=new User(context);
        uid=user.getuId();
        //builder=new AlertDialog.Builder(context);
    }

    public void ProcessInterstitialAdd(final int adCount,final String activityType){
            stringRequest=new StringRequest(Request.Method.POST, Appurls.image_add_view_success, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONArray jsonArray=new JSONArray(response);
                        JSONObject jsonObject=jsonArray.getJSONObject(0);
                        String code=jsonObject.getString("code");
                    }catch (JSONException e){

                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            }){
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String,String> params=new HashMap<String, String>();
                    params.put("uid",uid);
                    params.put("activity_type",activityType);
                    return params;
                }
            };
            Mysingleton.getmInstance(context).AddToRequestQue(stringRequest);
    }
    public void ProcessVideoAdd(final int adCount){
        stringRequest=new StringRequest(Request.Method.POST, Appurls.video_add_view_success, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray=new JSONArray(response);
                    JSONObject jsonObject=jsonArray.getJSONObject(0);
                    String code=jsonObject.getString("code");
                }catch (JSONException e){

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params=new HashMap<String, String>();
                params.put("uid",uid);
                params.put("activity_type","view");
                return params;
            }
        };
        Mysingleton.getmInstance(context).AddToRequestQue(stringRequest);
    }
    public void UpdateFraud(){
        stringRequest=new StringRequest(Request.Method.POST, Appurls.updateFraudCount, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray=new JSONArray(response);
                    JSONObject jsonObject=jsonArray.getJSONObject(0);
                    String code=jsonObject.getString("code");
                }catch (JSONException e){

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params=new HashMap<String, String>();
                 params.put("uid",uid);
                return params;
            }
        };
        Mysingleton.getmInstance(context).AddToRequestQue(stringRequest);
    }
    public void UpdateDeviceInfoLog(final String deviceId,final String deviceName,final String androidVersion){
        stringRequest=new StringRequest(Request.Method.POST, Appurls.updateDailyUserLog, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray=new JSONArray(response);
                    JSONObject jsonObject=jsonArray.getJSONObject(0);
                    String code=jsonObject.getString("code");
                }catch (JSONException e){

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params=new HashMap<String, String>();
                params.put("uid",uid);
                params.put("device_id",deviceId);
                params.put("device_model",deviceName);
                params.put("android_version",androidVersion);
                return params;
            }
        };
        Mysingleton.getmInstance(context).AddToRequestQue(stringRequest);
    }
    public void UpdateBreak(){
        stringRequest=new StringRequest(Request.Method.POST, Appurls.submit_break_time, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray=new JSONArray(response);
                    JSONObject jsonObject=jsonArray.getJSONObject(0);
                    String code=jsonObject.getString("code");
                }catch (JSONException e){

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params=new HashMap<String, String>();
                params.put("uid",uid);
                params.put("break_status","1");
                return params;
            }
        };
        Mysingleton.getmInstance(context).AddToRequestQue(stringRequest);
    }

    /*
    public void DisplayAlert(final String code){
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if(code.equals("illegal_view")){
                    builder.setTitle("Illegal View");
                    builder.setMessage("Please Open The Add At least 5 Second");
                }
                if (code.equals("video_illegal_view")){
                    builder.setTitle("Illegal View");
                    builder.setMessage("Please View the full video Add");
                }
            }
        });
        AlertDialog alertDialog=builder.create();
        alertDialog.show();
    }*/
}
