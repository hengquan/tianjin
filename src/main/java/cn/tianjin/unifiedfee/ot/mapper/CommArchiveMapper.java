package cn.tianjin.unifiedfee.ot.mapper;

import java.util.List;
import java.util.Map;

import cn.tianjin.unifiedfee.ot.entity.CommArchive;
import cn.tianjin.unifiedfee.ot.entity.KjRefSource;

public interface CommArchiveMapper {
    int insert(CommArchive record);

    public List<CommArchive> getArchiveByObjIds(Map<String, Object> param);

    List<CommArchive> selectByObjId(String objId);

    int update(CommArchive commArchive);

    int delete(CommArchive commArchive);

    KjRefSource get(String id);
}