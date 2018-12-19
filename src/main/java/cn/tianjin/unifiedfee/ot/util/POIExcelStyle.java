package cn.tianjin.unifiedfee.ot.util;

import org.apache.poi.hssf.usermodel.HSSFBorderFormatting;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;

public class POIExcelStyle {
    private HSSFCellStyle _TITLE=null;//标题
    private HSSFCellStyle _LABEL1=null;//字段名-表眉
    private HSSFCellStyle _VALUE1=null;//字段值-表眉
    private HSSFCellStyle _TABLE_HEAD=null;//字段值-表眉
    private HSSFCellStyle _COMMON_CELL=null;//基本单元-剧中
    private HSSFCellStyle _NUM_CELL=null;//数值单元
    private HSSFCellStyle _MEMO_CELL=null;//大段文字
    private HSSFCellStyle _HEAD_MERGE_CELL=null;//表头合并

    private HSSFCellStyle _COMMON_CELL_alignleft=null;  //基本单元-靠左
    private HSSFCellStyle _COMMON_CELL_alignright=null; //基本单元-靠右

    public POIExcelStyle(HSSFWorkbook workbook) {
        this.build_TITLE(workbook);
        this.build_LABEL1(workbook);
        this.build_VALUE1(workbook);
        this.build_TABLE_HEAD(workbook);
        this.build_COMMON_CELL(workbook);
        this.build_NUM_CELL(workbook);
        this.build_MEMO_CELL(workbook);
        this.build_HEAD_MERGE_CELL(workbook);
    }
    private void _setDefaultBorder(HSSFCellStyle s) {
        s.setBorderTop(HSSFBorderFormatting.BORDER_THIN);
        s.setTopBorderColor(HSSFColor.BLACK.index);
        s.setBorderRight(HSSFBorderFormatting.BORDER_THIN);
        s.setRightBorderColor(HSSFColor.BLACK.index);
        s.setBorderBottom(HSSFBorderFormatting.BORDER_THIN);
        s.setBottomBorderColor(HSSFColor.BLACK.index);
        s.setBorderLeft(HSSFBorderFormatting.BORDER_THIN);
        s.setLeftBorderColor(HSSFColor.BLACK.index);
    }
    private void build_TITLE(HSSFWorkbook workbook) {
        HSSFFont font = workbook.createFont();
        font.setFontName("黑体");
        font.setFontHeightInPoints((short)22);
        font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);

        _TITLE=workbook.createCellStyle();
        _TITLE.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        _TITLE.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
        _TITLE.setFont(font);
    }
    private void build_LABEL1(HSSFWorkbook workbook) {
        HSSFFont font = workbook.createFont();
        font.setFontName("宋体");
        font.setFontHeightInPoints((short)12);
        font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);

        _LABEL1=workbook.createCellStyle();
        _LABEL1.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
        _LABEL1.setFont(font);
    }
    private void build_VALUE1(HSSFWorkbook workbook) {
        HSSFFont font = workbook.createFont();
        font.setFontName("宋体");
        font.setFontHeightInPoints((short)12);

        _VALUE1=workbook.createCellStyle();
        _VALUE1.setAlignment(HSSFCellStyle.ALIGN_LEFT);
        _VALUE1.setFont(font);
    }
    private void build_TABLE_HEAD(HSSFWorkbook workbook) {
        HSSFFont font = workbook.createFont();
        font.setFontName("黑体");
        font.setFontHeightInPoints((short)12);
        font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);

        _TABLE_HEAD=workbook.createCellStyle();
        _setDefaultBorder(_TABLE_HEAD);
        _TABLE_HEAD.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);
        _TABLE_HEAD.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        _TABLE_HEAD.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        _TABLE_HEAD.setFont(font);
    }
    private void build_COMMON_CELL(HSSFWorkbook workbook) {
        HSSFFont font = workbook.createFont();
        font.setFontName("宋体");
        font.setFontHeightInPoints((short)10);

        _COMMON_CELL=workbook.createCellStyle();
        _setDefaultBorder(_COMMON_CELL);
        _COMMON_CELL.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        _COMMON_CELL.setFont(font);

        _COMMON_CELL_alignleft=workbook.createCellStyle();
        _setDefaultBorder(_COMMON_CELL_alignleft);
        _COMMON_CELL_alignleft.setAlignment(HSSFCellStyle.ALIGN_LEFT);
        _COMMON_CELL_alignleft.setFont(font);

        _COMMON_CELL_alignright=workbook.createCellStyle();
        _setDefaultBorder(_COMMON_CELL_alignright);
        _COMMON_CELL_alignright.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
        _COMMON_CELL_alignright.setFont(font);
    }
    private void build_NUM_CELL(HSSFWorkbook workbook) {
        HSSFFont font = workbook.createFont();
        font.setFontName("宋体");
        font.setFontHeightInPoints((short)10);

        _NUM_CELL=workbook.createCellStyle();
        _setDefaultBorder(_NUM_CELL);
        _NUM_CELL.setDataFormat(HSSFDataFormat.getBuiltinFormat("#,#0"));
        _NUM_CELL.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
        _NUM_CELL.setFont(font);
    }
    private void build_MEMO_CELL(HSSFWorkbook workbook) {
        HSSFFont font = workbook.createFont();
        font.setFontName("宋体");
        font.setFontHeightInPoints((short)11);

        _MEMO_CELL=workbook.createCellStyle();
        _setDefaultBorder(_MEMO_CELL);
        _MEMO_CELL.setAlignment(HSSFCellStyle.ALIGN_LEFT);
        _MEMO_CELL.setVerticalAlignment(HSSFCellStyle.VERTICAL_TOP);
        _MEMO_CELL.setWrapText(true);
        _MEMO_CELL.setFont(font);
    }
    private void build_HEAD_MERGE_CELL(HSSFWorkbook workbook) {
        HSSFFont font = workbook.createFont();
        font.setFontName("黑体");
        font.setFontHeightInPoints((short)12);
        font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);

        _HEAD_MERGE_CELL=workbook.createCellStyle();
        _setDefaultBorder(_HEAD_MERGE_CELL);
        _HEAD_MERGE_CELL.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);
        _HEAD_MERGE_CELL.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        _HEAD_MERGE_CELL.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        _HEAD_MERGE_CELL.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
        _HEAD_MERGE_CELL.setFont(font);
    }

    public HSSFCellStyle getTitleStyle() {
        return _TITLE;
    }
    public HSSFCellStyle getLabel1Style() {
        return _LABEL1;
    }
    public HSSFCellStyle getValue1Style() {
        return _VALUE1;
    }
    public HSSFCellStyle getHeadStyle() {
        return _TABLE_HEAD;
    }
    public HSSFCellStyle getCommonCellStyle() {
        return _COMMON_CELL;
    }
    public HSSFCellStyle getCommonCellStyle(String align) {
        if ("left".equals(align==null?null:align.toLowerCase())) return _COMMON_CELL_alignleft;
        else
        if ("right".equals(align==null?null:align.toLowerCase())) return _COMMON_CELL_alignright;
        else
        return _COMMON_CELL;
    }
    public HSSFCellStyle getNumCellStyle() {
        return _NUM_CELL;
    }
    public HSSFCellStyle getMemoCellStyle() {
        return _MEMO_CELL;
    }
    public HSSFCellStyle getHeadMergeStyle() {
        return _HEAD_MERGE_CELL;
    }
}