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

public class R_match extends AppCompatActivity {
    private static final String TAG = "R_match"; // 用你的类名替代 YourClassName

    @Override


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.r_match);

        ImageButton backButton = findViewById(R.id.back);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 在这里添加返回逻辑
                Intent intent = new Intent(R_match.this, R_topic.class);
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
        DocumentReference documentRef = db.collection("R_match").document("1");

        // 从 Reading 集合中的 Document ID 2 中读取数据
        documentRef.get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            //q
                            //options
                            if (document.exists()) {
                                //setContent
                                String content = document.getString("content");
                                String[] splitCont = content.split("\\n");
                                StringBuilder combinedContent = new StringBuilder();
                                for (String line : splitCont) {
                                    combinedContent.append(line).append("\n");
                                }
                                TextView ContentTextView = findViewById(R.id.Content);
                                ContentTextView.setText(combinedContent.toString());
                                //set section
                                String section1 = document.getString("section1");
                                String[] splitSec = section1.split("\\.");
                                StringBuilder combinedSec = new StringBuilder();
                                for (String line : splitSec) {
                                    combinedSec.append(line).append("\n");
                                }
                                TextView SecTextView = findViewById(R.id.section1);
                                SecTextView.setText(combinedSec.toString());
                                //set match
                                String match = document.getString("match");
                                String[] splitMatch = match.split("\\.");
                                StringBuilder combinedMatch = new StringBuilder();
                                for (String line : splitMatch) {
                                    combinedMatch.append(line).append(".\n");
                                }
                                TextView MatchTextView = findViewById(R.id.match);
                                MatchTextView.setText(combinedMatch.toString());

                               // 假設您要抓取的欄位數量是3
                                int numberOfFields = 5;
                                // 創建一個 StringBuilder 陣列來保存結果
                                StringBuilder[] resultBuilders = new StringBuilder[numberOfFields];
                                StringBuilder[] resultBuilders2 = new StringBuilder[numberOfFields];
                                for (int i = 0; i < numberOfFields; i++) {
                                    resultBuilders[i] = new StringBuilder();
                                    resultBuilders2[i] = new StringBuilder();
                                }

                                for (int i = 0; i < numberOfFields; i++) {

                                    String titleName = "title" + (i + 1); // 組合欄位名稱，例如 Q1、Q2、Q3
                                    String titleData = document.getString(titleName);

                                    String fieldName = "Q" + (i + 1); // 組合欄位名稱，例如 Q1、Q2、Q3
                                    String fieldData = document.getString(fieldName);
                                    Toast.makeText(R_match.this, fieldName, Toast.LENGTH_SHORT).show();

                                    String ansName = "A" + (i + 1);
                                        try {
                                    int questionId = getResources().getIdentifier(fieldName, "id", getPackageName());
                                    int ansId = getResources().getIdentifier(ansName, "id", getPackageName());
                                    int titleId = getResources().getIdentifier(titleName, "id", getPackageName());
                                    //check title exit
                                    if (document.contains(titleName) || document.contains(fieldName)) {
                                        // 调用 toString() 方法

                                        String[] splitTitle = titleData.split("\\.");
                                        resultBuilders[i] = new StringBuilder();
                                        for (int j = 0; j < splitTitle.length; j++) {
                                            resultBuilders[i].append(splitTitle[j].trim()).append(".\n");
                                        }
                                        displayTitle(titleName, resultBuilders[i].toString());

//                                    }
//                                    else{
//                                        findViewById(titleId).setVisibility(View.GONE);
//                                    }
//                                    //check Q exit
//                                    if(document.contains(fieldName)) {
                                        String[] splitQue = fieldData.split("\\.");
                                        resultBuilders2[i] = new StringBuilder();
                                        for (int k = 0; k < splitQue.length; k++) {
                                            resultBuilders2[i].append(splitQue[k].trim()).append(".\n");
                                        }
                                        displayQuestion(fieldName, resultBuilders2[i].toString());
                                    }else {
                                        findViewById(questionId).setVisibility(View.GONE);
                                        findViewById(ansId).setVisibility(View.GONE);
                                        findViewById(titleId).setVisibility(View.GONE);


                                    }} catch (Exception e) {
                                    e.printStackTrace();
//                                    Toast.makeText(R_match.this, fieldName, Toast.LENGTH_SHORT).show();
                                    // 处理异常，例如记录日志或者其他操作
                                }
                                }

                                Toast.makeText(R_match.this, "faile45", Toast.LENGTH_SHORT).show();

                            } else {
                                Log.d(TAG, "No such document");
                                Toast.makeText(R_match.this, "failed33", Toast.LENGTH_SHORT).show();

                            }
                        } else {
                            Log.e(TAG, "Error getting document", task.getException());
                            Toast.makeText(R_match.this, "failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
    private void displayQuestion(String cul,String que){
        TextView optTextView = null;
        switch (cul) {
            case "Q1":
                optTextView = findViewById(R.id.Q1);
                break;
            case "Q2":
                optTextView = findViewById(R.id.Q2);
                break;
            case "Q3":
                optTextView = findViewById(R.id.Q3);
                break;
            case "Q4":
                optTextView = findViewById(R.id.Q4);
                break;
            case "Q5":
                optTextView = findViewById(R.id.Q5);
                break;

            default:
//                Toast.makeText(R_match.this, cul, Toast.LENGTH_SHORT).show();

                break;
        }

        if (optTextView != null) {
            optTextView.setText(que);
        } else {
            Log.e(TAG, "OptTextView is null for cul: " + cul);
        }
    }
    private void displayTitle(String cul,String que){
        TextView titleTextview = null;
        switch (cul){
            case "title1":
                titleTextview = findViewById(R.id.title1);
                break;
            case "title2":
                titleTextview = findViewById(R.id.title2);
                break;
            default:
                Toast.makeText(R_match.this, cul, Toast.LENGTH_SHORT).show();

                break;
        }
        if (titleTextview != null) {
            titleTextview.setText(que);
        } else {
            Log.e(TAG, "OptTextView is null for cul: " + cul);
        }
    }
}

