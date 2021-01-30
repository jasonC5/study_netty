package com.test.dxh;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.push.model.v20160801.PushNoticeToAndroidRequest;
import com.aliyuncs.push.model.v20160801.PushNoticeToAndroidResponse;
import com.google.gson.Gson;
import org.junit.Test;

public class AliPushTest {

    @Test
    public void testpush() {
        DefaultProfile profile = DefaultProfile.getProfile("cn-beijing", "333367531", "eb21b3a4a22d4b65aba498bc061187b8");
        IAcsClient client = new DefaultAcsClient(profile);

        PushNoticeToAndroidRequest request = new PushNoticeToAndroidRequest();
        request.setAppKey(333367531L);
        request.setTarget("ALIAS");
        request.setTargetValue("dxh_3207_1011");
        request.setTitle("测试");
        request.setBody("您有新的订单");
        request.setExtParameters("{\"orderNo\":\"11111\",\"event\":\"1\"}");

        try {
            PushNoticeToAndroidResponse response = client.getAcsResponse(request);
            System.out.println(new Gson().toJson(response));
        } catch (ServerException e) {
            e.printStackTrace();
        } catch (ClientException e) {
            System.out.println("ErrCode:" + e.getErrCode());
            System.out.println("ErrMsg:" + e.getErrMsg());
            System.out.println("RequestId:" + e.getRequestId());
        }

    }
}
