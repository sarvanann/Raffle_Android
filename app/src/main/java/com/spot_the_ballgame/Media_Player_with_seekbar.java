package com.spot_the_ballgame;

import android.annotation.SuppressLint;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class Media_Player_with_seekbar extends AppCompatActivity {
    private Button b3;
    private MediaPlayer mediaPlayer;
    private double startTime = 0;
    private double finalTime = 0;
    private Handler myHandler = new Handler();
    private SeekBar seekbar;
    private TextView tx1;
    public static int oneTimeOnly = 0;
    Button btn_pause_paly;
    ArrayList<Integer> audio_IntegerArrayList = new ArrayList<>();

    @SuppressLint("DefaultLocale")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_media_player_with_seekbar);
        b3 = findViewById(R.id.button3);
        btn_pause_paly = findViewById(R.id.btn_pause_paly);
        tx1 = findViewById(R.id.textView2);
        seekbar = findViewById(R.id.seekBar);
        seekbar.setClickable(false);

        audio_IntegerArrayList.add(R.raw.cut_01);
        audio_IntegerArrayList.add(R.raw.cut_02);
        audio_IntegerArrayList.add(R.raw.cut_03);
        audio_IntegerArrayList.add(R.raw.cut_04);
        audio_IntegerArrayList.add(R.raw.cut_01);
        audio_IntegerArrayList.add(R.raw.cut_02);
        audio_IntegerArrayList.add(R.raw.cut_03);
        audio_IntegerArrayList.add(R.raw.cut_04);
        audio_IntegerArrayList.add(R.raw.cut_01);
        audio_IntegerArrayList.add(R.raw.cut_02);
        mediaPlayer = MediaPlayer.create(this, audio_IntegerArrayList.get(0));
        b3.setOnClickListener(v -> {
            mediaPlayer.start();
            finalTime = mediaPlayer.getDuration();
            startTime = mediaPlayer.getCurrentPosition();
            if (oneTimeOnly == 0) {
                seekbar.setMax((int) finalTime);
                oneTimeOnly = 1;
            }
            seekbar.setProgress((int) startTime);
            myHandler.postDelayed(UpdateSongTime, 100);
            b3.setEnabled(false);
        });
    }

    private Runnable UpdateSongTime = new Runnable() {
        @SuppressLint("DefaultLocale")
        public void run() {
            startTime = mediaPlayer.getCurrentPosition();
            tx1.setText(String.format("%d min, %d sec",
                    TimeUnit.MILLISECONDS.toMinutes((long) startTime),
                    TimeUnit.MILLISECONDS.toSeconds((long) startTime) -
                            TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.
                                    toMinutes((long) startTime)))
            );
            Log.e("finalTime", "" + finalTime);
            Log.e("startTime", "" + TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.
                    toMinutes((long) startTime)));

            if (finalTime <= startTime) {
                btn_pause_paly.setBackground(getResources().getDrawable(R.drawable.play_button__2_));
            }
            seekbar.setProgress((int) startTime);
            myHandler.postDelayed(this, 100);
        }
    };
}
