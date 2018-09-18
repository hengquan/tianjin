package cn.tianjin.unifiedfee.ot.controller;

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

import cn.taiji.oauthbean.dto.UserInfo;
import cn.taiji.web.security.UserService;
import cn.tianjin.unifiedfee.ot.entity.Mnsc;
import cn.tianjin.unifiedfee.ot.service.MnscService;
import cn.tianjin.unifiedfee.ot.util.HttpPush;

@RequestMapping("/mnsc")
@Controller
public class MnscController {

    @Autowired
    private MnscService mnscService;
    @Autowired
    public UserService userService;

    // 获取分页数据
    @RequestMapping("getPageData")
    @ResponseBody
    public Map<String, Object> getPageData(@RequestParam(value = "offset", defaultValue = "1") int pageNum,
            @RequestParam(value = "limit", defaultValue = "10") int pageSize, HttpServletRequest request,
            HttpServletResponse response) {
        // 返回结果
        Map<String, Object> retMap = new HashMap<String, Object>();
        // 请求参数
        Map<String, Object> map = new HashMap<String, Object>();
        // 跨域
        HttpPush.responseInfo(response);
        // 设置page
        PageHelper.startPage(pageNum, pageSize);
        // 查询数据
        List<Mnsc> mnscs = mnscService.getPageData(map);
        // 放入分页
        PageInfo<Mnsc> pageList = new PageInfo<Mnsc>(mnscs);
        // 返回
        retMap.put("total", pageList.getTotal());
        retMap.put("rows", pageList.getList());
        return retMap;
    }

    // 添加
    @RequestMapping("insert")
    @ResponseBody
    public Map<String, Object> insert(Mnsc mnsc, HttpServletRequest request, HttpServletResponse response) {
        // 获取用户数据
        UserInfo user = userService.getUserInfo();
        // 返回数据
        Map<String, Object> map = new HashMap<String, Object>();
        // 跨域
        HttpPush.responseInfo(response);
        try {
            // 添加数据
            boolean result = mnscService.insert(mnsc, user);
            if (result)
                map.put("resultCode", "100");
            else
                map.put("resultCode", "101");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }

    // 修改
    @RequestMapping("update")
    @ResponseBody
    public Map<String, Object> update(Mnsc mnsc, HttpServletRequest request, HttpServletResponse response) {
        // 返回数据
        Map<String, Object> map = new HashMap<String, Object>();
        // 跨域
        HttpPush.responseInfo(response);
        try {
            // 添加数据
            boolean result = mnscService.update(mnsc);
            if (result)
                map.put("resultCode", "100");
            else
                map.put("resultCode", "101");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }

    // 删除
    @RequestMapping("delete")
    @ResponseBody
    public Map<String, Object> delete(Mnsc mnsc, HttpServletRequest request, HttpServletResponse response) {
        // 返回数据
        Map<String, Object> map = new HashMap<String, Object>();
        // 跨域
        HttpPush.responseInfo(response);
        try {
            // 添加数据
            boolean result = mnscService.delete(mnsc);
            if (result)
                map.put("resultCode", "100");
            else
                map.put("resultCode", "101");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }

    // 查单条记录
    @RequestMapping("get")
    @ResponseBody
    public Map<String, Object> get(Mnsc mnsc, HttpServletRequest request, HttpServletResponse response) {
        // 返回数据
        Map<String, Object> map = new HashMap<String, Object>();
        // 跨域
        HttpPush.responseInfo(response);
        try {
            // 获取数据
            mnsc = mnscService.get(mnsc);
            map.put("data", mnsc);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }

    // 查多条记录跟据ids
    @RequestMapping("getDataList")
    @ResponseBody
    public Map<String, Object> getDataList(Mnsc mnsc, HttpServletRequest request, HttpServletResponse response) {
        // 返回数据
        Map<String, Object> map = new HashMap<String, Object>();
        // 跨域
        HttpPush.responseInfo(response);
        try {
            // 获取数据
            List<Mnsc> mnscs = mnscService.getDataListByIds(mnsc);
            map.put("dataList", mnscs);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }
}
