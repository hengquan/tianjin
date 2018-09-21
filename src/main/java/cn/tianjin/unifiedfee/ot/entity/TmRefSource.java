package cn.tianjin.unifiedfee.ot.entity;

public class TmRefSource {
    private String id;

    private String tmId;

    private String refTabname;

    private String refId;
    
    private String refname;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public String getTmId() {
        return tmId;
    }

    public void setTmId(String tmId) {
        this.tmId = tmId == null ? null : tmId.trim();
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

    public String getRefname() {
        return refname;
    }

    public void setRefname(String refname) {
        this.refname = refname;
    }
}