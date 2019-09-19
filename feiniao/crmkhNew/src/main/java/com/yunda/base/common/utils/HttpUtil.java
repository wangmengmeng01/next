package com.yunda.base.common.utils;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * <p>Http工具类
 * 
 * <p>Http工具类，为系统提供通用Http访问操作方法：
 * 
 * <p>1、发送GET请求；
 * <p>2、发送POST请求。
 * 
 */
public class HttpUtil {

    /**
     * <p>发送GET请求
     * 
     * @param  url GET请求地址
     * 
     * @return 与当前请求对应的响应内容字节数组
     * 
     */
    public static byte[] doGet(String url) {

        return HttpUtil.doGet(url , null , null , 0);
    }

    /**
     * <p>发送GET请求
     * 
     * @param  url       GET请求地址
     * @param  headerMap GET请求头参数容器
     * 
     * @return 与当前请求对应的响应内容字节数组
     * 
     */
    public static byte[] doGet(String url , Map headerMap) {

        return HttpUtil.doGet(url , headerMap , null , 0);
    }

    /**
     * <p>发送GET请求
     * 
     * @param  url       GET请求地址
     * @param  proxyUrl  代理服务器地址
     * @param  proxyPort 代理服务器端口号
     * 
     * @return 与当前请求对应的响应内容字节数组
     * 
     * @modify 窦海宁, 2012-03-19
     */
    public static byte[] doGet(String url , String proxyUrl , int proxyPort) {

        return HttpUtil.doGet(url , null , proxyUrl , proxyPort);
    }

    /**
     * <p>发送GET请求
     * 
     * @param  url       GET请求地址
     * @param  headerMap GET请求头参数容器
     * @param  proxyUrl  代理服务器地址
     * @param  proxyPort 代理服务器端口号
     * 
     * @return 与当前请求对应的响应内容字节数组
     * 
     * @modify 窦海宁, 2012-03-19
     */
    public static byte[] doGet(String url , Map headerMap , String proxyUrl , int proxyPort) {

        byte[]     content    = null;
        HttpClient httpClient = new HttpClient();
        GetMethod  getMethod  = new GetMethod(url);

        if (headerMap != null) {

            //头部请求信息
            if (headerMap != null) {

                Iterator iterator = headerMap.entrySet().iterator();
                while (iterator.hasNext()) {

                    Entry entry = (Entry) iterator.next();
                    getMethod.addRequestHeader(entry.getKey().toString() , entry.getValue().toString());
                }
            }
        }

        if (StringUtils.isNotBlank(proxyUrl)) {

            httpClient.getHostConfiguration().setProxy(proxyUrl , proxyPort);
        }

        //设置成了默认的恢复策略，在发生异常时候将自动重试3次，在这里你也可以设置成自定义的恢复策略
        getMethod.getParams().setParameter(HttpMethodParams.SO_TIMEOUT , 10000);
        //postMethod.getParams().setParameter(HttpMethodParams.RETRY_HANDLER , new DefaultHttpMethodRetryHandler());
        InputStream inputStream = null;
        try {

            if (httpClient.executeMethod(getMethod) == HttpStatus.SC_OK) {

                //读取内容
                inputStream = getMethod.getResponseBodyAsStream();
                content     = IOUtils.toByteArray(inputStream);
            } else {

                System.err.println("Method failed: " + getMethod.getStatusLine());
            }
        } catch (IOException ex) {

           // ex.printStackTrace();
        } finally {

            IOUtils.closeQuietly(inputStream);
            getMethod.releaseConnection();
        }
        return content;
    }

    /**
     * <p>发送POST请求
     * 
     * @param  url          POST请求地址
     * @param  parameterMap POST请求参数容器
     * 
     * @return 与当前请求对应的响应内容字节数组
     * 
     */
    public static byte[] doPost(String url , Map parameterMap) {

        return HttpUtil.doPost(url , null , parameterMap , null , null , 0);
    }

    /**
     * <p>发送POST请求
     * 
     * @param  url          POST请求地址
     * @param  parameterMap POST请求参数容器
     * @param  paramCharset 参数字符集名称
     * 
     * @return 与当前请求对应的响应内容字节数组
     * 
     * @modify 窦海宁, 2012-05-21
     */
    public static byte[] doPost(String url , Map parameterMap , String paramCharset) {

        return HttpUtil.doPost(url , null , parameterMap , paramCharset , null , 0);
    }

    /**
     * <p>发送POST请求
     * 
     * @param  url          POST请求地址
     * @param  headerMap    POST请求头参数容器
     * @param  parameterMap POST请求参数容器
     * @param  paramCharset 参数字符集名称
     * 
     * @return 与当前请求对应的响应内容字节数组
     * 
     * @modify 窦海宁, 2012-05-21
     */
    public static byte[] doPost(String url , Map headerMap , Map parameterMap , String paramCharset) {

        return HttpUtil.doPost(url , headerMap , parameterMap , paramCharset , null , 0);
    }

    /**
     * <p>发送POST请求
     * 
     * @param  url          POST请求地址
     * @param  parameterMap POST请求参数容器
     * @param  paramCharset 参数字符集名称
     * @param  proxyUrl     代理服务器地址
     * @param  proxyPort    代理服务器端口号
     * 
     * @return 与当前请求对应的响应内容字节数组
     * 
     */
    public static byte[] doPost(String url , Map parameterMap , String paramCharset , String proxyUrl , int proxyPort) {

        return HttpUtil.doPost(url , null , parameterMap , paramCharset , proxyUrl , proxyPort);
    }

    /**
     * <p>发送POST请求
     * 
     * @param  url          POST请求地址
     * @param  headerMap    POST请求头参数容器
     * @param  parameterMap POST请求参数容器
     * @param  paramCharset 参数字符集名称
     * @param  proxyUrl     代理服务器地址
     * @param  proxyPort    代理服务器端口号
     * 
     * @return 与当前请求对应的响应内容字节数组
     * 
     * @modify 窦海宁, 2012-05-21
     */
    public static byte[] doPost(String url , Map headerMap , Map parameterMap , String paramCharset , String proxyUrl , int proxyPort) {

        byte[]     content    = null;
        HttpClient httpClient = new HttpClient();
        PostMethod postMethod = new PostMethod(url);

        if (StringUtils.isNotBlank(paramCharset)) {

            postMethod.getParams().setContentCharset(paramCharset);
            postMethod.getParams().setHttpElementCharset(paramCharset);
        }

        if (headerMap != null) {

            //头部请求信息
            if (headerMap != null) {

                Iterator iterator = headerMap.entrySet().iterator();
                while (iterator.hasNext()) {

                    Entry entry = (Entry) iterator.next();
                    postMethod.addRequestHeader(entry.getKey().toString() , entry.getValue().toString());
                }
            }
        }

        Iterator iterator = parameterMap.keySet().iterator();
        while (iterator.hasNext()) {

            String key = (String) iterator.next();
            postMethod.addParameter(key , (String) parameterMap.get(key));
        }

        if (StringUtils.isNotBlank(proxyUrl)) {

            httpClient.getHostConfiguration().setProxy(proxyUrl , proxyPort);
        }

        //设置成了默认的恢复策略，在发生异常时候将自动重试3次，在这里你也可以设置成自定义的恢复策略
        postMethod.getParams().setParameter(HttpMethodParams.SO_TIMEOUT , 10000);
        //postMethod.getParams().setParameter(HttpMethodParams.RETRY_HANDLER , new DefaultHttpMethodRetryHandler());
        InputStream inputStream = null;
        try {

            if (httpClient.executeMethod(postMethod) == HttpStatus.SC_OK) {

                //读取内容
                inputStream = postMethod.getResponseBodyAsStream();
                content     = IOUtils.toByteArray(inputStream);
            } else {

                System.err.println("Method failed: " + postMethod.getStatusLine());
            }
        } catch (IOException ex) {

           // ex.printStackTrace();
        } finally {

            IOUtils.closeQuietly(inputStream);
            postMethod.releaseConnection();
        }
        return content;
    }
    
   public static JSONObject doPost(String url,JSONArray json){
        
        CloseableHttpClient httpclient = HttpClientBuilder.create().build();
        HttpPost post = new HttpPost(url);
        JSONObject response = null;
        try {
            StringEntity s = new StringEntity(json.toString());
            s.setContentEncoding("UTF-8");
            s.setContentType("application/json");//发送json数据需要设置contentType
            post.setEntity(s);
            HttpResponse res = httpclient.execute(post);
            if(res.getStatusLine().getStatusCode() == HttpStatus.SC_OK){
                String result = EntityUtils.toString(res.getEntity());// 返回json格式：
                response = JSONObject.fromObject(result);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return response;
    }
    public static void main(String[] args) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("wd", "nima");
        byte[] b = doGet("http://www.baidu.com", map);
        System.out.println("-------------------"+new String(b));
    }
    


    public static String post(String requestUrl, String accessToken, String params)
            throws Exception {
        String contentType = "application/x-www-form-urlencoded";
        return HttpUtil.post(requestUrl, accessToken, contentType, params);
    }

    public static String post(String requestUrl, String accessToken, String contentType, String params)
            throws Exception {
        String encoding = "UTF-8";
        if (requestUrl.contains("nlp")) {
            encoding = "GBK";
        }
        return HttpUtil.post(requestUrl, accessToken, contentType, params, encoding);
    }

    public static String post(String requestUrl, String accessToken, String contentType, String params, String encoding)
            throws Exception {
        String url = requestUrl + "?access_token=" + accessToken;
        return HttpUtil.postGeneralUrl(url, contentType, params, encoding);
    }

    public static String postGeneralUrl(String generalUrl, String contentType, String params, String encoding)
            throws Exception {
        URL url = new URL(generalUrl);
        // 打开和URL之间的连接
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        // 设置通用的请求属性
        connection.setRequestProperty("Content-Type", contentType);
        connection.setRequestProperty("Connection", "Keep-Alive");
        connection.setUseCaches(false);
        connection.setDoOutput(true);
        connection.setDoInput(true);

        // 得到请求的输出流对象
        DataOutputStream out = new DataOutputStream(connection.getOutputStream());
        out.write(params.getBytes(encoding));
        out.flush();
        out.close();

        // 建立实际的连接
        connection.connect();
        // 获取所有响应头字段
        Map<String, List<String>> headers = connection.getHeaderFields();
        // 遍历所有的响应头字段
        for (String key : headers.keySet()) {
            System.err.println(key + "--->" + headers.get(key));
        }
        // 定义 BufferedReader输入流来读取URL的响应
        BufferedReader in = null;
        in = new BufferedReader(
                new InputStreamReader(connection.getInputStream(), encoding));
        String result = "";
        String getLine;
        while ((getLine = in.readLine()) != null) {
            result += getLine;
        }
        in.close();
        System.err.println("result:" + result);
        return result;
    }

}