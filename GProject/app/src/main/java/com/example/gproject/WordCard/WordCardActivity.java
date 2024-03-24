package com.example.gproject.WordCard;

import android.animation.Animator;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.example.gproject.R;


public class WordCardActivity extends AppCompatActivity {
    private TextView flashcardQuestion;
    private GestureDetector gestureDetector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_card_activity);

        TextView flashcardQuestion = ((TextView) findViewById(R.id.flashcard_word));
        TextView flashcardHint = ((TextView) findViewById(R.id.flashcard_hint));
        // 设置手势识别器
        gestureDetector = new GestureDetector(this, new GestureListener());

        // 设置触摸监听器
        flashcardQuestion.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return gestureDetector.onTouchEvent(event);
            }
        });
        // User can tap on question to see answer
        flashcardQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flashcardQuestion.setCameraDistance(25000);
                flashcardHint.setCameraDistance(25000);

                flashcardQuestion.animate()
                        .rotationY(90)
                        .setDuration(200)
                        .withEndAction(
                                new Runnable() {
                                    @Override
                                    public void run() {
                                        flashcardQuestion.setVisibility(View.INVISIBLE);
                                        flashcardHint.setVisibility(View.VISIBLE);
                                        // second quarter turn
                                        flashcardHint.setRotationY(-90);
                                        flashcardHint.animate()
                                                .rotationY(0)
                                                .setDuration(200)
                                                .start();
                                    }
                                }
                        ).start();
            }
        });

        // User can tap on answer to toggle back to question
        flashcardHint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flashcardQuestion.setCameraDistance(25000);
                flashcardHint.setCameraDistance(25000);

                flashcardHint.animate()
                        .rotationY(90)
                        .setDuration(200)
                        .withEndAction(
                                new Runnable() {
                                    @Override
                                    public void run() {
                                        flashcardHint.setVisibility(View.INVISIBLE);
                                        flashcardQuestion.setVisibility(View.VISIBLE);
                                        // second quarter turn
                                        flashcardQuestion.setRotationY(-90);
                                        flashcardQuestion.animate()
                                                .rotationY(0)
                                                .setDuration(200)
                                                .start();
                                    }
                                }
                        ).start();
            }
        });
    }
    // 手势监听器
    private class GestureListener extends GestureDetector.SimpleOnGestureListener {
        private static final int SWIPE_THRESHOLD = 100;
        private static final int SWIPE_VELOCITY_THRESHOLD = 100;

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            try {
                float diffX = e2.getX() - e1.getX();
                float diffY = e2.getY() - e1.getY();
                if (Math.abs(diffX) > Math.abs(diffY) &&
                        Math.abs(diffX) > SWIPE_THRESHOLD &&
                        Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
                    if (diffX > 0) {
                        // 向右滑动
                        showNextFlashcard();
                    } else {
                        // 向左滑动
                        showPreviousFlashcard();
                    }
                    return true;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return false;
        }
    }

    // 显示下一个 Flashcard
    private void showNextFlashcard() {
        // 添加你的逻辑代码
        Log.e("WordCardActivity","right");

    }

    // 显示上一个 Flashcard
    private void showPreviousFlashcard() {
        // 添加你的逻辑代码
        Log.e("WordCardActivity","left");
    }

}

