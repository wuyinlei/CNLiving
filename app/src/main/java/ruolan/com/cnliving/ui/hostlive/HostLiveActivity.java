package ruolan.com.cnliving.ui.hostlive;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.google.gson.Gson;
import com.tencent.TIMCallBack;
import com.tencent.TIMFriendshipManager;
import com.tencent.TIMMessage;
import com.tencent.TIMUserProfile;
import com.tencent.TIMValueCallBack;
import com.tencent.av.sdk.AVRoomMulti;
import com.tencent.ilivesdk.ILiveCallBack;
import com.tencent.ilivesdk.ILiveConstants;
import com.tencent.ilivesdk.core.ILiveLoginManager;
import com.tencent.ilivesdk.core.ILiveRoomManager;
import com.tencent.ilivesdk.view.AVRootView;
import com.tencent.livesdk.ILVCustomCmd;
import com.tencent.livesdk.ILVLiveConfig;
import com.tencent.livesdk.ILVLiveConstants;
import com.tencent.livesdk.ILVLiveManager;
import com.tencent.livesdk.ILVLiveRoomOption;
import com.tencent.livesdk.ILVText;


import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import ruolan.com.cnliving.CNApplication;
import ruolan.com.cnliving.CustomProfile;
import ruolan.com.cnliving.R;
import ruolan.com.cnliving.customerview.BottomControlView;
import ruolan.com.cnliving.customerview.ChatMsgListView;
import ruolan.com.cnliving.customerview.ChatView;
import ruolan.com.cnliving.customerview.DanmuView;
import ruolan.com.cnliving.customerview.GiftFullView;
import ruolan.com.cnliving.customerview.GiftRepeatView;
import ruolan.com.cnliving.customerview.TitleView;
import ruolan.com.cnliving.customerview.VipEnterView;
import ruolan.com.cnliving.model.ChatMsgInfo;
import ruolan.com.cnliving.model.Constants;
import ruolan.com.cnliving.model.GiftCmdInfo;
import ruolan.com.cnliving.model.GiftInfo;
import ruolan.com.cnliving.model.UserInfo;
import ruolan.com.cnliving.ui.watcher.SendOrGetGiftRequest;
import ruolan.com.cnliving.util.request.BaseRequest;
import ruolan.com.cnliving.widget.HostControlDialog;
import ruolan.com.cnliving.widget.SizeChangeRelativeLayout;
import tyrantgit.widget.HeartLayout;

/**
 * Created by wuyinlei on 2017/11/15.
 *
 * @function
 */

public class HostLiveActivity extends AppCompatActivity {

    private SizeChangeRelativeLayout mSizeChangeLayout;
    private AVRootView mLiveView;
    private BottomControlView mControlView;
    private ChatView mChatView;
    private ChatMsgListView mChatListView;
    private DanmuView mDanmuView;
    private GiftRepeatView giftRepeatView;
    private GiftFullView giftFullView;
    private TitleView mTitleView;

    private HeartLayout heartLayout;

    private VipEnterView mVipEnterView;

    private HostControlState hostControlState;
    private FlashlightHelper flashlightHelper;

    private int mRoomId;

    private Timer heartTimer = new Timer();
    private Timer heartBeatTimer = new Timer();
    private Random heartRandom = new Random();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_host_live);

        findAllViews();

        createLive();
    }

    //创建直播
    private void createLive() {

        hostControlState = new HostControlState();
        flashlightHelper = new FlashlightHelper();

        mRoomId = getIntent().getIntExtra("roomId", -1);
        if (mRoomId < 0) {
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
                    //界面显示礼物动画。
                    GiftCmdInfo giftCmdInfo = new Gson().fromJson(cmd.getParam(), GiftCmdInfo.class);
                    int giftId = giftCmdInfo.giftId;
                    String repeatId = giftCmdInfo.repeatId;
                    GiftInfo giftInfo = GiftInfo.getGiftById(giftId);
                    if (giftInfo == null) {
                        return;
                    }
                    if (giftInfo.giftId == GiftInfo.Gift_Heart.giftId) {
                        heartLayout.addHeart(getRandomColor());
                    } else if (giftInfo.type == GiftInfo.Type.ContinueGift) {
                        giftRepeatView.showGift(giftInfo, repeatId, userProfile);
                    } else if (giftInfo.type == GiftInfo.Type.FullScreenGift) {
                        //全屏礼物
                        giftFullView.showGift(giftInfo, userProfile);
                    }

                    SendOrGetGiftRequest.SendOrGetGiftParam param = new SendOrGetGiftRequest.SendOrGetGiftParam();
                    TIMUserProfile selfProfile = CNApplication.getApplication().getSelfProfile();
                    param.userId = selfProfile.getIdentifier();
                    param.exp = 0;

                    //接收礼物
                    SendOrGetGiftRequest request = new SendOrGetGiftRequest();
                    request.request(request.getUrl(param, false));
                    request.setOnResultListener(new BaseRequest.OnResultListener<UserInfo>() {
                        @Override
                        public void onFail(int code, String msg) {

                        }

                        @Override
                        public void onSuccess(UserInfo userInfo) {

                            //更新用户等级信息
                            TIMFriendshipManager.getInstance().setCustomInfo(CustomProfile.CUSTOM_LEVEL,
                                    (userInfo.getUserLevel() + "").getBytes(), new TIMCallBack() {
                                        @Override
                                        public void onError(int i, String s) {

                                        }

                                        @Override
                                        public void onSuccess() {
                                            getSelfProfile();
                                        }
                                    });

                            TIMFriendshipManager.getInstance().setCustomInfo(CustomProfile.CUSTOM_GET,
                                    (userInfo.getGetNums() + "").getBytes(), new TIMCallBack() {
                                        @Override
                                        public void onError(int i, String s) {

                                        }

                                        @Override
                                        public void onSuccess() {
                                            getSelfProfile();
                                        }
                                    });

                            TIMFriendshipManager.getInstance().setCustomInfo(CustomProfile.CUSTOM_SEND,
                                    (userInfo.getSendNums() + "").getBytes(), new TIMCallBack() {
                                        @Override
                                        public void onError(int i, String s) {

                                        }

                                        @Override
                                        public void onSuccess() {
                                            getSelfProfile();
                                        }
                                    });
                        }


                    });


                } else if (cmd.getCmd() == ILVLiveConstants.ILVLIVE_CMD_ENTER) {
                    //用户进入直播
                    mTitleView.addWatcher(userProfile);
                    mVipEnterView.showVipEnter(userProfile);

                } else if (cmd.getCmd() == ILVLiveConstants.ILVLIVE_CMD_LEAVE) {
                    //用户离开消息
                    mTitleView.removeWatcher(userProfile);
                }

            }

            @Override
            public void onNewOtherMsg(TIMMessage message) {
                //接收到其他消息
            }
        });

        //创建房间配置项
        ILVLiveRoomOption hostOption = new ILVLiveRoomOption(ILiveLoginManager.getInstance().getMyUserId()).
                controlRole("LiveMaster")//角色设置
                .autoFocus(true)
                .autoMic(hostControlState.isVoiceOn())
                .authBits(AVRoomMulti.AUTH_BITS_DEFAULT)//权限设置
                .cameraId(hostControlState.getCameraid())//摄像头前置后置
                .videoRecvMode(AVRoomMulti.VIDEO_RECV_MODE_SEMI_AUTO_RECV_CAMERA_VIDEO);//是否开始半自动接收


        //创建房间
        ILVLiveManager.getInstance().createRoom(mRoomId, hostOption, new ILiveCallBack() {
            @Override
            public void onSuccess(Object data) {
                //开始心形动画
                startHeartAnim();
                //开始发送心跳
                startHeartBeat();
            }


            @Override
            public void onError(String module, int errCode, String errMsg) {
                //失败的情况下，退出界面。
                Toast.makeText(HostLiveActivity.this, "创建直播失败！", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }


    //开始发送心跳
    private void startHeartBeat() {


        heartBeatTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                //发送心跳包

            }
        }, 0, 4000); //4秒钟 。服务器是10秒钟去检测一次。

    }

    //开始心形动画
    private void startHeartAnim() {
        heartTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                heartLayout.post(new Runnable() {
                    @Override
                    public void run() {
                        heartLayout.addHeart(getRandomColor());
                    }
                });
            }
        }, 0, 1000); //1秒钟
    }

    private int getRandomColor() {
        return Color.rgb(heartRandom.nextInt(255), heartRandom.nextInt(255), heartRandom.nextInt(255));
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
            }

            @Override
            public void onOptionClick(View view) {
                //显示主播操作对话框

                boolean beautyOn = hostControlState.isBeautyOn();
                boolean flashOn = flashlightHelper.isFlashLightOn();
                boolean voiceOn = hostControlState.isVoiceOn();

                HostControlDialog hostControlDialog = new HostControlDialog(HostLiveActivity.this);

                hostControlDialog.setOnControlClickListener(controlClickListener);
                hostControlDialog.updateView(beautyOn, flashOn, voiceOn);
                hostControlDialog.show(view);
            }
        });

        mChatView = (ChatView) findViewById(R.id.chat_view);
        mChatView.setOnChatSendListener(new ChatView.OnChatSendListener() {
            @Override
            public void onChatSend(final ILVCustomCmd msg) {

                //发送消息
                msg.setDestId(ILiveRoomManager.getInstance().getIMGroupId());


                ILVLiveManager.getInstance().sendCustomCmd(msg, new ILiveCallBack() {
                    @Override
                    public void onSuccess(Object data) {
                        //发送消息
                        if (msg.getCmd() == Constants.CMD_CHAT_MSG_LIST) {
                            String chatContent = msg.getParam();
                            String sendId = CNApplication.getApplication().getSelfProfile().getIdentifier();
                            String avatar = CNApplication.getApplication().getSelfProfile().getFaceUrl();

                            ChatMsgInfo info = ChatMsgInfo.createListInfo(chatContent, sendId, avatar);
                            mChatListView.addMsgInfo(info);

                        } else if (msg.getCmd() == Constants.CMD_CHAT_MSG_DANMU) {
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

                    @Override
                    public void onError(String module, int errCode, String errMsg) {

                    }
                });


            }
        });

        mControlView.setVisibility(View.VISIBLE);
        mChatView.setVisibility(View.INVISIBLE);

        mChatListView = (ChatMsgListView) findViewById(R.id.chat_list);
        mDanmuView = (DanmuView) findViewById(R.id.danmu_view);
        giftRepeatView = (GiftRepeatView) findViewById(R.id.gift_repeat_view);
        giftFullView = (GiftFullView) findViewById(R.id.gift_full_view);
        mTitleView = (TitleView) findViewById(R.id.title_view);
        mTitleView.setHost(CNApplication.getApplication().getSelfProfile());

        mVipEnterView = (VipEnterView) findViewById(R.id.vip_enter);

        heartLayout = (HeartLayout) findViewById(R.id.heart_layout);
    }

    private HostControlDialog.OnControlClickListener controlClickListener = new HostControlDialog.OnControlClickListener() {
        @Override
        public void onBeautyClick() {
            //点击美颜
            boolean isBeautyOn = hostControlState.isBeautyOn();
            if (isBeautyOn) {
                //关闭美颜
                ILiveRoomManager.getInstance().enableBeauty(0);
                hostControlState.setBeautyOn(false);
            } else {
                //打开美颜
                ILiveRoomManager.getInstance().enableBeauty(50);
                hostControlState.setBeautyOn(true);
            }
        }

        @Override
        public void onFlashClick() {
            // 闪光灯
            boolean isFlashOn = flashlightHelper.isFlashLightOn();
            if (isFlashOn) {
                flashlightHelper.enableFlashLight(false);
            } else {
                flashlightHelper.enableFlashLight(true);
            }
        }

        @Override
        public void onVoiceClick() {
            //声音
            boolean isVoiceOn = hostControlState.isVoiceOn();
            if (isVoiceOn) {
                //静音
                ILiveRoomManager.getInstance().enableMic(false);
                hostControlState.setVoiceOn(false);
            } else {
                ILiveRoomManager.getInstance().enableMic(true);
                hostControlState.setVoiceOn(true);
            }
        }

        @Override
        public void onCameraClick() {
            int cameraId = hostControlState.getCameraid();
            if (cameraId == ILiveConstants.FRONT_CAMERA) {
                ILiveRoomManager.getInstance().switchCamera(ILiveConstants.BACK_CAMERA);
                hostControlState.setCameraid(ILiveConstants.BACK_CAMERA);
            } else if (cameraId == ILiveConstants.BACK_CAMERA) {
                ILiveRoomManager.getInstance().switchCamera(ILiveConstants.FRONT_CAMERA);
                hostControlState.setCameraid(ILiveConstants.FRONT_CAMERA);
            }
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
        heartTimer.cancel();
        heartBeatTimer.cancel();
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        quitLive();
    }

    private void quitLive() {

        finish();
    }

    private void getSelfProfile() {
        TIMFriendshipManager.getInstance().getSelfProfile(new TIMValueCallBack<TIMUserProfile>() {
            @Override
            public void onError(int i, String s) {

            }

            @Override
            public void onSuccess(TIMUserProfile timUserProfile) {
                CNApplication.getApplication().setSelfProfile(timUserProfile);
            }
        });
    }

}
