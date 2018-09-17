package com.huzhou.gjj.bean;

import java.io.Serializable;

public class News implements Serializable {


    /**
     * operId : 10000
     * inputDate : 20161220
     * inputTime : 15:01:01
     * operName : admin
     * chanCode : 02
     * subSystem : 0
     * modifyDate : 20161220
     * infoId : TZ005
     * modifyTime : 15:02:02
     * info : APP通知测试通知测试通知测试通知测试通知测试
     * infoTitle : APP测试
     */

    private String operId;
    private String inputDate;
    private String inputTime;
    private String operName;
    private String chanCode;
    private String subSystem;
    private String modifyDate;
    private String infoId;
    private String modifyTime;
    private String info;
    private String infoTitle;

    public String getOperId() {
        return operId;
    }

    public void setOperId(String operId) {
        this.operId = operId;
    }

    public String getInputDate() {
        return inputDate;
    }

    public void setInputDate(String inputDate) {
        this.inputDate = inputDate;
    }

    public String getInputTime() {
        return inputTime;
    }

    public void setInputTime(String inputTime) {
        this.inputTime = inputTime;
    }

    public String getOperName() {
        return operName;
    }

    public void setOperName(String operName) {
        this.operName = operName;
    }

    public String getChanCode() {
        return chanCode;
    }

    public void setChanCode(String chanCode) {
        this.chanCode = chanCode;
    }

    public String getSubSystem() {
        return subSystem;
    }

    public void setSubSystem(String subSystem) {
        this.subSystem = subSystem;
    }

    public String getModifyDate() {
        return modifyDate;
    }

    public void setModifyDate(String modifyDate) {
        this.modifyDate = modifyDate;
    }

    public String getInfoId() {
        return infoId;
    }

    public void setInfoId(String infoId) {
        this.infoId = infoId;
    }

    public String getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(String modifyTime) {
        this.modifyTime = modifyTime;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getInfoTitle() {
        return infoTitle;
    }

    public void setInfoTitle(String infoTitle) {
        this.infoTitle = infoTitle;
    }
}
