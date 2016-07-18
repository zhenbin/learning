package com.zhenbin.lzb.util;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by zhenbin.lzb on 2016/7/14.
 */
public class HttpUtils {

    private final static String CHAR_SET = "UTF-8";

    /**
     * HTTP GET
     *
     * @param url
     * @throws IOException
     */
    public static void get(String url) throws IOException {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet(url);
        CloseableHttpResponse response = httpclient.execute(httpGet);
        try {
            HttpEntity entity = response.getEntity();
            InputStream instream = entity.getContent();
            String responseStr = EntityUtils.toString(entity, CHAR_SET);
            System.out.println(response.getStatusLine());
            System.out.println("响应报文Response content：\n" + responseStr);
            EntityUtils.consume(entity);

        } finally {
            response.close();
            httpclient.close();
        }
    }

    /**
     * HTTP POST 完整Body
     *
     * @param url
     * @param bodyContent
     * @param contentType
     * @return
     * @throws IOException
     */
    public static String postBody(String url, String bodyContent, String contentType) throws IOException {
        CloseableHttpClient httpclient = HttpClients.createDefault();

        String responseBody;
        HttpPost httpPost = new HttpPost(url);
        httpPost.setHeader("Content-Type", contentType);
        HttpEntity xmlEntity = new ByteArrayEntity(bodyContent.getBytes(CHAR_SET));
        httpPost.setEntity(xmlEntity);
        HttpResponse response = httpclient.execute(httpPost);
        try {
            HttpEntity entity = response.getEntity();
            System.out.println(response.getStatusLine());
            responseBody = EntityUtils.toString(entity, CHAR_SET);
            System.out.println("响应报文Response content：\n" + responseBody);
            EntityUtils.consume(entity);
        } finally {
            httpclient.close();
        }
        return responseBody;
    }

    public static String postParams(String url, Map<String, String> params) throws IOException {
        return postParams(url, params, "application/x-www-form-urlencoded");
    }

    /**
     * HTTP POST Form Parmas
     *
     * @param url
     * @param params
     * @param contentType
     * @return
     * @throws IOException
     */
    public static String postParams(String url, Map<String, String> params, String contentType) throws IOException {
        CloseableHttpClient httpclient = HttpClients.createDefault();

        List<NameValuePair> nvps = new ArrayList<NameValuePair>();
        for (String key : params.keySet()) {
            nvps.add(new BasicNameValuePair(key, params.get(key)));
        }

        String responseBody;
        HttpPost httpPost = new HttpPost(url);
        httpPost.setHeader("Content-Type", contentType);
        HttpEntity httpEntity = new UrlEncodedFormEntity(nvps, CHAR_SET);
        httpPost.setEntity(httpEntity);
        HttpResponse response = httpclient.execute(httpPost);
        try {
            HttpEntity entity = response.getEntity();
            System.out.println(response.getStatusLine());
            responseBody = EntityUtils.toString(entity, CHAR_SET);
            System.out.println("响应报文Response content：\n" + responseBody);
            EntityUtils.consume(entity);
        } finally {
            httpclient.close();
        }
        return responseBody;
    }

    /**
     * get
     *
     * @param url     请求的url
     * @param queries 请求的参数，在浏览器？后面的数据，没有可以传null
     * @return
     * @throws IOException
     */
    public static String get(String url, Map<String, String> queries) throws IOException {
        String responseBody = "";
        CloseableHttpClient httpClient = HttpClients.createDefault();
        StringBuilder sb = new StringBuilder(url);

        if (queries != null && queries.keySet().size() > 0) {
            boolean firstFlag = true;
            Iterator iterator = queries.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry entry = (Map.Entry<String, String>) iterator.next();
                if (firstFlag) {
                    sb.append("?" + (String) entry.getKey() + "=" + URLEncoder.encode((String) entry.getValue(), "UTF-8"));
                    firstFlag = false;
                } else {
                    sb.append("&" + (String) entry.getKey() + "=" + URLEncoder.encode((String) entry.getValue(), "UTF-8"));
                }
            }
        }

        HttpGet httpGet = new HttpGet(sb.toString());
        System.out.println("Executing request " + httpGet.getRequestLine());
        //请求数据
        CloseableHttpResponse response = httpClient.execute(httpGet);
        System.out.println(response.getStatusLine().toString());
        int status = response.getStatusLine().getStatusCode();
        if (status == HttpStatus.SC_OK) {
            HttpEntity entity = response.getEntity();
            responseBody = EntityUtils.toString(entity);
        }
        return responseBody;
    }

    public static String subCallBackString(String content) {
        int head = content.indexOf("(") + 1;
        int tail = content.lastIndexOf(")");
        return content.substring(head, tail);
    }
}
