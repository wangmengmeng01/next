package com.yunda.base.api.service.impl;

import static com.yunda.base.bigcustomer.service.impl.OrderServiceImpl.decrypt;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Base64Utils;

import com.yunda.base.api.dao.OrderAppDao;
import com.yunda.base.api.domain.JieDanDTO;
import com.yunda.base.api.domain.OrderDTO;
import com.yunda.base.api.domain.RoleDTO;
import com.yunda.base.api.service.OrderAppService;
import com.yunda.base.bigcustomer.domain.OperateDO;
import com.yunda.base.bigcustomer.domain.OrderDO;
import com.yunda.base.bigcustomer.domain.OrganizationManageDO;
import com.yunda.base.bigcustomer.service.impl.OrderServiceImpl;
import com.yunda.base.common.utils.HttpUtil;
import com.yunda.base.system.config.SysConfig;
import com.yunda.base.system.dao.UserDao;
import com.yunda.base.system.domain.UserDO;
import com.yunda.ydmbspringbootstarter.common.utils.DateUtils;
import com.yunda.ydmbspringbootstarter.common.utils.RC4Util;
import com.yunda.ydmbspringbootstarter.common.utils.StringUtils;

import net.minidev.json.JSONValue;
import net.sf.json.JSONObject;

/**
 * @program: bigCustomer->OrderAppServiceImpl
 * @author: luzhiwei
 * @email: luzhiwei8794@yundasys.com
 * @create: 2019-07-22 13:33
 * @description: App咨询单服务
 */
@Service
public class OrderAppServiceImpl implements OrderAppService {
    Logger log = LoggerFactory.getLogger(OrderServiceImpl.class);

    @Resource
    private OrderAppDao orderAppDao;

    @Resource
    private UserDao userDao;

    @Autowired
    StringRedisTemplate stringRedisTemplate;

    @Override
    public OrderDO getOrderDetailByOrderId(String orderId) {
        return orderAppDao.getOrderDetailByOrderId(orderId);
    }

    @Override
    public OrderDO getOrderByOrderId(String orderId) {
        return orderAppDao.getOrderByOrderId(orderId);
    }

    @Override
    public List<String> getAllConsultType() {
        return orderAppDao.getAllConsultType();
    }

    @Override
    public List<OrderDO> list(OrderDTO orderDTO) {
        return orderAppDao.list(orderDTO);
    }

    @Override
    public int count(OrderDTO orderDTO) {
        return orderAppDao.count(orderDTO);
    }

    @Override
    public int shenLing(String orderId, String userName, String name, String state) {
        return orderAppDao.shenLing(orderId, userName, name, state);
    }

    @Override
    public String getRoleNameByUserName(String username) {
        return orderAppDao.getRoleNameByUserName(username);
    }

    @Override
    public List<String> getAllUserInfoByOrgCode(String orgCode, String name) {
        return orderAppDao.getAllUserInfoByOrgCode(orgCode, name);
    }

    @Override
    public UserDO getUserByUserName(String dealCode) {
        return orderAppDao.getUserByUserName(dealCode);
    }

    @Override
    public List<OperateDO> getListByOrderId(OrderDTO orderDTO) {
        return orderAppDao.getListByOrderId(orderDTO);
    }

    @Override
    public int countByOrderId(String orderId) {
        return orderAppDao.countByOrderId(orderId);
    }

    @Override
    public List<OrderDO> getListByWaybillNum(OrderDTO orderDTO) {
        return orderAppDao.getListByWaybillNum(orderDTO);
    }

    @Override
    public int countByWaybillNum(String waybillNum) {
        return orderAppDao.countByWaybillNum(waybillNum);
    }

    @Override
    public void updateOrderByDeal(JieDanDTO jieDanDTO) {
        orderAppDao.updateOrderByDeal(jieDanDTO);
    }

    @Override
    public List<Long> findOrderMenuByParam(Long userId, Long parentId, Long subId) {
        return orderAppDao.findOrderMenuByParam(userId, parentId, subId);
    }

    @Override
    public List<Long> findOrderMenuByUserIdAndParentId(Long userId, Long parentId) {
        return orderAppDao.findOrderMenuByUserIdAndParentId(userId, parentId);
    }

    @Override
    public RoleDTO getRoleByUserId(Long userId) {
        return orderAppDao.getRoleByUserId(userId);
    }

    @Override
    public List<OrganizationManageDO> findAllOrgInfo(String organizationNum) {
        return orderAppDao.findAllOrgInfo(organizationNum);
    }

    @Override
    public void zhuanFa(String orderId, String organizationNum, String state) {
        orderAppDao.zhuanFa(orderId, organizationNum, state);
    }

    @Override
    @Transactional
    public int save(OrderDO order, UserDO userDO) {
        //存放发起人的编码
        OrderDO orderDO = this.getOrder(order, userDO);
        int flag = orderAppDao.save(orderDO);
        //处理操作记录(后面用aop来处理)
        OperateDO operateDO = new OperateDO();
        operateDO.setOrderId(order.getOrderId());
        operateDO.setOperateName(userDO.getName());
        operateDO.setOperateCode(userDO.getUsername());
        operateDO.setType("发起咨询");
        //根据用户账号获取用户所属机构,最好存放机构编码
        String orgCodeByUserName = this.getOrgCodeByUserName(userDO.getUsername());
        operateDO.setOperateOrganization(orgCodeByUserName);
        operateDO.setTime(DateUtils.getCurrentTime());
        this.saveOperateInfo(operateDO);
        return flag;
    }

    @Override
    public OrderDO getOrder(OrderDO order, UserDO userDO){
        //资源来源默认APP应用发起
        order.setConsultSource(SysConfig.CONSULT_SOURCE);
        //发起人账号
        order.setFaqiCode(userDO.getUsername());
        order.setFaqiName(userDO.getName());
        order.setFaqiOrgCode(userDO.getOrgCode());
        order.setFaqiOrgName(userDO.getOrgName());
        //咨询单号  用时间+流水号生成
        order.setOrderId(StringUtils.generateNumber(new Date(), ""));
        order.setConsultTime(DateUtils.getCurrentTime());
        //刚创建状态为待申领
        order.setState("待申领");

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
                customer = orderAppDao.getCustomerName(custId);
            }
            if(!custId.equals("") && custId.length()<=6){
                String res = entryComp+custId;
                customer = orderAppDao.getCustomerName(res);
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
    public String getOrgCodeByUserName(String username) {
        return orderAppDao.getOrgCodeByUserName(username);
    }

    @Override
    public int saveOperateInfo(OperateDO operateDO) {
        return orderAppDao.saveOperateInfo(operateDO);
    }

    @Override
    public List<String> getUserNameListByOrgCode(String orgCode) {
        return orderAppDao.getUserNameListByOrgCode(orgCode);
    }

}
