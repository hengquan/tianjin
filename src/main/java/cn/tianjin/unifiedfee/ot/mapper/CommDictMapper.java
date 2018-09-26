package cn.tianjin.unifiedfee.ot.mapper;

import cn.tianjin.unifiedfee.ot.entity.CommDict;

public interface CommDictMapper {
    int insert(CommDict record);

    int insertSelective(CommDict record);
}