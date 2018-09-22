package cn.tianjin.unifiedfee.ot.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import cn.tianjin.unifiedfee.ot.entity.Tm;
import cn.tianjin.unifiedfee.ot.entity.TmSelect;
import cn.tianjin.unifiedfee.ot.mapper.TmMapper;
import cn.tianjin.unifiedfee.ot.mapper.TmSelectMapper;

/**
 * 试卷服务
 */
@Service
public class SjService {
    @Autowired
    private TmMapper dao;
    @Autowired
    private TmSelectMapper selectDao;

    /**
     * 随机后的所给定的对象所对应题目的Id
     * @param objType
     * @param objId
     * @param tmCount
     * @return
     */
    public List<Map<String,Object>> getTempSj(String refType, String refId, int tmCount) {
        Random ran=new Random();

        Map<String, Object> param=new HashMap<String, Object>();
        param.put("refTabName", "ts_"+refType);
        param.put("refId", refId);
        List<Tm> tl=dao.getTmListByObjInfo(param);
        //随机排序，先不随机？
        List<Tm> retTl=new ArrayList<Tm>();
        while (tl.size()>0&&retTl.size()<tmCount) {
            int rIndex=ran.nextInt(tl.size());
            retTl.add(tl.get(rIndex));
            tl.remove(rIndex);
        }

        //转换为Map，并选择每一道题目的选项，这个会比较慢，先这样实现。
        List<Map<String, Object>> ml=new ArrayList<Map<String, Object>>();
        for (int i=0; i<retTl.size(); i++) {
            Map<String, Object> oneTm=getTmMap(retTl.get(i), i);
            List<TmSelect> selects=selectDao.getselectData(retTl.get(i).getId());
            if (selects!=null&&selects.size()>0) {
                List<Map<String, Object>> tmSelects=new ArrayList<Map<String, Object>>();
                for (int j=0; j<selects.size(); j++) {
                    tmSelects.add(getSelectMap(selects.get(j)));
                }
                oneTm.put("tmItems", tmSelects);
            }
            ml.add(oneTm);
        }
        return ml;
    }
    private Map<String, Object> getTmMap(Tm tm, int i) {
        Map<String, Object> m=new HashMap<String, Object>();
        m.put("tmId", tm.getId());
        m.put("tmIndex", i+1);
        m.put("tmDesc", tm.getTmHtml());
        m.put("tmType", tm.getTmType());
        m.put("tmScore", tm.getScore());
        return m;
    }
    private Map<String, Object> getSelectMap(TmSelect select) {
        Map<String, Object> m=new HashMap<String, Object>();
        m.put("itemSign", select.getTmSelectSign());
        m.put("itemDesc", select.getTmSelectDesc());
        return m;
    }
}