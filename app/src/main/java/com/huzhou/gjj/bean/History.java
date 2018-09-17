package com.huzhou.gjj.bean;

import java.io.Serializable;

public class History implements Serializable {

    /**
     * dkywmxlx : 02
     * dqqc : 89
     * yhrq : 20160820
     * fxje : 0.00
     * dkye : 194993.59
     * hkrq : 20160820
     * lxje : 531.95
     * bjje : 1443.63
     */

    private String dkywmxlx;
    private String dqqc;
    private String yhrq;
    private String fxje;
    private String dkye;
    private String hkrq;
    private String lxje;
    private String bjje;

    public String getDkywmxlx() {
        return dkywmxlx;
    }

    public void setDkywmxlx(String dkywmxlx) {
        this.dkywmxlx = dkywmxlx;
    }

    public String getDqqc() {
        return dqqc;
    }

    public void setDqqc(String dqqc) {
        this.dqqc = dqqc;
    }

    public String getYhrq() {
        if (yhrq.length() != 8) return yhrq;
        return yhrq.substring(0, 4) + "/" + yhrq.substring(4, 6) + "/" + yhrq.substring(6, 8);
    }

    public void setYhrq(String yhrq) {
        this.yhrq = yhrq;
    }

    public String getFxje() {
        return fxje;
    }

    public void setFxje(String fxje) {
        this.fxje = fxje;
    }

    public String getDkye() {
        return dkye;
    }

    public void setDkye(String dkye) {
        this.dkye = dkye;
    }

    public String getHkrq() {
        return hkrq;
    }

    public void setHkrq(String hkrq) {
        this.hkrq = hkrq;
    }

    public String getLxje() {
        return lxje;
    }

    public void setLxje(String lxje) {
        this.lxje = lxje;
    }

    public String getBjje() {
        return bjje;
    }

    public void setBjje(String bjje) {
        this.bjje = bjje;
    }
}
