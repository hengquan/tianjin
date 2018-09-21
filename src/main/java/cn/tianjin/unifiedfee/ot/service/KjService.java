package cn.tianjin.unifiedfee.ot.service;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.taiji.oauthbean.dto.UserInfo;
import cn.tianjin.unifiedfee.ot.entity.Kj;
import cn.tianjin.unifiedfee.ot.entity.KjRefSource;
import cn.tianjin.unifiedfee.ot.entity.Mnsc;
import cn.tianjin.unifiedfee.ot.mapper.KjMapper;
import cn.tianjin.unifiedfee.ot.mapper.KjRefSourceMapper;
import cn.tianjin.unifiedfee.ot.util.Onlylogo;

@Service
public class KjService {
    @Autowired
    private KjMapper dao;
    @Autowired
    private KjRefSourceMapper kjRefDao;

    // 获取分页数据
    public List<Kj> getPageData(Map<String, Object> param) {
        return dao.getPageData(param);
    }

    // 添加
    public boolean insert(Kj kj, UserInfo user) throws Exception {
        kj.setId(Onlylogo.getUUID());
        kj.setCreateBy(user.getUserId());
        kj.setState(2);
        kj.setKjHtml("null");
        kj.setCreateName(user.getUsername());
        Boolean isok = dao.insert(kj) > 0 ? true : false;
        // 添加课件-课件关系表
        // 是否添加成功
        Boolean isAdd = true;
        String ids = kj.getIds();
        if (StringUtils.isNotEmpty(ids)) {
            // 删除
            kjRefDao.deleteByKjId(kj.getId());
            // 添加
            KjRefSource kjRefSource = new KjRefSource();
            String[] split = ids.split(",");
            for (String kjid : split) {
                kjRefSource.setId(Onlylogo.getUUID());
                kjRefSource.setKjId(kj.getId());
                kjRefSource.setRefTabname("ts_kj");
                kjRefSource.setRefId(kjid);
                kjRefSource.setCreateBy(user.getUserId());
                kjRefSource.setCreateName(user.getUsername());
                isAdd = kjRefDao.insert(kjRefSource) > 0 ? true : false;
                if (!isAdd)
                    break;
            }
        }
        return (isok && isAdd);
    }

    // 更新
    public boolean update(Kj kj, UserInfo user) throws Exception {
        // 更新主表
        Boolean isok = dao.update(kj) > 0 ? true : false;
        // 添加课件-课件关系表
        // 是否添加成功
        Boolean isAdd = true;
        String ids = kj.getIds();
        if (StringUtils.isNotEmpty(ids)) {
            // 删除
            kjRefDao.deleteByKjId(kj.getId());
            // 添加
            KjRefSource kjRefSource = new KjRefSource();
            String[] split = ids.split(",");
            for (String kjid : split) {
                kjRefSource.setId(Onlylogo.getUUID());
                kjRefSource.setKjId(kj.getId());
                kjRefSource.setRefTabname("ts_kj");
                kjRefSource.setRefId(kjid);
                kjRefSource.setCreateBy(user.getUserId());
                kjRefSource.setCreateName(user.getUsername());
                isAdd = kjRefDao.insert(kjRefSource) > 0 ? true : false;
                if (!isAdd)
                    break;
            }
        }
        return (isok && isAdd);
    }

    // 删除
    public boolean delete(Kj entity) throws Exception {
        return dao.delete(entity) > 0 ? true : false;
    }

    // 保存
    public Mnsc save(Kj entity) throws Exception {
        return null;
    }

    // 获取单条信息
    public Kj get(Kj kj) throws Exception {
        // 获取主表信息
        kj = dao.get(kj.getId());
        if (kj != null) {
            // 获取课件-课件数据
            List<KjRefSource> KjRefSources = kjRefDao.getDataListByKjId(kj.getId());
            String ids = "";
            if (KjRefSources != null && KjRefSources.size() > 0) {
                for (KjRefSource kjRefSource : KjRefSources) {
                    String refId = kjRefSource.getRefId();
                    if (StringUtils.isNotEmpty(refId)) {
                        ids += "," + refId;
                    }
                }
            }
            if (StringUtils.isNotEmpty(ids)) {
                ids = ids.substring(1);
                kj.setIds(ids);
                List<Kj> kjs = dao.getDataListByIds(kj);
                if (kjs != null && kjs.size() > 0) {
                    kj.setKjs(kjs);
                }
            }
        }
        return kj;
    }

    // 获取全部信息
    public List<Kj> selectAllMsg() {
        return dao.selectAllMsg();
    }

    public List<Kj> getDataListByIds(Kj kj) {
        return dao.getDataListByIds(kj);
    }

    public List<Kj> find4Web(Map<String, Object> param) {
        return dao.find4Web(param);
    }

    public List<Kj> findRefKj4Web(Map<String, Object> param) {
        return dao.findRefKj4Web(param);
    }
}