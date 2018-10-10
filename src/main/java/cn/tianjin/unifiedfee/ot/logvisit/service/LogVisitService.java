package cn.tianjin.unifiedfee.ot.logvisit.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.spiritdata.framework.util.SequenceUUID;
import com.spiritdata.framework.util.StringUtils;

import cn.tianjin.unifiedfee.ot.entity.LogVisit;
import cn.tianjin.unifiedfee.ot.mapper.LogVisitMapper;

/**
 * 访问日志管理服务类，提供如下服务：<br/>
 * <pre>
 * 1-与持久化数据交互的功能在这个服务中提供
 * 2-用户切换功能
 * 3-根据日志分类获得分类服务
 * </pre>
 * @author wh
 */
@Service
public class LogVisitService {
    @Autowired
    private LogVisitMapper logDao;

    /**
     * 保存访问日志信息到数据库
     * @param vlp 访问日志信息
     */
    public void Save2DB(LogVisit lv) {
        if (StringUtils.isNullOrEmptyOrSpace(lv.getId())) lv.setId(SequenceUUID.getPureUUID());
        logDao.insert(lv);
    }

    public List<LogVisit> getDataByObjType(Integer state) {
        String objType = "";
        if(state==1){
            objType = "ts_mnsc";
        }else if(state==2){
            objType = "ts_kj";
        }else if(state==3){
            objType = "q_sj";
        }
        return logDao.getDataByObjType(objType);
    }

    public List<LogVisit> getLogVisitList(Integer rownum) {
        return logDao.getLogVisitList(rownum);
    }

    public List<Map<String, Object>> getVisitCountByUi(String userId) {
        return logDao.getVisitCountByUi(userId);
    }

    public List<Map<String, Object>> getMyLogVisitList(Integer rownum, String userId) {
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("rownum", rownum);
        map.put("userId", userId);
        return logDao.getMyLogVisitList(map);
    }

    public List<Map<String, Object>> getXyxxList(Map<String, Object> param) {
        return logDao.getXyxxList(param);
    }

    public List<Map<String, Object>> getXyxxSumList(Map<String, Object> param) {
        return logDao.getXyxxSumList(param);
    }

    public List<Map<String, Object>> getCompUserList(Map<String, Object> param) {
        return logDao.getCompUserList(param);
    }

    public List<Map<String, Object>> getAllVisitCount() {
        return logDao.getAllVisitCount();
    }

    public List<Map<String, Object>> getAllLogVisitList(Integer rownum) {
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("rownum", rownum);
        return logDao.getAllLogVisitList(map);
    }

    public List<Map<String, Object>> getKjStateByCate(int dayCount) {
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("dayCount", dayCount);
        return logDao.getKjStateByCate(map);
    }

    public List<Map<String, Object>> getMnscStateByCate(int dayCount) {
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("dayCount", dayCount);
        return logDao.getMnscStateByCate(map);
    }
}