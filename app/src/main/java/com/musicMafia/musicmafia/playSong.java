package com.musicMafia.musicmafia;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.SurfaceHolder;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.musicMafia.musicmafia.R;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class playSong extends AppCompatActivity {
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mediaPlayer.stop();
        mediaPlayer.release();
        updateseek.interrupt();
    }
    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
    }

    SeekBar seekBar;
    VideoView videoView;
    TextView textView,playerduration,playerposition;
    ImageButton next, previous, play,repeat,loop;
    ArrayList songs;
    String textviewContent;
    int position;
    Thread updateseek;
    MediaPlayer mediaPlayer,mediaPlayer1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_song);

        play = findViewById(R.id.playID);
        next = findViewById(R.id.nextID);
        repeat=findViewById(R.id.repeatID);
        previous = findViewById(R.id.previousID);
        loop=findViewById(R.id.loopID);
        textView = findViewById(R.id.textViewID);
        videoView=findViewById(R.id.videoViewID);
        seekBar=findViewById(R.id.seekBar);
        mediaPlayer1 = MediaPlayer.create(this,R.raw.music_mafia_cd_1080p);
        mediaPlayer1.start();
        mediaPlayer1.setLooping(true);
        videoView.setKeepScreenOn(true);
        playerduration=findViewById(R.id.playerdurationID);
        playerposition=findViewById(R.id.playerpositionID);
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        songs =  bundle.getParcelableArrayList("songlist");
        textviewContent = intent.getStringExtra("currentsong");
        textView.setText(textviewContent);
        textView.setSelected(true);
        position = intent.getIntExtra("position", 0);
        Uri uri = Uri.parse(songs.get(position).toString());
        mediaPlayer = MediaPlayer.create(this, uri);
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
        CountDownTimer Timer = new CountDownTimer(mediaPlayer.getDuration(), 1000) {

            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                mediaPlayer.release();
                if(position!=songs.size()-1){
                    position=position+1;

                }else {
                    position=0;
                }
                Uri uri = Uri.parse(songs.get(position).toString());
                mediaPlayer = MediaPlayer.create(getApplicationContext(), uri);
                mediaPlayer.start();

            }
        };
        Timer.start();
        SurfaceHolder surfaceholder = videoView.getHolder();
        surfaceholder.addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(@NonNull SurfaceHolder holder) {
                mediaPlayer1.setDisplay(surfaceholder);
            }

            @Override
            public void surfaceChanged(@NonNull SurfaceHolder holder, int format, int width, int height) {

            }

            @Override
            public void surfaceDestroyed(@NonNull SurfaceHolder holder) {

            }
        });

        updateseek=new Thread(){
            @Override
            public void run() {
                super.run();
                int currentposition=0;
                try{
                while (mediaPlayer.getDuration() > currentposition) {
                    currentposition = mediaPlayer.getCurrentPosition();
                    seekBar.setProgress(currentposition);
                    String sPosition= convertFormat(mediaPlayer.getCurrentPosition());
                    playerposition.setText(sPosition);
                    sleep(800);
                }
                }catch(Exception e){
                    e.printStackTrace();
                    }
                }
        };
        updateseek.start();
        play.setOnClickListener(v -> {
            if(mediaPlayer.isPlaying()){
                play.setImageResource(R.drawable.play);
                mediaPlayer.pause();
                mediaPlayer1.pause();
            }
            else {
                play.setImageResource(R.drawable.pause);
                mediaPlayer.start();
                mediaPlayer1.start();
                mediaPlayer1.setLooping(true);

            }
        });
        loop.setOnClickListener(v -> {
            if(!mediaPlayer.isLooping()){
            mediaPlayer.setLooping(true);
            loop.setImageResource(R.drawable.loop_color);
        }else{
                mediaPlayer.setLooping(false);
                loop.setImageResource(R.drawable.loop);
            }
        });
        previous.setOnClickListener(v -> {
            mediaPlayer.stop();
            mediaPlayer.release();

            if(position!=0){
                position=position-1;
            }else {
                position=songs.size()-1;
            }

            play.setImageResource(R.drawable.pause);
            Uri uri12 = Uri.parse(songs.get(position).toString());
            mediaPlayer = MediaPlayer.create(getApplicationContext(), uri12);
            mediaPlayer.start();
            seekBar.setMax(mediaPlayer.getDuration());
            mediaPlayer1.start();
            mediaPlayer1.setLooping(true);
            textviewContent=songs.get(position).toString();
            textView.setText(textviewContent);
            int duration= mediaPlayer.getDuration();
            String sDuration= convertFormat(duration);
            playerduration.setText(sDuration);
            String sPosition= convertFormat(mediaPlayer.getCurrentPosition());
            playerposition.setText(sPosition);

        });
        next.setOnClickListener(v -> {
            mediaPlayer.stop();
            mediaPlayer.release();
            if(position!=songs.size()-1){
                position=position+1;

            }else {
                position=0;
            }

            play.setImageResource(R.drawable.pause);
            Uri uri13 = Uri.parse(songs.get(position).toString());
            mediaPlayer = MediaPlayer.create(getApplicationContext(), uri13);
            mediaPlayer.start();
            seekBar.setMax(mediaPlayer.getDuration());
            mediaPlayer1.start();
            mediaPlayer1.setLooping(true);
            textviewContent=songs.get(position).toString();
            textView.setText(textviewContent);
            int duration= mediaPlayer.getDuration();
            String sDuration= convertFormat(duration);
            playerduration.setText(sDuration);
            String sPosition= convertFormat(mediaPlayer.getCurrentPosition());
            playerposition.setText(sPosition);

        });
        repeat.setOnClickListener(v -> {

            mediaPlayer.pause();
            seekBar.setProgress(0);
            Uri uri1 = Uri.parse(songs.get(position).toString());
            mediaPlayer = MediaPlayer.create(getApplicationContext(), uri1);
            mediaPlayer.start();

        });
        int duration= mediaPlayer.getDuration();
        String sDuration= convertFormat(duration);
        playerduration.setText(sDuration);
        String sPosition= convertFormat(mediaPlayer.getCurrentPosition());
        playerposition.setText(sPosition);
        if(sPosition.equals(sDuration)){
            mediaPlayer1.setLooping(false);
            mediaPlayer1.stop();
            updateSong();
        }
        }
        public void updateSong(){
        if(position!=songs.size()-1) {
        position=position+1;
        }else{
            position=0;
        }
        mediaPlayer.stop();
        mediaPlayer.release();
        Uri uri = Uri.parse(songs.get(position).toString());
        mediaPlayer= MediaPlayer.create(getApplicationContext(), uri);
        mediaPlayer.start();
        }


    @SuppressLint("DefaultLocale")
    private  String convertFormat(int duration) {
        return String.format("%02d:%02d", TimeUnit.MILLISECONDS.toMinutes(duration),
                TimeUnit.MILLISECONDS.toSeconds(duration) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(duration))
        );
    }


}