package com.huzhou.gjj.bean;

import android.text.TextUtils;

import java.io.Serializable;

public class Change implements Serializable {
    /**
     * jym : 0002
     * bghz : 1
     * bgzd : yctqsq
     * zjczsj : 20160902162152
     * jylsh : 2016000273804
     * bgqz : 0
     */

    private String jym;
    private String bghz;
    private String bgzd;
    private String zjczsj;
    private String jylsh;
    private String bgqz;

    public String getJym() {
        return jym;
    }

    public void setJym(String jym) {
        this.jym = jym;
    }

    public String getBghz() {
        if (TextUtils.isEmpty(bghz)) bghz = "无";
        return bghz;
    }

    public void setBghz(String bghz) {
        this.bghz = bghz;
    }

    public String getBgzd() {
        if (TextUtils.isEmpty(bgzd)) bgzd = "无";
        return bgzd;
    }

    public void setBgzd(String bgzd) {
        this.bgzd = bgzd;
    }

    public String getZjczsj() {
        String str = zjczsj.substring(0, 8);
        if (str.length() != 8) return str;
        return str.substring(0, 4) + "/" + str.substring(4, 6) + "/" + str.substring(6, 8);
    }

    public void setZjczsj(String zjczsj) {
        this.zjczsj = zjczsj;
    }

    public String getJylsh() {
        return jylsh;
    }

    public void setJylsh(String jylsh) {
        this.jylsh = jylsh;
    }

    public String getBgqz() {
        if (TextUtils.isEmpty(bgqz)) bgqz = "无";
        return bgqz;
    }

    public void setBgqz(String bgqz) {
        this.bgqz = bgqz;
    }

//    变更明细

}
