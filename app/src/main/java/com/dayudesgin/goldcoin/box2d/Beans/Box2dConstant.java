package com.dayudesgin.goldcoin.box2d.Beans;

/**
 * Box2d功能常量定义
 */
public class Box2dConstant {
    /**
     * 允许的最大数量的金币
     */
    public static final int MaxBallCounter = 80;
    /**
     * 金币的生命长短,单位秒
     */
    public static final float MaxBallLifer = 8f;
    /**
     * 金币显示的时间 = 金币生命时间 - 消失过程所占用的时间
     */
    public static final float MaxBallLiferSP = MaxBallLifer - 0.4f;

}
