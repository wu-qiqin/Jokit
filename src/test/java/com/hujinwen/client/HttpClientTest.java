package com.hujinwen.client;

import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.entity.UrlEncodedFormEntity;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.HttpEntity;
import org.apache.hc.core5.http.NameValuePair;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.http.message.BasicNameValuePair;
import org.junit.jupiter.api.Test;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

class HttpClientTest {

    @Test
    void test() throws IOException {
        try (
                CloseableHttpClient httpclient = HttpClients.createDefault()
        ) {
            HttpGet httpGet = new HttpGet("http://httpbin.org/get");
            // The underlying HTTP connection is still held by the response object
            // to allow the response content to be streamed directly from the network socket.
            // In order to ensure correct deallocation of system resources
            // the user MUST call CloseableHttpResponse#close() from a finally clause.
            // Please note that if response content is not fully consumed the underlying
            // connection cannot be safely re-used and will be shut down and discarded
            // by the connection manager.
            try (
                    CloseableHttpResponse response1 = httpclient.execute(httpGet)
            ) {
                System.out.println(response1.getCode() + " " + response1.getReasonPhrase());
                HttpEntity entity1 = response1.getEntity();
                // do something useful with the response body
                // and ensure it is fully consumed
                EntityUtils.consume(entity1);
            }

            HttpPost httpPost = new HttpPost("http://httpbin.org/post");
            List<NameValuePair> nvps = new ArrayList<>();
            nvps.add(new BasicNameValuePair("username", "vip"));
            nvps.add(new BasicNameValuePair("password", "secret"));
            httpPost.setEntity(new UrlEncodedFormEntity(nvps));

            try (
                    CloseableHttpResponse response2 = httpclient.execute(httpPost)
            ) {
                System.out.println(response2.getCode() + " " + response2.getReasonPhrase());
                HttpEntity entity2 = response2.getEntity();
                // do something useful with the response body
                // and ensure it is fully consumed
                EntityUtils.consume(entity2);
            }
        }
    }

    @Test
    void test2() throws IOException {
        final HttpClient httpClient = HttpClient.createDefault();
        final String content = httpClient.doGetAsStr("https://www.baidu.com");
        System.out.println();
        httpClient.close();
    }

    @Test
    void test3() throws IOException {
        final HttpClient httpClient = HttpClient.createDefault();
        final InputStream inputStream = httpClient.doGetAsStream("http://dldir1.qq.com/qqfile/qq/TIM3.0.0/TIM3.0.0.21315.exe");

        try (
                HttpClient _1 = httpClient;
                final FileOutputStream fileOutputStream = new FileOutputStream("TIM.exe");
                final BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(fileOutputStream)
        ) {

            final byte[] buffer = new byte[1024];
            int read;
            while ((read = inputStream.read(buffer)) != -1) {
                bufferedOutputStream.write(buffer, 0, read);
            }
        }
        System.out.println();
    }


}