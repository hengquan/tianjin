package cn.tianjin.unifiedfee.ot.service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.spiritdata.framework.util.SequenceUUID;

import cn.taiji.oauthbean.dto.UserInfo;
import cn.tianjin.unifiedfee.ot.entity.CommArchive;
import cn.tianjin.unifiedfee.ot.entity.Kj;
import cn.tianjin.unifiedfee.ot.entity.LogVisit;
import cn.tianjin.unifiedfee.ot.entity.Mnsc;
import cn.tianjin.unifiedfee.ot.entity.MnscRefSource;
import cn.tianjin.unifiedfee.ot.mapper.CommArchiveMapper;
import cn.tianjin.unifiedfee.ot.mapper.KjMapper;
import cn.tianjin.unifiedfee.ot.mapper.LogVisitMapper;
import cn.tianjin.unifiedfee.ot.mapper.MnscMapper;
import cn.tianjin.unifiedfee.ot.mapper.MnscRefSourceMapper;

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
    @Autowired
    private LogVisitMapper logVisitMapper;

    // 获取分页数据
    public List<Mnsc> getPageData(Map<String, Object> retMap) {
        List<Mnsc> mnscs = dao.getPageData(retMap);
        if (mnscs != null && mnscs.size() > 0) {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            for (Mnsc mnsc : mnscs) {
                String id = mnsc.getId();
                // 查附件表
                List<CommArchive> commArchives = commArchiveMapper.selectByObjId(id);
                if (commArchives != null && commArchives.size() > 0) {
                    for (CommArchive commArchive : commArchives) {
                        String archiveType = commArchive.getArchiveType();
                        if (StringUtils.isNotEmpty(archiveType)) {
                            if (archiveType.equals("img")) {
                                String fileUrl = commArchive.getFileUrl();
                                if (StringUtils.isNotEmpty(fileUrl))
                                    mnsc.setMainUrl(fileUrl);
                            }
                        }
                    }
                }
                // 更改日期格式
                Date createDate = mnsc.getCreateDate();
                String createdate = format.format(createDate);
                if (StringUtils.isNotEmpty(createdate))
                    mnsc.setCreatedate(createdate);
            }
        }
        return mnscs;
    }

    // 添加
    public boolean insert(Mnsc mnsc, UserInfo user) throws Exception {
        mnsc.setId(SequenceUUID.getPureUUID());
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
                mnscRefSource.setId(SequenceUUID.getPureUUID());
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
                mnscRefSource.setId(SequenceUUID.getPureUUID());
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
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
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
                Kj oneKj = new Kj();
                oneKj.setIds(kjids);
                List<Kj> kjs = kjDao.getDataListByIds(oneKj);
                if (kjs != null && kjs.size() > 0) {
                    for (Kj kj : kjs) {
                        Date createDate = kj.getCreateDate();
                        String createdate = format.format(createDate);
                        if (StringUtils.isNotEmpty(createdate))
                            kj.setCreatedate(createdate);
                    }
                    mnsc.setKjs(kjs);
                }
            }
        }
        //获取相当附件信息
        List<CommArchive> commArchives = commArchiveMapper.selectByObjId(mnsc.getId());
        if(commArchives!=null && commArchives.size()>0){
            mnsc.setCommArchives(commArchives);
        }
        // 处理日期
        Date createDate = mnsc.getCreateDate();
        String createdate = format.format(createDate);
        if (StringUtils.isNotEmpty(createdate))
            mnsc.setCreatedate(createdate);
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

    public List<Mnsc> getMnscList(Integer rownum) {
        List<Mnsc> mnscList = dao.getMnscList(rownum);
        if (mnscList != null && mnscList.size() > 0) {
            for (Mnsc mnsc : mnscList) {
                String objId = mnsc.getId();
                // 查看模拟实操访问人数
                List<LogVisit> logVisits = logVisitMapper.getDataByObjId(objId);
                if (logVisits != null && logVisits.size() > 0) {
                    mnsc.setLogVisitCount(logVisits.size());
                } else {
                    mnsc.setLogVisitCount(0);
                }
                // 查该模拟实操的缩略图
                List<CommArchive> commArchives = commArchiveMapper.selectByObjId(objId);
                if (commArchives != null && commArchives.size() > 0) {
                    for (CommArchive commArchive : commArchives) {
                        String archiveType = commArchive.getArchiveType();
                        if (StringUtils.isNotEmpty(archiveType)) {
                            if (archiveType.equals("img")) {
                                String fileUrl = commArchive.getFileUrl();
                                if (StringUtils.isNotEmpty(fileUrl))
                                    mnsc.setMainUrl(fileUrl);
                            }
                        }
                    }
                }
                // 日期格式
                Date createDate = mnsc.getCreateDate();
                SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日");
                String str = format.format(createDate);
                mnsc.setCreatedate(str);
            }
        }
        return mnscList;
    }

    public List<Map<String, Object>> getNewMnscList(Integer rownum, String userId) {
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("rownum", rownum);
//        map.put("userId", userId);
        return dao.getNewMnscList(map);
    }

    public List<Map<String, Object>> getMnscStateByCate() {
        return dao.getMnscStateByCate();
    }
}