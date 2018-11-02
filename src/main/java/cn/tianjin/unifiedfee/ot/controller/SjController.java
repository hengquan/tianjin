package cn.tianjin.unifiedfee.ot.controller;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.spiritdata.framework.util.DateUtils;

import cn.taiji.oauthbean.dto.UserInfo;
import cn.taiji.web.security.UserService;
import cn.tianjin.unifiedfee.ot.service.SjService;
import cn.tianjin.unifiedfee.ot.util.HttpPush;

@RequestMapping("/sj")
@Controller
public class SjController {
    @Autowired
    private SjService sjService;
    @Autowired
    public UserService userService;

    /**
     * 获得某用户的考试列表，为企业端在线练习提供服务
     * @param request
     * @param response
     * @param catNames
     * @param date1
     * @param date2
     * @param offset
     * @param limit
     * @return
     */
    //得到的某人的试卷列表
    @RequestMapping("getSjList")
    @ResponseBody
    public Map<String, Object> getSjList(HttpServletRequest request, HttpServletResponse response,
            @RequestParam(required=false) String catNames,
            @RequestParam(required=false) String date1,
            @RequestParam(required=false) String date2,
            @RequestParam(value="offset", defaultValue="1") int offset,
            @RequestParam(value="limit", defaultValue="10") int limit) {
        // 跨域
        HttpPush.responseInfo(response);
        // 返回数据
        Map<String, Object> retMap=new HashMap<String, Object>();

        try {
            UserInfo ui=userService.getUserInfo();
            if (ui==null) {
                retMap.put("returnCode", "02");
                retMap.put("messageInfo", "无用户登录");
                return retMap;
            }

            //设置page
            PageHelper.offsetPage(offset, limit);

            Map<String, Object> param=new HashMap<String, Object>();
            param.put("userId", ui.getUserId());

            String catSql="";
            if (!StringUtils.isBlank(catNames)) {
                catSql=catNames.replaceAll(",", "')>0 or instr(a.sj_cat_names, '");
                catSql="(instr(a.sj_cat_names, '"+catSql+"')>0)";
            }
            param.put("catSql", catSql);//所选择的人员的id列表

            param.put("date1", date1);//开始时间
            param.put("date2", date2);//结束时间

            List<Map<String, Object>> sjList=sjService.getSjList4User(param);
            PageInfo<Map<String, Object>> pageList=new PageInfo<Map<String, Object>>(sjList);
            if (sjList==null||sjList.size()==0) return null;

            //处理每一项
            List<Map<String, Object>> cl=new ArrayList<Map<String, Object>>();
            for (Map<String, Object> sjXX: sjList) {
                Map<String, Object> newSj=new HashMap<String, Object>();
                newSj.put("id", sjXX.get("ID")==null?"":sjXX.get("ID"));
                newSj.put("catNames", sjXX.get("SJ_CAT_NAMES")==null?"":sjXX.get("SJ_CAT_NAMES"));
                newSj.put("sjName", sjXX.get("SJ_NAME")==null?"":sjXX.get("SJ_NAME"));
                newSj.put("diffCount", sjXX.get("DIFF_TYPE")==null?"":sjXX.get("DIFF_TYPE"));
                newSj.put("tmCount", sjXX.get("TM_COUNT")==null?"":sjXX.get("TM_COUNT"));
                newSj.put("rightCount", sjXX.get("OK_COUNT")==null?"":sjXX.get("OK_COUNT"));
                newSj.put("state", sjXX.get("STATE")==null?"":sjXX.get("STATE"));
                Date tmp=null;
                if (null!=sjXX.get("CREATE_DATE")) {
                    tmp=new java.sql.Date(((Timestamp)sjXX.get("CREATE_DATE")).getTime());
                    newSj.put("createDate", DateUtils.convert2LongLocalStr(tmp));
                }
                cl.add(newSj);
            }

            retMap.put("total", pageList.getTotal());
            retMap.put("rows", cl);
            return retMap;
        } catch (Exception e) {
            e.printStackTrace();
            retMap.put("returnCode", "01");
            retMap.put("messageInfo", e.toString());
        }
        return retMap;
    }

    /**
     * 获得某用户的考试列表，为企业端在线练习提供服务
     * @param request
     * @param response
     * @param catNames
     * @param date1
     * @param date2
     * @param offset
     * @param limit
     * @return
     */
    //删除试卷，设置为删除标志
    @RequestMapping("delSj")
    @ResponseBody
    public Map<String, Object> delSj(HttpServletRequest request, HttpServletResponse response,
            @RequestParam(required=false) String id) {
        // 跨域
        HttpPush.responseInfo(response);
        // 返回数据
        Map<String, Object> retMap=new HashMap<String, Object>();

        try {
            UserInfo ui=userService.getUserInfo();
            if (ui==null) {
                retMap.put("returnCode", "02");
                retMap.put("messageInfo", "无用户登录");
                return retMap;
            }
            if (StringUtils.isBlank(id)) {
                retMap.put("returnCode", "03");
                retMap.put("messageInfo", "试卷Id为空");
                return retMap;
            }
            retMap=sjService.changeState(id, 5);
            return retMap;
        } catch (Exception e) {
            e.printStackTrace();
            retMap.put("returnCode", "01");
            retMap.put("messageInfo", e.toString());
        }
        return retMap;
    }
}