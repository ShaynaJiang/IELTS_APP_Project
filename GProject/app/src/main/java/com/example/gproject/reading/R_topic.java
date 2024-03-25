package com.example.gproject.reading;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

import com.example.gproject.R;
import com.example.gproject.WordListActivity;
//import com.example.gproject.WordQuizActivity;
import com.example.gproject.WordQuiz.LevelCQuizActivity;
import com.example.gproject.meaning.ShowMeaning;

public class R_topic extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.r_topic);

        ImageButton backButton = findViewById(R.id.back);
        ImageButton ButBlank = findViewById(R.id.Q1);
        ImageButton ButJudge = findViewById(R.id.Q2);
        ImageButton BUtChos = findViewById(R.id.Q3);
        ImageButton BUtMatch = findViewById(R.id.Q4);
        ImageButton BUtWord = findViewById(R.id.Q6);
        ImageButton ButTest = findViewById(R.id.Q7);
        ImageButton ButCollect = findViewById(R.id.Q8);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 在这里添加返回逻辑
                Intent intent = new Intent(R_topic.this, R_blank.class);
                startActivity(intent);
                // 结束当前活动（可选）
                finish();
            }
        });
        ButBlank.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 在这里添加返回逻辑
                Intent intent = new Intent(R_topic.this, R_blank.class);
                startActivity(intent);
                // 结束当前活动（可选）
                finish();
            }
        });
        ButJudge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 在这里添加返回逻辑
                Intent intent = new Intent(R_topic.this, R_judge.class);
                startActivity(intent);
                // 结束当前活动（可选）
                finish();
            }
        });
        BUtChos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 在这里添加返回逻辑
                Intent intent = new Intent(R_topic.this, R_chose.class);
                startActivity(intent);
                // 结束当前活动（可选）
                finish();
            }
        });
        BUtMatch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 在这里添加返回逻辑
                Intent intent = new Intent(R_topic.this, R_match.class);
                startActivity(intent);
                // 结束当前活动（可选）
                finish();
            }
        });

        BUtWord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 在这里添加返回逻辑
                Intent intent = new Intent(R_topic.this, LevelCQuizActivity.class);
                startActivity(intent);
                // 结束当前活动（可选）
                finish();
            }
        });
        ButTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 在这里添加返回逻辑
                Intent intent = new Intent(R_topic.this, ShowMeaning.class);
                startActivity(intent);
                // 结束当前活动（可选）
                finish();
            }
        });
        ButCollect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 在这里添加返回逻辑
                Intent intent = new Intent(R_topic.this, WordListActivity.class);
                startActivity(intent);
                // 结束当前活动（可选）
                finish();
            }
        });
    }

}

