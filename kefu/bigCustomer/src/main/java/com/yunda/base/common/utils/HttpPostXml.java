package com.yunda.base.common.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Map;

import com.yunda.base.system.config.SysConfig;

/**
 * 
 * @ClassName: HttpPostXml
 * @Description: TODO 使用http+post+xml方式访问远端接口
 * @author: zhrb
 * @date: 2018年7月6日 上午11:26:28 v1.0
 */
public class HttpPostXml {
	/**
	 * 
	 * @author:            zhrb
	 * @Title:             creatPostAndTransData
	 * @Description:       TODO
	 * @param:             @param dataMap,内含ip,disPhone,email
	 * @param:             @return   String 类型,string是请求接口返回的报文信息拼接的字符串
	 * @return:            String   
	 * @throws MalformedURLException,IOException
	 */
	public static String creatPostAndTransData(Map<String, Object> dataMap) {
		String urlStr = "";
		String line = "";
		StringBuffer resultSting = new StringBuffer();
		try {
			//urlStr,写在了配置文件中,直接进行获取
			urlStr = SysConfig.LAN_JIE;
			//声明URL
			URL url = new URL(urlStr);
			//1.创建链接
			URLConnection con = url.openConnection();
			//2.封装报文传输进行传输
			//调用getXmlInfo(Map<String, Object> dataMap)进行报文的封装
			String xmlInfo = getXmlInfo(dataMap);
			byte[] xmlData = xmlInfo.getBytes();
			con.setDoOutput(true);   
			con.setDoInput(true);  
			con.setUseCaches(false); 
			con.setRequestProperty("Pragma:", "no-cache");
			con.setRequestProperty("Cache-Control", "no-cache");
			con.setRequestProperty("Content-Type", "text/xml");
			con.setRequestProperty("Content-length",String.valueOf(xmlData.length)); 
			OutputStreamWriter out = new OutputStreamWriter(
					con.getOutputStream());
			System.out.println(">>>>>>>>>>接口地址为urlStr=" + urlStr);
			System.out.println(">>>>>>>>>>传入的报文xmlInfo=" + xmlInfo);
			out.write(new String(xmlInfo.getBytes("ISO-8859-1")));
			out.flush();
			out.close();
			//3.获取返回报文
			BufferedReader br = new BufferedReader(new InputStreamReader(
					con.getInputStream()));
			//对返回值报文进行打印
			for (line = br.readLine(); line != null; line = br.readLine()) {
				//对返回的报文进行结果判断<RspCode>0000</RspCode>
				System.out.println(">>>>>>>>>>>>>>>>>>>返回的结果报文内容为:---------"+line);
				//对返回的报文进行拼接,然后返回给业务层,在业务层进行判断
				resultSting.append(line);
			}
			return resultSting.toString();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return resultSting.toString();
	}
	/**
	 * 
	 * @author:            zhrb
	 * @Title:             getXmlInfo
	 * @Description:       TODO 接收数据,拼接生成请求报文
	 * @param:             @param dataMap,内含ip,disPhone,email
	 * @param:             @return   
	 * @return:            String   
	 * @throws
	 */
	private static String getXmlInfo(Map<String, Object> dataMap) {
		String waybillNum   = "";
		String branchCode= "";
		String lanJieType= "";

		//服务层传参,新增用户的手机号
		waybillNum = dataMap.get("waybillNum").toString();
		//服务层传参,新增用户的邮箱
		branchCode = dataMap.get("branchCode").toString();
		lanJieType = dataMap.get("lanJieType").toString();
		// TODO 动态传入参数
		StringBuilder sb = new StringBuilder();
		sb.append("<req op=\"intercept\"><h><ver>3.0</ver></h>");
		sb.append("<data>");
		sb.append("    <o>");
		sb.append("        <d>" + waybillNum + "</d>");
		sb.append("        <d>" + lanJieType + "</d>");
		sb.append("        <d>" + branchCode + "</d>");
		sb.append("        <d>" + "拦截备注" + "</d>");
		sb.append("        <d>" + SysConfig.LAN_JIE_INFO_SOURCE_TYPE + "</d>");
		sb.append("        <d>" + SysConfig.LAN_JIE_FLAG + "</d>");
		sb.append("        <d>"  + "</d>");
		sb.append("        <d>"  + "</d>");
		sb.append("        <d>"  + "</d>");
		sb.append("    </o>");
		sb.append("</data>");
		sb.append("</req>");
		return sb.toString();
	}	
}
