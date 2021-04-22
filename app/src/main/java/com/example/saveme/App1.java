package com.example.saveme;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

public class App1 extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private AdView madview;
    private InterstitialAd mInterstitialAd;
    private EditText edt_key1, edt_value;
    private String fname;
    private FirebaseAuth fauth;
   // private Firebase firebase;
    private Button btn_save;
    private Toolbar toolbar;
    private ActionBarDrawerToggle toggle;
    private DrawerLayout drawer;
    NavigationView Nview;
    private String uid;
    DatabaseReference reff;
    Info inf;
    private byte Enycrptionkey[]={9,115,51,86,105,4,-31,-23,-68,88,17,20,3,-105,119,-53};
    private Cipher cipher;
    private SecretKeySpec secretKeySpec;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_second);
        edt_key1 = (EditText) findViewById(R.id.edt_key1);
        fauth= FirebaseAuth.getInstance();
        edt_value = (EditText) findViewById(R.id.edt_value);

        btn_save = (Button) findViewById(R.id.btn_save);

        toolbar = (androidx.appcompat.widget.Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);
       // fname=edt_fname.getText().toString();

        drawer = (DrawerLayout) findViewById(R.id.Drawer);
        toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.naigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        //getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
        Nview=(NavigationView)findViewById(R.id.nav_bar);
        Nview.setNavigationItemSelectedListener(this);
        inf=new Info();
        reff= FirebaseDatabase.getInstance().getReference().child("Info");
        uid=fauth.getUid();


        MobileAds.initialize(this,"ca-app-pub-3908726115058703~9607802297");
        madview=(AdView)findViewById(R.id.adview);
        AdRequest adRequest= new AdRequest.Builder().build();
        madview.loadAd(adRequest);

       // presentad();

        MobileAds.initialize(this,
                "ca-app-pub-3908726115058703~9607802297");

        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId("ca-app-pub-3908726115058703/9440227433");
        mInterstitialAd.loadAd(new AdRequest.Builder().build());







        try {
            cipher=Cipher.getInstance("AES");
           // decipher=Cipher.getInstance("AES");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        }

        secretKeySpec = new SecretKeySpec(Enycrptionkey,"AES");

         //final SharedPreferences prefs = getPreferences(Context.MODE_PRIVATE);
        // final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        // final SharedPreferences.Editor medt=prefs.edit();
        btn_save.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                String key = edt_key1.getText().toString().trim();
                String value = edt_value.getText().toString().trim();
                if(TextUtils.isEmpty(key)){
                    edt_key1.setError("Key/Header is Required");
                    return;
                }
                if(TextUtils.isEmpty(value)){
                    edt_value.setError("Infromation is Required");
                    return;
                }
                       // medt.putString(key, value);
                       // medt.commit();
                        inf.setKey(key);
                        inf.setInfo(value);
                        reff.child(uid).child(key).setValue(AESEncryptionMethod(value));
                Toast.makeText(App1.this, "Saved!", Toast.LENGTH_SHORT).show();
                edt_key1.getText().clear();
                edt_value.getText().clear();
            }

            private String AESEncryptionMethod(String value) {
                byte[] stringbyte = value.getBytes();
                byte[] encryptionbyte= new byte[stringbyte.length];

                try {
                    cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);
                    try {
                        encryptionbyte=cipher.doFinal(stringbyte);
                    } catch (BadPaddingException e) {
                        e.printStackTrace();
                    } catch (IllegalBlockSizeException e) {
                        e.printStackTrace();
                    }
                } catch (InvalidKeyException e) {
                    e.printStackTrace();
                }
              String returnstring=null;
                try {
                    returnstring =new String(encryptionbyte,"ISO-8859-1");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
              return returnstring;
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



    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(toggle.onOptionsItemSelected(item)){

        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        int id= menuItem.getItemId();
        if(id== R.id.find){
           // mInterstitialAd.show();
            Toast.makeText(this,"Find!",Toast.LENGTH_SHORT).show();
            startActivity(new Intent(getApplicationContext(),App2.class));
        }
        if(id== R.id.about){
            startActivity(new Intent(getApplicationContext(),About.class));
            Toast.makeText(this,"About!",Toast.LENGTH_SHORT).show();
        }
        if(id== R.id.logout){
            fauth.signOut();
            startActivity(new Intent(getApplicationContext(),Login.class));
            Toast.makeText(this,"LogOut!",Toast.LENGTH_SHORT).show();
        }
        return false;
    }

}

