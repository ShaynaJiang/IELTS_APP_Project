package com.example.gproject;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gproject.adapter.WordQuizAdapter;
import com.example.gproject.adapter.WordQuizData;
import com.example.gproject.meaning.DataHolder;
import com.example.gproject.reading.R_topic;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.regex.Pattern;

public class WordQuizActivity2 extends AppCompatActivity {
    private static final String TAG = "R_word";
    private RecyclerView recyclerView;
    private WordQuizAdapter adapter;
    private List<WordQuizData> dataList;
    private FirebaseFirestore db;
    private Handler handler = new Handler(Looper.getMainLooper());
    private boolean isMatched = false;  // 声明 isMatched 变量


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pra_word);

        recyclerView = findViewById(R.id.rcyQ);
        adapter = new WordQuizAdapter(new ArrayList<>());
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
//        handler = new Handler(Looper.getMainLooper());

        ImageButton backButton = findViewById(R.id.back);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WordQuizActivity2.this, R_topic.class);
                startActivity(intent);
                finish();
            }
        });

        // 在按钮点击事件的回调中使用 Handler 运行 UI 操作
        Button wordSendButton = findViewById(R.id.wordSend);
        wordSendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String opt1 = adapter.getSelectedWord().getOpt1();
                String opt2 = adapter.getSelectedWord().getOpt2();

                // 獲取 RecyclerView 中的所有 item 數據
                List<WordQuizData> questions = adapter.getQuestions();
                // 迭代 RecyclerView 中的所有 item，檢查每個 item 的 Que 值是否與選取的 RadioButton 匹配
//                AtomicBoolean foundCorrectAnswer = new AtomicBoolean(false);

                for (int i = 0; i < questions.size(); i++) {
                    String selectedOption = adapter.getSelectedOption(i); //selection
                    String queValue = questions.get(i).getDefinition(); //que

                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    DatabaseReference ref = database.getReference("word_list");

                    ref.child(selectedOption).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (!isMatched && dataSnapshot.exists()) {
                                DataSnapshot definitionsSnapshot = dataSnapshot.child("definitions");
                                if (definitionsSnapshot.exists() && definitionsSnapshot.hasChild(queValue)) {
                                    String definitionsValue = definitionsSnapshot.child(queValue).getValue(String.class);
                                    if (definitionsValue != null && TextUtils.equals(definitionsValue, queValue)) {
                                        Log.e("ReadDefinitions2", "成功匹配" + selectedOption);
                                        // 处理匹配的逻辑
                                        isMatched = true;  // 设置标志为 true，表示已经匹配成功

                                        // 取消对该路径的监听
                                        ref.child(selectedOption).removeEventListener(this);
                                    } else {
                                        // 更新 opt1 对应的单词的 cont 欄位
                                        updateContForWord(opt1);

                                        // 更新 opt2 对应的单词的 cont 欄位
                                        updateContForWord(opt2);
                                        Log.e("ReadDefinitions2", "成功匹配2" + selectedOption);
                                        // 处理不匹配的逻辑
                                    }
                                } else {
                                    Log.e("ReadDefinitions2", "成功匹配3" + selectedOption);
                                    // 处理 "queValue" 不存在的情况
                                }
                            } else {
                                Log.e("ReadDefinitions2", "成功匹配4" + selectedOption);
                                // Handle the case where the key does not exist
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            Log.e("ReadDefinitions", "Failed to read definitions for word: " + selectedOption, databaseError.toException());
                        }
                    });

                }
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        Log.e("ReadDefinitions", "成功更新資料庫");

//                        Toast.makeText(WordQuizActivity2.this, "成功更新資料庫", Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });
        // 准备测试数据并设置到 RecyclerView
        prepareAndSetData();
    }
    // 更新單字的 cont 欄位的方法
    private void updateContForWord(String selectedWord) {
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference root = db.getReference("word_collect");

        root.child(selectedWord).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                try {
                    // Ensure that the dataSnapshot is not null and contains valid data
                    if (dataSnapshot.exists()) {
                        // Get the DataHolder object directly from the snapshot
                        DataHolder data = dataSnapshot.getValue(DataHolder.class);

                        // Check if data is not null
                        if (data != null) {
                            // 更新 cont 欄位
                            int newCont = data.getCont() + 1;
                            dataSnapshot.getRef().child("cont").setValue(newCont);
                            Log.d("ReadDefinitions", "成功更新資料庫");

//                            Toast.makeText(WordQuizActivity2.this, "成功更新資料庫", Toast.LENGTH_SHORT).show();
                        } else {
                            Log.d("ReadDefinitions", "無效的資料");

                            // Handle the case where DataHolder is null
//                            Toast.makeText(WordQuizActivity2.this, "無效的資料", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Log.e("ReadDefinitions2", "成功匹配5" );

                        // Handle the case where the dataSnapshot does not exist
//                        Toast.makeText(WordQuizActivity2.this, "資料不存在", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e("WordQuizActivity2", "Failed with error: " + e.getMessage());
//                    Toast.makeText(WordQuizActivity2.this, "failed3380", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.e("WordQuizActivity2", "更新資料庫失敗");

//                        Toast.makeText(WordQuizActivity2.this, "更新資料庫失敗", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
    private void prepareAndSetData() {
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference root = db.getReference("word_collect");
        root.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                try {
                    List<WordQuizData> wordList = new ArrayList<>();

                    for (DataSnapshot wordSnapshot : dataSnapshot.getChildren()) {
                        String correctWord = wordSnapshot.getKey();
                        String definitions = wordSnapshot.child("definitions").getValue(String.class);
                        WordQuizData wordData = new WordQuizData(definitions, correctWord, "");
                        wordList.add(wordData);
                    }

                    // 檢查 wordList 的大小是否足夠
                    if (wordList.size() >= 3) {
                        // 隨機選取五個問題
                        List<WordQuizData> selectedQuestions = new ArrayList<>();
                        for (int i = 0; i < 5; i++) {
                            // 隨機選取一個正確單字作為問題
                            WordQuizData question = wordList.get(new Random().nextInt(wordList.size()));

                            // 從 wordList 中排除正確單字
                            List<WordQuizData> options = new ArrayList<>(wordList);
                            options.remove(question);

                            // 從剩下的單字中選擇一個作為不正確的單字
                            WordQuizData incorrectWordData = options.get(new Random().nextInt(options.size()));
                            question.setIncorrectWord(incorrectWordData.getCorrectWord());

                            // 添加問題到 selectedQuestions
                            selectedQuestions.add(question);
                        }
                        // 設置問題和選項到 RecyclerView 的 adapter
                        adapter.setRandomQuestionAndOptions(selectedQuestions);
                    } else {
                        Toast.makeText(WordQuizActivity2.this, "Not enough words for quiz", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e("WordCardActivity", "Failed with error: " + e.getMessage());
                    Toast.makeText(WordQuizActivity2.this, "failed33", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(WordQuizActivity2.this, "Failed to fetch words", Toast.LENGTH_SHORT).show();
            }
        });
    }
}

