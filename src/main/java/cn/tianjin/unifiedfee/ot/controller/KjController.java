package cn.tianjin.unifiedfee.ot.controller;

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
import cn.tianjin.unifiedfee.ot.entity.CommArchive;
import cn.tianjin.unifiedfee.ot.entity.Kj;
import cn.tianjin.unifiedfee.ot.service.CommArchiveService;
import cn.tianjin.unifiedfee.ot.service.KjRefSourceService;
import cn.tianjin.unifiedfee.ot.service.KjService;
import cn.tianjin.unifiedfee.ot.util.HttpPush;

@RequestMapping("/kj")
@Controller
public class KjController {
    @Autowired
    private KjService kjService;
    @Autowired
    public UserService userService;
    @Autowired
    public KjRefSourceService kjRefSourceService;
    @Autowired
    private CommArchiveService commArchiveService;

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
        String ids = "";
        String kjName = request.getParameter("kjName");
        String kjCatId = request.getParameter("kjCatId");
        String score = request.getParameter("score");
        String createStart = request.getParameter("createStart");
        String createEnd = request.getParameter("createEnd");
        String kjIds = request.getParameter("kjIds");
        String disable = request.getParameter("disable");
        String isvalid = request.getParameter("isvalid");
        // 传参
        param.put("kjName", kjName);
        param.put("kjCatId", kjCatId);
        param.put("score", score);
        param.put("createStart", createStart);
        param.put("createEnd", createEnd);
        if (StringUtils.isNotEmpty(kjIds)) {
            String[] kjIdList = kjIds.split(",");
            for (String kjId : kjIdList) {
                ids += "," + "'" + kjId + "'";
            }
            if (StringUtils.isNotEmpty(ids))
                ids = ids.substring(1);
        }
        param.put("kjIds", ids);
        param.put("disable", disable);
        param.put("isvalid", isvalid);
        System.out.println("----------------------");
        System.out.println(isvalid);
        System.out.println("----------------------");
        // 查询数据
        List<Kj> kjs = kjService.getPageData(param);
        if (kjs!=null) {
            // 放入分页
            PageInfo<Kj> pageList = new PageInfo<Kj>(kjs);
            // 返回
            map.put("total", pageList.getTotal());
            map.put("rows", pageList.getList());
        } else {
            // 返回
            map.put("total", null);
            map.put("rows", 0);
        }
        return map;
    }

    // 添加
    @RequestMapping("insert")
    @ResponseBody
    public Map<String, Object> insert(Kj kj, HttpServletRequest request, HttpServletResponse response) {
        // 获取参数
        String commArchiveMp4Id = request.getParameter("commArchiveMp4Id") == null ? ""
                : request.getParameter("commArchiveMp4Id").toString();
        String commArchiveImgId = request.getParameter("commArchiveImgId") == null ? ""
                : request.getParameter("commArchiveImgId").toString();
        // 获取用户数据
        UserInfo user = userService.getUserInfo();
        // 返回数据
        Map<String, Object> map = new HashMap<String, Object>();
        // 跨域
        HttpPush.responseInfo(response);
        try {
            // 添加数据
            boolean result = kjService.insert(kj, user);
            String kjId = kj.getId();
            String type = "0";// 0未保存，1已保存
            if (result) {
                if (StringUtils.isNotEmpty(commArchiveMp4Id) && StringUtils.isNotEmpty(commArchiveImgId)) {
                    // 视频
                    CommArchive commArchive = commArchiveService.get(commArchiveMp4Id);
                    commArchive.setObjId(kjId);
                    commArchiveService.update(commArchive, "ts_kj", "main");
                    // 图片
                    CommArchive commArchive1 = commArchiveService.get(commArchiveImgId);
                    commArchive1.setObjId(kjId);
                    commArchiveService.update(commArchive1, "ts_kj", "img");
                    // 已保存
                    type = "1";
                }
                map.put("resultCode", "100");
                map.put("objId", kjId);
                map.put("type", type);
            } else {
                map.put("resultCode", "101");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }

    // 修改
    @RequestMapping("update")
    @ResponseBody
    public Map<String, Object> update(Kj kj, String id, HttpServletRequest request, HttpServletResponse response) {
        // 获取参数
        String commArchiveMp4Id = request.getParameter("commArchiveMp4Id") == null ? ""
                : request.getParameter("commArchiveMp4Id").toString();
        String commArchiveImgId = request.getParameter("commArchiveImgId") == null ? ""
                : request.getParameter("commArchiveImgId").toString();
        // 获取用户数据
        UserInfo user = userService.getUserInfo();
        // 返回数据
        Map<String, Object> map = new HashMap<String, Object>();
        // 跨域
        HttpPush.responseInfo(response);
        try {
            // 更新数据
            boolean result = kjService.update(kj, user);
            String kjId = kj.getId();
            String type = "0";// 0未保存，1已保存
            if (result) {
                if (StringUtils.isNotEmpty(commArchiveMp4Id) && StringUtils.isNotEmpty(commArchiveImgId)) {
                    // 视频
                    CommArchive commArchive = commArchiveService.get(commArchiveMp4Id);
                    commArchive.setObjId(kjId);
                    commArchiveService.update(commArchive, "ts_kj", "main");
                    // 图片
                    CommArchive commArchive1 = commArchiveService.get(commArchiveImgId);
                    commArchive1.setObjId(kjId);
                    commArchiveService.update(commArchive1, "ts_kj", "img");
                    // 已保存
                    type = "1";
                }
                map.put("resultCode", "100");
                map.put("objId", kjId);
                map.put("type", type);
            } else {
                map.put("resultCode", "101");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }

    // 删除
    @RequestMapping("delete")
    @ResponseBody
    public Map<String, Object> delete(Kj kj, HttpServletRequest request, HttpServletResponse response) {
        // 返回数据
        Map<String, Object> map = new HashMap<String, Object>();
        // 跨域
        HttpPush.responseInfo(response);
        try {
            // 逻辑删除数据
            kj.setIsvalid(2);
            boolean result = kjService.updateIsvalid(kj);
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
    public Map<String, Object> get(Kj kj, HttpServletRequest request, HttpServletResponse response) {
        // 返回数据
        Map<String, Object> map = new HashMap<String, Object>();
        // 跨域
        HttpPush.responseInfo(response);
        try {
            // 获取主表数据
            kj = kjService.get(kj);
            map.put("data", kj);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }

    // 查多条记录跟据ids
    @RequestMapping("getDataList")
    @ResponseBody
    public Map<String, Object> getDataList(Kj kj, HttpServletRequest request, HttpServletResponse response) {
        // 返回数据
        Map<String, Object> map = new HashMap<String, Object>();
        // 跨域
        HttpPush.responseInfo(response);
        try {
            // 获取数据
            List<Kj> kjs = kjService.getDataListByIds(kj);
            map.put("dataList", kjs);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }
}
