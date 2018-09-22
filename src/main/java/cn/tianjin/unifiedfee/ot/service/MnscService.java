package cn.tianjin.unifiedfee.ot.service;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.taiji.oauthbean.dto.UserInfo;
import cn.tianjin.unifiedfee.ot.entity.CommArchive;
import cn.tianjin.unifiedfee.ot.entity.Kj;
import cn.tianjin.unifiedfee.ot.entity.Mnsc;
import cn.tianjin.unifiedfee.ot.entity.MnscRefSource;
import cn.tianjin.unifiedfee.ot.mapper.CommArchiveMapper;
import cn.tianjin.unifiedfee.ot.mapper.KjMapper;
import cn.tianjin.unifiedfee.ot.mapper.MnscMapper;
import cn.tianjin.unifiedfee.ot.mapper.MnscRefSourceMapper;
import cn.tianjin.unifiedfee.ot.util.Onlylogo;

@Service
public class MnscService {
    @Autowired
    private MnscMapper dao;
    @Autowired
    private KjMapper kjDao;
    @Autowired
    private MnscRefSourceMapper mnscRefDao;
    @Autowired
    private CommArchiveMapper commArchiveMapper;

    // 获取分页数据
    public List<Mnsc> getPageData(Map<String, Object> retMap) {
        List<Mnsc> mnscs = dao.getPageData(retMap);
        if (mnscs != null && mnscs.size() > 0) {
            for (Mnsc mnsc : mnscs) {
                String id = mnsc.getId();
                // 查附件表
                List<CommArchive> commArchives = commArchiveMapper.selectByObjId(id);
                if (commArchives != null && commArchives.size() > 0) {
                    for (CommArchive commArchive : commArchives) {
                        String archiveType = commArchive.getArchiveType();
                        if (StringUtils.isNotEmpty(archiveType)) {
                            if (archiveType.equals("main")) {
                                String fileUrl = commArchive.getFileUrl();
                                if (StringUtils.isNotEmpty(fileUrl))
                                    mnsc.setMainUrl(fileUrl);
                            }
                        }
                    }
                }
            }
        }
        return mnscs;
    }

    // 添加
    public boolean insert(Mnsc mnsc, UserInfo user) throws Exception {
        mnsc.setId(Onlylogo.getUUID());
        mnsc.setState(2);
        mnsc.setCreateBy(user.getUserId());
        mnsc.setCreateName(user.getUsername());
        Boolean isok = dao.insert(mnsc) > 0 ? true : false;
        // 添加摸拟实操-课件关系表
        // 是否添加成功
        Boolean isAdd = true;
        String ids = mnsc.getKjids();
        if (StringUtils.isNotEmpty(ids)) {
            // 删除
            mnscRefDao.deleteByMnscId(mnsc.getId());
            // 添加
            MnscRefSource mnscRefSource = new MnscRefSource();
            String[] split = ids.split(",");
            for (String kjid : split) {
                mnscRefSource.setId(Onlylogo.getUUID());
                mnscRefSource.setMnscId(mnsc.getId());
                mnscRefSource.setRefTabname("ts_mnsc");
                mnscRefSource.setRefId(kjid);
                mnscRefSource.setCreateBy(user.getUserId());
                mnscRefSource.setCreateName(user.getUsername());
                isAdd = mnscRefDao.insert(mnscRefSource) > 0 ? true : false;
                if (!isAdd)
                    break;
            }
        }
        return isok && isAdd;
    }

    // 更新
    public boolean update(Mnsc mnsc, UserInfo user) throws Exception {
        // 更新主表
        Boolean isok = dao.update(mnsc) > 0 ? true : false;
        // 添加摸拟实操-课件关系表
        // 是否添加成功
        Boolean isAdd = true;
        String ids = mnsc.getKjids();
        if (StringUtils.isNotEmpty(ids)) {
            // 删除
            mnscRefDao.deleteByMnscId(mnsc.getId());
            // 添加
            MnscRefSource mnscRefSource = new MnscRefSource();
            String[] split = ids.split(",");
            for (String kjid : split) {
                mnscRefSource.setId(Onlylogo.getUUID());
                mnscRefSource.setMnscId(mnsc.getId());
                mnscRefSource.setRefTabname("ts_kj");
                mnscRefSource.setRefId(kjid);
                mnscRefSource.setCreateBy(user.getUserId());
                mnscRefSource.setCreateName(user.getUsername());
                isAdd = mnscRefDao.insert(mnscRefSource) > 0 ? true : false;
                if (!isAdd)
                    break;
            }
        }
        return isok && isAdd;
    }

    // 删除
    public boolean delete(Mnsc entity) throws Exception {
        return dao.delete(entity) > 0 ? true : false;
    }

    // 保存
    public Mnsc save(Mnsc entity) throws Exception {
        return null;
    }

    // 获取单条信息
    public Mnsc get(Mnsc mnsc) throws Exception {
        mnsc = dao.get(mnsc.getId());
        if (mnsc != null) {
            // 获取摸拟实操-课件数据
            List<MnscRefSource> mnscRefSources = mnscRefDao.getDataListByMnscId(mnsc.getId());
            String kjids = "";
            if (mnscRefSources != null && mnscRefSources.size() > 0) {
                for (MnscRefSource mnscRefSource : mnscRefSources) {
                    String refId = mnscRefSource.getRefId();
                    if (StringUtils.isNotEmpty(refId)) {
                        kjids += "," + refId;
                    }
                }
            }
            if (StringUtils.isNotEmpty(kjids)) {
                kjids = kjids.substring(1);
                mnsc.setKjids(kjids);
                // 实例化课件
                Kj kj = new Kj();
                kj.setIds(kjids);
                List<Kj> kjs = kjDao.getDataListByIds(kj);
                if (kjs != null && kjs.size() > 0)
                    mnsc.setKjs(kjs);
            }
        }
        return mnsc;
    }

    // 获取全部信息
    public List<Mnsc> selectAllMsg() {
        return dao.selectAllMsg();
    }

    public Mnsc get(String mnscId) {
        return dao.get(mnscId);
    }

    public List<Mnsc> getDataListByIds(Mnsc mnsc) {
        return dao.getDataListByIds(mnsc);
    }
}