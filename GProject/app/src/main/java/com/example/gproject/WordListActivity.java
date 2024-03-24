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

import com.example.gproject.adapter.CardAdapter;
import com.example.gproject.adapter.CardData;
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
    private CardAdapter adapter;

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

        //初始化 RecyclerView 和 adapter
        recyclerView = findViewById(R.id.rcyQ);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        List<CardData> cardDataList = new ArrayList<>();
        adapter = new CardAdapter(cardDataList);

        setCollectWordData();
    }

    private void setCollectWordData() {
        // 獲取 Firebase Database 的參考
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference root = db.getReference("word_collect");
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String userID = user.getUid();
        root.child(userID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // 數據變化時的處理
                for (DataSnapshot wordSnapshot : dataSnapshot.getChildren()) {
                    try { // 讀取單字
                        String word = wordSnapshot.getKey();
                        DataSnapshot speechTextSnapshot = wordSnapshot.child("speechText");

                        // 检查 "speechText" 节点是否存在
                        if (speechTextSnapshot.exists()) {
                            String speechText = wordSnapshot.child("speechText").getValue(String.class);

                            CardData cardData = new CardData(word, speechText);
                            // 将 CardData 添加到适配器的数据列表中
                            adapter.getDataList().add(cardData);
                            adapter.notifyDataSetChanged();
                            Toast.makeText(WordListActivity.this, "Success", Toast.LENGTH_SHORT).show();

                        } else {
                            Log.e("WordCardActivity", "speechTextSnapshot dosen't exists()");
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.e("WordCardActivity", "Failed with error: " + e.getMessage());
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // 讀取數據出錯時的處理
                System.out.println("Read data error: " + databaseError.getMessage());
            }
        });
    }
}
