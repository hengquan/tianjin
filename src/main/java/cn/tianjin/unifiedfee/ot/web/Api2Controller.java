package cn.tianjin.unifiedfee.ot.web;

import java.util.ArrayList;
import java.util.Calendar;
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

import com.spiritdata.framework.core.model.tree.TreeNode;
import com.spiritdata.framework.core.model.tree.TreeNodeBean;
import com.spiritdata.framework.util.DateUtils;

import cn.taiji.oauthbean.dto.UserInfo;
import cn.taiji.web.security.UserService;
import cn.tianjin.unifiedfee.ot.entity.CommArchive;
import cn.tianjin.unifiedfee.ot.entity.Kj;
import cn.tianjin.unifiedfee.ot.entity.Mnsc;
import cn.tianjin.unifiedfee.ot.logvisit.service.LogVisitService;
import cn.tianjin.unifiedfee.ot.model.CategoryNode;
import cn.tianjin.unifiedfee.ot.service.ArchiveService;
import cn.tianjin.unifiedfee.ot.service.CategoryService;
import cn.tianjin.unifiedfee.ot.service.KjService;
import cn.tianjin.unifiedfee.ot.service.MnscRefSourceService;
import cn.tianjin.unifiedfee.ot.service.MnscService;
import cn.tianjin.unifiedfee.ot.util.HttpPush;

@Controller
@RequestMapping("/train")
public class Api2Controller {
    private int _DEFALT_PS = 10;// default page size
    @Autowired // 注入Service
    public UserService userService;
    @Autowired
    public CategoryService categoryService;
    @Autowired
    private MnscService mnscService;
    @Autowired
    private KjService kjService;
    @Autowired
    private MnscRefSourceService mnscRefSourceService;
    @Autowired
    private ArchiveService archiveService;
    @Autowired
    private LogVisitService logVisitService;

    /**
     * 1.3.1、获得模拟实操列表<br>
     * 获得模拟实操列表，以列表形式有分页功能
     * 
     * @param request
     * @param response
     * @param categoryId 分类ID，若为空，获得所有分类
     * @param searchStr 查询字符串，若为空，获得所有摸拟实操
     * @return
     */
    @RequestMapping("getMnscList")
    @ResponseBody
    public Map<String, Object> getMnscList(HttpServletRequest request, HttpServletResponse response,
            @RequestParam(required = false) String categoryId, @RequestParam(required = false) String searchStr,
            @RequestParam(defaultValue = "-1", required = false) int pageNo,
            @RequestParam(defaultValue = "-1", required = false) int pageSize) {
        HttpPush.responseInfo(response);// 跨域
        // 返回结果
        Map<String, Object> retMap = new HashMap<String, Object>();
        // 请求参数
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            UserInfo ui = userService.getUserInfo();
            if (ui == null) {
                retMap.put("returnCode", "02");
                retMap.put("messageInfo", "无用户登录");
                return retMap;
            }
//            // 处理分页
//            if (pageNo == -1)
//                pageNo = 1;
//            if (pageSize == -1)
//                pageSize = _DEFALT_PS;
//            // 设置分页
//            // PageHelper.offsetPage(pageNo, pageSize);
//            // 查询列表
            map.put("categoryId", categoryId);
            map.put("searchStr", searchStr);
            List<Map<String, Object>> mnscs = mnscService.getMnscList4web(map);

            if (mnscs == null || mnscs.size() == 0) {
                retMap.put("returnCode", "99");
                retMap.put("messageInfo", "列表为空");
            } else {
                List<Map<String, Object>> retL=new ArrayList<Map<String, Object>>();
                for (int i=0; i<mnscs.size(); i++) {
                    Map<String, Object> one=getMnscMap(mnscs.get(i));
                    retL.add(one);
                }
                retMap.put("returnCode", "00");
                retMap.put("data", retL);
            }
        } catch (Exception e) {
            e.printStackTrace();
            retMap.put("returnCode", "01");
            retMap.put("messageInfo", e.toString());
        }
        return retMap;
    }

    /**
     * 1.3.2、获得模拟实操详细信息<br>
     * 获得模拟实操详细信息
     * 
     * @param request
     * @param response
     * @param mnscId
     *            摸拟实操ID，不允许为空
     * @return
     */
    @RequestMapping("getMnscInfo")
    @ResponseBody
    public Map<String, Object> getMnscInfo(HttpServletRequest request, HttpServletResponse response,
            @RequestParam(required = false) String mnscId) {
        HttpPush.responseInfo(response);// 跨域
        // 返回结果
        Map<String, Object> retMap = new HashMap<String, Object>();
        try {
            UserInfo ui = userService.getUserInfo();
            if (ui == null) {
                retMap.put("returnCode", "02");
                retMap.put("messageInfo", "无用户登录");
                return retMap;
            }
            if (StringUtils.isEmpty(mnscId)) {
                retMap.put("returnCode", "03");
                retMap.put("messageInfo", "摸拟实操ID为空");
                return retMap;
            }
            // 查询
            Mnsc mnsc = mnscService.get(mnscId);
            if (mnsc == null) {
                retMap.put("returnCode", "99");
                retMap.put("messageInfo", "信息为空");
            } else {
                //获得该模拟实操的访问数量
                retMap.put("returnCode", "00");
                retMap.put("data", mnsc);
            }
        } catch (Exception e) {
            e.printStackTrace();
            retMap.put("returnCode", "01");
            retMap.put("messageInfo", e.toString());
        }
        return retMap;
    }

    /**
     * 1.3.3、获得模拟实操相关课件列表<br>
     * 获得模拟实操相关课件列表,以列表形式无分页功能
     * 
     * @param request
     * @param response
     * @param mainId 摸拟实操ID，不允许为空
     * @return
     */
    @RequestMapping("getMnscRefKjList")
    @ResponseBody
    public Map<String, Object> getMnscRefKjList(HttpServletRequest request, HttpServletResponse response,
            @RequestParam(required = false) String mnscId) {
        HttpPush.responseInfo(response);// 跨域
        // 返回结果
        Map<String, Object> retMap = new HashMap<String, Object>();
        try {
            UserInfo ui = userService.getUserInfo();
            if (ui == null) {
                retMap.put("returnCode", "02");
                retMap.put("messageInfo", "无用户登录");
                return retMap;
            }
            if (StringUtils.isEmpty(mnscId)) {
                retMap.put("returnCode", "03");
                retMap.put("messageInfo", "摸拟实操ID为空");
                return retMap;
            }
            // 查询
            List<Kj> kjList = mnscRefSourceService.getKjList(mnscId);

            if (kjList == null || kjList.size() <= 0) {
                retMap.put("returnCode", "99");
                retMap.put("messageInfo", "信息为空");
            } else {
                //查询相关图片
                String orSql="";
                for (Kj kj: kjList) {
                    orSql+=" or obj_id='"+kj.getId()+"'";
                }

                List<Map<String, Object>> retL=new ArrayList<Map<String, Object>>();
                if (!StringUtils.isBlank(orSql)) {
                    orSql=orSql.substring(4);
                    List<CommArchive> al=archiveService.getArchiveByObjIds("ts_KJ", "img", orSql);
                    for (Kj kj:kjList) {
                        Map<String, Object> m=_getKjMap(kj);
                        if (al!=null) {
                            for (CommArchive ca: al) {
                                if (ca.getObjId().equals(kj.getId())) {
                                    m.put("imgUrl", ca.getFileUrl());
                                }
                            }
                        }
                        if (m.get("imgUrl")==null) {//默认图片
//                            m.put("imgUrl", "/images/defaultKJ.png");
                            m.put("imgUrl", "");
                        }
                        retL.add(m);
                    }
                }

                retMap.put("returnCode", "00");
                retMap.put("data", retL);
            }
        } catch (Exception e) {
            e.printStackTrace();
            retMap.put("returnCode", "01");
            retMap.put("messageInfo", e.toString());
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

    /**
     * 1.6.1、获得统计访问次数<br>
     * 获得统计访问次数
     * 
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("getLogVisitCount")
    @ResponseBody
    public Map<String, Object> getLogVisitCount(HttpServletRequest request, HttpServletResponse response) {
        HttpPush.responseInfo(response);// 跨域
        // 返回结果
        Map<String, Object> retMap = new HashMap<String, Object>();
        try {
            UserInfo ui = userService.getUserInfo();
            if (ui == null) {
                retMap.put("returnCode", "02");
                retMap.put("messageInfo", "无用户登录");
                return retMap;
            }
            // 查询
            List<Map<String, Object>> countMap = logVisitService.getVisitCountByUi(ui.getUserId());
            if (countMap == null || countMap.size() <= 0) {
                retMap.put("returnCode", "00");
                retMap.put("data", "0");
            } else {
                retMap.put("returnCode", "00");
                retMap.put("data", countMap);
            }
        } catch (Exception e) {
            e.printStackTrace();
            retMap.put("returnCode", "01");
            retMap.put("messageInfo", e.toString());
        }
        return retMap;
    }

    /**
     * 1.5.3、获得最新模拟实操数据<br>
     * 获得最新模拟实操数据
     * 
     * @param request
     * @param response
     * @param rownum
     *            返回前几条
     * @return
     */
    @RequestMapping("getNewMnscList")
    @ResponseBody
    public Map<String, Object> getNewMnscList(HttpServletRequest request, HttpServletResponse response,
            @RequestParam(required = false) Integer rownum) {
        HttpPush.responseInfo(response);// 跨域
        // 返回结果
        Map<String, Object> retMap = new HashMap<String, Object>();
        try {
            UserInfo ui = userService.getUserInfo();
            if (ui == null) {
                retMap.put("returnCode", "02");
                retMap.put("messageInfo", "无用户登录");
                return retMap;
            }
            // 查询
            List<Map<String, Object>> mnscList = mnscService.getNewMnscList(rownum, ui.getUserId());
            if (mnscList == null || mnscList.size() <= 0) {
                retMap.put("returnCode", "99");
                retMap.put("messageInfo", "信息为空");
            } else {
                List<Map<String, Object>> retL=new ArrayList<Map<String, Object>>();
                for (int i=0; i<mnscList.size(); i++) {
                    Map<String, Object> one=getMnscMap(mnscList.get(i));
                    retL.add(one);
                }
                retMap.put("returnCode", "00");
                retMap.put("data", retL);
            }
        } catch (Exception e) {
            e.printStackTrace();
            retMap.put("returnCode", "01");
            retMap.put("messageInfo", e.toString());
        }
        return retMap;
    }
    private Map<String, Object> getMnscMap(Map<String, Object> om) {
        Map<String, Object> nm=new HashMap<String, Object>();
        nm.put("id", om.get("ID"));
        nm.put("createBy", om.get("CREATBY"));
        nm.put("createDate", om.get("CREATEDATE")+"");
        nm.put("catNames", om.get("CATNAMES"));
        nm.put("imgUrl", om.get("IMGURL"));
        nm.put("mnscUrl", om.get("MNSCURL"));
        nm.put("logVisitCount", (om.get("LOGVISITCOUNT")==null?0:om.get("LOGVISITCOUNT"))+"");
        nm.put("mnscName", om.get("MNSCNAME")+"");
        nm.put("remarks", om.get("REMARKS")+"");
        nm.put("score", om.get("SCORE"));
        nm.put("sort", om.get("SORT"));
        return nm;
    }

    /**
     * 1.5.4、获得最新课件数据<br>
     * 获得最新课件数据
     * 
     * @param request
     * @param response
     * @param 
     * @return
     */
    @RequestMapping("getNewKjList")
    @ResponseBody
    public Map<String, Object> getNewKjList(HttpServletRequest request, HttpServletResponse response,
            @RequestParam(required = false) Integer rownum) {
        HttpPush.responseInfo(response);// 跨域
        // 返回结果
        Map<String, Object> retMap = new HashMap<String, Object>();
        try {
            UserInfo ui = userService.getUserInfo();
            if (ui == null) {
                retMap.put("returnCode", "02");
                retMap.put("messageInfo", "无用户登录");
                return retMap;
            }
            // 查询
            List<Map<String, Object>> kjList = kjService.getNewKjList(rownum,ui.getUserId());
            if (kjList == null || kjList.size() <= 0) {
                retMap.put("returnCode", "99");
                retMap.put("messageInfo", "信息为空");
            } else {
                List<Map<String, Object>> retL=new ArrayList<Map<String, Object>>();
                for (int i=0; i<kjList.size(); i++) {
                    Map<String, Object> one=getKjMap(kjList.get(i));
                    retL.add(one);
                }
                retMap.put("returnCode", "00");
                retMap.put("data", retL);
            }
        } catch (Exception e) {
            e.printStackTrace();
            retMap.put("returnCode", "01");
            retMap.put("messageInfo", e.toString());
        }
        return retMap;
    }
    private Map<String, Object> getKjMap(Map<String, Object> om) {
        Map<String, Object> nm=new HashMap<String, Object>();
        nm.put("id", om.get("ID"));
        nm.put("createBy", om.get("CREATBY"));
        nm.put("createDate", om.get("CREATEDATE")+"");
        nm.put("catNames", om.get("CATNAMES"));
        nm.put("imgUrl", om.get("IMGURL"));
        nm.put("logVisitCount", (om.get("LOGVISITCOUNT")==null?0:om.get("LOGVISITCOUNT"))+"");
        nm.put("kjName", om.get("KJNAME")+"");
        nm.put("remarks", om.get("REMARKS")+"");
        return nm;
    }

    /**
     * 1.5.5、获得最新访问信息<br>
     * 获得最新访问信息
     * 
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("getLogVisitList")
    @ResponseBody
    public Map<String, Object> getLogVisitList(HttpServletRequest request, HttpServletResponse response,
            @RequestParam(required = false) Integer rownum) {
        HttpPush.responseInfo(response);// 跨域
        // 返回结果
        Map<String, Object> retMap = new HashMap<String, Object>();
        try {
            UserInfo ui = userService.getUserInfo();
            if (ui == null) {
                retMap.put("returnCode", "02");
                retMap.put("messageInfo", "无用户登录");
                return retMap;
            }
            // 查询
            List<Map<String, Object>> logVisitList = logVisitService.getMyLogVisitList(rownum,ui.getUserId());
            if (logVisitList == null || logVisitList.size() <= 0) {
                retMap.put("returnCode", "99");
                retMap.put("messageInfo", "信息为空");
            } else {
                List<Map<String, Object>> retL=new ArrayList<Map<String, Object>>();
                for (int i=0; i<logVisitList.size(); i++) {
                    Map<String, Object> one=getLogMap(logVisitList.get(i));
                    retL.add(one);
                }
                retMap.put("returnCode", "00");
                retMap.put("data", retL);
            }
        } catch (Exception e) {
            e.printStackTrace();
            retMap.put("returnCode", "01");
            retMap.put("messageInfo", e.toString());
        }
        return retMap;
    }
    private Map<String, Object> getLogMap(Map<String, Object> om) {
        Map<String, Object> nm=new HashMap<String, Object>();
        nm.put("id", om.get("ID"));
        nm.put("visitTime", om.get("CREATE_DATE")+"");
        nm.put("visitorName", om.get("VISITOR_NAME")+"");
        nm.put("groupName", om.get("GROUP_NAME")==null?"":om.get("GROUP_NAME"));
        nm.put("objType", om.get("VISIT_MODULE_ID")==null?"":om.get("VISIT_MODULE_ID"));
        nm.put("objName", om.get("OBJ_NAME")==null?"":om.get("OBJ_NAME"));
        return nm;
    }

    /**
     * 1.6.5、获得所有对象的分布情况，为后台处理<br>
     * 获得统计访问次数
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("getAllLogVisitCount")
    @ResponseBody
    public Map<String, Object> getAllLogVisitCount(HttpServletRequest request, HttpServletResponse response) {
        HttpPush.responseInfo(response);// 跨域
        // 返回结果
        Map<String, Object> retMap = new HashMap<String, Object>();
        try {
            UserInfo ui = userService.getUserInfo();
            if (ui == null) {
                retMap.put("returnCode", "02");
                retMap.put("messageInfo", "无用户登录");
                return retMap;
            }
            // 查询
            List<Map<String, Object>> countMap = logVisitService.getAllVisitCount();
            if (countMap == null || countMap.size() <= 0) {
                retMap.put("returnCode", "00");
                retMap.put("data", "0");
            } else {
                retMap.put("returnCode", "00");
                retMap.put("data", countMap);
            }
        } catch (Exception e) {
            e.printStackTrace();
            retMap.put("returnCode", "01");
            retMap.put("messageInfo", e.toString());
        }
        return retMap;
    }

    /**
     * 1.6.6、获得最近访问情况，为后台处理<br>
     * 获得最新访问信息
     * 
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("getAllLogVisitList")
    @ResponseBody
    public Map<String, Object> getAllLogVisitList(HttpServletRequest request, HttpServletResponse response,
            @RequestParam(required = false) Integer rownum) {
        HttpPush.responseInfo(response);// 跨域
        // 返回结果
        Map<String, Object> retMap = new HashMap<String, Object>();
        try {
            UserInfo ui = userService.getUserInfo();
            if (ui == null) {
                retMap.put("returnCode", "02");
                retMap.put("messageInfo", "无用户登录");
                return retMap;
            }
            // 查询
            List<Map<String, Object>> logVisitList = logVisitService.getAllLogVisitList(rownum);
            if (logVisitList == null || logVisitList.size() <= 0) {
                retMap.put("returnCode", "99");
                retMap.put("messageInfo", "信息为空");
            } else {
                List<Map<String, Object>> retL=new ArrayList<Map<String, Object>>();
                for (int i=0; i<logVisitList.size(); i++) {
                    Map<String, Object> one=getLogMap(logVisitList.get(i));
                    retL.add(one);
                }
                retMap.put("returnCode", "00");
                retMap.put("data", retL);
            }
        } catch (Exception e) {
            e.printStackTrace();
            retMap.put("returnCode", "01");
            retMap.put("messageInfo", e.toString());
        }
        return retMap;
    }

    /**
     * 1.6.7、获得课件统计信息，为后台首页<br>
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("getKjStat")
    @ResponseBody
    public Map<String, Object> getKjStat(HttpServletRequest request, HttpServletResponse response) {
        HttpPush.responseInfo(response);// 跨域
        // 返回结果
        Map<String, Object> retMap = new HashMap<String, Object>();
        try {
            UserInfo ui = userService.getUserInfo();
            if (ui == null) {
                retMap.put("returnCode", "02");
                retMap.put("messageInfo", "无用户登录");
                return retMap;
            }

            //获得课件分布
            List<Map<String, Object>> kjml=kjService.getKjStateByCate();
            //获得课件日志分布
            List<Map<String, Object>> logml=logVisitService.getKjStateByCate(30);

            //获得所有分类
            TreeNode<CategoryNode> node=(TreeNode<CategoryNode>)categoryService.getCategoryNodeById(null);
            if (node==null||node.getChildCount()==0) {
                retMap.put("returnCode", "03");
                retMap.put("messageInfo", "没有有效分类");
                return retMap;
            }
            //获得所有一级分类，并组织返回值
            List<Map<String, String>> sl=new ArrayList<Map<String, String>>();
            for (TreeNode<? extends TreeNodeBean> on: node.getChildren()) {
                Map<String, String> rm=new HashMap<String, String>();
                rm.put("catName", on.getNodeName());
                rm.put("allKjCatCount", "0");
                rm.put("recentVisitKjCatCount", "0");
                for (Map<String, Object> kjm: kjml) {
                    if (on.getNodeName().equals(kjm.get("UPCATENAME"))) {
                        rm.put("allKjCatCount", ""+kjm.get("COUNT"));
                    }
                }
                for (Map<String, Object> logm: logml) {
                    if (on.getNodeName().equals(logm.get("UPCATENAME"))) {
                        rm.put("recentVisitKjCatCount", ""+logm.get("COUNT"));
                    }
                }
                sl.add(rm);
            }
            retMap.put("returnCode", "00");
            retMap.put("data", sl);
        } catch (Exception e) {
            e.printStackTrace();
            retMap.put("returnCode", "01");
            retMap.put("messageInfo", e.toString());
        }
        return retMap;
    }

    /**
     * 1.6.8、获得模拟实操统计信息，为后台首页<br>
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("getMnscStat")
    @ResponseBody
    public Map<String, Object> getMnscStat(HttpServletRequest request, HttpServletResponse response) {
        HttpPush.responseInfo(response);// 跨域
        // 返回结果
        Map<String, Object> retMap = new HashMap<String, Object>();
        try {
            UserInfo ui = userService.getUserInfo();
            if (ui == null) {
                retMap.put("returnCode", "02");
                retMap.put("messageInfo", "无用户登录");
                return retMap;
            }

            //获得课件分布
            List<Map<String, Object>> mnscml=mnscService.getMnscStateByCate();
            //获得课件日志分布
            List<Map<String, Object>> logml=logVisitService.getMnscStateByCate(30);

            //获得所有分类
            TreeNode<CategoryNode> node=(TreeNode<CategoryNode>)categoryService.getCategoryNodeById(null);
            if (node==null||node.getChildCount()==0) {
                retMap.put("returnCode", "03");
                retMap.put("messageInfo", "没有有效分类");
                return retMap;
            }
            //获得所有一级分类，并组织返回值
            List<Map<String, String>> sl=new ArrayList<Map<String, String>>();
            for (TreeNode<? extends TreeNodeBean> on: node.getChildren()) {
                Map<String, String> rm=new HashMap<String, String>();
                rm.put("catName", on.getNodeName());
                rm.put("allKjCatCount", "0");
                rm.put("recentVisitKjCatCount", "0");
                for (Map<String, Object> mnscm: mnscml) {
                    if (on.getNodeName().equals(mnscm.get("UPCATENAME"))) {
                        rm.put("allKjCatCount", ""+mnscm.get("COUNT"));
                    }
                }
                for (Map<String, Object> logm: logml) {
                    if (on.getNodeName().equals(logm.get("UPCATENAME"))) {
                        rm.put("recentVisitKjCatCount", ""+logm.get("COUNT"));
                    }
                }
                sl.add(rm);
            }
            retMap.put("returnCode", "00");
            retMap.put("data", sl);
        } catch (Exception e) {
            e.printStackTrace();
            retMap.put("returnCode", "01");
            retMap.put("messageInfo", e.toString());
        }
        return retMap;
    }

    /**
     * 1.6.9、获得课件统计信息，为后台首页<br>
     * 这里是为饼图服务的，获得的数据是课件各分类的统计数据，注意是有效的课件
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("getKjPieStat")
    @ResponseBody
    public Map<String, Object> getKjPieStat(HttpServletRequest request, HttpServletResponse response) {
        HttpPush.responseInfo(response);// 跨域
        // 返回结果
        Map<String, Object> retMap = new HashMap<String, Object>();
        try {
            UserInfo ui = userService.getUserInfo();
            if (ui == null) {
                retMap.put("returnCode", "02");
                retMap.put("messageInfo", "无用户登录");
                return retMap;
            }

            //1-获得有效分类
            List<Map<String, Object>> cl=categoryService.getCateList4View(null, 0);
            //2-获得有效分类的
            List<Map<String, Object>> pieData=kjService.getKjPieState();
            //组合数据
            List<Map<String, Object>> retL=new ArrayList<Map<String, Object>>();
            for (Map<String, Object> cm: cl) {
                Map<String, Object> m=new HashMap<String, Object>();
                String catName=(String)cm.get("text");
                m.put("name", catName);
                m.put("value", "0");
                for (Map<String,Object> stat: pieData) {
                    if ((stat.get("CATNAME")).equals(catName)) {
                        m.put("value", stat.get("CATCOUNT"));
                        break;
                    }
                }
                retL.add(m);
            }
            retMap.put("returnCode", "00");
            retMap.put("data", retL);
        } catch (Exception e) {
            e.printStackTrace();
            retMap.put("returnCode", "01");
            retMap.put("messageInfo", e.toString());
        }
        return retMap;
    }

    /**
     * 1.6.10、获得模拟实操统计信息，为后台首页<br>
     * 这里是为饼图服务的，获得的数据是模拟实操各分类的统计数据，注意是有效的模拟实操
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("getMnscPieStat")
    @ResponseBody
    public Map<String, Object> getMnscPieStat(HttpServletRequest request, HttpServletResponse response) {
        HttpPush.responseInfo(response);// 跨域
        // 返回结果
        Map<String, Object> retMap = new HashMap<String, Object>();
        try {
            UserInfo ui = userService.getUserInfo();
            if (ui == null) {
                retMap.put("returnCode", "02");
                retMap.put("messageInfo", "无用户登录");
                return retMap;
            }

            //1-获得有效分类
            List<Map<String, Object>> cl=categoryService.getCateList4View(null, 0);
            //2-获得有效分类的
            List<Map<String, Object>> pieData=mnscService.getMnscPieState();
            //组合数据
            List<Map<String, Object>> retL=new ArrayList<Map<String, Object>>();
            for (Map<String, Object> cm: cl) {
                Map<String, Object> m=new HashMap<String, Object>();
                String catName=(String)cm.get("text");
                m.put("name", catName);
                m.put("value", "0");
                for (Map<String,Object> stat: pieData) {
                    if ((stat.get("CATNAME")).equals(catName)) {
                        m.put("value", stat.get("CATCOUNT"));
                        break;
                    }
                }
                retL.add(m);
            }
            retMap.put("returnCode", "00");
            retMap.put("data", retL);
        } catch (Exception e) {
            e.printStackTrace();
            retMap.put("returnCode", "01");
            retMap.put("messageInfo", e.toString());
        }
        return retMap;
    }
    

    /**
     * 1.6.11、获得最近7天按分类分布的访问量。为后台首页<br>
     * 这里是为分类折线图做准备
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("getVisitStat")
    @ResponseBody
    public Map<String, Object> getVisitStat(HttpServletRequest request, HttpServletResponse response) {
        HttpPush.responseInfo(response);// 跨域
        // 返回结果
        Map<String, Object> retMap = new HashMap<String, Object>();
        try {
            UserInfo ui = userService.getUserInfo();
            if (ui == null) {
                retMap.put("returnCode", "02");
                retMap.put("messageInfo", "无用户登录");
                return retMap;
            }

            //1-获得有效分类
            List<Map<String, Object>> cl=categoryService.getCateList4View(null, 0);
            //2-获得最近7天的字符串数组
            Calendar now = Calendar.getInstance();
            String[] dayStrAry=new String[7];
            dayStrAry[6]=getMonthDay(now);
            for (int i=5; i>=0; i--) {
                now.add(Calendar.DATE, -1);
                dayStrAry[i]=getMonthDay(now);
            }
            //3-获得访问数据
            List<Map<String, Object>> logData=logVisitService.getVisitStatLate(1,7);

            //组合数据
            List<Map<String, Object>> retL=new ArrayList<Map<String, Object>>();
            for (Map<String, Object> cm: cl) {
                List<Integer> numList=new ArrayList<Integer>();
                for (int i=0; i<dayStrAry.length; i++) numList.add(0);

                Map<String, Object> m=new HashMap<String, Object>();
                String catName=(String)cm.get("text");
                m.put("name", catName);
                for (Map<String,Object> stat: logData) {
                    boolean find=false;
                    if ((stat.get("CATNAME")).equals(catName)) {
                        for (int j=0; j<dayStrAry.length; j++) {
                            if (stat.get("DATE").equals(dayStrAry[j])) {
                                numList.set(j, Integer.parseInt(""+stat.get("NUM")));
                                find=true;
                                break;
                            }
                        }
                    }
                    if (find) break;
                }
                m.put("numList", numList);
                retL.add(m);
            }
            retMap.put("returnCode", "00");
            retMap.put("data", retL);
            retMap.put("dayAry", dayStrAry);
        } catch (Exception e) {
            e.printStackTrace();
            retMap.put("returnCode", "01");
            retMap.put("messageInfo", e.toString());
        }
        return retMap;
    }
    private String getMonthDay(Calendar c) {
        return (c.get(Calendar.MONTH)+1)+"月"+c.get(Calendar.DATE)+"日";
    }
}