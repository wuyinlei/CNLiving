package ruolan.com.cnliving.model;

import android.annotation.SuppressLint;
import android.support.annotation.IdRes;

import ruolan.com.cnliving.R;

/**
 * Created by wuyinlei on 2017/11/18.
 */

public class GiftInfo {


    public static enum Type {
        ContinueGift, FullScreenGift
    }

    public int giftResId;
    public int expValue;
    public String name;
    public Type type;
    public int giftId;

    public GiftInfo(int giftId, @IdRes int resId, int exp, String name, Type type) {
        this.giftId = giftId;
        this.giftResId = resId;
        this.type = type;
        this.expValue = exp;
        this.name = name;
    }
    @SuppressLint("ResourceType")
    public static GiftInfo Gift_Heart = new GiftInfo(-1, 0, 1, "💖", Type.ContinueGift);

    @SuppressLint("ResourceType")
    public static GiftInfo Gift_Empty = new GiftInfo(0, R.drawable.gift_none, 0, "", Type.ContinueGift);
    @SuppressLint("ResourceType")
    public static GiftInfo Gift_BingGun = new GiftInfo(1, R.drawable.gift_1, 1, "冰棍", Type.ContinueGift);
    @SuppressLint("ResourceType")
    public static GiftInfo Gift_BingJiLing = new GiftInfo(2, R.drawable.gift_2, 5, "冰激凌", Type.ContinueGift);
    @SuppressLint("ResourceType")
    public static GiftInfo Gift_MeiGui = new GiftInfo(3, R.drawable.gift_3, 10, "玫瑰花", Type.ContinueGift);
    @SuppressLint("ResourceType")
    public static GiftInfo Gift_PiJiu = new GiftInfo(4, R.drawable.gift_4, 15, "啤酒", Type.ContinueGift);
    @SuppressLint("ResourceType")
    public static GiftInfo Gift_HongJiu = new GiftInfo(5, R.drawable.gift_5, 20, "红酒", Type.ContinueGift);
    @SuppressLint("ResourceType")
    public static GiftInfo Gift_Hongbao = new GiftInfo(6, R.drawable.gift_6, 50, "红包", Type.ContinueGift);
    @SuppressLint("ResourceType")
    public static GiftInfo Gift_ZuanShi = new GiftInfo(7, R.drawable.gift_7, 100, "钻石", Type.ContinueGift);
    @SuppressLint("ResourceType")
    public static GiftInfo Gift_BaoXiang = new GiftInfo(8, R.drawable.gift_8, 200, "宝箱", Type.ContinueGift);
    @SuppressLint("ResourceType")
    public static GiftInfo Gift_BaoShiJie = new GiftInfo(9, R.drawable.gift_9, 1000, "保时捷", Type.FullScreenGift);

    public static GiftInfo getGiftById(int id) {
        switch (id) {
            case -1:

                return Gift_Heart;
            case 1:
                return Gift_BingGun;
            case 2:
                return Gift_BingJiLing;
            case 3:
                return Gift_MeiGui;
            case 4:
                return Gift_PiJiu;
            case 5:
                return Gift_HongJiu;
            case 6:
                return Gift_Hongbao;
            case 7:
                return Gift_ZuanShi;
            case 8:
                return Gift_BaoXiang;
            case 9:
                return Gift_BaoShiJie;
        }

        return null;
    }

}
