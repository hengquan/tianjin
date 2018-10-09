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

import cn.taiji.oauthbean.dto.UserInfo;
import cn.taiji.web.security.UserService;
import cn.tianjin.unifiedfee.ot.entity.Kj;
import cn.tianjin.unifiedfee.ot.entity.Mnsc;
import cn.tianjin.unifiedfee.ot.logvisit.service.LogVisitService;
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
    private MnscService mnscService;
    @Autowired
    private KjService kjService;
    @Autowired
    private MnscRefSourceService mnscRefSourceService;
    @Autowired
    private LogVisitService logVisitService;

    /**
     * 1.3.1、获得模拟实操列表<br>
     * 获得模拟实操列表，以列表形式有分页功能
     * 
     * @param request
     * @param response
     * @param categoryId
     *            分类ID，若为空，获得所有分类
     * @param searchStr
     *            查询字符串，若为空，获得所有摸拟实操
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
            // 处理分页
            if (pageNo == -1)
                pageNo = 1;
            if (pageSize == -1)
                pageSize = _DEFALT_PS;
            // 设置分页
            // PageHelper.offsetPage(pageNo, pageSize);
            // 查询列表
            map.put("categoryId", categoryId);
            map.put("searchStr", searchStr);
            List<Mnsc> mnscs = mnscService.getPageData(map);
            // 放入分页
            // PageInfo<Mnsc> pageList = new PageInfo<Mnsc>(mnscs);
            if (mnscs == null || mnscs.size() == 0) {
                retMap.put("returnCode", "99");
                retMap.put("messageInfo", "列表为空");
            } else {
                retMap.put("returnCode", "00");
                retMap.put("data", mnscs);
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
     * @param mainId
     *            摸拟实操ID，不允许为空
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
                retMap.put("returnCode", "00");
                retMap.put("data", kjList);
            }
        } catch (Exception e) {
            e.printStackTrace();
            retMap.put("returnCode", "01");
            retMap.put("messageInfo", e.toString());
        }
        return retMap;
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
        nm.put("groupName", om.get("GROUP_NAME")==null?"":om.get("GROUP_NAME"));
        nm.put("objType", om.get("VISIT_MODULE_ID")==null?"":om.get("VISIT_MODULE_ID"));
        nm.put("objName", om.get("OBJ_NAME")==null?"":om.get("OBJ_NAME"));
        return nm;
    }
}