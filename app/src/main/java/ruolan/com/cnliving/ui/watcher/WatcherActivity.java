package ruolan.com.cnliving.ui.watcher;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.widget.Toast;

import com.tencent.av.sdk.AVRoomMulti;
import com.tencent.ilivesdk.ILiveCallBack;
import com.tencent.ilivesdk.view.AVRootView;
import com.tencent.livesdk.ILVLiveManager;
import com.tencent.livesdk.ILVLiveRoomOption;

import ruolan.com.cnliving.R;

/**
 * Created by wuyinlei on 2017/11/17.
 *
 * @function
 */

public class WatcherActivity extends AppCompatActivity {

    private String hostId;
    private int mRoomId;
    private AVRootView mLiveView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_watcher);

        findAllViews();

        joinRoom();

    }

    private void joinRoom() {
        mRoomId = getIntent().getIntExtra("roomId", -1);
        hostId = getIntent().getStringExtra("hostId");
        if (mRoomId < 0 || TextUtils .isEmpty(hostId)) {
            return;
        }

        //加入房间配置项
        ILVLiveRoomOption memberOption = new ILVLiveRoomOption(hostId)
                .autoCamera(false) //是否自动打开摄像头
                .controlRole("Guest") //角色设置
                .authBits(AVRoomMulti.AUTH_BITS_JOIN_ROOM | AVRoomMulti.AUTH_BITS_RECV_AUDIO | AVRoomMulti.AUTH_BITS_RECV_CAMERA_VIDEO | AVRoomMulti.AUTH_BITS_RECV_SCREEN_VIDEO) //权限设置
                .videoRecvMode(AVRoomMulti.VIDEO_RECV_MODE_SEMI_AUTO_RECV_CAMERA_VIDEO) //是否开始半自动接收
                .autoMic(false);//是否自动打开mic

        //加入房间
        ILVLiveManager.getInstance().joinRoom(mRoomId, memberOption, new ILiveCallBack() {
            @Override
            public void onSuccess(Object data) {
                //开始心形动画
                startHeartAnim();
                //同时发送进入直播的消息。
                sendEnterRoomMsg();
                //显示主播的头像
                updateTitleView();
                //开始心跳包
                startHeartBeat();
            }

            @Override
            public void onError(String module, int errCode, String errMsg) {
                Toast.makeText(WatcherActivity.this,"直播已结束",Toast.LENGTH_SHORT).show();
                quitRoom();
            }
        });



    }

    private void startHeartBeat() {



    }

    private void updateTitleView() {

    }

    private void sendEnterRoomMsg() {


    }

    private void startHeartAnim() {



    }

    private void quitRoom() {



    }

    private void findAllViews() {

    }
}
