package cn.tianjin.unifiedfee.ot.entity;

import java.util.Date;

public class SJTm {
    private String id;

    private String sjId;

    private String tmId;

    private Integer score;
    private Integer tmScore;

    private Integer sort;

    private Integer inType;

    private Date createDate;

    private String createBy;

    private String createName;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public String getSjId() {
        return sjId;
    }

    public void setSjId(String sjId) {
        this.sjId = sjId == null ? null : sjId.trim();
    }

    public String getTmId() {
        return tmId;
    }

    public void setTmId(String tmId) {
        this.tmId = tmId == null ? null : tmId.trim();
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public Integer getInType() {
        return inType;
    }

    public void setInType(Integer inType) {
        this.inType = inType;
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

    public Integer getTmScore() {
        return tmScore;
    }

    public void setTmScore(Integer tmScore) {
        this.tmScore = tmScore;
    }
}