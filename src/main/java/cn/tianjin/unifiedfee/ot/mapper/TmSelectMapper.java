package cn.tianjin.unifiedfee.ot.mapper;

import java.util.List;
import java.util.Map;

import cn.tianjin.unifiedfee.ot.entity.Tm;
import cn.tianjin.unifiedfee.ot.entity.TmSelect;

public interface TmSelectMapper {
    int insert(TmSelect record);
    int insertSelective(TmSelect record);
    List<TmSelect> getselectData(String tmId);
}