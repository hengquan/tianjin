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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import cn.taiji.file.service.FileObjectService;
import cn.taiji.oauthbean.dto.UserInfo;
import cn.taiji.web.security.UserService;
import cn.tianjin.unifiedfee.ot.entity.Kj;
import cn.tianjin.unifiedfee.ot.entity.Tm;
import cn.tianjin.unifiedfee.ot.entity.TmSelect;
import cn.tianjin.unifiedfee.ot.service.KjService;
import cn.tianjin.unifiedfee.ot.service.StService;
import cn.tianjin.unifiedfee.ot.util.HttpPush;
import cn.tianjin.unifiedfee.ot.util.Onlylogo;

@RequestMapping("/st")
@Controller
public class StController {

    @Autowired
    private StService stService;
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
        String tmName = request.getParameter("tmName");
        // 传参
        param.put("tmName", tmName);
        // 查询数据
        List<Tm> Tms = stService.getPageData(param);
        if (Tms != null && Tms.size() > 0) {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            for (Tm tm : Tms) {
                Date createDate = tm.getCreateDate();
                String strcreatedate = format.format(createDate);
                System.out.print(strcreatedate+"================");
                if (StringUtils.isNotEmpty(strcreatedate))
                    tm.setStrcreatedate(strcreatedate);
            }
        }
        // 放入分页
        PageInfo<Tm> pageList = new PageInfo<Tm>(Tms);
        // 返回
        map.put("total", pageList.getTotal());
        map.put("rows", pageList.getList());
        return map;
    }

    // 添加
    @RequestMapping("insert")
    @ResponseBody
    public Map<String, Object> insert(Tm tm, HttpServletRequest request, HttpServletResponse response) {
        //获取用户数据
        UserInfo user=userService.getUserInfo();
        // 返回数据
        Map<String, Object> map = new HashMap<String, Object>();
        // 跨域
        HttpPush.responseInfo(response);
        try {
            // 添加数据
            boolean result = stService.insert(tm,user);
            if (result)
                map.put("resultCode", "100");
            else
                map.put("resultCode", "101");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }
 // 添加
    @RequestMapping("insertselect")
    @ResponseBody
    public Map<String, Object> insertselect(Tm tm, HttpServletRequest request, HttpServletResponse response) {
        //获取用户数据
        UserInfo user=userService.getUserInfo();
        // 返回数据
        Map<String, Object> map = new HashMap<String, Object>();
        // 跨域
        HttpPush.responseInfo(response);
        try {
            // 添加数据
            boolean result = stService.insertselect(tm,user);
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
    public Map<String, Object> update(Tm tm,String id, HttpServletRequest request, HttpServletResponse response) {
        // 返回数据
        Map<String, Object> map = new HashMap<String, Object>();
        // 跨域
        HttpPush.responseInfo(response);
        try {
            // 更新数据
            boolean result = stService.update(tm);
            if (result)
                map.put("resultCode", "100");
            else
                map.put("resultCode", "101");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }
    //修改选项和答案    
 // 修改
    @RequestMapping("updateSelct")
    @ResponseBody
    public Map<String, Object> updateSelct(TmSelect tm,String id, HttpServletRequest request, HttpServletResponse response) {
        // 返回数据
        Map<String, Object> map = new HashMap<String, Object>();
        // 跨域
        HttpPush.responseInfo(response);
        try {
            // 更新数据
            boolean result = stService.updateSelct(tm);
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
    public Map<String, Object> delete(Tm tm, HttpServletRequest request, HttpServletResponse response) {
        // 返回数据
        Map<String, Object> map = new HashMap<String, Object>();
        // 跨域
        HttpPush.responseInfo(response);
        try {
            // 添加数据
            boolean result = stService.delete(tm);
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
    public Map<String, Object> get(Tm tm, HttpServletRequest request, HttpServletResponse response) {
        // 返回数据
        Map<String, Object> map = new HashMap<String, Object>();
        // 跨域
        HttpPush.responseInfo(response);
        try {
            // 获取数据
            tm = stService.get(tm);
            map.put("data", tm);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }
 // 获取分页数据
    @RequestMapping("getselctData")
    @ResponseBody
    public Map<String, Object> getselectData(Tm tm, HttpServletRequest request, HttpServletResponse response) {
        // 返回数据
        Map<String, Object> map = new HashMap<String, Object>();    
        // 跨域
        HttpPush.responseInfo(response);
        List<TmSelect> Tms = stService.getselectData(tm);        
        // 返回
        map.put("total", 1);
        map.put("rows", Tms);
        return map;
    }
     // 获取分页数据
    @RequestMapping("getselect")
    @ResponseBody   
    public Map<String, Object> getselect(TmSelect tmselect, HttpServletRequest request, HttpServletResponse response) {
        // 返回数据
        Map<String, Object> map = new HashMap<String, Object>();
        // 跨域
        HttpPush.responseInfo(response);
        try {
            // 获取数据
            tmselect = stService.getselect(tmselect);
            map.put("data", tmselect);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    } 
        
    // 删除
    @RequestMapping("deleteSelect")
    @ResponseBody
    public Map<String, Object> deleteSelect(TmSelect tm, HttpServletRequest request, HttpServletResponse response) {
        // 返回数据
        Map<String, Object> map = new HashMap<String, Object>();
        // 跨域
        HttpPush.responseInfo(response);
        try {
            // 添加数据
            boolean result = stService.deleteSelect(tm);
            if (result)
                map.put("resultCode", "100");
            else
                map.put("resultCode", "101");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }
}
