package cn.tianjin.unifiedfee.ot.web;

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
import com.github.pagehelper.PageInfo;

import cn.taiji.oauthbean.dto.UserInfo;
import cn.taiji.web.security.UserService;
import cn.tianjin.unifiedfee.ot.entity.Mnsc;
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
            PageHelper.offsetPage(pageNo, pageSize);
            // 查询列表
            map.put("categoryId", categoryId);
            map.put("searchStr", searchStr);
            List<Mnsc> mnscs = mnscService.getPageData(map);
            // 放入分页
            PageInfo<Mnsc> pageList = new PageInfo<Mnsc>(mnscs);
            if (mnscs == null || mnscs.size() == 0) {
                retMap.put("returnCode", "99");
                retMap.put("messageInfo", "列表为空");
            } else {
                retMap.put("returnCode", "00");
                retMap.put("data", pageList);
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
            if(StringUtils.isEmpty(mnscId)){
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
}