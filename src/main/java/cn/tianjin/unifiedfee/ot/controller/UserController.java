package cn.tianjin.unifiedfee.ot.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.taiji.format.result.ObjectResponseResult;
import cn.taiji.oauthbean.dto.UserInfo;
import cn.taiji.system.domain.SysResource;
import cn.taiji.web.menu.service.SecurityMenuService;
import cn.taiji.web.security.UserService;
import cn.tianjin.unifiedfee.ot.util.HttpPush;

@RequestMapping("/user")
@Controller
@Configuration
public class UserController {
    @Autowired // 注入Service
    public UserService userService;
    @Autowired // 注入Service
    public SecurityMenuService securityMenuService;

    @Value("${ot-server.manage.role.name}")
    private String trainNameId;

    // 获取分页数据
    @RequestMapping("getUserMenu")
    @ResponseBody
    public Map<String, Object> getPageData(HttpServletRequest request, HttpServletResponse response) {
        this.trainNameId="企业门户（chenph）";
        HttpPush.responseInfo(response);//跨域

        Map<String, Object> retMap=new HashMap<String, Object>();
        try {
            UserInfo ui=userService.getUserInfo();
            if (ui==null) {
                retMap.put("returnCode","02");
                retMap.put("messageInfo","无用户登录");
                return retMap;
            }
            retMap.put("username", ui.getUsername());
            retMap.put("userImg", ui.getClass());
            ObjectResponseResult<List<SysResource>> result=securityMenuService.findMenuByUsername(ui.getUsername());
            boolean hadTrain=false;
            if (result!=null&&result.getData()!=null&&result.getData().size()>0) {
                List<SysResource> l=findTrainMenu(result.getData(), trainNameId);
                if (l!=null) {
                    retMap.put("returnCode", "00");
                    retMap.put("data", l);
                    hadTrain=true;
               }
            }
            if (!hadTrain) {
                retMap.put("returnCode", "99");
                retMap.put("messageInfo", "未获得任何菜单");
            }
        } catch(Exception e) {
            e.printStackTrace();
            retMap.put("returnCode","01");
            retMap.put("messageInfo",e.toString());
        }
        return retMap;
    }

    private List<SysResource> findTrainMenu(List<SysResource> lsr, String roleName) {
        List<SysResource> retl=null;
        for (SysResource sr: lsr) {
            if (sr.getResourcesName().equals(roleName)) {
                return sr.getChildren();
            } else {
                if (sr.getChildren()==null||sr.getChildren().size()==0) return null;
                retl=findTrainMenu(sr.getChildren(), roleName);
                if (retl!=null) return retl;
            }
        }
        return null;
    }
}