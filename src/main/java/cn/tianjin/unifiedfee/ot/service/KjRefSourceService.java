package cn.tianjin.unifiedfee.ot.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.tianjin.unifiedfee.ot.entity.KjRefSource;
import cn.tianjin.unifiedfee.ot.mapper.KjRefSourceMapper;

@Service
public class KjRefSourceService {
    @Autowired
    private KjRefSourceMapper dao;

    // 添加
    public boolean insert(KjRefSource kjRefSource) throws Exception {
        return dao.insert(kjRefSource) > 0 ? true : false;
    }

    // 更新
    public boolean update(KjRefSource kjRefSource) throws Exception {
        return dao.update(kjRefSource) > 0 ? true : false;
    }

    // 删除
    public boolean delete(KjRefSource kjRefSource) throws Exception {
        return dao.delete(kjRefSource) > 0 ? true : false;
    }

    // 获取单条信息
    public KjRefSource get(KjRefSource kjRefSource) throws Exception {
        return dao.get(kjRefSource.getId());
    }

    // 获取KjId获取多条信息
    public List<KjRefSource> getDataListByKjId(String kjId) throws Exception {
        return dao.getDataListByKjId(kjId);
    }
}