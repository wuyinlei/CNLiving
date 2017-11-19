package ruolan.com.cnliving.customerview;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import com.tencent.TIMUserProfile;

import java.util.LinkedList;
import java.util.List;

import ruolan.com.cnliving.R;
import ruolan.com.cnliving.model.GiftInfo;

/**
 * Created by wuyinlei on 2017/11/19.
 *
 * @function
 */

public class GiftRepeatView extends LinearLayout {

    private GiftRepeatItemView item0, item1;



    private class GiftSenderAndInfo {
        public GiftInfo giftInfo;
        public String repeatId;
        public TIMUserProfile senderProfile;
    }

    private List<GiftSenderAndInfo> giftSenderAndInfoList = new LinkedList<GiftSenderAndInfo>();



    public GiftRepeatView(Context context) {
        super(context);
        init();
    }

    public GiftRepeatView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public GiftRepeatView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.view_gift_repeat, this, true);
        findAllViews();
        item0.setVisibility(INVISIBLE);
        item1.setVisibility(INVISIBLE);
    }

    private void findAllViews() {
        item0 = (GiftRepeatItemView) findViewById(R.id.item0);
        item1 = (GiftRepeatItemView) findViewById(R.id.item1);

    }

    public void showGift(GiftInfo giftInfo, String repeatId, TIMUserProfile selfProfile) {

        GiftRepeatItemView avaliableView = getAvaliableView(giftInfo, repeatId, selfProfile);
        if (avaliableView == null) {
            GiftSenderAndInfo info = new GiftSenderAndInfo();
            info.giftInfo = giftInfo;
            info.senderProfile = selfProfile;
            info.repeatId = repeatId;
            giftSenderAndInfoList.add(info);
        } else {
            avaliableView.showGift(giftInfo, repeatId, selfProfile);
        }

    }

    private GiftRepeatItemView getAvaliableView(GiftInfo giftInfo, String repeatId, TIMUserProfile profile) {

        if (item0.isAvaliable(giftInfo, repeatId, profile)) {
            return item0;
        }

        if (item1.isAvaliable(giftInfo, repeatId, profile)) {
            return item1;
        }

        if (item0.getVisibility() == INVISIBLE) {
            return item0;
        }
        if (item1.getVisibility() == INVISIBLE) {
            return item1;
        }

        return null;
    }

}
