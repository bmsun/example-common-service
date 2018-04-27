package com.example.commons.utils;

import com.example.commons.model.ExcelVersionEnum;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/***
 * @Date 2018/4/14
 * @Description POI excel导入导出
 * @author zhanghesheng
 * */
@Slf4j
public class ExcelUtils {
    //sheet中总行数
    public int totalRows;
    //每一行总单元格数
    public static int totalCells;

    public static final String EMPTY = "";
    public static final String POINT = ".";
    public static SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");


    /**
     * @param fileName excel文件名
     * @return
     * @author zhanghesheng
     * @date 2018/4/15
     * @Description
     */
    public List<ArrayList<String>> importExcel(String fileName) throws Exception {
        //创建输入流
        FileInputStream fis = new FileInputStream(new File(fileName));
        Workbook workBook;
        if (fileName.endsWith(".xlsx")) {
            workBook = getWorkBook(ExcelVersionEnum.V2007, fis);
        } else {
            workBook = getWorkBook(ExcelVersionEnum.V2003, fis);
        }
        return readExcel(workBook);
    }

    /**
     * @param sheetName
     * @param titleName
     * @return
     * @author zhanghesheng
     * @date 2018/4/15
     * @Description
     */
    public void exportExcel(String sheetName, String titleName,String filePath,
                            String fileName, int[] columnWidth,
                            String[] columnName, List<ArrayList<String>> dataList) {
        if (columnName == null || columnWidth == null) {
            log.error("请指定列头与列宽度");
            return;
        }
        if (columnName.length == columnWidth.length) {
            if (fileName == null) {
                log.error("请指定要导出的文件名");
                return;
            }
            // 第一步，创建一个webbook，对应一个Excel文件
            Workbook workBook;
            if (fileName.endsWith(".xlsx")) {
                workBook = getWorkBook(ExcelVersionEnum.V2007, null);
            } else {
                if (!fileName.contains(".xls")) {
                    fileName = fileName.concat(".xls");
                }
                workBook = getWorkBook(ExcelVersionEnum.V2003, null);
            }
            // 第二步，在webbook中添加一个sheet,对应Excel文件中的sheet
            Sheet sheet = workBook.createSheet(sheetName);
            // sheet.setDefaultColumnWidth(15); //统一设置列宽
            for (int i = 0; i < columnName.length; i++) {
                for (int j = 0; j <= i; j++) {
                    if (i == j) {
                        sheet.setColumnWidth(i, columnWidth[j] * 256); // 单独设置每列的宽
                    }
                }
            }
            //设置标题
            setTitle(titleName, columnName, workBook, sheet);
            // 创建第1行 也就是表头
            Row row = sheet.createRow((int) 1);
            row.setHeightInPoints(37);// 设置表头高度
            // 第四步，创建表头单元格样式 以及表头的字体样式
            CellStyle style = workBook.createCellStyle();
            setColumnHeader(workBook, style);
            // 第四.一步，创建表头的列
            for (int i = 0; i < columnName.length; i++) {
                Cell cell = row.createCell(i);
                cell.setCellValue(columnName[i]);
                cell.setCellStyle(style);
            }

            // 第五步，创建单元格，并设置值
            setData(columnName, dataList, workBook, sheet);
            // 第六步，将文件存到指定位置
            FileOutputStream fout =null;
            try {
                File file =new File(filePath);
                if (!file.exists()){
                    file.mkdirs();
                }
                 file =new File(filePath+"/"+fileName);
                 fout = new FileOutputStream(file);
                workBook.write(fout);
                String str = "导出" + fileName + "成功！";
                System.out.println(str);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                String str1 = "导出" + fileName + "失败！";
                System.out.println(str1);
            }catch (IOException e){
                e.printStackTrace();
            } finally {
                try {
                    if (fout != null) {
                        fout.close();
                    }
                    workBook.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        } else {
            log.error("列数目长度名称三个数组长度要一致");
        }
    }

    private void setData(String[] columnName, List<ArrayList<String>> dataList, Workbook workBook, Sheet sheet) {
        Row row;
        for (int i = 0; i < dataList.size(); i++) {
            row = sheet.createRow((int) i + 2);
            // 为数据内容设置特点新单元格样式1 自动换行 上下居中
            CellStyle zidonghuanhang = workBook.createCellStyle();
            zidonghuanhang.setWrapText(true);// 设置自动换行
            zidonghuanhang.setVerticalAlignment(VerticalAlignment.CENTER); // 创建一个居中格式

            // 设置边框
            zidonghuanhang.setBottomBorderColor((short) 8);
            zidonghuanhang.setBorderBottom(BorderStyle.THIN);
            zidonghuanhang.setBorderLeft(BorderStyle.THIN);
            zidonghuanhang.setBorderRight(BorderStyle.THIN);
            zidonghuanhang.setBorderTop(BorderStyle.THIN);
            Cell datacell = null;
            for (int j = 0; j < columnName.length; j++) {
                datacell = row.createCell(j);
                datacell.setCellStyle(zidonghuanhang);
                for (String value : dataList.get(i)) {
                    datacell.setCellValue(value);
                }
            }
        }
    }

    private void setColumnHeader(Workbook workBook, CellStyle style) {
        style.setWrapText(true);// 设置自动换行
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER); // 创建一个居中格式

        style.setBottomBorderColor((short) 3);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        style.setBorderTop(BorderStyle.THIN);
        Font headerFont = workBook.createFont(); // 创建字体样式
        headerFont.setBold(true); // 字体加粗
        headerFont.setFontName("黑体"); // 设置字体类型
        headerFont.setFontHeightInPoints((short) 10); // 设置字体大小
        style.setFont(headerFont); // 为标题样式设置字体样式
    }


    private void setTitle(String titleName, String[] columnName, Workbook workBook, Sheet sheet) {
        // 创建第0行 也就是标题
        Row row1 = sheet.createRow((int) 0);
        row1.setHeightInPoints(50);// 设备标题的高度
        // 第三步创建标题的单元格样式style2以及字体样式headerFont1
        CellStyle style2 = workBook.createCellStyle();
        style2.setAlignment(HorizontalAlignment.CENTER);
        style2.setVerticalAlignment(VerticalAlignment.CENTER);
        //设置背景
        style2.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        //todo
        style2.setFillBackgroundColor((short) 3);
        Font headerFont1 = workBook.createFont(); // 创建字体样式
        headerFont1.setBold(true); // 字体加粗
        headerFont1.setFontName("黑体"); // 设置字体类型
        headerFont1.setFontHeightInPoints((short) 15); // 设置字体大小
        style2.setFont(headerFont1); // 为标题样式设置字体样式

        Cell cell1 = row1.createCell(0);// 创建标题第一列
        sheet.addMergedRegion(new CellRangeAddress(0, 0, 0,
                columnName.length - 1)); // 合并列标题
        cell1.setCellValue(titleName); // 设置值标题
        cell1.setCellStyle(style2); // 设置标题样式
    }


    /**
     * @param version Excel版本号
     * @return
     * @author zhanghesheng
     * @date 2018/4/14
     * @Description
     */
    private Workbook getWorkBook(ExcelVersionEnum version, InputStream inputStream) {
        Workbook workbook = null;
        if (inputStream != null) {
            try {
                if (version == ExcelVersionEnum.V2003) {
                    workbook = new HSSFWorkbook(inputStream);
                } else {
                    workbook = new XSSFWorkbook(inputStream);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            if (version == ExcelVersionEnum.V2003) {
                workbook = new HSSFWorkbook();
            } else {
                workbook = new XSSFWorkbook();
            }
        }
        return workbook;
    }

    private List<ArrayList<String>> readExcel(Workbook wb) {
        List<ArrayList<String>> list = new ArrayList<ArrayList<String>>();
        ArrayList<String> rowList = null;
        //读取sheet(页)
        try {
            for (int numSheet = 0; numSheet < wb.getNumberOfSheets(); numSheet++) {
                Sheet sheet = wb.getSheetAt(numSheet);
                if (sheet == null) {
                    continue;
                }
                totalRows = sheet.getLastRowNum();
                //读取Row,从第二行开始
                for (int rowNum = 1; rowNum <= totalRows; rowNum++) {
                    Row row = sheet.getRow(rowNum);
                    if (row != null) {
                        rowList = new ArrayList<String>();
                        totalCells = row.getLastCellNum();
                        //读取列，从第一列开始
                        for (int c = 0; c <= totalCells + 1; c++) {
                            Cell cell = row.getCell(c);
                            if (cell == null) {
                                rowList.add(EMPTY);
                                continue;
                            }
                            rowList.add(getValue(cell).trim());
                        }
                        list.add(rowList);
                    }
                }
            }
            return list;
        } finally {
            try {
                wb.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 单元格格式
     *
     * @param xssfCell
     * @return
     */
    @Deprecated
    public static String getXValue(XSSFCell xssfCell) {
        if (xssfCell.getCellTypeEnum() == CellType.BOOLEAN) {
            return String.valueOf(xssfCell.getBooleanCellValue());
        } else if (xssfCell.getCellTypeEnum() == CellType.NUMERIC) {
            String cellValue = "";
            if (PoiDateUtil.isCellDateFormatted(xssfCell)) {
                Date date = PoiDateUtil.getJavaDate(xssfCell.getNumericCellValue());
                cellValue = sdf.format(date);
            } else {
                DecimalFormat df = new DecimalFormat("#.##");
                cellValue = df.format(xssfCell.getNumericCellValue());
                String strArr = cellValue.substring(cellValue.lastIndexOf(POINT) + 1, cellValue.length());
                if (strArr.equals("00")) {
                    cellValue = cellValue.substring(0, cellValue.lastIndexOf(POINT));
                }
            }
            return cellValue;
        } else {
            return String.valueOf(xssfCell.getStringCellValue());
        }
    }


    /**
     * 单元格格式
     *
     * @param hssfCell
     * @return
     */
    @Deprecated
    @SuppressWarnings({"static-access", "deprecation"})
    public static String getHValue(HSSFCell hssfCell) {
        if (hssfCell.getCellTypeEnum() == CellType.BOOLEAN) {
            return String.valueOf(hssfCell.getBooleanCellValue());
        } else if (hssfCell.getCellTypeEnum() == CellType.NUMERIC) {
            String cellValue = "";
            if (PoiDateUtil.isCellDateFormatted(hssfCell)) {
                Date date = PoiDateUtil.getJavaDate(hssfCell.getNumericCellValue());
                cellValue = sdf.format(date);
            } else {
                DecimalFormat df = new DecimalFormat("#.##");
                cellValue = df.format(hssfCell.getNumericCellValue());
                String strArr = cellValue.substring(cellValue.lastIndexOf(POINT) + 1, cellValue.length());
                if (strArr.equals("00")) {
                    cellValue = cellValue.substring(0, cellValue.lastIndexOf(POINT));
                }
            }
            return cellValue;
        } else {
            return String.valueOf(hssfCell.getStringCellValue());
        }
    }


    /**
     * 单元格格式
     *
     * @param cell
     * @return todo 目前来看：无需区分XSSFCell 和HSSFCell
     */
    @SuppressWarnings({"static-access", "deprecation"})
    public static String getValue(Cell cell) {

        switch (cell.getCellTypeEnum()) {
            case BLANK:
                return EMPTY;
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            case NUMERIC:
                String cellValue = "";
                if (PoiDateUtil.isCellDateFormatted(cell)) {
                    Date date = PoiDateUtil.getJavaDate(cell.getNumericCellValue());
                    cellValue = sdf.format(date);
                } else {
                    DecimalFormat df = new DecimalFormat("#.##");
                    cellValue = df.format(cell.getNumericCellValue());
                    String strArr = cellValue.substring(cellValue.lastIndexOf(POINT) + 1, cellValue.length());
                    if (strArr.equals("00")) {
                        cellValue = cellValue.substring(0, cellValue.lastIndexOf(POINT));
                    }
                }
                return cellValue;
            case STRING:
                return String.valueOf(cell.getStringCellValue());
            case _NONE:
            case FORMULA:
            case ERROR:
            default:
                return "";

        }
    }


    static class PoiDateUtil extends DateUtil {
        protected static int absoluteDay(Calendar cal, boolean use1904windowing) {
            return DateUtil.absoluteDay(cal, use1904windowing);
        }
    }


    public static void main(String[] args) {
        System.out.println("22".concat(".xls"));
    }
}
