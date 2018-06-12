package realearn.com.apricot;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.security.MessageDigest;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class TestActivity extends AppCompatActivity {

    EditText textToEncrypt;
    TextView encryptedText,decryptedText;
    Button encrypt,decrypt;
    private static String EncryptedPassword="R@h@^o1830415206";
    String AES="AES";
    String outputString;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        textToEncrypt=(EditText)findViewById(R.id.field);
        encryptedText=(TextView)findViewById(R.id.encryptedText);
        decryptedText=(TextView)findViewById(R.id.decryptedText);

        encrypt=(Button)findViewById(R.id.encrypt);
        decrypt=(Button)findViewById(R.id.decrypt);

        encrypt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    outputString=encrypt(textToEncrypt.getText().toString());
                    encryptedText.setText(outputString);
                } catch (Exception e) {
                    Log.i("message",e.getMessage());
                }
            }
        });
        decrypt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    //outputString=decrypt(outputString);
                    decryptedText.setText(decrypt(textToEncrypt.getText().toString()));
                } catch (Exception e) {
                    Log.i("message",e.getMessage());
                }

            }
        });

    }


    public String encrypt(String input) throws Exception{
        // This is base64 encoding, which is not an encryption
        // For Login Problem Encrytion is turned off
        //return Base64.encodeToString(input.getBytes(), Base64.DEFAULT);


        SecretKeySpec key=generateKey(EncryptedPassword);       // EncryptedPassword is the key for nencryption
        Cipher c=Cipher.getInstance(AES);
        c.init(Cipher.ENCRYPT_MODE,key);
        byte[] encVal=c.doFinal(input.getBytes());
        String encryptedValue= Base64.encodeToString(encVal,Base64.DEFAULT);
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
