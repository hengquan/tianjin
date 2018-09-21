package cn.tianjin.unifiedfee.ot.mapper;

import java.util.List;

import cn.tianjin.unifiedfee.ot.entity.TmRefSource;

public interface TmRefSourceMapper {
    int insert(TmRefSource record);
    int insertSelective(TmRefSource record);
    List<TmRefSource> getBytmid(String tmid);
    void deleteBytmid(String tmid);
    
    
}