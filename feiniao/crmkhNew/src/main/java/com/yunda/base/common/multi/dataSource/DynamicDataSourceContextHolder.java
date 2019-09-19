package com.yunda.base.common.multi.dataSource;

import java.util.ArrayList;
import java.util.List;

public class DynamicDataSourceContextHolder {

	private static final ThreadLocal<String> CONTEXT_HOLDER = new ThreadLocal<String>();

	public static  List<String> datasourceId =new ArrayList<String>();
	
    public static void setDateSoureType(String dateSoureType){
        CONTEXT_HOLDER.set(dateSoureType);
    }
    
    public static String getDateSoureType(){
       return CONTEXT_HOLDER.get();
    }
    
    public static void clearDateSoureType(){
        CONTEXT_HOLDER.remove();
    }
    
    public static boolean existDateSoure(String dateSoureType){
		return datasourceId.contains(dateSoureType);
    }
}
