package com.qunar.common.utils;

import android.content.Context;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.qunar.common.BaseParam;
import com.qunar.common.CommonConfig;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.security.KeyStore;
import java.util.Iterator;
import java.util.Set;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManagerFactory;

/**
 * Https网络请求工具类
 * Created by chengxiang.peng on 2016/11/6.
 */
public class HttpsTools {
    //p12证书类型
    private static final String KEY_STORE_TYPE_P12 = "PKCS12";
    //bks证书类型
    private static final String KEY_STORE_TYPE_BKS = "bks";
    //客户端给服务器端认证的证书
    private static final String KEY_STORE_QPRPJECT_PATH = "qproject.p12";
    //客户端验证服务器端的信任证书库
    private static final String KEY_STORE_QPROJECTTRUST_PATH = "qproject.truststore";
    //客户端证书密码
    private static final String KEY_STORE_PASSWORD = "123456";
    //客户端信任证书库密码
    private static final String KEY_STORE_TRUST_PASSWORD = "123456";

    /**
     * 获取SSLContext
     *
     * @param context 上下文
     * @return SSLContext
     */
    public static SSLContext getSSLContext(Context context) {
        SSLContext sslContext = null;
        try {
            //初始化服务器端需要验证的客户端证书-qprojectKeyStore、客户端信任的服务器端证书库-trustKeyStore
            KeyStore qprojectKeyStore = KeyStore.getInstance(KEY_STORE_TYPE_P12);
            KeyStore trustKeyStore = KeyStore.getInstance(KEY_STORE_TYPE_BKS);
            InputStream qprojectInPutStream = context.getResources().getAssets().open(KEY_STORE_QPRPJECT_PATH);
            InputStream trustInputStream = context.getResources().getAssets().open(KEY_STORE_QPROJECTTRUST_PATH);
            try {
                qprojectKeyStore.load(qprojectInPutStream, KEY_STORE_PASSWORD.toCharArray());
                trustKeyStore.load(trustInputStream, KEY_STORE_TRUST_PASSWORD.toCharArray());
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    qprojectInPutStream.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    trustInputStream.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            //初始化SSLContext上下文对象
            sslContext = SSLContext.getInstance("TLS");
            TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            trustManagerFactory.init(trustKeyStore);
            KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance("X509");
            keyManagerFactory.init(qprojectKeyStore, KEY_STORE_PASSWORD.toCharArray());
            sslContext.init(keyManagerFactory.getKeyManagers(), trustManagerFactory.getTrustManagers(), null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sslContext;
    }

    /**
     * 获取HttpsURLConnection
     *
     * @param context 上下文
     * @param url     连接url
     * @param method  请求方式
     * @return HttpsURLConnection
     */
    public static HttpsURLConnection getHttpsURLConnection(Context context, String url, String method) {
        URL u;
        HttpsURLConnection connection = null;
        try {
            SSLContext sslContext = getSSLContext(context);
            if (sslContext != null) {
                u = new URL(url);
                connection = (HttpsURLConnection) u.openConnection();
                connection.setRequestMethod(method);
                connection.setDoOutput(true);
                connection.setDoInput(true);
                connection.setUseCaches(false);
                connection.setSSLSocketFactory(sslContext.getSocketFactory());
                connection.setConnectTimeout(30000);
                //忽略请求域名和证书域名的校验
                connection.setDefaultHostnameVerifier(new HostnameVerifier() {
                    public boolean verify(String string, SSLSession ssls) {
                        return true;
                    }
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return connection;
    }

    /**
     * 执行登录Get请求
     */
    public static String doGet(Context context, BaseParam params) {
        String result = null;
        try {
            String parasString = getParamsString(params);
            String url = CommonConfig.SERVER_URL + "HttpsServlet?" + parasString;
            HttpsURLConnection httpsURLConnection = HttpsTools.getHttpsURLConnection(context, url, "GET");
            if (httpsURLConnection.getResponseCode() == 200) {
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(httpsURLConnection.getInputStream()));
                String readLine;
                if ((readLine = bufferedReader.readLine()) != null) {
                    result += readLine;
                }
                bufferedReader.close();
                httpsURLConnection.disconnect();
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }

    /**
     * 执行登录Post请求
     */
    public static String doPost(Context context, BaseParam params) {
        String result = new String();
        try {
            String parasString = getParamsString(params);
            HttpsURLConnection httpsURLConnection = HttpsTools.getHttpsURLConnection(context, CommonConfig.SERVER_URL + "HttpsServlet", "POST");
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(httpsURLConnection.getOutputStream()));
            bufferedWriter.write(parasString);
            bufferedWriter.flush();

            if (httpsURLConnection.getResponseCode() == 200) {
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(httpsURLConnection.getInputStream()));
                String readLine;
                if ((readLine = bufferedReader.readLine()) != null) {
                    result += readLine;
                }
                bufferedReader.close();
                httpsURLConnection.disconnect();
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }

    /**
     * 获取参数字符串
     *
     * @param params 网络请求对象
     * @return 参数字符串
     */
    private static String getParamsString(BaseParam params) {
        JSONObject jsonObject = (JSONObject) JSON.toJSON(params);

        int index = 0;
        String parasString = new String();

        Set<String> keys = jsonObject.keySet();
        Iterator iterator = keys.iterator();
        while (iterator.hasNext()) {
            String key = (String) iterator.next();
            Object value = jsonObject.get(key);
            parasString += (key + "=" + value);

            if (index < keys.size() - 1) {
                parasString += "&";
            }
            index++;
        }

        return parasString;
    }
}