package realearn.com.apricot;

import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

public class RegisterActivity extends AppCompatActivity {

    EditText et_fn,et_ln,et_mn,et_mi,et_pass,et_cp;
    String firstName,lastName,mobile,referID,pass,ConfPass;
    Button btn_reg;
    AlertDialog.Builder builder;
    String reg_url=Appurls.reg_ur;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        btn_reg=(Button) findViewById(R.id.btn_submit);

        et_fn=(EditText)findViewById(R.id.et_first_name);
        et_ln=(EditText)findViewById(R.id.et_last_name);
        et_mn=(EditText)findViewById(R.id.et_mobile);
        et_mi=(EditText)findViewById(R.id.et_refer);
        et_pass=(EditText)findViewById(R.id.et_pass);
        et_cp=(EditText)findViewById(R.id.et_comPass);

        builder=new AlertDialog.Builder(RegisterActivity.this);
        btn_reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firstName=et_fn.getText().toString();
                lastName=et_ln.getText().toString();
                mobile=et_mn.getText().toString();
                referID=et_mi.getText().toString();
                pass=et_pass.getText().toString();
                ConfPass=et_cp.getText().toString();
                if (firstName.equals("")|| lastName.equals("")|| mobile.equals("")||referID.equals("")||pass.equals("")||ConfPass.equals("")){
                    builder.setTitle("Error!!");
                    builder.setMessage("Please Fill up all the fields");
                    DisplayAlert("input_error");
                }else{
                    if(!(pass.equals(ConfPass))){
                        builder.setTitle("Error!!");
                        builder.setMessage("Password Did not match");
                        DisplayAlert("input_error");
                    }else {
                        StringRequest stringRequest=new StringRequest(Request.Method.POST, reg_url,
                                new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        try {
                                            JSONArray jsonArray=new JSONArray(response);
                                            JSONObject jsonObject=jsonArray.getJSONObject(0);
                                            String code=jsonObject.getString("code");
                                            String message=jsonObject.getString("message");
                                            builder.setTitle("Alert!!");
                                            builder.setMessage(message);
                                            DisplayAlert(code);
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

                                params.put("first_name",firstName);
                                params.put("last_name",lastName);
                                params.put("mobile",mobile);
                                params.put("device_id",deviceId);
                                params.put("mother_mobile",referID);
                                params.put("password",pass);

                                return params;
                            }
                        };
                        Mysingleton.getmInstance(RegisterActivity.this).AddToRequestQue(stringRequest);
                    }
                }
            }
        });

    }

    public void DisplayAlert(final String code){
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if(code.equals("input_error")){
                    et_pass.setText("");
                    et_cp.setText("");
                }else if (code.equals("reg_success")){
                    finish();
                }else if (code.equals("reg_failed")){
                    et_mi.setText("");
                    et_pass.setText("");
                    et_cp.setText("");
                }
            }
        });
        AlertDialog alertDialog=builder.create();
        alertDialog.show();
    }
}
