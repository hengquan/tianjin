package cn.tianjin.unifiedfee.ot.mapper;

import java.util.List;
import java.util.Map;

import cn.tianjin.unifiedfee.ot.entity.Kjitem;

public interface KjitemMapper {
    int insert(Kjitem record);

    int insertSelective(Kjitem record);

    List<Kjitem> getPageData(Map<String, Object> param);

    int update(Kjitem entity);

    int delete(Kjitem entity);

    Kjitem get(String id);

    List<Kjitem> selectAllMsg();

    List<Kjitem> getDataListByIds(Kjitem kjitem);

    public List<Kjitem> find4Web(Map<String, Object> param);

    public List<Kjitem> findRefKj4Web(Map<String, Object> param);

    List<Kjitem> getKjList(Integer rownum);

    List<Kjitem> getMyKjList(Map<String, Object> map);
}