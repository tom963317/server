package com.pj.utils;

import cn.hutool.core.text.UnicodeUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import cn.hutool.log.StaticLog;
import org.apache.http.*;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.LayeredConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.SSLException;
import javax.net.ssl.SSLHandshakeException;
import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.UnknownHostException;
import java.util.Map;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class HttpPoolUtil {
    private static Logger logger = LoggerFactory.getLogger(HttpPoolUtil.class);

    private static final int CONNECT_TIMEOUT = Config.getHttpConnectTimeout();// 设置连接建立的超时时间为10s
    private static final int SOCKET_TIMEOUT = Config.getHttpSocketTimeout();
    private static final int MAX_CONN = Config.getHttpMaxPoolSize(); // 最大连接数
    private static final int Max_PRE_ROUTE = Config.getHttpMaxPoolSize();
    private static final int MAX_ROUTE = Config.getHttpMaxPoolSize();
    private static CloseableHttpClient httpClient; // 发送请求的客户端单例
    private static PoolingHttpClientConnectionManager manager; //连接池管理类
    private static ScheduledExecutorService monitorExecutor;

    private final static Object syncLock = new Object(); // 相当于线程锁,用于线程安全

    /**
     * 对http请求进行基本设置
     *
     * @param httpRequestBase http请求
     */
    private static void setRequestConfig(HttpRequestBase httpRequestBase) {
        RequestConfig requestConfig = RequestConfig.custom().setConnectionRequestTimeout(CONNECT_TIMEOUT)
                .setConnectTimeout(CONNECT_TIMEOUT)
                .setSocketTimeout(SOCKET_TIMEOUT).build();

        httpRequestBase.setConfig(requestConfig);
    }

    public static CloseableHttpClient getHttpClient(String url) {
        String hostName = url.split("/")[2];
        int port = 80;
        if (hostName.contains(":")) {
            String[] args = hostName.split(":");
            hostName = args[0];
            port = Integer.parseInt(args[1]);
        }

        if (httpClient == null) {
            //多线程下多个线程同时调用getHttpClient容易导致重复创建httpClient对象的问题,所以加上了同步锁
            synchronized (syncLock) {
                if (httpClient == null) {
                    httpClient = createHttpClient(hostName, port);
                    //开启监控线程,对异常和空闲线程进行关闭
                    monitorExecutor = Executors.newScheduledThreadPool(1);
                    monitorExecutor.scheduleAtFixedRate(new TimerTask() {
                        @Override
                        public void run() {
                            //关闭异常连接
                            manager.closeExpiredConnections();
                            //关闭5s空闲的连接
                            manager.closeIdleConnections(Config.getHttpIdelTimeout(), TimeUnit.MILLISECONDS);
                        }
                    }, Config.getHttpMonitorInterval(), Config.getHttpMonitorInterval(), TimeUnit.MILLISECONDS);
                }
            }
        }
        return httpClient;
    }

    /**
     * 根据host和port构建httpclient实例
     *
     * @param host 要访问的域名
     * @param port 要访问的端口
     * @return
     */
    public static CloseableHttpClient createHttpClient(String host, int port) {
        ConnectionSocketFactory plainSocketFactory = PlainConnectionSocketFactory.getSocketFactory();
        LayeredConnectionSocketFactory sslSocketFactory = SSLConnectionSocketFactory.getSocketFactory();
        Registry<ConnectionSocketFactory> registry = RegistryBuilder.<ConnectionSocketFactory>create().register("http", plainSocketFactory)
                .register("https", sslSocketFactory).build();

        manager = new PoolingHttpClientConnectionManager(registry);
        //设置连接参数
        manager.setMaxTotal(MAX_CONN); // 最大连接数
        manager.setDefaultMaxPerRoute(Max_PRE_ROUTE); // 路由最大连接数

        HttpHost httpHost = new HttpHost(host, port);
        manager.setMaxPerRoute(new HttpRoute(httpHost), MAX_ROUTE);

        //请求失败时,进行请求重试
        HttpRequestRetryHandler handler = (e, i, httpContext) -> {
            if (i > 3) {
                //重试超过3次,放弃请求
                return false;
            }
            if (e instanceof NoHttpResponseException) {
                //服务器没有响应,可能是服务器断开了连接,应该重试
                logger.error("receive no response from server, retry");
                return true;
            }
            if (e instanceof SSLHandshakeException) {
                // SSL握手异常
                return false;
            }
            if (e instanceof InterruptedIOException) {
                //超时
                return false;
            }
            if (e instanceof UnknownHostException) {
                // 服务器不可达
                return false;
            }

            if (e instanceof SSLException) {
                return false;
            }

            HttpClientContext context = HttpClientContext.adapt(httpContext);
            HttpRequest request = context.getRequest();
            if (!(request instanceof HttpEntityEnclosingRequest)) {
                //如果请求不是关闭连接的请求
                return true;
            }
            return false;
        };

        return HttpClients.custom().setConnectionManager(manager).setRetryHandler(handler).build();
    }


    public static JSONObject get(String url, Map<String, String> headers) {
        HttpGet httpGet = new HttpGet(url);
        setRequestConfig(httpGet);
        httpGet.setHeader("Content-Type", "application/json");
        //增加请求头
        if (headers != null) {
            headers.forEach(httpGet::addHeader);
        }
        CloseableHttpResponse response;
        JSONObject object = null;
        try {
            response = getHttpClient(url).execute(httpGet, HttpClientContext.create());
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                String result = EntityUtils.toString(entity);
                if (StrUtil.contains(result, "被阻断")) {
                    object = new JSONObject();
                    object.set("status", 100006).set("msg", "警告！请休息十分钟再刷");
                    return object;
                }
                object = JSONUtil.parseObj(UnicodeUtil.toString(result));
            }
        } catch (IOException e) {
            StaticLog.error("http error:{},{}", url, e.getMessage());
            if (StrUtil.contains(e.getMessage(), "connect timed out") || StrUtil.contains(e.getMessage(), "Read timed out")) {
                object = new JSONObject();
                object.set("status", 100606).set("msg", "网络波动，请重新启动");
                return object;
            }
        }
        return object;
    }


    public static String post(String url, String body) {
        HttpPost httpPost = new HttpPost(url);
        httpPost.setHeader("Content-Type", "application/json");
        setRequestConfig(httpPost);
        httpPost.setEntity(new StringEntity(body, "utf-8"));
        CloseableHttpResponse response;
        try {
            response = getHttpClient(url).execute(httpPost, HttpClientContext.create());
            HttpEntity entity = response.getEntity();
            return UnicodeUtil.toString(EntityUtils.toString(entity));
        } catch (IOException e) {
            return "";
        }
    }

    /**
     * 关闭连接池
     */
    public static void closeConnectionPool() {
        try {
            httpClient.close();
            manager.close();
            monitorExecutor.shutdown();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static class Config {

        static int httpConnectTimeout = 5000;

        static int httpSocketTimeout = 5000;

        static int httpMaxPoolSize = 100;

        static int httpMonitorInterval = 10 * 1000;

        static int httpIdelTimeout = 5000;

        public static int getHttpIdelTimeout() {
            return httpIdelTimeout;
        }

        public static int getHttpSocketTimeout() {
            return httpSocketTimeout;
        }

        public static int getHttpMaxPoolSize() {
            return httpMaxPoolSize;
        }

        public static int getHttpMonitorInterval() {
            return httpMonitorInterval;
        }

        public static int getHttpConnectTimeout() {
            return httpConnectTimeout;
        }
    }
}
