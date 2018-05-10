package realearn.com.apricot;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

public class Task_1 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_1);
        GetTaskData(new VolleyCallback(){
            @Override
            public void onSuccess(String result) {
                try {
                    JSONArray jsonArray=new JSONArray(result);
                    JSONObject jsonObject=jsonArray.getJSONObject(0);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
    public interface VolleyCallback{
        void onSuccess(String result);
    }
    public void GetTaskData(final Task_1.VolleyCallback callback){
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
        Mysingleton.getmInstance(Task_1.this).AddToRequestQue(stringRequest);
    }
}
