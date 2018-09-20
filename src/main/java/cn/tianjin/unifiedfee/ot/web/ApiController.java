package cn.tianjin.unifiedfee.ot.web;

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
import com.spiritdata.framework.core.model.tree.TreeNode;
import com.spiritdata.framework.core.model.tree.TreeNodeBean;
import com.spiritdata.framework.util.DateUtils;
import com.spiritdata.framework.util.TreeUtils;

import cn.taiji.oauthbean.dto.UserInfo;
import cn.taiji.web.security.UserService;
import cn.tianjin.unifiedfee.ot.entity.CommArchive;
import cn.tianjin.unifiedfee.ot.entity.Kj;
import cn.tianjin.unifiedfee.ot.model.CategoryNode;
import cn.tianjin.unifiedfee.ot.service.ArchiveService;
import cn.tianjin.unifiedfee.ot.service.CatagoryService;
import cn.tianjin.unifiedfee.ot.service.KjService;
import cn.tianjin.unifiedfee.ot.service.SjService;
import cn.tianjin.unifiedfee.ot.entity.Tm;
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
    private SjService sjService;
    @Autowired
    private ArchiveService archiveService;

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
     * @param searchStr 搜索串
     * @param pageNo 页码
     * @param pageSize 每页份数
     * @return
     */
    @RequestMapping("getKjList")
    @ResponseBody
    public Map<String, Object> getKjList(HttpServletRequest request, HttpServletResponse response,
        @RequestParam(required=false) String categoryId,
        @RequestParam(required=false) String searchStr,
        @RequestParam(defaultValue="0",required=false) int pageNo,
        @RequestParam(defaultValue="10",required=false) int pageSize) {
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
            if (pageNo==0) pageNo=1;
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
                flQuery+=" or kj_cat_id='"+c.getId()+"'";
                List<TreeNode<? extends TreeNodeBean>> l=TreeUtils.getDeepList(c);
                if (l!=null&&l.size()>0) {
                    for (TreeNode<? extends TreeNodeBean> _n: l) {
                        flQuery+=" or kj_cat_id='"+_n.getId()+"'";
                    }
                }
            }
            if (!StringUtils.isBlank(flQuery)) flQuery=flQuery.substring(4);
            // 设置page，若pageNo=-1,则不分页
            if (pageNo!=-1) PageHelper.offsetPage(pageNo,pageSize);
            // 获取参数
            Map<String, Object> param=new HashMap<String, Object>();
            param.put("flQuery", flQuery);
            if (!StringUtils.isBlank(searchStr)) searchStr=searchStr.replaceAll("'", "");
            param.put("searchStr", searchStr);
            // 查询数据
            List<Kj> kjs=kjService.find4Web(param);
            if (kjs==null||kjs.size()==0) {
                retMap.put("returnCode","99");
                retMap.put("messageInfo","列表为空");
                return retMap;
            }
            //查询相关图片
            String orSql="";
            for (Kj kj: kjs) {
                orSql+=" or obj_id='"+kj.getId()+"'";
            }
            if (!StringUtils.isBlank(orSql)) orSql=orSql.substring(4);
            List<CommArchive> al=archiveService.getArchiveByObjIds("ts_KJ", "img", orSql);
            List<Map<String, Object>> retL=new ArrayList<Map<String, Object>>();
            for (Kj kj:kjs) {
                Map<String, Object> m=_getKjMap(kj);
                if (al!=null) {
                    for (CommArchive ca: al) {
                        if (ca.getObjId().equals(kj.getId())) {
                            m.put("imgUrl", ca.getFileUrl());
                        }
                    }
                }
                if (m.get("imgUrl")==null) {//默认图片
//                    m.put("imgUrl", "/images/defaultKJ.png");
                    m.put("imgUrl", "");
                }
                retL.add(m);
            }
            retMap.put("returnCode","00");
            retMap.put("data",retL);
        } catch(Exception e) {
            e.printStackTrace();
            retMap.put("returnCode","01");
            retMap.put("messageInfo",e.toString());
        }
        return retMap;
    }
    private Map<String, Object> _getKjMap(Kj kj) {
        Map<String, Object> m=new HashMap<String, Object>();
        m.put("id", kj.getId());
        m.put("catNames", kj.getKjCatNames());
        m.put("name", kj.getKjName());
        m.put("remarks", kj.getRemarks());
        m.put("createDate", DateUtils.convert2LocalStr("yyyy-MM-dd HH:mm:ss", kj.getCreateDate()));
        return m;
    }
    private Map<String, Object> _getKjMapDetail(Kj kj) {
        Map<String, Object> m=_getKjMap(kj);
        m.put("score", kj.getScore());
        m.put("createName", kj.getCreateName());
        return m;
    }

    /**
     * 1.2.2、获得课件详细信息<br>
     * 获得课件信息，包括所有的附件
     * @param request
     * @param response
     * @param kjId 课件Id
     * @return
     */
    @RequestMapping("getKjInfo")
    @ResponseBody
    public Map<String, Object> getKjInfo(HttpServletRequest request, HttpServletResponse response,
        @RequestParam(required=false) String kjId) {
        HttpPush.responseInfo(response);//跨域

        Map<String, Object> retMap=new HashMap<String, Object>();
        try {
            UserInfo ui=userService.getUserInfo();
            if (ui==null) {
                retMap.put("returnCode","02");
                retMap.put("messageInfo","无用户登录");
                return retMap;
            }
            if (StringUtils.isBlank(kjId)) {
                retMap.put("returnCode","03");
                retMap.put("messageInfo","课件Id为空");
                return retMap;
            }

            Kj kj=new Kj();
            kj.setId(kjId);
            kj.setState(2);//正式课件
            kj=kjService.get(kj);
            if (kj==null) {
                retMap.put("returnCode","04");
                retMap.put("messageInfo","课件Id无对应课件");
                return retMap;
            }

            Map<String, Object> m=_getKjMapDetail(kj);
            //查询相关图片或主内容
            String orSql="obj_id='"+kj.getId()+"'";
            List<CommArchive> al=archiveService.getArchiveByObjIds("ts_KJ", "", orSql);
            if (al!=null) {
                for (CommArchive ca: al) {
                    if (ca.getObjId().equals(kj.getId())&&ca.getArchiveType().equals("img")&&m.get("imgUrl")==null) {
                        m.put("imgUrl", ca.getFileUrl());
                    }
                    if (ca.getObjId().equals(kj.getId())&&ca.getArchiveType().equals("main")&&m.get("kjUrl")==null) {
                        m.put("kjUrl", ca.getFileUrl());
                    }
                }
            }
            if (m.get("imgUrl")==null) {//默认图片
//              m.put("imgUrl", "/images/defaultKJ.png");
                m.put("imgUrl", "");
            }
            if (m.get("kjUrl")==null) m.put("kjUrl", "");
            
            retMap.put("returnCode","00");
            retMap.put("data", m);
        } catch(Exception e) {
            e.printStackTrace();
            retMap.put("returnCode","01");
            retMap.put("messageInfo",e.toString());
        }
        return retMap;
    }

    /**
     * 1.2.3、获得相关课件列表<br>
     * 获得课件相关课件列表信息
     * @param request
     * @param response
     * @param kjId 主课件Id
     * @return
     */
    @RequestMapping("getKjRefKjList")
    @ResponseBody
    public Map<String, Object> getKjRefKjList(HttpServletRequest request, HttpServletResponse response,
        @RequestParam(required=false) String kjId) {
        HttpPush.responseInfo(response);//跨域

        Map<String, Object> retMap=new HashMap<String, Object>();
        try {
            UserInfo ui=userService.getUserInfo();
            if (ui==null) {
                retMap.put("returnCode","02");
                retMap.put("messageInfo","无用户登录");
                return retMap;
            }
            if (StringUtils.isBlank(kjId)) {
                retMap.put("returnCode","03");
                retMap.put("messageInfo","课件Id为空");
                return retMap;
            }

            // 查询数据
            Map<String, Object> param=new HashMap<String, Object>();
            param.put("kjId", kjId);
            List<Kj> kjs=kjService.findRefKj4Web(param);
            //查询相关图片
            String orSql="";
            for (Kj kj: kjs) {
                orSql+=" or obj_id='"+kj.getId()+"'";
            }

            List<Map<String, Object>> retL=new ArrayList<Map<String, Object>>();
            if (!StringUtils.isBlank(orSql)) {
                orSql=orSql.substring(4);
                List<CommArchive> al=archiveService.getArchiveByObjIds("ts_KJ", "img", orSql);
                for (Kj kj:kjs) {
                    Map<String, Object> m=_getKjMap(kj);
                    if (al!=null) {
                        for (CommArchive ca: al) {
                            if (ca.getObjId().equals(kj.getId())) {
                                m.put("imgUrl", ca.getFileUrl());
                            }
                        }
                    }
                    if (m.get("imgUrl")==null) {//默认图片
//                        m.put("imgUrl", "/images/defaultKJ.png");
                        m.put("imgUrl", "");
                    }
                    retL.add(m);
                }
            }
            if (kjs==null||kjs.size()==0) {
                retMap.put("returnCode","99");
                retMap.put("messageInfo","列表为空");
            } else {
                retMap.put("returnCode","00");
                retMap.put("data",retL);
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
    @RequestMapping("getTempSj")
    @ResponseBody
    public Map<String, Object> getTempSj(HttpServletRequest request, HttpServletResponse response,
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
            List<Map<String,Object>> tmpSjTmList=sjService.getTempSj(refType, refId, tmCount);
            retMap.put("data", tmpSjTmList);
        } catch(Exception e) {
            e.printStackTrace();
            retMap.put("returnCode","01");
            retMap.put("messageInfo",e.toString());
        }
        return retMap;
    }
}