package com.huzhou.gjj.bean;

import java.io.Serializable;

public class Bank implements Serializable {


    /**
     * YHZH : 555542222552
     * YHKBDZT : 0
     * ID : 600349
     * KHYH : 301
     * KZYT : 3
     */

    private String YHZH;
    private String YHKBDZT;
    private String ID;
    private String KHYH;
    private String KZYT;

    public String getYHZH() {
        return YHZH;
    }

    public void setYHZH(String YHZH) {
        this.YHZH = YHZH;
    }

    public String getYHKBDZT() {
        return YHKBDZT;
    }

    public void setYHKBDZT(String YHKBDZT) {
        this.YHKBDZT = YHKBDZT;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getKHYH() {
        return KHYH;
    }

    public void setKHYH(String KHYH) {
        this.KHYH = KHYH;
    }

    public String getKZYT() {
        return KZYT;
    }

    public void setKZYT(String KZYT) {
        this.KZYT = KZYT;
    }
}
