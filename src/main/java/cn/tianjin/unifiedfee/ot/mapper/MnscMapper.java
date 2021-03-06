package cn.tianjin.unifiedfee.ot.mapper;

import java.util.List;
import java.util.Map;

import cn.tianjin.unifiedfee.ot.entity.Mnsc;

public interface MnscMapper {
    int insert(Mnsc record);

	List<Mnsc> getPageData(Map<String, Object> retMap);

	int update(Mnsc entity);

	int delete(Mnsc entity);

	List<Mnsc> selectAllMsg();

	Mnsc get(String id);

    List<Mnsc> getDataListByIds(Mnsc mnsc);

    List<Mnsc> getMnscList(Integer rownum);

    List<Map<String, Object>> getNewMnscList(Map<String, Object> map);

    List<Map<String, Object>> getMnscStateByCate();

    List<Map<String, Object>> getMnscList4Web(Map<String, Object> map);

    List<Map<String, Object>> getMnscPieState();
}