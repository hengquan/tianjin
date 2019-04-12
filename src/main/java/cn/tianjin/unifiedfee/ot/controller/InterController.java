package cn.tianjin.unifiedfee.ot.controller;

import java.text.SimpleDateFormat;
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

import cn.taiji.company.remote.SystemCompanyRemote;
import cn.tianjin.unifiedfee.ot.entity.AllCompany;
import cn.tianjin.unifiedfee.ot.logvisit.service.LogVisitService;
import cn.tianjin.unifiedfee.ot.mapper.AllCompanyMapper;

/**
 * 对外提供接口的控制类
 * @author WH
 */
@Controller
@RequestMapping("/tjtrain/stati")
public class InterController {
    SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd");

    @Autowired //从日志表汇出统计信息
    public LogVisitService logService;
    @Autowired
    public SystemCompanyRemote companyRemote;
    @Autowired
    private AllCompanyMapper allCompanydao;
    
    /**
     * 获得某企业的全体学员的练习情况——答题情况
     * 
     * @param companyId 企业id
     * @param startDate 开始日期
     * @param endDate   结束日期
     * @param pageNo    页码，从1开始
     * @param pageSize  每页记录条数
     * 
     * @return 返回为list，list中的元素为map，内容如下
     * <pre>{@code
     * "list": [{
     *   "companyId": "ZZJGD1539757689359",//企业标识
     *   "companyName":"XX企业" ,//字符串标题
     *   "count":12,//练习次数
     *   "percent":0.12//练习占比
     * }]
     * </pre>
     */
    @RequestMapping("practiceStati")
    @ResponseBody
    public Map<String, Object> practiceStati(HttpServletRequest request, HttpServletResponse response,
            @RequestParam(required=false) String companyId,
            @RequestParam(required=false) String startDate,
            @RequestParam(required=false) String endDate,
            @RequestParam(value="pageNo", defaultValue="1") int pageNo,
            @RequestParam(value="pageSize", defaultValue="20") int pageSize) {
        // 跨域
        //HttpPush.responseInfo(response);

        Map<String, Object> retMap=new HashMap<String, Object>();
        try {
            if (StringUtils.isNotBlank(companyId)) {
                AllCompany companyInfo=allCompanydao.getByCompanyId(companyId);
                if (companyInfo==null) {
                    retMap.put("returnCode", "11");
                    retMap.put("messageInfo", "companyId无对应企业");
                    return retMap;
                }
            }
            if (StringUtils.isNotBlank(startDate)) {
                try {
                    format.parse(startDate);
                } catch(Exception e) {
                    retMap.put("returnCode", "21");
                    retMap.put("messageInfo", "开始日期不合法，合法日期如：2018-09-05");
                    return retMap;
                }
            }
            if (StringUtils.isNotBlank(endDate)) {
                try {
                    format.parse(endDate);
                } catch(Exception e) {
                    retMap.put("returnCode", "21");
                    retMap.put("messageInfo", "结束日期不合法，合法日期如：2018-09-05");
                    return retMap;
                }
            }

            //设置参数
            Map<String, Object> param=new HashMap<String, Object>();
            param.put("date1", startDate);//开始时间
            param.put("date2", endDate);//结束时间
            //获得企业的总数，总课件数，总模拟实操数，总考试次数
            List<Map<String, Object>> _suml=logService.getQytjSumList(param);
            Map<String, Object> sumMap=new HashMap<String, Object>();
            sumMap.put("allKj", "0");
            sumMap.put("allMnsc", "0");
            sumMap.put("allSj", "0");
            if (_suml!=null&&_suml.size()>0) {
                for (Map<String, Object> _sum: _suml) {
                    if ("q_sj".equals(_sum.get("OBJ_TYPE"))) {
                        sumMap.put("allSj", _sum.get("FX"));
                    }
                }
            }

            //设置page
            int _offset=((pageNo<1?1:pageNo)-1)*pageSize;
            int _limit=pageSize;
            PageHelper.offsetPage(_offset, _limit);
            List<Map<String, Object>> _qytjl=logService.getQytjList(param);
            if (_qytjl==null||_qytjl.size()==0) return null;
            PageInfo<Map<String, Object>> pageList=new PageInfo<Map<String, Object>>(_qytjl);
            //对数据进行处理
            //处理每一项
            List<Map<String, Object>> cl=new ArrayList<Map<String, Object>>();
            for (Map<String, Object> qytj: _qytjl) {
                if (StringUtils.isNotBlank(companyId)&&!companyId.equals(qytj.get("GROUP_ID"))) continue;
                Map<String, Object> newQytj=new HashMap<String, Object>();
                newQytj.put("companyId", qytj.get("GROUP_ID")==null?"":qytj.get("GROUP_ID"));
                newQytj.put("companyName", qytj.get("GROUP_NAME")==null?"":qytj.get("GROUP_NAME"));
                newQytj.put("count", qytj.get("SJ_COUNT")==null?0:qytj.get("SJ_COUNT"));
                //计算比例-试卷练习
                String tmpVal="-";
                try {
                    if (!"0".equals(sumMap.get("allSj")+"")) {
                        tmpVal=TjController.accuracy(Double.parseDouble(newQytj.get("count")+""), Double.parseDouble(sumMap.get("allSj")+""),2);
                    }
                } catch(Exception e) {
                }
                newQytj.put("percent", tmpVal);

                cl.add(newQytj);
            }
            if (cl.size()==0) {
                retMap.put("returnCode", "01");
                retMap.put("messageInfo", "列表为空");
            } else {
                if (StringUtils.isNotBlank(companyId)) {
                    retMap.put("total", 1);
                } else {
                    retMap.put("total", pageList.getTotal());
                }
                retMap.put("rows", cl);
            }
            return retMap;
        } catch (Exception e) {
            e.printStackTrace();
            retMap.put("returnCode", "99");
            retMap.put("messageInfo", e.toString());
            return retMap;
        }
    }

    /**
     * 获得某企业的全体学员的学习情况——模拟实操及课件
     * 
     * @param companyId 企业id
     * @param startDate 开始日期
     * @param endDate   结束日期
     * @param pageNo    页码，从1开始
     * @param pageSize  每页记录条数
     * 
     * @return 返回为list，list中的元素为map，内容如下
     * <pre>{@code
     * "list": [{
     *   "companyId": "ZZJGD1539757689359",//企业标识
     *   "companyName":"XX企业" ,//字符串标题
     *   "mnscCount":12,//学习模拟实操次数
     *   "mnscPercent":0.12,//学习模拟实操占比
     *   "mnscCount":30,//学习课件次数
     *   "mnscPercent":0.23,//学习课件占比
     *   "percent":0.12//练习占比
     * }]
     * </pre>
     */
    @RequestMapping("learnStati")
    @ResponseBody
    public Map<String, Object> learnStati(HttpServletRequest request, HttpServletResponse response,
            @RequestParam(required=false) String companyId,
            @RequestParam(required=false) String startDate,
            @RequestParam(required=false) String endDate,
            @RequestParam(value="pageNo", defaultValue="1") int pageNo,
            @RequestParam(value="pageSize", defaultValue="20") int pageSize) {

        Map<String, Object> retMap=new HashMap<String, Object>();
        try {
            if (StringUtils.isNotBlank(companyId)) {
                AllCompany companyInfo=allCompanydao.getByCompanyId(companyId);
                if (companyInfo==null) {
                    retMap.put("returnCode", "11");
                    retMap.put("messageInfo", "companyId无对应企业");
                    return retMap;
                }
            }
            if (StringUtils.isNotBlank(startDate)) {
                try {
                    format.parse(startDate);
                } catch(Exception e) {
                    retMap.put("returnCode", "21");
                    retMap.put("messageInfo", "开始日期不合法，合法日期如：2018-09-05");
                    return retMap;
                }
            }
            if (StringUtils.isNotBlank(endDate)) {
                try {
                    format.parse(endDate);
                } catch(Exception e) {
                    retMap.put("returnCode", "21");
                    retMap.put("messageInfo", "结束日期不合法，合法日期如：2018-09-05");
                    return retMap;
                }
            }

            //设置参数
            Map<String, Object> param=new HashMap<String, Object>();
            if (StringUtils.isNotBlank(startDate)) {
                
            }
            param.put("date1", startDate);//开始时间
            param.put("date2", endDate);//结束时间
            //获得企业的总数，总课件数，总模拟实操数，总考试次数
            List<Map<String, Object>> _suml=logService.getQytjSumList(param);
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
                    }
                }
            }

            //设置page
            int _offset=((pageNo<1?1:pageNo)-1)*pageSize;
            int _limit=pageSize;
            PageHelper.offsetPage(_offset, _limit);
            List<Map<String, Object>> _qytjl=logService.getQytjList(param);
            if (_qytjl==null||_qytjl.size()==0) return null;
            PageInfo<Map<String, Object>> pageList=new PageInfo<Map<String, Object>>(_qytjl);
            //对数据进行处理
            //处理每一项
            List<Map<String, Object>> cl=new ArrayList<Map<String, Object>>();
            for (Map<String, Object> qytj: _qytjl) {
                if (StringUtils.isNotBlank(companyId)&&!companyId.equals(qytj.get("GROUP_ID"))) continue;
                Map<String, Object> newQytj=new HashMap<String, Object>();
                newQytj.put("companyId", qytj.get("GROUP_ID")==null?"":qytj.get("GROUP_ID"));
                newQytj.put("companyName", qytj.get("GROUP_NAME")==null?"":qytj.get("GROUP_NAME"));

                //计算比例-模拟实操
                newQytj.put("mnscCount", qytj.get("MNSC_COUNT")==null?0:qytj.get("MNSC_COUNT"));
                String tmpVal="-";
                try {
                    tmpVal="-";
                    if (!"0".equals(sumMap.get("allMnsc")+"")) {
                        tmpVal=TjController.accuracy(Double.parseDouble(newQytj.get("mnscCount")+""), Double.parseDouble(sumMap.get("allMnsc")+""),2);
                    }
                } catch(Exception e) {
                }
                newQytj.put("mnscPercent", tmpVal);

                //计算比例-课件
                newQytj.put("kjCount", qytj.get("KJ_COUNT")==null?0:qytj.get("KJ_COUNT"));
                tmpVal="-";
                try {
                    tmpVal="-";
                    if (!"0".equals(sumMap.get("allKj")+"")) {
                        tmpVal=TjController.accuracy(Double.parseDouble(newQytj.get("kjCount")+""), Double.parseDouble(sumMap.get("allKj")+""),2);
                    }
                } catch(Exception e) {
                }
                newQytj.put("kjPercent", tmpVal);

                cl.add(newQytj);
            }
            if (cl.size()==0) {
                retMap.put("returnCode", "01");
                retMap.put("messageInfo", "列表为空");
            } else {
                if (StringUtils.isNotBlank(companyId)) {
                    retMap.put("total", 1);
                } else {
                    retMap.put("total", pageList.getTotal());
                }
                retMap.put("rows", cl);
            }
            return retMap;
        } catch (Exception e) {
            e.printStackTrace();
            retMap.put("returnCode", "99");
            retMap.put("messageInfo", e.toString());
            return retMap;
        }
    }
}