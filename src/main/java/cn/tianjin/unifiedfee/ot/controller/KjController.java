package cn.tianjin.unifiedfee.ot.controller;

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

import cn.taiji.oauthbean.dto.UserInfo;
import cn.taiji.web.security.UserService;
import cn.tianjin.unifiedfee.ot.entity.Kj;
import cn.tianjin.unifiedfee.ot.service.KjRefSourceService;
import cn.tianjin.unifiedfee.ot.service.KjService;
import cn.tianjin.unifiedfee.ot.util.HttpPush;

@RequestMapping("/kj")
@Controller
public class KjController {

    @Autowired
    private KjService kjService;
    @Autowired
    public UserService userService;
    @Autowired
    public KjRefSourceService kjRefSourceService;

    // 获取分页数据
    @RequestMapping("getPageData")
    @ResponseBody
    public Map<String, Object> getPageData(@RequestParam(value = "offset", defaultValue = "1") int offset,
            @RequestParam(value = "limit", defaultValue = "10") int limit, HttpServletRequest request,
            HttpServletResponse response) {
        // 返回数据
        Map<String, Object> map = new HashMap<String, Object>();
        // 请求数据
        Map<String, Object> param = new HashMap<String, Object>();
        // 跨域
        HttpPush.responseInfo(response);
        // 设置page
        PageHelper.offsetPage(offset, limit);
        // 获取参数
        String kjName = request.getParameter("kjName");
        String kjCatId = request.getParameter("kjCatId");
        String score = request.getParameter("score");
        String createStart = request.getParameter("createStart");
        String createEnd = request.getParameter("createEnd");
        // 传参
        param.put("kjName", kjName);
        param.put("kjCatId", kjCatId);
        param.put("score", score);
        param.put("createStart", createStart);
        param.put("createEnd", createEnd);
        // 查询数据
        List<Kj> kjs = kjService.getPageData(param);
        // 放入分页
        PageInfo<Kj> pageList = new PageInfo<Kj>(kjs);
        // 返回
        map.put("total", pageList.getTotal());
        map.put("rows", pageList.getList());
        return map;
    }

    // 添加
    @RequestMapping("insert")
    @ResponseBody
    public Map<String, Object> insert(Kj kj, HttpServletRequest request, HttpServletResponse response) {
        // 获取用户数据
        UserInfo user = userService.getUserInfo();
        // 返回数据
        Map<String, Object> map = new HashMap<String, Object>();
        // 跨域
        HttpPush.responseInfo(response);
        try {
            // 添加数据
            boolean result = kjService.insert(kj, user);
            String id = kj.getId();
            if (StringUtils.isNotEmpty(id))
                map.put("objId", id);
            else
                map.put("objId", "");
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
    public Map<String, Object> update(Kj kj, String id, HttpServletRequest request, HttpServletResponse response) {
        // 获取用户数据
        UserInfo user = userService.getUserInfo();
        // 返回数据
        Map<String, Object> map = new HashMap<String, Object>();
        // 跨域
        HttpPush.responseInfo(response);
        try {
            // 更新数据
            boolean result = kjService.update(kj, user);
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
    public Map<String, Object> delete(Kj kj, HttpServletRequest request, HttpServletResponse response) {
        // 返回数据
        Map<String, Object> map = new HashMap<String, Object>();
        // 跨域
        HttpPush.responseInfo(response);
        try {
            // 添加数据
            boolean result = kjService.delete(kj);
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
    public Map<String, Object> get(Kj kj, HttpServletRequest request, HttpServletResponse response) {
        // 返回数据
        Map<String, Object> map = new HashMap<String, Object>();
        // 跨域
        HttpPush.responseInfo(response);
        try {
            // 获取主表数据
            kj = kjService.get(kj);
            map.put("data", kj);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }

    // 查多条记录跟据ids
    @RequestMapping("getDataList")
    @ResponseBody
    public Map<String, Object> getDataList(Kj kj, HttpServletRequest request, HttpServletResponse response) {
        // 返回数据
        Map<String, Object> map = new HashMap<String, Object>();
        // 跨域
        HttpPush.responseInfo(response);
        try {
            // 获取数据
            List<Kj> kjs = kjService.getDataListByIds(kj);
            map.put("dataList", kjs);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }
}
