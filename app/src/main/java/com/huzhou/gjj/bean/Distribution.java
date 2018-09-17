package com.huzhou.gjj.bean;

import java.io.Serializable;

public class Distribution implements Serializable {

    /**
     * id : 33050101
     * depAddress : 浙江省湖州市吴兴区新龙门3号楼1号店
     * depName : 市中心网点
     */

    private String id;
    private String depAddress;
    private String depName;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDepAddress() {
        return depAddress;
    }

    public void setDepAddress(String depAddress) {
        this.depAddress = depAddress;
    }

    public String getDepName() {
        return depName;
    }

    public void setDepName(String depName) {
        this.depName = depName;
    }
}
