package com.huzhou.gjj.bean;

import android.text.TextUtils;

import java.io.Serializable;

public class Detail implements Serializable {

    /**
     * 账户明细
     * jfny : 201609
     * jyrq : 20160906
     * grxm : 诸坚
     * zhlx : 01
     * fse : 2768.00
     * zhye : 108510.25
     * pzh : 2016000254266
     * GJHTQYWLX : 01
     * jdbj : 02
     * dwzh : 330501001527
     * grzh : 3305000001719
     * czrq : 20160906
     */

    private String jfny;
    private String jyrq;
    private String grxm;
    private String zhlx;
    private String fse;
    private String zhye;
    private String pzh;
    private String GJHTQYWLX;
    private String jdbj;
    private String dwzh;
    private String grzh;
    private String czrq;

    public String getJfny() {
        if (TextUtils.isEmpty(jfny))
            jfny = "无";
        if (jfny.length() == 6)
            return jfny.substring(0, 4) + "/" + jfny.substring(4, 6);
        return jfny;
    }

    public void setJfny(String jfny) {
        this.jfny = jfny;
    }

    public String getJyrq() {
        return jyrq;
    }

    public void setJyrq(String jyrq) {
        this.jyrq = jyrq;
    }

    public String getGrxm() {
        return grxm;
    }

    public void setGrxm(String grxm) {
        this.grxm = grxm;
    }

    public String getZhlx() {
        return zhlx;
    }

    public void setZhlx(String zhlx) {
        this.zhlx = zhlx;
    }

    public String getFse() {
        return fse;
    }

    public void setFse(String fse) {
        this.fse = fse;
    }

    public String getZhye() {
        return zhye;
    }

    public void setZhye(String zhye) {
        this.zhye = zhye;
    }

    public String getPzh() {
        return pzh;
    }

    public void setPzh(String pzh) {
        this.pzh = pzh;
    }

    public String getGJHTQYWLX() {
        return GJHTQYWLX;
    }

    public void setGJHTQYWLX(String GJHTQYWLX) {
        this.GJHTQYWLX = GJHTQYWLX;
    }

    public String getJdbj() {
        return jdbj;
    }

    public void setJdbj(String jdbj) {
        this.jdbj = jdbj;
    }

    public String getDwzh() {
        return dwzh;
    }

    public void setDwzh(String dwzh) {
        this.dwzh = dwzh;
    }

    public String getGrzh() {
        return grzh;
    }

    public void setGrzh(String grzh) {
        this.grzh = grzh;
    }

    public String getCzrq() {
        if (czrq.length() != 8) return czrq;
        return czrq.substring(0, 4) + "/" + czrq.substring(4, 6) + "/" + czrq.substring(6, 8);
    }

    public void setCzrq(String czrq) {
        this.czrq = czrq;
    }
}
