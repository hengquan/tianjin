package cn.tianjin.unifiedfee.ot.controller;

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
public class DispatchUrl {

//=============================以下为页面跳转
    /*后台管理首页 index页*/
    @RequestMapping("page")
    public String page() {
        return "/index";
    }

    //--分类管理
    @RequestMapping("cateList")
    public String toCateList() {
        return "/cate/cateList";
    }
    @RequestMapping("cate/edit")
    public String toCateEdit() {
        return "/cate/cateEdit";
    }
    @RequestMapping("cate/view")
    public String toCateView() {
        return "/cate/cateView";
    }
    
    //--摸拟实操管理
    @RequestMapping("mnscList")
    public String toMnscList() {
        return "/mnsc/mnscList";
    }
    @RequestMapping("mnsc/edit")
    public String toMnscEdit() {
        return "/mnsc/mnscEdit";
    }
    @RequestMapping("mnsc/view")
    public String toMnscView() {
        return "/mnsc/mnscView";
    }
    
    //--课件管理
    @RequestMapping("kjList")
    public String toKjList() {
        return "/kj/kjList";
    }
    @RequestMapping("kj/edit")
    public String toKjEdit() {
        return "/kj/kjEdit";
    }
    @RequestMapping("kj/view")
    public String toKjView() {
        return "/kj/kjView";
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