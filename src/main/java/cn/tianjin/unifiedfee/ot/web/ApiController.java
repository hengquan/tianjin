package cn.tianjin.unifiedfee.ot.web;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
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

//import com.github.pagehelper.PageHelper;
import com.spiritdata.framework.core.model.tree.TreeNode;
import com.spiritdata.framework.core.model.tree.TreeNodeBean;
import com.spiritdata.framework.util.DateUtils;
import com.spiritdata.framework.util.RequestUtils;
import com.spiritdata.framework.util.SequenceUUID;
import com.spiritdata.framework.util.TreeUtils;

import cn.taiji.company.remote.SystemCompanyRemote;
import cn.taiji.company.system.CompanyInfo;
import cn.taiji.format.result.ObjectResponseResult;
import cn.taiji.oauthbean.dto.UserInfo;
import cn.taiji.web.security.UserService;
import cn.tianjin.unifiedfee.ot.entity.CommArchive;
import cn.tianjin.unifiedfee.ot.entity.Kj;
import cn.tianjin.unifiedfee.ot.entity.LogVisit;
import cn.tianjin.unifiedfee.ot.entity.SJ;
import cn.tianjin.unifiedfee.ot.logvisit.LogVisitMemory;
import cn.tianjin.unifiedfee.ot.logvisit.service.LogVisitService;
import cn.tianjin.unifiedfee.ot.model.CategoryNode;
import cn.tianjin.unifiedfee.ot.service.ArchiveService;
import cn.tianjin.unifiedfee.ot.service.CategoryService;
import cn.tianjin.unifiedfee.ot.service.KjService;
import cn.tianjin.unifiedfee.ot.service.SjService;
import cn.tianjin.unifiedfee.ot.util.HttpPush;

@Controller
@RequestMapping("/train")
public class ApiController {
    //@Value("${page.web.pagesize.default}")
//    private int _DEFALT_PS=10;//default page size
    @Autowired
    private CategoryService categoryService;
    @Autowired
    public UserService userService;
    @Autowired
    private KjService kjService;
    @Autowired
    private SjService sjService;
    @Autowired
    private ArchiveService archiveService;
    @Autowired
    public SystemCompanyRemote companyRemote;
    @Autowired
    public LogVisitService logVisitService;

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
     * 1.1.0、获得分类树<br>
     * 获得分类信息，以列表的形式或以树的形式，以列表形式无分页功能
     * @param request
     * @param response
     * @param categoryId 上级分类，若为空，获得所有分类
     * @param type =0仅取有效分类；=1取所有分类，默认为0，仅取有效分类
     * @return
     */
    @RequestMapping("/getTree/ptTree")
    @ResponseBody
    public Map<String, Object> getCateTree_ptTree(HttpServletRequest request, HttpServletResponse response,
        @RequestParam(required=false) String categoryId,
        @RequestParam(defaultValue="0",required=false) int type) {
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
            Map<String, Object> retTree=_toTreeMap_ptTree(c, type);
            data.put("tree", retTree);
            retMap.put("returnCode", "00");
            retMap.put("data", data);
        } catch(Exception e) {
            e.printStackTrace();
            retMap.put("returnCode","01");
            retMap.put("messageInfo",e.toString());
        }
        return retMap;
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
                        if (((CategoryNode)(_c.getTnEntity())).getIsvalid()==1) {
                            new_cl.add(_toTreeMap(_c));
                        }
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
                    if (((CategoryNode)(_n.getTnEntity())).getIsvalid()==1) {
                        one=new HashMap<String, Object>();
                        one.put("id", _n.getId());
                        one.put("name", _n.getTreePathName("-", 0));
                        one.put("parentId", _n.getParentId());
                        retList.add(one);
                    }
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
            //先不分页
            /*
            //处理分页
            if (pageNo==0) pageNo=1;
            if (pageSize==-1) pageSize=100;
            // 设置page，若pageNo=-1,则不分页
            if (pageNo!=-1) PageHelper.offsetPage(pageNo,pageSize);
            */
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
            // 获取参数
            Map<String, Object> param=new HashMap<String, Object>();
            param.put("flQuery", flQuery);
            if (!StringUtils.isBlank(searchStr)) searchStr=searchStr.replaceAll("'", "");
            param.put("searchStr", searchStr);
            // 查询数据
            List<Map<String, Object>> kjs=kjService.find4Web(param);
            if (kjs==null||kjs.size()==0) {
                retMap.put("returnCode","99");
                retMap.put("messageInfo","列表为空");
                return retMap;
            } else {
                List<Map<String, Object>> retL=new ArrayList<Map<String, Object>>();
                for (int i=0; i<kjs.size(); i++) {
                    Map<String, Object> one=getKjMap(kjs.get(i));
                    retL.add(one);
                }
                retMap.put("returnCode", "00");
                retMap.put("data", retL);
            }
        } catch(Exception e) {
            e.printStackTrace();
            retMap.put("returnCode","01");
            retMap.put("messageInfo",e.toString());
        }
        return retMap;
    }
    private Map<String, Object> getKjMap(Map<String, Object> kjm) {
        Map<String, Object> km=new HashMap<String, Object>();
        km.put("id", kjm.get("ID"));
        km.put("catNames", kjm.get("KJ_CAT_NAMES"));
        km.put("name", kjm.get("KJ_NAME"));
        km.put("remarks", kjm.get("REMARKS"));
        try {
            km.put("createDate", DateUtils.convert2LocalStr("yyyy-MM-dd HH:mm:ss", new Date(((Timestamp)kjm.get("CREATE_DATE")).getTime())));
        } catch(Exception e) {
        }
        km.put("visitCount", kjm.get("LOGVISITCOUNT"));
        return km;
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
                    m.put("fileName", ca.getFileName());
                }
            }
            if (m.get("imgUrl")==null) {//默认图片
//              m.put("imgUrl", "/images/defaultKJ.png");
                m.put("imgUrl", "");
            }
            if (m.get("kjUrl")==null) m.put("kjUrl", "");
            if (m.get("fileName")==null) m.put("fileName", "");
            //获得课件访问量
            int logVisitCount=logVisitService.getVisitCount("ts_kj", kjId);
            m.put("logVisitCount", logVisitCount);
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
                retMap.put("data", retL);
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
     * @param refType 相关类型，mnsc or kj
     * @param refId 相关对象Id，模拟实操或课件的Id
     * @param tmCount 题目数量，默认为10
     * @return
     */
    @RequestMapping("getTempSj")
    @ResponseBody
    public Map<String, Object> getTempSj(HttpServletRequest request, HttpServletResponse response,
        @RequestParam(required=false) String refType,
        @RequestParam(required=false) String refId,
        @RequestParam(defaultValue="1",required=false) int tmCount) {
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
            if (tmpSjTmList==null||tmpSjTmList.size()==0) {
                retMap.put("returnCode","99");
                retMap.put("messageInfo","列表为空");
            } else {
                retMap.put("returnCode","00");
                retMap.put("data", tmpSjTmList);
            }
        } catch(Exception e) {
            e.printStackTrace();
            retMap.put("returnCode","01");
            retMap.put("messageInfo",e.toString());
        }
        return retMap;
    }

    /**
     * 1.4.2、获得临时试卷<br>
     * 随机获得某一课件或模拟实操相对应的试题。
     * @param request
     * @param response
     * @param cateIds 分类，可多选
     * @param refType 相关类型，mnsc or kj
     * @param refId 相关对象Id，模拟实操或课件的Id
     * @param diffRange 难度范围，从1到10，逗号隔开
     * @param tmCount 题目数量，默认为10
     * @return
     */
    @RequestMapping("getSj")
    @ResponseBody
    public Map<String, Object> getSj(HttpServletRequest request, HttpServletResponse response,
        @RequestParam(required=false) String cateIds,
        @RequestParam(required=false) String refType,
        @RequestParam(required=false) String refId,
        @RequestParam(required=false) String diffRange,
        @RequestParam(defaultValue="10",required=false) int tmCount) {
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
//            if (StringUtils.isBlank(cateIds)) {//若分类为空，则相关对象必须有
//                if (StringUtils.isBlank(refType)) {
//                    retMap.put("returnCode","03");
//                    retMap.put("messageInfo","相关对象类型为空");
//                    return retMap;
//                }
//                if (StringUtils.isBlank(refId)) {
//                    retMap.put("returnCode","03");
//                    retMap.put("messageInfo","相关对象Id为空");
//                    return retMap;
//                }
//            }
            //2-处理难度系数
            int diff1=0, diff2=9;
            if (!StringUtils.isBlank(diffRange)) {
                String[] sp=diffRange.split(",");
                if (sp.length==1) {
                    try {diff2=Integer.parseInt(sp[0].trim());} catch(Exception e) {}
                } else {
                    try {diff1=Integer.parseInt(sp[0].trim());} catch(Exception e) {};
                    try {diff2=Integer.parseInt(sp[1].trim());} catch(Exception e) {};
                    if (diff2<diff1) {
                        int _i=diff2;
                        diff2=diff1;
                        diff1=_i;
                    }
                }
            }
            Map<String,Object> sjInfo=sjService.getSj(cateIds, refType, refId, diff1, diff2, tmCount, ui);
            if (sjInfo==null) {
                retMap.put("returnCode","99");
                retMap.put("messageInfo","试卷为空");
            } else {
                retMap.put("returnCode","00");
                retMap.put("data", sjInfo);
                //这里要向日志表中写一条记录
                LogVisit lv=new LogVisit();
                lv.setId(SequenceUUID.getPureUUID());
                lv.setVisitorId(ui.getUserId());
                lv.setVisitorType("1");
                lv.setVisitorName(ui.getUsername());
                if (StringUtils.isBlank(lv.getServSysType())) lv.setServSysType("009");
                if (StringUtils.isBlank(lv.getServSysId())) lv.setServSysId("1");
                if (StringUtils.isBlank(lv.getVisitSysType())) lv.setVisitSysType("009");
                if (StringUtils.isBlank(lv.getVisitSysId())) lv.setVisitSysId("1");
                ObjectResponseResult<CompanyInfo> companyInfo=companyRemote.findCompanyBankInfoList(ui.getUserId());
                if (companyInfo!=null&&companyInfo.getData()!=null) {
                    lv.setGroupId(companyInfo.getData().getUniscid());
                    lv.setGroupName(companyInfo.getData().getEntname());
                }
                lv.setVisitModuleId("在线练习");
                lv.setObjId(""+sjInfo.get("id"));
                lv.setObjType("q_sj");
                LogVisitMemory.getInstance().put2Queue(lv);
            }
        } catch(Exception e) {
            e.printStackTrace();
            retMap.put("returnCode","01");
            retMap.put("messageInfo",e.toString());
        }
        return retMap;
    }

    /**
     * 1.4.3、提交试卷<br>
     * 学员答题后，提交试卷的答案。
     * @param request
     * @param response
     * @param id 试卷Id
     * @param beginTime 相关类型，mnsc or kj
     * @param endTime 相关对象Id，模拟实操或课件的Id
     * @param resultType 返回类型：=0仅返回分数，=1返回答案，默认1
     * @param tmCount 题目数量，默认为10
     * @return
     */
    @RequestMapping("commitSj")
    @ResponseBody
    public Map<String, Object> commitSj(HttpServletRequest request, HttpServletResponse response,
        @RequestParam(required=false) String id,
        @RequestParam(required=false) String beginTime,
        @RequestParam(required=false) String endTime,
        @RequestParam(defaultValue="1",required=false) int resultType,
        @RequestParam(required=false) String answers) {
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
            if (StringUtils.isBlank(id)) {//若分类为空，则相关对象必须有
                retMap.put("returnCode","03");
                retMap.put("messageInfo","试卷Id为空");
                return retMap;
            }
            SJ sj=sjService.getSjById(id);
            if (sj==null) {
                retMap.put("returnCode","04");
                retMap.put("messageInfo","试卷Id无对应试卷");
                return retMap;
            }
            Map<String, Object> commitSjResult=sjService.commitSj(sj, answers, resultType, beginTime, endTime);
            if (commitSjResult==null) {
                retMap.put("returnCode","99");
                retMap.put("messageInfo","无法处理");
            } else {
                retMap.put("returnCode","00");
                retMap.put("data", commitSjResult);
            }
        } catch(Exception e) {
            e.printStackTrace();
            retMap.put("returnCode","01");
            retMap.put("messageInfo",e.toString());
        }
        return retMap;
    }

    /**
     * 1.4.4、获得答题完毕的试卷内容<br>
     * @param request
     * @param response
     * @param id 试卷Id
     * @return
     */
    @RequestMapping("showSj")
    @ResponseBody
    public Map<String, Object> showSj(HttpServletRequest request, HttpServletResponse response,
        @RequestParam(required=false) String id) {
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
            if (StringUtils.isBlank(id)) {//若分类为空，则相关对象必须有
                retMap.put("returnCode","03");
                retMap.put("messageInfo","试卷Id为空");
                return retMap;
            }
            SJ sj=sjService.getSjById(id);
            if (sj==null) {
                retMap.put("returnCode","04");
                retMap.put("messageInfo","试卷Id无对应试卷");
                return retMap;
            }
            Map<String, Object> sjResult=sjService.getSj4Show(sj);
            if (sjResult==null) {
                retMap.put("returnCode","05");
                retMap.put("messageInfo","试卷信息为空");
            } else {
                retMap.put("returnCode","00");
                retMap.put("data", sjResult);
            }
        } catch(Exception e) {
            e.printStackTrace();
            retMap.put("returnCode","01");
            retMap.put("messageInfo",e.toString());
        }
        return retMap;
    }

    /**
     * 1.5.1、数据收集<br>
     * 学员答题后，提交试卷的答案。
     * @param request
     * @param response
     * @param id 试卷Id
     * @param beginTime 相关类型，mnsc or kj
     * @param endTime 相关对象Id，模拟实操或课件的Id
     * @param resultType 返回类型：=0仅返回分数，=1返回答案，默认1
     * @param tmCount 题目数量，默认为10
     * @return
     */
    @RequestMapping("gatherData")
    @ResponseBody
    public Map<String, Object> gatherData(HttpServletRequest request, HttpServletResponse response) {
        HttpPush.responseInfo(response);//跨域
        Map<String, Object> retMap=new HashMap<String, Object>();
        try {
            UserInfo ui=userService.getUserInfo();
            if (ui!=null) {
                LogVisit lv=getFromRequest(request);
                lv.setId(SequenceUUID.getPureUUID());
                lv.setVisitorId(ui.getUserId());
                lv.setVisitorType("1");
                lv.setVisitorName(ui.getUsername());
                if (StringUtils.isBlank(lv.getServSysType())) lv.setServSysType("009");
                if (StringUtils.isBlank(lv.getServSysId())) lv.setServSysId("1");
                if (StringUtils.isBlank(lv.getVisitSysType())) lv.setVisitSysType("009");
                if (StringUtils.isBlank(lv.getVisitSysId())) lv.setVisitSysId("1");

                ObjectResponseResult<CompanyInfo> companyInfo=companyRemote.findCompanyBankInfoList(ui.getUserId());
                if (companyInfo!=null&&companyInfo.getData()!=null&&!"无企业信息".equals(companyInfo.getMsg())) {
                    lv.setGroupId(companyInfo.getData().getUniscid());
                    lv.setGroupName(companyInfo.getData().getEntname());
                }
                if (!StringUtils.isBlank(lv.getObjType())) {
                    LogVisitMemory.getInstance().put2Queue(lv);
                }
            }
            retMap.put("returnCode", "000");
        } catch(Exception e) {
            retMap.put("returnCode", "001");
            retMap.put("messageInfo",e.toString());
        }
        return retMap;
    }
    private LogVisit getFromRequest(HttpServletRequest request) {
        Map<String, Object> mm=RequestUtils.getDataFromRequest(request);
        LogVisit lv=new LogVisit();
        lv.fromHashMap(mm);
        return lv;
    }
}