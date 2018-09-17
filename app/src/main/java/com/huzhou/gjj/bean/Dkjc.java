package com.huzhou.gjj.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/4/25.
 */
public class Dkjc implements Serializable {

    /**
     * rclb : 01
     * dkxxpo : 贷款信息：连续逾期次数：0次; 累计逾期次数：0次 还款违约月份：无违约记录@缴存月份： 无@个人信息：工资基数：元 月缴存额：0元 单位账号： 单位名称： @购房信息：共有0套房@贷款记录情况：无
     * grxm : 时馨平
     * dkxx : 贷款信息：连续逾期次数：0次; 累计逾期次数：0次 还款违约月份：无违约记录@缴存月份： [20161201, 20161101, 20161001, 20170101, 20170201, 20170301]@个人信息：工资基数：20000元 月缴存额：3200元 单位账号：33050100000001 单位名称：绍兴市天池服饰有限公司 @购房信息：共有1套房@贷款记录情况：合同编号：2016011030451,贷款状态：已放款,姓名：时馨平,贷款金额：150000元,结清时间：无,房屋地址：第一批次1212浙江省-湖州市-吴兴区--小区-乡镇-街道-;
     * csrq : 19930410
     * zjlx : 01
     * zjhm : 321323199304108684
     * jcye : 11893.19
     */

    private String rclb;
    private String zjhm;
    private String zjlx;
    private String csrq;

    public String getGjjzh() {
        return gjjzh;
    }

    public void setGjjzh(String gjjzh) {
        this.gjjzh = gjjzh;
    }

    public String getLxjcxx() {
        return lxjcxx;
    }

    public void setLxjcxx(String lxjcxx) {
        this.lxjcxx = lxjcxx;
    }

    private String gjjzh;
    private String grxm;
    private String dkxx;
    private String dkxxpo;
    private String lxjcxx;
    private String jcye;

    public String getRclb() {
        return rclb;
    }

    public void setRclb(String rclb) {
        this.rclb = rclb;
    }

    public String getDkxxpo() {
        return dkxxpo;
    }

    public void setDkxxpo(String dkxxpo) {
        this.dkxxpo = dkxxpo;
    }

    public String getGrxm() {
        return grxm;
    }

    public void setGrxm(String grxm) {
        this.grxm = grxm;
    }

    public String getDkxx() {
        return dkxx;
    }

    public void setDkxx(String dkxx) {
        this.dkxx = dkxx;
    }

    public String getCsrq() {
        return csrq;
    }

    public void setCsrq(String csrq) {
        this.csrq = csrq;
    }

    public String getZjlx() {
        return zjlx;
    }

    public void setZjlx(String zjlx) {
        this.zjlx = zjlx;
    }

    public String getZjhm() {
        return zjhm;
    }

    public void setZjhm(String zjhm) {
        this.zjhm = zjhm;
    }

    public String getJcye() {
        return jcye;
    }

    public void setJcye(String jcye) {
        this.jcye = jcye;
    }
}
