package ruolan.com.cnliving.ui.watcher;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.google.gson.Gson;
import com.tencent.TIMMessage;
import com.tencent.TIMUserProfile;
import com.tencent.av.sdk.AVRoomMulti;
import com.tencent.ilivesdk.ILiveCallBack;
import com.tencent.ilivesdk.core.ILiveRoomManager;
import com.tencent.ilivesdk.view.AVRootView;
import com.tencent.livesdk.ILVCustomCmd;
import com.tencent.livesdk.ILVLiveConfig;
import com.tencent.livesdk.ILVLiveConstants;
import com.tencent.livesdk.ILVLiveManager;
import com.tencent.livesdk.ILVLiveRoomOption;
import com.tencent.livesdk.ILVText;

import ruolan.com.cnliving.CNApplication;
import ruolan.com.cnliving.R;
import ruolan.com.cnliving.customerview.BottomControlView;
import ruolan.com.cnliving.customerview.ChatMsgListView;
import ruolan.com.cnliving.customerview.ChatView;
import ruolan.com.cnliving.customerview.DanmuView;
import ruolan.com.cnliving.model.ChatMsgInfo;
import ruolan.com.cnliving.model.Constants;
import ruolan.com.cnliving.model.GiftCmdInfo;
import ruolan.com.cnliving.model.GiftInfo;
import ruolan.com.cnliving.widget.GiftSelectDialog;
import ruolan.com.cnliving.widget.SizeChangeRelativeLayout;

/**
 * Created by wuyinlei on 2017/11/17.
 *
 * @function
 */

public class WatcherActivity extends AppCompatActivity {

    private String hostId;
    private int mRoomId;
    private SizeChangeRelativeLayout mSizeChangeLayout;
    private AVRootView mLiveView;
    private BottomControlView mControlView;
    private ChatView mChatView;
    private ChatMsgListView mChatListView;
    private DanmuView mDanmuView;
    private GiftSelectDialog giftSelectDialog;


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

        ILVLiveConfig liveConfig = CNApplication.getApplication().getLiveConfig();
        liveConfig.setLiveMsgListener(new ILVLiveConfig.ILVLiveMsgListener() {
            @Override
            public void onNewTextMsg(ILVText text, String SenderId, TIMUserProfile userProfile) {
                //接收到文本消息


            }

            @Override
            public void onNewCustomMsg(ILVCustomCmd cmd, String sendId, TIMUserProfile userProfile) {
                //接收到自定义消息
                if (cmd.getCmd() == Constants.CMD_CHAT_MSG_LIST) {
                    //得到的是聊天列表消息
                    String content = cmd.getParam();
                    ChatMsgInfo chatMsgInfo = ChatMsgInfo.createDanmuInfo(content,
                            sendId,
                            userProfile.getFaceUrl(),
                            userProfile.getNickName());
                    mChatListView.addMsgInfo(chatMsgInfo);

                } else if (cmd.getCmd() == Constants.CMD_CHAT_MSG_DANMU) {
                    //得到的是弹幕消息
                    String content = cmd.getParam();
                    ChatMsgInfo info = ChatMsgInfo.createListInfo(content, sendId, userProfile.getFaceUrl());
                    mChatListView.addMsgInfo(info);

                    String name = userProfile.getNickName();
                    if (TextUtils.isEmpty(name)) {
                        name = userProfile.getIdentifier();
                    }
                    ChatMsgInfo danmuInfo = ChatMsgInfo.createDanmuInfo(content, sendId, userProfile.getFaceUrl(), name);
                    mDanmuView.addMsgInfo(danmuInfo);

                } else if (cmd.getCmd() == Constants.CMD_CHAT_GIFT) {
                    //得到的消息是自定义的礼物

                } else if (cmd.getCmd() == ILVLiveConstants.ILVLIVE_CMD_ENTER) {
                    //用户进入直播

                } else if (cmd.getCmd() == ILVLiveConstants.ILVLIVE_CMD_LEAVE) {
                    //用户离开消息

                }

            }

            @Override
            public void onNewOtherMsg(TIMMessage message) {
                //接收到其他消息
            }
        });


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


        finish();

    }

    private void findAllViews() {
        mSizeChangeLayout = (SizeChangeRelativeLayout) findViewById(R.id.size_change_layout);
        mSizeChangeLayout.setOnSizeChangeListener(new SizeChangeRelativeLayout.OnSizeChangeListener() {
            @Override
            public void onLarge() {
                //键盘隐藏
                mChatView.setVisibility(View.INVISIBLE);
                mControlView.setVisibility(View.VISIBLE);
            }

            @Override
            public void onSmall() {
                //键盘显示
            }
        });

        mLiveView = (AVRootView) findViewById(R.id.live_view);
        ILVLiveManager.getInstance().setAvVideoView(mLiveView);

        mControlView = (BottomControlView) findViewById(R.id.control_view);
        mControlView.setIsHost(true);
        mControlView.setOnControlListener(new BottomControlView.OnControlListener() {
            @Override
            public void onChatClick() {
                //点击了聊天按钮，显示聊天操作栏
                mChatView.setVisibility(View.VISIBLE);
                mControlView.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onCloseClick() {
                // 点击了关闭按钮，关闭直播
                quitLive();
            }

            @Override
            public void onGiftClick() {
                //主播界面，不能发送礼物
                //显示礼物九宫格
                if (giftSelectDialog == null) {
                    giftSelectDialog = new GiftSelectDialog(WatcherActivity.this);

                    giftSelectDialog.setGiftSendListener(giftSendListener);
                }
                giftSelectDialog.show();
            }

            @Override
            public void onOptionClick(View view) {
                //显示主播操作对话框
            }
        });

        mChatView = (ChatView) findViewById(R.id.chat_view);
        mChatView.setOnChatSendListener(new ChatView.OnChatSendListener() {
            @Override
            public void onChatSend(ILVCustomCmd msg) {
                //发送消息
                if (msg.getCmd() == Constants.CMD_CHAT_MSG_LIST) {
                    String chatContent = msg.getParam();
                    String sendId = CNApplication.getApplication().getSelfProfile().getIdentifier();
                    String avatar = CNApplication.getApplication().getSelfProfile().getFaceUrl();

                    ChatMsgInfo info = ChatMsgInfo.createListInfo(chatContent, sendId, avatar);
                    mChatListView.addMsgInfo(info);

                } else if (msg.getCmd() == Constants.CMD_CHAT_MSG_DANMU){
                    String chatContent = msg.getParam();
                    String userId = CNApplication.getApplication().getSelfProfile().getIdentifier();
                    String avatar = CNApplication.getApplication().getSelfProfile().getFaceUrl();
                    ChatMsgInfo info = ChatMsgInfo.createListInfo(chatContent, userId, avatar);
                    mChatListView.addMsgInfo(info);

                    String name = CNApplication.getApplication().getSelfProfile().getNickName();
                    if (TextUtils.isEmpty(name)) {
                        name = userId;
                    }
                    ChatMsgInfo danmuInfo = ChatMsgInfo.createDanmuInfo(chatContent, userId, avatar, name);
                    mDanmuView.addMsgInfo(danmuInfo);


                }


            }
        });

        mControlView.setVisibility(View.VISIBLE);
        mChatView.setVisibility(View.INVISIBLE);

        mChatListView = (ChatMsgListView) findViewById(R.id.chat_list);
        mDanmuView = (DanmuView) findViewById(R.id.danmu_view);

    }


    private GiftSelectDialog.OnGiftSendListener giftSendListener = new GiftSelectDialog.OnGiftSendListener() {
        @Override
        public void onGiftSendClick(final ILVCustomCmd customCmd) {
            customCmd.setDestId(ILiveRoomManager.getInstance().getIMGroupId());

            ILVLiveManager.getInstance().sendCustomCmd(customCmd, new ILiveCallBack<TIMMessage>() {
                @Override
                public void onSuccess(TIMMessage data) {
                    if (customCmd.getCmd() == Constants.CMD_CHAT_GIFT) {
                        //界面显示礼物动画。
                        GiftCmdInfo giftCmdInfo = new Gson().fromJson(customCmd.getParam(), GiftCmdInfo.class);
                        int giftId = giftCmdInfo.giftId;
                        String repeatId = giftCmdInfo.repeatId;
                        GiftInfo giftInfo = GiftInfo.getGiftById(giftId);
                        if (giftInfo == null) {
                            return;
                        }
                        if (giftInfo.type == GiftInfo.Type.ContinueGift) {


                        } else if (giftInfo.type == GiftInfo.Type.FullScreenGift) {
                            //全屏礼物

                        }
                    }
                }

                @Override
                public void onError(String module, int errCode, String errMsg) {
                }

            });
        }
    };

    @Override
    protected void onPause() {
        super.onPause();
        ILVLiveManager.getInstance().onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        ILVLiveManager.getInstance().onResume();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        quitLive();
    }

    private void quitLive() {

        finish();
    }

}
