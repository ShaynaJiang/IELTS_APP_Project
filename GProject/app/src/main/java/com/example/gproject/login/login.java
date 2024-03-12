package com.example.gproject.login;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.gproject.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class login extends AppCompatActivity {

    private EditText emailEditText, passwordEditText;
    private Button loginButton;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        // 初始化 Firebase Authentication
        mAuth = FirebaseAuth.getInstance();

        // 獲取布局元素
        emailEditText = findViewById(R.id.inputEmail);
        passwordEditText = findViewById(R.id.inputPass);
        loginButton = findViewById(R.id.loginButton);

        // 設置按鈕點擊監聽器
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 在此處調用登錄方法
                loginUser();
            }
        });
    }

    private void loginUser() {
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();

        // 使用 Firebase Authentication 進行登錄
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // 登錄成功
                            Toast.makeText(login.this, "Login successful", Toast.LENGTH_SHORT).show();
                            // 可在此處導航到主畫面或執行其他操作

                        } else {
                            // 登錄失敗
                            Log.e("LoginActivity", "Login failed: " + task.getException().getMessage());
                            Toast.makeText(login.this, "Login failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
