package com.yunda.base.common.enums;

public enum RespEnum {
    /**
     * 请求成功
     */
    SUCCESS(200, "请求成功"),

    /**
     * 用户安全等级不足
     */
    NOTSAFE(490, "用户安全等级不足"),

    /**
     * 请求数据错误
     */
    ERROR_REQ_MISSPARAM(700, "确少请求参数错误"),
    /**
     * 请求数据错误
     */
    ERROR_REQ_PARAM(701, "请求参数解析错误"),
    /**
     * 请求头错误
     */
    ERROR_REQ_HEADER(702, "请求头错误"),
    /**
     * 请求过期
     */
    ERROR_REQ_EXPIRED(703, "请求过期"),
    /**
     * 请求方法错误
     */
    ERROR_REQ_METHOD(704, "请求方法错误"),
    /**
     * 请求参数valid错误
     */
    ERROR_REQ_BADPARAM(705, "请求参数valid错误"),
    /**
     * 请求方法错误
     */
    ERROR_REQ_CONTENTTYPE_UNSPPORT(706, "不支持当前媒体类型"),

    /**
     * 请求方法错误
     */
    ERROR_REQ_AGAIN(707, "业务正在处理中，请勿重复请求"),

    /**
     * 业务逻辑错误
     */
    ERROR_BUSINESS_OPERATE(800, "业务逻辑错误"),
    /**
     * 请先登录
     */
    ERROR_BUSINESS_NOT_LOGIN(801, "请先登录"),
    /**
     * 用户不存在
     */
    ERROR_BUSINESS_NO_USER(802, "用户不存在"),
    /**
     * 验证码错误!
     */
    ERROR_VERIFY_CODE(803, "验证码错误!"),
    /**
     * 验签错误!
     */
    ERROR_VERIFY_SIGN_PARAM(804, "验签错误，缺少签名参数!"),
    /**
     * 验签错误!
     */
    ERROR_VERIFY_SIGN_KEY(805, "验签错误，无法获取签名key!"),
    /**
     * 验签错误!
     */
    ERROR_VERIFY_SIGN_CHECK(806, "验签错误!"),

    /**
     * 用户名或密码错误
     */
    ERROR_LOGIN_WRONG(807, "用户名或密码错误"),
    /**
     * 用户名或密码错误
     */
    ERROR_LOGIN_FAIL(808, "登录失败"),
    /**
     * 已退出
     */
    ALREADY_EXIT(809, "因操作不当，您已被强制退出系统，请重新登录！"),

    /**
     * 验签错误!
     */
    REPORT_ING(850, "报表生成中"),

    /**
     * SQL错误!
     */
    ERROR_SQL(900, "SQL异常"),

    ERROR_EXPORT(901, "导出异常"),

    NO_DATA(902, "该时间段内没有数据,不生成Excel"),

    STANDING_BY(903, "导出任务已经加入导出队列中,导出结果请在[系统管理:文件导出队列]中稍后查看"),

    NO_CLICK(904, "数据还未生成,请不要重复点击"),

    FACE_FAIL(905, "面容匹配失败"),

    SMS_FAIL(906, "短信发送失败"), PHONE_FAIL(909, "手机号码非本人"), ERROR_NOT_EXITS_LOGIN_USER(509, "无基础数据"),

    SMS_VALID_FAIL(910, "短信验证码错误"),

    IDCARD_FAIL(911, "身份证错误"),

    MAC_FAIL(912, "mac地址错误"),

    STOP_RECORD(913, "停止录像"),

    KEEP_RECORD(914, "保持录像"),

    NO_NEED_MESSAGE(915, "不需要发送短信"),

    ORDER_DATA_NONEXIST(916, "近10天未查出同类型咨询单"),

    ORDER_DATA_EXCEED(920, "导入的数据行数超出限制"),
    ORDER_DATA_ERROR(921, "错误数据"),;

    private int code;
    private String message;

    RespEnum(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public static String getMessage(int code) {
        for (RespEnum p : RespEnum.values()) {
            if (p.getCode() == code) {
                return p.message;
            }
        }
        return "";
    }
}
