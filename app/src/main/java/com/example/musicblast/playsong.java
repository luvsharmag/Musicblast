package com.example.musicblast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ActionBar;
import android.app.PictureInPictureParams;
import android.content.Intent;
import android.graphics.Point;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.util.Rational;
import android.view.Display;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;

public class playsong extends AppCompatActivity {
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mediaPlayer.stop();
        mediaPlayer.release();
        updateseek.interrupt();
    }
    ActionBar actionBar;
    String name;
    ArrayList<File> Allsongs;
    TextView textView;
    int position;
    MediaPlayer mediaPlayer;
    SeekBar seekBar;
    Thread updateseek;
    ImageView play, next, previous;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playsong);
        actionBar = getActionBar();
        textView = findViewById(R.id.textView);
        seekBar = findViewById(R.id.seekBar);
        play = findViewById(R.id.play);
        previous = findViewById(R.id.previous);
        next = findViewById(R.id.next);
        Intent intent = getIntent();
        Bundle b = intent.getExtras();
        Allsongs = (ArrayList) b.getParcelableArrayList("all music");
        name = intent.getStringExtra("music name");
        textView.setText(name);
        textView.setSelected(true);
        position = intent.getIntExtra("position", 0);
        Uri uri = Uri.parse(Allsongs.get(position).toString());
        mediaPlayer = MediaPlayer.create(this, uri);
        mediaPlayer.start();
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                position+=1;
                mediaPlayer.stop();
                mediaPlayer.release();
            }
        });
        seekBar.setMax(mediaPlayer.getDuration());
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mediaPlayer.seekTo(seekBar.getProgress());
            }
        });
        updateseek = new Thread() {
            @Override
            public void run() {
                int position = 0;
                try {
                    while (position < mediaPlayer.getDuration()) {
                        position = mediaPlayer.getCurrentPosition();
                        seekBar.setProgress(position);
                        sleep(800);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        updateseek.start();
        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mediaPlayer.isPlaying()) {
                    play.setImageResource(R.drawable.play);
                    mediaPlayer.pause();
                } else {
                    play.setImageResource(R.drawable.pause);
                    mediaPlayer.start();
                }
            }
        });
        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaPlayer.stop();
                mediaPlayer.release();
                if (position != 0) {
                    position -= 1;
                } else {
                    position = Allsongs.size() - 1;
                }
                Uri uri = Uri.parse(Allsongs.get(position).toString());
                mediaPlayer = MediaPlayer.create(getApplicationContext(), uri);
                mediaPlayer.start();
                play.setImageResource(R.drawable.pause);
                seekBar.setMax(mediaPlayer.getDuration());
                name = Allsongs.get(position).getName();
                textView.setText(name);
                updateseek = new Thread() {
                    @Override
                    public void run() {
                        int position = 0;
                        try {
                            while (position < mediaPlayer.getDuration()) {
                                position = mediaPlayer.getCurrentPosition();
                                seekBar.setProgress(position);
                                sleep(800);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                };
                updateseek.start();
            }
        });
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaPlayer.stop();
                mediaPlayer.release();
                if (position != Allsongs.size() - 1) {
                    position += 1;
                } else {
                    position = 0;
                }
                Uri uri = Uri.parse(Allsongs.get(position).toString());
                mediaPlayer = MediaPlayer.create(getApplicationContext(), uri);
                mediaPlayer.start();
                play.setImageResource(R.drawable.pause);
                seekBar.setMax(mediaPlayer.getDuration());
                name = Allsongs.get(position).getName();
                textView.setText(name);
                updateseek = new Thread() {
                    @Override
                    public void run() {
                        int position = 0;
                        try {
                            while (position < mediaPlayer.getDuration()) {
                                position = mediaPlayer.getCurrentPosition();
                                seekBar.setProgress(position);
                                sleep(800);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                };
                updateseek.start();
            }
        });
    }
}
