package com.yunda.base.common.enums;

/**
 * @program: bigCustomer->DealStateEnum
 * @author: luzhiwei
 * @email: luzhiwei8794@yundasys.com
 * @create: 2019-07-29 10:06
 * @description: 字典枚举
 */
public enum DictEnum {

    STATE_TO_APPLY("state","0", "待申领"),
    STATE_TO_DEAL("state","1", "待处理"),
    STATE_TO_PROCESSING("state","2", "处理中"),
    STATE_TO_END("state","3", "已结单"),

    SHIXIAOSTATE_ALL("shiXiaoState","0", "全部"),
    SHIXIAOSTATE_OVERTIME("shiXiaoState","1", "超时"),
    SHIXIAOSTATE_WARNING("shiXiaoState","2", "预警"),
    SHIXIAOSTATE_NORMAL("shiXiaoState","3", "正常"),

    CONSULTYPE_CHECK("consultType","0", "订单核查"),
    CONSULTYPE_ATTITUDE("consultType","1", "服务态度"),
    CONSULTYPE_LOST("consultType","2", "丢件"),
    CONSULTYPE_DAMAGE("consultType","3", "破损"),
    CONSULTYPE_ABNORMAL_RECEVICE("consultType","4", "非正常签收"),
    CONSULTYPE_INTERCEPTION_RETURN("consultType","5", "拦截退回"),
    CONSULTYPE_REJECT("consultType","6", "拒收"),
    CONSULTYPE_URGENT_SEND("consultType","7", "催派件"),
    CONSULTYPE_CHANGE_ADDRESS_OR_INFORMATION("consultType","8", "改地址/改信息"),
    CONSULTYPE_CANCEL_ORDERS_OR_RETURN_WAREHOUSES("consultType","9", "取消订单/转退仓"),

    PRIORITY_GENERAL("friority","0", "一般"),
    PRIORITY_URGENT("friority","1", "紧急"),
    PRIORITY_LOW("friority","2", "低"),

    ORDER_MANAGE("menuId", "283", "咨询单管理"),
    ORDER_FOR_ME("menuId","284", "我的咨询单"),
    ORDER_TO_CREATE("menuId","285", "发起咨询单"),
    ORDER_POOL("menuId","329", "咨询单池"),
    ORDER_FOR_MYCREATE("menuId","341", "我发起的咨询单");


    private String tag;
    private String key;
    private String value;

    DictEnum(String tag, String key, String value) {
        this.tag = tag;
        this.key = key;
        this.value = value;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public static String getValueByKey(String tag, String key) {
        for (DictEnum p : DictEnum.values()) {
            if (p.getTag().equals(tag) && p.getKey().equals(key)) {
                return p.getValue();
            }
        }
        return "";
    }

    @Override
    public String toString() {
        return "DictEnum{" +
                "tag='" + tag + '\'' +
                ", key='" + key + '\'' +
                ", value='" + value + '\'' +
                '}';
    }
}
