package com.dayudesgin.goldcoin;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.badlogic.gdx.backends.android.AndroidFragmentApplication;
import com.badoo.mobile.util.WeakHandler;
import com.dayudesgin.goldcoin.box2d.Box2DFragment;
import com.dayudesgin.goldcoin.box2d.SpringEffect;
import com.dayudesgin.goldcoin.box2d.Tools.GiftParticleContants;

import butterknife.Bind;
import butterknife.ButterKnife;

public class Box2dActivity extends AppCompatActivity implements AndroidFragmentApplication.Callbacks {
    private Box2DFragment m_box2dFgm;
    private WeakHandler m_weakHandler = new WeakHandler();
    private SystemReceiveBroadCast m_systemreceiveBroadCast;
    private boolean m_bCrazyMode = false;

    private int m_giftIndex = 1;
    private int m_giftCounter = 0;

    @Bind(R.id.lyt_container)
    public FrameLayout m_container;

    public static void launch(Context context) {
        Intent intent = new Intent();
        intent.setClass(context, Box2dActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.box2d_activity);

        ButterKnife.bind(this);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        m_systemreceiveBroadCast = new SystemReceiveBroadCast();
        IntentFilter filter1 = new IntentFilter();
        filter1.setPriority(800);
        filter1.addAction(GiftParticleContants.BROADCAST_GIFTPARTICLE_BACKKEY);
        registerReceiver(m_systemreceiveBroadCast, filter1);

        m_box2dFgm = new Box2DFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.lyt_container, m_box2dFgm).commit();
        showBox2dFgmFullScreen();

        SpringEffect.doEffectSticky(findViewById(R.id.sendStarBtn), new Runnable() {
            @Override
            public void run() {
                m_weakHandler.postDelayed(m_runnableSendStar, 50);
            }
        });
    }


    //左右式
    private boolean m_testleft = false;
    private Runnable m_runnableCrazyMode = new Runnable() {
        @Override
        public void run() {
            m_box2dFgm.addGift(15);
            m_testleft = !m_testleft;
            m_weakHandler.postDelayed(m_runnableCrazyMode, 50);
        }
    };
    private boolean m_testleft1 = false;
    private int counter = 0;
    private Runnable m_runnableSendGift = new Runnable() {
        @Override
        public void run() {
            if (counter == m_giftCounter) {
                counter = 0;
                return;
            }
            counter++;
            m_box2dFgm.addGift(m_giftIndex);
            m_testleft1 = !m_testleft1;
            m_weakHandler.postDelayed(m_runnableSendGift, 50);
        }
    };
    private boolean m_testleft2 = false;
    private Runnable m_runnableSendStar = new Runnable() {
        @Override
        public void run() {
            counter++;
            m_box2dFgm.addStar(m_testleft2, true);
            m_testleft2 = !m_testleft2;
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //广播解绑
        unregisterReceiver(m_systemreceiveBroadCast);
        //移除监听回调
        m_weakHandler.removeCallbacks(m_runnableCrazyMode);
        //黄油刀解绑
        ButterKnife.unbind(this);
    }

    @Override
    public void exit() {
    }

    //再次点击退出
    private long m_exitTime;

    private boolean checkquit() {
        if ((System.currentTimeMillis() - m_exitTime) > 2000) {
            Toast.makeText(this, "再次点击退出", Toast.LENGTH_SHORT).show();
            m_exitTime = System.currentTimeMillis();
        } else {
            finish();
        }
        return true;
    }

    public class SystemReceiveBroadCast extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Log.d(MainActivity.class.getSimpleName(), "SystemReceiveBroadCast[^^^^^^^]play Particle Receive: " + intent.getAction());
            if (intent.getAction().equals(GiftParticleContants.BROADCAST_GIFTPARTICLE_BACKKEY)) {
                checkquit();
            }
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        //当屏幕发生变化的时候，设置动画布局match_parent
        showBox2dFgmFullScreen();
    }

    private void showBox2dFgmFullScreen() {
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) m_container.getLayoutParams();
        params.width = RelativeLayout.LayoutParams.MATCH_PARENT;
        params.height = RelativeLayout.LayoutParams.MATCH_PARENT;
        m_container.setLayoutParams(params);
    }

    @Override
    public void finish() {
        m_box2dFgm.preDestory();
        super.finish();
    }
}
