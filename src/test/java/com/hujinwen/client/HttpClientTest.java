package com.hujinwen.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.hujinwen.client.http.HttpClient;
import com.hujinwen.utils.JsonUtils;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.entity.UrlEncodedFormEntity;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.HttpEntity;
import org.apache.hc.core5.http.NameValuePair;
import org.apache.hc.core5.http.io.entity.StringEntity;
import org.apache.hc.core5.http.message.BasicNameValuePair;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

class HttpClientTest {

    @Test
    void test1() {
        final HttpClient httpClient = HttpClient.createDefault();

//        httpClient.doGetAsStream("https://9d772c20d782e4e5bcfa2711c7fb7553.dlied1.cdntips.com/dlied1.qq.com/qqweb/PCQQ/PCQQ_EXE/PCQQ2020.exe?mkey=5e9d6eefb4a68727&f=9943&cip=180.166.161.210&proto=https");
        httpClient.doGetAsStream("https://new.qq.com/omn/20200420/20200420A020PH00.html");
        final long contentLength = httpClient.getContentLength();


    }

    /**
     * Post 测试
     */
    @Test
    void test2() throws IOException {
        final HttpPost httpPost = new HttpPost("https://www.baidu.com");

        final List<NameValuePair> nvps = new ArrayList<>();
        nvps.add(new BasicNameValuePair("", ""));

        final UrlEncodedFormEntity urlEncodedFormEntity = new UrlEncodedFormEntity(nvps);

        httpPost.setEntity(urlEncodedFormEntity);

        final CloseableHttpClient httpClient = HttpClients.createDefault();
        httpClient.execute(httpPost);
    }

    @Test
    void bugFix() throws IOException {
        final HttpClient httpClient = HttpClient.createDefault();
        final HttpClient httpClient2 = HttpClient.createDefault();
        final InputStream inputStream = httpClient.doGetAsStream("http://forspeed.onlinedown.net/down/newdown/2/17/Warcraft3_1.24E.rar");
        final InputStream inputStream1 = httpClient2.doGetAsStream("http://forspeed.onlinedown.net/down/newdown/2/17/Warcraft3_1.24E.rar");
        httpClient.close();
        final int read = inputStream1.read();
        httpClient2.close();
        System.out.println();

    }

    @Test
    void returnCode() throws IOException {
        final HttpClient httpClient = HttpClient.createDefault();
        final String content = httpClient.doGetAsStr("Https://www.baidu.com");
        final Map<String, String> reqHeaders = httpClient.getReqHeaders();
        final int respCode = httpClient.getRespCode();
        System.out.println();
    }

    /**
     * post获取输入流测试
     */
    @Test
    void doPostAsStreamTest() {
        final HttpClient httpClient = HttpClient.createDefault();
        final HttpEntity httpEntity = new UrlEncodedFormEntity(new ArrayList<NameValuePair>() {{
            add(new BasicNameValuePair("url", "https://www.bilibili.com/video/BV1Nt4y1D7pW?spm_id_from=333.851.b_7265706f7274466972737431.7"));

        }});
        final InputStream inputStream = httpClient.doPostAsStream("http://10.0.2.55:8081/bilibili_jpg", httpEntity);
        System.out.println();
    }


    /**
     * HttpClient 做 application/json 请求
     */
    @Test
    void doPostAsStr() throws JsonProcessingException {
        final HttpClient httpClient = HttpClient.createDefault();

        final ObjectNode objectNode = JsonUtils.newObjectNode();
        objectNode.put("title", "This is Title");
        objectNode.put("text", "这是一段\n测试内容");

        final StringEntity stringEntity = new StringEntity(JsonUtils.toString(objectNode), ContentType.create("application/json", "UTF-8"));

        final InputStream inputStream = httpClient.doPostAsStream("https://open.feishu.cn/open-apis/bot/hook/76c87ba3-d979-43d1-99cb-5fea9cf3f36c", stringEntity);
        System.out.println();
    }

    /**
     * HttpClient5 3中不同的timeout配置
     */
    @Test
    void timeoutTest() {
        try {
            final HttpClient httpClient = HttpClient.createDefault();
            httpClient.setConnectTimeout(3, TimeUnit.SECONDS);
            httpClient.setConnectionRequestTimeout(3, TimeUnit.SECONDS);
            httpClient.setResponseTimeout(3, TimeUnit.SECONDS);
            final String content = httpClient.doGetAsStr("Https://127.127.127.127/asdf/afsdf");
            System.out.println();
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    public static void main(String[] args) {

        final String hello = System.getProperty("PATH", "Hello");
        System.out.println();
    }

}