package com.example.gproject.meaning;

import android.view.View;
import android.widget.Toast;

import com.example.gproject.WordCard.WordResult2;
import android.os.Handler;
import java.util.List;
import com.example.gproject.databinding.WordDicSearchBinding;
import retrofit2.Response;

public class DataHolder {
    String definitions, SpeechText;
    int cont;
    private Handler handler;

    public DataHolder(Handler handler) {
        this.handler = handler;
    }


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
