package com.example.tubes_uas;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.example.tubes_uas.Api.ApiClient;
import com.example.tubes_uas.Api.ApiInterface;
import com.example.tubes_uas.Model.UserDAO;
import com.example.tubes_uas.Model.UserResponse;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LogRegActivity extends AppCompatActivity {
    public static final String TAG = "TAG";
    private TextInputEditText editEmail, editPassword;
    private String emailText="";
    private String passwordText="";
    private MaterialButton signUpBtn, signInBtn;
    private FirebaseAuth firebaseAuth;
    private UserDAO userLogin;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_reg);

        editEmail = findViewById(R.id.email);
        editPassword = findViewById(R.id.password);
        signUpBtn = findViewById(R.id.signUpBtn);
        signInBtn = findViewById(R.id.signInBtn);

        //Firebase Authentitacion
        firebaseAuth = FirebaseAuth.getInstance();

        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String email, password;
                email = editEmail.getText().toString();
                password = editPassword.getText().toString();

                if(TextUtils.isEmpty(email)){
                    Toast.makeText(LogRegActivity.this,"Email invalid", Toast.LENGTH_SHORT).show();
                    return;
                }
                else if(TextUtils.isEmpty(password)){
                    Toast.makeText(LogRegActivity.this,"Password invalid", Toast.LENGTH_SHORT).show();
                    return;
                }
                else if(password.length()<6){
                    Toast.makeText(LogRegActivity.this,"Password kependekan", Toast.LENGTH_SHORT).show();
                    return;
                }

                firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            //Verifikasi Email
                            FirebaseUser fuser = firebaseAuth.getCurrentUser();
                            fuser.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()){
                                        Toast.makeText(LogRegActivity.this, "Cek Email untuk Verifikasi", Toast.LENGTH_SHORT).show();
                                        registerApi();
                                        editEmail.setText("");
                                        editPassword.setText("");
                                    }else {
                                        Toast.makeText(LogRegActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }else {
                            Toast.makeText(LogRegActivity.this, "Gagal", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

        signInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String email, password;
                email = editEmail.getText().toString();
                password = editPassword.getText().toString();

                if(TextUtils.isEmpty(email)){
                    Toast.makeText(LogRegActivity.this,"Email invalid", Toast.LENGTH_SHORT).show();
                    return;
                }
                else if(TextUtils.isEmpty(password)){
                    Toast.makeText(LogRegActivity.this,"Password invalid", Toast.LENGTH_SHORT).show();
                    return;
                }
                else if(password.length()<6){
                    Toast.makeText(LogRegActivity.this,"Password kependekan", Toast.LENGTH_SHORT).show();
                    return;
                }
                firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (firebaseAuth.getCurrentUser().isEmailVerified()){
                            if (email.equals("admin@gmail.com") && password.equals("admin@gmail.com")){
                                if (task.isSuccessful()){
                                    Toast.makeText(LogRegActivity.this,"Admin Mode", Toast.LENGTH_SHORT).show();
                                    Intent i = new Intent(LogRegActivity.this, MainActivity.class);
                                    startActivity(i);
                                }else {
                                    Toast.makeText(LogRegActivity.this, "Gagal", Toast.LENGTH_SHORT).show();
                                }
                            }else {
                                if (task.isSuccessful()) {
                                    loginApi();
                                } else {
                                    Toast.makeText(LogRegActivity.this, "Gagal", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }else {
                            if(email.equals("admin@gmail.com") && password.equals("admin@gmail.com")) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(LogRegActivity.this, "Admin Mode", Toast.LENGTH_SHORT).show();
                                    Intent i = new Intent(LogRegActivity.this, MainActivity.class);
                                    startActivity(i);
                                } else {
                                    Toast.makeText(LogRegActivity.this, "Gagal", Toast.LENGTH_SHORT).show();
                                }
                            }else {
                                Toast.makeText(LogRegActivity.this, "Email belum terverifikasi, cek email", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
            }
        });
    }

    private void loginApi() {
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<UserResponse> call = apiService.loginUser(String.valueOf(editEmail.getText()),
                String.valueOf(editPassword.getText()));

        call.enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                if(response.body().getMessage().equals("Authenticated")) {
                    Toast.makeText(LogRegActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    userLogin = response.body().getUser();
                    final int userLoginId = userLogin.getId();
                    Intent i = new Intent(LogRegActivity.this, ProfileActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putInt("id", userLoginId);
                    i.putExtras(bundle);
                    startActivity(i);
                } else {
                    Toast.makeText(LogRegActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<UserResponse> call, Throwable t) {
                Toast.makeText(LogRegActivity.this, "Kesalahan Jaringan", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });
    }

    private void registerApi() {
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<UserResponse> callRegister = apiService.registerUser(String.valueOf(editEmail.getText()),
                String.valueOf(editPassword.getText()));

        callRegister.enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(Call<UserResponse> callRegister, Response<UserResponse> response) {
                if(response.body().getMessage().equals("Register Success")) {
                    Toast.makeText(LogRegActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(LogRegActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<UserResponse> callRegister, Throwable t) {
                Toast.makeText(LogRegActivity.this, "Kesalahan Jaringan", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });
    }
}