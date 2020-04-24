package cn.frank.trading.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import cn.frank.trading.exception.HttpRequestException;
import com.google.common.base.Stopwatch;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.apache.commons.collections4.MapUtils;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Service
@Slf4j
public class HuobiHttpClient {

    private static final MediaType JSON_TYPE = MediaType.parse("application/json");
    private OkHttpClient httpClient;
    private AtomicInteger numRequestFaild = new AtomicInteger(0);

    @PostConstruct
    private void init() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .connectionPool(new ConnectionPool(50, 10, TimeUnit.SECONDS))
                .connectTimeout(3, TimeUnit.SECONDS)
                .readTimeout(3, TimeUnit.SECONDS);
        httpClient = builder.build();
    }

    public String doGet(String url, Map<String, String> params) {
        Request.Builder reqBuild = new Request.Builder();
        HttpUrl.Builder urlBuilder = Objects.requireNonNull(HttpUrl.parse(url)).newBuilder();
        if (MapUtils.isNotEmpty(params)) {
            params.forEach(urlBuilder::addQueryParameter);
        }
        reqBuild.url(urlBuilder.build());

        Response response;
        try {
            Stopwatch stopwatch = Stopwatch.createStarted();
            log.info("发送请求：{}", reqBuild.build().toString());
            response = httpClient.newCall(reqBuild.build()).execute();
            stopwatch.stop();
            log.info("请求耗时: {}ms", stopwatch.elapsed(TimeUnit.MILLISECONDS));
        } catch (IOException e) {
            if (e instanceof SocketTimeoutException) {
                numRequestFaild.getAndIncrement();
            }
            throw new HttpRequestException("http执行异常，url=" + url, e);
        }
        if (response.isSuccessful()) {
            try {
                reset();
                return response.body().string();
            } catch (IOException e) {
                throw new HttpRequestException("http结果解析异常", e);
            }
        } else {
            int statusCode = response.code();
            throw new HttpRequestException("响应码不为200，返回响应码：" + statusCode + "，url：" + urlBuilder.build());
        }
    }

    public String doPost(String url, Map<String, String> params) {
        FormBody.Builder builder = new FormBody.Builder();
        if (MapUtils.isNotEmpty(params)) {
            params.forEach(builder::add);
        }

        Request.Builder reqBuilder = new Request.Builder().url(url);
        if (MapUtils.isNotEmpty(params)) {
            reqBuilder.post(builder.build());
        }

        Response response;
        try {
            response = httpClient.newCall(reqBuilder.build()).execute();
        } catch (IOException e) {
            if (e instanceof SocketTimeoutException) {
                numRequestFaild.getAndIncrement();
            }
            throw new HttpRequestException("http执行异常，url=" + url, e);
        }
        if (response.isSuccessful()) {
            try {
                reset();
                return response.body().string();
            } catch (IOException e) {
                throw new HttpRequestException("http结果解析异常", e);
            }
        } else {
            int statusCode = response.code();
            throw new HttpRequestException("响应码不为200，返回响应码：" + statusCode + "，url：" + reqBuilder.build());
        }
    }

    public String doPostJson(String url, Map<String, String> params) {
        RequestBody body = RequestBody.create(JSON_TYPE, JSON.toJSONString(params));
        Request request = new Request.Builder().url(url).post(body).build();
        Response response;
        try {
            response = httpClient.newCall(request).execute();
        } catch (IOException e) {
            throw new HttpRequestException("http执行异常，url=" + url, e);
        }
        if (response.isSuccessful()) {
            try {
                return response.body().string();
            } catch (IOException e) {
                throw new HttpRequestException("http结果解析异常", e);
            }
        } else {
            int statusCode = response.code();
            throw new HttpRequestException("响应码不为200，返回响应码：" + statusCode + "，url：" + request);
        }
    }

    public String callJson(String accessKeyId, String accessKeySecret, String method, String uri, String str,
                           Map<String, String> params) throws IOException {

        JSONObject jasonObject = JSONObject.parseObject(str);
        return call(accessKeyId, accessKeySecret, method, uri, jasonObject, params);
    }

    public String call(String accessKeyId, String accessKeySecret, String method, String uri, Object object,
                       Map<String, String> params) throws IOException {
        ApiSignature sign = new ApiSignature();
        sign.createSignature(accessKeyId, accessKeySecret, method, uri, params);
        Request.Builder builder;
        if ("POST".equals(method)) {
            RequestBody body = RequestBody.create(JSON_TYPE, JSON.toJSONString(object));
            builder = new Request.Builder().url(uri + "?" + toQueryString(params)).post(body);
        } else {
            builder = new Request.Builder().url(uri + "?" + toQueryString(params)).get();
        }
        Request request = builder.build();
        Response response = httpClient.newCall(request).execute();
        return response.body().string();

    }

    private String toQueryString(Map<String, String> params) {
        return params.entrySet().stream()
                .map((entry) -> entry.getKey() + "=" + ApiSignature.urlEncode(entry.getValue()))
                .collect(Collectors.joining("&"));
    }

    private void reset() {
        numRequestFaild.set(0);
    }

    public int getRequestFaildTotal() {
        return numRequestFaild.get();
    }
}
