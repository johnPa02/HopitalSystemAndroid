package com.example.hospitalsystem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{
    private TextView register;
    private FirebaseAuth mAuth;
    private Button btn_login;
    private TextInputEditText etEmail;
    private TextInputEditText etPwd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();

        register = findViewById(R.id.register);
        register.setOnClickListener(this);
        btn_login = findViewById(R.id.login);
        etEmail = findViewById(R.id.email);
        etPwd = findViewById(R.id.pwd);
        btn_login.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.register:
                startActivity(new Intent(this,RegisterActivity.class));
                break;
            case R.id.login:
                loginUser();
                break;
        }
    }

    private void loginUser() {
        String email = etEmail.getText().toString();
        String pwd = etPwd.getText().toString();

        if(TextUtils.isEmpty(email)){
            etEmail.setError("Email không được để trống!");
            etEmail.requestFocus();
        }
        else if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etEmail.setError("Email không hợp lệ!");
            etEmail.requestFocus();
        }
        else if(TextUtils.isEmpty(pwd)){
            etPwd.setError("Mật khẩu không được để trống!");
            etPwd.requestFocus();
        }
        else if(pwd.length() < 6) {
            etPwd.setError("Mật khẩu phải từ 6 kí tự trở lên!");
            etPwd.requestFocus();
        }
        else{
            mAuth.signInWithEmailAndPassword(email,pwd).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        Toast.makeText(LoginActivity.this,"Đăng nhập thành công!",Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(LoginActivity.this,MainActivity.class));
                    }
                    else{
                        Toast.makeText(LoginActivity.this,"Nhập sai email hoặc mật khẩu!" ,Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
}