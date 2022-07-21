package com.vishal.vmusix;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.Image;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;

public class PlaySong extends AppCompatActivity {
    ImageView previous , play , next , art;
    TextView sname, cur , dur;
    ArrayList<File> songs;
    MediaPlayer mediaPlayer;
    String textContent ;
    int position ;
    SeekBar seekBar;
    Thread updateSeek;


    @Override
    protected void onDestroy() {
        super.onDestroy();
        mediaPlayer.stop();
        mediaPlayer.release();
        updateSeek.interrupt();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_song);
        previous = findViewById(R.id.previous);
        play = findViewById(R.id.play);
        next = findViewById(R.id.next);
        sname = findViewById(R.id.sname);
        art = findViewById(R.id.art);
        seekBar = findViewById(R.id.seekBar);
        cur = findViewById(R.id.cur);
        dur = findViewById(R.id.dur);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        songs = (ArrayList) bundle.getParcelableArrayList("SongList");
        textContent = intent.getStringExtra("CurrentSong");
        sname.setText(textContent);
        sname.setSelected(true);
        position = intent.getIntExtra("position",0);
        Uri uri = Uri.parse(songs.get(position).toString());
        mediaPlayer = MediaPlayer.create(this, uri);
        mediaPlayer.start();
        seekBar.setMax(mediaPlayer.getDuration());
//         dur.setText(Integer.toString(mediaPlayer.getDurationInM()));
//         cur.setText(Integer.toString(mediaPlayer.getCurrentPosition()));
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
         updateSeek = new Thread(){
             @Override
             public void run() {
                 int currentPosition = 0 ;
                 try{
                     while (currentPosition < mediaPlayer.getDuration()){
                     currentPosition = mediaPlayer.getCurrentPosition();
                     seekBar.setProgress(currentPosition);
                     sleep(800);}
                 }catch (Exception e){
                      e.printStackTrace();
                 }


             }
         };
         updateSeek.start();


         play.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 if(mediaPlayer.isPlaying()){
                     play.setImageResource(R.drawable.play);
                     mediaPlayer.pause();
                 }else{
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

                 if(position != 0){
                     position-- ;
                 }else{
                     position = songs.size() - 1 ;
                 }
                 Uri uri = Uri.parse(songs.get(position).toString());
                 mediaPlayer = MediaPlayer.create(getApplicationContext(), uri);
                 mediaPlayer.start();
                 seekBar.setMax(mediaPlayer.getDuration());
                 textContent = songs.get(position).getName().toString();
                 sname.setText(textContent);
                 play.setImageResource(R.drawable.pause);
                 updateSeek = new Thread(){
                     @Override
                     public void run() {
                         int currentPosition = 0 ;
                         try{
                             while (currentPosition < mediaPlayer.getDuration()){
                                 currentPosition = mediaPlayer.getCurrentPosition();
                                 seekBar.setProgress(currentPosition);
                                 sleep(800);}
                         }catch (Exception e){
                             e.printStackTrace();
                         }


                     }
                 };
                 updateSeek.start();
             }
         });

       next.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               mediaPlayer.stop();
               mediaPlayer.release();
               if(position != songs.size()-1){
                   position++;
               }else{
                   position = 0 ;
               }
               Uri uri = Uri.parse(songs.get(position).toString());
               mediaPlayer = MediaPlayer.create(getApplicationContext(),uri);
               mediaPlayer.start();
               seekBar.setMax(mediaPlayer.getDuration());
               textContent = songs.get(position).getName().toString();
               sname.setText(textContent);
               play.setImageResource(R.drawable.pause);
               updateSeek = new Thread(){
                   @Override
                   public void run() {
                       int currentPosition = 0 ;
                       try{
                           while (currentPosition < mediaPlayer.getDuration()){
                               currentPosition = mediaPlayer.getCurrentPosition();
                               seekBar.setProgress(currentPosition);
                               sleep(800);}
                       }catch (Exception e){
                           e.printStackTrace();
                       }


                   }
               };
               updateSeek.start();
           }
       });
    }
}