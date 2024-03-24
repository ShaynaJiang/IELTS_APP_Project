package com.example.gproject.meaning;

import static java.util.Collections.emptyList;

import com.example.gproject.R;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.gproject.WordCard.WordResult2;
import com.example.gproject.databinding.TestBinding;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import retrofit2.Response;

import com.example.gproject.reading.R_topic;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ShowMeaning extends AppCompatActivity {
    private String currentWord, definitions;
    private final AtomicBoolean isStarred = new AtomicBoolean(false);
    private TestBinding binding;
    private MeaningAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = TestBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ImageButton backButton = findViewById(R.id.back);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 在这里添加返回逻辑
                Intent intent = new Intent(ShowMeaning.this, R_topic.class);
                startActivity(intent);
                // 结束当前活动（可选）
                finish();
            }
        });
        //Search
        binding.searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentWord = binding.searchInput.getText().toString();

                getMeaning(currentWord);
            }
        });

        // Collect
        binding.starButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (isStarred.get()) {
                    // 如果已经收藏，点击后取消收藏
                    deleteCollectData();
                    // 设置为默认状态的背景
                    binding.starButton.setImageResource(R.drawable.ic_star_default);
                    // 更新状态
                    isStarred.set(false);
                    Log.e("collect", "Failed with error: ");
                    Toast.makeText(getApplicationContext(), "Removed from collection", Toast.LENGTH_LONG).show();
                } else {
                        // 如果未收藏，点击后收藏
                        insertCollectData();
                        // 设置为按下状态的背景
                        binding.starButton.setImageResource(R.drawable.ic_star_pressed);
                        // 更新状态
                        isStarred.set(true);
                    Toast.makeText(getApplicationContext(), "Added to collection", Toast.LENGTH_LONG).show();
                }
            }
        });
        adapter = new MeaningAdapter(emptyList());
        binding.meaningRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.meaningRecyclerView.setAdapter(adapter);
    }

    public void insertCollectData() {
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference root = db.getReference("word_collect");
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user != null) {
            String userId = user.getUid();

            String word = binding.wordTextview.getText().toString();
            String speechText = binding.phoneticTextview.getText().toString();
            MeaningAdapter.MeaningViewHolder viewHolder = (MeaningAdapter.MeaningViewHolder) binding.meaningRecyclerView.findViewHolderForAdapterPosition(0);
            String definitionsText = viewHolder.getDefinitionsText();
            DataHolder obj = new DataHolder(definitionsText, speechText, 0);

            root.child(userId).child(word).setValue(obj);
            Log.e("collect", "Added to collection " + userId + 1);

        } else {
            // Handle the case where the user is not authenticated
            Log.e("collect", "User not authenticated");
        }
    }

    public void deleteCollectData() {
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference root = db.getReference("word_collect");
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user != null) {
            String userId = user.getUid();

            String word = binding.wordTextview.getText().toString();
            String SpeechText = binding.phoneticTextview.getText().toString();
            MeaningAdapter.MeaningViewHolder viewHolder = (MeaningAdapter.MeaningViewHolder) binding.meaningRecyclerView.findViewHolderForAdapterPosition(0); // 这里假设只有一项，你需要根据实际情况调整
            String definitionsText = viewHolder.getDefinitionsText();
            DataHolder obj = new DataHolder(definitionsText, SpeechText, 0);

            root.child(userId).child(word).removeValue();
            Log.e("collect", "Added to collection " + userId + 2);

        } else {
            // Handle the case where the user is not authenticated
            Log.e("collect", "User not authenticated");
        }
    }

    public void getMeaning(String word) {
        setInProgress(true);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Response<List<WordResult2>> response = RetrofitInstance2.getInstance()
                            .create(DictionaryApi.class)
                            .getMeaning(word)
                            .execute();

                    if (response.body() == null) {
                        throw new Exception();
                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            setInProgress(false);
                            List<WordResult2> results = response.body();
                            if (results != null && !results.isEmpty()) {
                                setUI(results.get(0));
                            }
                        }
                    });
                } catch (Exception e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            setInProgress(false);
                            Toast.makeText(getApplicationContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        }).start();
    }

    private void setUI(WordResult2 response) {
        binding.wordTextview.setText(response.getWord());
        binding.phoneticTextview.setText(response.getPhonetic());
        adapter.updateNewData(response.getMeanings());
    }

    private void setInProgress(boolean inProgress) {
        if (inProgress) {
            binding.searchBtn.setVisibility(View.INVISIBLE);
            binding.progressBar.setVisibility(View.VISIBLE);
        } else {
            binding.searchBtn.setVisibility(View.VISIBLE);
            binding.progressBar.setVisibility(View.INVISIBLE);
        }
    }
}