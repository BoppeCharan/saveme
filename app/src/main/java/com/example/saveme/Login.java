package com.example.saveme;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Login extends AppCompatActivity {
    EditText edt_email,edt_password;
    Button btn_login;
    TextView tv_Nregister,Fpass;
    FirebaseAuth fAuth;
    ProgressBar pbar1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_login);
        edt_email=(EditText)findViewById(R.id.edt_email1);
        edt_password=(EditText)findViewById(R.id.edt_password1);
        tv_Nregister=(TextView) findViewById(R.id.tv_Nregister);
        Fpass=(TextView)findViewById(R.id.Fpass);
        btn_login=(Button)findViewById(R.id.btn_login);
        fAuth=FirebaseAuth.getInstance();
        pbar1=(ProgressBar)findViewById(R.id.progressBar2);

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = edt_email.getText().toString().trim();
                String password = edt_password.getText().toString().trim();

                if(TextUtils.isEmpty(email)){
                    edt_email.setError("Email is Required");
                    return;
                }
                if(TextUtils.isEmpty(password)){
                    edt_password.setError("Password is required");
                    return;
                }
                if(password.length()<6){
                    edt_password.setError("Password must be >= 6 Characters");
                    return;
                }
                pbar1.setVisibility(view.VISIBLE);

                //Auth the User

                fAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if(task.isSuccessful()){
                            Toast.makeText(Login.this,"Login is Sucessful!!",Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(),App1.class));
                            pbar1.setVisibility(View.INVISIBLE);
                        }
                        else{
                            Toast.makeText(Login.this,"Error !"+task.getException().getMessage(),Toast.LENGTH_SHORT);
                            pbar1.setVisibility(View.INVISIBLE);
                        }

                    }
                });
            }
        });

        tv_Nregister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),Register.class));
            }
        });
        Fpass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email=edt_email.getText().toString().trim();
                if(TextUtils.isEmpty(email)){
                    edt_email.setError("Email is Required");
                    return;
                }
                fAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(Login.this,"Reset Mail Sent",Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(Login.this,"Error !"+task.getException().getMessage(),Toast.LENGTH_SHORT);
                        }
                    }
                });
            }
        });

    }
}