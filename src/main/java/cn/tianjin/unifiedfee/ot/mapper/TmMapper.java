package cn.tianjin.unifiedfee.ot.mapper;

import java.util.List;
import java.util.Map;


import cn.tianjin.unifiedfee.ot.entity.Tm;

public interface TmMapper {
    int insert(Tm record);
    int update(Tm record);
    int delete(Tm record);
    int insertSelective(Tm record);    
    List<Tm> getPageData(Map<String, Object> param);
    Tm get(String id);
    List<Tm> selectAllMsg();

    public List<Tm> getTmListByObjInfo(Map<String, Object> param);
    public List<Tm> getTmList4sj(Map<String, Object> param);
    public List<Tm> getTmListTySjId(String id);
}