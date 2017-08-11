package com.pingxun.app.controller;

import com.pingxun.app.util.HttpRequest;
import com.pingxun.app.util.JsoupClient;
import com.pingxun.app.util.UrlUtils;

import java.util.Date;
import java.util.List;
import java.util.Map;

public class Entrance {

    public static void main(String[] args){
        String result=HttpRequest.sendGet("http://android.kuchuan.com/commentstatis?packagename=com.pingxun.loanking.activity&market=360&start_date=&end_date=&longType=7-d&date="+new Date().getTime(),"");
        System.out.print(new Date().getTime()+":"+result);
    }
}
