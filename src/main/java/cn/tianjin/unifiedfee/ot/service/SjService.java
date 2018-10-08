package cn.tianjin.unifiedfee.ot.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.spiritdata.framework.util.DateUtils;
import com.spiritdata.framework.util.SequenceUUID;

import cn.taiji.oauthbean.dto.UserInfo;
import cn.tianjin.unifiedfee.ot.entity.SJ;
import cn.tianjin.unifiedfee.ot.entity.SJTm;
import cn.tianjin.unifiedfee.ot.entity.Tm;
import cn.tianjin.unifiedfee.ot.entity.TmSelect;
import cn.tianjin.unifiedfee.ot.entity.TmUserAnswer;
import cn.tianjin.unifiedfee.ot.mapper.SJMapper;
import cn.tianjin.unifiedfee.ot.mapper.SJTmMapper;
import cn.tianjin.unifiedfee.ot.mapper.TmMapper;
import cn.tianjin.unifiedfee.ot.mapper.TmSelectMapper;
import cn.tianjin.unifiedfee.ot.mapper.TmUserAnswerMapper;

/**
 * 试卷服务
 */
@Service
public class SjService {
    @Autowired
    private TmMapper tmDao;
    @Autowired
    private SJMapper sjDao;
    @Autowired
    private SJTmMapper sjtmDao;
    @Autowired
    private TmSelectMapper selectDao;
    @Autowired
    private TmUserAnswerMapper tmAnswerDao;

    /**
     * 随机后的所给定的对象所对应题目的Id
     * @param objType
     * @param objId
     * @param tmCount
     * @return
     */
    public List<Map<String,Object>> getTempSj(String refType, String refId, int tmCount) {
        Random ran=new Random();

        Map<String, Object> param=new HashMap<String, Object>();
        if (!StringUtils.isBlank(refType)) param.put("refTabName", "ts_"+refType);
        param.put("refId", refId);
        List<Tm> tl=tmDao.getTmListByObjInfo(param);
        //随机排序，先不随机？
        List<Tm> retTl=new ArrayList<Tm>();
        while (tl.size()>0&&retTl.size()<tmCount) {
            int rIndex=ran.nextInt(tl.size());
            retTl.add(tl.get(rIndex));
            tl.remove(rIndex);
        }

        //转换为Map，并选择每一道题目的选项，这个会比较慢，先这样实现。
        List<Map<String, Object>> ml=new ArrayList<Map<String, Object>>();
        for (int i=0; i<retTl.size(); i++) {
            Map<String, Object> oneTm=getTmMap(retTl.get(i), i);
            List<TmSelect> selects=selectDao.getselectData(retTl.get(i).getId());
            if (selects!=null&&selects.size()>0) {
                List<Map<String, Object>> tmSelects=new ArrayList<Map<String, Object>>();
                for (int j=0; j<selects.size(); j++) {
                    tmSelects.add(getSelectMap(selects.get(j)));
                }
                oneTm.put("tmItems", tmSelects);
            }
            ml.add(oneTm);
        }
        return ml;
    }
    private Map<String, Object> getTmMap(Tm tm, int i) {
        Map<String, Object> m=new HashMap<String, Object>();
        m.put("tmId", tm.getId());
        m.put("tmIndex", i+1);
        m.put("tmDesc", tm.getTmHtml());
        m.put("tmType", tm.getTmType());
        m.put("tmScore", tm.getScore());
        return m;
    }
    private Map<String, Object> getSelectMap(TmSelect select) {
        Map<String, Object> m=new HashMap<String, Object>();
        m.put("itemSign", select.getTmSelectSign());
        m.put("itemDesc", select.getTmSelectDesc());
        return m;
    }

    /**
     * 按条件生成试卷
     * @param cateIds
     * @param refType
     * @param refId
     * @param tmCount
     * @return
     */
    @Transactional
    public Map<String, Object> getSj(String cateIds, String refType, String refId, int diff1, int diff2, int tmCount, UserInfo ui) {
        Random ran=new Random();

        Map<String, Object> param=new HashMap<String, Object>();
        //获得名称

        String cateQuery_kj="", cateQuery_mnsc="";
        if (!StringUtils.isBlank(cateIds)) {
            String[] sp=cateIds.split(",");
            for (int i=0; i<sp.length; i++) {
                cateQuery_mnsc+=" or mnsc_cat_id='"+sp[i].trim()+"'";
                cateQuery_kj+=" or kj_cat_id='"+sp[i].trim()+"'";
            }
        }
        if (cateQuery_mnsc.length()>0) {//处理分类
            cateQuery_mnsc=cateQuery_mnsc.substring(4);
            cateQuery_kj=cateQuery_kj.substring(4);
            String cateSql="";
            if (!StringUtils.isBlank(refType)) {
                if ("mnsc".equals(refType.trim())) {
                    cateSql="select distinct tm_id from q_TM_ref_source rf, ts_MNSC nmsc where upper(rf.ref_tabname)='TS_MNSC' and rf.ref_id=nmsc.id and ("+cateQuery_mnsc+")";
                } else
                if ("kj".equals(refType.trim())) {
                    cateSql="select distinct tm_id from q_TM_ref_source rf, ts_KJ kj where upper(rf.ref_tabname)='TS_KJ' and rf.ref_id=kj.id and ("+cateQuery_kj+")";
                }
            }
            if (StringUtils.isBlank(cateSql)) {
                cateSql="select distinct tm_id from q_TM_ref_source rf, ts_MNSC nmsc where upper(rf.ref_tabname)='TS_MNSC' and rf.ref_id=nmsc.id and ("+cateQuery_mnsc+")";
                cateSql+=" union ";
                cateSql+="select distinct tm_id from q_TM_ref_source rf, ts_KJ kj where upper(rf.ref_tabname)='TS_KJ' and rf.ref_id=kj.id and ("+cateQuery_kj+")";
            }
            cateSql="inner join ("+cateSql+") cat on cat.tm_id=a.id";
            param.put("cateSql", cateSql);
        }
        if (!StringUtils.isBlank(refType)) param.put("refTabName", "ts_"+refType);
        param.put("refId", refId);
        param.put("diff1", diff1);
        param.put("diff2", diff2);
        List<Tm> tl=tmDao.getTmList4sj(param);

        //随机排序
        List<Tm> retTl=new ArrayList<Tm>();
        Integer score=0;
        while (tl.size()>0&&retTl.size()<tmCount) {
            int rIndex=ran.nextInt(tl.size());
            Tm tm=tl.get(rIndex);
            retTl.add(tm);
            score+=tm.getScore();
            tl.remove(rIndex);
        }

        //转换为Map，并选择每一道题目的选项，这个会比较慢，先这样实现。
        List<Map<String, Object>> ml=new ArrayList<Map<String, Object>>();
        for (int i=0; i<retTl.size(); i++) {
            Map<String, Object> oneTm=getTmMap(retTl.get(i), i);
            List<TmSelect> selects=selectDao.getselectData(retTl.get(i).getId());
            if (selects!=null&&selects.size()>0) {
                List<Map<String, Object>> tmSelects=new ArrayList<Map<String, Object>>();
                for (int j=0; j<selects.size(); j++) {
                    tmSelects.add(getSelectMap(selects.get(j)));
                }
                oneTm.put("tmItems", tmSelects);
            }
            ml.add(oneTm);
        }

        Map<String, Object> dataM=null;
        if (ml.size()>0) {
            Date now=new Date();
            //录入试卷
            SJ sj=new SJ();
            sj.setId(SequenceUUID.getPureUUID());
            sj.setUserId(ui.getUserId());
            sj.setUserName(ui.getUsername());
            sj.setRefTabname(param.get("refTabName")==null?null:""+param.get("refTabName"));
            sj.setRefId(refId);
            sj.setSjName("练习题-"+ui.getUsername()+"-"+DateUtils.convert2TimeChineseStr(now));
            sj.setTimeUse(0);
            sj.setScore(score);
            sj.setTmCount(ml.size());
            sj.setDiffType(diff1+","+diff2);
            sj.setState(0);
            sj.setCreateBy(ui.getUserId());
            sj.setCreateName(ui.getUsername());
            sj.setCreateDate(new Date());
            //录入试卷的子表
            int j=retTl.size();
            for (int i=0; i<retTl.size(); i++) {
                Tm tm=retTl.get(i);
                SJTm sjtm=new SJTm();
                sjtm.setId(SequenceUUID.getPureUUID());
                sjtm.setSjId(sj.getId());
                sjtm.setTmId(tm.getId());
                sjtm.setTmScore(tm.getScore());
                sjtm.setSort(j--);
                sjtm.setInType(1);
                sjtm.setCreateBy(ui.getUserId());
                sjtm.setCreateName(ui.getUsername());
                sjtm.setCreateDate(now);
                sjtmDao.insertSelective(sjtm);
            }
            sjDao.insertSelective(sj);
            //组织返回值
            dataM=new HashMap<String, Object>();
            dataM.put("id", sj.getId());
            dataM.put("refType", refType);
            dataM.put("refId", refId);
            dataM.put("name", sj.getSjName());
            dataM.put("diffType", sj.getDiffType());
            dataM.put("score", score);
            dataM.put("tmList", ml);
        }
        return dataM;
    }

    public SJ getSjById(String id) {
        return sjDao.getById(id);
    }

    /**
     * 试卷提交
     * @param sj 试卷对昂
     * @param answers 答案字符串
     * @param resultType 返回类型，是否返回答案，1返回，其他不返回
     * @return
     */
    public Map<String, Object> commitSj(SJ sj, String answers, int resultType, String beginTime, String endTime) {
        //首先，得到试卷中的题目
        List<Tm> tmL=tmDao.getTmListTySjId(sj.getId());
        if (tmL==null||tmL.size()==0) return null;
        //处理用户答案
        String[] sp=answers.split(",");
        Map<String, String> answerMap=new HashMap<String, String>();
        int i=0;
        for (; i<sp.length; i++) {
            String[] sp2=sp[i].trim().split(":");
            if (sp2.length==2) {
                String tmId=sp2[0].trim();
                if (!StringUtils.isBlank(tmId)) {
                    String answer=sp2[1].trim();
                    if (!StringUtils.isBlank(answer)) {
                        answerMap.put(tmId, answer);
                    }
                }
            }
        }
        List<Map<String, Object>> ml=new ArrayList<Map<String, Object>>();
        int score=0;//总分
        for (i=0; i<tmL.size(); i++) {
            String okAnswer="";
            Map<String, String> okAnswerMap=new HashMap<String, String>();
            Map<String, Object> oneTm=getTmMap(tmL.get(i), i);
            List<TmSelect> selects=selectDao.getselectData(tmL.get(i).getId());
            if (selects!=null&&selects.size()>0) {
                List<Map<String, Object>> tmSelects=new ArrayList<Map<String, Object>>();
                for (int j=0; j<selects.size(); j++) {
                    tmSelects.add(getSelectMap(selects.get(j)));
                    if (selects.get(j).getIsAnswer()==1) {
                        okAnswer+=","+selects.get(j).getTmSelectSign();
                        okAnswerMap.put(selects.get(j).getTmSelectSign(), selects.get(j).getTmSelectDesc());
                    }
                }
                oneTm.put("tmItems", tmSelects);
            }
            ml.add(oneTm);
            if (resultType==1&&!StringUtils.isBlank(okAnswer)) {//需要返回答案
                oneTm.put("tmAnswer", okAnswer.substring(1));
            }
            //题目
            String _answer=answerMap.get(tmL.get(i).getId());
            if (_answer!=null) {
                String[] sp3=_answer.split("#");
                int okCount=0;
                boolean correct=false;
                for (int n=0; n<sp3.length; n++) {
                    if (okAnswerMap.get(sp3[n].trim())!=null) okCount++;
                }
                correct=(okAnswerMap.size()==okCount);
                if (correct) {
                    oneTm.put("score", tmL.get(i).getScore());
                    score+=tmL.get(i).getScore();
                } else {
                    oneTm.put("score", 0);
                }
                oneTm.put("answer", _answer.replaceAll("#", ","));
            } else {
                oneTm.put("score", 0);
            }
            //插入答题表
            TmUserAnswer tua= new TmUserAnswer();
            tua.setId(SequenceUUID.getPureUUID());
            tua.setSjId(sj.getId());
            tua.setTmId(tmL.get(i).getId());
            tua.setUserId(sj.getUserId());
            tua.setUserName(sj.getUserName());
            tua.setTmScore(tmL.get(i).getScore());
            tua.setScore(Integer.parseInt(""+oneTm.get("score")));
            tua.setCreateBy(sj.getCreateBy());
            tua.setCreateName(sj.getCreateName());
            tua.setCreateDate(new Date());
            tua.setAnswer(_answer==null?"":_answer.replaceAll("#", ","));
            tmAnswerDao.insertSelective(tua);
        }
        //更新试卷表
        sj.setScore(score);
        sj.setEndTime(new Date());
        sj.setBeginTime(new Date());
        sj.setState(2);//答题完成
        sjDao.update(sj);
        //组织返回值
        Map<String, Object> dataM=new HashMap<String, Object>();
        String refType=sj.getRefTabname();
        if (refType!=null&&refType.indexOf("_")>0) refType=refType.substring(refType.indexOf("_")+1);
        dataM.put("id", sj.getId());
        dataM.put("refType", refType);
        dataM.put("refId", sj.getRefId());
        dataM.put("name", sj.getSjName());
        dataM.put("diffType", sj.getDiffType());
        dataM.put("sjScore", sj.getScore());
        dataM.put("score", score);
        dataM.put("tmList", ml);
        return  dataM;
    }
}