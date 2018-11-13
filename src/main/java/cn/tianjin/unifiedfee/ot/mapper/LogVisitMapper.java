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

    List<Map<String, Object>> getAllVisitCount();

    List<Map<String, Object>> getAllLogVisitList(Map<String, Object> param);

    List<Map<String, Object>> getKjStateByCate(Map<String, Object> map);

    List<Map<String, Object>> getMnscStateByCate(Map<String, Object> map);

    List<Map<String, Object>> getXxrzList(Map<String, Object> param);

    List<LogVisit> getcatkjtj(Map<String, Object> param);
    List<LogVisit> getcatmnsctj(Map<String, Object> param);
    List<LogVisit> getcatcomptj(Map<String, Object> param);

    int getVisitCount(Map<String, Object> param);

    List<Map<String, Object>> getLateDay(Map<String, Object> param);

    List<Map<String, Object>> getDistinctCamp();

    List<Map<String, Object>> getQytjList(Map<String, Object> param);

    List<Map<String, Object>> getQytjSumList(Map<String, Object> param);
}