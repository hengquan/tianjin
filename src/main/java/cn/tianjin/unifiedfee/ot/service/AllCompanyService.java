package cn.tianjin.unifiedfee.ot.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.spiritdata.framework.util.SequenceUUID;

import cn.tianjin.unifiedfee.ot.entity.AllCompany;
import cn.tianjin.unifiedfee.ot.mapper.AllCompanyMapper;

@Service
public class AllCompanyService {
    @Autowired
    private AllCompanyMapper dao;

    // 添加
    public boolean insert(AllCompany allCompany) throws Exception {
        allCompany.setId(SequenceUUID.getPureUUID());
        return dao.insert(allCompany) > 0 ? true : false;
    }

    public Boolean save(List<Map<String, Object>> dataList) {
        Boolean isok = true;
        //清空
        Boolean isdel = dao.deleteAll()>0?true:false;
        //添加
        for (Map<String, Object> data : dataList) {
            System.out.println(data);
            String uniscid = data.get("uniscid") == null ? "" : data.get("uniscid").toString();
            String entname = data.get("entname") == null ? "" : data.get("entname").toString();
            AllCompany allCompany = new AllCompany();
            allCompany.setId(SequenceUUID.getPureUUID());
            allCompany.setEntname(entname);
            allCompany.setUniscid(uniscid);
            isok = dao.insert(allCompany) > 0 ? true : false;
            if (!isok)
                break;
        }
        return isok && isdel;
    }
}