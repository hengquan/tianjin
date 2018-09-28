package cn.tianjin.unifiedfee.ot.entity;

import java.util.Date;

import com.spiritdata.framework.core.model.BaseObject;

public class LogVisit extends BaseObject {
    private static final long serialVersionUID = -7582903982200904333L;

    private String id;

    private String servSysType;

    private String servSysId;

    private Integer visitType;

    private String visitSysType;

    private String visitSysId;

    private String visitModulelId;

    private String objType;

    private String objId;

    private String apiName;

    private String fromUrl;

    private String reqUrl;

    private String method;

    private String reqParam;

    private String visitorType;

    private String visitorId;

    private String visitorName;

    private String groupId;

    private String groupName;

    private String clientIp;

    private String pointInfo;

    private Integer deviceType;

    private String deviceId;

    private String deviceClass;

    private String screenSzie;

    private String exploreName;

    private String exploreVer;

    private String osName;

    private String osVer;

    private Integer dealflag;

    private String returndata;

    private Date createDate;

    private Date endtime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public String getServSysType() {
        return servSysType;
    }

    public void setServSysType(String servSysType) {
        this.servSysType = servSysType == null ? null : servSysType.trim();
    }

    public String getServSysId() {
        return servSysId;
    }

    public void setServSysId(String servSysId) {
        this.servSysId = servSysId == null ? null : servSysId.trim();
    }

    public Integer getVisitType() {
        return visitType;
    }

    public void setVisitType(Integer visitType) {
        this.visitType = visitType;
    }

    public String getVisitSysType() {
        return visitSysType;
    }

    public void setVisitSysType(String visitSysType) {
        this.visitSysType = visitSysType == null ? null : visitSysType.trim();
    }

    public String getVisitSysId() {
        return visitSysId;
    }

    public void setVisitSysId(String visitSysId) {
        this.visitSysId = visitSysId == null ? null : visitSysId.trim();
    }

    public String getVisitModulelId() {
        return visitModulelId;
    }

    public void setVisitModulelId(String visitModulelId) {
        this.visitModulelId = visitModulelId == null ? null : visitModulelId.trim();
    }

    public String getObjType() {
        return objType;
    }

    public void setObjType(String objType) {
        this.objType = objType == null ? null : objType.trim();
    }

    public String getObjId() {
        return objId;
    }

    public void setObjId(String objId) {
        this.objId = objId == null ? null : objId.trim();
    }

    public String getApiName() {
        return apiName;
    }

    public void setApiName(String apiName) {
        this.apiName = apiName == null ? null : apiName.trim();
    }

    public String getFromUrl() {
        return fromUrl;
    }

    public void setFromUrl(String fromUrl) {
        this.fromUrl = fromUrl == null ? null : fromUrl.trim();
    }

    public String getReqUrl() {
        return reqUrl;
    }

    public void setReqUrl(String reqUrl) {
        this.reqUrl = reqUrl == null ? null : reqUrl.trim();
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method == null ? null : method.trim();
    }

    public String getReqParam() {
        return reqParam;
    }

    public void setReqParam(String reqParam) {
        this.reqParam = reqParam == null ? null : reqParam.trim();
    }

    public String getVisitorType() {
        return visitorType;
    }

    public void setVisitorType(String visitorType) {
        this.visitorType = visitorType == null ? null : visitorType.trim();
    }

    public String getVisitorId() {
        return visitorId;
    }

    public void setVisitorId(String visitorId) {
        this.visitorId = visitorId == null ? null : visitorId.trim();
    }

    public String getVisitorName() {
        return visitorName;
    }

    public void setVisitorName(String visitorName) {
        this.visitorName = visitorName == null ? null : visitorName.trim();
    }

    public String getClientIp() {
        return clientIp;
    }

    public void setClientIp(String clientIp) {
        this.clientIp = clientIp == null ? null : clientIp.trim();
    }

    public String getPointInfo() {
        return pointInfo;
    }

    public void setPointInfo(String pointInfo) {
        this.pointInfo = pointInfo == null ? null : pointInfo.trim();
    }

    public Integer getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(Integer deviceType) {
        this.deviceType = deviceType;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId == null ? null : deviceId.trim();
    }

    public String getDeviceClass() {
        return deviceClass;
    }

    public void setDeviceClass(String deviceClass) {
        this.deviceClass = deviceClass == null ? null : deviceClass.trim();
    }

    public String getScreenSzie() {
        return screenSzie;
    }

    public void setScreenSzie(String screenSzie) {
        this.screenSzie = screenSzie == null ? null : screenSzie.trim();
    }

    public String getExploreName() {
        return exploreName;
    }

    public void setExploreName(String exploreName) {
        this.exploreName = exploreName == null ? null : exploreName.trim();
    }

    public String getExploreVer() {
        return exploreVer;
    }

    public void setExploreVer(String exploreVer) {
        this.exploreVer = exploreVer == null ? null : exploreVer.trim();
    }

    public String getOsName() {
        return osName;
    }

    public void setOsName(String osName) {
        this.osName = osName == null ? null : osName.trim();
    }

    public String getOsVer() {
        return osVer;
    }

    public void setOsVer(String osVer) {
        this.osVer = osVer == null ? null : osVer.trim();
    }

    public Integer getDealflag() {
        return dealflag;
    }

    public void setDealflag(Integer dealflag) {
        this.dealflag = dealflag;
    }

    public String getReturndata() {
        return returndata;
    }

    public void setReturndata(String returndata) {
        this.returndata = returndata == null ? null : returndata.trim();
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getEndtime() {
        return endtime;
    }

    public void setEndtime(Date endtime) {
        this.endtime = endtime;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }
}