package com.hujinwen.client;

import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.entity.UrlEncodedFormEntity;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.NameValuePair;
import org.apache.hc.core5.http.message.BasicNameValuePair;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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


}