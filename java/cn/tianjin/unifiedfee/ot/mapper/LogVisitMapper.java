package cn.tianjin.unifiedfee.ot.mapper;

import cn.tianjin.unifiedfee.ot.entity.LogVisit;

public interface LogVisitMapper {
    int insert(LogVisit record);

    int insertSelective(LogVisit record);
}