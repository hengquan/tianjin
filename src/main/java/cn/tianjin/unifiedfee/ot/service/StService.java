package cn.tianjin.unifiedfee.ot.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.taiji.oauthbean.dto.UserInfo;
import cn.tianjin.unifiedfee.ot.entity.Tm;
import cn.tianjin.unifiedfee.ot.entity.TmRefSource;
import cn.tianjin.unifiedfee.ot.entity.TmSelect;
import cn.tianjin.unifiedfee.ot.mapper.TmMapper;
import cn.tianjin.unifiedfee.ot.mapper.TmRefSourceMapper;
import cn.tianjin.unifiedfee.ot.mapper.TmSelectMapper;
import cn.tianjin.unifiedfee.ot.util.Onlylogo;

@Service
public class StService {
	@Autowired
	private TmMapper dao;
    @Autowired
	private TmRefSourceMapper refDao;
    @Autowired
    private TmSelectMapper selectDao;

	// 获取分页数据
	public List<Tm> getPageData(Map<String, Object> param) {
		return dao.getPageData(param);
	}	
	public List<Tm> getPageData4kj(Map<String, Object> param) {
        return dao.getTmListByObjInfo(param);
    }
	// 添加
	public String insert(Tm tm,UserInfo user) throws Exception {
	    tm.setId(Onlylogo.getUUID());
	    tm.setCreateBy(user.getUserId());
        tm.setCreateName(user.getUsername());
        TmRefSource tmRefSource = new TmRefSource();
        TmSelect tmSelect = new TmSelect();      
	    String kjList ;        
	    String mnscList ;
	    kjList = tm.getKjList();   
	    mnscList = tm.getMnscList();    
	    String[] tmp ;
	    if (!kjList.equals("")){
	            tmp = kjList.split(",");            
	            for (int i=0;i<tmp.length;i++) {
	                tmRefSource.setRefId(tmp[i]);
	                tmRefSource.setRefTabname("ts_kj");
	                tmRefSource.setId(Onlylogo.getUUID());
	                tmRefSource.setTmId(tm.getId());
	                refDao.insertSelective(tmRefSource);
	            }
	    }
	        if (!mnscList.equals("")){
	            tmp = mnscList.split(",");
	            for (int i=0;i<tmp.length;i++) {
	                tmRefSource.setRefId(tmp[i]);
	                tmRefSource.setRefTabname("ts_mnsc");
	                tmRefSource.setId(Onlylogo.getUUID());
	                tmRefSource.setTmId(tm.getId());
	                refDao.insertSelective(tmRefSource);        
	            }
	        }
	        /*创建选项实例*/             
	        tmSelect.setTmId(tm.getId());
	        tmSelect.setCreateBy(tm.getCreateBy());
	        tmSelect.setCreateName(tm.getCreateName());
	        /*判断题的处理*/
	        if (tm.getTmType().equals("判断题")){          
	            /*插入正确*/
	            tmSelect.setId(Onlylogo.getUUID());
	            tmSelect.setTmSelectSign("A");
	            tmSelect.setTmSelectDesc("正确");
	            if (tm.getIsAnswer().equals("1"))
	                tmSelect.setIsAnswer(1);
	            else
	                tmSelect.setIsAnswer(0);
	            tmSelect.setSort(1);
	            selectDao.insertSelective(tmSelect); 
	            /*插入错误*/
	            tmSelect.setId(Onlylogo.getUUID());           
	            tmSelect.setTmSelectSign("B");
	            tmSelect.setTmSelectDesc("错误");
	            if (tm.getIsAnswer().equals("2"))
	                tmSelect.setIsAnswer(0);
	            else
	                tmSelect.setIsAnswer(1);
	            tmSelect.setSort(2);
	            selectDao.insertSelective(tmSelect); 
	          }     
	        return dao.insert(tm) >  0 ? tm.getId() : "";
	      
	  }
	
	
	
	//添加选项	
	public boolean insertselect(Tm tm,UserInfo user) throws Exception {
        tm.setCreateBy(user.getUserId());
        tm.setCreateName(user.getUsername());
        TmSelect tmSelect = new TmSelect(); 
        tmSelect.setId(Onlylogo.getUUID());
        tmSelect.setTmId(tm.getId());
        tmSelect.setSort(tm.getSort());
        tmSelect.setTmSelectDesc(tm.getTmSelectDesc());
        tmSelect.setTmSelectSign(tm.getTmSelectSign());
        tmSelect.setIsAnswer(tm.getIsAnswer());
        tmSelect.setCreateBy(tm.getCreateBy());
        tmSelect.setCreateName(tm.getCreateName());        
        return selectDao.insert(tmSelect) > 0 ? true : false;
	}
   	// 更新
	public boolean update(Tm entity) throws Exception {
	    String kjList ;
	    Tm tm = entity;
        kjList = tm.getKjList();    
        String mnscList ;
        mnscList = tm.getMnscList();    
        String[] tmp ;
        tmp = kjList.split(",");
        TmRefSource tmRefSource = new TmRefSource();        
        if (!tmp[0].equals("")) {
            refDao.deleteBytmid(tm.getId());
            for (int i=0;i<tmp.length;i++) {
                tmRefSource.setRefId(tmp[i]);
                tmRefSource.setRefTabname("ts_kj");
                tmRefSource.setId(Onlylogo.getUUID());
                tmRefSource.setTmId(tm.getId());
                refDao.insertSelective(tmRefSource);
            }
        }
        tmp = mnscList.split(",");
        if (!tmp[0].equals("")) {
            refDao.deleteBytmid(tm.getId());
            for (int i=0;i<tmp.length;i++) {
                tmRefSource.setRefId(tmp[i]);
                tmRefSource.setRefTabname("ts_mnsc");
                tmRefSource.setId(Onlylogo.getUUID());
                tmRefSource.setTmId(tm.getId());
                refDao.insertSelective(tmRefSource);        
            }
        }
        /*判断题的处理*/
        TmSelect tmSelect = new TmSelect(); 
        tmSelect.setTmId(tm.getId());
        tmSelect.setCreateBy(tm.getCreateBy());
        tmSelect.setCreateName(tm.getCreateName());
        if (tm.getTmType() != null) {
          if (tm.getTmType().equals("判断题")){   
            /*先进行删除操作*/
            selectDao.deleteBytmid(tm.getId());
            /*插入正确*/
            tmSelect.setId(Onlylogo.getUUID());
            tmSelect.setTmSelectSign("A");
            tmSelect.setTmSelectDesc("正确");
            if (tm.getIsAnswer().equals("1"))
                tmSelect.setIsAnswer(1);
            else
                tmSelect.setIsAnswer(0);
            tmSelect.setSort(1);
            selectDao.insertSelective(tmSelect); 
            /*插入错误*/
            tmSelect.setId(Onlylogo.getUUID());           
            tmSelect.setTmSelectSign("B");
            tmSelect.setTmSelectDesc("错误");
            if (tm.getIsAnswer().equals("2"))
                tmSelect.setIsAnswer(0);
            else
                tmSelect.setIsAnswer(1);
            tmSelect.setSort(2);
            selectDao.insertSelective(tmSelect); 
          }   
        }
		return dao.update(entity) > 0 ? true : false;
	}
    //修改选项和答案 updateSelct
	public boolean updateSelct(TmSelect entity) throws Exception {
       return selectDao.update(entity) > 0 ? true : false;
    }
	// 删除
	public boolean delete(Tm entity) throws Exception {
		return dao.delete(entity) > 0 ? true : false;
	}

	// 保存
	public Tm save(Tm entity) throws Exception {
		return null;
	}

	// 获取单条信息
	public Tm get(Tm entity) throws Exception {
	    Tm tm = new Tm();
	    tm = dao.get(entity.getId());
	    List<TmRefSource> tmRefSource ;
	    tmRefSource = refDao.getBytmid(tm.getId());
	    String kjName = "" ;
	    String mnscName = "";
	    String kjList = "" ;
        String mnscList = "";
	    for (int i=0;i<tmRefSource.size();i++) {
	        if (tmRefSource.get(i).getRefTabname().equals("ts_kj")) {
	            kjList += ","+tmRefSource.get(i).getRefId();
	            kjName += ","+tmRefSource.get(i).getRefname();
	        }
	        if (tmRefSource.get(i).getRefTabname().equals("ts_mnsc")) {
	            mnscList  += "," +tmRefSource.get(i).getRefId();
	            mnscName += ","+tmRefSource.get(i).getRefname();
	        }
	    }
	    if (!kjList.equals("")) {
	        kjList=kjList.substring(1);
	        kjName=kjName.substring(1);
	    }
	    if (!mnscList.equals("")) {
	        mnscList=mnscList.substring(1);
	        mnscName=mnscName.substring(1);
	    }
	    tm.setKjList(kjList);
	    tm.setMnscList(mnscList);
	    tm.setKjnameList(kjName);
	    tm.setMnscnameList(mnscName);
	    TmSelect tmSelect ;
	    //获得选择题的答案
	    tmSelect = selectDao.getselectBytmid(tm.getId());
	    if ( tmSelect != null ) {
	        tm.setTmSelectDesc(tmSelect.getTmSelectDesc());
	        tm.setIsAnswer(tmSelect.getIsAnswer());
	    }
		return tm;
	}

	// 获取全部信息
	public List<Tm> selectAllMsg() {
		return dao.selectAllMsg();
	}
	//获取选项信息
	public List<TmSelect> getselectData (Tm tm) {	 
	    return selectDao.getselectData(tm.getId());
	}
	public TmSelect getselect(TmSelect entity) throws Exception {
	    TmSelect tm = new TmSelect();
        tm = selectDao.get(entity.getId());
        return tm; 
	}
	public boolean deleteSelect(TmSelect entity) throws Exception {
        return selectDao.delete(entity) > 0 ? true : false;   
    }
}