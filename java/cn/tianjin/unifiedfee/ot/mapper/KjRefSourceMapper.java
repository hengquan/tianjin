package cn.tianjin.unifiedfee.ot.mapper;

import java.util.List;

import cn.tianjin.unifiedfee.ot.entity.KjRefSource;

public interface KjRefSourceMapper {
    int insert(KjRefSource record);

    int deleteByKjId(String id);

    int update(KjRefSource record);

    int delete(KjRefSource kjRefSource);

    KjRefSource get(String id);

    List<KjRefSource> getDataListByKjId(String kjId);
}