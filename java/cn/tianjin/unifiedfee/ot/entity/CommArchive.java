package cn.tianjin.unifiedfee.ot.entity;

import java.util.Date;

public class CommArchive {
    private String id;

    private String fileName;

    private String filePath;

    private String fileUrl;

    private Integer isShow;

    private String objTabname;

    private String objId;

    private String archiveType;

    private Integer sort;

    private Date createDate;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName == null ? null : fileName.trim();
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath == null ? null : filePath.trim();
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl == null ? null : fileUrl.trim();
    }

    public Integer getIsShow() {
        return isShow;
    }

    public void setIsShow(Integer isShow) {
        this.isShow = isShow;
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

    public String getArchiveType() {
        return archiveType;
    }

    public void setArchiveType(String archiveType) {
        this.archiveType = archiveType == null ? null : archiveType.trim();
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