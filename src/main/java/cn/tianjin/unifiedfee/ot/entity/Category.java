package cn.tianjin.unifiedfee.ot.entity;

import java.sql.Date;

public class Category {
    private String id;          //主键
    private String parentId;    //父级编号
    private String parentIds;   //所有父级编号
    private String name ;       //字典项名称
    private int sort;           //排序，大者靠前
    private int isvalid;        //是否有效，是否显示,1有效，0无效
    private String createId;    //创建者Id
    private String createName;  //创建者名称
    private Date createDate;    //创建者时间
    private String updateId;    //最后修改者Id
    private String updateName;  //最后修改者名称
    private Date updateDate;    //最后修改时间
    private String remarks;     //说明

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id=id;
    }
    public String getParentId() {
        return parentId;
    }
    public void setParentId(String parentId) {
        this.parentId=parentId;
    }
    public String getParentIds() {
        return parentIds;
    }
    public void setParentIds(String parentIds) {
        this.parentIds=parentIds;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name=name;
    }
    public int getSort() {
        return sort;
    }
    public void setSort(int sort) {
        this.sort=sort;
    }
    public int getIsvalid() {
        return isvalid;
    }
    public void setIsvalid(int isvalid) {
        this.isvalid=isvalid;
    }
    public String getCreateId() {
        return createId;
    }
    public void setCreateId(String createId) {
        this.createId=createId;
    }
    public String getCreateName() {
        return createName;
    }
    public void setCreateName(String createName) {
        this.createName=createName;
    }
    public Date getCreateDate() {
        return createDate;
    }
    public void setCreateDate(Date createDate) {
        this.createDate=createDate;
    }
    public String getUpdateId() {
        return updateId;
    }
    public void setUpdateId(String updateId) {
        this.updateId=updateId;
    }
    public String getUpdateName() {
        return updateName;
    }
    public void setUpdateName(String updateName) {
        this.updateName=updateName;
    }
    public Date getUpdateDate() {
        return updateDate;
    }
    public void setUpdateDate(Date updateDate) {
        this.updateDate=updateDate;
    }
    public String getRemarks() {
        return remarks;
    }
    public void setRemarks(String remarks) {
        this.remarks=remarks;
    }
}