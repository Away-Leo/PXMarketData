package com.pingxun.app.util;

import java.sql.*;

public class DbHelper {

    public static final String url = "jdbc:mysql://192.168.1.100:3306/cwapp?useUnicode=true&characterEncoding=UTF-8";
//    public static final String url = "jdbc:mysql://119.23.64.92:3306/cwapp?useUnicode=true&characterEncoding=UTF-8";
    public static final String name = "com.mysql.jdbc.Driver";
    public static final String user = "root";
    public static final String password = "123456";
//    public static final String password = "pingxundata123!";

    public Connection conn = null;
    public Statement pst = null;

    public DbHelper(AppProperties appProperties) {
        try {
            String sql="update cw_app_market set download_num_yesterday=download_num,download_num="+appProperties.getDownnum()+",comment_num="+appProperties.getComment()+" where code='"+appProperties.getMarket()+"'";
            Class.forName(name);//指定连接类型
            conn = DriverManager.getConnection(url, user, password);//获取连接
            pst = conn.createStatement();//准备执行语句
            pst.execute(sql);
            close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void close() {
        try {
            this.conn.close();
            this.pst.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}