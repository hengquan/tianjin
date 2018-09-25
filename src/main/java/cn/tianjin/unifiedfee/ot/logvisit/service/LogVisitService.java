package cn.tianjin.unifiedfee.ot.logvisit.service;

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
}