package cn.tianjin.unifiedfee.ot.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import cn.tianjin.unifiedfee.ot.entity.CommArchive;
import cn.tianjin.unifiedfee.ot.entity.KjRefSource;
import cn.tianjin.unifiedfee.ot.mapper.CommArchiveMapper;
import cn.tianjin.unifiedfee.ot.util.Onlylogo;

@Service
public class CommArchiveService {
    @Autowired
    private CommArchiveMapper dao;

    // 添加
    public boolean insert(CommArchive commArchive) throws Exception {
        commArchive.setId(Onlylogo.getUUID());
        return dao.insert(commArchive) > 0 ? true : false;
    }

    // 更新
    public boolean update(CommArchive commArchive) throws Exception {
        return dao.update(commArchive) > 0 ? true : false;
    }

    // 删除
    public boolean delete(CommArchive commArchive) throws Exception {
        return dao.delete(commArchive) > 0 ? true : false;
    }

    // 获取单条信息
    public KjRefSource get(CommArchive commArchive) throws Exception {
        return dao.get(commArchive.getId());
    }
}