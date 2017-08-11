package com.pingxun.app.util;

import com.pingxun.app.entity.TableInfo;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DataBaseHelper {

    public static final String url = "jdbc:mysql://192.168.1.100:3306/cwapp?useUnicode=true&characterEncoding=UTF-8";
    //    public static final String url = "jdbc:mysql://119.23.64.92:3306/cwapp?useUnicode=true&characterEncoding=UTF-8";
    public static final String name = "com.mysql.jdbc.Driver";
    public static final String user = "root";
    public static final String password = "123456";
//    public static final String password = "pingxundata123!";

    // 此方法为获取数据库连接
    public static Connection getConnection() {
        Connection conn = null;
        try {
            // 加载数据库驱动
            Class.forName(name);
            if (null == conn) {
                conn = DriverManager.getConnection(url, user, password);
            }
        } catch (ClassNotFoundException e) {
            System.out.println("Sorry,can't find the Driver!");
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return conn;
    }

    /**
     * 增删改【Add、Del、Update】
     *
     * @param sql
     * @return int
     */
    public static int executeNonQuery(String sql) {
        int result = 0;
        Connection conn = null;
        Statement stmt = null;

        try {
            conn = getConnection();
            stmt = conn.createStatement();
            result = stmt.executeUpdate(sql);
        } catch (SQLException err) {
            err.printStackTrace();
            free(null, stmt, conn);
        } finally {
            free(null, stmt, conn);
        }

        return result;
    }

    /**
     * 增删改【Add、Delete、Update】
     *
     * @param sql
     * @param obj
     * @return int
     */
    public static int executeNonQuery(String sql, Object... obj) {
        int result = 0;
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = getConnection();
            pstmt = conn.prepareStatement(sql);

            for (int i = 0; i < obj.length; i++) {
                pstmt.setObject(i + 1, obj[i]);
            }

            result = pstmt.executeUpdate();
        } catch (SQLException err) {
            err.printStackTrace();
            free(null, pstmt, conn);
        } finally {
            free(null, pstmt, conn);
        }
        return result;
    }

    /**
     * 查【Query】
     *
     * @param sql
     * @return ResultSet
     */
    public static ResultSet executeQuery(String sql) {
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;

        try {
            conn = getConnection();
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);
        } catch (SQLException err) {
            err.printStackTrace();
            free(rs, stmt, conn);
        }

        return rs;
    }

    /**
     * 查【Query】
     *
     * @param sql
     * @param obj
     * @return ResultSet
     */
    public static ResultSet executeQuery(String sql, Object... obj) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = getConnection();
            pstmt = conn.prepareStatement(sql);

            for (int i = 0; i < obj.length; i++) {
                pstmt.setObject(i + 1, obj[i]);
            }

            rs = pstmt.executeQuery();
        } catch (SQLException err) {
            err.printStackTrace();
            free(rs, pstmt, conn);
        }

        return rs;
    }

    /**
     * 判断记录是否存在
     *
     * @param sql
     * @return Boolean
     */
    public static Boolean isExist(String sql) {
        ResultSet rs = null;

        try {
            rs = executeQuery(sql);
            rs.last();
            int count = rs.getRow();
            if (count > 0) {
                return true;
            } else {
                return false;
            }
        } catch (SQLException err) {
            err.printStackTrace();
            free(rs);
            return false;
        } finally {
            free(rs);
        }
    }

    /**
     * 判断记录是否存在
     *
     * @param sql
     * @return Boolean
     */
    public static Boolean isExist(String sql, Object... obj) {
        ResultSet rs = null;

        try {
            rs = executeQuery(sql, obj);
            rs.last();
            int count = rs.getRow();
            if (count > 0) {
                return true;
            } else {
                return false;
            }
        } catch (SQLException err) {
            err.printStackTrace();
            free(rs);
            return false;
        } finally {
            free(rs);
        }
    }

    /**
     * 获取查询记录的总行数
     *
     * @param sql
     * @return int
     */
    public static int getCount(String sql) {
        int result = 0;
        ResultSet rs = null;

        try {
            rs = executeQuery(sql);
            rs.last();
            result = rs.getRow();
        } catch (SQLException err) {
            free(rs);
            err.printStackTrace();
        } finally {
            free(rs);
        }

        return result;
    }

    /**
     * 获取查询记录的总行数
     *
     * @param sql
     * @param obj
     * @return int
     */
    public static int getCount(String sql, Object... obj) {
        int result = 0;
        ResultSet rs = null;

        try {
            rs = executeQuery(sql, obj);
            rs.last();
            result = rs.getRow();
        } catch (SQLException err) {
            err.printStackTrace();
        } finally {
            free(rs);
        }

        return result;
    }

    /**
     * 释放【ResultSet】资源
     *
     * @param rs
     */
    public static void free(ResultSet rs) {
        try {
            if (rs != null) {
                rs.close();
            }
        } catch (SQLException err) {
            err.printStackTrace();
        }
    }

    /**
     * 释放【Statement】资源
     *
     * @param st
     */
    public static void free(Statement st) {
        try {
            if (st != null) {
                st.close();
            }
        } catch (SQLException err) {
            err.printStackTrace();
        }
    }

    /**
     * 释放【Connection】资源
     *
     * @param conn
     */
    public static void free(Connection conn) {
        try {
            if (conn != null) {
                conn.close();
            }
        } catch (SQLException err) {
            err.printStackTrace();
        }
    }

    /**
     * 释放所有数据资源
     *
     * @param rs
     * @param st
     * @param conn
     */
    public static void free(ResultSet rs, Statement st, Connection conn) {
        free(rs);
        free(st);
        free(conn);
    }

    /**
     * 判断表是否存在
     * @param tableName
     * @return
     */
    public static boolean isTableExist(String tableName){
        String sql="SELECT table_name FROM information_schema.TABLES WHERE table_name ='"+tableName.trim().toLowerCase()+"'";
        return isExist(sql);
    }

    /**
     * 得到表信息
     * @param tableName
     * @return
     */
    public static List<TableInfo> getTableInfo(String tableName){
        String sql="SELECT column_name as \"columnName\",column_comment as \"columnComment\",data_type as \"dataType\" FROM information_schema.columns WHERE table_name='"+tableName+"'";
        ResultSet resultSet=executeQuery(sql);
        return packageDataToEntity(resultSet,TableInfo.class);
    }

    /**
     * 根据实体返回实体集合
     * @param sql
     * @param tClass
     * @param <T>
     * @return
     */
    public static <T>List<T> getDataList(String sql,Class<T> tClass){
        return packageDataToEntity(executeQuery(sql),tClass);
    }

    /**
     * 封装数据为泛型集合
     * @param resultSet
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T>List<T>  packageDataToEntity(ResultSet resultSet,Class<T> clazz){
        List<T> returnData=null;
        if(ObjectHelper.isNotEmpty(resultSet)){
            returnData=new ArrayList<>();
            ResultSetMetaData md = null;//获取键名
            try {
                md = resultSet.getMetaData();
                int columnCount = md.getColumnCount();//获取行的数量
                while (resultSet.next()) {
                    Object object=clazz.newInstance();
                    object=writeFields(object,resultSet);
                    returnData.add((T)object);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return returnData;
    }

    /**
     * 将返回集反射为对象
     * @param model
     * @param resultSet
     * @return
     */
    public static Object writeFields(Object model,ResultSet resultSet){
        Field[] field = model.getClass().getDeclaredFields(); // 获取实体类的所有属性，返回Field数组
        try {
            for (int j = 0; j < field.length; j++) { // 遍历所有属性
                String name = field[j].getName(); // 获取属性的名字
                name = name.substring(0, 1).toUpperCase() + name.substring(1); // 将属性的首字符大写，方便构造get，set方法
                String type = field[j].getGenericType().toString(); // 获取属性的类型
                if (type.equals("class java.lang.String")) { // 如果type是类类型，则前面包含"class "，后面跟类名
                    Method m = model.getClass().getMethod("get" + name);
                    String value = (String) m.invoke(model); // 调用getter方法获取属性值
                    if (value == null) {
                        String strResult=resultSet.getString(name);
                        m = model.getClass().getMethod("set"+name,String.class);
                        m.invoke(model, strResult);
                    }
                }
                if (type.equals("class java.lang.Integer")) {
                    Method m = model.getClass().getMethod("get" + name);
                    Integer value = (Integer) m.invoke(model);
                    if (value == null) {
                        Integer intResult=resultSet.getInt(name);
                        m = model.getClass().getMethod("set"+name,Integer.class);
                        m.invoke(model, intResult);
                    }
                }
                if (type.equals("class java.lang.Boolean")) {
                    Method m = model.getClass().getMethod("get" + name);
                    Boolean value = (Boolean) m.invoke(model);
                    if (value == null) {
                        Boolean booResult=resultSet.getBoolean(name);
                        m = model.getClass().getMethod("set"+name,Boolean.class);
                        m.invoke(model, booResult);
                    }
                }
                if (type.equals("class java.util.Date")) {
                    Method m = model.getClass().getMethod("get" + name);
                    Date value = (Date) m.invoke(model);
                    if (value == null) {
                        java.util.Date dateResult=resultSet.getDate(name);
                        m = model.getClass().getMethod("set"+name,Date.class);
                        m.invoke(model,dateResult);
                    }
                }
                if (type.equals("class java.lang.Long")) {
                    Method m = model.getClass().getMethod("get" + name);
                    Long value = (Long) m.invoke(model);
                    if (value == null) {
                        Long longResult= resultSet.getLong(name);
                        m = model.getClass().getMethod("set"+name,Date.class);
                        m.invoke(model,longResult);
                    }
                }
            }
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return model;
    }


}
