package ruolan.com.cnliving.ui.watcher;

import java.io.IOException;

import ruolan.com.cnliving.net.ResponseObject;
import ruolan.com.cnliving.util.request.BaseRequest;

/**
 * Created by wuyinlei on 2017/11/19.
 *
 * @function
 */

public class SendOrGetGiftRequest extends BaseRequest {

    private static final String Action = "http://192.168.0.103:8080/CNLive/userServlet";


    private static final String RequestParamKey_UserId = "userId";
    private static final String RequestParamKey_Exp = "exp";
    private static final String RequestParamKey_Is_Send = "isSend";


    public static class SendOrGetGiftParam {
        public String userId;
        public int exp;
        public String isSend;
    }

    public String getUrl(SendOrGetGiftRequest.SendOrGetGiftParam param , boolean isSend) {
       param.isSend =  isSend ? "send" : "get";
        return Action
                + "?" + RequestParamKey_UserId + "=" + param.userId
                + "&" + RequestParamKey_Exp + "=" + param.exp
                + "&" + RequestParamKey_Is_Send + "=" + param.isSend;
    }


    @Override
    protected void onFail(IOException e) {

        sendFailMsg(-100, e.getMessage());
    }

    @Override
    protected void onResponseFail(int code) {
        sendFailMsg(code, "服务出现异常");
    }

    @Override
    protected void onResponseSuccess(String body) {
        UserInfoResponseObj responseObject = gson.fromJson(body,UserInfoResponseObj.class);
        if (responseObject == null) {
            sendFailMsg(-101, "数据格式错误");
            return;
        }

        if (responseObject.code.equals(ResponseObject.CODE_SUCCESS)) {
            sendSuccMsg(responseObject.data);
        } else if (responseObject.code.equals(ResponseObject.CODE_FAIL)) {
            sendFailMsg(Integer.valueOf(responseObject.errCode), responseObject.errMsg);
        }
    }
}
