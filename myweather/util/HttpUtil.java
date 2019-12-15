package cn.edu.lit.myweather.util;

import okhttp3.OkHttpClient;
import okhttp3.Request;

public class HttpUtil {
    //  当我们调用sendOkHttoRequest发送请求的时候  需要传入请求的地址  和注册一个回调函数 用于处理服务器的响应
    public static void sendOkHttpRequest(String address, okhttp3.Callback callback) {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(address).build();
        client.newCall(request).enqueue(callback);
    }



}
