package cn.tianjin.unifiedfee.ot.service;

import java.util.HashMap;
import java.util.Map;

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
    public boolean update(CommArchive commArchive, String objTableName, String archiveType) throws Exception {
        // 删除
        delObjTableNameAndObjId(commArchive.getObjId(), objTableName,archiveType);
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

    public Boolean delObjTableNameAndObjId(String kjId, String objTableName, String archiveType) {
        // 删除
        Map<String, Object> parm = new HashMap<String, Object>();
        parm.put("objTabname", objTableName);
        parm.put("objId", kjId);
        parm.put("archiveType", archiveType);
        return dao.delObjTableNameAndObjId(parm) > 0 ? true : false;
    }
}