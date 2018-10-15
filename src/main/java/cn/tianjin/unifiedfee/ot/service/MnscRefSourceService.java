package cn.tianjin.unifiedfee.ot.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.tianjin.unifiedfee.ot.entity.Kj;
import cn.tianjin.unifiedfee.ot.entity.KjRefSource;
import cn.tianjin.unifiedfee.ot.entity.MnscRefSource;
import cn.tianjin.unifiedfee.ot.mapper.KjMapper;
import cn.tianjin.unifiedfee.ot.mapper.MnscRefSourceMapper;

@Service
public class MnscRefSourceService {
    @Autowired
    private MnscRefSourceMapper dao;
    @Autowired
    private KjMapper kjDao;

    // 添加
    public boolean insert(MnscRefSource mnscRefSource) throws Exception {
        return dao.insert(mnscRefSource) > 0 ? true : false;
    }

    // 更新
    public boolean update(MnscRefSource mnscRefSource) throws Exception {
        return dao.update(mnscRefSource) > 0 ? true : false;
    }

    // 删除
    public boolean delete(MnscRefSource mnscRefSource) throws Exception {
        return dao.delete(mnscRefSource) > 0 ? true : false;
    }

    // 获取多条信息
    public List<MnscRefSource> getDataListByMnscId(MnscRefSource mnscRefSource) throws Exception {
        return dao.getDataListByMnscId(mnscRefSource.getId());
    }

    // 获取多条信息
    public List<Kj> getKjList(String mnscId) throws Exception {
        List<Kj> kjs = new ArrayList<Kj>();
        List<MnscRefSource> mnscRefSources = dao.getDataListByMnscId(mnscId);
        if (mnscRefSources != null && mnscRefSources.size() > 0) {
            String kjids = "";
            for (MnscRefSource mnscRefSource : mnscRefSources) {
                String refId = mnscRefSource.getRefId();
                if (StringUtils.isNotEmpty(refId)) {
                    kjids += "," + refId;
                }
            }
            if(StringUtils.isNotEmpty(kjids)){
                kjids = kjids.substring(1);
                Kj kj = new Kj();
                kj.setIds(kjids);
                kjs = kjDao.getDataListByIds(kj);
            }
        }
        return kjs;
    }
}