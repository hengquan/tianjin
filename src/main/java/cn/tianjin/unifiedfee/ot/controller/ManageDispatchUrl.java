package cn.tianjin.unifiedfee.ot.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;


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
public class ManageDispatchUrl {
//=============================以下为页面跳转
    /*后台管理首页 index页*/
    @RequestMapping("page")
    public String page() {
        return "/manage/index";
    }

    //--分类管理
    @RequestMapping("cateList")
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
    
    //--摸拟实操管理
    @RequestMapping("mnscList")
    public String toMnscList() {
        return "/manage/mnsc/mnscList";
    }
    @RequestMapping("mnscSelList")
    public String mnscSelList() {
        return "/manage/mnsc/mnscSelList";
    }
    @RequestMapping("mnsc/mnscList4st")
    public String toMnscList4st() {
        return "/manage/mnsc/mnscList4st";
    }
    @RequestMapping("mnsc/edit")
    public String toMnscEdit(HttpServletRequest request) {
        return "/manage/mnsc/mnscEdit";
    }
    @RequestMapping("mnsc/view")
    public String toMnscView(HttpServletRequest request) {
        return "/manage/mnsc/mnscView";
    }
    
    //--课件管理
    @RequestMapping("kjList")
    public String toKjList() {
        return "/manage/kj/kjList";
    }
    @RequestMapping("kjSelList")
    public String kjSelList() {
        return "/manage/kj/kjSelList";
    }
    @RequestMapping("kj/edit")
    public String toKjEdit(HttpServletRequest request) {
        return "/manage/kj/kjEdit";
    }
    @RequestMapping("kj/view")
    public String toKjView(HttpServletRequest request) {
        return "/manage/kj/kjView";
    }
    @RequestMapping("kj/kjList4st")
    public String toKjList4st() {
        return "/manage/kj/kjList4st";
    }
  //--试题管理
    @RequestMapping("tmList")
    public String toTmList() {
        return "/manage/st/tmList";
    }
    @RequestMapping("st/edit")
    public String toStEdit(HttpServletRequest request) {
        return "/manage/st/tmEdit";
    }
    @RequestMapping("st/view")
    public String toTmView(HttpServletRequest request) {
        return "/manage/st/tmView";
    }

    /*测试页面*/
    @RequestMapping("testTt")
    public String toTestPage() {
        return "/treeTableDemo";
    }
   
//=============================以上为页面跳转

/*
    @Autowired
    private UserInfoService userInfoService;
    @Autowired
    private TmpService tmpService;

	@RequestMapping("str")
	@ResponseBody
	public String test(){
		return "字符串";
	}


    @RequestMapping("index2")
    public String index2(){
        return "/index2";
    }

    @RequestMapping("table")
	public String table(){
		return "/table";
	}
	

    @RequestMapping("str123")
	@ResponseBody
	public Map<String, Object> data(){
		//当前页数
		int pageNum = 1;
		//一页多少条
		int pageSize = 10;
		PageHelper.startPage(pageNum,pageSize);
		Map<String,Object> map = new HashMap<String,Object>();
		List<UserInfo> userInfos = userInfoService.getAll();
		//放入分页
		PageInfo<UserInfo> pageInfo = new PageInfo<UserInfo>(userInfos);
		//返回
		map.put("data", pageInfo);
		return map;
	}
	
    @RequestMapping("mnscList")
    @ResponseBody
    public Map<String, Object> mnscList(){
        //当前页数
        int pageNum = 1;
        //一页多少条
        int pageSize = 10;
        PageHelper.startPage(pageNum,pageSize);
        Map<String,Object> map = new HashMap<String,Object>();
        List<UserInfo> userInfos = userInfoService.getAll();
        //放入分页
        PageInfo<UserInfo> pageInfo = new PageInfo<UserInfo>(userInfos);
        //返回
        map.put("data", pageInfo);
        map=new HashMap<String, Object>();
        map.put("total", 1);
        List l=new ArrayList();
        map.put("rows", l);
        return map;
    }
	
	@RequestMapping("tmp")
	@ResponseBody
	public Map<String, Object> tmp(){
		//当前页数
		int pageNum = 1;
		//一页多少条
		int pageSize = 10;
		PageHelper.startPage(pageNum,pageSize);
		Map<String,Object> map = new HashMap<String,Object>();
		List<Tmp> tmps = tmpService.getAll();
		//放入分页
		PageInfo<Tmp> a = new PageInfo<Tmp>(tmps);
		//返回
		map.put("data", a);
		return map;
	}
*/
}