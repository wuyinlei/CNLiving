package ruolan.com.cnliving;

import android.app.Application;
import android.content.Context;

import com.squareup.leakcanary.LeakCanary;
import com.tencent.TIMManager;
import com.tencent.TIMUserProfile;
import com.tencent.ilivesdk.ILiveSDK;
import com.tencent.livesdk.ILVLiveConfig;
import com.tencent.livesdk.ILVLiveManager;

import java.util.ArrayList;
import java.util.List;

import ruolan.com.cnliving.util.QnUploadHelper;

/**
 * Created by wuyinlei on 2017/11/14.
 *
 * @function 全局application
 */

public class CNApplication extends Application {


    private static CNApplication INSTANCE;
    private static Context appContext;
    private ILVLiveConfig mLiveConfig;

    private TIMUserProfile mSelfProfile;

    @Override
    public void onCreate() {
        super.onCreate();


        INSTANCE = this;
        appContext = getApplicationContext();
        ILiveSDK.getInstance().initSdk(getApplicationContext(), 1400050053, 10523);
        List<String> customInfos = new ArrayList<String>();
        customInfos.add(CustomProfile.CUSTOM_GET);
        customInfos.add(CustomProfile.CUSTOM_SEND);
        customInfos.add(CustomProfile.CUSTOM_LEVEL);
        customInfos.add(CustomProfile.CUSTOM_RENZHENG);
        TIMManager.getInstance().initFriendshipSettings(CustomProfile.allBaseInfo, customInfos);


        //初始化直播场景
        mLiveConfig = new ILVLiveConfig();
        ILVLiveManager.getInstance().init(mLiveConfig);

        QnUploadHelper.init("r5Gmrjbc9VZKnVIttU3JInHdW3iwK6SACE2_a-d3",
                "2saF0tYPYvND-oGCpcD8phMHJyhuN4mnffapc7HG",
                "http://ozejm5ujq.bkt.clouddn.com/",
                "ruolan");

        LeakCanary.install(this);
    }

    private void initLiveSdk() {

        //初始化腾讯直播SDK
        ILiveSDK.getInstance().initSdk(getContext(), 1400050053, 10523);

        ILVLiveConfig liveConfig = new ILVLiveConfig();
        ILVLiveManager.getInstance().init(liveConfig);

    }


    public static Context getContext() {
        return appContext;
    }


    public static CNApplication getApplication() {
        return INSTANCE;
    }


    public void setSelfProfile(TIMUserProfile userProfile) {

        mSelfProfile = userProfile;
    }

    public TIMUserProfile getSelfProfile() {
        return mSelfProfile;
    }

    public ILVLiveConfig getLiveConfig() {
        return mLiveConfig;
    }
}
