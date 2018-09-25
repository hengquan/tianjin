package cn.tianjin.unifiedfee.ot.mapper;

import cn.tianjin.unifiedfee.ot.entity.SJ;

public interface SJMapper {
    public int insert(SJ record);

    public int insertSelective(SJ record);

    public SJ getById(String id);

    public int update(SJ sj);
}