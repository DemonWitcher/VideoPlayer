package com.witcher.videoplayer;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;
import android.widget.TableLayout;

import com.witcher.videoplayerlib.entity.Video;
import com.witcher.videoplayerlib.manager.PlayerManager;

public class PlayActivity extends AppCompatActivity {

    public static void startPlayActivity(Context context,Video video){
        Intent intent = new Intent(context,PlayActivity.class);
        intent.putExtra("video",video);
        context.startActivity(intent);
    }

    private PlayerManager mPlayerManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.video_main);

        mPlayerManager = new PlayerManager(findViewById(R.id.fl_main),this);
        mPlayerManager.setDebugHudView((TableLayout) findViewById(R.id.hud_view));
        mPlayerManager.setVideo((Video) getIntent().getExtras().get("video"));
        mPlayerManager.start();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mPlayerManager.onStop();
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
        mPlayerManager.onRestart();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPlayerManager.onDestroy();
    }
}
