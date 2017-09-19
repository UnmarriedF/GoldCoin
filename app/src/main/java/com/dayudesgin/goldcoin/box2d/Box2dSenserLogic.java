package com.dayudesgin.goldcoin.box2d;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;

/**
 * 控制box2d 重力感应逻辑
 * 除了逻辑控制，已读完
 */

public class Box2dSenserLogic {
    /**
     * 传感器监听
     */
    private SensorEventListener sensorEventListener;
    /**
     * 系统所有传感器的管理器,有了它的实例之后就可以调用getDefaultSensor()方法来得到任意的传感器类型了
     * 另外始终要记得， 当程序退出或传感器使用完毕时， 一定要调用 unregisterListener ()方法将使用的资源释放掉
     */
    private SensorManager sensorManager;
    /**
     * 传感器（根据获取的传感器类型确定）
     */
    private Sensor sensor;

    private boolean m_isPortrait = true;

    private final static String TAG = Box2dSenserLogic.class.getSimpleName();
    /**
     * Box2D 世界场景(绘图区域)
     */
    private World mWorld;

    public Box2dSenserLogic(final World world, Context context) {
        mWorld = world;
        //获得重力感应硬件控制器
        sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        //添加重力感应侦听，并实现其方法，
        sensorEventListener = new SensorEventListener() {
            public void onSensorChanged(SensorEvent se) {
                float x = se.values[SensorManager.DATA_X];
                float y = se.values[SensorManager.DATA_Y];
//				Log.d(TAG, "x:" + x + ",y:" + y);
//				float m = x*90f/9.8f;
//				Log.d(TAG, "m:" + m);
                float xSenser, ySenser;
                if (m_isPortrait) {
                    xSenser = -5.0f * x;
                    ySenser = (y >= -1) ? -30 : -y * 5.0f;
                    if (y < -1) {
                        xSenser = -1 * xSenser;
                        ySenser = -1 * ySenser;
                    }
                } else {
                    xSenser = 5.0f * y;
                    ySenser = (x >= -1) ? -30 : -x * 5.0f;

                    if (x < -1) {
                        xSenser = -1 * xSenser;
                        ySenser = -1 * ySenser;
                    }
                }
                Vector2 vec2 = m_isPortrait ? new Vector2(xSenser, ySenser) : new Vector2(xSenser, ySenser);
                if (world != null)
                    //设置世界的重力方向
                    world.setGravity(vec2);
            }

            public void onAccuracyChanged(Sensor arg0, int arg1) {
            }
        };

    }

    public void setIsPortrait(boolean isPortrait) {
        m_isPortrait = isPortrait;
    }

    public void release() {
        sensorManager.unregisterListener(sensorEventListener);
    }

    public void startListener() {
        sensorManager.registerListener(sensorEventListener, sensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    public void stopListener() {
        sensorManager.unregisterListener(sensorEventListener);
    }
}
