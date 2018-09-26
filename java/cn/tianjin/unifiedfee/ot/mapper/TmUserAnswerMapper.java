package cn.tianjin.unifiedfee.ot.mapper;

import cn.tianjin.unifiedfee.ot.entity.TmUserAnswer;

public interface TmUserAnswerMapper {
    int insert(TmUserAnswer record);

    int insertSelective(TmUserAnswer record);
}