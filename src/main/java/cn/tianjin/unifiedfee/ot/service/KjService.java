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
import cn.tianjin.unifiedfee.ot.entity.KjRefSource;
import cn.tianjin.unifiedfee.ot.entity.LogVisit;
import cn.tianjin.unifiedfee.ot.entity.Mnsc;
import cn.tianjin.unifiedfee.ot.mapper.CommArchiveMapper;
import cn.tianjin.unifiedfee.ot.mapper.KjMapper;
import cn.tianjin.unifiedfee.ot.mapper.KjRefSourceMapper;
import cn.tianjin.unifiedfee.ot.mapper.LogVisitMapper;

@Service
public class KjService {
    @Autowired
    private KjMapper dao;
    @Autowired
    private KjRefSourceMapper kjRefDao;
    @Autowired
    private LogVisitMapper logVisitMapper;
    @Autowired
    private CommArchiveMapper commArchiveMapper;

    // 获取分页数据
    public List<Kj> getPageData(Map<String, Object> param) {
        List<Kj> kjs = dao.getPageData(param);
        if (kjs != null && kjs.size() > 0) {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            for (Kj kj : kjs) {
                Date createDate = kj.getCreateDate();
                String createdate = format.format(createDate);
                if (StringUtils.isNotEmpty(createdate))
                    kj.setCreatedate(createdate);
            }
        }
        return kjs;
    }

    // 添加
    public boolean insert(Kj kj, UserInfo user) throws Exception {
        kj.setId(SequenceUUID.getPureUUID());
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
                kjRefSource.setId(SequenceUUID.getPureUUID());
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
                kjRefSource.setId(SequenceUUID.getPureUUID());
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
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
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
                    for (Kj oneKj : kjs) {
                        Date createDate = oneKj.getCreateDate();
                        String createdate = format.format(createDate);
                        if (StringUtils.isNotEmpty(createdate))
                            oneKj.setCreatedate(createdate);
                    }
                    kj.setKjs(kjs);
                }
            }
        }
        //处理相关附件
        List<CommArchive> commArchives = commArchiveMapper.selectByObjId(kj.getId());
        if(commArchives!=null && commArchives.size()>0){
            kj.setCommArchives(commArchives);
        }
        //处理日期
        Date createDate = kj.getCreateDate();
        String createdate = format.format(createDate);
        if (StringUtils.isNotEmpty(createdate))
            kj.setCreatedate(createdate);
        return kj;
    }

    // 获取全部信息
    public List<Kj> selectAllMsg() {
        return dao.selectAllMsg();
    }

    public List<Kj> getDataListByIds(Kj kj) {
        List<Kj> kjs = dao.getDataListByIds(kj);
        if (kjs != null && kjs.size() > 0) {
            for (Kj oneKj : kjs) {
                // 日期格式
                Date createDate = oneKj.getCreateDate();
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                String str = format.format(createDate);
                oneKj.setCreatedate(str);
            }
        }
        return kjs;
    }

    public List<Kj> find4Web(Map<String, Object> param) {
        return dao.find4Web(param);
    }

    public List<Kj> findRefKj4Web(Map<String, Object> param) {
        return dao.findRefKj4Web(param);
    }

    public List<Kj> getKjList(Integer rownum) {
        List<Kj> kjList = dao.getKjList(rownum);
        if (kjList != null && kjList.size() > 0) {
            for (Kj kj : kjList) {
                String objId = kj.getId();
                // 查该课件的访问人数
                List<LogVisit> logVisits = logVisitMapper.getDataByObjId(objId);
                if (logVisits != null && logVisits.size() > 0) {
                    kj.setLogVisitCount(logVisits.size());
                } else {
                    kj.setLogVisitCount(0);
                }
                // 查该课件的缩略图
                List<CommArchive> commArchives = commArchiveMapper.selectByObjId(objId);
                if (commArchives != null && commArchives.size() > 0) {
                    for (CommArchive commArchive : commArchives) {
                        String archiveType = commArchive.getArchiveType();
                        if (StringUtils.isNotEmpty(archiveType)) {
                            if (archiveType.equals("img")) {
                                String fileUrl = commArchive.getFileUrl();
                                if (StringUtils.isNotEmpty(fileUrl))
                                    kj.setMainUrl(fileUrl);
                            }
                        }
                    }
                }
                // 日期格式
                Date createDate = kj.getCreateDate();
                SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日");
                String str = format.format(createDate);
                kj.setCreatedate(str);
            }
        }
        return kjList;
    }

    public List<Kj> getMyKjList(Integer rownum, String userId) {
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("rownum", rownum);
        map.put("userId", userId);
        return dao.getMyKjList(map);
    }
}