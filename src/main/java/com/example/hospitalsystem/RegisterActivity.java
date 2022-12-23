package com.example.hospitalsystem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    private TextInputEditText Name, Email, Phone, Pass, ConfirmPass;
    private Button btnDangKy;
    private TextView DangNhap;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        Name = findViewById(R.id.name);
        Email = findViewById(R.id.email);
        Phone = findViewById(R.id.phone);
        Pass = findViewById(R.id.pass1);
        ConfirmPass = findViewById(R.id.pass2);
        btnDangKy = findViewById(R.id.btnDK);
        DangNhap = findViewById(R.id.tvDN);
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        btnDangKy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerUser();
            }
        });

        DangNhap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
            }
        });
    }

    private void registerUser()
    {
        String name = Name.getText().toString();
        String phone = Phone.getText().toString();
        String email = Email.getText().toString();
        String pass = Pass.getText().toString();
        String pass2 = ConfirmPass.getText().toString();

        if(TextUtils.isEmpty(name)){
            Name.setError("Tên không được để trống!");
            Name.requestFocus();
        }
        else if(TextUtils.isEmpty(phone)){
            Phone.setError("Số điện thoại không được để trống!");
            Phone.requestFocus();
        }
        else if(phone.length() != 10){
            Phone.setError("Số điện thoại không hợp lệ!");
            Phone.requestFocus();
        }
        else if(TextUtils.isEmpty(email)){
            Email.setError("Email không được để trống!");
            Email.requestFocus();
        }
        else if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Email.setError("Email không hợp lệ!");
            Email.requestFocus();
        }
        else if(TextUtils.isEmpty(pass)){
            Pass.setError("Mật khẩu không được để trống!");
            Pass.requestFocus();
        }
        else if(pass.length() < 6) {
            Pass.setError("Mật khẩu phải từ 6 kí tự trở lên!");
            Pass.requestFocus();
        }
        else if(TextUtils.isEmpty(pass2)) {
            ConfirmPass.setError("Chưa xác nhận mật khẩu!");
            ConfirmPass.requestFocus();
        }
        else if(!pass.equals(pass2)) {
            Pass.setError("Mật khẩu không khớp!");
            Pass.requestFocus();
        }
        else
        {
            mAuth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful())
                    {
                        Map<String, Object> userDetail = new HashMap<>();
                        userDetail.put("name", name);
                        userDetail.put("email",email);
                        userDetail.put("bd", "");
                        userDetail.put("phone", phone);
                        userDetail.put("address", "");
                        userDetail.put("cccd", "");
                        userDetail.put("avt", "content://com.android.providers.media.documents/document/image%3A31");

                        db.collection("Users").add(userDetail)
                                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                    @Override
                                    public void onSuccess(DocumentReference documentReference) {
                                        Toast.makeText(RegisterActivity.this, "Đăng ký thành công!", Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(RegisterActivity.this, MainActivity.class));
                                    }
                                });

                    }
                    else
                    {
                        Toast.makeText(RegisterActivity.this, "Đăng ký thất bại: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });


        }
    }
}