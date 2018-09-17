package cn.tianjin.unifiedfee.ot.mapper;

import java.util.List;
import java.util.Map;

import cn.tianjin.unifiedfee.ot.entity.Kj;

public interface KjMapper {
    int insert(Kj record);

    int insertSelective(Kj record);

    List<Kj> getPageData(Map<String, Object> param);

    int update(Kj entity);

    int delete(Kj entity);

    Kj get(String id);

    List<Kj> selectAllMsg();

    List<Kj> getDataListByIds(Kj kj);
}