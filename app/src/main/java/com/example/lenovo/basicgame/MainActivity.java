package com.example.lenovo.basicgame;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    ArrayList<String> wordsArray = new ArrayList<>();
    TextView wordTextView;
    EditText wordEditText;
    TextView scoreTextView;
    TextView playTextView;
    RelativeLayout playHolder;
    RelativeLayout gameOverHolder;
    TextView gameOverTextView;
    TextView scoreGameOverTextView;
    TextView highScoreGameOverTextView;
    TextView timeTextView;

    Timer myTimer = new Timer();
    Boolean scheduledTimer = false;
    private static SharedPreferences preferences;
    private static final String HIGH_SCORE = "high_score";
    double highScore;


    private double score = 0;
    private double secondsPassed = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        preferences = MainActivity.this.getSharedPreferences(HIGH_SCORE, Context.MODE_PRIVATE);
        highScore = Double.parseDouble(preferences.getString(HIGH_SCORE, String.valueOf(0)));

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        addWords();
        findViews();

        playHolder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetTimer();
                startTimer();
                playHolder.setVisibility(View.GONE);
                score = 0; //reset score
                scoreTextView.setText("Score = " + formatedDouble(score));
                getWord();
            }
        });

        gameOverHolder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gameOverHolder.setVisibility(View.GONE);
                playHolder.setVisibility(View.VISIBLE);
            }
        });

        wordEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (getUserInput().length() == getCurrentWord().length()) {
                    if (getUserInput().equalsIgnoreCase(getCurrentWord())) {
                        score = updatedScore();
                        resetTimer();
                        scoreTextView.setText("Score = " + formatedDouble(score));
                        getWord();
                    } else {
                        hideKeyBoard();

                        gameOverHolder.setVisibility(View.VISIBLE);
                        scoreGameOverTextView.setText("Score = " + formatedDouble(score));

                        if (score > highScore) {
                            updateHighScore();
                            highScoreGameOverTextView.setText("New highscore = " + formatedDouble(highScore));
                        } else{
                            highScoreGameOverTextView.setText("Highscore = " + formatedDouble(highScore));
                        }
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void findViews() {
        wordTextView = findViewById(R.id.word_textView);
        wordEditText = findViewById(R.id.word_editText);
        scoreTextView = findViewById(R.id.score_textView);
        playTextView = findViewById(R.id.play_TextView);
        playHolder = findViewById(R.id.play_Holder);
        gameOverHolder = findViewById(R.id.game_over_Holer);
        gameOverTextView = findViewById(R.id.game_over_TextView);
        scoreGameOverTextView = findViewById(R.id.score_game_over_TextView);
        highScoreGameOverTextView = findViewById(R.id.high_score_game_over_TextView);
        timeTextView = findViewById(R.id.time_textView);
    }

    private void updateHighScore() {
        highScore = score;
        preferences.edit().putString(HIGH_SCORE, String.valueOf(highScore)).apply();
    }

    private double updatedScore() {
        return score + (double) getCurrentWord().length() / secondsPassed;
    }

    private void hideKeyBoard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(MainActivity.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
    }

    private void resetTimer() {
        secondsPassed = 0;
    }

    private void startTimer() {
        if (!scheduledTimer) {
            myTimer.schedule(task, 0, 1000);
            scheduledTimer = true;
        }
    }

    private String formatedDouble(double number){
        return String.format("%.2f", number);
    }

    TimerTask task = new TimerTask() {
        @Override
        public void run() {
            try {
                synchronized (this) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            secondsPassed++;
                            timeTextView.setText("Sekondat = " + (int) secondsPassed);
                        }
                    });
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    };


    private String getWord() {
        wordEditText.setText("");
        Random r = new Random();
        wordTextView.setText(wordsArray.get(r.nextInt(wordsArray.size())));
        return wordsArray.get(r.nextInt(wordsArray.size()));
    }

    private String getCurrentWord() {
        return wordTextView.getText().toString();
    }

    private String getUserInput() {
        return wordEditText.getText().toString();
    }

    private void addWords() {
        wordsArray.add("pewdiepie"); //subscribe to pewdipie
        wordsArray.add("t-series"); //unsubscribe from t-series
        wordsArray.add("youtube");  //breawstore platform
        wordsArray.add("yaaah");    //cringe nr1.0
        wordsArray.add("it's");     //cringe nr1.1
        wordsArray.add("rewind");   //cringe nr1.2
        wordsArray.add("time");    //cringe nr1.3
        wordsArray.add("EEEEEEEEEEEEEEEEEE macarena");
        wordsArray.add("It would be a shame if something happened to your score.");
    }
}
