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
import cn.tianjin.unifiedfee.ot.entity.Kjitem;
import cn.tianjin.unifiedfee.ot.service.KjitemService;
import cn.tianjin.unifiedfee.ot.util.HttpPush;

@RequestMapping("/kjitem")
@Controller
public class KjitemController {

    @Autowired
    private KjitemService kjitemService;
    @Autowired
    public UserService userService;    

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
        // 传参
        param.put("kjName", kjName);
        // 查询数据
        List<Kjitem> kjs = kjitemService.getPageData(param);
        if (kjs!=null) {
            // 放入分页
            PageInfo<Kjitem> pageList = new PageInfo<Kjitem>(kjs);
            // 返回
            map.put("total", pageList.getTotal());
            map.put("rows", pageList.getList());
        } else {
            // 返回
            map.put("total", null);
            map.put("rows", 0);
        }
        return map;
    }

    // 添加
    @RequestMapping("insert")
    @ResponseBody
    public Map<String, Object> insert(Kjitem kj, HttpServletRequest request, HttpServletResponse response) {
        // 获取用户数据
        UserInfo user = userService.getUserInfo();
        // 返回数据
        Map<String, Object> map = new HashMap<String, Object>();
        // 跨域
        HttpPush.responseInfo(response);
        try {
            // 添加数据
            boolean result = kjitemService.insert(kj, user);
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
    public Map<String, Object> update(Kjitem kj, String id, HttpServletRequest request, HttpServletResponse response) {
        // 获取用户数据
        UserInfo user = userService.getUserInfo();
        // 返回数据
        Map<String, Object> map = new HashMap<String, Object>();
        // 跨域
        HttpPush.responseInfo(response);
        try {
            // 更新数据
            boolean result = kjitemService.update(kj, user);
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
    public Map<String, Object> delete(Kjitem kj, HttpServletRequest request, HttpServletResponse response) {
        // 返回数据
        Map<String, Object> map = new HashMap<String, Object>();
        // 跨域
        HttpPush.responseInfo(response);
        try {
            // 添加数据
            boolean result = kjitemService.delete(kj);
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
    public Map<String, Object> get(Kjitem kj, HttpServletRequest request, HttpServletResponse response) {
        // 返回数据
        Map<String, Object> map = new HashMap<String, Object>();
        // 跨域
        HttpPush.responseInfo(response);
        try {
            // 获取主表数据
            kj = kjitemService.get(kj);
            map.put("data", kj);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }

    // 查多条记录跟据ids
    @RequestMapping("getDataList")
    @ResponseBody
    public Map<String, Object> getDataList(Kjitem kj, HttpServletRequest request, HttpServletResponse response) {
        // 返回数据
        Map<String, Object> map = new HashMap<String, Object>();
        // 跨域
        HttpPush.responseInfo(response);
        try {
            // 获取数据
            List<Kjitem> kjs = kjitemService.getDataListByIds(kj);
            map.put("dataList", kjs);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }
}
