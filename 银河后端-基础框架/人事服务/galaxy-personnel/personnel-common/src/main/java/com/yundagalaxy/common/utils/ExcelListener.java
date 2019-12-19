package com.yundagalaxy.common.utils;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.excel.metadata.ExcelHeadProperty;
import lombok.Data;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Data
public  class ExcelListener extends AnalysisEventListener {
    //自定义用于暂时存储data
    private List<Object> data = new ArrayList<>();
    //导入表头
    private String importHeads = "";
    //模版表头
    private String modelHeads = "";

    /**
     * 通过 AnalysisContext 对象还可以获取当前 sheet，当前行等数据
     */
    @Override
    public void invoke(Object o, AnalysisContext analysisContext) {
        Integer currentRowNum = analysisContext.getCurrentRowNum();
        //获取导入表头，默认第一行为表头
        if(currentRowNum == 0){
            try {
                Map<String,Object> m = objToMap(o);
                for (Object v : m.values()) {
                    importHeads += String.valueOf(v).trim() + ",";
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else{
            data.add(o);
        }
    }

    /**
     * 读取完之后的操作
     */
    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {
        //获取模版表头
        ExcelHeadProperty ehp = analysisContext.getExcelHeadProperty();
        for(List<String> s : ehp.getHead()){
            modelHeads += s.get(0) + ",";
        }
    }

    public List<Object> getData() {
        return data;
    }

    public void setData(List<Object> data) {
        this.data = data;
    }

    public String getImportHeads() {
        return importHeads;
    }

    public void setImportHeads(String importHeads) {
        this.importHeads = importHeads;
    }

    public String getModelHeads() {
        return modelHeads;
    }

    public void setModelHeads(String modelHeads) {
        this.modelHeads = modelHeads;
    }

    //Object转换为Map
    public  Map<String,Object> objToMap(Object obj) throws Exception{
        Map<String,Object> map = new LinkedHashMap<String, Object>();
        Field[] fields = obj.getClass().getDeclaredFields();
        for(Field field : fields){
            field.setAccessible(true);
            map.put(field.getName(), field.get(obj));
        }
        return map;
    }

}
