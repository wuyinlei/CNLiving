package ruolan.com.cnliving.ui.live;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.tencent.TIMUserProfile;

import ruolan.com.cnliving.CNApplication;
import ruolan.com.cnliving.R;
import ruolan.com.cnliving.model.RoomInfo;
import ruolan.com.cnliving.ui.hostlive.HostLiveActivity;
import ruolan.com.cnliving.util.ImgUtils;
import ruolan.com.cnliving.util.PicChooserHelper;

/**
 * Created by wuyinlei on 2017/11/14.
 *
 * @function
 */

public class CreateLiveActivity extends AppCompatActivity {

    private View mSetCoverView;
    private ImageView mCoverImg;
    private TextView mCoverTipTxt;
    private EditText mTitleEt;
    private TextView mCreateRoomBtn;
    private TextView mRoomNoText;
    private EditText mEtLiveTitle;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_room);

        findAllViews();
        setListeners();
        setupTitlebar();
    }

    private void findAllViews() {
        mSetCoverView = findViewById(R.id.set_cover);
        mCoverImg = (ImageView) findViewById(R.id.cover);
        mCoverTipTxt = (TextView) findViewById(R.id.tv_pic_tip);
        mTitleEt = (EditText) findViewById(R.id.title);
        mCreateRoomBtn = (TextView) findViewById(R.id.create);
        mRoomNoText = (TextView) findViewById(R.id.room_no);
        mEtLiveTitle = (EditText) findViewById(R.id.et_live_title);
    }

    private void setListeners() {
        mSetCoverView.setOnClickListener(clickListener);
        mCreateRoomBtn.setOnClickListener(clickListener);
    }

    private void setupTitlebar() {
        Toolbar titlebar = (Toolbar) findViewById(R.id.titlebar);
        titlebar.setTitle("开始我的直播");
        titlebar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(titlebar);
    }

    private View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            int id = view.getId();
            if (id == R.id.create) {
                mCreateRoomBtn.setClickable(false);
                //创建直播
                requestCreateRoom();
            } else if (id == R.id.set_cover) {
                //选择图片
                choosePic();
            }
        }
    };

    private PicChooserHelper mPicChooserHelper;

    private void choosePic() {
        if (mPicChooserHelper == null) {
            mPicChooserHelper = new PicChooserHelper(this, PicChooserHelper.PicType.Cover);
            mPicChooserHelper.setOnChooseResultListener(new PicChooserHelper.OnChooseResultListener() {
                @Override
                public void onSuccess(String url) {
                    //获取图片成功
                    updateCover(url);
                }

                @Override
                public void onFail(String msg) {
                    //获取图片失败
                    Toast.makeText(CreateLiveActivity.this, "选择失败：" + msg, Toast.LENGTH_SHORT).show();
                }
            });
        }
        mPicChooserHelper.showPicChooserDialog();

    }

    private String coverUrl = null;

    private void updateCover(String url) {
        coverUrl = url;
        ImgUtils.load(url, mCoverImg);
        mCoverTipTxt.setVisibility(View.GONE);
    }


    private void requestCreateRoom() {

        CreateRoomRequest.CreateRoomParam param = new CreateRoomRequest.CreateRoomParam();
        TIMUserProfile selfProfile = CNApplication.getApplication().getSelfProfile();
        param.userId = selfProfile.getIdentifier();
        param.userAvatar = selfProfile.getFaceUrl();
        String nickName = selfProfile.getNickName();
        param.userName = TextUtils.isEmpty(nickName) ? selfProfile.getIdentifier() : nickName;
        String liveTitle = mEtLiveTitle.getText().toString();
        if (TextUtils.isEmpty(liveTitle)) {
            Toast.makeText(this, "直播标题不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        param.liveTitle = liveTitle;
        if (TextUtils.isEmpty(coverUrl)) {
            coverUrl = "http://ozejm5ujq.bkt.clouddn.com/_1510838000902_avatar";
        }
        param.liveCover = coverUrl;

        //创建房间
        CreateRoomRequest request = new CreateRoomRequest();
//        request.getUrl(param);
        request.request(request.getUrl(param));
        request.setOnResultListener(new BaseRequest.OnResultListener<RoomInfo>() {
            @Override
            public void onFail(int code, String msg) {
                Toast.makeText(CreateLiveActivity.this, "请求失败：" + msg, Toast.LENGTH_SHORT).show();
                mCreateRoomBtn.setClickable(true);
            }

            @Override
            public void onSuccess(RoomInfo roomInfo) {
                Toast.makeText(CreateLiveActivity.this, "请求成功：" + roomInfo.roomId, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent();
                intent.setClass(CreateLiveActivity.this, HostLiveActivity.class);
                intent.putExtra("roomId", roomInfo.roomId);
                startActivity(intent);

                finish();

                mCreateRoomBtn.setClickable(true);
            }

        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (mPicChooserHelper != null) {
            mPicChooserHelper.onActivityResult(requestCode, resultCode, data);
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}
