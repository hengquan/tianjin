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
import cn.tianjin.unifiedfee.ot.entity.Kjitem;
import cn.tianjin.unifiedfee.ot.entity.LogVisit;
import cn.tianjin.unifiedfee.ot.entity.Mnsc;
import cn.tianjin.unifiedfee.ot.mapper.CommArchiveMapper;
import cn.tianjin.unifiedfee.ot.mapper.KjitemMapper;
import cn.tianjin.unifiedfee.ot.mapper.KjRefSourceMapper;
import cn.tianjin.unifiedfee.ot.mapper.LogVisitMapper;

@Service
public class KjitemService {
    @Autowired
    private KjitemMapper dao;
    @Autowired
    private KjRefSourceMapper kjRefDao;
    @Autowired
    private LogVisitMapper logVisitMapper;
    @Autowired
    private CommArchiveMapper commArchiveMapper;

    // 获取分页数据
    public List<Kjitem> getPageData(Map<String, Object> param) {
        List<Kjitem> kjs = dao.getPageData(param);
        if (kjs != null && kjs.size() > 0) {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            for (Kjitem kj : kjs) {
                Date createDate = kj.getCreateDate();
                String createdate = format.format(createDate);
                if (StringUtils.isNotEmpty(createdate))
                    kj.setCreatedate(createdate);
            }
        }
        return kjs;
    }

    // 添加
    public boolean insert(Kjitem kj, UserInfo user) throws Exception {
        kj.setId(SequenceUUID.getPureUUID());
        kj.setCreateBy(user.getUserId());
        kj.setState(2);
        kj.setKjHtml("null");
        kj.setCreateName(user.getUsername());
        Boolean isok = dao.insert(kj) > 0 ? true : false;        
        return isok;
    }

    // 更新
    public boolean update(Kjitem kj, UserInfo user) throws Exception {
        // 更新主表
        Boolean isok = dao.update(kj) > 0 ? true : false;          
        return isok;
    }

    // 删除
    public boolean delete(Kjitem entity) throws Exception {
        return dao.delete(entity) > 0 ? true : false;
    }

   

    // 获取单条信息
    public Kjitem get(Kjitem kj) throws Exception {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        // 获取主表信息
        kj = dao.get(kj.getId());
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
    public List<Kjitem> selectAllMsg() {
        return dao.selectAllMsg();
    }

    public List<Kjitem> getDataListByIds(Kjitem kj) {
        List<Kjitem> kjs = dao.getDataListByIds(kj);
        if (kjs != null && kjs.size() > 0) {
            for (Kjitem oneKj : kjs) {
                // 日期格式
                Date createDate = oneKj.getCreateDate();
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                String str = format.format(createDate);
                oneKj.setCreatedate(str);
            }
        }
        return kjs;
    }

    public List<Kjitem> find4Web(Map<String, Object> param) {
        return dao.find4Web(param);
    }

    public List<Kjitem> findRefKj4Web(Map<String, Object> param) {
        return dao.findRefKj4Web(param);
    }

    public List<Kjitem> getKjList(Integer rownum) {
        List<Kjitem> kjList = dao.getKjList(rownum);
        if (kjList != null && kjList.size() > 0) {
            for (Kjitem kj : kjList) {
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

    public List<Kjitem> getMyKjList(Integer rownum, String userId) {
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("rownum", rownum);
        map.put("userId", userId);
        return dao.getMyKjList(map);
    }
}