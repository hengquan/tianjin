package cn.tianjin.unifiedfee.ot.controller;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;
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

import cn.taiji.format.result.ObjectResponseResult;
import cn.taiji.oauthbean.dto.UserInfo;
import cn.taiji.system.domain.CompanyBasicInfo;
import cn.taiji.web.company.remote.SystemCompanyRemote;
import cn.taiji.web.security.UserService;
import cn.tianjin.unifiedfee.ot.entity.LogVisit;
import cn.tianjin.unifiedfee.ot.logvisit.service.LogVisitService;
import cn.tianjin.unifiedfee.ot.util.HttpPush;

@RequestMapping("/tj")
@Controller
public class TjController {
    @Autowired
    public UserService userService;
    @Autowired
    public SystemCompanyRemote companyRemote;
    @Autowired //从日志表汇出统计信息
    public LogVisitService catService;

    /**
     * 获得学员学习的统计列表，企业的学员学习
     * @param compIds 
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("getXyxxList")
    @ResponseBody
    public Map<String, Object> getXyxxList(HttpServletRequest request, HttpServletResponse response,
            @RequestParam(required=false) String userIds,
            @RequestParam(required=false) String date1,
            @RequestParam(required=false) String date2,
            @RequestParam(value="offset", defaultValue="1") int offset,
            @RequestParam(value="limit", defaultValue="10") int limit) {
        // 跨域
        HttpPush.responseInfo(response);

        Map<String, Object> retMap=new HashMap<String, Object>();
        try {
            UserInfo ui=userService.getUserInfo();
            if (ui == null) {
                retMap.put("returnCode", "02");
                retMap.put("messageInfo", "无用户登录");
                return retMap;
            }
            //获得用户所在企业
            ObjectResponseResult<CompanyBasicInfo> companyInfo=companyRemote.findCompanyInfo(ui.getUserId());
            if (companyInfo==null||companyInfo.getData()==null) {
                //return null;
            }

            //设置page
            PageHelper.offsetPage(offset, limit);
            Map<String, Object> param=new HashMap<String, Object>();
//            param.put("compId", companyInfo.getData().getCompanyId());
            param.put("compId", "98311111234523456B"); //先写死，准备删除
            if (!StringUtils.isBlank(userIds)) {
                userIds=userIds.replaceAll(",", "' or visitor_id='");
                userIds="(visitor_id='"+userIds+"')";
            }
            param.put("userIds", userIds);//所选择的人员的id列表
            param.put("date1", date1);//开始时间
            param.put("date2", date2);//结束时间
            List<Map<String, Object>> _xyxxl=catService.getXyxxList(param);
            PageInfo<Map<String, Object>> pageList=new PageInfo<Map<String, Object>>(_xyxxl);
            if (_xyxxl==null||_xyxxl.size()==0) {
                return null;
            }
            //对数据进行处理
            //获得企业的总数，总课件数，总模拟实操数，总考试次数
            List<Map<String, Object>> _suml=catService.getXyxxSumList(param);
            Map<String, Object> sumMap=new HashMap<String, Object>();
            sumMap.put("allKj", "0");
            sumMap.put("allMnsc", "0");
            sumMap.put("allSj", "0");
            if (_suml!=null&&_suml.size()>0) {
                for (Map<String, Object> _sum: _suml) {
                    if ("ts_mnsc".equals(_sum.get("OBJ_TYPE"))) {
                        sumMap.put("allMnsc", _sum.get("FX"));
                    } else
                    if ("ts_kj".equals(_sum.get("OBJ_TYPE"))) {
                        sumMap.put("allKj", _sum.get("FX"));
                    } else
                    if ("q_sj".equals(_sum.get("OBJ_TYPE"))) {
                        sumMap.put("allSj", _sum.get("FX"));
                    }
                }
            }
            //处理每一项
            List<Map<String, Object>> cl=new ArrayList<Map<String, Object>>();
            for (Map<String, Object> xyxx: _xyxxl) {
                Map<String, Object> newXyxx=new HashMap<String, Object>();
                newXyxx.put("userName", xyxx.get("USER_NAME")==null?"":xyxx.get("USER_NAME"));
                newXyxx.put("kjCount", xyxx.get("KJ_COUNT")==null?0:xyxx.get("KJ_COUNT"));
                newXyxx.put("mnscCount", xyxx.get("MNSC_COUNT")==null?0:xyxx.get("MNSC_COUNT"));
                newXyxx.put("sjCount", xyxx.get("SJ_COUNT")==null?0:xyxx.get("SJ_COUNT"));
                newXyxx.putAll(sumMap);
                //计算比例-模拟实操
                String tmpVal="-";
                try {
                    tmpVal="-";
                    if (!"0".equals(newXyxx.get("allMnsc")+"")) {
                        tmpVal=accuracy(Double.parseDouble(newXyxx.get("mnscCount")+""), Double.parseDouble(newXyxx.get("allMnsc")+""),2);
                    }
                } catch(Exception e) {
                }
                newXyxx.put("mnscRatio", tmpVal);
                //计算比例-课件
                tmpVal="-";
                try {
                    tmpVal="-";
                    if (!"0".equals(newXyxx.get("allKj")+"")) {
                        tmpVal=accuracy(Double.parseDouble(newXyxx.get("kjCount")+""), Double.parseDouble(newXyxx.get("allKj")+""),2);
                    }
                } catch(Exception e) {
                }
                newXyxx.put("kjRatio", tmpVal);
                //计算比例-试卷练习
                tmpVal="-";
                try {
                    tmpVal="-";
                    if (!"0".equals(newXyxx.get("allSj")+"")) {
                        tmpVal=accuracy(Double.parseDouble(newXyxx.get("sjCount")+""), Double.parseDouble(newXyxx.get("allSj")+""),2);
                    }
                } catch(Exception e) {
                }
                newXyxx.put("sjRatio", tmpVal);

                cl.add(newXyxx);
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

    public static String accuracy(double num, double total, int scale) {
         DecimalFormat df = (DecimalFormat)NumberFormat.getInstance();
         df.setMaximumFractionDigits(scale);     //模式 例如四舍五入     
         df.setRoundingMode(RoundingMode.HALF_UP);       
         double accuracy_num = num / total * 100;       
         return df.format(accuracy_num)+"%";
    }
    
    @RequestMapping("getcatkjtj")
    @ResponseBody
    public Map<String, Object> getcatkjtj(HttpServletRequest request, HttpServletResponse response,
            @RequestParam(required=false) String userIds,
            @RequestParam(required=false) String date1,
            @RequestParam(required=false) String date2,
            @RequestParam(value="offset", defaultValue="1") int offset,
            @RequestParam(value="limit", defaultValue="10") int limit) {
           Map<String, Object> param=new HashMap<String, Object>();  
           param.put("date1", date1);//开始时间
           param.put("date2", date2);//结束时间
           Map<String, Object> map = new HashMap<String, Object>();
           List<LogVisit> loglist = catService.getcatkjtj(param);
           PageInfo<LogVisit> pageList = new PageInfo<LogVisit>(loglist);
           map.put("total", pageList.getTotal());
           map.put("rows", pageList.getList());
           return map;
    }
}