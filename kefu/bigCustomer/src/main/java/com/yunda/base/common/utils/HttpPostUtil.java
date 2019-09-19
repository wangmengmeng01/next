package com.yunda.base.common.utils;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpPostUtil {

    private static Logger log = LoggerFactory.getLogger(HttpPostUtil.class);

    public String post(String url, String content) {
        String responsemsg = "";
        try {
            URL postUrl = new URL(url);
            HttpURLConnection connection = (HttpURLConnection) postUrl
                    .openConnection();

            // 设置是否向connection输出，因为这个是post请求，参数要放在
            // http正文内，因此需要设为true
            connection.setDoOutput(true);
            // Read from the connection. Default is true.
            connection.setDoInput(true);
            // 默认是 GET方式
            connection.setRequestMethod("POST");

            // Post 请求不能使用缓存
            connection.setUseCaches(false);

            connection.setInstanceFollowRedirects(true);

            connection.setRequestProperty("Content-Type",
                    "application/json;charset=UTF-8");
            connection.connect();
            DataOutputStream out = new DataOutputStream(
                    connection.getOutputStream());
            // DataOutputStream.writeBytes将字符串中的16位的unicode字符以8位的字符形式写到流里面
            out.write(content.getBytes());

            out.flush();
            out.close();

            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    connection.getInputStream()));
            String line = "";
            while ((line = reader.readLine()) != null) {
                responsemsg = line;
            }
            reader.close();
            connection.disconnect();
        } catch (Exception e) {
            log.error("HttpPostUtil post Exception",e);
        }
        return responsemsg;
    }

    /**
     * 
     * @param urlPath
     *            下载路径
     * @param
     *            下载存放目录
     * @return 返回下载文件
     * @author ysn
     * @since 2017年2月15日
     */
    @SuppressWarnings({ "finally", "unused" })
    public static File downloadFile(String urlPath,HttpServletResponse response) {
        File file = null;
        BufferedInputStream bin = null;
        OutputStream out = null;
        try {
            // 统一资源
            URL url = new URL(urlPath);
            // 连接类的父类，抽象类
            URLConnection urlConnection = url.openConnection();
            // http的连接类
            HttpURLConnection httpURLConnection = (HttpURLConnection) urlConnection;
            // 设定请求的方法，默认是GET
            httpURLConnection.setRequestMethod("GET");
            // 设置字符编码
            httpURLConnection.setRequestProperty("Charset", "UTF-8");
            // 打开到此 URL 引用的资源的通信链接（如果尚未建立这样的连接）。
            httpURLConnection.connect();

            // 文件大小
//            int fileLength = httpURLConnection.getContentLength();

            // 文件名
            String filePathUrl = httpURLConnection.getURL().getFile();
            String fileFullName = filePathUrl.substring(filePathUrl
                    .lastIndexOf("/") + 1);
//            System.out.println("file length---->" + fileLength);
            url.openConnection();

            bin = new BufferedInputStream(
                    httpURLConnection.getInputStream());

            out = response.getOutputStream();
            int size = 0;
            int len = 0;
            byte[] buf = new byte[1024];
            while ((size = bin.read(buf)) != -1) {
                len += size;
                out.write(buf, 0, size);
//                 打印下载百分比
//                 System.out.println("下载了-------> " + len * 100 / fileLength
//                 + "%\n");
            }
        } catch (MalformedURLException e) {
            log.warn(e.getMessage(), e);
        } catch (IOException e) {
            log.warn(e.getMessage(), e);
        } finally {
            try {
                bin.close();
                out.close(); 
            } catch (IOException e) {
                log.warn(e.getMessage(),e);
            }
        }
        return file;

    }
}
