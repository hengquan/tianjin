package cn.tianjin.unifiedfee.ot.controller;

import java.text.SimpleDateFormat;
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

import cn.taiji.oauthbean.dto.UserInfo;
import cn.taiji.web.security.UserService;
import cn.tianjin.unifiedfee.ot.entity.Tm;
import cn.tianjin.unifiedfee.ot.entity.TmSelect;
import cn.tianjin.unifiedfee.ot.service.StService;
import cn.tianjin.unifiedfee.ot.util.HttpPush;

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
        String tmHtml = request.getParameter("tmHtml");
        String catid = request.getParameter("catid");
        // 传参
        param.put("tmHtml", tmHtml);
        param.put("catid", catid);
        // 查询数据
        List<Tm> Tms = stService.getPageData(param);
        if (Tms != null && Tms.size() > 0) {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            for (Tm tm : Tms) {
                Date createDate = tm.getCreateDate();
                String strcreatedate = format.format(createDate);
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
    //题目课件
    @RequestMapping("getPageData4kj")
    @ResponseBody
    public Map<String, Object> getPageData4kj(@RequestParam(value = "offset", defaultValue = "1") int offset,
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
        String refId = request.getParameter("kjid");
        String tmHtml = request.getParameter("tmHtml");
        // 传参       
        param.put("refId", refId);
        param.put("tmHtml", tmHtml);
        // 查询数据
        List<Tm> Tms = stService.getPageData4kj(param);
        if (Tms != null && Tms.size() > 0) {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            for (Tm tm : Tms) {
                Date createDate = tm.getCreateDate();
                String strcreatedate = format.format(createDate);
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
        tm.setIsvalid(Integer.valueOf(request.getParameter("isvalid")));
        try {
            // 添加数据
            String newId = stService.insert(tm,user);
            map.put("resultCode", "00");
            map.put("id", newId);
        } catch (Exception e) {
            e.printStackTrace();
            map.put("resultCode", "01");
            map.put("messageInfo",e.toString());
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
            String result = stService.insertselect(tm,user);            
                map.put("resultCode", result);
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
        boolean result;        
        List<TmSelect> tmselects = stService.getselectallAnswer(tm); 
        if (tmselects.size()==0) {
            map.put("resultCode", "003");
        }else {
            try {
                // 更新数据
                result = stService.update(tm);
                if (result)
                    map.put("resultCode", "001");
                else
                    map.put("resultCode", "002");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return map;
    }
    @RequestMapping("updateIsvalid")
    @ResponseBody
    public Map<String, Object> updateIsvalid(Tm tm,String id, HttpServletRequest request, HttpServletResponse response) {
        // 返回数据
        Map<String, Object> map = new HashMap<String, Object>();
        // 跨域
        HttpPush.responseInfo(response);
        boolean result;        
        try {
                // 更新数据
            result = stService.update(tm);
            if (result)
               map.put("resultCode", "001");
            else
               map.put("resultCode", "002");
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
        tm.setId(request.getParameter("id"));
        // 跨域
        HttpPush.responseInfo(response);
        List<TmSelect> Tms = stService.getselectData(tm);        
        // 返回
        map.put("total", Tms.size());
        map.put("rows", Tms);
        return map;
    }
        
    // 获取分页数据
    @RequestMapping("getselectSign")
    @ResponseBody
    public Map<String, Object> getselectSign(Tm tm, HttpServletRequest request, HttpServletResponse response) {
        // 返回数据
        Map<String, Object> map = new HashMap<String, Object>();    
        // 跨域
        HttpPush.responseInfo(response);
        List<TmSelect> Tms = stService.getselectSign(tm);        
        // 返回
        map.put("total", Tms.size());
        map.put("rows", Tms);
        return map;
    }
    
    @RequestMapping("getselectAnswer")
    @ResponseBody
    public Map<String, Object> getselectAnswer(Tm tm, HttpServletRequest request, HttpServletResponse response) {
        // 返回数据
        Map<String, Object> map = new HashMap<String, Object>();    
        // 跨域
        HttpPush.responseInfo(response);
        List<TmSelect> Tms = stService.getselectAnswer(tm);        
        // 返回
        map.put("total", Tms.size());
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
