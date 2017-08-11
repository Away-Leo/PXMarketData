package com.pingxun.app.util;

import com.pingxun.app.entity.MartketData;

import java.util.List;
import java.util.Map;

/**
 * 数据分析工具类
 */

public class AnalysisUtil {

    /**
     * 应用宝
     */
    public static void analysiYingYongBao(){
        Map<String,Object> requestResult=JsoupClient.getDataFromNetWithClasses(UrlUtils.yingyongbao,new String[]{"info-value"});
        if(ObjectHelper.isNotEmpty(requestResult.get("info-value"))){
            List<String> datas=(List<String>) requestResult.get("info-value");
            MartketData martketData=new MartketData();
        }
    }
    /**
     * 百度
     */
    public static void analysiBaiDu(){

    }
    /**
     * 360
     */
    public static void analysi360(){

    }
    /**
     * 小米
     */
    public static void analysiXiaoMi(){

    }
    /**
     * 华为
     */
    public static void analysiHuaWei(){

    }
    /**
     * oppo
     */
    public static void analysiOppo(){

    }
    /**
     * 搜狗
     */
    public static void analysiSogo(){

    }
    /**
     * vivo
     */
    public static void analysiVivo(){

    }
    /**
     * 木蚂蚁
     */
    public static void analysiMumayi(){

    }
    /**
     * 乐视
     */
    public static void analysiLeshi(){

    }
    /**
     * 安智市场
     */
    public static void analysiAnzhi(){

    }
}
