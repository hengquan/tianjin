package cn.tianjin.unifiedfee.ot.mapper;

import cn.tianjin.unifiedfee.ot.entity.TmAnswer;

public interface TmAnswerMapper {
    int insert(TmAnswer record);

    int insertSelective(TmAnswer record);
}