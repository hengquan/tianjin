package cn.tianjin.unifiedfee.ot.mapper;

import java.util.List;
import java.util.Map;

import cn.tianjin.unifiedfee.ot.entity.SJ;

public interface SJMapper {
    public int insert(SJ record);

    public int insertSelective(SJ record);

    public SJ getById(String id);

    public int update(SJ sj);

    public List<Map<String, Object>> getSjList4User(Map<String, Object> param);

    public void changeState(Map<String, Object> param);
}