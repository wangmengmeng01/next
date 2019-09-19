package com.yunda.base.bigcustomer.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;
import javax.crypto.spec.IvParameterSpec;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Base64Utils;

import com.yunda.base.bigcustomer.dao.OrderDao;
import com.yunda.base.bigcustomer.domain.JieDanDO;
import com.yunda.base.bigcustomer.domain.OperateDO;
import com.yunda.base.bigcustomer.domain.OrderDO;
import com.yunda.base.bigcustomer.domain.OrderTemplateDO;
import com.yunda.base.bigcustomer.domain.ZhuanFaDO;
import com.yunda.base.bigcustomer.service.OrderService;
import com.yunda.base.bigcustomer.service.OrganizationManageService;
import com.yunda.base.common.utils.HttpUtil;
import com.yunda.base.common.utils.ShiroUtils;
import com.yunda.base.system.config.SysConfig;
import com.yunda.base.system.domain.UserDO;
import com.yunda.ydmbspringbootstarter.common.utils.DateUtils;
import com.yunda.ydmbspringbootstarter.common.utils.RC4Util;
import com.yunda.ydmbspringbootstarter.common.utils.StringUtils;

import net.minidev.json.JSONValue;
import net.sf.json.JSONObject;


@Service
public class OrderServiceImpl implements OrderService {
	Logger log = LoggerFactory.getLogger(OrderServiceImpl.class);
	@Autowired
	private OrderDao orderDao;
	@Autowired
	StringRedisTemplate stringRedisTemplate;
	@Autowired
	private OrganizationManageService organizationManageService;

	@Override
	@Transactional
	public void saveList(List<OrderTemplateDO> list) {
		//这里改成了批量插入数据  单个咨询单的保存方式就需要修改
		String username = ShiroUtils.getUser().getUsername();
        String name = ShiroUtils.getUser().getName();
		//获取发起机构编码和机构名称
		String orgCode = this.getOrgCodeByUserName(username);
		String orgName = organizationManageService.listOrgName(orgCode);
        List<OrderTemplateDO> orderList = new ArrayList<OrderTemplateDO>();//运单表
		OrderTemplateDO orderTreat = new OrderTemplateDO();
        List<OperateDO> operateList = new ArrayList<OperateDO>();//处理记录表
        OperateDO operateDO = new OperateDO();
        for(OrderTemplateDO orderDO : list) {
			orderTreat = getOrderTemplate(orderDO,username,name,orgCode,orgName);
			orderList.add(orderTreat);//运单表信息处理后添加到list 后续批量插入
			
			operateDO = new OperateDO();
            operateDO.setOrderId(orderDO.getOrderId());
            operateDO.setOperateName(name);
            operateDO.setOperateCode(username);
            operateDO.setType("发起咨询");
            
            operateDO.setOperateOrganization(orgCode);
            operateDO.setTime(DateUtils.getCurrentTime());
            operateList.add(operateDO);
		}
        //批量插入
        orderDao.saveOrderList(orderList);
        orderDao.saveOperateList(operateList);
	}

	@Override
	@Transactional
	public int save(OrderDO order) {
		//封装数据
		OrderDO orderDO = this.getOrder(order);
        int flag = orderDao.save(orderDO);

        //处理操作记录(后面用aop来处理)
		//判断是不是勾选了多个,如果勾选多个就要批量存放操作记录
		//获取用户账号
		OperateDO operateDO = new OperateDO();
		operateDO.setOrderId(order.getOrderId());
		operateDO.setOperateName(orderDO.getFaqiName());
		operateDO.setOperateCode(orderDO.getFaqiCode());
		operateDO.setType("发起咨询");
		//用户所属机构,最好存放机构编码
		operateDO.setOperateOrganization(orderDO.getFaqiOrgCode());
		operateDO.setTime(DateUtils.getCurrentTime());
		this.saveOperateInfo(operateDO);
		return flag;
	}


	@Override
	public OrderDO get(Integer id){
		return orderDao.get(id);
	}
	
	@Override
	public List<OrderDO> list(Map<String, Object> map){
		return orderDao.list(map);
	}
	
	@Override
	public int count(Map<String, Object> map){
		return orderDao.count(map);
	}
	
	@Override
	public OrderDO getOrder(OrderDO order){
		//资源来源默认工作台发起
		order.setConsultSource(SysConfig.CONSULT_SOURCE);
		//生成14位咨询订单号(6位年月日+流水号)
		//String datePrefix = DateUtils.formatDate(new Date(),"yyMMdd");
		//流水号根据数据库中最大id值来获取下一个流水号
		//int maxId = orderDao.getMaxId()+1;
		//String maxIdString = String.valueOf(maxId);
		//高位补0
		/*if(maxIdString.length()<8){
			int num = 8-maxIdString.length();
			DecimalFormat df = new DecimalFormat(SysConfig.STR_FORMAT);
			String formatOrderId = df.format(maxId);
			String orderId = datePrefix+formatOrderId;
			order.setOrderId(orderId);
		}*/
		Date date =new Date();
		order.setOrderId(StringUtils.generateNumber(date, ""));//咨询单号  用时间+流水号生成
		
		order.setConsultTime(DateUtils.getCurrentTime());
		//刚创建状态为待申领
		order.setState("待申领");
		//存入发起人账号
		String username = ShiroUtils.getUser().getUsername();
		order.setFaqiCode(username);
		//根据订单号查询运单信息
		//String logisticOrderNum = order.getLogisticOrderNum();
		String waybillNum = order.getWaybillNum();
		//如果运单号为空就用物流单号给运单号去执行下面的查询查询
		if(waybillNum==null || waybillNum.equals("")){
			waybillNum=order.getLogisticOrderNum();
		}
		Map<String, String> map = new HashMap<>();
		//先从录单上获取信息
		map.put("ship_id",waybillNum);
		JSONObject jsonLudan = HttpUtil.doPost(SysConfig.LUDAN, JSONObject.fromObject(map));
		if(jsonLudan!=null && jsonLudan.getInt("errorCode")==0){
			List dataList = (List) jsonLudan.get("data");
			JSONObject data = (JSONObject) dataList.get(0);
			String docSrc = data.getString("doc_src");
			order.setSendName(RC4Util.decry_RC4(data.getString("snd_cust_nm"),SysConfig.LUDAN_KEY));
			order.setSendAddress(data.getString("snd_cust_addr"));
            order.setSendPhone(RC4Util.decry_RC4(data.getString("send_mob_tel"),SysConfig.LUDAN_KEY));
			order.setReceiverName(RC4Util.decry_RC4(data.getString("rcv_cust_nm"),SysConfig.LUDAN_KEY));
			order.setReceiverAddress(data.getString("rcv_cust_addr"));
			order.setReceiverPhone(RC4Util.decry_RC4(data.getString("recv_mob_tel"),SysConfig.LUDAN_KEY));
			order.setVipCode(data.getString("cust_id"));
			order.setBranchCode(data.getString("entry_comp"));
			String entryComp = data.getString("entry_comp");
			String custId = data.getString("cust_id");
			//不管是什么来源的，都去VIP系统里面取名称
			//根据录单表的cust_id长度判断，若cust_id>6位，则cust_id=BM+GS，若cust_id<=6位，则entry_comp+cust_id=BM+GS
			OrderDO customer=null;
			if(!custId.equals("") && custId.length()>6){
				customer = orderDao.getCustomerName(custId);
			}
			if(!custId.equals("") && custId.length()<=6){
				String res = entryComp+custId;
				customer = orderDao.getCustomerName(res);
			}
			if(customer!=null){
				order.setCustomerName(customer.getCustomerName());
				order.setCustomerCode(customer.getCustomerCode());
			}
			System.out.println("录单的信息===="+jsonLudan);
		}else {
			map.clear();
			String res = "";
			/*//用户画像和从菜鸟仓的运单信息
			String timestamp = System.currentTimeMillis() + "";
			String appId = SysConfig.YUNDAN_CAINIAO_APPID;
			String appKey = SysConfig.YUNDAN_CAINIAO_APPKEY;
			map.put("mailno", waybillNum);
			map.put("timestamp", timestamp);
			map.put("appId", appId);
			//签名算法说明：validate = md5(mailno+timestamp+appID+appKEY)
			String validate = md5(waybillNum + timestamp + appId + appKey);
			map.put("validate", validate);
			JSONObject jsonObject = HttpUtil.doPost(SysConfig.YUNDAN_CAINIAO, JSONObject.fromObject(map));
			Object result = jsonObject.get("result");
			if (result.equals(Boolean.TRUE)) {
				//有数据,将数据存放到数据库中
				JSONObject data = (JSONObject) jsonObject.get("data");
				order.setReceiverName((String) data.get("receiverName"));
				String receiverMobile = (String) data.get("receiverMobile");
				if (null != receiverMobile && !receiverMobile.equals("")) {
					order.setReceiverPhone(receiverMobile);
				} else {
					order.setReceiverPhone((String) data.get("receiverPhone"));
				}
				//目前只有接受人信息而且没有地址,后面再改
			} else {*/
				//二维码缓存中的运单信息(从redis缓存中获取,通过3des解密)
				res = stringRedisTemplate.opsForValue().get(waybillNum);
				if (res != null) {
					try {
						/*byte[] key = Base64Utils.decodeFromString(SysConfig.DES_KEYS);*/
						byte[] key = SysConfig.DES_KEYS.getBytes();
						byte[] iv = Base64Utils.decodeFromString(SysConfig.DES_IV);
						byte[] byRes = Base64Utils.decodeFromString(res);
//					JSONObject decrypt = DesEncrypt.decrypt(res, SysConfig.DES_KEYS, SysConfig.DES_IV);
						byte[] bytes = decrypt(byRes, key, iv);
						String decrypt = new String(bytes);
						Map<String, String> json = JSONValue.parse(decrypt, Map.class);
						order.setReceiverName(json.get("rn"));
						order.setReceiverPhone(json.get("rp"));
						order.setReceiverAddress(json.get("ra"));
						//二维码缓存中没有发件人姓名
						order.setSendPhone(json.get("sp"));
						order.setSendAddress(json.get("sa"));

					} catch (Exception e) {
						log.error(e.getMessage());
					}
				}/*else {
					return -1;        //即便没查到也存放
				}*/
			/*}*/
		}

//		return orderDao.save(order);
		return order;
	}

	public OrderTemplateDO getOrderTemplate(OrderTemplateDO order,String username,String name,String orgCode,String orgName){
		//资源来源默认工作台发起
		order.setConsultSource(SysConfig.CONSULT_SOURCE);
		//发起人账号
		order.setFaqiCode(ShiroUtils.getUser().getUsername());
		//生成14位咨询订单号(6位年月日+流水号)
		//String datePrefix = DateUtils.formatDate(new Date(),"yyMMdd");
		//流水号根据数据库中最大id值来获取下一个流水号
		//int maxId = orderDao.getMaxId()+1;
		//String maxIdString = String.valueOf(maxId);
		//高位补0
		/*if(maxIdString.length()<8){
			int num = 8-maxIdString.length();
			DecimalFormat df = new DecimalFormat(SysConfig.STR_FORMAT);
			String formatOrderId = df.format(maxId);
			String orderId = datePrefix+formatOrderId;
			order.setOrderId(orderId);
		}*/
		Date date =new Date();
		order.setOrderId(StringUtils.generateNumber(date, ""));//咨询单号  用时间+流水号生成

		order.setConsultTime(DateUtils.getCurrentTime());
		//刚创建状态为待申领
		order.setState("待申领");
		//存放发起人编码
		order.setFaqiCode(username);
		order.setFaqiName(name);
		order.setFaqiOrgCode(orgCode);
		order.setFaqiOrgName(orgName);
		//根据订单号查询运单信息
		//String logisticOrderNum = order.getLogisticOrderNum();
		String waybillNum = order.getWaybillNum();
		//如果运单号为空就用物流单号给运单号去执行下面的查询查询
		if(waybillNum==null || waybillNum.equals("")){
			waybillNum=order.getLogisticOrderNum();
		}
		Map<String, String> map = new HashMap<>();
		//先从录单上获取信息
		map.put("ship_id",waybillNum);
		JSONObject jsonLudan = HttpUtil.doPost(SysConfig.LUDAN, JSONObject.fromObject(map));
		if(jsonLudan!=null && jsonLudan.getInt("errorCode")==0){
			List dataList = (List) jsonLudan.get("data");
			JSONObject data = (JSONObject) dataList.get(0);
			String docSrc = data.getString("doc_src");
			order.setSendName(RC4Util.decry_RC4(data.getString("snd_cust_nm"),SysConfig.LUDAN_KEY));
			order.setSendAddress(data.getString("snd_cust_addr"));
			order.setSendPhone(RC4Util.decry_RC4(data.getString("send_mob_tel"),SysConfig.LUDAN_KEY));
			order.setReceiverName(RC4Util.decry_RC4(data.getString("rcv_cust_nm"),SysConfig.LUDAN_KEY));
			order.setReceiverAddress(data.getString("rcv_cust_addr"));
			order.setReceiverPhone(RC4Util.decry_RC4(data.getString("recv_mob_tel"),SysConfig.LUDAN_KEY));
			order.setVipCode(data.getString("cust_id"));
			order.setBranchCode(data.getString("entry_comp"));
			String entryComp = data.getString("entry_comp");
			String custId = data.getString("cust_id");
			//不管是什么来源的，都去VIP系统里面取名称
			//根据录单表的cust_id长度判断，若cust_id>6位，则cust_id=BM+GS，若cust_id<=6位，则entry_comp+cust_id=BM+GS
			OrderDO customer=null;
			if(!custId.equals("") && custId.length()>6){
				customer = orderDao.getCustomerName(custId);
			}
			if(!custId.equals("") && custId.length()<=6){
				String res = entryComp+custId;
				customer = orderDao.getCustomerName(res);
			}
			if(customer!=null){
				order.setCustomerName(customer.getCustomerName());
				order.setCustomerCode(customer.getCustomerCode());
			}
			System.out.println("录单的信息===="+jsonLudan);
		}else {
			map.clear();
			String res = "";
			/*//用户画像和从菜鸟仓的运单信息
			String timestamp = System.currentTimeMillis() + "";
			String appId = SysConfig.YUNDAN_CAINIAO_APPID;
			String appKey = SysConfig.YUNDAN_CAINIAO_APPKEY;
			map.put("mailno", waybillNum);
			map.put("timestamp", timestamp);
			map.put("appId", appId);
			//签名算法说明：validate = md5(mailno+timestamp+appID+appKEY)
			String validate = md5(waybillNum + timestamp + appId + appKey);
			map.put("validate", validate);
			JSONObject jsonObject = HttpUtil.doPost(SysConfig.YUNDAN_CAINIAO, JSONObject.fromObject(map));
			Object result = jsonObject.get("result");
			if (result.equals(Boolean.TRUE)) {
				//有数据,将数据存放到数据库中
				JSONObject data = (JSONObject) jsonObject.get("data");
				order.setReceiverName((String) data.get("receiverName"));
				String receiverMobile = (String) data.get("receiverMobile");
				if (null != receiverMobile && !receiverMobile.equals("")) {
					order.setReceiverPhone(receiverMobile);
				} else {
					order.setReceiverPhone((String) data.get("receiverPhone"));
				}
				//目前只有接受人信息而且没有地址,后面再改
			} else {*/
			//二维码缓存中的运单信息(从redis缓存中获取,通过3des解密)
			res = stringRedisTemplate.opsForValue().get(waybillNum);
			if (res != null) {
				try {
					/*byte[] key = Base64Utils.decodeFromString(SysConfig.DES_KEYS);*/
					byte[] key = SysConfig.DES_KEYS.getBytes();
					byte[] iv = Base64Utils.decodeFromString(SysConfig.DES_IV);
					byte[] byRes = Base64Utils.decodeFromString(res);
//					JSONObject decrypt = DesEncrypt.decrypt(res, SysConfig.DES_KEYS, SysConfig.DES_IV);
					byte[] bytes = decrypt(byRes, key, iv);
					String decrypt = new String(bytes);
					Map<String, String> json = JSONValue.parse(decrypt, Map.class);
					order.setReceiverName(json.get("rn"));
					order.setReceiverPhone(json.get("rp"));
					order.setReceiverAddress(json.get("ra"));
					//二维码缓存中没有发件人姓名
					order.setSendPhone(json.get("sp"));
					order.setSendAddress(json.get("sa"));

				} catch (Exception e) {
					log.error(e.getMessage());
				}
			}/*else {
					return -1;        //即便没查到也存放
				}*/
			/*}*/
		}

//		return orderDao.save(order);
		return order;
	}
	
	@Override
	@Transactional
	public int update(OrderDO order){
		return orderDao.update(order);
	}
	
	@Override
	public int remove(Integer id){
		return orderDao.remove(id);
	}
	
	@Override
	public int batchRemove(Integer[] ids){
		return orderDao.batchRemove(ids);
	}

	@Override
	public int shenLing(String[] orderIds, String userName, String name,String state) {
		return orderDao.shenLing(orderIds,userName,name,state);
	}

	@Override
	@Transactional
	public int updateByOrderIds(JieDanDO jieDanDO) {
		String orderIds = jieDanDO.getOrderIds();
		//判断是否勾选多个
		if(orderIds.contains(",")){
			//给拼接一个括号
			jieDanDO.setOrderIds("("+orderIds+")");
			return orderDao.updateByOrderIds(jieDanDO);
		}else {
			return orderDao.updateByOrderId(jieDanDO);
		}
	}

	@Override
	public OrderDO getByOrderId(String orderId) {
		return orderDao.getByOrderId(orderId);
	}

	@Override
	public int updateOrganizationNumByOrderIds(ZhuanFaDO zhuanFaDO) {
		String orderIds = zhuanFaDO.getOrderIds();
		//判断是否勾选多个
		if(orderIds.contains(",")){
			//给拼接一个括号
			zhuanFaDO.setOrderIds("("+orderIds+")");
			return orderDao.updateOrganizationNumByOrderIds(zhuanFaDO);
		}else {
			return orderDao.updateOrganizationNumByOrderId(zhuanFaDO);
		}
	}

	@Override
	public OrderDO checkOrder(OrderDO order) {
		return orderDao.checkOrder(order);
	}

	@Override
	public List<OperateDO> getListByOrderId(Map<String, Object> map) {
		return orderDao.getListByOrderId(map);
	}

	@Override
	public int countByOrderId(Map<String, Object> map) {
		return orderDao.countByOrderId(map);
	}

	@Override
	public List<OperateDO> getListByWaybillNum(Map<String, Object> map) {
		return orderDao.getListByWaybillNum(map);
	}

	@Override
	public int countByWaybillNum(Map<String, Object> map) {
		return orderDao.countByWaybillNum(map);
	}

	//=======================================================================
	private static final String MCRYPT_TRIPLEDES = "DESede";
	private static final String TRANSFORMATION = "DESede/CBC/PKCS5Padding";

	public static byte[] decrypt(byte[] data, byte[] key, byte[] iv) throws Exception {
		DESedeKeySpec spec = new DESedeKeySpec(key);
		SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(MCRYPT_TRIPLEDES);
		SecretKey sec = keyFactory.generateSecret(spec);
		Cipher cipher = Cipher.getInstance(TRANSFORMATION);
		IvParameterSpec IvParameters = new IvParameterSpec(iv);
		cipher.init(Cipher.DECRYPT_MODE, sec, IvParameters);
		return cipher.doFinal(data);
	}

	@Override
	public List getAllConsultype() {
		return orderDao.getAllConsultype();
	}

	@Override
	public List<String> getAllUserInfoByOrgCode(String orgCode) {
		return orderDao.getAllUserInfoByOrgCode(orgCode);
	}

	@Override
	public UserDO getUserByUserName(String dealCode) {
		return orderDao.getUserByUserName(dealCode);
	}

	@Override
	public int saveOperateInfo(OperateDO operateDO) {
		return orderDao.saveOperateInfo(operateDO);
	}

	@Override
	public int countOrder(String monthBegin,String monthEnd) {
		return orderDao.countOrder(monthBegin,monthEnd);
	}

	@Override
	public int countByState(String monthBegin, String monthEnd, String state) {
		return orderDao.countByState(monthBegin,monthEnd,state);
	}

	@Override
	public String getOrgCodeByUserName(String username) {
		return orderDao.getOrgCodeByUserName(username);
	}

    @Override
    public List<String> getUserNameListByOrgCode(String orgCode) {
        return orderDao.getUserNameListByOrgCode(orgCode);
    }

	@Override
	public String getRoleNameByUserName(String username) {
		return orderDao.getRoleNameByUserName(username);
	}

	@Override
	public int countByConsultType(String consultType) {
		return orderDao.countByConsultType(consultType);
	}

	


}
