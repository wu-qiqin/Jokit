package com.hujinwen.client;

import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.ParseException;
import org.apache.hc.core5.http.io.entity.EntityUtils;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by joe on 2020/4/9
 */
public class HttpClient implements Closeable {

    private CloseableHttpClient httpClient = null;
    private CloseableHttpResponse response = null;

    private HttpClient() {
        httpClient = HttpClients.createDefault();
    }

    public static HttpClient createDefault() {
        return new HttpClient();
    }


    /**
     * get请求获取字符串
     */
    public String doGetAsStr(String url) throws IOException {
        final HttpGet httpGet = new HttpGet(url);
        try (
                final CloseableHttpResponse response = httpClient.execute(httpGet);
        ) {
            return EntityUtils.toString(response.getEntity(), "utf-8");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public InputStream doGetAsStream(String url) {
        final HttpGet httpGet = new HttpGet(url);
        try {
            response = httpClient.execute(httpGet);
            return response.getEntity().getContent();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    @Override
    public void close() throws IOException {
        if (response != null) {
            response.close();
        }
        if (httpClient != null) {
            httpClient.close();
        }
    }

}
