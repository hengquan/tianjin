package cn.tianjin.unifiedfee.ot.entity;

import java.util.Date;
import java.util.List;

public class Mnsc {
    private String id;

    private String mnscName;

    private String mnscCatId;

    private String mnscCatNames;

    private String expireDate;

    private String mnscUrl;

    private String remarks;

    private Integer state;

    private Integer score;

    private Integer sort;

    private Date createDate;

    private String createBy;

    private String createName;

    private String kjids;
    
    private List<Kj> kjs;
    
    private String mainUrl;
    
    private Integer logVisitCount;
    
    private String createdate;
    
    private List<CommArchive> commArchives;
    
    private Integer isvalid;

    public Integer getIsvalid() {
        return isvalid;
    }

    public void setIsvalid(Integer isvalid) {
        this.isvalid = isvalid;
    }

    public List<CommArchive> getCommArchives() {
        return commArchives;
    }

    public void setCommArchives(List<CommArchive> commArchives) {
        this.commArchives = commArchives;
    }

    public String getCreatedate() {
        return createdate;
    }

    public void setCreatedate(String createdate) {
        this.createdate = createdate;
    }

    public Integer getLogVisitCount() {
        return logVisitCount;
    }

    public void setLogVisitCount(Integer logVisitCount) {
        this.logVisitCount = logVisitCount;
    }

    public String getMainUrl() {
        return mainUrl;
    }

    public void setMainUrl(String mainUrl) {
        this.mainUrl = mainUrl;
    }

    public List<Kj> getKjs() {
        return kjs;
    }

    public void setKjs(List<Kj> kjs) {
        this.kjs = kjs;
    }

    public String getKjids() {
        return kjids;
    }

    public void setKjids(String kjids) {
        this.kjids = kjids;
    }

    private List<Mnsc> mnscs;

    public List<Mnsc> getMnscs() {
        return mnscs;
    }

    public void setMnscs(List<Mnsc> mnscs) {
        this.mnscs = mnscs;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public String getMnscName() {
        return mnscName;
    }

    public void setMnscName(String mnscName) {
        this.mnscName = mnscName == null ? null : mnscName.trim();
    }

    public String getMnscCatId() {
        return mnscCatId;
    }

    public void setMnscCatId(String mnscCatId) {
        this.mnscCatId = mnscCatId == null ? null : mnscCatId.trim();
    }

    public String getMnscCatNames() {
        return mnscCatNames;
    }

    public void setMnscCatNames(String mnscCatNames) {
        this.mnscCatNames = mnscCatNames == null ? null : mnscCatNames.trim();
    }

    public String getExpireDate() {
        return expireDate;
    }

    public void setExpireDate(String expireDate) {
        this.expireDate = expireDate == null ? null : expireDate.trim();
    }

    public String getMnscUrl() {
        return mnscUrl;
    }

    public void setMnscUrl(String mnscUrl) {
        this.mnscUrl = mnscUrl == null ? null : mnscUrl.trim();
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
}