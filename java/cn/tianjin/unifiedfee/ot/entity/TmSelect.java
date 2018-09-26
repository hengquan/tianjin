package cn.tianjin.unifiedfee.ot.entity;

import java.util.Date;

public class TmSelect {
    private String id;

    private String tmId;

    private String tmSelectSign;

    private String tmSelectDesc;

    private Integer sort;
    
    private Integer isAnswer;

    private Date createDate;

    private String createBy;

    private String createName;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public String getTmId() {
        return tmId;
    }

    public void setTmId(String tmId) {
        this.tmId = tmId == null ? null : tmId.trim();
    }

    public String getTmSelectSign() {
        return tmSelectSign;
    }

    public void setTmSelectSign(String tmSelectSign) {
        this.tmSelectSign = tmSelectSign == null ? null : tmSelectSign.trim();
    }

    public String getTmSelectDesc() {
        return tmSelectDesc;
    }

    public void setTmSelectDesc(String tmSelectDesc) {
        this.tmSelectDesc = tmSelectDesc == null ? null : tmSelectDesc.trim();
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy == null ? null : createBy.trim();
    }

    public String getCreateName() {
        return createName;
    }

    public void setCreateName(String createName) {
        this.createName = createName == null ? null : createName.trim();
    }

    public Integer getIsAnswer() {
        return isAnswer;
    }

    public void setIsAnswer(Integer isAnswer) {
        this.isAnswer = isAnswer;
    }
}