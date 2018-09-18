package cn.tianjin.unifiedfee.ot.mapper;

import java.util.List;

import cn.tianjin.unifiedfee.ot.entity.MnscRefSource;

public interface MnscRefSourceMapper {
    int insert(MnscRefSource record);

    int insertSelective(MnscRefSource record);

    int deleteByMnscId(String id);

    List<MnscRefSource> getDataListByMnscId(String mnscId);
}