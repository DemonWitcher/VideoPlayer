package com.witcher.videoplayerlib.manager;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.os.Handler;
import android.os.Message;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TableLayout;
import android.widget.TextView;

import com.witcher.videoplayerlib.Constant;
import com.witcher.videoplayerlib.R;
import com.witcher.videoplayerlib.Util.L;
import com.witcher.videoplayerlib.Util.Util;
import com.witcher.videoplayerlib.adapter.DefinitionAdapter;
import com.witcher.videoplayerlib.broadcastreceiver.BatteryChangedReceiver;
import com.witcher.videoplayerlib.entity.Definition;
import com.witcher.videoplayerlib.entity.Video;
import com.witcher.videoplayerlib.event.BatteryStateEvent;
import com.witcher.videoplayerlib.event.NetStateEvent;
import com.witcher.videoplayerlib.media.IjkVideoView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import tv.danmaku.ijk.media.player.IMediaPlayer;

/**
 * Created by witcher on 2017/2/27 0027.
 */

public class PlayerManager implements View.OnClickListener {

    private Activity mActivity;
    private AudioManager mAudioManager;
    private BatteryChangedReceiver mBatteryChangedReceiver;
    private View mViewRoot;
    private Video mVideo;

    private FrameLayout mFlMain;
    private ListView mLvDefinition;
    private DefinitionAdapter mDefinitionAdapter;
    private IjkVideoView mVvContent/*,mVvPreview*/;
    private TextView mTvName, mTvPosition, mTvDuration, mTvCenter, mTvTime, mTvNetState, mTvDefinition;
    private SeekBar mSbVideo;
    private ProgressBar mPbVolume, mPbBrightness;
    private ImageView mIvPause, mIvTvPause, mIvBattery;
    private RelativeLayout mRlTop, mRlSeek, mRlMenu, mRlDefinition;
    private LinearLayout mLlBottom, mLlVolume, mLlBrightness;

    private boolean mIsDragging, mIsLive, mIsTouchDragging, mIsDefinition;
    private int mIntSreenWidth, mIntMaxVolume, mIntCurrentVolume, mIntCurrentBrightness, mIntCurrentPosition, mIntNewProgress;

    private interface MsgWhat {
        int NOTIFY_UI = 200;
        int HIDE_ALL_HUD = 201;
        int HIDE_CENTER_TV = 202;
        int HIDE_VOLUME = 203;
        int HIDE_BRIGHTNESS = 204;
        int HIDE_VIDEO_PROGRESS = 205;
    }

    private Handler mHandler = new Handler(android.os.Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MsgWhat.NOTIFY_UI: {
                    notifyUI();
                }
                break;
                case MsgWhat.HIDE_ALL_HUD: {
                    hideHud();
                }
                break;
                case MsgWhat.HIDE_CENTER_TV: {
                    hideCenterTv();
                }
                break;
                case MsgWhat.HIDE_VOLUME: {
                    hideVolumeView();
                }
                break;
                case MsgWhat.HIDE_BRIGHTNESS: {
                    hideBrightnessView();
                }
                break;
                case MsgWhat.HIDE_VIDEO_PROGRESS: {
                    hideVideoProgressHud();
                }
                break;
            }
        }
    };

    public PlayerManager(View viewRoot, Activity context) {
        this.mViewRoot = viewRoot;
        this.mActivity = context;
        initView();
        initData();
        new Looper().loop();
    }

    private void initView() {
        mLvDefinition = (ListView) mViewRoot.findViewById(R.id.lv_definition);
        mFlMain = (FrameLayout) mViewRoot.findViewById(R.id.fl_main);
        mTvName = (TextView) mViewRoot.findViewById(R.id.tv_name);
        mTvPosition = (TextView) mViewRoot.findViewById(R.id.tv_position);
        mTvDuration = (TextView) mViewRoot.findViewById(R.id.tv_duration);
        mTvCenter = (TextView) mViewRoot.findViewById(R.id.tv_center);
        mTvTime = (TextView) mViewRoot.findViewById(R.id.tv_time);
        mTvNetState = (TextView) mViewRoot.findViewById(R.id.tv_net_state);
        mTvDefinition = (TextView) mViewRoot.findViewById(R.id.tv_definition);
        mSbVideo = (SeekBar) mViewRoot.findViewById(R.id.sb_seek);
        mPbVolume = (ProgressBar) mViewRoot.findViewById(R.id.pb_volume);
        mPbBrightness = (ProgressBar) mViewRoot.findViewById(R.id.pb_brightness);
        mIvPause = (ImageView) mViewRoot.findViewById(R.id.iv_pause);
        mIvTvPause = (ImageView) mViewRoot.findViewById(R.id.iv_tv_pause);
        mIvBattery = (ImageView) mViewRoot.findViewById(R.id.iv_battery);
        mRlTop = (RelativeLayout) mViewRoot.findViewById(R.id.rl_top);
        mRlSeek = (RelativeLayout) mViewRoot.findViewById(R.id.rl_seek);
        mRlMenu = (RelativeLayout) mViewRoot.findViewById(R.id.rl_menu);
        mRlDefinition = (RelativeLayout) mViewRoot.findViewById(R.id.rl_definition);
        mLlBottom = (LinearLayout) mViewRoot.findViewById(R.id.ll_bottom);
        mLlVolume = (LinearLayout) mViewRoot.findViewById(R.id.ll_volume);
        mLlBrightness = (LinearLayout) mViewRoot.findViewById(R.id.ll_brightness);
        mVvContent = (IjkVideoView) mViewRoot.findViewById(R.id.vv_content);
//        mVvPreview = (IjkVideoView) mViewRoot.findViewById(R.id.vv_preview);

        mDefinitionAdapter = new DefinitionAdapter(mActivity, null);
        mDefinitionAdapter.setOnItemClick(new DefinitionAdapter.OnItemClick() {
            @Override
            public void onClick(int position) {
//                if(mVideo!=null){
//                    mVvContent.setVideoPath(mVideo.getList().get(position).getUrl());
//                }
//                hideDefinition();
            }
        });
        mLvDefinition.setAdapter(mDefinitionAdapter);

        mIvPause.setOnClickListener(this);
        mIvTvPause.setOnClickListener(this);
        mTvDefinition.setOnClickListener(this);
        mSbVideo.setOnSeekBarChangeListener(mSeekListener);
        mVvContent.setOnSeekCompleteListener(mOnSeekCompleteListener);
        mVvContent.setOnPreparedListener(mOnPreparedListener);
        mFlMain.setClickable(true);
        final GestureDetector gestureDetector = new GestureDetector(mActivity, new PlayerGestureListener());
        mFlMain.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (gestureDetector.onTouchEvent(event))
                    return true;
                switch (event.getAction() & MotionEvent.ACTION_MASK) {
                    case MotionEvent.ACTION_UP:
                        if (mIsDefinition) {
                            mIsDefinition = false;
                        } else if (mIsTouchDragging) {
                            mIsTouchDragging = false;
                            seekTo();
                        } else {
                            mHandler.sendEmptyMessageDelayed(MsgWhat.HIDE_BRIGHTNESS, Constant.VOLUME_HUD_AUTO_HIDE);
                            mHandler.sendEmptyMessageDelayed(MsgWhat.HIDE_VOLUME, Constant.VOLUME_HUD_AUTO_HIDE);
                        }
                        break;
                }
                return false;
            }
        });
    }

    private void initData() {
        EventBus.getDefault().register(this);
        mIntSreenWidth = Util.getScreenWidth(mActivity);
        mAudioManager = (AudioManager) mActivity.getSystemService(Context.AUDIO_SERVICE);
        mIntMaxVolume = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        mPbVolume.setMax(mIntMaxVolume);
        mPbBrightness.setMax(mIntMaxVolume);
        mPbVolume.setProgress(Util.getVolume(mAudioManager));
        Util.setBrightness(mActivity, Constant.INIT_BRIGHTNESS);
        mPbBrightness.setProgress((int) (Constant.INIT_BRIGHTNESS * mIntMaxVolume));
        mTvNetState.setText(Util.getNetState(mActivity));
        mTvTime.setText(Util.getTime());
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Intent.ACTION_BATTERY_CHANGED);
        intentFilter.addAction(Intent.ACTION_BATTERY_LOW);
        intentFilter.addAction(Intent.ACTION_BATTERY_OKAY);
        mBatteryChangedReceiver = new BatteryChangedReceiver();
        mActivity.registerReceiver(mBatteryChangedReceiver, intentFilter);
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.iv_pause) {
            pauseOrStart();
        } else if (i == R.id.iv_tv_pause) {
            pauseOrStart();
        } else if (i == R.id.tv_definition) {
            showOrHideDefinition();
        }
    }

    private IMediaPlayer.OnPreparedListener mOnPreparedListener = new IMediaPlayer.OnPreparedListener() {

        @Override
        public void onPrepared(IMediaPlayer iMediaPlayer) {
            L.i("onPrepared");
            mTvDuration.setText(Util.formatTime(mVvContent.getDuration()));
            mSbVideo.setMax(mVvContent.getDuration());
            mHandler.sendEmptyMessageDelayed(MsgWhat.HIDE_ALL_HUD, Constant.HUD_AUTO_HIDE);
        }
    };

    private IMediaPlayer.OnSeekCompleteListener mOnSeekCompleteListener = new IMediaPlayer.OnSeekCompleteListener() {
        @Override
        public void onSeekComplete(IMediaPlayer iMediaPlayer) {
            L.i("onSeekComplete:" + Util.formatTime(iMediaPlayer.getCurrentPosition()));
            mHandler.sendEmptyMessage(MsgWhat.NOTIFY_UI);
            mIsDragging = false;
        }
    };

    private SeekBar.OnSeekBarChangeListener mSeekListener = new SeekBar.OnSeekBarChangeListener() {

        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            if (fromUser) {
                setTvPosition(progress);
//                setVvPreview();
            }
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
            L.i("onStartTrackingTouch");
            mIsDragging = true;
            mHandler.removeMessages(MsgWhat.NOTIFY_UI);
            mHandler.removeMessages(MsgWhat.HIDE_ALL_HUD);
        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            seekTo();
        }
    };

    private class PlayerGestureListener extends GestureDetector.SimpleOnGestureListener {
        private boolean mIsDownTouch;
        private boolean mIsVolume;
        private boolean mIsLandscape;

        @Override
        public boolean onDoubleTap(MotionEvent e) {
            pauseOrStart();
            return false;
        }

        @Override
        public boolean onDown(MotionEvent e) {
            if (isDefinitionShow()) {
                mIsDownTouch = true;
                hideDefinition();
            }else{
                mIsDownTouch = true;
                mIntCurrentVolume = mPbVolume.getProgress();
                mIntCurrentBrightness = mPbBrightness.getProgress();
                mIntCurrentPosition = mSbVideo.getProgress();
            }
            return false;
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            if(mIsDefinition){
                return false;
            }
            float mOldX = e1.getX(), mOldY = e1.getY();
            float deltaX = mOldX - e2.getX();
            if (mIsDownTouch) {
                mIsLandscape = Math.abs(distanceX) >= Math.abs(distanceY);
                mIsVolume = mOldX > mIntSreenWidth * 0.5f;
                mIsDownTouch = false;
            }
            if (mIsLandscape) {
                if (!mIsLive) {
                    //把videoView.getWidth()分成90份,每份是一秒,两边滑到头就拉倒了,滑的时候seek动 画面不受影响继续播放 松手时候seek
                    setProgress(Math.round(-deltaX / (mVvContent.getWidth() / Constant.WIDTH_SECOND)));
                }
            } else {
                float deltaY = mOldY - e2.getY();
                //这里的设定是,以mVideoView高度的80%为基准 分成MaxVolume份,每滑动一份,就增量一次声音或亮度
                //在我的测试机上B站就是这种表现,我就和B站一样好了
                int increment = Math.round(deltaY / ((mVvContent.getHeight() * 80 / 100) / mIntMaxVolume));
                if (mIsVolume) {
                    setVolume(increment);
                } else {
                    setBrightness(increment);
                }
            }
            return false;
        }

        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            if(mIsDefinition){
                return false;
            }
            hudShowOrHide();
            return false;
        }
    }

    private class Looper extends Thread {
        Looper() {
            super(new Runnable() {
                @Override
                public void run() {
                    while (!Thread.currentThread().isInterrupted()) {
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        if (mVvContent.isPlaying() && isHudShow() && !mIsDragging && !mIsTouchDragging) {
                            mHandler.sendEmptyMessage(MsgWhat.NOTIFY_UI);
                        }
                    }
                }
            });
        }

        private void loop() {
            start();
        }
    }

    private void showVolumeView() {
        if (mLlVolume.getVisibility() == View.GONE) {
            mLlVolume.setVisibility(View.VISIBLE);
        }
    }

    private void hideVolumeView() {
        if (mLlVolume.getVisibility() == View.VISIBLE) {
            mLlVolume.setVisibility(View.GONE);
        }
    }

    private void showBrightnessView() {
        if (mLlBrightness.getVisibility() == View.GONE) {
            mLlBrightness.setVisibility(View.VISIBLE);
        }
    }

    private void hideBrightnessView() {
        if (mLlBrightness.getVisibility() == View.VISIBLE) {
            mLlBrightness.setVisibility(View.GONE);
        }
    }

    private void showCenterTv() {
        if (mTvCenter.getVisibility() == View.GONE) {
            mTvCenter.setVisibility(View.VISIBLE);
        }
    }

    private void hideCenterTv() {
        if (mTvCenter.getVisibility() == View.VISIBLE) {
            mTvCenter.setVisibility(View.GONE);
        }
    }

    private boolean isHudShow() {
        return mRlSeek.getVisibility() == View.VISIBLE;
    }

    private void showVideoProgressHud() {
        if (mIvTvPause.getVisibility() == View.GONE) {
            mIvTvPause.setVisibility(View.VISIBLE);
        }
        if (mRlSeek.getVisibility() == View.GONE) {
            mRlSeek.setVisibility(View.VISIBLE);
        }
    }

    private void hideVideoProgressHud() {
        if (mIvTvPause.getVisibility() == View.VISIBLE) {
            mIvTvPause.setVisibility(View.GONE);
        }
        if (mRlSeek.getVisibility() == View.VISIBLE) {
            mRlSeek.setVisibility(View.GONE);
        }
    }

    private void showAllHud() {
        L.i("showAllHud");
        if (mRlTop.getVisibility() == View.GONE) {
            mRlTop.setVisibility(View.VISIBLE);
        }
        if (mIvTvPause.getVisibility() == View.GONE) {
            mIvTvPause.setVisibility(View.VISIBLE);
        }
        if (mRlSeek.getVisibility() == View.GONE) {
            mRlSeek.setVisibility(View.VISIBLE);
        }
        if (mRlMenu.getVisibility() == View.GONE) {
            mRlMenu.setVisibility(View.VISIBLE);
        }
        mHandler.removeMessages(MsgWhat.HIDE_ALL_HUD);
        mHandler.sendEmptyMessageDelayed(MsgWhat.HIDE_ALL_HUD, Constant.HUD_AUTO_HIDE);
    }

    private void hideHud() {
        L.i("hideHud");
        if (mRlTop.getVisibility() == View.VISIBLE) {
            mRlTop.setVisibility(View.GONE);
        }
        if (mIvTvPause.getVisibility() == View.VISIBLE) {
            mIvTvPause.setVisibility(View.GONE);
        }
        if (mRlSeek.getVisibility() == View.VISIBLE) {
            mRlSeek.setVisibility(View.GONE);
        }
        if (mRlMenu.getVisibility() == View.VISIBLE) {
            mRlMenu.setVisibility(View.GONE);
        }
    }

    private void hudShowOrHide() {
        if (isHudShow()) {
            hideHud();
        } else {
            showAllHud();
        }
    }

    private void showOrHideDefinition() {
        if (isDefinitionShow()) {
            hideDefinition();
        } else {
            showDefinition();
        }
    }

    private void hideDefinition() {
        if (mRlDefinition.getVisibility() == View.VISIBLE) {
            mRlDefinition.setVisibility(View.GONE);
        }
    }

    private void showDefinition() {
        if (mRlDefinition.getVisibility() == View.GONE) {
            mRlDefinition.setVisibility(View.VISIBLE);
        }
    }

    private boolean isDefinitionShow() {
        return mRlDefinition.getVisibility() == View.VISIBLE;
    }

    private void pauseOrStart() {
        if (mVvContent.isPlaying()) {
            mVvContent.pause();
            mIvPause.setImageResource(R.drawable.bili_player_play_can_play);
            mIvTvPause.setImageResource(R.drawable.ic_tv_play);
        } else {
            mVvContent.start();
            mIvPause.setImageResource(R.drawable.bili_player_play_can_pause);
            mIvTvPause.setImageResource(R.drawable.ic_tv_stop);
        }
    }

    private void notifyUI() {
        setTvPosition(mVvContent.getCurrentPosition());
        setSbPosition();
        mTvTime.setText(Util.getTime());
    }

    private void setSbPosition() {
        mSbVideo.setProgress(mVvContent.getCurrentPosition());
        mSbVideo.setSecondaryProgress((int) Util.formatBuffer(mVvContent.getDuration(), mVvContent.getBufferPercentage()));
    }

    private void setTvPosition(int currentPosition) {
        mTvPosition.setText(Util.formatTime(currentPosition));
    }

    private void seekTo() {
        L.i("seekTo:" + Util.formatTime(mSbVideo.getProgress()));
        mVvContent.seekTo(mSbVideo.getProgress());
        mHandler.removeMessages(MsgWhat.NOTIFY_UI);
        mHandler.sendEmptyMessageDelayed(MsgWhat.HIDE_ALL_HUD, Constant.HUD_AUTO_HIDE);
    }

//    private void setVvPreview(){
//        L.i("PreviewseekTo:" + Util.formatTime(mSbVideo.getProgress()));
//        mVvPreview.seekTo(mSbVideo.getProgress());
//    }

    private void setProgress(int second) {
        //一个屏幕宽是90秒的进度 停止looper,
        if (isHudShow()) {
            mHandler.removeMessages(MsgWhat.HIDE_ALL_HUD);
        } else {
            mHandler.removeMessages(MsgWhat.HIDE_VIDEO_PROGRESS);
            showVideoProgressHud();
        }
        mIsTouchDragging = true;
        mIntNewProgress = mIntCurrentPosition + (second * 1000);
        if (mIntNewProgress < 0) {
            mIntNewProgress = 0;
        } else if (mIntNewProgress > mVvContent.getDuration()) {
            mIntNewProgress = mVvContent.getDuration();
        }
        mSbVideo.setProgress(mIntNewProgress);
        setTvPosition(mIntNewProgress);
//        setVvPreview();
    }

    private void setVolume(int increment) {
        int newVolume = increment + mIntCurrentVolume;
        if (newVolume > mIntMaxVolume) {
            newVolume = mIntMaxVolume;
        } else if (newVolume < 1) {
            newVolume = 0;
        }
        mHandler.removeMessages(MsgWhat.HIDE_VOLUME);
        mHandler.removeMessages(MsgWhat.HIDE_CENTER_TV);
        showVolumeView();
        showCenterTv();
        if (newVolume == 0) {
            mTvCenter.setText(R.string.zero_volume);
        } else {
            int value = Math.round((float) newVolume / mIntMaxVolume * 100);
            mTvCenter.setText(String.format(mActivity.getResources().getString(R.string.set_volume), value));
        }
        mPbVolume.setProgress(newVolume);
        if (mPbVolume.getProgress() != newVolume) {
            Util.setVolume(mAudioManager, newVolume);
        }
        mHandler.sendEmptyMessageDelayed(MsgWhat.HIDE_CENTER_TV, Constant.VOLUME_HUD_AUTO_HIDE);
    }

    private void setBrightness(int increment) {
        int newBrightness = increment + mIntCurrentBrightness;
        if (newBrightness > mIntMaxVolume) {
            newBrightness = mIntMaxVolume;
        } else if (newBrightness < 1) {
            newBrightness = 0;
        }
        mHandler.removeMessages(MsgWhat.HIDE_BRIGHTNESS);
        mHandler.removeMessages(MsgWhat.HIDE_CENTER_TV);
        showBrightnessView();
        showCenterTv();
        if (newBrightness == 0) {
            mTvCenter.setText(R.string.zero_brightness);
            if (mPbBrightness.getProgress() != newBrightness) {
                Util.setBrightness(mActivity, 0.01f);
            }
        } else {
            float brightness = (float) newBrightness / mIntMaxVolume;
            int value = Math.round(brightness * 100);
            mTvCenter.setText(String.format(mActivity.getResources().getString(R.string.set_brightness), value));
            if (mPbBrightness.getProgress() != newBrightness) {
                Util.setBrightness(mActivity, brightness);
            }
        }
        mPbBrightness.setProgress(newBrightness);
        mHandler.sendEmptyMessageDelayed(MsgWhat.HIDE_CENTER_TV, Constant.VOLUME_HUD_AUTO_HIDE);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(NetStateEvent event) {
        mTvNetState.setText(Util.getNetState(mActivity, event.netState));
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(BatteryStateEvent event) {
        mIvBattery.setImageResource(Util.getBatteryStateRes(event.batteryState));
    }

    public void start() {
        mVvContent.start();
//        mVvPreview.start();
    }

    public void setVideo(Video video) {
        this.mVideo = video;
        mDefinitionAdapter.setData(mVideo.getList());
        Definition definition = video.getList().get(video.getList().size() - 1);
        mVvContent.setVideoPath(definition.getUrl());
        mTvDefinition.setText(definition.getName());
        mTvName.setText(video.getName());
//        mVvPreview.setVideoPath(video.getUri());
    }

    public void setHudView(TableLayout hudView) {
        mVvContent.setHudView(hudView);
    }

    public void setIsLive(boolean isLive) {
        this.mIsLive = isLive;
    }

    public void onDestroy() {
        mActivity.unregisterReceiver(mBatteryChangedReceiver);
        EventBus.getDefault().unregister(this);
    }
}
