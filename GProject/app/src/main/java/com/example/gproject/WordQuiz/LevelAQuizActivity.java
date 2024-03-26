package com.example.gproject.WordQuiz;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
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
import com.example.gproject.reading.R_topic;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;


public class LevelAQuizActivity extends AppCompatActivity {
    private FirebaseFirestore db;
    private WordQuizAdapter adapter;

    private String collectionName;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pra_word);

        collectionName = "R_wordA";

        try {
            RecyclerView QuizRecycler = findViewById(R.id.rcyQ);
            List<WordQuizData> questionsList = new ArrayList<>();
            QuizRecycler.setLayoutManager(new LinearLayoutManager(this));
            QuizRecycler.setAdapter(adapter);
            adapter = new WordQuizAdapter(this, questionsList);
            QuizRecycler.setAdapter(adapter);

            db = FirebaseFirestore.getInstance();

            ImageButton backButton = findViewById(R.id.back);
            backButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(LevelAQuizActivity.this, R_topic.class);
                    startActivity(intent);
                    finish();
                }
            });

            Button wordSendButton = findViewById(R.id.wordSend);
            wordSendButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int score = 0;
                    // 遍历 RecyclerView 中的每个单词
                    for (int i = 0; i < adapter.getItemCount(); i++) {
                        try {
                            // 获取当前位置的单词数据
                            WordQuizData word = adapter.getQuestions().get(i);
                            // 获取当前单词的定义和词性
                            String questionText = word.getDefinition() + " " + word.getPartOfSpeech();
                            // 获取当前 RecyclerView 中的 ViewHolder
                            RecyclerView.ViewHolder viewHolder = QuizRecycler.findViewHolderForAdapterPosition(i);
                            if (viewHolder != null) {
                                // 在 ViewHolder 中查找 RadioButton 和 QueWord
                                RadioButton radioButton = adapter.getRadioButtonAtPosition(i, QuizRecycler);
                                TextView QueWord = viewHolder.itemView.findViewById(R.id.Que);
//                                String AAA = selectedRadioButtonText.getDefinition()
                                // 检查用户选择的答案是否与正确答案匹配
                                if (radioButton.isChecked() && questionText.equals(QueWord.getText().toString())) {
                                    // 回答正确
                                    score++;
                                    Log.e("Correct check", QueWord.getText().toString());
                                    Log.e("Correct Answer",  questionText + ", Correct Option: " + radioButton.getText().toString());
                                } else {
                                    // 回答错误，标记正确答案为红色或采取其他操作
                                    Log.e("Incorrect AAA", QueWord.getText().toString());
                                    Log.e("Incorrect Answer", "Word: " + questionText + ", Correct Option: " + radioButton.getText().toString());
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            Log.e("FireStore A2 ", "error: " + e.getMessage());
                        }
                    }showScoreDialog(score);
                }

            });
            // Get random questions and options from Firestore
            getRandomQuestionAndOptions(collectionName);
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("FireStore A1 ", "error: " + e.getMessage());
        }
    }

    public void showScoreDialog(int score) {
        AlertDialog.Builder builder = new AlertDialog.Builder(LevelAQuizActivity.this);
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
    public void getRandomQuestionAndOptions(String collectionName) {

        db.collection(collectionName)
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
                                    Log.e("Show A", meaning);
                                }
                                setRandomQuestionAndOptions(wordList);
                            } else {
                                Toast.makeText(LevelAQuizActivity.this, "Failed to fetch data", Toast.LENGTH_SHORT).show();
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
                    String questionText2 = questionWord.getDefinition() + " " + pos;

//                adapter.getQuestions().add(new WordQuizData(questionText, correctOption, incorrectOption));
                    adapter.getQuestions().add(new WordQuizData(questionText2, pos, correctOption, incorrectOption));
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
