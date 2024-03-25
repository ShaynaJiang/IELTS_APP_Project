package com.example.gproject.WordQuiz;

import android.content.DialogInterface;
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

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gproject.R;
import com.example.gproject.adapter.WordQuizAdapter;
import com.example.gproject.adapter.WordQuizData;
import com.example.gproject.meaning.DataHolder;
import com.example.gproject.reading.R_topic;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class LevelCQuizActivity extends AppCompatActivity {
    private static final String TAG = "R_word";
    private RecyclerView recyclerView;
    private WordQuizAdapter adapter;
    private List<WordQuizData> dataList;
    private FirebaseFirestore db;
    private Handler handler = new Handler(Looper.getMainLooper());
    private List<WordQuizData> questionsList;
    private RadioGroup RG1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pra_word);

        recyclerView = findViewById(R.id.rcyQ);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        questionsList = new ArrayList<>();
        adapter = new WordQuizAdapter(this, questionsList);
        recyclerView.setAdapter(adapter);

        db = FirebaseFirestore.getInstance();

        ImageButton backButton = findViewById(R.id.back);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LevelCQuizActivity.this, R_topic.class);
                startActivity(intent);
                finish();
            }
        });

        Button wordSendButton = findViewById(R.id.wordSend);
        wordSendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int score = 0;
                RadioGroup RG1 = findViewById(R.id.radioGroup);
                TextView QueWord = findViewById(R.id.Que);
                // 获取用户选择的答案
                String selectedAnswer = "";
                int selectedRadioButtonId = RG1.getCheckedRadioButtonId();
                if (selectedRadioButtonId != -1) {
                    RadioButton selectedRadioButton = findViewById(selectedRadioButtonId);
                    selectedAnswer = selectedRadioButton.getText().toString();
                }

                // 遍历所有单词并检查答案
                for (int i = 0; i < adapter.getItemCount(); i++) {
                    WordQuizData word = adapter.getQuestions().get(i);
                    String questionText = word.getDefinition() + " " + word.getPartOfSpeech();
                    if (questionText.equals(QueWord.getText().toString())) {
                        if (selectedAnswer.equals(word.getCorrectWord())) {
                            // 回答正确
                            score++;
                        } else {
                            // 回答错误，标记正确答案为红色
                            adapter.markCorrectAnswer(i);
                        }
                        break;
                    }
                }
                showScoreDialog(score);
            }
        });
        // Get random questions and options from Firestore
        getRandomQuestionAndOptions();
    }

    private void showScoreDialog(int score) {
        AlertDialog.Builder builder = new AlertDialog.Builder(LevelCQuizActivity.this);
        builder.setTitle("Your Score");
        builder.setMessage("You scored " + score + " out of " + adapter.getQuestions().size());
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
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
                        Log.e("ReadDefinitions2", "成功匹配5");

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

    private void getRandomQuestionAndOptions() {
        db.collection("R_wordC")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        try {

                            if (task.isSuccessful()) {
                                List<WordQuizData> wordList = new ArrayList<>();
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    String word = document.getId();
                                    String meaning = document.getString("meaning");
                                    String pos = document.getString("pos");
                                    wordList.add(new WordQuizData(meaning, pos, word, word));
                                }
                                setRandomQuestionAndOptions(wordList);
                            } else {
                                Toast.makeText(LevelCQuizActivity.this, "Failed to fetch data", Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            Log.e("FireStore", "Failed with error: " + e.getMessage());
                        }
                    }
                });
    }

    public void setRandomQuestionAndOptions(List<WordQuizData> wordList) {
        try {
            if (wordList.size() >= 3) {
                int numberOfQuestions = 3;
                for (int i = 0; i < numberOfQuestions; i++) {
                    // Select a random word as the question
                    WordQuizData questionWord = wordList.get(new Random().nextInt(wordList.size()));

                    // 從剩下的單詞中隨機選擇一個作為選項
                    List<WordQuizData> optionWords = new ArrayList<>(wordList);
                    optionWords.remove(questionWord);
                    Collections.shuffle(optionWords);
                    WordQuizData option = optionWords.get(0);

                    // 隨機將正確答案和錯誤答案放入 A1 或 A2 中
                    boolean isCorrectOptionFirst = new Random().nextBoolean();
                    String correctOption;
                    String incorrectOption;
                    if (isCorrectOptionFirst) {
                        correctOption = questionWord.getCorrectWord();
                        incorrectOption = option.getIncorrectWord();
                    } else {
                        correctOption = option.getCorrectWord();
                        incorrectOption = questionWord.getIncorrectWord();
                    }
                    String pos = questionWord.getPartOfSpeech();
                    // 設置題目和選項
                    String questionText = questionWord.getDefinition() + " " + pos;

//                adapter.getQuestions().add(new WordQuizData(questionText, correctOption, incorrectOption));
                    adapter.getQuestions().add(new WordQuizData(questionText, pos, correctOption, incorrectOption));
                }
                adapter.notifyDataSetChanged();
            } else {
                Toast.makeText(this, "Insufficient number of words to conduct quiz", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("FireStore2", "Failed with error: " + e.getMessage());
        }
    }
}

