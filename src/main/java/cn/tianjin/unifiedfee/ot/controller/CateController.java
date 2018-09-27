package cn.tianjin.unifiedfee.ot.controller;

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
import com.spiritdata.framework.util.DateUtils;

import cn.taiji.oauthbean.dto.UserInfo;
import cn.taiji.web.base.controller.BaseController;
import cn.taiji.web.security.UserService;
import cn.tianjin.unifiedfee.ot.entity.Category;
import cn.tianjin.unifiedfee.ot.model.CategoryNode;
import cn.tianjin.unifiedfee.ot.service.CatagoryService;
import cn.tianjin.unifiedfee.ot.util.HttpPush;

import java.sql.Date;
import java.sql.Timestamp;

import com.spiritdata.framework.core.model.tree.TreeNodeBean;

@RequestMapping("/cate")
@Controller
public class CateController extends BaseController {
    @Autowired
    private CatagoryService categoryService;
    @Autowired // 注入Service
    public UserService userService;

    @RequestMapping("get")
    @ResponseBody
    public Map<String, Object> get(String id, HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> retMap = new HashMap<String, Object>();
        try {
            UserInfo ui = userService.getUserInfo();
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
                String upperName=node.getTreePathName("-", 0);
                if (upperName.indexOf("-")!=-1) upperName=upperName.substring(0, upperName.indexOf("-"));
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
        Map<String, Object> retMap = new HashMap<String, Object>();
        try {
            UserInfo ui = userService.getUserInfo();
            if (ui == null) {
                retMap.put("returnCode", "02");
                retMap.put("messageInfo", "无用户登录");
                return retMap;
            }
            retMap = categoryService.save(cate, ui);
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
        Map<String, Object> retMap = new HashMap<String, Object>();
        try {
            UserInfo ui = userService.getUserInfo();
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
            retMap = categoryService.changeValid(id, valid);
        } catch (Exception e) {
            e.printStackTrace();
            retMap.put("returnCode", "01");
            retMap.put("messageInfo", e.toString());
        }
        return retMap;
    }
    
    @RequestMapping("getPageData")
    @ResponseBody
    public Map<String, Object> getPageData(Category cate,
            @RequestParam(value = "offset", defaultValue = "1") int offset,
            @RequestParam(value = "limit", defaultValue = "10") int limit, HttpServletRequest request,
            HttpServletResponse response) {
        // 跨域
        HttpPush.responseInfo(response);

        Map<String, Object> map = new HashMap<String, Object>();
        // 设置page
        PageHelper.offsetPage(offset, limit);
        // 查询数据
        List<Map<String, Object>> cl=categoryService.getPageData(cate);
        PageInfo<Map<String, Object>> pageList = new PageInfo<Map<String, Object>>(cl);
        if (cl!=null) {
            //放入分页
            List<Map<String,Object>> cList=new ArrayList<Map<String,Object>>();
            for (Map<String,Object> _m: cl) {
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
                newData.put("valid", (Integer.parseInt(""+_m.get("ISVALID")))==1?"有效":"失效");
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
            }
            map.put("total", pageList.getTotal());
            map.put("rows", cList);
        }
        return map;
    }
}
