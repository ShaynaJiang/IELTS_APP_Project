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
    private RecyclerView QuizRecycler;
    private List<WordQuizData> questionsList;
    private WordQuizAdapter adapter;
    private List<WordQuizData> dataList;
    private FirebaseFirestore db;
    private Handler handler = new Handler(Looper.getMainLooper());
    LevelAQuizActivity levelAQuizActivity = new LevelAQuizActivity(); // Build LevelAQuizActivity
    private String collectionName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pra_word);

        collectionName= "R_wordC";

        QuizRecycler = findViewById(R.id.rcyQ);
        QuizRecycler.setLayoutManager(new LinearLayoutManager(this));
        QuizRecycler.setAdapter(adapter);
        questionsList = new ArrayList<>();
        adapter = new WordQuizAdapter(this, questionsList);
        QuizRecycler.setAdapter(adapter);

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
                levelAQuizActivity.showScoreDialog(score);
            }
        });
        // Get random questions and options from Firestore
        levelAQuizActivity.getRandomQuestionAndOptions(collectionName);
    }

}

