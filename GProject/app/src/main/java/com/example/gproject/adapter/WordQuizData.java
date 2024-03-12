package com.example.gproject.adapter;

public class WordQuizData {
    private String definition;
    private String correctWord;
    private String incorrectWord;
    private int selectedOption;  // 新增字段，用於記錄所選的選項
    private String opt1;  // 新增字段，用于存储选项1的值
    private String opt2;  // 新增字段，用于存储选项2的值

    public WordQuizData(String definition, String correctWord, String incorrectWord) {
        this.definition = definition;
        this.correctWord = correctWord;
        this.incorrectWord = incorrectWord;
        this.selectedOption = -1;  // 初始值設為-1表示未選擇
        this.opt1 = opt1;
        this.opt2 = opt2;

    }

    public void setOpt1(String opt1) {
        this.opt1 = opt1;
    }

    public void setOpt2(String opt2) {
        this.opt2 = opt2;
    }

    public String getDefinition() {
        return definition;
    }

    public void setDefinition(String definition) {
        this.definition = definition;
    }

    public String getCorrectWord() {
        return correctWord;
    }

    public void setCorrectWord(String correctWord) {
        this.correctWord = correctWord;
    }

    public String getIncorrectWord() {
        return incorrectWord;
    }

    public void setIncorrectWord(String incorrectWord) {
        this.incorrectWord = incorrectWord;
    }

    public int getSelectedOption() {
        return selectedOption;
    }

    public void setSelectedOption(int selectedOption) {
        this.selectedOption = selectedOption;
    }
    public String getOpt1() {
        return correctWord; // 或者返回相應的內容
    }

    public String getOpt2() {
        return incorrectWord; // 或者返回相應的內容
    }

}
