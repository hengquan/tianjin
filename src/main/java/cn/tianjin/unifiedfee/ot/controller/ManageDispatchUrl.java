package cn.tianjin.unifiedfee.ot.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.taiji.format.result.ObjectResponseResult;
import cn.taiji.oauthbean.dto.UserInfo;
import cn.taiji.system.domain.SysResource;
import cn.taiji.web.menu.service.SecurityMenuService;
import cn.taiji.web.security.UserService;
import cn.tianjin.unifiedfee.ot.entity.CommArchive;
import cn.tianjin.unifiedfee.ot.mapper.CommArchiveMapper;

//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//import org.springframework.web.bind.annotation.ResponseBody;
//import com.github.pagehelper.PageHelper;
//import com.github.pagehelper.PageInfo;
//import cn.tianjin.unifiedfee.ot.entity.Tmp;
//import cn.tianjin.unifiedfee.ot.entity.UserInfo;
//import org.springframework.beans.factory.annotation.Autowired;
//import cn.tianjin.unifiedfee.ot.service.TmpService;
//import cn.tianjin.unifiedfee.ot.service.UserInfoService;

@Controller
@Configuration
public class ManageDispatchUrl {
    @Autowired // 注入Service
    public UserService userService;
    @Autowired // 注入Service
    public SecurityMenuService securityMenuService;
    @Autowired // 注入Service
    public CommArchiveMapper commArchiveMapper;

    @Value("${ot-server.prefix}")
    private String prefix;
    @Value("${ot-server.manage.role.name}")
    private String trainNameId;

    // =============================以下为页面跳转
    /* 后台管理首页 index页 */
    @RequestMapping(value = { "/index", "/" })
    public String page(Model model) {
        // 获得权限信息，并回写到对象中
        // trainNameId="企业门户（chenph）";
        trainNameId = "线上培训子系统（chenph）";
        String menuHtml = "", indexHtml = "";
        try {
            UserInfo ui = userService.getUserInfo();
            if (ui != null) {
                model.addAttribute("username", ui.getUsername());
                model.addAttribute("userImg",
                        "http://1.202.219.107:8088/pm-server-innerweb/src/images/defaultAvatar@2x.png");
                ObjectResponseResult<List<SysResource>> result = securityMenuService
                        .findMenuByUsername(ui.getUsername());
                if (result != null && result.getData() != null && result.getData().size() > 0) {
                    List<SysResource> l = findTrainMenu(result.getData(), trainNameId);
                    /**
                     * 测试代码 List<SysResource> _ret=new ArrayList<SysResource>();
                     * for (SysResource s: l) if
                     * (s.getResourcesName().equals("培训中心"))
                     * {_ret.add(s);break;} l=_ret;
                     */
                    if (l != null) {
                        for (int i = 0; i < l.size(); i++) {
                            SysResource m1 = l.get(i);
                            if (m1.getResourcesName().equals("首页")) {
                                indexHtml = "<li class='pt-menu-list'><span style='width: 14px;height: 14px;position: absolute;left: 12px;' aria-hidden='true'><img src='"
                                        + prefix + "/src/images/house.png' width='100%'></span>";
                                indexHtml += "<h3 class='pt-menu-title'><a href='javascript:void(0);' onclick='showMenu(\""
                                        + prefix
                                        + "/mainpage"/* +m1.getResourceUrl() */ + "\")'>" + m1.getResourcesName()
                                        + "</a></h3>";
                                indexHtml += "</li>";
                            } else if (m1.getChildren() != null && m1.getChildren().size() > 0) {
                                menuHtml += "<li class='pt-menu-list'><span class='glyphicon glyphicon-triangle-right' aria-hidden='true'></span>"
                                        + "<h3 class='pt-menu-title'><a href='#'>" + m1.getResourcesName()
                                        + "</a></h3>";
                                menuHtml += "<ul class='pt-second-menu'>";
                                for (int j = 0; j < m1.getChildren().size(); j++) {
                                    SysResource m2 = m1.getChildren().get(j);
                                    menuHtml += "<li class='pt-menu-child'><span class='glyphicon  glyphicon-th-large' aria-hidden='true'></span>"
                                            // +"<h3 class='pt-menu-title'><a
                                            // href='javascript:void(0);'
                                            // onclick='showMenu(\""+m2.getSysName()+"\\"+m2.getResourceUrl()+"\")'>"+m2.getResourcesName()+"</a></h3></li>";
                                            + "<h3 class='pt-menu-title'><a href='javascript:void(0);' onclick='showMenu(\""
                                            + prefix + "" + m2.getResourceUrl() + "\")'>" + m2.getResourcesName()
                                            + "</a></h3></li>";
                                }
                                menuHtml += "</ul></li>";
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (!StringUtils.isBlank(indexHtml))
            menuHtml = indexHtml + menuHtml;
        model.addAttribute("menuHtml", menuHtml);
        return "/manage/index";
    }

    private List<SysResource> findTrainMenu(List<SysResource> lsr, String roleName) {
        List<SysResource> retl = null;
        for (SysResource sr : lsr) {
            if (sr.getResourcesName().equals(roleName)) {
                return sr.getChildren();
            } else {
                if (sr.getChildren() == null || sr.getChildren().size() == 0)
                    return null;
                retl = findTrainMenu(sr.getChildren(), roleName);
                if (retl != null)
                    return retl;
            }
        }
        return null;
    }

    // --分类管理
    @RequestMapping("/mainpage")
    public String toMainPage() {
        return "/manage/mainpage";
    }

    @RequestMapping("cate/list")
    public String toCateList() {
        return "/manage/cate/cateList";
    }

    @RequestMapping("cate/edit")
    public String toCateEdit() {
        return "/manage/cate/cateEdit";
    }

    @RequestMapping("cate/view")
    public String toCateView() {
        return "/manage/cate/cateView";
    }

    // --摸拟实操管理
    @RequestMapping("mnsc/list")
    public String toMnscList() {
        return "/manage/mnsc/mnscList";
    }

    @RequestMapping("mnsc/selList")
    public String mnscSelList() {
        return "/manage/mnsc/mnscSelList";
    }

    @RequestMapping("mnsc/list4st")
    public String toMnscList4st() {
        return "/manage/mnsc/mnscList4st";
    }

    @RequestMapping("mnsc/edit")
    public String toMnscEdit(HttpServletRequest request, Model model) {
        String objId = request.getParameter("id");
        String imgJsons = "";
        if (StringUtils.isNotEmpty(objId)) {
            // 处理相关附件
            List<CommArchive> commArchives = commArchiveMapper.selectByObjId(objId);
            if (commArchives != null && commArchives.size() > 0) {
                for (CommArchive commArchive : commArchives) {
                    String archiveType = commArchive.getArchiveType();
                    if (archiveType.equals("img")) {
                        String fileUrl = commArchive.getFileUrl();
                        if (StringUtils.isNotEmpty(fileUrl)) {
                            String src = "<img src='" + fileUrl + "' class='file-preview-image'>";
                            imgJsons += "," + src;
                        }
                    }
                }
            }
        }
        if (StringUtils.isNotEmpty(imgJsons))
            imgJsons = imgJsons.substring(1);
        model.addAttribute("imgJson", imgJsons);
        return "/manage/mnsc/mnscEdit";
    }

    @RequestMapping("mnsc/view")
    public String toMnscView(HttpServletRequest request) {
        return "/manage/mnsc/mnscView";
    }

    // --课件管理
    @RequestMapping("kj/list")
    public String toKjList() {
        return "/manage/kj/kjList";
    }

    @RequestMapping("kj/selList")
    public String kjSelList() {
        return "/manage/kj/kjSelList";
    }

    @RequestMapping("kj/edit")
    public String toKjEdit(HttpServletRequest request, Model model) {
        String objId = request.getParameter("id");
        String imgJsons = "";
        String videoJsons = "";
        if (StringUtils.isNotEmpty(objId)) {
            // 处理相关附件
            List<CommArchive> commArchives = commArchiveMapper.selectByObjId(objId);
            if (commArchives != null && commArchives.size() > 0) {
                for (CommArchive commArchive : commArchives) {
                    String archiveType = commArchive.getArchiveType();
                    if (archiveType.equals("img")) {
                        String fileUrl = commArchive.getFileUrl();
                        if (StringUtils.isNotEmpty(fileUrl)) {
                            String src = "<img src='" + fileUrl + "' class='file-preview-image'>";
                            imgJsons += "," + src;
                        }
                    } else if (archiveType.equals("main")) {
                        String fileUrl = commArchive.getFileUrl();
                        if (StringUtils.isNotEmpty(fileUrl)) {
                            String src = "<video height='160px' width='213px' controls class='file-preview-image'>";
                            src += "<source src='" + fileUrl + "' type='video/mp4'>";
                            src += "</video>";
                            videoJsons += "," + src;
                        }
                    }
                }
            }
        }
        if (StringUtils.isNotEmpty(imgJsons))
            imgJsons = imgJsons.substring(1);
        if (StringUtils.isNotEmpty(videoJsons))
            videoJsons = videoJsons.substring(1);
        model.addAttribute("imgJson", imgJsons);
        model.addAttribute("videoJson", videoJsons);
        return "/manage/kj/kjEdit";
    }

    @RequestMapping("kj/view")
    public String toKjView(HttpServletRequest request) {
        return "/manage/kj/kjView";
    }

    @RequestMapping("kj/list4st")
    public String toKjList4st() {
        return "/manage/kj/kjList4st";
    }

    // --试题管理
    @RequestMapping("st/list")
    public String toStList() {
        return "/manage/st/tmList";
    }

    @RequestMapping("st/list4kt")
    public String toStList4kj() {
        return "/manage/st/tmList4kj";
    }

    @RequestMapping("st/edit")
    public String toStEdit(HttpServletRequest request) {
        return "/manage/st/tmEdit";
    }

    @RequestMapping("st/view")
    public String toStView(HttpServletRequest request) {
        return "/manage/st/tmView";
    }

    @RequestMapping("st/selectEdit")
    public String toStselectEdit(HttpServletRequest request) {
        return "/manage/st/selectEdit";
    }

    // =============================为配合目前部署，所修改的信息===========
    // --分类管理
    @RequestMapping("cateList")
    public String toDelCateList() {
        return "/manage/cate/cateList";
    }

    // --摸拟实操管理
    @RequestMapping("mnscList")
    public String toDelMnscList() {
        return "/manage/mnsc/mnscList";
    }

    // --课件管理
    @RequestMapping("kjList")
    public String toDelKjList() {
        return "/manage/kj/kjList";
    }

    // --试题管理
    @RequestMapping("tmList")
    public String toDelTmList() {
        return "/manage/st/tmList";
    }

}