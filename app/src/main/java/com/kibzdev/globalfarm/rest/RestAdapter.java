package com.kibzdev.globalfarm.rest;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.kibzdev.globalfarm.utils.AppUtils;


import java.security.KeyStore;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RestAdapter {

    public static ApiInterface createAPI() {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();

        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient.Builder builder = new OkHttpClient.Builder();

        try {
            // Create a trust manager that does not validate certificate chains
            TrustManager[] trustAllCerts = {
                    new X509TrustManager() {
                        @Override
                        public void checkClientTrusted(X509Certificate[] chain, String authType)
                                throws CertificateException {
                        }

                        @Override
                        public void checkServerTrusted(X509Certificate[] chain, String authType)
                                throws CertificateException {
                        }

                        @Override
                        public X509Certificate[] getAcceptedIssuers() {
                            return new X509Certificate[0];
                        }
                    }
            };

            // Install the all-trusting trust manager
            SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
            // Create an ssl socket factory with our all-trusting manager
            SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();

            //Enable Full Body Logging
            HttpLoggingInterceptor logger = new HttpLoggingInterceptor();

            logger.setLevel(HttpLoggingInterceptor.Level.BODY);


            TrustManagerFactory trustManagerFactory = TrustManagerFactory.
                    getInstance(TrustManagerFactory.getDefaultAlgorithm());
            trustManagerFactory.init((KeyStore) null);
            TrustManager[] trustManagers = trustManagerFactory.getTrustManagers();

            if (trustManagers.length != 1 || !(trustManagers[0] instanceof X509TrustManager)) {
                throw new IllegalStateException("Unexpected default trust managers:" + Arrays.
                        toString(trustManagers));
            }

            X509TrustManager trustManager = (X509TrustManager) trustManagers[0];

            //Set SSL certificate to OkHttpClient Builder
//            builder.sslSocketFactory(sslSocketFactory, trustManager);

            builder.hostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            });


        } catch (Exception e) {
            throw new RuntimeException(e);
        }


        builder.connectTimeout(180, TimeUnit.SECONDS);//5
        builder.writeTimeout(180, TimeUnit.SECONDS);//10
        builder.readTimeout(180, TimeUnit.SECONDS);//30

        //Enable Full Body Logging
        HttpLoggingInterceptor logger = new HttpLoggingInterceptor();


        //Interceptor :> Full Body utils.Logr and ApiRequest Header


        logger.setLevel(HttpLoggingInterceptor.Level.BODY);
        builder.addInterceptor(logger);
        builder.addInterceptor(logging);

        Gson gson = new GsonBuilder()
                .setLenient()
                .create();


        builder.cache(null);
        OkHttpClient okHttpClient = builder.build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(AppUtils.ip)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(okHttpClient)
                .build();

        return retrofit.create(ApiInterface.class);

    }


}
