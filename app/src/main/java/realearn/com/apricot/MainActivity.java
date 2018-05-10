package realearn.com.apricot;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.RequestQueue;

import realearn.com.apricot.R;

public class MainActivity extends AppCompatActivity {

    Button login_btn,register_btn;
    TextView txt;
    RequestQueue requestQueue;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        User user=new User(MainActivity.this);
        login_btn=(Button)findViewById(R.id.btn_login);
        register_btn=(Button)findViewById(R.id.btn_register);
        txt=(TextView) findViewById(R.id.response);
        if (user.getuId()!=null){
            finish();
            startActivity(new Intent(MainActivity.this,UserWelcome.class));
        }else {
            login_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(MainActivity.this,LoginActivity.class));
                }
            });
            register_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(MainActivity.this,RegisterActivity.class));
                }
            });
        }

    }
}
