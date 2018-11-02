package cn.tianjin.unifiedfee.ot.mapper;

import java.util.List;
import java.util.Map;

import cn.tianjin.unifiedfee.ot.entity.TmUserAnswer;

public interface TmUserAnswerMapper {
    int insert(TmUserAnswer record);

    int insertSelective(TmUserAnswer record);

    List<TmUserAnswer> getUserAnswerList(Map<String, Object> param);
}