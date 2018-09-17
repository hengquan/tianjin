package cn.tianjin.unifiedfee.ot.service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.spiritdata.framework.core.model.tree.TreeNode;
import com.spiritdata.framework.core.model.tree.TreeNodeBean;
import com.spiritdata.framework.util.DateUtils;
import com.spiritdata.framework.util.SequenceUUID;
import com.spiritdata.framework.util.TreeUtils;

import cn.taiji.oauthbean.dto.UserInfo;
import cn.tianjin.unifiedfee.ot.entity.Category;
import cn.tianjin.unifiedfee.ot.mapper.CategoryMapper;
import cn.tianjin.unifiedfee.ot.model.CategoryNode;

@Service
public class CatagoryService {
    private static TreeNode<CategoryNode> root=null;
    @Autowired
    private CategoryMapper categoryDao;

    @SuppressWarnings({ "unchecked", "static-access" })
    private void initRoot() {
        List<Map<String, Object>> ml=categoryDao.getAllListWithMap();
        List<Category> cList=new ArrayList<Category>();
        for (Map<String, Object> _m: ml) {
            cList.add(getCategoryFromMap(_m));
        }

        CategoryNode cRootNode=new CategoryNode();
        cRootNode.setId("0");
        cRootNode.setParentId("-1");
        cRootNode.setNodeName("分类根节点");
        TreeNode<CategoryNode> _root=new TreeNode<CategoryNode>(cRootNode);

        if (cList!=null&&!cList.isEmpty()) {
            List<CategoryNode> cTreeNodeList=new ArrayList<CategoryNode>();
            for (Category c: cList) {
                CategoryNode cn=new CategoryNode();
                cn.buildFromPo(c);
                cTreeNodeList.add(cn);
            }

            Map<String, Object> m=TreeUtils.convertFromList(cTreeNodeList);
            List<TreeNode<? extends TreeNodeBean>> l=(List<TreeNode<? extends TreeNodeBean>>) m.get("forest");

            List<Map<String, Object>> ret=new ArrayList<Map<String, Object>>();
            for (TreeNode<? extends TreeNodeBean> cn : l) {
                ret.add(getMap(cn));
                if (!cn.isLeaf()) deepAdd(cn.getChildren(), ret);
            }
            _root.setChildren(l);
        }
        this.root=_root;
    }
    @SuppressWarnings({ "unused", "unchecked" })
    public TreeNode<CategoryNode> getCategoryNodeById(String cId) {
        if (root==null) initRoot();
        return StringUtils.isBlank(cId)?root:(TreeNode<CategoryNode>)root.findNode(cId);
    }

    public Map<String, Object> getTreeDate() {
        if (root==null) initRoot();
        Map<String, Object> retM=new HashMap<String, Object>();
        retM.put("DataTree", root);
        retM.put("DataList", TreeUtils.getDeepList(root));
        return retM;
    }

    private void deepAdd(List<TreeNode<? extends TreeNodeBean>> l, List<Map<String, Object>> deepList) {
        for (TreeNode<? extends TreeNodeBean> isn : l) {
            deepList.add(getMap(isn));
            if (!isn.isLeaf()) {
                deepAdd(isn.getChildren(), deepList);
            }
        }
    }

    private Map<String, Object> getMap(TreeNode<? extends TreeNodeBean> cTreeNode) {
        Map<String, Object> ret=new HashMap<String, Object>();
        ret.put("id", cTreeNode.getId());
        ret.put("parentId", cTreeNode.getParentId());
        ret.put("parentIds", cTreeNode.getAttribute("parentIds"));
        ret.put("name", cTreeNode.getNodeName());
        ret.put("sort", cTreeNode.getAttribute("sort"));
        ret.put("isvalid", cTreeNode.getAttribute("isvalid"));
        ret.put("createName", cTreeNode.getAttribute("isvalid"));
        Date d=new Date(((((CategoryNode)cTreeNode.getTnEntity()).getCreateDate()).getTime()));
        ret.put("createDate", DateUtils.convert2LongLocalStr(d));
        ret.put("remarkds", cTreeNode.getAttribute("remark"));
        return ret;
    }

    private Category getCategoryFromMap(Map<String, Object> m) {
        Category c=new Category();
        c.setId((String)m.get("ID"));
        c.setParentId((String)m.get("PARENT_ID"));
        c.setParentIds((String)m.get("PARENT_IDS"));
        c.setName((String)m.get("NAME"));
        c.setSort(Integer.parseInt(""+m.get("SORT")));
        c.setIsvalid(Integer.parseInt(""+m.get("ISVALID")));
        c.setCreateId((String)m.get("CREATE_BY"));
        c.setCreateName((String)m.get("CREATE_NAME"));
        c.setCreateDate(new java.sql.Date(((Timestamp)m.get("CREATE_DATE")).getTime()));
        c.setUpdateId((String)m.get("UPDATE_BY"));
        c.setUpdateName((String)m.get("UPDATE_NAME"));
        c.setUpdateDate(new java.sql.Date(((Timestamp)m.get("UPDATE_DATE")).getTime()));
        return c;
    }

    public Map<String, Object> save(Category cate, UserInfo ui) {
        Map<String, Object> retM=new HashMap<String, Object>();
        //检查是否合法
        //检查是否选择了父节点
        
        //检查名称是否有，且同级不能重名
        if (StringUtils.isBlank(cate.getParentId())) cate.setParentId("0");
        boolean isInsert=false;
        if (StringUtils.isBlank(cate.getId())) {
            isInsert=true;
            cate.setId(SequenceUUID.getPureUUID());
            cate.setCreateDate(new java.sql.Date(System.currentTimeMillis()));
            cate.setCreateId(ui.getUserId());
            cate.setCreateName(ui.getUsername());
        }
        TreeNode<CategoryNode> cn=null;
        if (!StringUtils.isBlank(cate.getParentId())) cn=this.getCategoryNodeById(cate.getParentId());
        if (cn==null) {
            cate.setParentId("0");
            cate.setParentIds("0,");
        } else {
            cate.setParentIds((cn.getTnEntity()).getParentIds()+cn.getParentId()+",");
        }
        cate.setUpdateDate(new java.sql.Date(System.currentTimeMillis()));
        cate.setUpdateId(ui.getUserId());
        cate.setUpdateName(ui.getUsername());
        if (isInsert) categoryDao.insertSelective(cate);
        else categoryDao.update(cate);
        retM.put("returnCode", "00");
        return retM;
    }
}