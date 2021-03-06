package cn.tianjin.unifiedfee.ot.mapper;

import java.util.List;
import java.util.Map;

import cn.tianjin.unifiedfee.ot.entity.Tm;
import cn.tianjin.unifiedfee.ot.entity.TmSelect;

public interface TmSelectMapper {
    int insert(TmSelect record);
    int update(TmSelect record);
    int insertSelective(TmSelect record);
    List<TmSelect> getselectData(String tmId);
    TmSelect get(String id);
    TmSelect getselectBytmid(String tmId);
    void deleteBytmid(String tmid);
    int  delete(TmSelect record);
    List<TmSelect> getselectSign(TmSelect tmselect);
    List<TmSelect> getselectAnswer(TmSelect tmselect);
    List<TmSelect> getselectallAnswer(TmSelect tmselect);
    
}