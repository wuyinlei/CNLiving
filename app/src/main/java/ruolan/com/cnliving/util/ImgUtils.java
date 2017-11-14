package ruolan.com.cnliving.util;

import android.widget.ImageView;

import com.bumptech.glide.Glide;

import jp.wasabeef.glide.transformations.CropCircleTransformation;
import ruolan.com.cnliving.CNApplication;

/**
 * Created by Administrator on 2017/4/3.
 */

public class ImgUtils {

    public static void load(String url, ImageView targetView) {
        Glide.with(CNApplication.getContext())
                .load(url)
                .into(targetView);
    }

    public static void load(int resId, ImageView targetView) {
        Glide.with(CNApplication.getContext())
                .load(resId)
                .into(targetView);
    }

    public static void loadRound(String url, ImageView targetView) {
        Glide.with(CNApplication.getContext())
                .load(url)
                .bitmapTransform(new CropCircleTransformation(CNApplication.getContext()))
                .into(targetView);
    }

    public static void loadRound(int resId, ImageView targetView) {
        Glide.with(CNApplication.getContext())
                .load(resId)
                .bitmapTransform(new CropCircleTransformation(CNApplication.getContext()))
                .into(targetView);
    }
}
