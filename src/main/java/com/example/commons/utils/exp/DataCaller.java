package com.example.commons.utils.exp;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.NoHttpResponseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.config.ConnectionConfig;
import org.apache.http.config.SocketConfig;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.protocol.HttpContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.SocketTimeoutException;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/***
 * @Date 2017/11/24
 *@Description httpClient
 * @author zhanghesheng
 * */
@Slf4j
public class DataCaller {

    private static final Logger LOGGER = LoggerFactory.getLogger(DataCaller.class);

    private CloseableHttpClient client = null;

    private DataCaller() {
        open();
    }

    private final static DataCaller dataCaller = new DataCaller();

    public static DataCaller getCaller() {
        return dataCaller;
    }

    public void open() {
        createConnectionManager();
    }

    private void createConnectionManager() {
        PoolingHttpClientConnectionManager mgr = new PoolingHttpClientConnectionManager();
        mgr.setMaxTotal(100);
        mgr.setDefaultMaxPerRoute(50);
        RequestConfig requestConfig = RequestConfig.custom().
                setConnectTimeout(20000).
                setConnectionRequestTimeout(20000).
                setSocketTimeout(20000).setExpectContinueEnabled(false).build();
        ConnectionConfig connectionConfig = ConnectionConfig.custom().
                setCharset(Charset.forName("UTF-8")).build();
        SocketConfig socketConfig = SocketConfig.custom().
                setSoKeepAlive(true).setTcpNoDelay(false).build();

        HttpRequestRetryHandler retryHandler = new HttpRequestRetryHandler() {
            @Override
            public boolean retryRequest(IOException exception, int executionCount, HttpContext context) {
                if (executionCount >= 3) {
                    return false;
                }
                if (exception instanceof NoHttpResponseException) {
                    return true;
                } else if (exception instanceof ClientProtocolException) {
                    return true;
                } else if (exception instanceof SocketTimeoutException) {
                    return true;
                }
                return false;
            }
        };
        client = HttpClients.custom().
                setConnectionManager(mgr).
                setDefaultRequestConfig(requestConfig).
                setDefaultConnectionConfig(connectionConfig).setDefaultSocketConfig(socketConfig)
                .setRetryHandler(retryHandler).
                        build();

    }

    //文件下载
    public String downFile(String serviceUrl, String downloadFile) throws Exception {
        HttpGet get = null;
        CloseableHttpResponse response = null;
        InputStream is = null;
        FileOutputStream fos = null;
        try {
            get = new HttpGet(serviceUrl);
            get.addHeader("Connection", "keep-alive");
            response = client.execute(get);
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                is = entity.getContent();
                File file = new File(String.format("%s.zip", downloadFile));
                fos = new FileOutputStream(file);
                byte[] buffer = new byte[4096];
                int len = -1;
                while ((len = is.read(buffer)) != -1) {
                    fos.write(buffer, 0, len);
                }
            }

        } catch (java.net.SocketException ex) {
            ex.printStackTrace();
            LOGGER.error("DataCaller call other exception " + ex.getMessage(), ex);
            throw new Exception(ex);
        } catch (ClientProtocolException e) {
            LOGGER.error(String.format("url:%s--\n ClientProtocolException:%s", serviceUrl, e.getMessage()), e);
            throw new ClientProtocolException(e);
        } catch (IOException e) {
            LOGGER.error("DataCaller call other exception " + e.getMessage(), e);
            throw new IOException(e);
        } finally {
            if (fos != null) {
                fos.close();
            }
            if (is != null) {
                is.close();
            }
            if (response != null) {
                response.close();
            }
            if (get != null) {
                get.releaseConnection();
            }
        }
        return downloadFile;
    }
    /**
    *@author zhanghesheng
    * @param serviceUrl 远程服务器url
     *@param fileByte 文件字节
    * @date 2017/11/25
    * @return 请求返回体
    *@Description 远程服务器上传文件
    */
    public String uploadFile(String serviceUrl, byte[] fileByte) throws Exception {
        String response = null;
        HttpPost post = null;
        try {
            post = new HttpPost(serviceUrl);
            post.addHeader("Connection", "keep-alive");
            ByteArrayEntity byteArr = new ByteArrayEntity(fileByte);
            post.setEntity(byteArr);
            response = client.execute(post, new BasicResponseHandler());
        } catch (java.net.SocketException ex) {
            ex.printStackTrace();
            LOGGER.error("DataCaller call other exception " + ex.getMessage(), ex);
            throw new Exception(ex);
        } catch (ClientProtocolException e) {
            LOGGER.error(String.format("url:%s--\n ClientProtocolException:%s", serviceUrl, e.getMessage()), e);
            throw new ClientProtocolException(e);
        } catch (IOException e) {
            LOGGER.error("DataCaller call other exception " + e.getMessage(), e);
            throw new IOException(e);
        } finally {
            if (post != null) {
                post.releaseConnection();
            }
        }
        return response;
    }

    //post请求
    public String postCall(String serviceUrl, String context) throws Exception {
        String response = null;
        HttpPost post = null;
        try {
            post = new HttpPost(serviceUrl);
            post.addHeader("Connection", "keep-alive");
            post.addHeader("Accept", "application/json");
            post.addHeader("Content-Type", "application/json");
            StringEntity entity = new StringEntity(context, "UTF-8");
            post.setEntity(entity);
            response = client.execute(post, new BasicResponseHandler());
        } catch (java.net.SocketException ex) {
            ex.printStackTrace();
            LOGGER.error("DataCaller call other exception " + ex.getMessage(), ex);
            throw new Exception(ex);
        } catch (ClientProtocolException e) {
            LOGGER.error(String.format("url:%s--\n ClientProtocolException:%s", serviceUrl, e.getMessage()), e);
            throw new ClientProtocolException(e);
        } catch (IOException e) {
            throw new IOException(e);
        } finally {
            if (post != null) post.releaseConnection();
        }
        return response;
    }

    @SuppressWarnings("rawtypes")
    //get 参数拼接
    public String getCall(String serviceUrl, Map<String, String> pars) throws ClientProtocolException, IOException {

        String response = null;
        HttpGet get = null;
        try {

            // 请求实体
            if (pars != null && pars.size() > 0) {
                if (serviceUrl.indexOf("?") < 0) serviceUrl += "?";
                Set<String> key = pars.keySet();
                for (Iterator it = key.iterator(); it.hasNext(); ) {
                    String name = (String) it.next();
                    String value = pars.get(name);
                    if (name != null && !name.isEmpty() && value != null) {
                        if (!serviceUrl.endsWith("?") && !serviceUrl.endsWith("&")) serviceUrl += "&";
                        serviceUrl += String.format("%s=%s", name, java.net.URLEncoder.encode(value, "utf-8"));
                    }

                }

            }
            get = new HttpGet(serviceUrl);
            get.addHeader("Connection", "keep-alive");
            response = client.execute(get, new BasicResponseHandler());
        } catch (java.net.SocketException ex) {
            ex.printStackTrace();
            LOGGER.error("DataCaller call other exception " + ex.getMessage(), ex);
        } catch (ClientProtocolException e) {
            LOGGER.error(String.format("url:%s--\n ClientProtocolException:%s", serviceUrl, e.getMessage()), e);
            throw new ClientProtocolException(e);
        } catch (IOException e) {
            throw new IOException(e);
        } finally {
            if (get != null) {
                get.releaseConnection();
            }
        }
        return response;
    }
    //get
    public String getCall(String url) throws Exception {
        String response = null;
        HttpGet get = null;
        try {
            get = new HttpGet(url);
            get.addHeader("Connection", "keep-alive");
            response = client.execute(get, new BasicResponseHandler());
        } catch (java.net.SocketException ex) {
            ex.printStackTrace();
            LOGGER.error("DataCaller call other exception " + ex.getMessage(), ex);
            throw new Exception(ex);
        } catch (ClientProtocolException e) {
            LOGGER.error(String.format("url:%s--\n ClientProtocolException:%s", url, e.getMessage()), e);
            throw new ClientProtocolException(e);
        } catch (IOException e) {
            throw new IOException(e);
        } finally {
            get.releaseConnection();
        }

        return response;
    }

    /**
    *@author zhanghesheng
    * @param file 文件
    * @date 2017/11/25
    * @return 转化后的字节数组
    *@Description 文件转化为字节数组
    */
    public static byte[] file2byte(File file) {
        byte[] buffer = null;
        try {
            FileInputStream fis = new FileInputStream(file);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            byte[] b = new byte[1024];
            int n;
            while ((n = fis.read(b)) != -1) {
                bos.write(b, 0, n);
            }
            IOUtils.closeQuietly(fis);
            IOUtils.closeQuietly(bos);
            buffer = bos.toByteArray();
        } catch (FileNotFoundException e) {
            log.error("action=file2byte, occur exception", e);
        } catch (IOException e) {
            log.error("action=file2byte, occur exception", e);
        }
        return buffer;
    }

}
