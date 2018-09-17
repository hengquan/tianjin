package cn.tianjin.unifiedfee.ot.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import cn.tianjin.unifiedfee.ot.entity.Tm;
import cn.tianjin.unifiedfee.ot.mapper.TmMapper;

/**
 * 试卷服务
 */
@Service
public class SjService {
    @Autowired
    private TmMapper dao;
    /**
     * 随机后的所给定的对象所对应题目的Id
     * @param objType
     * @param objId
     * @param tmCount
     * @return
     */
    public List<Tm> getTempSj(String objType, String objId, int tmCount) {
        Map<String, Object> m=new HashMap<String, Object>();
        
        //List<Tm> tl=dao.getTmListByObjId(null);
        return null;
    }
}