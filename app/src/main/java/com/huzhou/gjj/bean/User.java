package com.huzhou.gjj.bean;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

import java.io.Serializable;

/**
 * onCreated = "sql"：当第一次创建表需要插入数据时候在此写sql语句
 */
@Table(name = "user")
public class User implements Serializable {

    /**
     * phone :
     * accountId :
     * centerCode :
     * personAcctNo : 3305000001719
     * orgNo :
     * compAcctNo :
     * orgName :
     * loginType :
     * roleList : []
     * sealData :
     * unitName :
     * orgCode :
     * custCode :
     * certNo : 231004199308199221
     * custName :
     * pageRole :
     * unitCode :
     */
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    /**
     * name = "id"：数据库表中的一个字段
     * isId = true：是否是主键
     * autoGen = true：是否自动增长
     * property = "NOT NULL"：添加约束
     */
//    @Column(name = "id", isId = true, autoGen = true, property = "NOT NULL")
    @Column(name = "personAcctNo", isId = true)
    private String personAcctNo;
    @Column(name = "id")
    private int id;
    @Column(name = "phone")
    private String phone;
    @Column(name = "accountId")
    private String accountId;
    @Column(name = "centerCode")
    private String centerCode;
    @Column(name = "orgNo")
    private String orgNo;
    @Column(name = "compAcctNo")
    private String compAcctNo;
    @Column(name = "orgName")
    private String orgName;
    @Column(name = "loginType")
    private String loginType;
    @Column(name = "sealData")
    private String sealData;
    @Column(name = "unitName")
    private String unitName;
    @Column(name = "orgCode")
    private String orgCode;
    @Column(name = "custCode")
    private String custCode;
    @Column(name = "certNo")
    private String certNo;
    @Column(name = "custName")
    private String custName;
    @Column(name = "pageRole")
    private String pageRole;
    @Column(name = "unitCode")
    private String unitCode;

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getCenterCode() {
        return centerCode;
    }

    public void setCenterCode(String centerCode) {
        this.centerCode = centerCode;
    }

    public String getPersonAcctNo() {
        return personAcctNo;
    }

    public void setPersonAcctNo(String personAcctNo) {
        this.personAcctNo = personAcctNo;
    }

    public String getOrgNo() {
        return orgNo;
    }

    public void setOrgNo(String orgNo) {
        this.orgNo = orgNo;
    }

    public String getCompAcctNo() {
        return compAcctNo;
    }

    public void setCompAcctNo(String compAcctNo) {
        this.compAcctNo = compAcctNo;
    }

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public String getLoginType() {
        return loginType;
    }

    public void setLoginType(String loginType) {
        this.loginType = loginType;
    }

    public String getSealData() {
        return sealData;
    }

    public void setSealData(String sealData) {
        this.sealData = sealData;
    }

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

    public String getOrgCode() {
        return orgCode;
    }

    public void setOrgCode(String orgCode) {
        this.orgCode = orgCode;
    }

    public String getCustCode() {
        return custCode;
    }

    public void setCustCode(String custCode) {
        this.custCode = custCode;
    }

    public String getCertNo() {
        return certNo;
    }

    public void setCertNo(String certNo) {
        this.certNo = certNo;
    }

    public String getCustName() {
        return custName;
    }

    public void setCustName(String custName) {
        this.custName = custName;
    }

    public String getPageRole() {
        return pageRole;
    }

    public void setPageRole(String pageRole) {
        this.pageRole = pageRole;
    }

    public String getUnitCode() {
        return unitCode;
    }

    public void setUnitCode(String unitCode) {
        this.unitCode = unitCode;
    }


}
