package com.pingxun.app.util;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

public class JsoupClient {

    /**
     * 通过网页标签class属性获取数据
     * @param url
     * @param classes
     * @return
     */
    public static Map<String,Object> getDataFromNetWithClasses(String url,String[] classes){
        Map<String,Object> returnDada=null;
        try {
            Document doc = Jsoup.connect(url).get();
            if(ObjectHelper.isNotEmpty(doc)&&ObjectHelper.isNotEmpty(classes)){
                returnDada=new HashMap<String, Object>();
                //根据传进的class名称进行查找
                for(int i=0;i<classes.length;i++){
                    List<String> datas=new ArrayList<String>();
                    if(ObjectHelper.isNotEmpty(classes[i])){
                        Elements elements=doc.getElementsByClass(classes[i]);
                        if(ObjectHelper.isNotEmpty(elements)&&elements.size()>0){
                            for(int j=0;j<elements.size();j++){
                                Element element=elements.get(j);
                                datas.add(element.text());
                            }
                            returnDada.put(classes[i],datas);
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return returnDada;
    }

    /**
     * 通过网页标签id属性获取数据
     * @param url
     * @param classes
     * @return
     */
    public static Map<String,Object> getDataFromNetWithIds(String url,Map<String,Object> classes){
        Map<String,Object> returnData=null;
        return returnData;
    }
}
