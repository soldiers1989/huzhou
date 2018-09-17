package com.huzhou.gjj.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2017/4/25.
 */
public class Dkjc3 implements Serializable {
    private String jcjsSum;
    private String yhke;
    private String jcjsbl;
    private String RtnFlag;

    public List<Dkjc4> getHkjhxx() {
        return hkjhxx;
    }

    public void setHkjhxx(List<Dkjc4> hkjhxx) {
        this.hkjhxx = hkjhxx;
    }

    public String getJcjsSum() {
        return jcjsSum;
    }

    public void setJcjsSum(String jcjsSum) {
        this.jcjsSum = jcjsSum;
    }

    public String getYhke() {
        return yhke;
    }

    public void setYhke(String yhke) {
        this.yhke = yhke;
    }

    public String getJcjsbl() {
        return jcjsbl;
    }

    public void setJcjsbl(String jcjsbl) {
        this.jcjsbl = jcjsbl;
    }

    public String getRtnFlag() {
        return RtnFlag;
    }

    public void setRtnFlag(String rtnFlag) {
        RtnFlag = rtnFlag;
    }

    private List<Dkjc4> hkjhxx;
}
