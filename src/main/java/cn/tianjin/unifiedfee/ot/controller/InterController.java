package cn.tianjin.unifiedfee.ot.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import cn.tianjin.unifiedfee.ot.logvisit.service.LogVisitService;
import cn.tianjin.unifiedfee.ot.util.HttpPush;

/**
 * 对外提供接口的控制类
 * @author WH
 */
@Controller
@RequestMapping("/tjtrain/stati")
public class InterController {
    @Autowired //从日志表汇出统计信息
    public LogVisitService logService;

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
     *   "companyId": "58d10e09c7224afa99120c935ad49f99",//企业标识
     *   "companyName":"XX企业" ,//字符串，文章标题
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
            @RequestParam(value="offset", defaultValue="1") int pageNo,
            @RequestParam(value="limit", defaultValue="10") int pageSize) {
        // 跨域
        HttpPush.responseInfo(response);

        Map<String, Object> retMap=new HashMap<String, Object>();
        try {
            //设置page
            int _offset=((pageNo<1?1:pageNo)-1)*pageSize;
            int _limit=pageSize;
            PageHelper.offsetPage(_offset, _limit);
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
            List<Map<String, Object>> _qytjl=logService.getQytjList(param);
            if (_qytjl==null||_qytjl.size()==0) return null;
            PageInfo<Map<String, Object>> pageList=new PageInfo<Map<String, Object>>(_qytjl);
            //对数据进行处理
            //处理每一项
            List<Map<String, Object>> cl=new ArrayList<Map<String, Object>>();
            for (Map<String, Object> qytj: _qytjl) {
                Map<String, Object> newQytj=new HashMap<String, Object>();
                newQytj.put("groupName", qytj.get("GROUP_NAME")==null?"":qytj.get("GROUP_NAME"));
                newQytj.put("kjCount", qytj.get("KJ_COUNT")==null?0:qytj.get("KJ_COUNT"));
                newQytj.put("mnscCount", qytj.get("MNSC_COUNT")==null?0:qytj.get("MNSC_COUNT"));
                newQytj.put("sjCount", qytj.get("SJ_COUNT")==null?0:qytj.get("SJ_COUNT"));
                newQytj.putAll(sumMap);
                //计算比例-模拟实操
                String tmpVal="-";
                try {
                    tmpVal="-";
                    if (!"0".equals(newQytj.get("allMnsc")+"")) {
                        tmpVal=TjController.accuracy(Double.parseDouble(newQytj.get("mnscCount")+""), Double.parseDouble(newQytj.get("allMnsc")+""),2);
                    }
                } catch(Exception e) {
                }
                newQytj.put("mnscRatio", tmpVal);
                //计算比例-课件
                tmpVal="-";
                try {
                    tmpVal="-";
                    if (!"0".equals(newQytj.get("allKj")+"")) {
                        tmpVal=TjController.accuracy(Double.parseDouble(newQytj.get("kjCount")+""), Double.parseDouble(newQytj.get("allKj")+""),2);
                    }
                } catch(Exception e) {
                }
                newQytj.put("kjRatio", tmpVal);
                //计算比例-试卷练习
                tmpVal="-";
                try {
                    tmpVal="-";
                    if (!"0".equals(newQytj.get("allSj")+"")) {
                        tmpVal=TjController.accuracy(Double.parseDouble(newQytj.get("sjCount")+""), Double.parseDouble(newQytj.get("allSj")+""),2);
                    }
                } catch(Exception e) {
                }
                newQytj.put("sjRatio", tmpVal);

                cl.add(newQytj);
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
}