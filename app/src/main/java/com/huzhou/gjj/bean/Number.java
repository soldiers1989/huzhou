package com.huzhou.gjj.bean;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

import java.io.Serializable;

@Table(name = "number")
public class Number implements Serializable {


    /**
     * code : 21
     * dictType : zy
     * id : 860
     * isDef :
     * name : 企业管理人员
     * parentId :
     * rank : 0
     * seqno : 4
     */
    @Column(name = "id", isId = true)
    private int id;
    @Column(name = "code")
    private String code;
    @Column(name = "dictType")
    private String dictType;
    @Column(name = "isDef")
    private String isDef;
    @Column(name = "name")
    private String name;
    @Column(name = "parentId")
    private String parentId;
    @Column(name = "rank")
    private int rank;
    @Column(name = "seqno")
    private String seqno;

    public Number(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDictType() {
        return dictType;
    }

    public void setDictType(String dictType) {
        this.dictType = dictType;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getIsDef() {
        return isDef;
    }

    public void setIsDef(String isDef) {
        this.isDef = isDef;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public String getSeqno() {
        return seqno;
    }

    public void setSeqno(String seqno) {
        this.seqno = seqno;
    }
}
