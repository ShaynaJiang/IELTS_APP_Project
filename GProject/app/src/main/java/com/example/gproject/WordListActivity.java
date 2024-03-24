package com.example.gproject;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gproject.WordCard.WordCardActivity;
import com.example.gproject.adapter.WordListAdapter;
import com.example.gproject.adapter.WordListData;
import com.example.gproject.reading.R_topic;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class WordListActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private WordListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pra_word);

        ImageButton backButton = findViewById(R.id.back);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 在这里添加返回逻辑
                Intent intent = new Intent(WordListActivity.this, R_topic.class);
                startActivity(intent);
                finish();
            }
        });

        // 初始化 RecyclerView
        recyclerView = findViewById(R.id.rcyQ);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // 实例化适配器
        List<WordListData> wordListDataList = new ArrayList<>();
        adapter = new WordListAdapter(wordListDataList);
        recyclerView.setAdapter(adapter);

        // 设置收集单词数据
        setCollectWordData();

        adapter.setOnItemClickListener(new WordListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                // 获取点击项的数据
//                WordListData clickedItem = adapter.getItem(position);
//                if (clickedItem != null) {
//                    // 创建 Intent 对象
//                    Intent intent = new Intent(WordListActivity.this, WordCardActivity.class);
//                    intent.putExtra("word", clickedItem.getWord());
//                    intent.putExtra("phonetic", clickedItem.getPhonetic());
//                    startActivity(intent);
//                }
                // 启动 WordCardActivity 并传递点击的位置
                Intent intent = new Intent(WordListActivity.this, WordCardActivity.class);
                intent.putExtra("position", position);
                startActivity(intent);
            }
        });

    }

    private void setCollectWordData() {
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference root = db.getReference("word_collect");
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String userID = user.getUid();
        root.child(userID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
//                adapter.clearData(); // 清除适配器中的数据列表
                for (DataSnapshot wordSnapshot : dataSnapshot.getChildren()) {
                    try {
                        String word = wordSnapshot.getKey();
                        DataSnapshot speechTextSnapshot = wordSnapshot.child("speechText");
                        String speechText = speechTextSnapshot.getValue(String.class);

                        if (speechTextSnapshot.exists()) {
                            WordListData cardData = new WordListData(word, speechText);
                            adapter.getDataList().add(cardData); // 将单词数据添加到适配器的数据列表中
                            Log.d("FirebaseData", "Key: " + word + ", speechText: " + cardData);
                        } else {
                            Log.d("FirebaseData2", "Key: " + word + ", speechText: " + speechText);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.e("WordListActivity", "Failed with error: " + e.getMessage());
                    }
                }
                adapter.notifyDataSetChanged(); // 刷新 RecyclerView
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("Read data error: " + databaseError.getMessage());
            }
        });
    }
}
