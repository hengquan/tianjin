package cn.tianjin.unifiedfee.ot.controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

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

import cn.tianjin.unifiedfee.ot.entity.Kj;
import cn.tianjin.unifiedfee.ot.service.KjService;
import cn.tianjin.unifiedfee.ot.util.HttpPush;

@RequestMapping("/kj")
@Controller
public class KjController {

    @Autowired
    private KjService kjService;

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
        List<Kj> kjs = kjService.getPageData(param);
        if (kjs != null && kjs.size() > 0) {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            for (Kj kj : kjs) {
                Date createDate = kj.getCreateDate();
                String createdate = format.format(createDate);
                if (StringUtils.isNotEmpty(createdate))
                    kj.setCreatedate(createdate);
            }
        }
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
        // 返回数据
        Map<String, Object> map = new HashMap<String, Object>();
        // 跨域
        HttpPush.responseInfo(response);
        try {
            // 添加数据
            String uuid = UUID.randomUUID().toString().replaceAll("-", "");
            kj.setId(uuid);
            kj.setKjCatId("1");
            kj.setKjCatNames("1");
            kj.setCreateBy("Admin");
            kj.setKjHtml("12312312312");
            kj.setCreateName("Admin");
            boolean result = kjService.insert(kj);
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
    public Map<String, Object> update(Kj kj,String id, HttpServletRequest request, HttpServletResponse response) {
        // 返回数据
        Map<String, Object> map = new HashMap<String, Object>();
        // 跨域
        HttpPush.responseInfo(response);
        try {
            // 更新数据
            boolean result = kjService.update(kj);
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
            // 获取数据
            kj = kjService.get(kj);
            map.put("data", kj);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }
}
