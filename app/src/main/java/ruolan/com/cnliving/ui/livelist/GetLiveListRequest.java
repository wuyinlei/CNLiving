package ruolan.com.cnliving.ui.livelist;

import java.io.IOException;

import ruolan.com.cnliving.net.ResponseObject;
import ruolan.com.cnliving.util.request.BaseRequest;

/**
 * Created by wuyinlei on 2017/11/17.
 */

public class GetLiveListRequest extends BaseRequest {

    private static final String HOST = "http://192.168.0.103:8080/CNLive/roomServlet?action=liveList";

    public static class LiveListParam {
        public int pageIndex;

        public String toUrlParam() {
            return "&pageIndex=" + pageIndex;
        }
    }

    public String getUrl(LiveListParam param) {
        return HOST + param.toUrlParam();
    }

    @Override
    protected void onFail(IOException e) {
        sendFailMsg(-100,e.toString());
    }

    @Override
    protected void onResponseSuccess(String body) {
        LiveListResponseObj liveListresponseObject = gson.fromJson(body, LiveListResponseObj.class);
        if (liveListresponseObject == null) {
            sendFailMsg(-101, "数据格式错误");
            return;
        }

        if (liveListresponseObject.code.equals(ResponseObject.CODE_SUCCESS)) {
            sendSuccMsg(liveListresponseObject.data);
        } else if (liveListresponseObject.code.equals(ResponseObject.CODE_FAIL)) {
            sendFailMsg(Integer.valueOf(liveListresponseObject.errCode), liveListresponseObject.errMsg);
        }
    }

    @Override
    protected void onResponseFail(int code) {
        sendFailMsg(code,"服务器异常");
    }

}
