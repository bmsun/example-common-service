package com.example.commons.utils;

import com.example.commons.utils.exp.PathConstant;
import com.example.commons.utils.exp.ResultResponse;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Map;
import java.util.Set;

public class ReqUrlUtil {
    private static Logger LOGGER = LoggerFactory.getLogger(ReqUrlUtil.class);

    public static ResultResponse getResponse(String url, String json,ContentType contentType) {
        ResultResponse response;
        if (StringUtils.isBlank(json)) {
            // System.out.println("GET请求");
            response = getResponseWithGet(url);
        } else {
            response = getResponseWithPost(url,json,contentType);
        }
        return response;
    }

    //get 请求
    private static ResultResponse getResponseWithGet(String url) {
        ResultResponse moxieResponse = new ResultResponse();
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet(url);
        LOGGER.debug("GET 请求....", httpGet.getURI());
        //设置请求头信息
        httpGet.setHeader(PathConstant.AUTHORIZATION_KEY, PathConstant.AUTHORIZATION_VALUE);
        httpGet.setHeader(PathConstant.X_MOXIE_PARAM_KEY, PathConstant.X_MOXIE_PARAM_VALUE);
        httpGet.setHeader(HTTP.CONTENT_TYPE, PathConstant.APPLICATION_JSON);
        httpGet.setHeader(PathConstant.ACCEPT_KEY, PathConstant.ACCEPT_VALUE);
        try {
            CloseableHttpResponse httpResponse = httpClient.execute(httpGet);
            moxieResponse.setHttpStatusCode(httpResponse.getStatusLine().getStatusCode());
            moxieResponse.setHttpMessage(httpResponse.getStatusLine().getReasonPhrase());
            HttpEntity httpEntity = httpResponse.getEntity();
            if (httpEntity != null) {
                String result = EntityUtils.toString(httpEntity);
                moxieResponse.setResult(result);
                LOGGER.debug("GET请求返回体：", result);
            }
            httpResponse.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                httpClient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return moxieResponse;
    }

    //post请求
    private static ResultResponse getResponseWithPost(String url, String json, ContentType type) {
        ResultResponse moxieResponse = new ResultResponse();
        CloseableHttpClient httpClient = HttpClients.createDefault();
        try {
            HttpPost post = new HttpPost(url);

            //设置请求头信息
            post.setHeader(PathConstant.AUTHORIZATION_KEY, PathConstant.AUTHORIZATION_VALUE);
            post.setHeader(PathConstant.X_MOXIE_PARAM_KEY, PathConstant.X_MOXIE_PARAM_VALUE);
            post.setHeader(HTTP.CONTENT_TYPE, PathConstant.APPLICATION_JSON);
            post.setHeader(PathConstant.ACCEPT_KEY, PathConstant.ACCEPT_VALUE);
            //设置请求体信息
            StringEntity se = new StringEntity(json, type);
            post.setEntity(se);
            LOGGER.debug("POST 请求....", post.getURI());
            //执行请求
            CloseableHttpResponse httpResponse = httpClient.execute(post);
            try {
                HttpEntity entity = httpResponse.getEntity();
                if (null != entity) {
                    String result = EntityUtils.toString(entity);
                    moxieResponse.setResult(result);
                    LOGGER.debug("POST请求返回体：", EntityUtils.toString(entity));
                }
            } finally {
                httpResponse.close();
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (httpClient != null) {
                    httpClient.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return moxieResponse;
    }


    public static String getMethod(String url, Map<String, String> headers, String cookies) {
        String result = null;
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet(url);
        LOGGER.debug("GET 请求....", httpGet.getURI());
        //设置请求头信息
        if (headers!=null) {
            Set<Map.Entry<String, String>> entries = headers.entrySet();
            for (Map.Entry<String, String> entry : entries) {
                httpGet.setHeader(entry.getKey(), entry.getValue());
            }
        }
        //添加Cookie
        if (StringUtils.isNotBlank(cookies)) {
            httpGet.addHeader(new BasicHeader("Cookie", cookies));
        }
        try {
            CloseableHttpResponse httpResponse = httpClient.execute(httpGet);
            HttpEntity httpEntity = httpResponse.getEntity();
            if (httpEntity != null) {
                result = EntityUtils.toString(httpEntity);
                LOGGER.debug("GET请求返回体：", result);
            }
            httpResponse.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                httpClient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return result;
    }

    public static String postMethod(String url, String json, ContentType type, Map<String, String> headers, String cookies){
         String result =null;
        CloseableHttpClient httpClient = HttpClients.createDefault();
        try {
            HttpPost post = new HttpPost(url);
            //设置请求头信息
            if (headers!=null) {
                Set<Map.Entry<String, String>> entries = headers.entrySet();
                for (Map.Entry<String, String> entry : entries) {
                    post.setHeader(entry.getKey(), entry.getValue());
                }
            }
            //添加Cookie
            if (StringUtils.isNotBlank(cookies)) {
                post.addHeader(new BasicHeader("Cookie", cookies));
            }
            //设置请求体信息
            StringEntity se = new StringEntity(json, type);
            post.setEntity(se);
            LOGGER.debug("POST 请求....", post.getURI());
            //执行请求
            CloseableHttpResponse httpResponse = httpClient.execute(post);
            try {
                HttpEntity entity = httpResponse.getEntity();
                if (null != entity) {
                    result = EntityUtils.toString(entity);
                    LOGGER.debug("POST请求返回体：", result);
                }
            } finally {
                httpResponse.close();
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (httpClient != null) {
                    httpClient.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return result;
    }
}
