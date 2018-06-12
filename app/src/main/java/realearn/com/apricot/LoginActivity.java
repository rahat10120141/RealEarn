package realearn.com.apricot;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

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

import realearn.com.apricot.R;

public class LoginActivity extends AppCompatActivity {

    EditText etUserId,etPass;
    Button signIn;
    String mobile,pass;
    AlertDialog.Builder builder;
    String url=Appurls.login_url;
    User user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        user=new User(LoginActivity.this);
        if (user.getuId()!=null){
            startActivity(new Intent(LoginActivity.this,UserWelcome.class));
            finish();
        }

        Log.i("deviceID",Settings.Secure.getString(getContentResolver(),Settings.Secure.ANDROID_ID));
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        etUserId=(EditText) findViewById(R.id.et_userId);
        etPass=(EditText) findViewById(R.id.et_pass);
        signIn=(Button) findViewById(R.id.btn_signIn);
        builder=new AlertDialog.Builder(LoginActivity.this);
        //getApplicationContext().getSharedPreferences("realEarn", 0).edit().clear().commit();
        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mobile=etUserId.getText().toString();
                pass=etPass.getText().toString();
                StringRequest stringRequest=new StringRequest(Request.Method.POST, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    JSONArray jsonArray=new JSONArray(response);
                                    JSONObject jsonObject=jsonArray.getJSONObject(0);
                                    String code=jsonObject.getString("code");
                                    String message=jsonObject.getString("message");
                                    if(code.equals("log_failed")){
                                        builder.setTitle("Error");
                                        builder.setMessage(message);
                                        DisplayAlert(code);
                                    }else{
                                        String uid=jsonObject.getString("user_id");
                                        User user=new User(LoginActivity.this);
                                        user.setuId(uid);
                                        user.setMobile(mobile);
                                        startActivity(new Intent(LoginActivity.this,UserWelcome.class));
                                        finish();
                                    }
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
                        Map<String,String> params=new HashMap<String, String>();
                        String deviceId= Settings.Secure.getString(getContentResolver(),Settings.Secure.ANDROID_ID);
                        params.put("device_id",deviceId);
                        params.put("mobile",mobile);
                        params.put("userPass",pass);
                        return params;
                    }
                };
                Mysingleton.getmInstance(LoginActivity.this).AddToRequestQue(stringRequest);
            }
        });

    }

    public void DisplayAlert(final String code){
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if(code.equals("log_failed")){
                    etUserId.setText("");
                    etPass.setText("");
                }
            }
        });
        AlertDialog alertDialog=builder.create();
        alertDialog.show();
    }

}
