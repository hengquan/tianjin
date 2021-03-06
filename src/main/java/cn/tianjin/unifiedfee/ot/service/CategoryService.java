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
public class CategoryService {
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

    /**
     * 获得分类树，深度遍历
     * @param cId 树节点id，为空则获得整棵树
     * @param type =0获得有效节点，=1获得所有节点
     * @return
     */
    @SuppressWarnings({ "unused", "unchecked" })
    public List<Map<String, Object>> getCateList4View(String cId, int type) {
        if (root==null) initRoot();
        TreeNode<CategoryNode> node= StringUtils.isBlank(cId)?root:(TreeNode<CategoryNode>)root.findNode(cId);
        Map<String, Object> retTree=_toTreeMap_ptTree(node, type);
        if (retTree==null) return null;
        return (List<Map<String, Object>>)retTree.get("nodes");
    }
    private Map<String, Object> _toTreeMap_ptTree(TreeNode<? extends TreeNodeBean> treeNode, int type) {
        Map<String, Object> treeMap=new HashMap<String, Object>();
        treeMap.put("id", treeNode.getId());
        treeMap.put("text", treeNode.getNodeName());
        treeMap.put("pathName", treeNode.getTreePathName("-", 0));
        if (!treeNode.isLeaf()&&treeNode.getChildCount()>0) {
            List<Map<String, Object>> new_cl=new ArrayList<Map<String, Object>>();
            for (TreeNode<? extends TreeNodeBean> _c:treeNode.getChildren()) {
                CategoryNode cn=(CategoryNode)_c.getTnEntity();
                if (type==1) {
                    new_cl.add(_toTreeMap_ptTree(_c, type));
                } else {
                    if (cn.getIsvalid()==1) new_cl.add(_toTreeMap_ptTree(_c, type));
                }
            }
            treeMap.put("nodes", new_cl);
        }
        return treeMap;
    }

    public Map<String, Object> getTreeData() {
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
        c.setRemarks((String)m.get("REMARKS"));
        return c;
    }

    @SuppressWarnings("unchecked")
    public Map<String, Object> save(Category cate, UserInfo ui) {
        Map<String, Object> retM=new HashMap<String, Object>();

        //检查是否合法
        TreeNode<CategoryNode> _curRootNode=root;
        if (StringUtils.isBlank(cate.getParentId())) cate.setParentId("0");
        if (!"0".equals(cate.getParentId())) {
            _curRootNode=(TreeNode<CategoryNode>)root.findNode(cate.getParentId());
        }
        if (_curRootNode==null) {
            retM.put("returnCode", "03");
            retM.put("messageInfo","父节点无效");
            return retM;
        }

        boolean isInsert=false;
        if (StringUtils.isBlank(cate.getId())) {
            isInsert=true;
            cate.setId(SequenceUUID.getPureUUID());
            cate.setCreateDate(new java.sql.Date(new Date().getTime()));
            cate.setCreateId(ui.getUserId());
            cate.setCreateName(ui.getUsername());

            //检查名称是否有，且同级不能重名
            boolean sameName=false;
            if (_curRootNode.getChildCount()>0) {
                for (TreeNode<? extends TreeNodeBean> cn: _curRootNode.getChildren()) {
                    if (cn.getNodeName().equals(cate.getName())) {
                        sameName=true;
                        break;
                    }
                }
            }
            if (sameName) {
                retM.put("returnCode", "04");
                retM.put("messageInfo","同级有重名分类");
                return retM;
            }
        }

        cate.setUpdateDate(new java.sql.Date(System.currentTimeMillis()));
        cate.setUpdateId(ui.getUserId());
        cate.setUpdateName(ui.getUsername());
        if (_curRootNode.isRoot()) cate.setParentIds("0,");
        else cate.setParentIds(_curRootNode.getTnEntity().getParentIds()+_curRootNode.getId()+",");

        CategoryNode cn=new CategoryNode();
        cn.buildFromPo(cate);
        if (isInsert) {
            categoryDao.insertSelective(cate);
            TreeNode<CategoryNode> tncn=new TreeNode<CategoryNode>(cn);
            _curRootNode.addChild(tncn);
        } else {
            categoryDao.update(cate);
            TreeNode<CategoryNode> tncn=(TreeNode<CategoryNode>) _curRootNode.findNode(cate.getId());
            cn.setCreateDate(tncn.getTnEntity().getCreateDate());
            cn.setCreateId(tncn.getTnEntity().getCreateId());
            cn.setCreateName(tncn.getTnEntity().getCreateName());
            tncn.setTnEntity(cn);
        }
        retM.put("returnCode", "00");
        return retM;
    }

    //@SuppressWarnings("unchecked")
    public List<Map<String, Object>> getPageData(Map<String, Object> cate) {
        return categoryDao.getListWithMap(cate);
    }

    @SuppressWarnings("unchecked")
    public Map<String, Object> changeValid(String id, int valid) {
        if (root==null) initRoot();

        Map<String, Object> retMap=new HashMap<String, Object>();

        TreeNode<CategoryNode> node=(TreeNode<CategoryNode>)root.findNode(id);
        if (node==null) {
            retMap.put("returnCode", "04");
            retMap.put("messageInfo", "分类id无对应分类，无法处理");
        } else {
            node.getTnEntity().setIsvalid(valid);
            Category c=new Category();
            c.setId(node.getId());
            c.setIsvalid(valid);
            categoryDao.changeValid(c);
            retMap.put("returnCode", "00");
        }
        return retMap;
    }
}