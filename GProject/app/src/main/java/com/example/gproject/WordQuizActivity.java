//package com.example.gproject;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.view.View;
//import android.widget.ImageButton;
//import android.widget.Toast;
//import android.text.TextUtils;
//
//import androidx.annotation.NonNull;
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.recyclerview.widget.LinearLayoutManager;
//import androidx.recyclerview.widget.RecyclerView;
//
//import com.example.gproject.adapter.WordQuizAdapter;
//import com.example.gproject.adapter.WordQuizData;
//import com.example.gproject.reading.R_topic;
//import com.google.android.gms.tasks.OnCompleteListener;
//import com.google.android.gms.tasks.Task;
//import com.google.firebase.firestore.DocumentReference;
//import com.google.firebase.firestore.DocumentSnapshot;
//import com.google.firebase.firestore.FirebaseFirestore;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Random;
//import java.util.regex.Pattern;
//
//public class WordQuizActivity extends AppCompatActivity {
//    private static final String TAG = "R_word";
//    private RecyclerView recyclerView;
//    private WordQuizAdapter adapter;
//    private List<WordQuizData> dataList;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.pra_word);
//
//        ImageButton backButton = findViewById(R.id.back);
//        backButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // 在这里添加返回逻辑
//                Intent intent = new Intent(WordQuizActivity.this, R_topic.class);
//                startActivity(intent);
//                // 结束当前活动（可选）
//                finish();
//            }
//        });
//
//        // 初始化 RecyclerView 和 adapter
//        recyclerView = findViewById(R.id.rcyQ);
//        adapter = new WordQuizAdapter(new ArrayList<>());
//        recyclerView.setLayoutManager(new LinearLayoutManager(this));
//        recyclerView.setAdapter(adapter);
//
//        // 准备测试数据并设置到 RecyclerView
//        prepareAndSetData();
//    }
//
//    private void prepareAndSetData() {
//        FirebaseFirestore db = FirebaseFirestore.getInstance();
//        DocumentReference documentRef = db.collection("R_word").document("1");
//
//        documentRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//                if (task.isSuccessful()) {
//                    DocumentSnapshot document = task.getResult();
//                    if (document.exists()) {
//                        dataList = new ArrayList<>();
//
//                        for (int i = 1; i <= 5; i++) {
//                            try {
//                                String fieldName = "Q" + i;
//                                String fieldData = document.getString(fieldName);
//
//                                if (fieldData != null) {
//                                    // 分割字段内容
//                                    String[] splitData = fieldData.split("\\.");
//
//                                    // 将英文选项添加到 RecyclerView 中
//                                    List<String> englishOptions = new ArrayList<>();
//                                    List<String> otherOptions = new ArrayList<>();
//                                    for (String option : splitData) {
//                                        String[] parts = option.split(" ");
//                                        for (int j = 0; j < parts.length; j++) {
//                                            if (parts[j].length() > 0 && !Pattern.matches("^[a-zA-Z]*$", parts[j])) {
//                                                otherOptions.add(parts[j].trim());
//                                            } else {
//                                                englishOptions.add(parts[j].trim());
//                                            }
//                                        }
//                                        List<String> yourList = otherOptions;
////                                        String[] yourList =parts;
//                                        String listAsString = TextUtils.join(", ", yourList);
//                                        Toast.makeText(WordQuizActivity.this, listAsString, Toast.LENGTH_SHORT).show();
//                                    }
//                                    // 随机选择一个英文选项添加到 RecyclerView 中
//                                    Random random = new Random();
//                                    int randomIndex = random.nextInt(englishOptions.size());
//                                    String englishOption1 = englishOptions.get(randomIndex);
//                                    englishOptions.remove(randomIndex); // 避免重复选择
//                                    randomIndex = random.nextInt(englishOptions.size());
//                                    String englishOption2 = englishOptions.get(randomIndex);
//                                    // 随机选择一个中文选项添加到 RecyclerView 中
//                                    randomIndex = random.nextInt(otherOptions.size());
//                                    String otherOptions1 = otherOptions.get(randomIndex);
//
//                                    // 随机选择一个中文选项添加到 RecyclerView 中
////                                    String otherOptions1 = splitData[random.nextInt(splitData.length)].split(" ")[0];
//                                    // 创建 WordQuestion 对象并添加到 dataList 中
//                                    WordQuizData question = new WordQuizData("Question " + i + ": "+otherOptions1, englishOption1, englishOption2);
//                                    dataList.add(question);
//                                }
//                            } catch (Exception e) {
//                                e.printStackTrace();
//                            }
//                        }
//                        // 设置 dataList 到 adapter
//                        adapter.setQuestions(dataList);
//                    }
//                }
//            }
//        });
//    }
//}
