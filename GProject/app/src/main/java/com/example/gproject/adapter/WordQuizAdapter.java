package com.example.gproject.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.widget.RadioButton;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;


import com.example.gproject.R;

public class WordQuizAdapter extends RecyclerView.Adapter<WordQuizAdapter.ViewHolder> {
    private List<WordQuizData> questions, correctWord, incorrectWord;
    private Context context;
    private WordQuizData selectedWord;  // 用於保留當前選擇的單詞

    private RadioGroup radioGroup;
    private TextView queTextView;
    private boolean isMatched = false;

    public WordQuizAdapter(List<WordQuizData> questions) {
        this.questions = questions;
        this.correctWord = correctWord;
        this.incorrectWord = incorrectWord;
        this.radioGroup = radioGroup;
        this.queTextView = queTextView;
        this.selectedWord = new WordQuizData("", "", "");  // 初始化 selectedWord
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // 创建ViewHolder
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.word_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // 在ViewHolder中设置问题和选项
        WordQuizData question = questions.get(position);
        holder.questionTextView.setText(question.getDefinition());
        holder.opt1TextView.setText(question.getCorrectWord());
        holder.opt2TextView.setText(question.getIncorrectWord());

        // 設置RadioButton的文本為相應opt的內容
        holder.radioButton1.setText("");
        holder.radioButton2.setText("");
        holder.radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // 使用 holder.getAdapterPosition() 获取当前准确的位置
                int currentPosition = holder.getAdapterPosition();

                // 更新 selectedOption
                if (checkedId == R.id.A1) {
                    questions.get(currentPosition).setSelectedOption(1);
                } else if (checkedId == R.id.A2) {
                    questions.get(currentPosition).setSelectedOption(2);
                } else {
                    questions.get(currentPosition).setSelectedOption(-1);  // 表示未选择
                }

                // 使用 holder.getAdapterPosition() 获取当前准确的位置
                selectedWord = questions.get(currentPosition);
            }
        });
    }


    @Override
    public int getItemCount() {
        return questions.size();
    }

    public void setQuestions(List<WordQuizData> questions) {
        this.questions = questions;
        notifyDataSetChanged();
    }

    // 定义ViewHolder
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView questionTextView;
        RadioButton radioButton1;
        TextView opt1TextView;
        RadioButton radioButton2;
        TextView opt2TextView;
        RadioGroup radioGroup;  // 新增這一行
        TextView queTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            questionTextView = itemView.findViewById(R.id.Que);
            radioButton1 = itemView.findViewById(R.id.A1);
            opt1TextView = itemView.findViewById(R.id.opt1);
            radioButton2 = itemView.findViewById(R.id.A2);
            opt2TextView = itemView.findViewById(R.id.opt2);
            radioGroup = itemView.findViewById(R.id.radioGroup);  // 初始化 radioGroup
            queTextView = itemView.findViewById(R.id.Que);

            // 為每個 RadioButton 設置單獨的 OnClickListener
            RadioGroup radioGroup = itemView.findViewById(R.id.radioGroup);
            radioGroup.setId(View.generateViewId());  // 給 RadioGroup 設置唯一的 id

            radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup group, int checkedId) {
                    handleRadioButtonClick(getAdapterPosition(), checkedId);
                }
            });
        }
    }

    // 在适配器中定义一个方法用于处理RadioButton的点击事件
    private void handleRadioButtonClick(int position, int selectedOption) {
        // 將選擇的選項設置到數據模型中，您可以進行相應的處理
        WordQuizData selectedQuestion = questions.get(position);
        selectedQuestion.setSelectedOption(selectedOption);
        // 通知數據集發生更改
        notifyDataSetChanged();
    }

    /// 定义一个接口
    public interface OnAnswerSelectedListener {
        void onAnswerSelected(String selectedAnswer, String correctAnswer);
    }
    private OnAnswerSelectedListener answerSelectedListener;
    // 提供一个设置监听器的方法
    public void setOnAnswerSelectedListener(OnAnswerSelectedListener listener) {
        this.answerSelectedListener = listener;
    }

    public List<WordQuizData> getQuestions() {
        return questions;
    }
    public List<WordQuizData> getoption1() {
        return correctWord;
    }
    public List<WordQuizData> getoption2() {
        return incorrectWord;
    }
    public WordQuizData getSelectedWord() {
        return selectedWord;
    }

    // 獲取選擇的選項
    public String getSelectedOption(int position) {
        if (position >= 0 && position < questions.size()) {
            WordQuizData question = questions.get(position);
            if (question.getSelectedOption() == 1) {
                return question.getOpt1();
            } else if (question.getSelectedOption() == 2) {
                return question.getOpt2();
            }
        }
        return null;
    }


    public void setRandomQuestionAndOptions(List<WordQuizData> wordList) {
        if (wordList.size() >= 3) {
            int numberOfQuestions;
            numberOfQuestions = 3;
            for (int i = 0; i < numberOfQuestions; i++) {
                // 在這裡實作隨機選擇題目和選項的邏輯
                // 選擇一個隨機的單字作為題目

                WordQuizData questionWord = wordList.get(new Random().nextInt(wordList.size()));

                // 從剩下的單字中選擇一個作為正確答案
                List<WordQuizData> optionWords = new ArrayList<>(wordList);
                optionWords.remove(questionWord);
                Collections.shuffle(optionWords);
                WordQuizData correctOption = optionWords.get(0);

                // 隨機選擇一個錯誤答案
                optionWords.remove(correctOption);
                Collections.shuffle(optionWords);
                WordQuizData incorrectOption = optionWords.get(0);

                // 設置題目和選項
//                questions.clear();
                questions.add(new WordQuizData(questionWord.getDefinition(), correctOption.getCorrectWord(), incorrectOption.getIncorrectWord()));
            }
            notifyDataSetChanged();
        } else {
            // 處理單字不足的情況
            Toast.makeText(context, "單字數量不足，無法進行測驗", Toast.LENGTH_SHORT).show();
        }
    }
}





