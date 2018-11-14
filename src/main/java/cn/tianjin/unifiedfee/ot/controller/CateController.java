package cn.tianjin.unifiedfee.ot.controller;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.spiritdata.framework.core.model.tree.TreeNode;
import com.spiritdata.framework.core.model.tree.TreeNodeBean;
import com.spiritdata.framework.util.DateUtils;

import cn.taiji.oauthbean.dto.UserInfo;
import cn.taiji.web.base.controller.BaseController;
import cn.tianjin.unifiedfee.ot.entity.Category;
import cn.tianjin.unifiedfee.ot.model.CategoryNode;
import cn.tianjin.unifiedfee.ot.service.CategoryService;
import cn.tianjin.unifiedfee.ot.util.HttpPush;

@RequestMapping("/cate")
@Controller
public class CateController extends BaseController{
    @Autowired
    private CategoryService categoryService;

    @RequestMapping("get")
    @ResponseBody
    public Map<String, Object> get(String id, HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> retMap=new HashMap<String, Object>();
        try {
            UserInfo ui=this.getUserInfo();
            if (ui == null) {
                retMap.put("returnCode", "02");
                retMap.put("messageInfo", "无用户登录");
                return retMap;
            }
            if (StringUtils.isBlank(id)) {
                retMap.put("returnCode", "03");
                retMap.put("messageInfo", "分类id为空，无法处理");
                return retMap;
            }
            TreeNode<CategoryNode> node=(TreeNode<CategoryNode>)categoryService.getCategoryNodeById(id);
            if (node==null) {
                retMap.put("returnCode", "04");
                retMap.put("messageInfo", "分类id无对应分类，无法处理");
            } else {
                retMap.put("returnCode", "00");
                Map<String, Object> dataM=node.getTnEntity().toHashMap();
                CategoryNode cn=node.getTnEntity();
                Date d=new Date(cn.getCreateDate().getTime());
                dataM.put("createDate", DateUtils.convert2LongLocalStr(d));
                d=new Date(cn.getUpdateDate().getTime());
                dataM.put("updateDate", DateUtils.convert2LongLocalStr(d));
                String upperName=node.getTreePathName("-", 0);
                //if (upperName.indexOf("-")!=-1) upperName=upperName.substring(0, upperName.indexOf("-"));
                dataM.put("pathName", upperName);
                retMap.put("data", dataM);
            }
        } catch (Exception e) {
            e.printStackTrace();
            retMap.put("returnCode", "01");
            retMap.put("messageInfo", e.toString());
        }
        return retMap;
    }

    @RequestMapping("insert")
    @ResponseBody
    public Map<String, Object> save(Category cate, HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> retMap=new HashMap<String, Object>();
        try {
            UserInfo ui=this.getUserInfo();
            if (ui == null) {
                retMap.put("returnCode", "02");
                retMap.put("messageInfo", "无用户登录");
                return retMap;
            }
            retMap=categoryService.save(cate, ui);
        } catch (Exception e) {
            e.printStackTrace();
            retMap.put("returnCode", "01");
            retMap.put("messageInfo", e.toString());
        }
        return retMap;
    }
    
    @RequestMapping("changeValid")
    @ResponseBody
    public Map<String, Object> changeValid(HttpServletRequest request, HttpServletResponse response,
            @RequestParam(required=false, value="id") String id,
            @RequestParam(required=false, value="valid", defaultValue="-1") int valid) {
        Map<String, Object> retMap=new HashMap<String, Object>();
        try {
            UserInfo ui=this.getUserInfo();
            if (ui == null) {
                retMap.put("returnCode", "02");
                retMap.put("messageInfo", "无用户登录");
                return retMap;
            }
            if (StringUtils.isBlank(id)) {
                retMap.put("returnCode", "03");
                retMap.put("messageInfo", "分类id为空，无法处理");
                return retMap;
            }
            if (valid==-1) {
                retMap.put("returnCode", "05");
                retMap.put("messageInfo", "状态类型无法获得");
                return retMap;
            }
            retMap=categoryService.changeValid(id, valid);
        } catch (Exception e) {
            e.printStackTrace();
            retMap.put("returnCode", "01");
            retMap.put("messageInfo", e.toString());
        }
        return retMap;
    }
    
    @SuppressWarnings("unchecked")
    @RequestMapping("getPageData")
    @ResponseBody
    public Map<String, Object> getPageData(Category cate,
            @RequestParam(value="offset", defaultValue="1") int offset,
            @RequestParam(value="limit", defaultValue="10") int limit, 
            @RequestParam(required=false) String date1,
            @RequestParam(required=false) String date2,
            HttpServletRequest request, HttpServletResponse response) {
        // 跨域
        HttpPush.responseInfo(response);

        Map<String, Object> map=new HashMap<String, Object>();
        Map<String, Object> param=new HashMap<String, Object>();
        param.put("keyword", request.getParameter("keyword"));
        param.put("createName", request.getParameter("createName"));
        param.put("cateName", request.getParameter("cateName"));
        param.put("parentId", request.getParameter("parentId"));
        param.put("date1", date1);//开始时间
        param.put("date2", date2);//结束时间
        String tmpStr=request.getParameter("valid");
        if (!StringUtils.isBlank(tmpStr)&&(!"1,0".equals(tmpStr))&&(!"0,1".equals(tmpStr))) {
            param.put("valid", tmpStr);
        }
        //设置page
        PageHelper.offsetPage(offset, limit);
        //查询数据
        List<Map<String, Object>> cl=categoryService.getPageData(param);
        PageInfo<Map<String, Object>> pageList=new PageInfo<Map<String, Object>>(cl);

        Map<String, Object> m=categoryService.getTreeData();
        List<Object> list=(List<Object>)m.get("DataList");
        if (cl!=null) {
            List<Map<String,Object>> cList=new ArrayList<Map<String,Object>>();
            for (Object _ct: list) {
                TreeNode<CategoryNode> cn=(TreeNode<CategoryNode>)_ct;
                for (Map<String,Object> _m: cl) {
                    if (_m.get("ID").equals(cn.getId())) {
                        Map<String ,Object> newData=new HashMap<String, Object>();
                        newData.put("id", (String)_m.get("ID"));
                        newData.put("name", (String)_m.get("NAME"));
                        newData.put("desc", (String)_m.get("REMARKS"));
                        newData.put("parentId", "0".equals((String)_m.get("PARENT_ID"))?null:(String)_m.get("PARENT_ID"));
                        String pn="";
                        TreeNode<CategoryNode> node=(TreeNode<CategoryNode>)categoryService.getCategoryNodeById((String)_m.get("ID"));
                        if (node!=null) {
                            TreeNode<? extends TreeNodeBean> pNode=node.getParent();
                            if (pNode.isRoot()) pn="根";
                            else pn=node.getParent().getTreePathName("-", 0);
                        }
                        newData.put("parentName", pn);
                        newData.put("sort", Integer.parseInt(""+_m.get("SORT")));
                        newData.put("valid", (Integer.parseInt(""+_m.get("ISVALID"))));
                        newData.put("createId", (String)_m.get("CREATE_ID"));
                        newData.put("createName", (String)_m.get("CREATE_NAME"));
                        newData.put("updateId", (String)_m.get("UPDATE_ID"));
                        newData.put("updateName", (String)_m.get("UPDATE_NAME"));
                        Date tmp=null;
                        if (null!=_m.get("CREATE_DATE")) {
                            tmp=new java.sql.Date(((Timestamp)_m.get("CREATE_DATE")).getTime());
                            newData.put("createDate", DateUtils.convert2LongLocalStr(tmp));
                        }
                        if (null!=_m.get("UPDATE_DATE")) {
                            tmp=new java.sql.Date(((Timestamp)_m.get("UPDATE_DATE")).getTime());
                            newData.put("updateDate", DateUtils.convert2LongLocalStr(tmp));
                        }
                        cList.add(newData);
                        break;
                    }
                }
            }
            map.put("total", pageList.getTotal());
            map.put("rows", cList);
        }
        return map;
    }
}
