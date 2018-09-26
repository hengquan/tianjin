package cn.tianjin.unifiedfee.ot.entity;

import java.util.Date;
import java.util.List;

public class Kj {
    private String id;

    private String kjName;

    private String kjCatId;

    private String kjCatNames;

    private String expireDate;

    private String remarks;

    private Integer state;

    private Integer score;

    private Integer sort;

    private Date createDate;

    private String createdate;

    private String createBy;

    private String createName;

    private String kjHtml;

    private String ids;

    private List<Kj> kjs;
    
    private String mainUrl;

    private Integer logVisitCount;

    public String getMainUrl() {
        return mainUrl;
    }

    public void setMainUrl(String mainUrl) {
        this.mainUrl = mainUrl;
    }

    public Integer getLogVisitCount() {
        return logVisitCount;
    }

    public void setLogVisitCount(Integer logVisitCount) {
        this.logVisitCount = logVisitCount;
    }

    public List<Kj> getKjs() {
        return kjs;
    }

    public void setKjs(List<Kj> kjs) {
        this.kjs = kjs;
    }

    public String getIds() {
        return ids;
    }

    public void setIds(String ids) {
        this.ids = ids;
    }

    public String getCreatedate() {
        return createdate;
    }

    public void setCreatedate(String createdate) {
        this.createdate = createdate;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public String getKjName() {
        return kjName;
    }

    public void setKjName(String kjName) {
        this.kjName = kjName == null ? null : kjName.trim();
    }

    public String getKjCatId() {
        return kjCatId;
    }

    public void setKjCatId(String kjCatId) {
        this.kjCatId = kjCatId == null ? null : kjCatId.trim();
    }

    public String getKjCatNames() {
        return kjCatNames;
    }

    public void setKjCatNames(String kjCatNames) {
        this.kjCatNames = kjCatNames == null ? null : kjCatNames.trim();
    }

    public String getExpireDate() {
        return expireDate;
    }

    public void setExpireDate(String expireDate) {
        this.expireDate = expireDate == null ? null : expireDate.trim();
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks == null ? null : remarks.trim();
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
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

    public String getKjHtml() {
        return kjHtml;
    }

    public void setKjHtml(String kjHtml) {
        this.kjHtml = kjHtml == null ? null : kjHtml.trim();
    }
}