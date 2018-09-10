package cn.tianjin.unifiedfee.ot.model;

import java.util.HashMap;
import java.util.Map;

import com.spiritdata.framework.core.model.tree.TreeNode;
import com.spiritdata.framework.core.model.tree.TreeNodeBean;
import com.spiritdata.framework.exceptionC.Plat0003CException;
import com.spiritdata.framework.ui.tree.UiTree;

public class UIBootstrapTreeTable4Category<T extends TreeNodeBean> extends UiTree<T> {
    private static final long serialVersionUID = -4240752866278555428L;

    private boolean isParent;
    private boolean isOpen;
    private boolean checked;
    private String pName;
    public String getNodeName() {
        return nodeName;
    }
    public void setNodeName(String nodeName) {
        this.nodeName = nodeName;
    }
    public String getpName() {
        return pName;
    }
    public void setpName(String pName) {
        this.pName = pName;
    }
    private String parentNodeName;
    
    public String getParentNodeName() {
        return parentNodeName;
    }
    public void setParentNodeName(String parentNodeName) {
        this.parentNodeName = parentNodeName;
    }
    public boolean isChecked() {
        return checked;
    }
    public void setChecked(boolean checked) {
        this.checked = checked;
    }
    public boolean isParent() {
        return isParent;
    }
    public void setIsParent(boolean isParent) {
        this.isParent = isParent;
    }
    public boolean isOpen() {
        return isOpen;
    }
    public void setIsOpen(boolean isOpen) {
        this.isOpen = isOpen;
    }

    /**
     * 构造函数
     * @param tn 需要转换的结点数据
     * @throws CloneNotSupportedException 
     */
    @SuppressWarnings("unchecked")
    public UIBootstrapTreeTable4Category(T tn) {
        try {
            this.setTnEntity((T)tn.clone());
        } catch(Exception e) {
        }
    }

    /**
     * 构造函数
     * @param tn 需要转换的树
     * @throws CloneNotSupportedException 
     */
    @SuppressWarnings({ "unchecked", "rawtypes"})
    public UIBootstrapTreeTable4Category(TreeNode<? extends TreeNodeBean> tn) {
        try {
            this.setTnEntity((T)((T)tn.getTnEntity()).clone());
        } catch(Exception e) {
        }
        this.setIsOpen(false);
        if (tn.isLeaf()) {
            this.setIsParent(false);
        } else {
            this.setIsParent(true);
            if (tn.getLevel()==0) this.setIsOpen(true);
            for (TreeNode<? extends TreeNodeBean> t: tn.getChildren()) this.addChild(new UIBootstrapTreeTable4Category(t));
        }
    }

    /**
     * 构造函数，根据原树tn，构造深度遍历树的显示树，并满足如下条件：<br/>
     * 若原树tn的总结点数大于limitCount，则只返回此树结点下的一级结点树，否则返回整棵树。
     * @param tn 需要转换的树
     * @throws CloneNotSupportedException 
     */
    @SuppressWarnings({ "unchecked", "rawtypes"})
    public UIBootstrapTreeTable4Category(TreeNode<? extends TreeNodeBean> tn, int limitCount) {
        this.setTnEntity((T)tn.getTnEntity());
        if (limitCount<1) throw new Plat0003CException(new IllegalArgumentException("结点限制数必须大于0"));
        if (tn.getAllCount()>limitCount) {
            this.setIsParent(!tn.isLeaf());
            for (TreeNode<? extends TreeNodeBean> t: tn.getChildren()) {
                UIBootstrapTreeTable4Category<? extends TreeNodeBean> uit=new UIBootstrapTreeTable4Category(t.getTnEntity());
                uit.setIsParent(!t.isLeaf());
                this.addChild(uit);
            }
        } else {
            this.setIsOpen(false);
            if (tn.isLeaf()) {
                this.setIsParent(false);
            } else {
                this.setIsParent(true);
                if (tn.getLevel()==0) this.setIsOpen(true);
                for (TreeNode<? extends TreeNodeBean> t: tn.getChildren()) this.addChild(new UIBootstrapTreeTable4Category(t));
            }
        }
    }

    @Override
    protected Map<String, Object> convert4Tree() {
        Map<String, Object> treeM = new HashMap<String, Object>();
        treeM.put("id", this.getId());
        treeM.put("name", this.getNodeName());
        treeM.put("isParent", this.isParent());
        treeM.put("isOpen", this.isOpen());
        treeM.put("checked", this.isChecked());
        Map<String, Object> m=this.getTnEntity().toHashMapAsBean();
        if (m.get("displayName")!=null) treeM.put("text", m.get("displayName")+"");
        return treeM;
    }

    @Override
    protected Map<String, Object> convert4Attributes() {
        Map<String, Object> retM = this.getTnEntity().toHashMapAsBean();
        retM.putAll(this.getAttributes());
        retM.put("pathName", this.getTreePathName());
        return retM;
    }

    @Override
    protected Map<String, Object> convert4TreeGrid() {
        Map<String, Object> treeGridM = new HashMap<String, Object>();
        treeGridM.put("isParent", this.isParent());
        treeGridM.put("isOpen", this.isOpen());
        treeGridM.put("checked", this.isChecked());
        treeGridM.putAll(this.convert4Attributes());
        return treeGridM;
    }
}
