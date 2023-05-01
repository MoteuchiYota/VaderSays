package org.proven.dnf_vader_says;

import android.content.Intent;
import android.graphics.Typeface;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.method.ScrollingMovementMethod;
import android.text.style.StyleSpan;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;

public class ScoreBoard extends AppCompatActivity {


    TextView scoreboard, title;
    int scoreBoardSound;
    private SoundPool soundPool;
    private static final String FILENAME = "scoreboard.txt";
    MediaPlayer mMediaPlayer = new MediaPlayer();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.scoreboard);
        mMediaPlayer = MediaPlayer.create(this, R.raw.thronethemescore);
        mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mMediaPlayer.setLooping(true);
        mMediaPlayer.start();
        instantiateElements();
        setElementsToListener();
        readFile();
    }

    private void setElementsToListener() {
        scoreboard.setOnClickListener(listener);
        title.setOnClickListener(listener);
    }

    private void instantiateElements() {

        scoreboard = (TextView) findViewById(R.id.pointslist);
        scoreboard.setMovementMethod(new ScrollingMovementMethod());
        title = (TextView) findViewById(R.id.title);

    }

    View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.pointslist:
                    screenTapped(v);
                    break;
                case R.id.title:
                    screenTapped(v);
                    break;
                default:
                    screenTapped(v);
                    break;
            }
        }
    };

    public void screenTapped(View view) {
        mMediaPlayer.stop();
        Intent backmain = new Intent(ScoreBoard.this, VaderSaysActivity.class);
        startActivity(backmain);
    }

    private void readFile() {
        FileInputStream fis = null;
        try {
            fis = openFileInput(FILENAME);
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader bufferedReader = new BufferedReader(isr);
            String scoreline;
            StringBuilder strBuilder = new StringBuilder();
            while ((scoreline = bufferedReader.readLine()) != null) {
                strBuilder.append(scoreline).append("\n");
            }
            final TextView scoreboard = findViewById(R.id.pointslist);
            scoreboard.setText(strBuilder);
            Spannable span = new SpannableString(scoreboard.getText());
            span.setSpan(new StyleSpan(Typeface.BOLD), 0, 100, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            scoreboard.setText(span);

            Animation starWarsAnimation = AnimationUtils.loadAnimation(this, R.anim.scoreboard_crawl);
            scoreboard.startAnimation(starWarsAnimation);
            starWarsAnimation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                    // Animation started
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    // Animation ended, start the next activity
                    Intent backmain = new Intent(ScoreBoard.this, VaderSaysActivity.class);
                    startActivity(backmain);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {
                    // Animation repeated
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
