package ruolan.com.cnliving.customerview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import ruolan.com.cnliving.R;
import ruolan.com.cnliving.model.GiftInfo;
import ruolan.com.cnliving.util.ImgUtils;

import static ruolan.com.cnliving.model.GiftInfo.Gift_Empty;

/**
 * Created by wuyinlei on 2017/11/18.
 *
 * @function
 */

public class GiftGridView extends GridView {

    public GiftGridView(Context context) {
        super(context);
        init();
    }


    public GiftGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public GiftGridView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private GifAdapter gridAdapter;

    private void init() {

        setNumColumns(4);
        gridAdapter = new GifAdapter();
        setAdapter(gridAdapter);

    }

    public void notifyDataSetChanged() {
        gridAdapter.notifyDataSetChanged();
    }

    public void setGiftInfoList(List<GiftInfo> giftInfos) {
        mGiftInfos.clear();
        mGiftInfos.addAll(giftInfos);
        gridAdapter.notifyDataSetChanged();
    }



    private List<GiftInfo> mGiftInfos = new ArrayList<>();

    public void addGifs(List<GiftInfo> giftInfos) {
        this.mGiftInfos = giftInfos;
    }

    private class GifAdapter extends BaseAdapter {

        @Override
        public int getCount() {

            return mGiftInfos.size();
        }

        @Override
        public Object getItem(int position) {
            return mGiftInfos.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View view, ViewGroup parent) {
            GiftHolder holder = null;
            if (view == null) {
                view = LayoutInflater.from(getContext()).inflate(R.layout.view_gift_item, parent, false);
                holder = new GiftHolder(view);
                view.setTag(holder);

            } else {
                holder = (GiftHolder) view.getTag();
            }

            GiftInfo giftInfo = mGiftInfos.get(position);
            holder.bindData(giftInfo);

            return view;
        }
    }

    private GiftInfo selectGiftInfo;

    public void setSelectGiftInfo(GiftInfo selectGiftInfo) {
        this.selectGiftInfo = selectGiftInfo;
    }

    public int getGridViewHeight() {
        //获取高度：adapter item 的高度 * 行数
        View item = gridAdapter.getView(0, null, this);
        item.measure(0, 0);
        int height = item.getMeasuredHeight();
        return height * 2;
    }


    private class GiftHolder {

        private View view;
        private ImageView giftImg;
        private TextView giftExp;
        private TextView giftName;
        private ImageView giftSelect;

        public GiftHolder(View view) {
            this.view = view;
            giftImg = (ImageView) view.findViewById(R.id.gift_img);
            giftExp = (TextView) view.findViewById(R.id.gift_exp);
            giftName = (TextView) view.findViewById(R.id.gift_name);
            giftSelect = (ImageView) view.findViewById(R.id.gift_select);
        }

        @SuppressLint("SetTextI18n")
        public void bindData(final GiftInfo giftInfo) {

            ImgUtils.load(giftInfo.giftResId, giftImg);
            if (giftInfo != Gift_Empty) {
                giftExp.setText(giftInfo.expValue + "经验值");
                giftName.setText(giftInfo.name);
                if (giftInfo == selectGiftInfo) {
                    giftSelect.setImageResource(R.drawable.gift_selected);
                } else {
                    if (giftInfo.type == GiftInfo.Type.ContinueGift) {
                        giftSelect.setImageResource(R.drawable.gift_repeat);
                    } else if (giftInfo.type == GiftInfo.Type.FullScreenGift) {
                        giftSelect.setImageResource(R.drawable.gift_none);
                    }
                }
            } else {
                giftExp.setText("");
                giftName.setText("");
                giftSelect.setImageResource(R.drawable.gift_none);
            }

            view.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (giftInfo == Gift_Empty) {
                        return;
                    }

                    if (giftInfo == selectGiftInfo) {
                        if (mOnGiftItemClickListener != null) {
                            mOnGiftItemClickListener.onClick(null);
                        }
                    } else {
                        if (mOnGiftItemClickListener != null) {
                            mOnGiftItemClickListener.onClick(giftInfo);
                        }
                    }

                }
            });
        }
    }

    private OnGiftItemClickListener mOnGiftItemClickListener;

    public void setOnGiftItemClickListener(OnGiftItemClickListener l) {
        mOnGiftItemClickListener = l;
    }

    public interface OnGiftItemClickListener {
        void onClick(GiftInfo giftInfo);
    }

}
