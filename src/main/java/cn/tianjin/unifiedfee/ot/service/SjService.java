package cn.tianjin.unifiedfee.ot.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

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
    public List<Map<String,Object>> getTempSj(String objType, String objId, int tmCount) {
        Map<String, Object> m=new HashMap<String, Object>();
        Random ran=new Random();

        List<Tm> tl=dao.getTmListByObjInfo(objType, objId);
        //随机排序，先不随机？
        List<Tm> retTl=new ArrayList<Tm>();
        while (tl.size()>0&&retTl.size()<tmCount) {
            int rIndex=ran.nextInt(tl.size());
            retTl.add(tl.get(rIndex));
            tl.remove(rIndex);
        }
        List<Map<String, Object>> ml=new ArrayList<Map<String, Object>>();
        for (Tm tm: retTl) {
            ;
        }
        return ml;
    }
}