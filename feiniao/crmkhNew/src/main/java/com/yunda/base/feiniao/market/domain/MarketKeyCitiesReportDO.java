package com.yunda.base.feiniao.market.domain;

import com.github.crab2died.annotation.ExcelField;
import com.yunda.base.feiniao.market.bo.Bo_marketKeyCity;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Date;


/**
 * 市场占有率数据上报
 * 
 * @author yunda
 * @email zhanghan813@163.com
 * @date 2018-10-18132725
 */
public class MarketKeyCitiesReportDO implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//数据记录主键ID
	@ExcelField(title = "数据记录主键ID", order = 1)
	private Integer recordId;
	//大区
	@ExcelField(title = "大区", order = 2)
	private String bigarea;
	//省ID
	@ExcelField(title = "省ID", order = 3)
	private String provinceid;
	//省名称
	@ExcelField(title = "省名称", order = 4)
	private String provincename;
	//城市ID
	@ExcelField(title = "城市ID", order = 5)
	private String cityid;
	//城市名称
	@ExcelField(title = "城市名称", order = 6)
	private String cityname;
	//负责人
	@ExcelField(title = "负责人", order = 7)
	private String responsiblePeople;
	//所含重点城市
	@ExcelField(title = "所含重点城市", order = 8)
	private String containCity;
	//上报月份
	@ExcelField(title = "上报月份", order = 9)
	private Date reportDate;
	//上报状态
	@ExcelField(title = "上报状态", order = 10)
	private String reportStatus;
	//审核结果
	@ExcelField(title = "审核结果", order = 11)
	private String auditResult;
	//审核备注
	@ExcelField(title = "审核备注", order = 12)
	private String auditRemarks;
	//地区/公司
	@ExcelField(title = "地区/公司", order = 13)
	private String regionGs;
	//韵达占有率
	@ExcelField(title = "韵达占有率", order = 14)
	private Double proportionYd;
	//圆通占有率
	@ExcelField(title = "圆通占有率", order = 15)
	private Double proportionYt;
	//中通占有率
	@ExcelField(title = "中通占有率", order = 16)
	private Double proportionZt;
	//申通占有率
	@ExcelField(title = "申通占有率", order = 17)
	private Double proportionSt;
	//百世占有率
	@ExcelField(title = "百世占有率", order = 18)
	private Double proportionBs;
	//审核人
	@ExcelField(title = "审核人", order = 19)
	private String auditUser;
	//更新时间
	@ExcelField(title = "更新时间", order = 20)
	private Date updateTime;
	//本月得分
	@ExcelField(title = "本月得分", order = 21)
	private Integer monthScore;
	//上报年份
	@ExcelField(title = "上报年份", order = 22)
	private String reportNian;
	//上报月份
	@ExcelField(title = "上报月份", order = 23)
	private String reportYue;
	//省--韵达量
	@ExcelField(title = "省--韵达量", order = 24)
	private Double orderProvinceYdSum;
	//集团总量
	@ExcelField(title = "集团总量", order = 25)
	private Double orderJtzlYdSum;

 
	private double orderSum = 0;
	
	private double ydyzl = 0;
	private double kdhyzl = 0;
	
	private double orderAvg = 0;
	
	//基础信息
	private String basicalInformation;
	
	//各省韵达在全国占比
	private String AB;
	//各省快递总量在全国快递总量占比
	private String CD;
	//快递全省日均单量(单位：万件)
	private String kdqsrjdl;
	//快递全省月总量(单位：万件)
	private String kdqsyzl;
	
	//韵达全省月总量(单位：万件)
	private String ydqsyzl;
	//韵达全省日均量(单位：万件)
	private String ydqsrjl;
	//韵达全省市场份额占比
	private String ydqsscfezb;
	
	//中通全省月总量(单位：万件)
	private String ztqsyzl;
	//中通全省日均量(单位：万件)
	private String ztqsrjl;
	//中通全省市场份额占比
	private String ztqsscfezb;
	
	//圆通全省月总量(单位：万件)
	private String ytqsyzl;
	//圆通全省日均量(单位：万件)
	private String ytqsrjl;
	//圆通全省市场份额占比
	private String ytqsscfezb;
	
	//申通全省月总量(单位：万件)
	private String stqsyzl;
	//申通全省日均量(单位：万件)
	private String stqsrjl;
	//申通全省市场份额占比
	private String stqsscfezb;
	
	//百世全省月总量(单位：万件)
	private String bsqsyzl;
	//百世全省日均量(单位：万件)
	private String bsqsrjl;
	//百世全省市场份额占比
	private String bsqsscfezb;
	
	double zbMax =0;
	String zbName="";
	//1 大区，2省，3市
	private int colorFlag = 0;
	
	
	public int getColorFlag() {
		return colorFlag;
	}
	public void setColorFlag(int colorFlag) {
		this.colorFlag = colorFlag;
	}
	public double getZbMax() {
		return zbMax;
	}
	public void setZbMax(double zbMax) {
		this.zbMax = zbMax;
	}
	public String getZbName() {
		return zbName;
	}
	public void setZbName(String zbName) {
		this.zbName = zbName;
	}
	public double getOrderAvg() {
		return orderAvg;
	}
	public void setOrderAvg(double orderAvg) {
		this.orderAvg = orderAvg;
	}
	public String getAB() {
		return AB;
	}
	public void setAB(String aB) {
		AB = aB;
	}
	public String getCD() {
		return CD;
	}
	public void setCD(String cD) {
		CD = cD;
	}
	public String getKdqsrjdl() {
		return kdqsrjdl;
	}
	public void setKdqsrjdl(String kdqsrjdl) {
		this.kdqsrjdl = kdqsrjdl;
	}
	public String getKdqsyzl() {
		return kdqsyzl;
	}
	public void setKdqsyzl(String kdqsyzl) {
		this.kdqsyzl = kdqsyzl;
	}
	public String getYdqsyzl() {
		return ydqsyzl;
	}
	public void setYdqsyzl(String ydqsyzl) {
		this.ydqsyzl = ydqsyzl;
	}
	public String getYdqsrjl() {
		return ydqsrjl;
	}
	public void setYdqsrjl(String ydqsrjl) {
		this.ydqsrjl = ydqsrjl;
	}
	public String getYdqsscfezb() {
		return ydqsscfezb;
	}
	public void setYdqsscfezb(String ydqsscfezb) {
		this.ydqsscfezb = ydqsscfezb;
	}
	public String getZtqsyzl() {
		return ztqsyzl;
	}
	public void setZtqsyzl(String ztqsyzl) {
		this.ztqsyzl = ztqsyzl;
	}
	public String getZtqsrjl() {
		return ztqsrjl;
	}
	public void setZtqsrjl(String ztqsrjl) {
		this.ztqsrjl = ztqsrjl;
	}
	public String getZtqsscfezb() {
		return ztqsscfezb;
	}
	public void setZtqsscfezb(String ztqsscfezb) {
		this.ztqsscfezb = ztqsscfezb;
	}
	public String getYtqsyzl() {
		return ytqsyzl;
	}
	public void setYtqsyzl(String ytqsyzl) {
		this.ytqsyzl = ytqsyzl;
	}
	public String getYtqsrjl() {
		return ytqsrjl;
	}
	public void setYtqsrjl(String ytqsrjl) {
		this.ytqsrjl = ytqsrjl;
	}
	public String getYtqsscfezb() {
		return ytqsscfezb;
	}
	public void setYtqsscfezb(String ytqsscfezb) {
		this.ytqsscfezb = ytqsscfezb;
	}
	public String getStqsyzl() {
		return stqsyzl;
	}
	public void setStqsyzl(String stqsyzl) {
		this.stqsyzl = stqsyzl;
	}
	public String getStqsrjl() {
		return stqsrjl;
	}
	public void setStqsrjl(String stqsrjl) {
		this.stqsrjl = stqsrjl;
	}
	public String getStqsscfezb() {
		return stqsscfezb;
	}
	public void setStqsscfezb(String stqsscfezb) {
		this.stqsscfezb = stqsscfezb;
	}
	public String getBsqsyzl() {
		return bsqsyzl;
	}
	public void setBsqsyzl(String bsqsyzl) {
		this.bsqsyzl = bsqsyzl;
	}
	public String getBsqsrjl() {
		return bsqsrjl;
	}
	public void setBsqsrjl(String bsqsrjl) {
		this.bsqsrjl = bsqsrjl;
	}
	public String getBsqsscfezb() {
		return bsqsscfezb;
	}
	public void setBsqsscfezb(String bsqsscfezb) {
		this.bsqsscfezb = bsqsscfezb;
	}
	public String getBasicalInformation() {
		return basicalInformation;
	}
	public void setBasicalInformation(String basicalInformation) {
		this.basicalInformation = basicalInformation;
	}
	public double getYdyzl() {
		return ydyzl;
	}
	public void setYdyzl(double ydyzl) {
		this.ydyzl = ydyzl;
	}
	public double getKdhyzl() {
		return kdhyzl;
	}
	public void setKdhyzl(double kdhyzl) {
		this.kdhyzl = kdhyzl;
	}
	public double getOrderSum() {
		return orderSum;
	}
	public void setOrderSum(double orderSum) {
		this.orderSum = orderSum;
	}
	/**
	 * 设置：数据记录主键ID
	 */
	public void setRecordId(Integer recordId) {
		this.recordId = recordId;
	}
	/**
	 * 获取：数据记录主键ID
	 */
	public Integer getRecordId() {
		return recordId;
	}
	/**
	 * 设置：大区
	 */
	public void setBigarea(String bigarea) {
		this.bigarea = bigarea;
	}
	/**
	 * 获取：大区
	 */
	public String getBigarea() {
		return bigarea;
	}
	/**
	 * 设置：省ID
	 */
	public void setProvinceid(String provinceid) {
		this.provinceid = provinceid;
	}
	/**
	 * 获取：省ID
	 */
	public String getProvinceid() {
		return provinceid;
	}
	/**
	 * 设置：省名称
	 */
	public void setProvincename(String provincename) {
		this.provincename = provincename;
	}
	/**
	 * 获取：省名称
	 */
	public String getProvincename() {
		return provincename;
	}
	/**
	 * 设置：城市ID
	 */
	public void setCityid(String cityid) {
		this.cityid = cityid;
	}
	/**
	 * 获取：城市ID
	 */
	public String getCityid() {
		return cityid;
	}
	/**
	 * 设置：城市名称
	 */
	public void setCityname(String cityname) {
		this.cityname = cityname;
	}
	/**
	 * 获取：城市名称
	 */
	public String getCityname() {
		return cityname;
	}
	/**
	 * 设置：负责人
	 */
	public void setResponsiblePeople(String responsiblePeople) {
		this.responsiblePeople = responsiblePeople;
	}
	/**
	 * 获取：负责人
	 */
	public String getResponsiblePeople() {
		return responsiblePeople;
	}
	/**
	 * 设置：所含重点城市
	 */
	public void setContainCity(String containCity) {
		this.containCity = containCity;
	}
	/**
	 * 获取：所含重点城市
	 */
	public String getContainCity() {
		return containCity;
	}
	/**
	 * 设置：上报月份
	 */
	public void setReportDate(Date reportDate) {
		this.reportDate = reportDate;
	}
	/**
	 * 获取：上报月份
	 */
	public Date getReportDate() {
		return reportDate;
	}
	/**
	 * 设置：上报状态
	 */
	public void setReportStatus(String reportStatus) {
		this.reportStatus = reportStatus;
	}
	/**
	 * 获取：上报状态
	 */
	public String getReportStatus() {
		return reportStatus;
	}
	/**
	 * 设置：审核结果
	 */
	public void setAuditResult(String auditResult) {
		this.auditResult = auditResult;
	}
	/**
	 * 获取：审核结果
	 */
	public String getAuditResult() {
		return auditResult;
	}
	/**
	 * 设置：审核备注
	 */
	public void setAuditRemarks(String auditRemarks) {
		this.auditRemarks = auditRemarks;
	}
	/**
	 * 获取：审核备注
	 */
	public String getAuditRemarks() {
		return auditRemarks;
	}
	/**
	 * 设置：地区/公司
	 */
	public void setRegionGs(String regionGs) {
		this.regionGs = regionGs;
	}
	/**
	 * 获取：地区/公司
	 */
	public String getRegionGs() {
		return regionGs;
	}
	/**
	 * 设置：韵达占有率
	 */
	public void setProportionYd(Double proportionYd) {
		this.proportionYd = proportionYd;
	}
	/**
	 * 获取：韵达占有率
	 */
	public Double getProportionYd() {
		return proportionYd;
	}
	/**
	 * 设置：圆通占有率
	 */
	public void setProportionYt(Double proportionYt) {
		this.proportionYt = proportionYt;
	}
	/**
	 * 获取：圆通占有率
	 */
	public Double getProportionYt() {
		return proportionYt;
	}
	/**
	 * 设置：中通占有率
	 */
	public void setProportionZt(Double proportionZt) {
		this.proportionZt = proportionZt;
	}
	/**
	 * 获取：中通占有率
	 */
	public Double getProportionZt() {
		return proportionZt;
	}
	/**
	 * 设置：申通占有率
	 */
	public void setProportionSt(Double proportionSt) {
		this.proportionSt = proportionSt;
	}
	/**
	 * 获取：申通占有率
	 */
	public Double getProportionSt() {
		return proportionSt;
	}
	/**
	 * 设置：百世占有率
	 */
	public void setProportionBs(Double proportionBs) {
		this.proportionBs = proportionBs;
	}
	/**
	 * 获取：百世占有率
	 */
	public Double getProportionBs() {
		return proportionBs;
	}
	/**
	 * 设置：审核人
	 */
	public void setAuditUser(String auditUser) {
		this.auditUser = auditUser;
	}
	/**
	 * 获取：审核人
	 */
	public String getAuditUser() {
		return auditUser;
	}
	/**
	 * 设置：更新时间
	 */
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
	/**
	 * 获取：更新时间
	 */
	public Date getUpdateTime() {
		return updateTime;
	}
	/**
	 * 设置：本月得分
	 */
	public void setMonthScore(Integer monthScore) {
		this.monthScore = monthScore;
	}
	/**
	 * 获取：本月得分
	 */
	public Integer getMonthScore() {
		return monthScore;
	}
	/**
	 * 设置：上报年份
	 */
	public void setReportNian(String reportNian) {
		this.reportNian = reportNian;
	}
	/**
	 * 获取：上报年份
	 */
	public String getReportNian() {
		return reportNian;
	}
	/**
	 * 设置：上报月份
	 */
	public void setReportYue(String reportYue) {
		this.reportYue = reportYue;
	}
	/**
	 * 获取：上报月份
	 */
	public String getReportYue() {
		return reportYue;
	}
	/**
	 * 设置：省--韵达量
	 */
	public void setOrderProvinceYdSum(Double orderProvinceYdSum) {
		this.orderProvinceYdSum = orderProvinceYdSum;
	}
	/**
	 * 获取：省--韵达量
	 */
	public Double getOrderProvinceYdSum() {
		return orderProvinceYdSum;
	}
	/**
	 * 设置：集团总量
	 */
	public void setOrderJtzlYdSum(Double orderJtzlYdSum) {
		this.orderJtzlYdSum = orderJtzlYdSum;
	}
	/**
	 * 获取：集团总量
	 */
	public Double getOrderJtzlYdSum() {
		return orderJtzlYdSum;
	}
	
	public MarketKeyCitiesReportDO toDataHandle(Bo_marketKeyCity boMarketKeyCity) {
		MarketKeyCitiesReportDO marketKeyCityReportDO = new MarketKeyCitiesReportDO();
		DecimalFormat    df   = new DecimalFormat("######0.0"); 
		 NumberFormat nt = NumberFormat.getPercentInstance();
	   //设置百分数精确度2即保留两位小数
	   nt.setMinimumFractionDigits(1);
	   double ydyzlhj = boMarketKeyCity.getYdyzl();
	   double kdhyzlhj = boMarketKeyCity.getKdhyzl();
	   
	   
	    double skdhyzl = 0;
	    double skdhyrj = 0;
	    if(proportionYd != 0.0){
	        skdhyzl = orderSum/(proportionYd/100);
	        //全国快递行业总量
	        skdhyrj = orderAvg/(proportionYd/100);
	    }
	   
	    if(ydyzlhj != 0.0 && kdhyzlhj != 0.0){
	    	marketKeyCityReportDO.setResponsiblePeople(responsiblePeople);
	 	   marketKeyCityReportDO.setMonthScore(monthScore);
	    	AB = nt.format(orderSum/ydyzlhj);
		    marketKeyCityReportDO.setAB(AB);
		    CD = nt.format(skdhyzl/kdhyzlhj);
		    marketKeyCityReportDO.setCD(CD);
		    
		    kdqsrjdl = df.format(skdhyrj/10000);
		    marketKeyCityReportDO.setKdqsrjdl(kdqsrjdl);
		    kdqsyzl = df.format(skdhyzl/10000);
		    marketKeyCityReportDO.setKdqsyzl(kdqsyzl);
		    
		    ydqsyzl = df.format(orderSum/10000);
		    marketKeyCityReportDO.setYdqsyzl(ydqsyzl);
		    ydqsrjl = df.format(orderAvg/10000);
		    marketKeyCityReportDO.setYdqsrjl(ydqsrjl);
		    ydqsscfezb = nt.format(proportionYd/100);
		    marketKeyCityReportDO.setYdqsscfezb(ydqsscfezb);
    	    if(proportionYd>zbMax){
    	    	zbName = "ydqsscfezb";
    	    	zbMax = proportionYd;
    	    }
		    
		    ztqsyzl = df.format((skdhyzl*(proportionZt/100))/10000);
		    marketKeyCityReportDO.setZtqsyzl(ztqsyzl);
		    ztqsrjl = df.format((skdhyrj*(proportionZt/100))/10000);
		    marketKeyCityReportDO.setZtqsrjl(ztqsrjl);
		    ztqsscfezb = nt.format(proportionZt/100);
		    marketKeyCityReportDO.setZtqsscfezb(ztqsscfezb);
    	    if(proportionZt>zbMax){
    	    	zbName = "ztqsscfezb";
    	    	zbMax = proportionZt;
    	    }
		    
		    ytqsyzl = df.format((skdhyzl*(proportionYt/100))/10000);
		    marketKeyCityReportDO.setYtqsyzl(ytqsyzl);
		    
		    ytqsrjl = df.format((skdhyrj*(proportionYt/100))/10000);
		    marketKeyCityReportDO.setYtqsrjl(ytqsrjl);
		    
		    ytqsscfezb = nt.format(proportionYt/100);
		    marketKeyCityReportDO.setYtqsscfezb(ytqsscfezb);
    	    if(proportionYt>zbMax){
    	    	zbName = "ytqsscfezb";
    	    	zbMax = proportionYt;
    	    }
		    
		    stqsyzl = df.format((skdhyzl*(proportionSt/100))/10000);
		    marketKeyCityReportDO.setStqsyzl(stqsyzl);
		    
		    stqsrjl = df.format((skdhyrj*(proportionSt/100))/10000);
		    marketKeyCityReportDO.setStqsrjl(stqsrjl);
		    
		    stqsscfezb = nt.format(proportionSt/100);
		    marketKeyCityReportDO.setStqsscfezb(stqsscfezb);
    	    if(proportionSt>zbMax){
    	    	zbName = "stqsscfezb";
    	    	zbMax = proportionSt;
    	    }
		    
		    bsqsyzl = df.format((skdhyzl*(proportionBs/100))/10000);
		    marketKeyCityReportDO.setBsqsyzl(bsqsyzl);
		    
		    bsqsrjl = df.format((skdhyrj*(proportionBs/100))/10000);
		    marketKeyCityReportDO.setBsqsrjl(bsqsrjl);
		    
		    bsqsscfezb = nt.format(proportionBs/100);
		    marketKeyCityReportDO.setBsqsscfezb(bsqsscfezb);
    	    if(proportionBs>zbMax){
    	    	zbName = "bsqsscfezb";
    	    	zbMax = proportionBs;
    	    }
	    }
	    
	    
		return marketKeyCityReportDO;
    
		
	}
}
