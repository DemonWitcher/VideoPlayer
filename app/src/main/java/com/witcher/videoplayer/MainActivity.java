package com.witcher.videoplayer;

import android.os.Bundle;
import android.os.Environment;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;
import android.widget.TableLayout;

import com.witcher.videoplayerlib.entity.Definition;
import com.witcher.videoplayerlib.entity.Video;
import com.witcher.videoplayerlib.manager.PlayerManager;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private String url2 = "http://cn-sdjn-cu-v-09.acgvideo.com/vg0/3/d8/14671202-1.mp4?expires=1488454200&platform=html5&ssig=YRgs-ypD3EnHXEqx0WtMLg&oi=2071384608&nfa=BaDS8BUFZDb8iKo4Vfwarw==&dynamic=1";
    private String url3 = Environment.getExternalStorageDirectory().getPath() + File.separator + "abc.mkv";
    private PlayerManager mPlayerManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.video_main);

        mPlayerManager = new PlayerManager(findViewById(R.id.fl_main),this);
        mPlayerManager.setDebugHudView((TableLayout) findViewById(R.id.hud_view));
        List<Definition> list = new ArrayList<>();
        String url = url2;
        list.add(new Definition("标清",url));
        list.add(new Definition("高清",url));
        list.add(new Definition("超清",url));
        mPlayerManager.setVideo(new Video("宅舞大全",list));
        mPlayerManager.start();
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPlayerManager.onDestroy();
    }
}
