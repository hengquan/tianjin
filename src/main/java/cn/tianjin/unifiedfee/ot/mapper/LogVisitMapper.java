package cn.tianjin.unifiedfee.ot.mapper;

import java.util.List;
import java.util.Map;

import cn.tianjin.unifiedfee.ot.entity.LogVisit;

public interface LogVisitMapper {
    int insert(LogVisit record);

    List<LogVisit> getDataByObjType(String objType);

    List<LogVisit> getLogVisitList(Integer rownum);

    List<LogVisit> getDataByObjId(String objId);

    Map<String, Object> getVisitCountByUi(String userId);
}