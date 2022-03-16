package com.example.musicplayer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;

public class playsong extends AppCompatActivity {
    //ctrl+o dabane kai y method baad ondestorkrna hai taaki backaane kai saath music stop ho gaye aur clcik karne pai phir  sai chaalo ho jaaye
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mediaPlayer.start();
       mediaPlayer.stop();
        updateseek.interrupt();
    }

    TextView textView;
    ImageView play,next,previous,loop,favourite,dlist;
    ArrayList<File> songs;
    MediaPlayer mediaPlayer;
    String textContent;
    int position;
    int i;
    Thread updateseek;
    SeekBar seekBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playsong);
        play = findViewById(R.id.play);
        previous = findViewById(R.id.previous);
        next = findViewById(R.id.next);
        dlist = findViewById(R.id.dlist);
        loop = findViewById(R.id.loop);
        textView = findViewById(R.id.textView);
        seekBar = findViewById(R.id.seekBar);
        favourite = findViewById(R.id.favorite);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        songs = (ArrayList) bundle.getParcelableArrayList("songlist");
//        textContent = intent.getStringExtra("currentsong");
//        textView.setText(textContent);
//        textView.setSelected(true);
        position = intent.getIntExtra("position",0);
        Uri uri = Uri.parse(songs.get(position).toString());
        mediaPlayer = MediaPlayer.create(this,uri);
        mediaPlayer.start();
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

        updateseek = new Thread(){
            // create a method run

            @Override
            public void run() {
                int currentPosition = 0;
                try{
                    while (currentPosition<mediaPlayer.getDuration()){
                        currentPosition = mediaPlayer.getCurrentPosition();
                        seekBar.setProgress(currentPosition);
                        sleep(800);
                    }

                }
                catch (Exception e){
                    e.printStackTrace();
                }
            }
        };
        updateseek.start();

        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mediaPlayer.isPlaying()){
                    play.setImageResource(R.drawable.ic_baseline_play_circle_filled_24);
                    mediaPlayer.pause();
                }
                else{
                    play.setImageResource(R.drawable.pause);
                    mediaPlayer.start();
                }
            }
        });
        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mediaPlayer.stop();
                mediaPlayer.release();
                if (position!=0){
                    position = position -1;
                }
                else{
                    position = songs.size()-1;
                }
                Uri uri = Uri.parse(songs.get(position).toString());
                mediaPlayer = MediaPlayer.create(getApplicationContext(),uri);
                mediaPlayer.start();
                play.setImageResource(R.drawable.pause);
                seekBar.setMax(mediaPlayer.getDuration());
                textContent =songs.get(position).getName().toString();
                textView.setText(textContent);
                textView.setSelected(true);


            }
        });
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mediaPlayer.stop();
                mediaPlayer.release();
                if (position!=songs.size()-1){
                    position = position+1;
                }
                else{
                    position =0;
                }
                Uri uri = Uri.parse(songs.get(position).toString());
                mediaPlayer = MediaPlayer.create(getApplicationContext(),uri);
                mediaPlayer.start();
                play.setImageResource(R.drawable.pause);
                seekBar.setMax(mediaPlayer.getDuration());
                textContent =songs.get(position).getName().toString();
                textView.setText(textContent);
                textView.setSelected(true);
            }
        });
        loop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mediaPlayer.isLooping()){
                    loop.setImageResource(R.drawable.loop);
                    mediaPlayer.setLooping(false);
                }
                else{
                    mediaPlayer.setLooping(true);
                    loop.setImageResource(R.drawable.ic_baseline_shuffle_24);
                }
            }
        });
        favourite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mediaPlayer.isPlaying()) {
                    favourite.setImageResource(R.drawable.colored_favourite);
                }
                else{
                    favourite.setImageResource(R.drawable.favorite);
                }
            }
        });
        dlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(intent);
                Uri uri = Uri.parse(songs.get(position).toString());
                mediaPlayer = MediaPlayer.create(getApplicationContext(),uri);
                mediaPlayer.start();
            }
        });
    }
}