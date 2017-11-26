package com.example.commons;

import com.google.common.base.Preconditions;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import retrofit2.Call;
import retrofit2.CallAdapter;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLEngine;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509ExtendedTrustManager;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.net.Socket;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Slf4j
public class BaseRetrofitUtils {
    private static final Integer DefaultConnectTimeout = 5;
    private static final Integer DefaultWriteTimeout = 10;
    private static final Integer DefaultReadTimeout = 10;

    public static Retrofit.Builder builder(String baseUrl, Map<String, String> headers) {
        return builder(baseUrl, headers, DefaultConnectTimeout, DefaultWriteTimeout, DefaultReadTimeout);
    }

    public static Retrofit.Builder builder(String baseUrl, Map<String, String> headers, Integer connectTimeout, Integer writeTimeout, Integer readTimeout) {
        Preconditions.checkArgument(StringUtils.isNotBlank(baseUrl), "baseUrl不能为空");
        Preconditions.checkArgument(connectTimeout != null, "connectTimeout不能为null");
        Preconditions.checkArgument(writeTimeout != null, "writeTimeout");
        Preconditions.checkArgument(readTimeout != null, "readTimeout");

        OkHttpClient.Builder clientBuilder = getClientBuilder();
        if (headers != null && !headers.isEmpty()) {
            clientBuilder.addInterceptor((chain) -> {
                Request.Builder requestBuilder = chain.request().newBuilder();
                headers.entrySet().forEach(entry -> requestBuilder.addHeader(entry.getKey(), entry.getValue()));
                return chain.proceed(requestBuilder.build());
            });
        }

        clientBuilder.connectTimeout(connectTimeout, TimeUnit.SECONDS)
                .writeTimeout(writeTimeout, TimeUnit.SECONDS)
                .readTimeout(readTimeout, TimeUnit.SECONDS);

        return new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(JacksonConverterFactory.create())
                .addCallAdapterFactory(SynchronousCallAdapterFactory.create())
                .client(clientBuilder.build());
    }

    private static OkHttpClient.Builder getClientBuilder() {
        SSLContext sslContext = null;
        try {
            sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, new TrustManager[]{new MyTrustManager()}, new SecureRandom());
        } catch (Throwable e) {
            log.error("初始化SSLContext异常", e);
        }

        OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder();
        if (sslContext != null) {
            clientBuilder.sslSocketFactory(sslContext.getSocketFactory());
        }
        clientBuilder.hostnameVerifier(new NoopHostnameVerifier());
        return clientBuilder;
    }

    static class MyTrustManager extends X509ExtendedTrustManager {
        @Override
        public void checkClientTrusted(X509Certificate[] x509Certificates, String s, Socket socket)
                throws CertificateException {
        }

        @Override
        public void checkServerTrusted(X509Certificate[] x509Certificates, String s, Socket socket)
                throws CertificateException {
        }

        @Override
        public void checkClientTrusted(X509Certificate[] x509Certificates, String s, SSLEngine sslEngine)
                throws CertificateException {
        }

        @Override
        public void checkServerTrusted(X509Certificate[] x509Certificates, String s, SSLEngine sslEngine)
                throws CertificateException {
        }

        @Override
        public void checkClientTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {
        }

        @Override
        public void checkServerTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {
        }

        @Override
        public X509Certificate[] getAcceptedIssuers() {
            return new X509Certificate[0];
        }
    }

    static class SynchronousCallAdapterFactory extends CallAdapter.Factory {
        public static CallAdapter.Factory create() {
            return new SynchronousCallAdapterFactory();
        }

        @Override
        public CallAdapter<Object> get(final Type returnType, Annotation[] annotations, Retrofit retrofit) {
            if (returnType.getTypeName().contains("retrofit2.Call")) {
                return null;
            }

            return new CallAdapter<Object>() {
                @Override
                public Type responseType() {
                    return returnType;
                }

                @Override
                public <R> Object adapt(Call<R> call) {
                    try {
                        Response response = call.execute();
                        if (returnType.getTypeName().contains("retrofit2.Response")) {
                            return response;
                        }

                        if (response.body() != null) {
                            return response.body();
                        } else {
                            return BaseJsonUtils.readValue(response.errorBody().string(), Map.class);
                        }
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            };
        }
    }
}
