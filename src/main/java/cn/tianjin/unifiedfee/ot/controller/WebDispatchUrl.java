package cn.tianjin.unifiedfee.ot.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class WebDispatchUrl {
    /*前端企业首页 index页*/
    @RequestMapping("webpage")
    public String pageWeb() {
        return "/web/index";
    }

    /*学员概况*/
    @RequestMapping("summary")
    public String toSummary() {
        return "/web/summary";
    }

    /*模拟实操*/
    @RequestMapping("practice")
    public String toSimulation() {
        return "/web/practice";
    }

    /*在线学习*/
    @RequestMapping("courseware")
    public String toOnline() {
        return "/web/courseware";
    }
    
    /*播放课件*/
    @RequestMapping("onlineLearning")
    public String toOnlineLearning(HttpServletRequest request) {
        System.out.println("12312312");
        return "/web/onlineLearning";
    }
}