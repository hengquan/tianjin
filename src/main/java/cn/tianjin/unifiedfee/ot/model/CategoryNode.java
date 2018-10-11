package cn.tianjin.unifiedfee.ot.model;

import java.io.Serializable;
import java.sql.Date;
import java.util.HashMap;
import java.util.Map;

import com.spiritdata.framework.core.model.ModelSwapPo;
import com.spiritdata.framework.core.model.tree.TreeNodeBean;
import com.spiritdata.framework.exceptionC.Plat0006CException;
import com.spiritdata.framework.util.DateUtils;
import com.spiritdata.framework.util.SequenceUUID;
import com.spiritdata.framework.util.StringUtils;

import cn.tianjin.unifiedfee.ot.entity.Category;

public class CategoryNode extends TreeNodeBean implements Serializable, ModelSwapPo {
    private static final long serialVersionUID = -1955635556689723791L;

    //private String id;          //主键
    //private String parentId;    //父级编号
    private String parentIds;   //所有父级编号
    //private String name ;       //字典项名称
    //private int sort;           //排序，大者靠前
    private int isvalid;        //是否有效，是否显示,1有效，0无效
    private String createId;    //创建者Id
    private String createName;  //创建者名称
    private Date createDate;    //创建者时间
    private String updateId;    //最后修改者Id
    private String updateName;  //最后修改者名称
    private Date updateDate;    //最后修改时间
    private String remarks;     //说明

    public CategoryNode() {
        super();
        this.setOrderType(1);
    }
    public String getParentIds() {
        return parentIds;
    }
    public void setParentIds(String parentIds) {
        this.parentIds=parentIds;
    }
//    public int getSort() {
//        return sort;
//    }
//    public void setSort(int sort) {
//        this.sort=sort;
//    }
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

    @Override
    public Object convert2Po() {
        Category ret=new Category();

        if (StringUtils.isNullOrEmptyOrSpace(this.getId())) ret.setId(SequenceUUID.getPureUUID());
        else ret.setId(this.getId());

        ret.setParentId(this.getParentId());
        ret.setParentIds(this.getParentIds());
        ret.setName(this.getNodeName());
        ret.setIsvalid(this.getIsvalid());
        ret.setSort(this.getOrder());
        ret.setCreateId(this.getCreateId());
        ret.setCreateName(this.getCreateName());
        ret.setCreateDate(this.getCreateDate());
        ret.setUpdateId(this.getUpdateId());
        ret.setUpdateName(this.getUpdateName());
        ret.setUpdateDate(this.getUpdateDate());
        ret.setRemarks(this.getRemarks());
        return ret;
    }

    @Override
    public void buildFromPo(Object po) {
        if (po==null) throw new Plat0006CException("Po对象为空，无法从空对象得到概念/逻辑对象！");
        if (!(po instanceof Category)) throw new Plat0006CException("Po对象不是Category的实例，无法从此对象构建字典项对象！");
        Category _po=(Category)po;
        this.setId(_po.getId());
        this.setParentId(_po.getParentId());
        this.setParentIds(_po.getParentIds());
        this.setNodeName(_po.getName());
        this.setOrder(_po.getSort());
        this.setIsvalid(_po.getIsvalid());
        this.setCreateId(_po.getCreateId());
        this.setCreateName(_po.getCreateName());
        this.setCreateDate(_po.getCreateDate());
        this.setUpdateId(_po.getUpdateId());
        this.setUpdateName(_po.getUpdateName());
        this.setUpdateDate(_po.getUpdateDate());
        this.setRemarks(_po.getRemarks());
    }

    public Map<String, Object> toMap() {
        Map<String, Object> ret=new HashMap<String, Object>();
        ret.put("id", this.getId());
        ret.put("parentId", this.getParentId());
        ret.put("parentIds", this.getParentIds());
        ret.put("name", this.getNodeName());
        ret.put("sort", this.getOrder());
        ret.put("isvalid", this.getIsvalid());
        ret.put("createName", this.getCreateName());
        ret.put("createDate", DateUtils.convert2LongLocalStr(this.getCreateDate()));
        ret.put("remarkds", this.getRemarks());
        return ret;
    }
}