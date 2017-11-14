package ruolan.com.cnliving;

import android.app.Application;
import android.content.Context;

/**
 * Created by wuyinlei on 2017/11/14.
 */

public class CNApplication extends Application {


    private static CNApplication app;
    private static Context appContext;

    @Override
    public void onCreate() {
        super.onCreate();

    }


    public static Context getContext() {
        return appContext;
    }

    public static CNApplication getApplication() {
        return app;
    }


}
