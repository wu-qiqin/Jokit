package com.hujinwen.client.http;

import com.hujinwen.entity.http.HttpProxy;
import com.hujinwen.entity.http.HttpResult;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.hc.client5.http.auth.AuthScope;
import org.apache.hc.client5.http.auth.UsernamePasswordCredentials;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.cookie.BasicCookieStore;
import org.apache.hc.client5.http.impl.auth.BasicCredentialsProvider;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClientBuilder;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.client5.http.impl.cookie.BasicClientCookie;
import org.apache.hc.client5.http.protocol.HttpClientContext;
import org.apache.hc.client5.http.protocol.RedirectLocations;
import org.apache.hc.core5.http.Header;
import org.apache.hc.core5.http.HttpEntity;
import org.apache.hc.core5.http.HttpHost;
import org.apache.hc.core5.http.ParseException;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.http.message.BasicClassicHttpRequest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by joe on 2020/4/9
 */
public class HttpClient implements Closeable {
    private static final Logger logger = LogManager.getLogger(HttpClient.class);

    /**
     * client
     */
    private CloseableHttpClient httpClient = null;

    /**
     * 响应体
     */
    private CloseableHttpResponse response = null;

    /**
     * 请求头
     */
    private Map<String, String> headers = new HashMap<>();

    /**
     * cookie store
     */
    private final BasicCookieStore cookieStore = new BasicCookieStore();

    /**
     * client builder
     */
    private final HttpClientBuilder clientBuilder = HttpClients.custom();

    /**
     * 提供验证凭据
     */
    private final BasicCredentialsProvider credentialsProvider = new BasicCredentialsProvider();

    /**
     * 客户端上下文
     */
    private HttpClientContext context = null;

    /**
     * 请求结果保存
     */
    private final HttpResult httpResult = new HttpResult();


    private HttpClient() {
        // TODO 这里可以做一些有用的事情
    }

    public static HttpClient createDefault() {
        // TODO 这里可以做一些有用的事情
        return new HttpClient();
    }


    /**
     * get请求获取字符串
     *
     * @param url     请求链接
     * @param headers 请求头
     * @param cookies 请求cookie
     * @param proxy   请求代理
     */
    public String doGetAsStr(String url, Map<String, String> headers, Map<String, String> cookies, HttpProxy proxy) {
        final HttpGet httpGet = new HttpGet(url);
        try {
            exec(httpGet, headers, cookies, proxy, null);
            return EntityUtils.toString(response.getEntity(), "utf-8");
        } catch (IOException | ParseException e) {
            logger.error(e.getMessage(), e);
        }
        return null;
    }

    /**
     * overwrite
     */
    public String doGetAsStr(String url) throws IOException {
        return doGetAsStr(url, null, null, null);
    }

    /**
     * overwrite
     */
    public String doGetAsStr(String url, Map<String, String> headers) {
        return doGetAsStr(url, headers, null, null);
    }

    /**
     * overwrite
     */
    public String doGetAsStr(String url, Map<String, String> headers, Map<String, String> cookies) {
        return doGetAsStr(url, headers, cookies, null);
    }


    /**
     * get请求返回输入流
     */
    public InputStream doGetAsStream(String url, Map<String, String> headers, Map<String, String> cookies, HttpProxy proxy) {
        final HttpGet httpGet = new HttpGet(url);
        try {
            exec(httpGet, headers, cookies, proxy, null);
            if (response != null) {
                return response.getEntity().getContent();
            }
        } catch (IOException ioe) {
            logger.error(ioe.getMessage(), ioe);
        }
        return null;
    }


    /**
     * overwrite
     */
    public InputStream doGetAsStream(String url) {
        return doGetAsStream(url, null, null, null);
    }

    /**
     * overwrite
     */
    public InputStream doGetAsStream(String url, Map<String, String> headers) {
        return doGetAsStream(url, headers, null, null);
    }

    /**
     * overwrite
     */
    public InputStream doGetAsStream(String url, Map<String, String> headers, Map<String, String> cookies) {
        return doGetAsStream(url, headers, cookies, null);
    }

    /**
     * post请求，返回输入流
     */
    public InputStream doPostAsStream(String url, Map<String, String> headers, Map<String, String> cookies, HttpProxy proxy, HttpEntity entity) {
        final HttpPost httpPost = new HttpPost(url);

        try {
            exec(httpPost, headers, cookies, proxy, entity);
            if (response != null) {
                return response.getEntity().getContent();
            }
        } catch (IOException ioe) {
            logger.error(ioe.getMessage(), ioe);
        }
        return null;
    }


    /**
     * 执行
     */
    private void exec(BasicClassicHttpRequest request, Map<String, String> headers, Map<String, String> cookies, HttpProxy proxy, HttpEntity entity) throws IOException {
        // 请求头
        if (ObjectUtils.isEmpty(headers)) {
            headers = this.headers;
        }
        for (Map.Entry<String, String> entry : headers.entrySet()) {
            request.setHeader(entry.getKey(), entry.getValue());  // request setHeader和addHeader的区别是，setHeader会修改同名header
        }
        boolean needRebuild = false;
        // cookies
        if (cookies != null) {
            setCookie(cookies);
            needRebuild = true;
        }
        // 代理
        if (proxy != null) {
            setProxy(proxy);
            needRebuild = true;
        }
        if (needRebuild) {
            rebuildClient();
        }
        // entity
        if (entity != null) {
            request.setEntity(entity);
        }

        if (httpClient == null) {
            httpClient = clientBuilder.build();
        }
        context = HttpClientContext.create();
        response = httpClient.execute(request, context);

        httpResult.extractField(response);
        httpResult.extractField(context);
    }

    /**
     * 重新构建client
     */
    private void rebuildClient() throws IOException {
        if (httpClient != null) {
            httpClient.close();
        }
        httpClient = clientBuilder.build();
    }


    /**
     * 设置代理
     */
    public void setProxy(HttpProxy proxy) {
        if (StringUtils.isNotBlank(proxy.getUsername())) {
            credentialsProvider.setCredentials(
                    new AuthScope(proxy.getHost(), proxy.getPort()),
                    new UsernamePasswordCredentials(proxy.getUsername(), proxy.getPassword().toCharArray())
            );
        }
        clientBuilder.setProxy(new HttpHost(proxy.getHost(), proxy.getPort()));
        clientBuilder.setDefaultCredentialsProvider(credentialsProvider);
    }

    /**
     * 设置cookie
     */
    public void setCookie(Map<String, String> cookies) {
        cookieStore.clear();
        for (Map.Entry<String, String> entry : cookies.entrySet()) {
            cookieStore.addCookie(new BasicClientCookie(entry.getKey(), entry.getValue()));
        }
        clientBuilder.setDefaultCookieStore(cookieStore);
    }

    /**
     * 设置headers
     */
    public void setHeaders(Map<String, String> headers) {
        if (ObjectUtils.isNotEmpty(headers)) {
            this.headers = headers;
        }
    }

    public void addHeader(String key, String value) {
        this.headers.put(key, value);
    }

    /**
     * 获取响应码
     */
    public int getRespCode() {
        return this.httpResult.getRespCode();
    }

    /**
     * 获取当前请求头
     */
    public Map<String, String> getReqHeaders() {
        return this.headers;
    }

    /**
     * 获取最近一次请求的响应头
     */
    public Map<String, String> getRespHeaders() {
        final Map<String, String> headers = new HashMap<>();

        for (Header header : response.getHeaders()) {
            headers.put(header.getName(), header.getValue());
        }
        return headers;
    }

    /**
     * 获取最后一次重定向的路径
     */
    public String getLastRedirectUrl() {
        final RedirectLocations redirectLocations = context.getRedirectLocations();
        final int size = redirectLocations.size();
        if (size == 0) {
            return "";
        }
        return redirectLocations.get(size - 1).toString();
    }

    /**
     * 获取content 长度
     */
    public long getContentLength() {
        return httpResult.getContentLength();
    }


    @Override
    public void close() throws IOException {
        // FIXME 5.0 和 4.5 有很大的差别。5.0 在 close response 的时候也会消费 InputStream
//        if (response != null) {
//            response.close();
//        }
        if (httpClient != null) {
            httpClient.close();
        }
    }

}
