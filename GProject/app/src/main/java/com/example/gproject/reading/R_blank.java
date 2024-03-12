package com.example.gproject.reading;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.gproject.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class R_blank extends AppCompatActivity {
    private static final String TAG = "R_blank"; // 用你的类名替代 YourClassName

    @Override


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.r_blank);

        ImageButton backButton = findViewById(R.id.back);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 在这里添加返回逻辑
                Intent intent = new Intent(R_blank.this, R_topic.class);
                startActivity(intent);
                // 结束当前活动（可选）
                finish();
            }
        });

        ScrollView scrollView = findViewById(R.id.scrollView);
        scrollView.post(new Runnable() {
            @Override
            public void run() {
                scrollView.fullScroll(View.FOCUS_UP); // 将滚动位置移动到top
            }
        });
        scrollView.fullScroll(ScrollView.FOCUS_UP);

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference documentRef = db.collection("R_blank").document("1");

        // 从 Reading 集合中的 Document ID 2 中读取数据
        documentRef.get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                // 文档存在，获取其中的数据
//                                String title = document.getString("title");
                                String content = document.getString("content");
                                String Q1 = document.getString("Q1");
//                                String Q2 = document.getString("Q2");
//                                String Q3 = document.getString("Q3");
                                // 将数据设置到布局中的相应 TextView 中
                                displayArticle(content, Q1);
                                Toast.makeText(R_blank.this, "faile45", Toast.LENGTH_SHORT).show();

                            } else {
                                Log.d(TAG, "No such document");
                                Toast.makeText(R_blank.this, "failed33", Toast.LENGTH_SHORT).show();

                            }
                        } else {
                            Log.e(TAG, "Error getting document", task.getException());
                            Toast.makeText(R_blank.this, "failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
    private void displayArticle(String content, String Q1) {
        TextView articleContentTextView = findViewById(R.id.blankContent);
        TextView Q1TextView = findViewById(R.id.Q1);

        // 将获取的数据设置到相应的 TextView 中
//        articleTitleTextView.setText(title);
        articleContentTextView.setText(content);
        Q1TextView.setText(Q1);
//        Q2TextView.setText(Q2);
//        Q3TextView.setText(Q3);
    }
}
