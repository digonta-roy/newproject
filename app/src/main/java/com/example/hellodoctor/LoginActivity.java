package com.example.hellodoctor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;

public class LoginActivity extends AppCompatActivity {

    private EditText editTextLoginEmail,editTextLoginPwd;
    private ProgressBar progressBar;
    private FirebaseAuth authProfile;
    private static final String TAG ="LoginActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        getSupportActionBar().setTitle("Login");
        editTextLoginEmail=findViewById(R.id.editText_login_email);
        editTextLoginPwd =findViewById(R.id.editText_login_pwd);
        progressBar =findViewById(R.id.progressBar);
        authProfile =FirebaseAuth.getInstance();

        //Login User
        Button buttonLogin =findViewById(R.id.button_login);
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String textEmail = editTextLoginEmail.getText().toString();
                String textPwd = editTextLoginPwd.getText().toString();
                if (TextUtils.isEmpty(textEmail)) {
                    Toast.makeText(LoginActivity.this, "Please Enter your Email", Toast.LENGTH_SHORT).show();
                    editTextLoginEmail.setError("Email is required");
                    editTextLoginEmail.requestFocus();
                } else if (!Patterns.EMAIL_ADDRESS.matcher(textEmail).matches()) {
                    Toast.makeText(LoginActivity.this, "Please Re-Enter your Email", Toast.LENGTH_SHORT).show();
                    editTextLoginEmail.setError("Valid Email is required");
                    editTextLoginEmail.requestFocus();

                } else if (TextUtils.isEmpty(textPwd)) {
                    Toast.makeText(LoginActivity.this, "Valid Password", Toast.LENGTH_SHORT).show();
                    editTextLoginPwd.setError("Email is required");
                    editTextLoginPwd.requestFocus();
                } else {
                    progressBar.setVisibility(View.VISIBLE);
                    loginUser(textEmail,textPwd);
                }
            }
        });
    }

    private void loginUser(String email, String Pwd) {
        authProfile.signInWithEmailAndPassword(email, Pwd).addOnCompleteListener(LoginActivity.this,new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Toast.makeText(LoginActivity.this, "You are Successfully Logged in", Toast.LENGTH_SHORT).show();
                    Button orderButton = (Button)findViewById(R.id.button_login);
                    orderButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(LoginActivity.this, Homepage.class);
                            startActivity(intent);
                        }
                    });
                }else{
                    try{
                        throw task.getException();

                    } catch(FirebaseAuthInvalidUserException e){
                        editTextLoginEmail.setError("User does not exist or is no longer valid");
                        editTextLoginEmail.requestFocus();
                    }catch(FirebaseAuthInvalidCredentialsException e){
                        editTextLoginEmail.setError("Invalid Credentials.kindly check and re-enter");
                        editTextLoginEmail.requestFocus();
                    }catch(Exception e){
                        Log.e(TAG,e.getMessage());
                        Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }


                }
                progressBar.setVisibility(View.GONE);
            }
        });
    }
}