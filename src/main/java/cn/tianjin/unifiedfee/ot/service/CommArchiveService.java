package cn.tianjin.unifiedfee.ot.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.spiritdata.framework.util.SequenceUUID;

import cn.tianjin.unifiedfee.ot.entity.CommArchive;
import cn.tianjin.unifiedfee.ot.mapper.CommArchiveMapper;

@Service
public class CommArchiveService {
    @Autowired
    private CommArchiveMapper dao;

    // 添加
    public boolean insert(CommArchive commArchive) throws Exception {
        commArchive.setId(SequenceUUID.getPureUUID());
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
    public CommArchive get(CommArchive commArchive) throws Exception {
        return dao.get(commArchive.getId());
    }

    public CommArchive get(String id) {
        return dao.get(id);
    }
}