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
import cn.taiji.web.base.controller.BaseController;
import cn.taiji.web.security.UserService;
import cn.tianjin.unifiedfee.ot.entity.Category;
import cn.tianjin.unifiedfee.ot.service.CatagoryService;
import cn.tianjin.unifiedfee.ot.util.HttpPush;

@RequestMapping("/cate")
@Controller
public class CateController extends BaseController {
    @Autowired
    private CatagoryService categoryService;
    @Autowired // 注入Service
    public UserService userService;

    @RequestMapping("insert")
    @ResponseBody
    public Map<String, Object> save(Category cate, HttpServletRequest request, HttpServletResponse response) {

        Map<String, Object> retMap = new HashMap<String, Object>();
        try {
            UserInfo ui = userService.getUserInfo();
            if (ui == null) {
                retMap.put("returnCode", "02");
                retMap.put("messageInfo", "无用户登录");
                return retMap;
            }
            retMap = categoryService.save(cate, ui);
        } catch (Exception e) {
            e.printStackTrace();
            retMap.put("returnCode", "01");
            retMap.put("messageInfo", e.toString());
        }
        return retMap;
    }

    @RequestMapping("getPageData")
    @ResponseBody
    public Map<String, Object> getPageData(Category cate,
            @RequestParam(value = "offset", defaultValue = "1") int offset,
            @RequestParam(value = "limit", defaultValue = "3") int limit, HttpServletRequest request,
            HttpServletResponse response) {
        // 跨域
        HttpPush.responseInfo(response);

        Map<String, Object> map = new HashMap<String, Object>();
        // 设置page
        PageHelper.offsetPage(offset, limit);
        // 查询数据
        List<Category> cl = categoryService.getPageData(cate);
        PageInfo<Category> pageList = new PageInfo<Category>(cl);
        // if (cl!=null) {
        // // 放入分页
        // //PageInfo<Map<String, Object>> pageList=new PageInfo<Map<String,
        // Object>>(cl);
        // // 返回
        // map.put("total", pageList.getTotal());
        ////
        //// List<Map<String,Object>> cList=new ArrayList<Map<String,Object>>();
        //// for (Map<String, Object> _m: cl) {
        //// Map<String ,Object> newData=new HashMap<String, Object>();
        //// newData.put("id", (String)_m.get("ID"));
        //// newData.put("name", (String)_m.get("NAME"));
        //// newData.put("desc", (String)_m.get("REMARKS"));
        //// newData.put("parentId",
        // "0".equals((String)_m.get("PARENT_ID"))?null:(String)_m.get("PARENT_ID"));
        //// newData.put("parentName", "");
        //// TreeNode<CategoryNode>
        // node=(TreeNode<CategoryNode>)categoryService.getCategoryNodeById((String)_m.get("ID"));
        //// if (node!=null) {
        //// if (node.getParent()!=null) newData.put("parentName",
        // node.getParent().getNodeName());
        //// }
        //// newData.put("sort", Integer.parseInt(""+_m.get("SORT")));
        //// newData.put("valid",
        // (Integer.parseInt(""+_m.get("ISVALID")))==1?"有效":"失效");
        //// newData.put("createId", (String)_m.get("CREATE_ID"));
        //// newData.put("createName", (String)_m.get("CREATE_NAME"));
        //// newData.put("createDate", new
        // java.sql.Date(((Timestamp)_m.get("CREATE_DATE")).getTime()));
        //// newData.put("updateDate", new
        // java.sql.Date(((Timestamp)_m.get("UPDATE_DATE")).getTime()));
        //// cList.add(newData);
        //// }
        //
        // map.put("rows", pageList.getList());
        // }
        // List<Kj> kjs = kjService.getPageData(null);
        // // 放入分页
        //// PageInfo<Kj> pageList = new PageInfo<Kj>(kjs);
        // 返回
        map.put("total", pageList.getTotal());
        map.put("rows", pageList.getList());
        return map;
    }
}
