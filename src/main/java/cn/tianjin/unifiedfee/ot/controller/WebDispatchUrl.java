package cn.tianjin.unifiedfee.ot.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class WebDispatchUrl {
    /*前端企业首页 index页*/
    @RequestMapping("webpage")
    public String pageWeb() {
        return "/web/index";
    }

}
