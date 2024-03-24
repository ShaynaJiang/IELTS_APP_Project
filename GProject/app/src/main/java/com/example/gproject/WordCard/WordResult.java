package com.example.gproject.WordCard;

import java.util.List;

public class WordResult {
    private String word;
    private String phonetic;
    private List<Meaning> meanings;

    public WordResult(String word, String phonetic, List<Meaning> meanings) {
        this.word = word;
        this.phonetic = phonetic;
        this.meanings = meanings;
    }

    public String getWord() {
        return word;
    }

    public String getPhonetic() {
        return phonetic;
    }

    public List<Meaning> getMeanings() {
        return meanings;
    }
}
