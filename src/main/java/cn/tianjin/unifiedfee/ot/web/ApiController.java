package cn.tianjin.unifiedfee.ot.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.github.pagehelper.PageHelper;
import com.spiritdata.framework.core.model.tree.TreeNode;
import com.spiritdata.framework.core.model.tree.TreeNodeBean;
import com.spiritdata.framework.util.TreeUtils;

import cn.taiji.oauthbean.dto.UserInfo;
import cn.taiji.web.security.UserService;
import cn.tianjin.unifiedfee.ot.entity.Kj;
import cn.tianjin.unifiedfee.ot.model.CategoryNode;
import cn.tianjin.unifiedfee.ot.service.CatagoryService;
import cn.tianjin.unifiedfee.ot.service.KjService;
import cn.tianjin.unifiedfee.ot.util.HttpPush;

@Controller
@RequestMapping("/train")
public class ApiController {
    //@Value("${page.web.pagesize.default}")
    private int _DEFALT_PS=10;//default page size
    @Autowired
    private CatagoryService categoryService;
    @Autowired // 注入Service
    public UserService userService;
    @Autowired
    private KjService kjService;
    @Autowired
    private TmService tmService;

//    @RequestMapping("getCateData")
//    @ResponseBody
//    public Map<String, Object> getCateData(HttpServletRequest request, HttpServletResponse response,
//        @RequestParam(required=false) String categoryId) {
//        HttpPush.responseInfo(response);//跨域
//
//        Map<String, Object> retMap=new HashMap<String, Object>();
//        try {
//            UserInfo ui=userService.getUserInfo();
//            if (ui==null) {
//                retMap.put("returnCode","02");
//                retMap.put("messageInfo","无用户登录");
//                return retMap;
//            }
//        } catch(Exception e) {
//            e.printStackTrace();
//            retMap.put("returnCode","01");
//            retMap.put("messageInfo",e.toString());
//        }
//        return retMap;
//    }

    /**
     * 1.1.1、获得分类信息<br>
     * 获得分类信息，以列表的形式或以树的形式，以列表形式无分页功能
     * @param request
     * @param response
     * @param categoryId 上级分类，若为空，获得所有分类
     * @param resultType =0列表形式；=1树形式，默认为0
     * @return
     */
    @RequestMapping("getCateData")
    @ResponseBody
    public Map<String, Object> getCateData(HttpServletRequest request, HttpServletResponse response,
        @RequestParam(required=false) String categoryId,
        @RequestParam(defaultValue="list",required=false) String resultType) {
        HttpPush.responseInfo(response);//跨域

        Map<String, Object> retMap=new HashMap<String, Object>();
        try {
            UserInfo ui=userService.getUserInfo();
            if (ui==null) {
                retMap.put("returnCode","02");
                retMap.put("messageInfo","无用户登录");
                return retMap;
            }
            TreeNode<CategoryNode> c=categoryService.getCategoryNodeById(categoryId);
            if (c==null) {
                retMap.put("returnCode","03");
                retMap.put("messageInfo","分类Id无对应分类");
                return retMap;
            }
            Map<String, Object> data=new HashMap<String, Object>();
            if ("tree".equals(resultType)) {
                Map<String, Object> retTree=new HashMap<String, Object>();
                retTree.put("id", c.getId());
                retTree.put("name", c.getTreePathName("-", 0));
                if (!c.isLeaf()&&c.getChildCount()>0) {
                    List<Map<String, Object>> new_cl=new ArrayList<Map<String, Object>>();
                    for (TreeNode<? extends TreeNodeBean> _c:c.getChildren()) {
                        new_cl.add(_toTreeMap(_c));
                    }
                    retTree.put("children", new_cl);
                }
                data.put("resultType", "tree");
                data.put("tree", retTree);
            } else {
                List<Map<String, Object>> retList=new ArrayList<Map<String, Object>>();
                Map<String, Object> one=null;//new HashMap<String, Object>();
                List<TreeNode<? extends TreeNodeBean>> l=TreeUtils.getDeepList(c);
                for (TreeNode<? extends TreeNodeBean> _n: l) {
                    one=new HashMap<String, Object>();
                    one.put("id", _n.getId());
                    one.put("name", _n.getTreePathName("-", 0));
                    one.put("parentId", _n.getParentId());
                    retList.add(one);
                }
                data.put("resultType", "list");
                data.put("list", retList);
            }
            retMap.put("returnCode", "00");
            retMap.put("data", data);
        } catch(Exception e) {
            e.printStackTrace();
            retMap.put("returnCode","01");
            retMap.put("messageInfo",e.toString());
        }
        return retMap;
    }
    private Map<String, Object> _toTreeMap(TreeNode<? extends TreeNodeBean> treeNode) {
        Map<String, Object> treeMap=new HashMap<String, Object>();
        treeMap.put("id", treeNode.getId());
        treeMap.put("name", treeNode.getTreePathName("-", 0));
        if (!treeNode.isLeaf()&&treeNode.getChildCount()>0) {
            List<Map<String, Object>> new_cl=new ArrayList<Map<String, Object>>();
            for (TreeNode<? extends TreeNodeBean> _c:treeNode.getChildren()) {
                new_cl.add(_toTreeMap(_c));
            }
            treeMap.put("children", new_cl);
        }
        return treeMap;
    }

    /**
     * 1.2.1、获得分类信息<br>
     * 获得分类信息，以列表的形式或以树的形式，以列表形式无分页功能
     * @param request
     * @param response
     * @param categoryId 上级分类，若为空，获得所有分类
     * @param resultType =0列表形式；=1树形式，默认为0
     * @return
     */
    @RequestMapping("getKjList")
    @ResponseBody
    public Map<String, Object> getKjList(HttpServletRequest request, HttpServletResponse response,
        @RequestParam(required=false) String categoryId,
        @RequestParam(required=false) String searchStr,
        @RequestParam(defaultValue="-1",required=false) int pageNo,
        @RequestParam(defaultValue="-1",required=false) int pageSize) {
        HttpPush.responseInfo(response);//跨域

        Map<String, Object> retMap=new HashMap<String, Object>();
        try {
            UserInfo ui=userService.getUserInfo();
            if (ui==null) {
                retMap.put("returnCode","02");
                retMap.put("messageInfo","无用户登录");
                return retMap;
            }
            //处理分页
            if (pageNo==-1) pageNo=1;
            if (pageSize==-1) pageSize=_DEFALT_PS;
            //处理分类
            String flQuery="";
            if (!StringUtils.isBlank(categoryId)) {
                TreeNode<CategoryNode> c=categoryService.getCategoryNodeById(categoryId);
                if (c==null) {
                    retMap.put("returnCode","03");
                    retMap.put("messageInfo","分类Id无对应分类");
                    return retMap;
                }
                List<TreeNode<? extends TreeNodeBean>> l=TreeUtils.getDeepList(c);
                for (TreeNode<? extends TreeNodeBean> _n: l) {
                    flQuery+=" or kj_cat_id='"+_n.getId()+"'";
                }
            }
            if (!StringUtils.isBlank(flQuery)) flQuery=flQuery.substring(4);
            // 设置page
            PageHelper.offsetPage(pageNo,pageSize);
            // 获取参数
            Map<String, Object> param=new HashMap<String, Object>();
            param.put("flQuery", flQuery);
            param.put("searchStr", searchStr);
            // 查询数据
            List<Kj> kjs=kjService.find4Web(param);
            if (kjs==null||kjs.size()==0) {
                retMap.put("returnCode","99");
                retMap.put("messageInfo","列表为空");
            } else {
                retMap.put("returnCode","00");
                retMap.put("data",kjs);
            }
        } catch(Exception e) {
            e.printStackTrace();
            retMap.put("returnCode","01");
            retMap.put("messageInfo",e.toString());
        }
        return retMap;
    }

    /**
     * 1.4.1、获得临时试卷<br>
     * 随机获得某一课件或模拟实操相对应的试题。
     * @param request
     * @param response
     * @param refType 上级分类，若为空，获得所有分类
     * @param refType 上级分类，若为空，获得所有分类
     * @param resultType =0列表形式；=1树形式，默认为0
     * @return
     */
    @RequestMapping("getKjInfo")
    @ResponseBody
    public Map<String, Object> getKjInfo(HttpServletRequest request, HttpServletResponse response,
        @RequestParam(required=false) String refType,
        @RequestParam(required=false) String refId,
        @RequestParam(defaultValue="5",required=false) int tmCount) {
        HttpPush.responseInfo(response);//跨域

        Map<String, Object> retMap=new HashMap<String, Object>();
        try {
            UserInfo ui=userService.getUserInfo();
            if (ui==null) {
                retMap.put("returnCode","02");
                retMap.put("messageInfo","无用户登录");
                return retMap;
            }
            //1-处理参数
            if (StringUtils.isBlank(refType)) {
                retMap.put("returnCode","03");
                retMap.put("messageInfo","相关对象类型为空");
                return retMap;
            }
            if (StringUtils.isBlank(refId)) {
                retMap.put("returnCode","03");
                retMap.put("messageInfo","相关对象Id为空");
                return retMap;
            }
            List<Tm> tmpSjTmList=tmService.getTempSj(retType, retId, tmCount);
        } catch(Exception e) {
            e.printStackTrace();
            retMap.put("returnCode","01");
            retMap.put("messageInfo",e.toString());
        }
        return retMap;
    }
}