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
import com.example.gproject.databinding.PraWordBinding;
import com.example.gproject.reading.R_match;
import com.example.gproject.reading.R_topic;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class WordCardActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private CardAdapter adapter;
    private List<CardData> dataList;
    private PraWordBinding binding;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pra_word);

        ImageButton backButton = findViewById(R.id.back);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 在这里添加返回逻辑
                Intent intent = new Intent(WordCardActivity.this, R_topic.class);
                startActivity(intent);
                // 结束当前活动（可选）
                finish();
            }
        });
        // 创建一个包含 CardData 的列表
        List<CardData> cardDataList = new ArrayList<>();
        // 创建 WordCardActivity 对象时传递正确的参数类型
        adapter = new CardAdapter(cardDataList);

        //初始化 RecyclerView 和 adapter
        recyclerView = findViewById(R.id.rcyQ);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        SetData();
    }

    private void SetData() {
        // 獲取 Firebase Database 的參考
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference root = db.getReference("word_collect");
        // 添加 ValueEventListener 以監聽數據變化
//        root = FirebaseDatabase.getInstance().getReference().child("word_collect");
        root.addValueEventListener(new ValueEventListener() {
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
                            Toast.makeText(WordCardActivity.this, "Success", Toast.LENGTH_SHORT).show();

                        } else {
                            // 处理 "speechText" 节点不存在的情况
                            Toast.makeText(WordCardActivity.this, "SpeechText node does not exist", Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.e("WordCardActivity", "Failed with error: " + e.getMessage());
                        Toast.makeText(WordCardActivity.this, "failed33", Toast.LENGTH_SHORT).show();
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
