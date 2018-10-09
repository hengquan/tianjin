package cn.tianjin.unifiedfee.ot.mapper;

import java.util.List;
import java.util.Map;

import cn.tianjin.unifiedfee.ot.entity.LogVisit;

public interface LogVisitMapper {
    int insert(LogVisit record);

    List<LogVisit> getDataByObjType(String objType);

    List<LogVisit> getLogVisitList(Integer rownum);

    List<LogVisit> getDataByObjId(String objId);

    List<Map<String, Object>> getVisitCountByUi(String userId);

    List<Map<String, Object>> getMyLogVisitList(Map<String, Object> map);

    List<Map<String, Object>> getXyxxList(Map<String, Object> param);

    List<Map<String, Object>> getXyxxSumList(Map<String, Object> param);

    List<Map<String, Object>> getCompUserList(Map<String, Object> param);
}