package com.witcher.videoplayer;

import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.witcher.videoplayerlib.entity.Definition;
import com.witcher.videoplayerlib.entity.Video;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private String url2 = "rtmp://203.207.99.19:1935/live/CCTV5";
    private String url3 = Environment.getExternalStorageDirectory().getPath() + File.separator + "a7.mp4";
    private String url4 = "http://www.jmzsjy.com/UploadFile/%E5%BE%AE%E8%AF%BE/%E5%9C%B0%E6%96%B9%E9%A3%8E%E5%91%B3%E5%B0%8F%E5%90%83%E2%80%94%E2%80%94%E5%AE%AB%E5%BB%B7%E9%A6%99%E9%85%A5%E7%89%9B%E8%82%89%E9%A5%BC.mp4";

    private Button mBt1, mBt2, mBt3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        mBt1 = (Button) findViewById(R.id.bt1);
        mBt2 = (Button) findViewById(R.id.bt2);
        mBt3 = (Button) findViewById(R.id.bt3);

        mBt1.setOnClickListener(this);
        mBt2.setOnClickListener(this);
        mBt3.setOnClickListener(this);

        List<Definition> list = new ArrayList<>();
        String url = url3;
        list.add(new Definition("标清", url));
        list.add(new Definition("高清", url));
        list.add(new Definition("超清", url));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt1: {
                Video video = new Video();
                List<Definition> list = new ArrayList<>();
                list.add(new Definition("超清", url3));
                video.setList(list);
                video.setName("画江湖");
                video.setLive(false);
                PlayActivity.startPlayActivity(this,video);
            }
            break;
            case R.id.bt2: {
                Video video = new Video();
                List<Definition> list = new ArrayList<>();
                list.add(new Definition("标清", url4));
                list.add(new Definition("高清", url4));
                list.add(new Definition("超清", url4));
                video.setList(list);
                video.setName("美食教学");
                video.setLive(false);
                PlayActivity.startPlayActivity(this,video);
            }
            break;
            case R.id.bt3: {
                Video video = new Video();
                List<Definition> list = new ArrayList<>();
                list.add(new Definition("标清", url2));
                list.add(new Definition("高清", url2));
                list.add(new Definition("超清", url2));
                video.setList(list);
                video.setName("CCTV5直播");
                video.setLive(true);
                PlayActivity.startPlayActivity(this,video);
            }
            break;
        }
    }
}
