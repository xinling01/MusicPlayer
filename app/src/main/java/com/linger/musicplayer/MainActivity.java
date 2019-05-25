package com.linger.musicplayer;

import android.app.Activity;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;

public class MainActivity extends Activity {
    private MediaPlayer mediaPlayer;
    private boolean isPause=false;
    File file;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);//设置全屏显示
        //获取要播放的音频文件
       // mediaPlayer=MediaPlayer.create(this,R.raw.knockheart);
       file=new File("/sdcard/knockheart.mp3");
       if(file.exists()){
           mediaPlayer=MediaPlayer.create(this,Uri.parse(file.getAbsolutePath()));
       }else {
           Toast.makeText(this, "要播放的文件不存在", Toast.LENGTH_SHORT).show();
           return;
       }

        final ImageButton btn_play=findViewById(R.id.ib1);//获取播放、暂停按钮
        final ImageButton btn_stop=findViewById(R.id.ib2);//获取stop停止按钮
        btn_play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mediaPlayer.isPlaying()&&!isPause){
                    mediaPlayer.pause();//暂停播放
                    isPause=true;
                    //更换图标为播放
                    ((ImageButton)v).setImageDrawable(getDrawable(R.drawable.play));
                }else {
                    mediaPlayer.start();//继续播放
                    isPause=false;//设置为播放状态
                    ((ImageButton)v).setImageDrawable(getDrawable(R.drawable.pause));
                }
            }
        });
        /*停止播放按钮的单击事件监听器*/
        btn_stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaPlayer.stop();//停止播放
                btn_play.setImageDrawable(getDrawable(R.drawable.play));
            }
        });
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                play();//重新播放功能
            }
        });
    }

    private void play() {
        mediaPlayer.reset();//重置MediaPlayer
        try {
            mediaPlayer.setDataSource(file.getAbsolutePath());
            mediaPlayer.prepare();//预加载音频
            mediaPlayer.start();//播放音频
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        if(mediaPlayer.isPlaying()){
            mediaPlayer.stop();//停止播放
        }
        mediaPlayer.release();//释放资源
        super.onDestroy();
    }
}
