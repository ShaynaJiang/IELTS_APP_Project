package com.example.gproject.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gproject.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class WordQuizAdapter extends RecyclerView.Adapter<WordQuizAdapter.ViewHolder> {
    private List<WordQuizData> questions;
    private Context context;
    private WordQuizData selectedWord;
    private RecyclerView recyclerView;
    private RadioGroup radioButton;

    public WordQuizAdapter(Context context, List<WordQuizData> questions) {
        this.context = context;
        this.questions = questions;
        this.selectedWord = new WordQuizData("", "","", "");  // 初始化 selectedWord
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.word_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        WordQuizData question = questions.get(position);
//        holder.questionTextView.setText(question.getDefinition() );
        String questionText = context.getString(R.string.question_text_format, question.getDefinition(), question.getPartOfSpeech());
        holder.questionTextView.setText(questionText);
        holder.opt1RadioButton.setText(question.getCorrectWord());
        holder.opt2RadioButton.setText(question.getIncorrectWord());

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
    public void markCorrectAnswer2(String correctAnswer) {
        for (int i = 0; i < getItemCount(); i++) {
            WordQuizData word = questions.get(i);
            if (word.getCorrectWord().equals(correctAnswer)) {
                // 找到正确答案，将相应的选项按钮标记为红色
                ViewHolder viewHolder = (ViewHolder) recyclerView.findViewHolderForAdapterPosition(i);
                if (viewHolder != null) {
                    viewHolder.radioButton.setTextColor(Color.RED);
                }
                break; // 找到正确答案后，停止循环
            }
        }
    }
    public void markCorrectAnswer(int position) {
        ViewHolder viewHolder = (ViewHolder) recyclerView.findViewHolderForAdapterPosition(position);
        if (viewHolder != null) {
            viewHolder.radioButton.setTextColor(Color.RED);
        }
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView questionTextView;
        RadioButton opt1RadioButton;
        RadioButton opt2RadioButton;
        RadioGroup radioGroup;
        public RadioButton radioButton;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            questionTextView = itemView.findViewById(R.id.Que);
            opt1RadioButton = itemView.findViewById(R.id.A1);
            opt2RadioButton = itemView.findViewById(R.id.A2);
            radioGroup = itemView.findViewById(R.id.radioGroup);

        }
    }

    public void setQuestions(List<WordQuizData> questions) {
        this.questions = questions;
        notifyDataSetChanged();
    }public WordQuizData getSelectedWord() {
        return selectedWord;
    }
    public List<WordQuizData> getQuestions() {
        return questions;
    }


    public void setRandomQuestionAndOptions(List<WordQuizData> wordList) {
        if (wordList.size() >= 3) {
            questions.clear();
            int numberOfQuestions = 3;
            for (int i = 0; i < numberOfQuestions; i++) {
                WordQuizData questionWord = wordList.get(new Random().nextInt(wordList.size()));
                List<WordQuizData> optionWords = new ArrayList<>(wordList);
                optionWords.remove(questionWord);
                Collections.shuffle(optionWords);
                WordQuizData correctOption = optionWords.get(0);
                optionWords.remove(correctOption);
                Collections.shuffle(optionWords);
                WordQuizData incorrectOption = optionWords.get(0);
//                questions.add(new WordQuizData(questionWord.getWord(), correctOption.getDefinition(), incorrectOption.getIncorrectWord()));
            }
            notifyDataSetChanged();
        } else {
            Toast.makeText(context, "Insufficient number of words to conduct quiz", Toast.LENGTH_SHORT).show();
        }
    }
}
