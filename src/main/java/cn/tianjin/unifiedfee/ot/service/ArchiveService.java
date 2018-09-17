package cn.tianjin.unifiedfee.ot.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.tianjin.unifiedfee.ot.entity.CommArchive;
import cn.tianjin.unifiedfee.ot.mapper.CommArchiveMapper;

@Service
public class ArchiveService {
    @Autowired
    private CommArchiveMapper dao;

    public List<CommArchive> getArchiveByObjIds(String objTabName, String archiveType, String objIdSql) {
        Map<String, Object> param=new HashMap<String, Object>();
        param.put("objTabName", objTabName);
        param.put("archiveType", archiveType);
        param.put("objIdSql", objIdSql);
        return dao.getArchiveByObjIds(param);
    }
}
