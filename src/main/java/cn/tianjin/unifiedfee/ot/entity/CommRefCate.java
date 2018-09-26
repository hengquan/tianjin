package cn.tianjin.unifiedfee.ot.entity;

import java.util.Date;

public class CommRefCate {
    private String id;

    private String catId;

    private String catNames;

    private Integer isvalid;

    private String objTabname;

    private String objId;

    private String refType;

    private Integer sort;

    private Date createDate;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public String getCatId() {
        return catId;
    }

    public void setCatId(String catId) {
        this.catId = catId == null ? null : catId.trim();
    }

    public String getCatNames() {
        return catNames;
    }

    public void setCatNames(String catNames) {
        this.catNames = catNames == null ? null : catNames.trim();
    }

    public Integer getIsvalid() {
        return isvalid;
    }

    public void setIsvalid(Integer isvalid) {
        this.isvalid = isvalid;
    }

    public String getObjTabname() {
        return objTabname;
    }

    public void setObjTabname(String objTabname) {
        this.objTabname = objTabname == null ? null : objTabname.trim();
    }

    public String getObjId() {
        return objId;
    }

    public void setObjId(String objId) {
        this.objId = objId == null ? null : objId.trim();
    }

    public String getRefType() {
        return refType;
    }

    public void setRefType(String refType) {
        this.refType = refType == null ? null : refType.trim();
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
}