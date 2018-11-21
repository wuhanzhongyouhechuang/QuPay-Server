package com.ntnikka.model;

import com.github.crab2died.annotation.ExcelField;

/**
 * @author luk
 * @create 2018-04-04 11:01
 * @desc 国民经济行业分类实体-2017 Junit使用
 **/
public class GbTempEntity {
    @ExcelField(title = "门类代码", order = 1)
    private String cid;

    @ExcelField(title = "门类名称", order = 2)
    private String name;

    @ExcelField(title = "大类代码", order = 3)
    private String bid;

    @ExcelField(title = "大类名称", order = 4)
    private String bname;

    @ExcelField(title = "中类代码", order = 3)
    private String mid;

    @ExcelField(title = "中类名称", order = 4)
    private String mname;

    @ExcelField(title = "小类代码", order = 3)
    private String lid;

    @ExcelField(title = "小类名称", order = 4)
    private String lname;

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBid() {
        return bid;
    }

    public void setBid(String bid) {
        this.bid = bid;
    }

    public String getBname() {
        return bname;
    }

    public void setBname(String bname) {
        this.bname = bname;
    }

    public String getMid() {
        return mid;
    }

    public void setMid(String mid) {
        this.mid = mid;
    }

    public String getMname() {
        return mname;
    }

    public void setMname(String mname) {
        this.mname = mname;
    }

    public String getLid() {
        return lid;
    }

    public void setLid(String lid) {
        this.lid = lid;
    }

    public String getLname() {
        return lname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }
}
