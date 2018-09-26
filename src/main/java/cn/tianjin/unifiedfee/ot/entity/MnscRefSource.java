package cn.tianjin.unifiedfee.ot.entity;

import java.util.Date;

public class MnscRefSource {
    private String id;

    private String mnscId;

    private String refTabname;

    private String refId;

    private Integer sort;

    private String memo;

    private Date createDate;

    private String createBy;

    private String createName;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public String getMnscId() {
        return mnscId;
    }

    public void setMnscId(String mnscId) {
        this.mnscId = mnscId == null ? null : mnscId.trim();
    }

    public String getRefTabname() {
        return refTabname;
    }

    public void setRefTabname(String refTabname) {
        this.refTabname = refTabname == null ? null : refTabname.trim();
    }

    public String getRefId() {
        return refId;
    }

    public void setRefId(String refId) {
        this.refId = refId == null ? null : refId.trim();
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo == null ? null : memo.trim();
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
}