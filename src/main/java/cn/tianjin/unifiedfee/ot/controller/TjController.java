package cn.tianjin.unifiedfee.ot.controller;

import java.io.IOException;
import java.io.OutputStream;
import java.math.RoundingMode;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFCellUtil;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.util.CellRangeAddress;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.spiritdata.framework.util.DateUtils;

import cn.taiji.format.result.ObjectResponseResult;
import cn.taiji.oauthbean.dto.UserInfo;
import cn.taiji.company.remote.SystemCompanyRemote;
import cn.taiji.company.system.CompanyInfo;
import cn.taiji.web.security.UserService;
import cn.tianjin.unifiedfee.ot.entity.LogVisit;
import cn.tianjin.unifiedfee.ot.logvisit.service.LogVisitService;
import cn.tianjin.unifiedfee.ot.util.HttpPush;
import cn.tianjin.unifiedfee.ot.util.POIExcelStyle;

@RequestMapping("/tj")
@Controller
public class TjController {
    @Autowired
    public UserService userService;
    @Autowired
    public SystemCompanyRemote companyRemote;
    @Autowired //从日志表汇出统计信息
    public LogVisitService logService;

    /**
     * 获得学员学习的统计列表，企业的学员学习
     * @param compIds 
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("getXyxxList")
    @ResponseBody
    public Map<String, Object> getXyxxList(HttpServletRequest request, HttpServletResponse response,
            @RequestParam(required=false) String userIds,
            @RequestParam(required=false) String date1,
            @RequestParam(required=false) String date2,
            @RequestParam(value="offset", defaultValue="1") int offset,
            @RequestParam(value="limit", defaultValue="10") int limit) {
        // 跨域
        HttpPush.responseInfo(response);

        Map<String, Object> retMap=new HashMap<String, Object>();
        try {
            UserInfo ui=userService.getUserInfo();
            if (ui == null) {
                retMap.put("returnCode", "02");
                retMap.put("messageInfo", "无用户登录");
                return retMap;
            }
            //获得用户所在企业
            ObjectResponseResult<CompanyInfo> companyInfo=companyRemote.findCompanyBankInfoList(ui.getUserId());
            if (companyInfo==null||companyInfo.getData()==null) {
                //return null;
            }

            //设置page
            PageHelper.offsetPage(offset, limit);
            Map<String, Object> param=new HashMap<String, Object>();
            param.put("compId", companyInfo.getData().getId());
//            param.put("compId", "98311111234523456B"); //先写死，准备删除
            if (!StringUtils.isBlank(userIds)) {
                userIds=userIds.replaceAll(",", "' or visitor_id='");
                userIds="(visitor_id='"+userIds+"')";
            }
            param.put("userIds", userIds);//所选择的人员的id列表
            param.put("date1", date1);//开始时间
            param.put("date2", date2);//结束时间
            List<Map<String, Object>> _xyxxl=logService.getXyxxList(param);
            if (_xyxxl==null||_xyxxl.size()==0) return null;
            PageInfo<Map<String, Object>> pageList=new PageInfo<Map<String, Object>>(_xyxxl);
            //对数据进行处理
            //获得企业的总数，总课件数，总模拟实操数，总考试次数
            List<Map<String, Object>> _suml=logService.getXyxxSumList(param);
            Map<String, Object> sumMap=new HashMap<String, Object>();
            sumMap.put("allKj", "0");
            sumMap.put("allMnsc", "0");
            sumMap.put("allSj", "0");
            if (_suml!=null&&_suml.size()>0) {
                for (Map<String, Object> _sum: _suml) {
                    if ("ts_mnsc".equals(_sum.get("OBJ_TYPE"))) {
                        sumMap.put("allMnsc", _sum.get("FX"));
                    } else
                    if ("ts_kj".equals(_sum.get("OBJ_TYPE"))) {
                        sumMap.put("allKj", _sum.get("FX"));
                    } else
                    if ("q_sj".equals(_sum.get("OBJ_TYPE"))) {
                        sumMap.put("allSj", _sum.get("FX"));
                    }
                }
            }
            //处理每一项
            List<Map<String, Object>> cl=new ArrayList<Map<String, Object>>();
            for (Map<String, Object> xyxx: _xyxxl) {
                Map<String, Object> newXyxx=new HashMap<String, Object>();
                newXyxx.put("userName", xyxx.get("USER_NAME")==null?"":xyxx.get("USER_NAME"));
                newXyxx.put("kjCount", xyxx.get("KJ_COUNT")==null?0:xyxx.get("KJ_COUNT"));
                newXyxx.put("mnscCount", xyxx.get("MNSC_COUNT")==null?0:xyxx.get("MNSC_COUNT"));
                newXyxx.put("sjCount", xyxx.get("SJ_COUNT")==null?0:xyxx.get("SJ_COUNT"));
                newXyxx.putAll(sumMap);
                //计算比例-模拟实操
                String tmpVal="-";
                try {
                    tmpVal="-";
                    if (!"0".equals(newXyxx.get("allMnsc")+"")) {
                        tmpVal=accuracy(Double.parseDouble(newXyxx.get("mnscCount")+""), Double.parseDouble(newXyxx.get("allMnsc")+""),2);
                    }
                } catch(Exception e) {
                }
                newXyxx.put("mnscRatio", tmpVal);
                //计算比例-课件
                tmpVal="-";
                try {
                    tmpVal="-";
                    if (!"0".equals(newXyxx.get("allKj")+"")) {
                        tmpVal=accuracy(Double.parseDouble(newXyxx.get("kjCount")+""), Double.parseDouble(newXyxx.get("allKj")+""),2);
                    }
                } catch(Exception e) {
                }
                newXyxx.put("kjRatio", tmpVal);
                //计算比例-试卷练习
                tmpVal="-";
                try {
                    tmpVal="-";
                    if (!"0".equals(newXyxx.get("allSj")+"")) {
                        tmpVal=accuracy(Double.parseDouble(newXyxx.get("sjCount")+""), Double.parseDouble(newXyxx.get("allSj")+""),2);
                    }
                } catch(Exception e) {
                }
                newXyxx.put("sjRatio", tmpVal);

                cl.add(newXyxx);
            }
            retMap.put("total", pageList.getTotal());
            retMap.put("rows", cl);
            return retMap;
        } catch (Exception e) {
            e.printStackTrace();
            retMap.put("returnCode", "01");
            retMap.put("messageInfo", e.toString());
        }
        return retMap;
    }

    private void setMergeCell(CellRangeAddress range, HSSFSheet sheet, HSSFCellStyle cellstyle) {
        for (int i=range.getFirstRow(); i<=range.getLastRow(); i++) {
            HSSFRow row=HSSFCellUtil.getRow(i, sheet);
            for (int j=range.getFirstColumn();j<=range.getLastColumn(); j++) {
                HSSFCell cell=HSSFCellUtil.getCell(row, (short)j);
                cell.setCellStyle(cellstyle);
            }
        }
    }

    @RequestMapping("expQytjList")
    @ResponseBody
    public void expQytjList(HttpServletRequest request, HttpServletResponse response,
            @RequestParam(required=false) String compIds,
            @RequestParam(required=false) String date1,
            @RequestParam(required=false) String date2,
            @RequestParam(required=false) String kjcsType,
            @RequestParam(required=false) String kjcs,
            @RequestParam(required=false) String kjzb1,
            @RequestParam(required=false) String kjzb2,
            @RequestParam(required=false) String sccsType,
            @RequestParam(required=false) String sccs,
            @RequestParam(required=false) String sczb1,
            @RequestParam(required=false) String sczb2,
            @RequestParam(required=false) String lxcsType,
            @RequestParam(required=false) String lxcs,
            @RequestParam(required=false) String lxzb1,
            @RequestParam(required=false) String lxzb2,
            @RequestParam(value="offset", defaultValue="1") int offset,
            @RequestParam(value="limit", defaultValue="10") int limit)  throws IOException {
        // 跨域
        HttpPush.responseInfo(response);

        //〇、获取用户信息，导出数据的用户
        UserInfo ui=userService.getUserInfo();

        //一、处理条件， 并拼接条件串
        String conditionStr="";//查询条件描述
        int conditionHeight=1;
        String oneCondition="";
        Map<String, Object> param=new HashMap<String, Object>();
        if (!StringUtils.isBlank(compIds)) {
            compIds=compIds.replaceAll(",", "' or group_id='");
            compIds="group_id='"+compIds+"'";
        }
        List<Map<String, Object>> compl=logService.getDistinctCamp();
        String[] sp=compIds.split(",");
        for (int i=0; i<sp.length; i++) {
            for (Map<String, Object> comp: compl) {
                if (sp[i].trim().equals(comp.get("GROUP_ID"))) {
                    oneCondition+=","+comp.get("GROUP_NAME");
                }
            }
        }
        if (!StringUtils.isBlank(oneCondition)) {
            conditionHeight++;
            conditionStr+="\n关注企业——"+oneCondition;
        }
        oneCondition="";
        param.put("compIds", compIds);//所选择的人员的id列表

        param.put("date1", date1);//开始时间
        if (!StringUtils.isBlank(date1)) {
            conditionStr+="\n开始时间——"+date1;
            conditionHeight++;
        }
        param.put("date2", date2);//结束时间
        if (!StringUtils.isBlank(date2)) {
            conditionStr+="\n开始时间——"+date2;
            conditionHeight++;
        }
        //获得企业的总数，总课件数，总模拟实操数，总考试次数
        List<Map<String, Object>> _suml=logService.getQytjSumList(param);
        Map<String, Object> sumMap=new HashMap<String, Object>();
        sumMap.put("allKj", "0");
        sumMap.put("allMnsc", "0");
        sumMap.put("allSj", "0");
        if (_suml!=null&&_suml.size()>0) {
            for (Map<String, Object> _sum: _suml) {
                if ("ts_mnsc".equals(_sum.get("OBJ_TYPE"))) {
                    sumMap.put("allMnsc", _sum.get("FX"));
                } else
                if ("ts_kj".equals(_sum.get("OBJ_TYPE"))) {
                    sumMap.put("allKj", _sum.get("FX"));
                } else
                if ("q_sj".equals(_sum.get("OBJ_TYPE"))) {
                    sumMap.put("allSj", _sum.get("FX"));
                }
            }
        }
        //组织其他的参数
        String filterSql="";
        float ftemp=0f;
        //课件
        if (!StringUtils.isBlank(kjcsType)&&!StringUtils.isBlank(kjcs)) {
            try {
                Long.parseLong(kjcs);//判断是否是数值
                if ("0".equals(kjcsType)) {
                    filterSql+=" and kj_count>"+kjcs;
                    oneCondition="课件学习次数——大于:"+kjcs;
                }
                if ("1".equals(kjcsType)) {
                    filterSql+=" and kj_count="+kjcs;
                    oneCondition="课件学习次数——等于:"+kjcs;
                }
                if ("2".equals(kjcsType)) {
                    filterSql+=" and kj_count<"+kjcs;
                    oneCondition="课件学习次数——小于:"+kjcs;
                }
            } catch(Exception e) {
            }
        }
        if (!StringUtils.isBlank(oneCondition)) {
            conditionStr+="\n"+oneCondition;
            conditionHeight++;
        }
        oneCondition="";
        ftemp=Float.parseFloat(""+sumMap.get("allKj"));
        if (!StringUtils.isBlank(kjzb1)) {
            try {
                Float.parseFloat(kjzb1);
                filterSql+=" and kj_count>="+ftemp*Float.parseFloat(kjzb1)*0.01f;
                conditionStr+="\n课件学习占比——大于等于:"+kjzb1+"%";
                conditionHeight++;
            } catch(Exception e) {
            }
        }
        if (!StringUtils.isBlank(kjzb2)) {
            try {
                Float.parseFloat(kjzb2);
                filterSql+=" and kj_count<="+ftemp*Float.parseFloat(kjzb2)*0.01f;
                conditionStr+="\n课件学习占比——小于等于:"+kjzb2+"%";
                conditionHeight++;
            } catch(Exception e) {
            }
        }
        //模拟实操
        if (!StringUtils.isBlank(sccsType)&&!StringUtils.isBlank(sccs)) {
            try {
                Long.parseLong(sccs);//判断是否是数值
                if ("0".equals(sccsType)) {
                    filterSql+=" and mnsc_count>"+sccs;
                    oneCondition="模拟实操次数——大于:"+sccs;
                }
                if ("1".equals(sccsType)) {
                    filterSql+=" and mnsc_count="+sccs;
                    oneCondition="模拟实操次数——等于:"+sccs;
                }
                if ("2".equals(sccsType)) {
                    filterSql+=" and mnsc_count<"+sccs;
                    oneCondition="模拟实操次数——小于:"+sccs;
                }
            } catch(Exception e) {
            }
        }
        if (!StringUtils.isBlank(oneCondition)) {
            conditionStr+="\n"+oneCondition;
            conditionHeight++;
        }
        oneCondition="";
        ftemp=Float.parseFloat(""+sumMap.get("allMnsc"));
        if (!StringUtils.isBlank(sczb1)) {
            try {
                Float.parseFloat(sczb1);
                filterSql+=" and mnsc_count>="+ftemp*Float.parseFloat(sczb1)*0.01f;
                conditionStr+="\n模拟实操占比——大于等于:"+sczb1+"%";
                conditionHeight++;
            } catch(Exception e) {
            }
        }
        if (!StringUtils.isBlank(sczb2)) {
            try {
                Float.parseFloat(sczb2);
                filterSql+=" and mnsc_count<="+ftemp*Float.parseFloat(sczb2)*0.01f;
                conditionStr+="\n模拟实操占比——小于等于:"+sczb2+"%";
                conditionHeight++;
            } catch(Exception e) {
            }
        }
        //在线练习
        if (!StringUtils.isBlank(lxcsType)&&!StringUtils.isBlank(lxcs)) {
            try {
                Long.parseLong(lxcs);//判断是否是数值
                if ("0".equals(lxcsType)) {
                    filterSql+=" and sj_count>"+lxcs;
                    oneCondition="在线练习次数——大于:"+lxcs;
                }
                if ("1".equals(lxcsType)) {
                    filterSql+=" and sj_count="+lxcs;
                    oneCondition="在线练习次数——等于:"+lxcs;
                }
                if ("2".equals(lxcsType)) {
                    filterSql+=" and sj_count<"+lxcs;
                    oneCondition="在线练习次数——小于:"+lxcs;
                }
            } catch(Exception e) {
            }
        }
        if (!StringUtils.isBlank(oneCondition)) {
            conditionStr+="\n"+oneCondition;
            conditionHeight++;
        }
        ftemp=Float.parseFloat(""+sumMap.get("allSj"));
        if (!StringUtils.isBlank(lxzb1)) {
            try {
                Float.parseFloat(lxzb1);
                filterSql+=" and sj_count>="+ftemp*Float.parseFloat(lxzb1)*0.01f;
                conditionStr+="\n在线练习占比——大于等于:"+lxzb1+"%";
                conditionHeight++;
            } catch(Exception e) {
            }
        }
        if (!StringUtils.isBlank(lxzb2)) {
            try {
                Float.parseFloat(lxzb2);
                filterSql+=" and sj_count<="+ftemp*Float.parseFloat(lxzb2)*0.01f;
                conditionStr+="\n在线练习占比——小于等于:"+lxzb2+"%";
                conditionHeight++;
            } catch(Exception e) {
            }
        }
        if (!StringUtils.isBlank(filterSql)) {
            param.put("filterSql", filterSql.substring(5));
        }

        //二、获得数据
        List<Map<String, Object>> _qytjl=logService.getQytjList(param);
        //对数据进行处理
        //处理每一项
        List<Map<String, Object>> cl=new ArrayList<Map<String, Object>>();
        for (Map<String, Object> qytj: _qytjl) {
            Map<String, Object> newQytj=new HashMap<String, Object>();
            newQytj.put("groupName", qytj.get("GROUP_NAME")==null?"":qytj.get("GROUP_NAME"));
            newQytj.put("kjCount", qytj.get("KJ_COUNT")==null?0:qytj.get("KJ_COUNT"));
            newQytj.put("mnscCount", qytj.get("MNSC_COUNT")==null?0:qytj.get("MNSC_COUNT"));
            newQytj.put("sjCount", qytj.get("SJ_COUNT")==null?0:qytj.get("SJ_COUNT"));
            newQytj.putAll(sumMap);
            //计算比例-模拟实操
            String tmpVal="-";
            try {
                tmpVal="-";
                if (!"0".equals(newQytj.get("allMnsc")+"")) {
                    tmpVal=accuracy(Double.parseDouble(newQytj.get("mnscCount")+""), Double.parseDouble(newQytj.get("allMnsc")+""),2);
                }
            } catch(Exception e) {
            }
            newQytj.put("mnscRatio", tmpVal);
            //计算比例-课件
            tmpVal="-";
            try {
                tmpVal="-";
                if (!"0".equals(newQytj.get("allKj")+"")) {
                    tmpVal=accuracy(Double.parseDouble(newQytj.get("kjCount")+""), Double.parseDouble(newQytj.get("allKj")+""),2);
                }
            } catch(Exception e) {
            }
            newQytj.put("kjRatio", tmpVal);
            //计算比例-试卷练习
            tmpVal="-";
            try {
                tmpVal="-";
                if (!"0".equals(newQytj.get("allSj")+"")) {
                    tmpVal=accuracy(Double.parseDouble(newQytj.get("sjCount")+""), Double.parseDouble(newQytj.get("allSj")+""),2);
                }
            } catch(Exception e) {
            }
            newQytj.put("sjRatio", tmpVal);
            cl.add(newQytj);
        }

        //三、处理表格
        String sheetName="企业培训情况统计("+DateUtils.convert2LocalStr("yyyy年MM月dd日", new Date())+")";//页签名称
        String fileName=sheetName+".xls";//文件名称
        int maxColumnIndex=11;
        HSSFWorkbook workbook=new HSSFWorkbook();
        POIExcelStyle eStyle=new POIExcelStyle(workbook);
        HSSFSheet sheet1=workbook.createSheet(sheetName);
        //第一行:标题
        HSSFRow r0=sheet1.createRow(0);
        r0.setHeight((short)900);
        HSSFCell r0c0=r0.createCell(1);
        r0c0.setCellValue(new HSSFRichTextString(sheetName+"导出数据"));
        r0c0.setCellStyle(eStyle.getTitleStyle());
        sheet1.addMergedRegion(new CellRangeAddress(0,0,1,maxColumnIndex));
        //第二行:打印人及打印时间
        HSSFRow r1=sheet1.createRow(1);
        HSSFCell r1c1=r1.createCell(1);//打印人
        String tmpStr="";
        tmpStr="导出人";
        r1c1.setCellValue(new HSSFRichTextString(tmpStr));
        r1c1.setCellStyle(eStyle.getLabel1Style());
        HSSFCell r1c2=r1.createCell(2);
        if (ui!=null) {
            r1c2.setCellValue(new HSSFRichTextString(ui.getUsername()));
        } else {
            r1c2.setCellValue("未知");
        }
        r1c2.setCellStyle(eStyle.getValue1Style());
        HSSFCell r1cD1=r1.createCell(maxColumnIndex-1);//打印时间
        r1cD1.setCellValue(new HSSFRichTextString("导出时间"));
        r1cD1.setCellStyle(eStyle.getLabel1Style());
        HSSFCell r1cD2=r1.createCell(maxColumnIndex);//打印时间
        r1cD2.setCellValue(new HSSFRichTextString(DateUtils.convert2LocalStr("yyyy年MM月dd日 HH:mm:ss", new Date())));
        r1cD2.setCellStyle(eStyle.getValue1Style());
        //第三、四行:表头
        //处理表头
        HSSFCellStyle headStyle=eStyle.getHeadStyle();
        HSSFRow rh=sheet1.createRow(2);
        HSSFCell rhcell=rh.createCell(1);
        rhcell.setCellStyle(headStyle);
        rhcell.setCellValue(new HSSFRichTextString("编号"));
        rhcell=rh.createCell(2);
        rhcell.setCellStyle(headStyle);
        rhcell.setCellValue(new HSSFRichTextString("企业名称"));
        rhcell=rh.createCell(3);
        rhcell.setCellStyle(headStyle);
        rhcell.setCellValue(new HSSFRichTextString("课件学习"));
        rhcell=rh.createCell(6);
        rhcell.setCellStyle(headStyle);
        rhcell.setCellValue(new HSSFRichTextString("模拟实操"));
        rhcell=rh.createCell(9);
        rhcell.setCellStyle(headStyle);
        rhcell.setCellValue(new HSSFRichTextString("学员练习"));
        rh=sheet1.createRow(3);
        rhcell=rh.createCell(3);
        rhcell.setCellStyle(headStyle);
        rhcell.setCellValue(new HSSFRichTextString("次数"));
        rhcell=rh.createCell(4);
        rhcell.setCellStyle(headStyle);
        rhcell.setCellValue(new HSSFRichTextString("总数"));
        rhcell=rh.createCell(5);
        rhcell.setCellStyle(headStyle);
        rhcell.setCellValue(new HSSFRichTextString("占比"));
        rhcell=rh.createCell(6);
        rhcell.setCellStyle(headStyle);
        rhcell.setCellValue(new HSSFRichTextString("次数"));
        rhcell=rh.createCell(7);
        rhcell.setCellStyle(headStyle);
        rhcell.setCellValue(new HSSFRichTextString("总数"));
        rhcell=rh.createCell(8);
        rhcell.setCellStyle(headStyle);
        rhcell.setCellValue(new HSSFRichTextString("占比"));
        rhcell=rh.createCell(9);
        rhcell.setCellStyle(headStyle);
        rhcell.setCellValue(new HSSFRichTextString("次数"));
        rhcell=rh.createCell(10);
        rhcell.setCellStyle(headStyle);
        rhcell.setCellValue(new HSSFRichTextString("总数"));
        rhcell=rh.createCell(11);
        rhcell.setCellStyle(headStyle);
        rhcell.setCellValue(new HSSFRichTextString("占比"));
        //合并
        CellRangeAddress range=new CellRangeAddress(2,3,1,1);
        sheet1.addMergedRegion(range);
        setMergeCell(range, sheet1, eStyle.getHeadMergeStyle());
        range=new CellRangeAddress(2,3,2,2);
        sheet1.addMergedRegion(range);
        setMergeCell(range, sheet1, eStyle.getHeadMergeStyle());
        range=new CellRangeAddress(2,2,3,5);
        sheet1.addMergedRegion(range);
        setMergeCell(range, sheet1, eStyle.getHeadMergeStyle());
        range=new CellRangeAddress(2,2,6,8);
        sheet1.addMergedRegion(range);
        setMergeCell(range, sheet1, eStyle.getHeadMergeStyle());
        range=new CellRangeAddress(2,2,9,11);
        sheet1.addMergedRegion(range);
        setMergeCell(range, sheet1, eStyle.getHeadMergeStyle());

        //第n行，数据循环
        int bh=1;
        if (cl!=null&&cl.size()>0) {
            for (Map<String, Object> oneData: cl) {
                HSSFCell rdcell=null;
                HSSFRow rd=sheet1.createRow(bh+3);
                //1-编号
                rdcell=rd.createCell(1, Cell.CELL_TYPE_NUMERIC);
                rdcell.setCellValue(new HSSFRichTextString(bh+""));
                rdcell.setCellStyle(eStyle.getCommonCellStyle("center"));
                //2-企业名称
                rdcell=rd.createCell(2, Cell.CELL_TYPE_STRING);
                rdcell.setCellValue(new HSSFRichTextString(""+oneData.get("groupName")));
                rdcell.setCellStyle(eStyle.getCommonCellStyle("center"));
                //3-课件学习-次数
                rdcell=rd.createCell(3, Cell.CELL_TYPE_NUMERIC);
                rdcell.setCellValue(new HSSFRichTextString(""+oneData.get("kjCount")));
                rdcell.setCellStyle(eStyle.getCommonCellStyle("right"));
                //4-课件学习-总数
                rdcell=rd.createCell(4, Cell.CELL_TYPE_NUMERIC);
                rdcell.setCellValue(new HSSFRichTextString(""+oneData.get("allKj")));
                rdcell.setCellStyle(eStyle.getCommonCellStyle("right"));
                //5-课件学习-占比
                rdcell=rd.createCell(5, Cell.CELL_TYPE_STRING);
                rdcell.setCellValue(new HSSFRichTextString(""+oneData.get("kjRatio")));
                rdcell.setCellStyle(eStyle.getCommonCellStyle("right"));
                //6-模拟实操-次数
                rdcell=rd.createCell(6, Cell.CELL_TYPE_NUMERIC);
                rdcell.setCellValue(new HSSFRichTextString(""+oneData.get("mnscCount")));
                rdcell.setCellStyle(eStyle.getCommonCellStyle("right"));
                //7-模拟实操-总数
                rdcell=rd.createCell(7, Cell.CELL_TYPE_NUMERIC);
                rdcell.setCellValue(new HSSFRichTextString(""+oneData.get("allMnsc")));
                rdcell.setCellStyle(eStyle.getCommonCellStyle("right"));
                //8-模拟实操-占比
                rdcell=rd.createCell(8, Cell.CELL_TYPE_STRING);
                rdcell.setCellValue(new HSSFRichTextString(""+oneData.get("mnscRatio")));
                rdcell.setCellStyle(eStyle.getCommonCellStyle("right"));
                //9-模拟实操-次数
                rdcell=rd.createCell(9, Cell.CELL_TYPE_NUMERIC);
                rdcell.setCellValue(new HSSFRichTextString(""+oneData.get("sjCount")));
                rdcell.setCellStyle(eStyle.getCommonCellStyle("right"));
                //10-模拟实操-总数
                rdcell=rd.createCell(10, Cell.CELL_TYPE_NUMERIC);
                rdcell.setCellValue(new HSSFRichTextString(""+oneData.get("allSj")));
                rdcell.setCellStyle(eStyle.getCommonCellStyle("right"));
                //11-模拟实操-占比
                rdcell=rd.createCell(11, Cell.CELL_TYPE_STRING);
                rdcell.setCellValue(new HSSFRichTextString(""+oneData.get("sjRatio")));
                rdcell.setCellStyle(eStyle.getCommonCellStyle("right"));
                bh++;
            }
        }
        //表尾:页尾——查询条件
        HSSFRow rCondition=sheet1.createRow(bh+3);
        rCondition.setHeight((short)((conditionHeight*310)));
        HSSFCell rConditonCell=rCondition.createCell(1, Cell.CELL_TYPE_STRING);//打印条件
        rConditonCell.setCellStyle(eStyle.getMemoCellStyle());
        if (!StringUtils.isBlank(conditionStr)) {
            rConditonCell.setCellValue(new HSSFRichTextString("查询条件："+conditionStr));
        } else {
            rConditonCell.setCellValue(new HSSFRichTextString("查询条件：无"));
        }
        range=new CellRangeAddress(bh+3,bh+3,1,maxColumnIndex);
        sheet1.addMergedRegion(range);
        setMergeCell(range, sheet1, eStyle.getMemoCellStyle());

        //自动列宽
        for (int i=1; i<=maxColumnIndex; i++) sheet1.autoSizeColumn((short)i);
        //锁定表头
        sheet1.createFreezePane(0, 4, 0, 4);

        response.reset();
        response.setContentType("application/x-msdownload;charset=utf-8");
        response.setHeader("Content-Disposition", "attachment;filename="+new String(fileName.getBytes("GBK"),"ISO8859_1"));
        OutputStream os=null;
        try {
            os=response.getOutputStream();
            workbook.write(os);
        } catch(Exception e) {
            e.printStackTrace();
        } finally {
            if (os != null) {
                os.flush();
                os.close();
            }
        }
    }
    /**
     * 获得企业统计列表，为系统管理员
     * @param compIds 
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("getQytjList")
    @ResponseBody
    public Map<String, Object> getQytjList(HttpServletRequest request, HttpServletResponse response,
            @RequestParam(required=false) String compIds,
            @RequestParam(required=false) String date1,
            @RequestParam(required=false) String date2,
            @RequestParam(required=false) String kjcsType,
            @RequestParam(required=false) String kjcs,
            @RequestParam(required=false) String kjzb1,
            @RequestParam(required=false) String kjzb2,
            @RequestParam(required=false) String sccsType,
            @RequestParam(required=false) String sccs,
            @RequestParam(required=false) String sczb1,
            @RequestParam(required=false) String sczb2,
            @RequestParam(required=false) String lxcsType,
            @RequestParam(required=false) String lxcs,
            @RequestParam(required=false) String lxzb1,
            @RequestParam(required=false) String lxzb2,
            @RequestParam(value="offset", defaultValue="1") int offset,
            @RequestParam(value="limit", defaultValue="10") int limit) {
        // 跨域
        HttpPush.responseInfo(response);

        Map<String, Object> retMap=new HashMap<String, Object>();
        try {
            UserInfo ui=userService.getUserInfo();
            if (ui == null) {
                retMap.put("returnCode", "02");
                retMap.put("messageInfo", "无用户登录");
                return retMap;
            }

            //设置page
            PageHelper.offsetPage(offset, limit);
            Map<String, Object> param=new HashMap<String, Object>();
            if (!StringUtils.isBlank(compIds)) {
                compIds=compIds.replaceAll(",", "' or group_id='");
                compIds="group_id='"+compIds+"'";
            }
            param.put("compIds", compIds);//所选择的人员的id列表
            param.put("date1", date1);//开始时间
            param.put("date2", date2);//结束时间
            //获得企业的总数，总课件数，总模拟实操数，总考试次数
            List<Map<String, Object>> _suml=logService.getQytjSumList(param);
            Map<String, Object> sumMap=new HashMap<String, Object>();
            sumMap.put("allKj", "0");
            sumMap.put("allMnsc", "0");
            sumMap.put("allSj", "0");
            if (_suml!=null&&_suml.size()>0) {
                for (Map<String, Object> _sum: _suml) {
                    if ("ts_mnsc".equals(_sum.get("OBJ_TYPE"))) {
                        sumMap.put("allMnsc", _sum.get("FX"));
                    } else
                    if ("ts_kj".equals(_sum.get("OBJ_TYPE"))) {
                        sumMap.put("allKj", _sum.get("FX"));
                    } else
                    if ("q_sj".equals(_sum.get("OBJ_TYPE"))) {
                        sumMap.put("allSj", _sum.get("FX"));
                    }
                }
            }

            //组织其他的参数
            String filterSql="";
            float ftemp=0f;
            //课件
            if (!StringUtils.isBlank(kjcsType)&&!StringUtils.isBlank(kjcs)) {
                try {
                    Long.parseLong(kjcs);//判断是否是数值
                    if ("0".equals(kjcsType)) filterSql+=" and kj_count>"+kjcs;
                    if ("1".equals(kjcsType)) filterSql+=" and kj_count="+kjcs;
                    if ("2".equals(kjcsType)) filterSql+=" and kj_count<"+kjcs;
                } catch(Exception e) {
                }
            }
            ftemp=Float.parseFloat(""+sumMap.get("allKj"));
            if (!StringUtils.isBlank(kjzb1)) {
                try {
                    Float.parseFloat(kjzb1);
                    filterSql+=" and kj_count>="+ftemp*Float.parseFloat(kjzb1)*0.01f;
                } catch(Exception e) {
                }
            }
            if (!StringUtils.isBlank(kjzb2)) {
                try {
                    Float.parseFloat(kjzb2);
                    filterSql+=" and kj_count<="+ftemp*Float.parseFloat(kjzb2)*0.01f;
                } catch(Exception e) {
                }
            }
            //模拟实操
            if (!StringUtils.isBlank(sccsType)&&!StringUtils.isBlank(sccs)) {
                try {
                    Long.parseLong(sccs);//判断是否是数值
                    if ("0".equals(sccsType)) filterSql+=" and mnsc_count>"+sccs;
                    if ("1".equals(sccsType)) filterSql+=" and mnsc_count="+sccs;
                    if ("2".equals(sccsType)) filterSql+=" and mnsc_count<"+sccs;
                } catch(Exception e) {
                }
            }
            ftemp=Float.parseFloat(""+sumMap.get("allMnsc"));
            if (!StringUtils.isBlank(sczb1)) {
                try {
                    Float.parseFloat(sczb1);
                    filterSql+=" and mnsc_count>="+ftemp*Float.parseFloat(sczb1)*0.01f;
                } catch(Exception e) {
                }
            }
            if (!StringUtils.isBlank(sczb2)) {
                try {
                    Float.parseFloat(sczb2);
                    filterSql+=" and mnsc_count<="+ftemp*Float.parseFloat(sczb2)*0.01f;
                } catch(Exception e) {
                }
            }
            //在线练习
            if (!StringUtils.isBlank(lxcsType)&&!StringUtils.isBlank(lxcs)) {
                try {
                    Long.parseLong(lxcs);//判断是否是数值
                    if ("0".equals(lxcsType)) filterSql+=" and sj_count>"+lxcs;
                    if ("1".equals(lxcsType)) filterSql+=" and sj_count="+lxcs;
                    if ("2".equals(lxcsType)) filterSql+=" and sj_count<"+lxcs;
                } catch(Exception e) {
                }
            }
            ftemp=Float.parseFloat(""+sumMap.get("allSj"));
            if (!StringUtils.isBlank(lxzb1)) {
                try {
                    Float.parseFloat(lxzb1);
                    filterSql+=" and sj_count>="+ftemp*Float.parseFloat(lxzb1)*0.01f;
                } catch(Exception e) {
                }
            }
            if (!StringUtils.isBlank(lxzb2)) {
                try {
                    Float.parseFloat(lxzb2);
                    filterSql+=" and sj_count<="+ftemp*Float.parseFloat(lxzb2)*0.01f;
                } catch(Exception e) {
                }
            }
            if (!StringUtils.isBlank(filterSql)) {
                param.put("filterSql", filterSql.substring(5));
            }

            List<Map<String, Object>> _qytjl=logService.getQytjList(param);
            if (_qytjl==null||_qytjl.size()==0) return null;
            PageInfo<Map<String, Object>> pageList=new PageInfo<Map<String, Object>>(_qytjl);
            //对数据进行处理
            //处理每一项
            List<Map<String, Object>> cl=new ArrayList<Map<String, Object>>();
            for (Map<String, Object> qytj: _qytjl) {
                Map<String, Object> newQytj=new HashMap<String, Object>();
                newQytj.put("groupName", qytj.get("GROUP_NAME")==null?"":qytj.get("GROUP_NAME"));
                newQytj.put("kjCount", qytj.get("KJ_COUNT")==null?0:qytj.get("KJ_COUNT"));
                newQytj.put("mnscCount", qytj.get("MNSC_COUNT")==null?0:qytj.get("MNSC_COUNT"));
                newQytj.put("sjCount", qytj.get("SJ_COUNT")==null?0:qytj.get("SJ_COUNT"));
                newQytj.putAll(sumMap);
                //计算比例-模拟实操
                String tmpVal="-";
                try {
                    tmpVal="-";
                    if (!"0".equals(newQytj.get("allMnsc")+"")) {
                        tmpVal=accuracy(Double.parseDouble(newQytj.get("mnscCount")+""), Double.parseDouble(newQytj.get("allMnsc")+""),2);
                    }
                } catch(Exception e) {
                }
                newQytj.put("mnscRatio", tmpVal);
                //计算比例-课件
                tmpVal="-";
                try {
                    tmpVal="-";
                    if (!"0".equals(newQytj.get("allKj")+"")) {
                        tmpVal=accuracy(Double.parseDouble(newQytj.get("kjCount")+""), Double.parseDouble(newQytj.get("allKj")+""),2);
                    }
                } catch(Exception e) {
                }
                newQytj.put("kjRatio", tmpVal);
                //计算比例-试卷练习
                tmpVal="-";
                try {
                    tmpVal="-";
                    if (!"0".equals(newQytj.get("allSj")+"")) {
                        tmpVal=accuracy(Double.parseDouble(newQytj.get("sjCount")+""), Double.parseDouble(newQytj.get("allSj")+""),2);
                    }
                } catch(Exception e) {
                }
                newQytj.put("sjRatio", tmpVal);

                cl.add(newQytj);
            }
            retMap.put("total", pageList.getTotal());
            retMap.put("rows", cl);
            return retMap;
        } catch (Exception e) {
            e.printStackTrace();
            retMap.put("returnCode", "01");
            retMap.put("messageInfo", e.toString());
        }
        return retMap;
    }

    public static String accuracy(double num, double total, int scale) {
         DecimalFormat df = (DecimalFormat)NumberFormat.getInstance();
         df.setMaximumFractionDigits(scale);     //模式 例如四舍五入     
         df.setRoundingMode(RoundingMode.HALF_UP);       
         double accuracy_num = num / total * 100;       
         return df.format(accuracy_num)+"%";
    }

    /**
     * 获得学员学习的统计列表，企业的学员学习
     * @param forType=1是企业端,=2企业管理员,3=企业一般人员
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("getXxrzList")
    @ResponseBody
    public Map<String, Object> getXxrzList(HttpServletRequest request, HttpServletResponse response,
            @RequestParam(required=false) String forType,
            @RequestParam(required=false) String userIds,
            @RequestParam(required=false) String objTypes,
            @RequestParam(required=false) String moduleNames,
            @RequestParam(required=false) String catNames,
            @RequestParam(required=false) String compName,
            @RequestParam(required=false) String userName,//多个用户名称
            @RequestParam(required=false) String date1,
            @RequestParam(required=false) String date2,
            @RequestParam(value="offset", defaultValue="1") int offset,
            @RequestParam(value="limit", defaultValue="10") int limit) {
        // 跨域
        HttpPush.responseInfo(response);

        Map<String, Object> retMap=new HashMap<String, Object>();
        try {
            UserInfo ui=userService.getUserInfo();
            if (ui==null) {
                retMap.put("returnCode", "02");
                retMap.put("messageInfo", "无用户登录");
                return retMap;
            }
            if (StringUtils.isBlank(forType)) forType="1";//为管理端获得内容

            String compId="";
            String userId="";
            if (!"1".equals(forType)) {//若不是为管理端，要获得当前的用户和企业信息
                if ("2".equals(forType)) {//企业管理员
                    //获得用户所在企业
                    ObjectResponseResult<CompanyInfo> companyInfo=companyRemote.findCompanyBankInfoList(ui.getUserId());
                    if (companyInfo!=null&&companyInfo.getData()!=null) {
                        compId=companyInfo.getData().getUniscid();
//                    } else {//这是测试代码，用guanliyuan进行测试
//                        compId="98311111234523456B";
                    }
                } else
                if ("3".equals(forType)) {//企业人员
                    userId=ui.getUserId();
                    //userId="ff808081670cad5f016710d5a3110009";
                }
            }

            //设置page
            PageHelper.offsetPage(offset, limit);
            Map<String, Object> param=new HashMap<String, Object>();

            param.put("compId", compId);
            param.put("userId", userId);

            String objSql="";
            if (!StringUtils.isBlank(objTypes)) {
                objSql=objTypes.replaceAll(",", "' or obj_type='");
                objSql="(obj_type='"+objSql+"')";
            }
            param.put("objSql", objSql);//所选择的人员的id列表

            String moduleSql="";
            if (!StringUtils.isBlank(moduleNames)) {
                moduleSql=moduleNames.replaceAll(",", "' or visit_module_id='");
                moduleSql="(visit_module_id='"+moduleSql+"')";
            }
            param.put("moduleSql", moduleSql);//所选择的人员的id列表

            String catSql="";
            if (!StringUtils.isBlank(catNames)&&!"请选择".equals(catNames)&&!"清空".equals(catNames)) {
                catSql=catNames.replaceAll(",", "')>0 or instr(cat_name, '");
                catSql="(instr(cat_name, '"+catSql+"')>0)";
            }
            param.put("catSql", catSql);//所选择的人员的id列表

            param.put("compName", compName);//所选择的人员的id列表
            param.put("userName", userName);//所选择的人员的id列表

            if (!StringUtils.isBlank(userIds)) {
                userIds=userIds.replaceAll(",", "' or visitor_id='");
                userIds="(visitor_id='"+userIds+"')";
            }
            param.put("userIds", userIds);//所选择的人员的id列表
            param.put("date1", date1);//开始时间
            param.put("date2", date2);//结束时间

            List<Map<String, Object>> _xxrzl=logService.getXxrzList(param);
            if (_xxrzl==null||_xxrzl.size()==0) return null;
            PageInfo<Map<String, Object>> pageList=new PageInfo<Map<String, Object>>(_xxrzl);

            //处理每一项
            List<Map<String, Object>> cl=new ArrayList<Map<String, Object>>();
            for (Map<String, Object> xxrz: _xxrzl) {
                Map<String, Object> newXxrz=new HashMap<String, Object>();
                newXxrz.put("id", xxrz.get("ID")==null?"":xxrz.get("ID"));
                newXxrz.put("moduleName", xxrz.get("VISIT_MODULE_ID")==null?"":xxrz.get("VISIT_MODULE_ID"));
                newXxrz.put("objType", xxrz.get("OBJ_TYPE")==null?"":xxrz.get("OBJ_TYPE"));
                newXxrz.put("objId", xxrz.get("OBJ_ID")==null?"":xxrz.get("OBJ_ID"));
                newXxrz.put("objName", xxrz.get("OBJ_NAME")==null?"":xxrz.get("OBJ_NAME"));
                newXxrz.put("catNames", xxrz.get("CAT_NAME")==null?"":xxrz.get("CAT_NAME"));
                newXxrz.put("visitorId", xxrz.get("VISITOR_ID")==null?"":xxrz.get("VISITOR_ID"));
                newXxrz.put("visitorName", xxrz.get("VISITOR_NAME")==null?"":xxrz.get("VISITOR_NAME"));
                newXxrz.put("groupId", xxrz.get("GROUP_ID")==null?"":xxrz.get("GROUP_ID"));
                newXxrz.put("groupName", xxrz.get("GROUP_NAME")==null?"":xxrz.get("GROUP_NAME"));
                Date tmp=null;
                if (null!=xxrz.get("CREATE_DATE")) {
                    tmp=new java.sql.Date(((Timestamp)xxrz.get("CREATE_DATE")).getTime());
                    newXxrz.put("createDate", DateUtils.convert2LongLocalStr(tmp));
                }
                cl.add(newXxrz);
            }
            retMap.put("total", pageList.getTotal());
            retMap.put("rows", cl);
            return retMap;
        } catch (Exception e) {
            e.printStackTrace();
            retMap.put("returnCode", "01");
            retMap.put("messageInfo", e.toString());
        }
        return retMap;
    }

    @RequestMapping("getcatkjtj")
    @ResponseBody
    public Map<String, Object> getcatkjtj(HttpServletRequest request, HttpServletResponse response,
            @RequestParam(required=false) String userIds,
            @RequestParam(required=false) String date1,
            @RequestParam(required=false) String date2,
            @RequestParam(value="offset", defaultValue="1") int offset,
            @RequestParam(value="limit", defaultValue="10") int limit) {
        Map<String, Object> param=new HashMap<String, Object>();  
        param.put("date1", date1);//开始时间
        param.put("date2", date2);//结束时间
        Map<String, Object> map = new HashMap<String, Object>();
        List<LogVisit> loglist = logService.getcatkjtj(param);
        if (loglist!=null) {
            PageInfo<LogVisit> pageList = new PageInfo<LogVisit>(loglist);
            map.put("total", pageList.getTotal());
            map.put("rows", pageList.getList());
        } else {
            map.put("total", null);
            map.put("rows", 0);
        }
        return map;
    }
    @RequestMapping("getcatmnsctj")
    @ResponseBody
    public Map<String, Object> getcatmnsctj(HttpServletRequest request, HttpServletResponse response,
            @RequestParam(required=false) String userIds,
            @RequestParam(required=false) String date1,
            @RequestParam(required=false) String date2,
            @RequestParam(value="offset", defaultValue="1") int offset,
            @RequestParam(value="limit", defaultValue="10") int limit) {
        Map<String, Object> param=new HashMap<String, Object>();  
        param.put("date1", date1);//开始时间
        param.put("date2", date2);//结束时间
        Map<String, Object> map = new HashMap<String, Object>();
        List<LogVisit> loglist = logService.getcatmnsctj(param);
        if (loglist!=null) {
            PageInfo<LogVisit> pageList = new PageInfo<LogVisit>(loglist);
            map.put("total", pageList.getTotal());
            map.put("rows", pageList.getList());
        } else {
            map.put("total", null);
            map.put("rows", 0);
        }
        return map;
    }

    /**
     * 企业统计
     * @return
     */
    @RequestMapping("getcatcomptj")
    @ResponseBody
    public Map<String, Object> getcatcomptj(HttpServletRequest request, HttpServletResponse response,
            @RequestParam(required=false) String userIds,
            @RequestParam(required=false) String date1,
            @RequestParam(required=false) String date2,
            @RequestParam(value="offset", defaultValue="1") int offset,
            @RequestParam(value="limit", defaultValue="10") int limit) {
        Map<String, Object> param=new HashMap<String, Object>();  
        param.put("date1", date1);//开始时间
        param.put("date2", date2);//结束时间
        Map<String, Object> map = new HashMap<String, Object>();
        List<LogVisit> loglist = logService.getcatcomptj(param);
        if (loglist!=null) {
            PageInfo<LogVisit> pageList = new PageInfo<LogVisit>(loglist);
            map.put("total", pageList.getTotal());
            map.put("rows", pageList.getList());
        } else {
            map.put("total", null);
            map.put("rows", 0);
        }
        return map;
    }
}