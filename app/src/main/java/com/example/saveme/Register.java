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

public class Register extends AppCompatActivity {
    EditText edt_fname,edt_email,edt_password;
    Button btn_register;
    TextView tv_alregister;
    FirebaseAuth fAuth;
    ProgressBar pbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_main);

        edt_fname=(EditText)findViewById(R.id.edt_fname);
        edt_email=(EditText)findViewById(R.id.edt_email);
        edt_password=(EditText)findViewById(R.id.edt_password);
        btn_register=(Button)findViewById(R.id.btn_register);
        tv_alregister=(TextView)findViewById(R.id.tv_alregister);
        fAuth= FirebaseAuth.getInstance();
        pbar=(ProgressBar)findViewById(R.id.progressBar);

        if(fAuth.getCurrentUser() != null){
            startActivity(new Intent(getApplicationContext(),App1.class));
            finish();
        }
        else{
            btn_register.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String email = edt_email.getText().toString().trim();
                    String password= edt_password.getText().toString().trim();

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
                    pbar.setVisibility(view.VISIBLE);

                    //REGISTERING  IN FIREBASE

                    fAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(Register.this,"User is Created",Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(getApplicationContext(),App1.class));
                                pbar.setVisibility(View.INVISIBLE);
                            }
                            else{
                                Toast.makeText(Register.this,"Error !"+task.getException().getMessage(),Toast.LENGTH_SHORT);
                                pbar.setVisibility(View.INVISIBLE);
                            }
                        }
                    });
                }
            });
            tv_alregister.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(getApplicationContext(),Login.class));
                }
            });
        }


    }
}