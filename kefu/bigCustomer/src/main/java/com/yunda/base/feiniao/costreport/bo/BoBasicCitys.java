package com.yunda.base.feiniao.costreport.bo;


public class BoBasicCitys {


	public Object[] getKeys(){
		return new Object[]{ 
		id
		};
	}
    /** 
    * name -  
    */
    private String name;
    
    /** 
    * parent -  
    */
    private String parent;
    /** 
    * id -  
    */
    private String id;
    
    /** 
    * code -  
    */
    private String code;
   
    /** 
    * type -  
    */
    private String type;
    
    /** 
    * city_level -  
    */
    private String city_level;
    
    /** 
    * order -  
    */
    private int order;
    
    /** 
    * path -  
    */
    private String path;
    
	public String getCode(){
        return this.code;
    }
    public void setCode(String code){
        this.code = code;
    }
	public String getName(){
        return this.name;
    }
    public void setName(String name){
        this.name = name;
    }
	public String getId(){
        return this.id;
    }
    public void setId(String id){
        this.id = id;
    }

	public String getParent(){
        return this.parent;
    }
    public void setParent(String parent){
        this.parent = parent;
    }
	public int getOrder(){
        return this.order;
    }
    public void setOrder(int order){
        this.order = order;
    }
	public String getPath(){
        return this.path;
    }
    public void setPath(String path){
        this.path = path;
    }
	public String getType(){
        return this.type;
    }
    public void setType(String type){
        this.type = type;
    }
	public String getCity_level(){
        return this.city_level;
    }
    public void setCity_level(String city_level){
        this.city_level = city_level;
    }

//	public CRMKH_basic_citysPO toPO(){
//		CRMKH_basic_citysPO po = new CRMKH_basic_citysPO();
//		
//		po.setId(id);
//		po.setCode(code);
//		po.setName(name);
//		po.setParent(parent);
//		po.setOrder(order);
//		po.setPath(path);
//		po.setType(type);
//		po.setCity_level(city_level);
//		
//		return po;
//	}
}
