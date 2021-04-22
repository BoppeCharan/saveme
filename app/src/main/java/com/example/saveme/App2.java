package com.example.saveme;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

public class App2 extends AppCompatActivity  {
    private AdView madview;
    private InterstitialAd mInterstitialAd;
    private EditText  edt_key2;
    DatabaseReference reff;;
    private Button  btn_find;
    private String ui;
    private TextView tv_value;
    private byte encrptionkey[]={9,115,51,86,105,4,-31,-23,-68,88,17,20,3,-105,119,-53};
    private Cipher decipher;
    SecretKeySpec secretKeySpec;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_app2);
        edt_key2 = (EditText) findViewById(R.id.edt_key2);
        btn_find = (Button) findViewById(R.id.btn_find);
        tv_value = (TextView) findViewById(R.id.tv_value);

        MobileAds.initialize(this,"ca-app-pub-3908726115058703~9607802297");
        madview=(AdView)findViewById(R.id.adview1);
        AdRequest adRequest= new AdRequest.Builder().build();
        madview.loadAd(adRequest);


        MobileAds.initialize(this,
                "ca-app-pub-3908726115058703~9607802297");

        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId("ca-app-pub-3908726115058703/7695082680");
        mInterstitialAd.loadAd(new AdRequest.Builder().build());

        //uid=firebaseAuth.getUid().toString();

        //final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);

       /* btn_find.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String key = edt_key2.getText().toString().trim();
                String value = prefs.getString(key, "Not Found The Value!");
                tv_value.setText(value);
            }
        });*/

       /* mDatabase = FirebaseDatabase.getInstance().getReference().child("Info").child(uid).child(key);
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // String inf=snapshot.getValue().toString();
                //tv_value.setText(inf);
                Toast.makeText(App2.this,"hey",Toast.LENGTH_LONG).show();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });*/
        secretKeySpec = new SecretKeySpec(encrptionkey,"AES");
        try {
            decipher=Cipher.getInstance("AES");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        }

        btn_find.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               final String key=edt_key2.getText().toString().trim();
               if(TextUtils.isEmpty(key)){
                   edt_key2.setError("Key/Header is Required");
                   return;
               }
               ui = FirebaseAuth.getInstance().getCurrentUser().getUid();
               reff= FirebaseDatabase.getInstance().getReference().child("Info").child(ui).child(key);
               reff.addValueEventListener(new ValueEventListener() {
                   @Override
                   public void onDataChange(@NonNull DataSnapshot snapshot) {
                       if(snapshot.getValue(String.class)!=null){
                           String inf=snapshot.getValue(String.class);
                           try {

                               tv_value.setText(AESDEncryptionMethod(inf));
                           } catch (UnsupportedEncodingException e) {
                             //  tv_value.setText(AESDEncryptionMethod(inf));
                              // e.printStackTrace();
                               tv_value.setText("error bounded");
                           }
                       }
                       else{
                           tv_value.setText("Not Found");
                       }
                   }
                   @Override
                   public void onCancelled(@NonNull DatabaseError error) {

                   }
               });
           }

            private String AESDEncryptionMethod(String inf) throws UnsupportedEncodingException {
                byte[] EncryptedByte = inf.getBytes("ISO-8859-1");
                String decryptedstring =inf;
                byte[] decryption;

                    try {
                        //Toast.makeText(App2.this,"hey",Toast.LENGTH_LONG);
                        decipher.init(Cipher.DECRYPT_MODE,secretKeySpec);
                        decryption=decipher.doFinal(EncryptedByte);
                        decryptedstring= new String(decryption);
                    } catch (InvalidKeyException e) {
                        e.printStackTrace();
                    } catch (BadPaddingException e) {
                        e.printStackTrace();
                    } catch (IllegalBlockSizeException e) {
                        e.printStackTrace();
                    }

                    return decryptedstring;

            }
        });

    }
    @Override
    public void onBackPressed() {
        if(mInterstitialAd.isLoaded()){
            mInterstitialAd.show();

            mInterstitialAd.setAdListener(new AdListener(){
                @Override
                public void onAdClosed() {

                    super.onAdClosed();
                    finish();
                }
            });

        }
        else {
            super.onBackPressed();
        }
    }

}





