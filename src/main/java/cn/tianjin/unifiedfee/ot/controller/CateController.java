package cn.tianjin.unifiedfee.ot.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.taiji.oauthbean.dto.UserInfo;
import cn.taiji.web.base.controller.BaseController;
import cn.taiji.web.security.UserService;
import cn.tianjin.unifiedfee.ot.entity.Category;
import cn.tianjin.unifiedfee.ot.service.CatagoryService;

@RequestMapping("/cate")
@Controller
public class CateController extends BaseController{
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
}
