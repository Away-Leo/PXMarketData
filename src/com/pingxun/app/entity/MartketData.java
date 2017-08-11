package com.pingxun.app.entity;

/**
 * 市场数据
 */
public class MartketData {

    public static final String TABLE_NAME="cw_market_data";

    /**
     * 数据ID
     */
    private int id;

    /**
     * 应用代码 用以区分产品
     */
    private String appCode;

    /**
     * 下载次数
     */
    private Long downLoadCount;

    /**
     * 评论数
     */
    private Long commentCount;

    /**
     * 写入数据时间
     */
    private Long writeDate;

    /**
     * 包名
     */
    private String packageName;

    /**
     * 市场名
     */
    private String marketCode;

    /**
     * 评论增长数
     */
    private Long commentCountIncrease;

    /**
     * 下载次数增长数
     */
    private Long downLoadCountIncrease;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAppCode() {
        return appCode;
    }

    public void setAppCode(String appCode) {
        this.appCode = appCode;
    }

    public Long getDownLoadCount() {
        return downLoadCount;
    }

    public void setDownLoadCount(Long downLoadCount) {
        this.downLoadCount = downLoadCount;
    }

    public Long getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(Long commentCount) {
        this.commentCount = commentCount;
    }

    public Long getWriteDate() {
        return writeDate;
    }

    public void setWriteDate(Long writeDate) {
        this.writeDate = writeDate;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getMarketCode() {
        return marketCode;
    }

    public void setMarketCode(String marketCode) {
        this.marketCode = marketCode;
    }

    public Long getCommentCountIncrease() {
        return commentCountIncrease;
    }

    public void setCommentCountIncrease(Long commentCountIncrease) {
        this.commentCountIncrease = commentCountIncrease;
    }

    public Long getDownLoadCountIncrease() {
        return downLoadCountIncrease;
    }

    public void setDownLoadCountIncrease(Long downLoadCountIncrease) {
        this.downLoadCountIncrease = downLoadCountIncrease;
    }
}
