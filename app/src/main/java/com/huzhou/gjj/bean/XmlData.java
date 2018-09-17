package com.huzhou.gjj.bean;

/**
 * Created by 雷力 on 2017/8/30.
 * 邮箱：853372328@qq.com
 */

public class XmlData {
    /**
     * ROOT : {"HEAD":{"UnitCode":"PDSGJJ","ChanCode":"ZFB","ResTrsBank":"10001","ResTrsTell":"10001","ResCode":"000","ResMsg":"交易成功","TrsCode":"2001","TrsChildCode":"299","ResSerial":"17083010000032"},"BODY":{"khbh":"99999","zxbh":"050010010","zjhm":"41048219XX080800XX","grxm":"测试户","csrq":"195908"}}
     */
    private String ResCode;
    private String ResMsg;
    private String khbh;
    private String zxbh;
    private String zjhm;
    private String grxm;
    private String csrq;
    private String dwmc;
    private String dwdz;

    public String getResCode() {
        return ResCode;
    }

    public void setResCode(String resCode) {
        ResCode = resCode;
    }

    public String getResMsg() {
        return ResMsg;
    }

    public void setResMsg(String resMsg) {
        ResMsg = resMsg;
    }

    public String getKhbh() {
        return khbh;
    }

    public void setKhbh(String khbh) {
        this.khbh = khbh;
    }

    public String getZxbh() {
        return zxbh;
    }

    public void setZxbh(String zxbh) {
        this.zxbh = zxbh;
    }

    public String getZjhm() {
        return zjhm;
    }

    public void setZjhm(String zjhm) {
        this.zjhm = zjhm;
    }

    public String getGrxm() {
        return grxm;
    }

    public void setGrxm(String grxm) {
        this.grxm = grxm;
    }

    public String getCsrq() {
        return csrq;
    }

    public void setCsrq(String csrq) {
        this.csrq = csrq;
    }

    public String getDwmc() {
        return dwmc;
    }

    public void setDwmc(String dwmc) {
        this.dwmc = dwmc;
    }

    public String getDwdz() {
        return dwdz;
    }

    public void setDwdz(String dwdz) {
        this.dwdz = dwdz;
    }
}
