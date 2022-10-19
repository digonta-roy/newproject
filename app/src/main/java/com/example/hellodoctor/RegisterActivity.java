package com.example.hellodoctor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class RegisterActivity extends AppCompatActivity {

    private EditText editTextRegisterFullname,editTextRegisterEmail,editTextRegisterDob,editTextRegisterMobile,
    editTextRegisterPwd,editTextRegisterConfirmPwd;
    private ProgressBar progressBar;
    private RadioGroup radioGroupRegisterGender;
    private RadioButton radioButtonRegisterGenderSelected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        getSupportActionBar().setTitle("Register");


        Toast.makeText(RegisterActivity.this, "you can register now", Toast.LENGTH_LONG).show();

        progressBar=findViewById(R.id.progressBar);
        editTextRegisterFullname = findViewById(R.id.editText_register_full_name);
        editTextRegisterEmail = findViewById(R.id.editText_register_email);
        editTextRegisterDob = findViewById(R.id.editText_register_dob);
        editTextRegisterMobile = findViewById(R.id.editText_register_mobile);
        editTextRegisterPwd = findViewById(R.id.editText_register_password);
        editTextRegisterConfirmPwd = findViewById(R.id.editText_register_confirm_password);

        //Radio button for Gender
        radioGroupRegisterGender = findViewById(R.id.radio_group_register_gender);
        radioGroupRegisterGender.clearCheck();

        Button buttonRegister = findViewById(R.id.button_register);
        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int selectedGenderId = radioGroupRegisterGender.getCheckedRadioButtonId();
                radioButtonRegisterGenderSelected = findViewById(selectedGenderId);

                //Obtain the entered data
                String textFullName = editTextRegisterFullname.getText().toString();
                String textEmail = editTextRegisterEmail.getText().toString();
                String textDob = editTextRegisterDob.getText().toString();
                String textMobile = editTextRegisterMobile.getText().toString();
                String textPwd = editTextRegisterPwd.getText().toString();
                String textConfirmPwd = editTextRegisterConfirmPwd.getText().toString();
                String textGender;

                if (TextUtils.isEmpty(textFullName)) {
                    Toast.makeText(RegisterActivity.this, "enter name", Toast.LENGTH_LONG).show();
                    editTextRegisterFullname.setError("Full name is required");
                    editTextRegisterFullname.requestFocus();
                } else if (TextUtils.isEmpty(textEmail)) {
                    Toast.makeText(RegisterActivity.this, "enter Email", Toast.LENGTH_LONG).show();
                    editTextRegisterEmail.setError("Email is required");
                    editTextRegisterEmail.requestFocus();
                } else if (!Patterns.EMAIL_ADDRESS.matcher(textEmail).matches()) {
                    Toast.makeText(RegisterActivity.this, "Re-enter Email", Toast.LENGTH_LONG).show();
                    editTextRegisterEmail.setError("valid Email is required");
                    editTextRegisterEmail.requestFocus();

                } else if (TextUtils.isEmpty(textDob)) {
                    Toast.makeText(RegisterActivity.this, "enter D O B", Toast.LENGTH_LONG).show();
                    editTextRegisterDob.setError("dob is required");
                    editTextRegisterDob.requestFocus();
                } else if (radioGroupRegisterGender.getCheckedRadioButtonId() == -1) {
                    Toast.makeText(RegisterActivity.this, "Select Gender", Toast.LENGTH_LONG).show();
                    radioButtonRegisterGenderSelected.setError("Gender is required");
                    radioButtonRegisterGenderSelected.requestFocus();
                } else if (TextUtils.isEmpty(textMobile)) {
                    Toast.makeText(RegisterActivity.this, "enter Mobile", Toast.LENGTH_LONG).show();
                    editTextRegisterMobile.setError("Mobile No. is required");
                    editTextRegisterMobile.requestFocus();
                } else if (textMobile.length() != 10) {
                    Toast.makeText(RegisterActivity.this, "enter 10 digits", Toast.LENGTH_LONG).show();
                    editTextRegisterMobile.setError("should be 10 digits");
                    editTextRegisterMobile.requestFocus();
                } else if (TextUtils.isEmpty(textPwd)) {
                    Toast.makeText(RegisterActivity.this, "enter Password", Toast.LENGTH_LONG).show();
                    editTextRegisterPwd.setError("password is required");
                    editTextRegisterPwd.requestFocus();

                } else if (textPwd.length() < 6) {
                    Toast.makeText(RegisterActivity.this, "enter strong password", Toast.LENGTH_LONG).show();
                    editTextRegisterPwd.setError("Password too weak");
                    editTextRegisterPwd.requestFocus();
                } else if (TextUtils.isEmpty(textConfirmPwd)) {
                    Toast.makeText(RegisterActivity.this, "enter confirm Password", Toast.LENGTH_LONG).show();
                    editTextRegisterConfirmPwd.setError("password confirmation is required");
                    editTextRegisterConfirmPwd.requestFocus();
                } else if (!textPwd.equals(textConfirmPwd)) {
                    Toast.makeText(RegisterActivity.this, "enter same Password", Toast.LENGTH_LONG).show();
                    editTextRegisterConfirmPwd.setError("password confirmation is required");
                    editTextRegisterConfirmPwd.requestFocus();

                    //clear entered passwords
                    editTextRegisterPwd.clearComposingText();
                    editTextRegisterConfirmPwd.clearComposingText();

                } else {
                    textGender = radioButtonRegisterGenderSelected.getText().toString();
                    progressBar.setVisibility(View.VISIBLE);
                    registerUser(textFullName, textEmail, textDob, textGender, textMobile, textPwd);
                }

            }
        });
    }
        private void registerUser(String textFullName, String textEmail,String textDob,String textGender,String textMobile,String textPwd)
        {
            FirebaseAuth auth = FirebaseAuth.getInstance();
            auth.createUserWithEmailAndPassword(textEmail,textPwd).addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        Toast.makeText(RegisterActivity.this,"User registered Successfully",Toast.LENGTH_LONG).show();
                        FirebaseUser firebaseUser =auth.getCurrentUser();

                        //send verification
                        firebaseUser.sendEmailVerification();

                       /* //open user profile after successful regi
                        Intent intent = new Intent(RegisterActivity.this,UserProfileActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish(); // to close register activity */


                    }
                }
            });
        }


    }

