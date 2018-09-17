package cn.tianjin.unifiedfee.ot.mapper;

import java.util.List;
import java.util.Map;

import cn.tianjin.unifiedfee.ot.entity.CommArchive;

public interface CommArchiveMapper {
    int insert(CommArchive record);

    int insertSelective(CommArchive record);

    public List<CommArchive> getArchiveByObjIds(Map<String, Object> param);
}