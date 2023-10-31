package com.pj.project.task;

import cn.hutool.json.JSONUtil;
import com.pj.current.config.SystemObject;
import com.pj.utils.cache.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;

import java.io.IOException;
@Slf4j
public class TokenThread  implements Runnable{
    static final OkHttpClient HTTP_CLIENT = new OkHttpClient().newBuilder().build();
    static void createAccessToken() throws IOException {
        String client_id = SystemObject.config.getClientId();
        String client_secret = SystemObject.config.getClientSecret();
        MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
        RequestBody body = RequestBody.create(mediaType, "");
        Request request = new Request.Builder()
                .url("https://aip.baidubce.com/oauth/2.0/token?client_id=" + client_id + "&client_secret=" + client_secret + "&grant_type=client_credentials")
                .method("POST", body)
                .addHeader("Content-Type", "application/json")
                .addHeader("Accept", "application/json")
                .build();
        Response response = HTTP_CLIENT.newCall(request).execute();
        String resp = response.body().string();
        log.info("get token resp:{}", resp);
        String token= JSONUtil.parseObj(resp).getStr("access_token");
        RedisUtil.setByDays("token", token, 29);
    }

    @Override
    public void run() {
        try {
            createAccessToken();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
