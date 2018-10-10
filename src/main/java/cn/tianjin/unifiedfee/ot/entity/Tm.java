package cn.tianjin.unifiedfee.ot.entity;

import java.util.Date;

public class Tm {
    private String id;

    private String tmType;

    private String tmHtml;

    private Integer score;

    private Integer diffScore;

    private Date createDate;
    
    private String strcreatedate;

    private String createBy;

    private String createName;
    
    private String kjList;
    
    private String mnscList;
    
    private String kjnameList;
    
    private String mnscnameList;
    
    private String tmSelectDesc;
    
    private Integer isAnswer;
    
    private String tmSelectSign;   
    
    private Integer sort;      
    
    private Integer isvalid;
    
    private String  catid;
    
    private String  catnames;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public String getTmType() {
        return tmType;
    }

    public void setTmType(String tmType) {
        this.tmType = tmType == null ? null : tmType.trim();
    }

    public String getTmHtml() {
        return tmHtml;
    }

    public void setTmHtml(String tmHtml) {
        this.tmHtml = tmHtml == null ? null : tmHtml.trim();
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public Integer getDiffScore() {
        return diffScore;
    }

    public void setDiffScore(Integer diffScore) {
        this.diffScore = diffScore;
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

    public String getStrcreatedate() {
        return strcreatedate;
    }

    public void setStrcreatedate(String strcreatedate) {
        this.strcreatedate = strcreatedate;
    }

    public String getKjList() {
        return kjList;
    }

    public void setKjList(String kjList) {
        this.kjList = kjList;
    }

    public String getMnscList() {
        return mnscList;
    }

    public void setMnscList(String mnscList) {
        this.mnscList = mnscList;
    }

    public String getKjnameList() {
        return kjnameList;
    }

    public void setKjnameList(String kjnameList) {
        this.kjnameList = kjnameList;
    }

    public String getMnscnameList() {
        return mnscnameList;
    }

    public void setMnscnameList(String mnscnameList) {
        this.mnscnameList = mnscnameList;
    }

    public String getTmSelectDesc() {
        return tmSelectDesc;
    }

    public void setTmSelectDesc(String tmSelectDesc) {
        this.tmSelectDesc = tmSelectDesc;
    }

    public String getTmSelectSign() {
        return tmSelectSign;
    }

    public void setTmSelectSign(String tmSelectSign) {
        this.tmSelectSign = tmSelectSign;
    }

    public Integer getIsAnswer() {
        return isAnswer;
    }

    public void setIsAnswer(Integer isAnswer) {
        this.isAnswer = isAnswer;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public Integer getIsvalid() {
        return isvalid;
    }

    public void setIsvalid(Integer isvalid) {
        this.isvalid = isvalid;
    }

    public String getCatid() {
        return catid;
    }

    public void setCatid(String catid) {
        this.catid = catid;
    }

    public String getCatnames() {
        return catnames;
    }

    public void setCatnames(String catnames) {
        this.catnames = catnames;
    }
}