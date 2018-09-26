package cn.tianjin.unifiedfee.ot.entity;

import java.util.Date;

public class CommAudit {
    private String id;

    private String flowId;

    private String flowName;

    private String activityId;

    private String activityName;

    private String commiterId;

    private String commiter;

    private String auditerId;

    private String auditer;

    private String objTabname;

    private String objId;

    private Date createDate;

    private Date auditDate;

    private Integer state;

    private String interpret;

    private String opinion;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public String getFlowId() {
        return flowId;
    }

    public void setFlowId(String flowId) {
        this.flowId = flowId == null ? null : flowId.trim();
    }

    public String getFlowName() {
        return flowName;
    }

    public void setFlowName(String flowName) {
        this.flowName = flowName == null ? null : flowName.trim();
    }

    public String getActivityId() {
        return activityId;
    }

    public void setActivityId(String activityId) {
        this.activityId = activityId == null ? null : activityId.trim();
    }

    public String getActivityName() {
        return activityName;
    }

    public void setActivityName(String activityName) {
        this.activityName = activityName == null ? null : activityName.trim();
    }

    public String getCommiterId() {
        return commiterId;
    }

    public void setCommiterId(String commiterId) {
        this.commiterId = commiterId == null ? null : commiterId.trim();
    }

    public String getCommiter() {
        return commiter;
    }

    public void setCommiter(String commiter) {
        this.commiter = commiter == null ? null : commiter.trim();
    }

    public String getAuditerId() {
        return auditerId;
    }

    public void setAuditerId(String auditerId) {
        this.auditerId = auditerId == null ? null : auditerId.trim();
    }

    public String getAuditer() {
        return auditer;
    }

    public void setAuditer(String auditer) {
        this.auditer = auditer == null ? null : auditer.trim();
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

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getAuditDate() {
        return auditDate;
    }

    public void setAuditDate(Date auditDate) {
        this.auditDate = auditDate;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public String getInterpret() {
        return interpret;
    }

    public void setInterpret(String interpret) {
        this.interpret = interpret == null ? null : interpret.trim();
    }

    public String getOpinion() {
        return opinion;
    }

    public void setOpinion(String opinion) {
        this.opinion = opinion == null ? null : opinion.trim();
    }
}