package cn.tianjin.unifiedfee.ot.util;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;

public abstract class POIExcelUtils {
    /**
     * 生成单行简单表头（单行单列，无合并单元格）
     * @param sheet 工作文件的页签对象
     * @param lineNum 表头所在行下标
     * @param beginColumnNum 表头开始的列坐标
     * @param headStyle 表头样式
     * @param headTitles 表头各列文字
     */
    public static void buildHeadInOneLine(HSSFSheet sheet, int lineNum, int beginColumnNum, HSSFCellStyle headStyle, String[] headTitles) {
       if (headTitles==null||headTitles.length==0||sheet==null||lineNum<0||beginColumnNum<0||headStyle==null) return;
       HSSFRow rh=sheet.createRow(lineNum);

       HSSFCell rhcell;
       for (int i=0; i<headTitles.length; i++) {
           rhcell=rh.createCell(beginColumnNum+i);
           rhcell.setCellValue(new HSSFRichTextString(headTitles[i]));
           rhcell.setCellStyle(headStyle);
       }
    }
}