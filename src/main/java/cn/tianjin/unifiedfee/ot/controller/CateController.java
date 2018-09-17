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
import org.springframework.web.bind.annotation.ResponseBody;

import com.spiritdata.framework.core.model.tree.TreeNode;

import cn.taiji.oauthbean.dto.UserInfo;
import cn.taiji.web.base.controller.BaseController;
import cn.taiji.web.security.UserService;
import cn.tianjin.unifiedfee.ot.entity.Category;
import cn.tianjin.unifiedfee.ot.model.CategoryNode;
import cn.tianjin.unifiedfee.ot.service.CatagoryService;

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
        UserInfo ui=userService.getUserInfo();
        
        return categoryService.save(cate, ui);
    }

    @RequestMapping("getPageData")
    @ResponseBody
    public Map<String, Object> getPageData(Category cate, HttpServletRequest request, HttpServletResponse response) {
        Map<String,Object> map = new HashMap<String,Object>();

        //当前页数
        int pageNum = 1;
        //一页多少条
//        int pageSize = 10;
//        PageHelper.startPage(pageNum,pageSize);
//        Map<String,Object> map = new HashMap<String,Object>();
//        List<Tmp> tmps = tmpService.getAll();
        //放入分页
//        PageInfo<Tmp> a = new PageInfo<Tmp>(tmps);

        Map<String, Object> m=categoryService.getTreeDate();
        List<Object> list=(List<Object>)m.get("DataList");
        if (list==null) {
            map.put("total", 1);
            List l=new ArrayList();
            Map<String, Object> oneM=new HashMap<String, Object>();
            oneM.put("id", "1");
            oneM.put("name", "Test11");
            l.add(oneM);
            map.put("rows", l);
        } else {
            map.put("total", list.size());
            Category c=null;
            List<Map<String, Object>> l=new ArrayList<Map<String, Object>>();
            for (Object _ct: list) {
                TreeNode<CategoryNode> cn=(TreeNode<CategoryNode>)_ct;
                Map<String, Object> _m=new HashMap<String, Object>();
                _m.put("id", cn.getTnEntity().getId());
                _m.put("name", cn.getTnEntity().getNodeName());
                _m.put("desc", cn.getTnEntity().getRemarks());
                _m.put("parentId", cn.getTnEntity().getParentId().equals("0")?null:cn.getTnEntity().getParentId());
                _m.put("parentName", cn.isRoot()?"":cn.getParent().getNodeName());
                _m.put("sort", cn.getOrder());
                _m.put("valid", cn.getTnEntity().getIsvalid()==1?"有效":"失效");
                _m.put("createName", cn.getTnEntity().getCreateName());
                _m.put("createDate", cn.getTnEntity().getCreateDate());
                l.add(_m);
            }
            map.put("rows",  l);
        }
        return map;
    }
}
