package com.hujinwen.client.http;

import com.hujinwen.entity.http.HttpProxy;
import com.hujinwen.entity.http.HttpResult;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.hc.client5.http.auth.AuthScope;
import org.apache.hc.client5.http.auth.UsernamePasswordCredentials;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.config.RequestConfig;
import org.apache.hc.client5.http.cookie.BasicCookieStore;
import org.apache.hc.client5.http.entity.UrlEncodedFormEntity;
import org.apache.hc.client5.http.impl.auth.BasicCredentialsProvider;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClientBuilder;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.client5.http.impl.cookie.BasicClientCookie;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManager;
import org.apache.hc.client5.http.io.HttpClientConnectionManager;
import org.apache.hc.client5.http.protocol.HttpClientContext;
import org.apache.hc.client5.http.protocol.RedirectLocations;
import org.apache.hc.client5.http.socket.ConnectionSocketFactory;
import org.apache.hc.client5.http.socket.PlainConnectionSocketFactory;
import org.apache.hc.client5.http.ssl.NoopHostnameVerifier;
import org.apache.hc.client5.http.ssl.SSLConnectionSocketFactory;
import org.apache.hc.core5.http.*;
import org.apache.hc.core5.http.config.Registry;
import org.apache.hc.core5.http.config.RegistryBuilder;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.http.message.BasicClassicHttpRequest;
import org.apache.hc.core5.http.message.BasicNameValuePair;
import org.apache.hc.core5.ssl.SSLContextBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.net.ssl.*;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Created by hu-jinwen on 2020/4/9
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
     * request config builder
     */
    private final RequestConfig.Builder reqConfBuilder = RequestConfig.custom();

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
        // 默认超时时间设置
        reqConfBuilder.setConnectTimeout(10, TimeUnit.SECONDS);
        reqConfBuilder.setConnectionRequestTimeout(10, TimeUnit.SECONDS);
        reqConfBuilder.setResponseTimeout(10, TimeUnit.SECONDS);
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
        httpGet.setConfig(reqConfBuilder.build());
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
        httpGet.setConfig(reqConfBuilder.build());
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
     * overwrite
     */
    public InputStream doPostAsStream(String url, HttpEntity entity) {
        return doPostAsStream(url, null, null, null, entity);
    }

    /**
     * overwrite
     */
    public InputStream doPostAsStream(String url, Map<String, String> data) {
        final List<NameValuePair> nameValuePairList = new ArrayList<>();
        for (Map.Entry<String, String> item : data.entrySet()) {
            nameValuePairList.add(new BasicNameValuePair(item.getKey(), item.getValue()));
        }
        final UrlEncodedFormEntity entity = new UrlEncodedFormEntity(nameValuePairList);
        return doPostAsStream(url, null, null, null, entity);
    }


    /**
     * post请求，返回输入流
     */
    public InputStream doPostAsStream(String url, Map<String, String> headers, Map<String, String> cookies, HttpProxy proxy, HttpEntity entity) {
        final HttpPost httpPost = new HttpPost(url);
        httpPost.setConfig(reqConfBuilder.build());
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
     * port请求，返回响应字符串
     */
    public String doPostAsStr(String url, Map<String, String> headers, Map<String, String> cookies, HttpProxy proxy, HttpEntity entity) {
        final HttpPost httpPost = new HttpPost(url);
        httpPost.setConfig(reqConfBuilder.build());
        try {
            exec(httpPost, headers, cookies, proxy, entity);
            if (response != null) {
                return EntityUtils.toString(response.getEntity());
            }
        } catch (IOException | ParseException ioe) {
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

        // FIXME 信任所有站点
        trustEveryone();
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

        // FIXME just for test
        try {
            SSLContextBuilder builder = new SSLContextBuilder();
            builder.loadTrustMaterial(null, (X509Certificate[] x509Certificates, String s) -> true);
            SSLConnectionSocketFactory socketFactory = new SSLConnectionSocketFactory(builder.build(), new String[]{"SSLv2Hello", "SSLv3", "TLSv1", "TLSv1.1", "TLSv1.2"}, null, NoopHostnameVerifier.INSTANCE);
            Registry<ConnectionSocketFactory> registry = RegistryBuilder.<ConnectionSocketFactory>create()
                    .register("http", new PlainConnectionSocketFactory())
                    .register("https", socketFactory).build();
            HttpClientConnectionManager connManager = new PoolingHttpClientConnectionManager(registry);
            clientBuilder.setConnectionManager(connManager);
        } catch (Exception e) {
            e.printStackTrace();
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


    public void setConnectTimeout(long connectTimeout, TimeUnit timeUnit) {
        reqConfBuilder.setConnectTimeout(connectTimeout, timeUnit);
    }

    public void setConnectionRequestTimeout(long connectionRequestTimeout, TimeUnit timeUnit) {
        reqConfBuilder.setConnectionRequestTimeout(connectionRequestTimeout, timeUnit);
    }

    public void setResponseTimeout(long responseTimeout, TimeUnit timeUnit) {
        reqConfBuilder.setResponseTimeout(responseTimeout, timeUnit);
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

    /**
     * 信任任何站点
     */
    public static void trustEveryone() {
        try {
            HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            });

            SSLContext context = SSLContext.getInstance("TLS");
            context.init(null, new X509TrustManager[]{new X509TrustManager() {
                @Override
                public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                }

                @Override
                public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                }

                @Override
                public X509Certificate[] getAcceptedIssuers() {
                    return new X509Certificate[0];
                }
            }}, new SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(context.getSocketFactory());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
