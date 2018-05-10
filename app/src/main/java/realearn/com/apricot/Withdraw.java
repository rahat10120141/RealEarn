package realearn.com.apricot;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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

public class Withdraw extends AppCompatActivity {
    StringRequest stringRequest;
    Button submit;
    TextView mobile;
    EditText amount;//conMobile
    String uid;
    User user;

    AlertDialog.Builder builder;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_withdraw);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        Toast.makeText(getApplicationContext(), "After Getting Payment Please Submit Your Payment Proof On Facebook Group, otherwise your id will be blocked",
                Toast.LENGTH_LONG).show();
        user=new User(Withdraw.this);
        uid=user.getuId();

        mobile=(TextView) findViewById(R.id.moobileNumber);
        //conMobile=(EditText)findViewById(R.id.confMobile);
        amount=(EditText) findViewById(R.id.txtWithdraw);
        submit=(Button) findViewById(R.id.withdrawSubmit);

        mobile.setText(user.getMobile());

        builder=new AlertDialog.Builder(Withdraw.this);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SendWithdrawRequest();
            }
        });
    }

    public void SendWithdrawRequest(){

        if (amount.getText().toString().equals("") || mobile.getText().toString().equals("")/* || conMobile.getText().toString().equals("") */){
            DisplayAlert("input_error");
        }else {
            int am=Integer.parseInt(amount.getText().toString());
            if(am<10){
                DisplayAlert("low_balance");
            }
            if (am%10!=0){
                DisplayAlert("illigal_formate");
            }
            /*if(!(mobile.getText().toString().equals(conMobile.getText().toString()))){
                DisplayAlert("error_mobile");
            }*/
            else{
                ProcessWithdraw();
            }
        }
       //ProcessWithdraw();
    }
    private void ProcessWithdraw(){

        stringRequest=new StringRequest(Request.Method.POST, Appurls.withdraw_money, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray=new JSONArray(response);
                    JSONObject jsonObject=jsonArray.getJSONObject(0);
                    String code=jsonObject.getString("code");
                    DisplayAlert(code);
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
                params.put("withdraw_amount",amount.getText().toString());
                params.put("activity_type","view");
                params.put("type","recharge");
                params.put("recharge_mobile",mobile.getText().toString());
                return params;
            }
        };
        Mysingleton.getmInstance(Withdraw.this).AddToRequestQue(stringRequest);
    }
    public void DisplayAlert(final String code){
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if(code.equals("input_error")){
                    builder.setTitle("Input Error");
                    builder.setMessage("Please Fill All the Fields");
                }else if (code.equals("error_mobile")){
                    builder.setTitle("Mobile Number");
                    builder.setMessage("Mobile Number Mismatch");
                }else if (code.equals("withdraw_success")){
                    builder.setTitle("Success");
                    builder.setMessage("Your Withdraw Request Successfully Submitted");
                    finish();
                    startActivity(new Intent(Withdraw.this,UserWelcome.class));
                }else if (code.equals("low_balance")){
                    builder.setTitle("Low Balance");
                    builder.setMessage("Your Balance is less than 10 taka");
                }else if(code.equals("illigal_formate")){
                    builder.setTitle("Illegal Formate");
                    builder.setMessage("Your Request Balance Should be like: 10/20/30");
                }else if(code.equals("insufficient_balance")){
                    builder.setTitle("Insufficient Balance");
                    builder.setMessage("Your Requested Balance exceed your current Balance ");
                    amount.setText("");
                }
            }
        });
        AlertDialog alertDialog=builder.create();
        alertDialog.show();
    }
}
