package com.pingxun.app.util;

import org.htmlparser.Node;
import org.htmlparser.Parser;
import org.htmlparser.filters.AndFilter;
import org.htmlparser.filters.HasAttributeFilter;
import org.htmlparser.filters.TagNameFilter;
import org.htmlparser.util.NodeList;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HttpRequest {
    /**
     * 向指定URL发送GET方法的请求
     *
     * @param url
     *            发送请求的URL
     * @param param
     *            请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
     * @return URL 所代表远程资源的响应结果
     */
    public static String sendGet(String url, String param) {
        String result = "";
        BufferedReader in = null;
        try {
            String urlNameString = url + "?" + param;
            URL realUrl = new URL(urlNameString);
            // 打开和URL之间的连接
            URLConnection connection = realUrl.openConnection();
            // 设置通用的请求属性
            connection.setRequestProperty("accept", "*/*");
            connection.setRequestProperty("connection", "Keep-Alive");
            connection.setRequestProperty("user-agent",
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            // 建立实际的连接
            connection.connect();
            // 获取所有响应头字段
            Map<String, List<String>> map = connection.getHeaderFields();
            // 遍历所有的响应头字段
            for (String key : map.keySet()) {
                System.out.println(key + "--->" + map.get(key));
            }
            // 定义 BufferedReader输入流来读取URL的响应
            in = new BufferedReader(new InputStreamReader(
                    connection.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            System.out.println("发送GET请求出现异常！" + e);
            e.printStackTrace();
        }
        // 使用finally块来关闭输入流
        finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        return result;
    }

    /**
     * 向指定 URL 发送POST方法的请求
     *
     * @param url
     *            发送请求的 URL
     * @param param
     *            请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
     * @return 所代表远程资源的响应结果
     */
    public static String sendPost(String url, String param) {
        PrintWriter out = null;
        BufferedReader in = null;
        String result = "";
        try {
            URL realUrl = new URL(url);
            // 打开和URL之间的连接
            URLConnection conn = realUrl.openConnection();
            // 设置通用的请求属性
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("user-agent",
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            // 发送POST请求必须设置如下两行
            conn.setDoOutput(true);
            conn.setDoInput(true);
            // 获取URLConnection对象对应的输出流
            out = new PrintWriter(conn.getOutputStream());
            // 发送请求参数
            out.print(param);
            // flush输出流的缓冲
            out.flush();
            // 定义BufferedReader输入流来读取URL的响应
            in = new BufferedReader(
                    new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            System.out.println("发送 POST 请求出现异常！"+e);
            e.printStackTrace();
        }
        //使用finally块来关闭输出流、输入流
        finally{
            try{
                if(out!=null){
                    out.close();
                }
                if(in!=null){
                    in.close();
                }
            }
            catch(IOException ex){
                ex.printStackTrace();
            }
        }
        return result;
    }

//    public static void main(String[] args)
//    {
//        //解析应用宝
//        httpPostData(UrlUtils.yingyongbao,"yingyongbao","row info text-center");
//        //解析华为
//        httpPostData(UrlUtils.huawei,"huawei","app-info-ul nofloat");
//        //解析vivo
//        httpPostData(UrlUtils.vivo,"vivo","item-introduce-download");
//        //木蚂蚁
//        httpGetData(UrlUtils.mumayi,"mumayi","downnumarea");
//        //搜狗
//        httpPostData(UrlUtils.sogo,"sogo","count");
//        //乐视
//        httpGetData(UrlUtils.leshi,"leshi","statics");
//        //安智市场
//        httpGetData(UrlUtils.anzhi,"anzhi","detail_line_ul");
//
//    }


    public static void httpGetData(String url,String market,String className)
    {
        try {
            AppProperties appProperties = new AppProperties();
            String result = HttpRequest.sendGet(url, "id=22081025&sid=1187550");
            //返回网页内容
            System.out.println("网页请求返回内容：" + result);
            if("leshi".equals(market))
            {
                //下载量
                JSONObject jsonObject = new JSONObject(result);
                JSONObject resultJSON = new JSONObject(jsonObject.getString("entity"));
                appProperties.setDownnum(resultJSON.getInt("downloadcount"));
                appProperties.setComment(resultJSON.getInt("commentcount"));

            }else if("mumayi".equals(market))
            {
                //下载量
                JSONObject jsonObject = new JSONObject(result.substring(1,result.length()-1));
                System.out.println("==="+jsonObject.getString("downNum"));
                appProperties.setDownnum(Integer.parseInt(jsonObject.getString("downNum")));
                appProperties.setComment(0);
            }else {
                Parser parser = new Parser(result);
                parser.setEncoding("utf-8");
                HasAttributeFilter filter =
                        new HasAttributeFilter("class", className);
                NodeList nodes = parser.parse(filter);
                appProperties = parseDiv(result, market, nodes.toHtml());
            }
            //解析内容数据
            appProperties.setMarket(market);

            new DbHelper(appProperties);

        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }


    public static void httpPostData(String url,String market,String className)
    {
        try {
            String result = HttpRequest.sendPost(url, "u=1543867507&&p=YX18623185183");
            //返回网页内容
            System.out.println("网页请求返回内容：" + result);
            Parser parser = new Parser(result);
            parser.setEncoding("utf-8");
            HasAttributeFilter filter =new HasAttributeFilter("class",className);
            NodeList nodes = parser.parse(filter);
            AppProperties appProperties = parseDiv(result,market,nodes.toHtml());
            appProperties.setMarket(market);
            //解析内容数据
            new DbHelper(appProperties);

        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    /**
     * 解析网页
     * @param html
     * @return
     */
    private static AppProperties parseDiv(String result,String market,String html)
    {
        AppProperties appProperties = new AppProperties();
        try {
            Parser parsernew = new Parser(html);
            parsernew.setEncoding("utf-8");
            //应用宝
            if("yingyongbao".equals(market)) {
                TagNameFilter filterNode = new TagNameFilter("div");
                NodeList nodees = parsernew.parse(filterNode);
                NodeList nodeList = nodees.elementAt(0).getChildren();
                for (int i = 0; i < nodeList.size(); i++) {
                    String childrenNode = nodeList.elementAt(i).toHtml();
                    if (!"".equals(replaceBlank(childrenNode))) {
                        parsernew = new Parser(childrenNode);
                        AndFilter andFilter = new AndFilter(new TagNameFilter("div"), new HasAttributeFilter("class", "info-value"));
                        nodees = parsernew.parse(andFilter);
                        if (!"".equals(replaceBlank(nodees.toHtml()))) {
                            String packageName = nodees.elementAt(0).getChildren().toHtml();
                            if (packageName.indexOf("com") != -1 && packageName.indexOf("http://") == -1) {
                                appProperties.setPackageName(packageName);
                            } else if (packageName.indexOf("总下载量") != -1) {
                                String sss = nodees.elementAt(0).getChildren().elementAt(0).getChildren().toHtml();
                                appProperties.setDownnum(Integer.parseInt(sss.replaceAll(",", "")));
                            } else if (packageName.indexOf("http://") == -1) {
                                appProperties.setCommpany(packageName);
                            }
                        } else {
                            parsernew = new Parser(childrenNode);
                            andFilter = new AndFilter(new TagNameFilter("div"), new HasAttributeFilter("class", "info-name"));
                            nodees = parsernew.parse(andFilter);
                            String commentNum = nodees.elementAt(0).getChildren().toHtml();
                            commentNum = commentNum.replaceAll("评论数", "");
                            appProperties.setComment(Integer.parseInt(commentNum.substring(1, commentNum.length() - 1)));
                        }
                    }
                }
            }
            if("huawei".equals(market)) {
                //下载量
                AndFilter andFilter = new AndFilter(new TagNameFilter("span"), new HasAttributeFilter("class", "grey sub"));
                NodeList nodeList = parsernew.parse(andFilter);
                if (!"".equals(replaceBlank(nodeList.toHtml()))) {
                    String downloadNum = nodeList.elementAt(0).getChildren().toHtml();
                    downloadNum = downloadNum.replaceAll("下载：","").replaceAll("次","");
                    System.out.println(downloadNum);
                    appProperties.setDownnum(Integer.parseInt(downloadNum));

                }
                //评论
                parsernew = new Parser(result);
                andFilter = new AndFilter(new TagNameFilter("h4"), new HasAttributeFilter("class", "sub nofloat"));
                nodeList = parsernew.parse(andFilter);
                if (!"".equals(replaceBlank(nodeList.toHtml()))) {
                    String downloadNum = nodeList.elementAt(0).getChildren().asString();
                    downloadNum = downloadNum.substring(downloadNum.indexOf("（")+1);
                    downloadNum = downloadNum.substring(0,downloadNum.indexOf("）")).replace("条","");
                    appProperties.setComment(Integer.parseInt(downloadNum));

                }
            }

            if("vivo".equals(market)) {
                //下载量
                parsernew = new Parser(result);
                AndFilter andFilter = new AndFilter(new TagNameFilter("dd"), new HasAttributeFilter("class", "item-introduce-download"));
                NodeList  nodeList = parsernew.parse(andFilter);
                String downloadNum = nodeList.toHtml();
                downloadNum = downloadNum.substring(downloadNum.indexOf("&nbsp;&nbsp;")+12);
                downloadNum = downloadNum.substring(downloadNum.indexOf("&nbsp;&nbsp;")+12);
                downloadNum = downloadNum.replaceAll("次下载</dd>","").trim();
                appProperties.setDownnum(Integer.parseInt(downloadNum));
                appProperties.setComment(0);
            }

            if("mumayi".equals(market)) {
                //下载量
                parsernew = new Parser(result);
                AndFilter andFilter = new AndFilter(new TagNameFilter("span"), new HasAttributeFilter("class", "downnumarea"));
                NodeList  nodeList = parsernew.parse(andFilter);
                String downloadNum = nodeList.toHtml();

                System.out.println("downloadNum = "+downloadNum);
            }


            if("sogo".equals(market)) {
                //下载量
                parsernew = new Parser(html);
                HasAttributeFilter filter =
                        new HasAttributeFilter("class","count");
                NodeList nodeList = parsernew.parse(filter);
                String downloadNum = nodeList.elementAt(0).toHtml();
                downloadNum = downloadNum.substring(0,downloadNum.indexOf("次下载"));
                downloadNum = downloadNum.substring(downloadNum.indexOf("<span>")+6);
                if(downloadNum.indexOf("万")!=-1)
                {
                    downloadNum = downloadNum.replaceAll("万","");
                    appProperties.setDownnum(Integer.parseInt(downloadNum)*10000);
                }else{
                    appProperties.setDownnum(Integer.parseInt(downloadNum));
                }
                appProperties.setComment(0);
                System.out.println("downloadNum = "+downloadNum);
            }

            if("anzhi".equals(market)) {
                //下载量
                parsernew = new Parser(result);
                AndFilter andFilter = new AndFilter(new TagNameFilter("ul"), new HasAttributeFilter("id", "detail_line_ul"));
                NodeList nodeList = parsernew.parse(andFilter);
                String downloadNum = nodeList.elementAt(0).toHtml();

                parsernew = new Parser(downloadNum);
                andFilter = new AndFilter(new TagNameFilter("span"), new HasAttributeFilter("class", "spaceleft"));
                nodeList = parsernew.parse(andFilter);
                downloadNum = nodeList.elementAt(0).toHtml();

                downloadNum = downloadNum.substring(downloadNum.indexOf("下载：")+3);
                downloadNum = downloadNum.substring(0,downloadNum.indexOf("+"));
                appProperties.setDownnum(Integer.parseInt(downloadNum));
                appProperties.setComment(0);
                System.out.println("downloadNum = "+downloadNum);
            }
        }catch (Exception e)
        {
            e.printStackTrace();
        }

        return appProperties;
    }
    public static String replaceBlank(String str) {
        String dest = "";
        if (str!=null) {
            Pattern p = Pattern.compile("\\s*|\t|\r|\n");
            Matcher m = p.matcher(str);
            dest = m.replaceAll("");
        }
        return dest;
    }
}