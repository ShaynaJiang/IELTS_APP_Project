package com.example.gproject.reading;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.gproject.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Random;
public class R_judge extends AppCompatActivity {

    private FirebaseFirestore firestore;
    private static final String TAG = "R_judge"; // 用你的类名替代 YourClassName

    @Override


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.r_judge);

        ScrollView scrollView = findViewById(R.id.scrollView);
        scrollView.post(new Runnable() {
            @Override
            public void run() {
                scrollView.fullScroll(View.FOCUS_DOWN); // 将滚动位置移动到底部
            }
        });
        scrollView.fullScroll(ScrollView.FOCUS_UP);

        ImageButton backButton = findViewById(R.id.back);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 在这里添加返回逻辑
                Intent intent = new Intent(R_judge.this, R_topic.class);
                startActivity(intent);
                // 结束当前活动（可选）
                finish();
            }
        });

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        // 获取 Reading 集合中的所有文档 ID
        db.collection("R_judge")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            ArrayList<String> documentIds = new ArrayList<>();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                documentIds.add(document.getId());
                            }

                            // 随机选择一个文档 ID
                            Random random = new Random();
                            String randomDocumentId = documentIds.get(random.nextInt(documentIds.size()));

                            // 使用选定的 ID 从 firestore 检索文档
                            db.collection("R_judge")
                                    .document(randomDocumentId)
                                    .get()
                                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                            if (task.isSuccessful()) {
                                                DocumentSnapshot document = task.getResult();
                                                if (document.exists()) {
                                                    // 获取数据
                                                    String title = document.getString("title");
                                                    String content = document.getString("content");
                                                    String Q1 = document.getString("Q1");
                                                    String Q2 = document.getString("Q2");
                                                    String Q3 = document.getString("Q3");

                                                    // 将数据设置到布局中的相应 TextView 中
                                                    displayArticle(title, content, Q1, Q2, Q3);
                                                } else {
                                                    Log.d(TAG, "No such document");
                                                    Toast.makeText(R_judge.this, "Document does not exist", Toast.LENGTH_SHORT).show();
                                                }
                                            } else {
                                                Log.e(TAG, "Error getting document", task.getException());
                                                Toast.makeText(R_judge.this, "Error getting document", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                        } else {
                            Log.e(TAG, "Error getting documents", task.getException());
                        }
                    }
                });
    }

    private void displayArticle(String title, String content, String Q1, String Q2, String Q3) {
        TextView articleTitleTextView = findViewById(R.id.articleTitle);
        TextView articleContentTextView = findViewById(R.id.articleContent);
        TextView Q1TextView = findViewById(R.id.Q1);
        TextView Q2TextView = findViewById(R.id.Q2);
        TextView Q3TextView = findViewById(R.id.Q3);
        // 将获取的数据设置到相应的 TextView 中
        articleTitleTextView.setText(title);
        articleContentTextView.setText(content);
        Q1TextView.setText(Q1);
        Q2TextView.setText(Q2);
        Q3TextView.setText(Q3);
    }


}
