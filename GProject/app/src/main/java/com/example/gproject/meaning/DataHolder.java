package com.example.gproject.meaning;

public class DataHolder {
    String definitions, SpeechText;
    int cont;

    //DataBase
    public DataHolder(String definitions, String SpeechText, int cont) {
        this.definitions = definitions;
        this.SpeechText = SpeechText;
        this.cont = cont;
    }


    public int getCont() {
        return cont;
    }

    public void setCont(int cont) {
        this.cont = cont;
    }

    public String getDefinitions() {
        return definitions;
    }

    public void setDefinitions(String definitions) {
        this.definitions = definitions;
    }

    public String getSpeechText() {
        return SpeechText;
    }

    public void setSpeechText(String speechText) {
        SpeechText = speechText;
    }

    //CollectCard

}
