package cn.tianjin.unifiedfee.ot.mapper;

import cn.tianjin.unifiedfee.ot.entity.CommAudit;

public interface CommAuditMapper {
    int insert(CommAudit record);

    int insertSelective(CommAudit record);
}