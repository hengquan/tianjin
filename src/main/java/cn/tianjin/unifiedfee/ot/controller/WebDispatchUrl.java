package cn.tianjin.unifiedfee.ot.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.taiji.format.result.ObjectResponseResult;
import cn.taiji.oauthbean.dto.UserInfo;
import cn.taiji.company.remote.SystemCompanyRemote;
import cn.taiji.company.system.CompanyBasicInfo;
import cn.taiji.web.menu.remote.SecurityMenuRemote;
import cn.taiji.web.security.UserService;
import cn.tianjin.unifiedfee.ot.logvisit.service.LogVisitService;
import cn.tianjin.unifiedfee.ot.mapper.CommArchiveMapper;

@Controller
public class WebDispatchUrl {
    @Autowired // 注入Service
    public UserService userService;
//  @Autowired // 注入Service
//  public SecurityMenuService securityMenuService;
    @Autowired // 注入Service
    public SecurityMenuRemote securityMenuRemote;
    @Autowired
    public LogVisitService catService;
    @Autowired
    public SystemCompanyRemote companyRemote;
    @Autowired // 注入Service
    public CommArchiveMapper commArchiveMapper;
 
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

    /*在线练习*/
    @RequestMapping("onlineExercise")
    public String toExercise() {
        return "/web/practice";
    }

    /*播放课件*/
    @RequestMapping("onlineLearning")
    public String toOnlineLearning(HttpServletRequest request) {
        return "/static/onlineLearning";
    }

    //--统计相关-1学员学习
    @RequestMapping("tj/xyxx")
    public String toTjXyxx(HttpServletRequest request, Model model) {
        //获得企业的人员
        String optionHtml="";
        UserInfo ui=userService.getUserInfo();
        if (ui!=null) {
            //获得用户所在企业
            ObjectResponseResult<CompanyBasicInfo> companyInfo=companyRemote.findCompanyInfo(ui.getUserId());
            if (companyInfo!=null&&companyInfo.getData()!=null) {
                Map<String, Object> param=new HashMap<String, Object>();
                param.put("compId", companyInfo.getData().getCompanyId());
                List<Map<String, Object>> ul=catService.getCompUserList(param);
                if (ul!=null&&ul.size()>0) {
                    for (Map<String, Object> u: ul) {
                        optionHtml+="<option value='"+u.get("visitor_id")+"'>"+u.get("visitor_name")+"</option>";
                    }
                }
            }
            //测试，准备删除
            Map<String, Object> param=new HashMap<String, Object>();
            param.put("compId", "98311111234523456B");
            List<Map<String, Object>> ul=catService.getCompUserList(param);
            if (ul!=null&&ul.size()>0) {
                for (Map<String, Object> u: ul) {
                    optionHtml+="<option value='"+u.get("VISITOR_ID")+"'>"+u.get("VISITOR_NAME")+"</option>";
                }
            }
        }
        model.addAttribute("optionHtml", optionHtml);
        return "/manage/tj/tjXyxx";
    }

    //--统计相关-学员日志-管理员
    @RequestMapping("xxrz4comp")
    public String toTjXxrz4comp(HttpServletRequest request, Model model) {
        //获得参数
        String urlParam=request.getQueryString();
        String optionHtml="";
        UserInfo ui=userService.getUserInfo();
        if (ui!=null) {
            //获得用户所在企业
            ObjectResponseResult<CompanyBasicInfo> companyInfo=companyRemote.findCompanyInfo(ui.getUserId());
            System.out.println("=================================================================");
            if (companyInfo!=null&&companyInfo.getData()!=null) {
                Map<String, Object> param=new HashMap<String, Object>();
                param.put("compId", companyInfo.getData().getCompanyId());
                List<Map<String, Object>> ul=catService.getCompUserList(param);
                if (ul!=null&&ul.size()>0) {
                    for (Map<String, Object> u: ul) {
                        optionHtml+="<option value='"+u.get("visitor_id")+"'>"+u.get("visitor_name")+"</option>";
                    }
                }
            } else {//这是测试代码，用guanliyuan进行测试
                Map<String, Object> param=new HashMap<String, Object>();
                param.put("compId", "98311111234523456B");
                List<Map<String, Object>> ul=catService.getCompUserList(param);
                if (ul!=null&&ul.size()>0) {
                    for (Map<String, Object> u: ul) {
                        optionHtml+="<option value='"+u.get("VISITOR_ID")+"'>"+u.get("VISITOR_NAME")+"</option>";
                    }
                }
            }
        }
        model.addAttribute("optionHtml", optionHtml);
        model.addAttribute("urlParam", urlParam);
        model.addAttribute("type", "2");
        return "/web/learnlog";
    }

    //--统计相关-学员日志-管理员
    @RequestMapping("xxrz4user")
    public String toTjXxrz4user(HttpServletRequest request, Model model) {
        //获得参数
        String urlParam=request.getQueryString();
        model.addAttribute("urlParam", urlParam);
        model.addAttribute("type", "3");
        return "/web/learnlog";
    }
}