package com.xtw.bridge.service;

import com.xtw.bridge.model.EnvironmentDevice;
import com.xtw.bridge.utils.MyUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.util.CellRangeAddress;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import javax.annotation.Resource;
import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * User: Mr.Chen
 * Date: 2021/10/18
 * Description: 全部设备数据导出为Excel
 */
@Configuration      //1.主要用于标记配置类，兼备Component的效果。
@EnableScheduling   // 2.开启定时任务
public class DataExportService {

    @Resource
    FibreTemperatureServiceImpl fibreTemperatureServiceImpl;
    @Resource
    EnvironmentServiceImpl environmentServiceImpl;
    @Resource
    OutPartialService outPartialService;

    //定义两种格式文件的后缀
    private final static String XLS = ".xls";
    private final static String XLSX = ".xlsx";

    // 导出所有设备的最新数据到Excel
    // 3.添加定时任务（每小时执行一次）
    @Scheduled(cron = "0 0/1 * * * ?")
    private void exportDataToExcel(){
        String beginTime = MyUtils.getDateTime(1, 0, -1);
        String endTime = MyUtils.getDateTime(1, 0, 0);
        HSSFRow row = null;

        //1.创建工作簿
        HSSFWorkbook wb = new HSSFWorkbook();


        // 获取光纤测温最新数据（所有分区三相最大值）
        LinkedHashMap<String, Object> map = fibreTemperatureServiceImpl.parseData(beginTime, endTime);
        // 构建光纤测温工作表
        HSSFSheet sheet = createSheet(wb, "光纤测温", new LinkedList<String>(){{add("A相"); add("B相"); add("C相"); add("时间");}}, 3);
        row =sheet.createRow(3);    //加3是因为我们标题和数据库字段已经占用三行
        row.createCell(0).setCellValue(String.valueOf(map.get("aPhase")));
        row.createCell(1).setCellValue(String.valueOf(map.get("bPhase")));
        row.createCell(2).setCellValue(String.valueOf(map.get("cPhase")));
        row.createCell(3).setCellValue(String.valueOf(map.get("datetime")));

        // 获取所有环境量最新数据
        ArrayList<HashMap<String,String>> environmentDataList = environmentServiceImpl.queryDatasHJL();
        // 构建环境量工作表
        HSSFSheet sheet2 = createSheet(wb, "环境量", new LinkedList<String>(){{add("分区"); add("温度"); add("湿度"); add("含氧量"); add("一氧化碳");add("硫化氢"); add("甲烷"); add("数据时间");}}, 7);
        // 渲染数据
        for(int i=0;i<environmentDataList.size();i++){
            row =sheet2.createRow(i+3);    //加3是因为我们标题和数据库字段已经占用三行

            row.createCell(0).setCellValue(environmentDataList.get(i).get("partitionID"));
            row.createCell(1).setCellValue(environmentDataList.get(i).get("sd"));
            row.createCell(2).setCellValue(environmentDataList.get(i).get("wd"));
            row.createCell(3).setCellValue(environmentDataList.get(i).get("yq"));
            row.createCell(4).setCellValue(environmentDataList.get(i).get("yyht"));
            row.createCell(5).setCellValue(environmentDataList.get(i).get("lhq"));
            row.createCell(6).setCellValue(environmentDataList.get(i).get("jw"));
            row.createCell(7).setCellValue(environmentDataList.get(i).get("time"));
        }

        // 获取所有表皮温度最新数据
        ArrayList<HashMap<String,String>> BPWDDataList = environmentServiceImpl.queryDatasBPWD();
        // 构建环境量工作表
        HSSFSheet sheet3 = createSheet(wb, "表皮温度", new LinkedList<String>(){{add("分区"); add("A相温度"); add("B相温度"); add("C相温度"); add("数据时间");}}, 4);
        // 渲染数据
        for(int i=0;i<BPWDDataList.size();i++){
            row =sheet3.createRow(i+3);    //加3是因为我们标题和数据库字段已经占用三行

            row.createCell(0).setCellValue(BPWDDataList.get(i).get("partitionID"));
            row.createCell(1).setCellValue(BPWDDataList.get(i).get("aPhase"));
            row.createCell(2).setCellValue(BPWDDataList.get(i).get("bPhase"));
            row.createCell(3).setCellValue(BPWDDataList.get(i).get("cPhase"));
            row.createCell(4).setCellValue(BPWDDataList.get(i).get("time"));
        }

        // 获取所有外置局放最新数据
        ArrayList<HashMap<String,String>> WZJFDataList = outPartialService.queryAllOutPartialData();
        // 构建外置局放工作表
        HSSFSheet sheet4 = createSheet(wb, "高频局放", new LinkedList<String>(){{add("分区"); add("设备ID"); add("A相最大放电量"); add("A相最大放电频次"); add("B相最大放电量"); add("B相最大放电频次"); add("C相最大放电量"); add("C相最大放电频次"); add("时间");}}, 8);
        row =sheet4.createRow(3);    //加3是因为我们标题和数据库字段已经占用三行
        // 渲染数据
        for(int i=0;i<WZJFDataList.size();i++){
            row =sheet4.createRow(i+3);    //加3是因为我们标题和数据库字段已经占用三行

            row.createCell(0).setCellValue(WZJFDataList.get(i).get("partitionID"));
            row.createCell(1).setCellValue(WZJFDataList.get(i).get("terminalID"));
            row.createCell(2).setCellValue(WZJFDataList.get(i).get("a_max_electric"));
            row.createCell(3).setCellValue(WZJFDataList.get(i).get("a_max_frequency"));
            row.createCell(4).setCellValue(WZJFDataList.get(i).get("b_max_electric"));
            row.createCell(5).setCellValue(WZJFDataList.get(i).get("b_max_frequency"));
            row.createCell(6).setCellValue(WZJFDataList.get(i).get("c_max_electric"));
            row.createCell(7).setCellValue(WZJFDataList.get(i).get("c_max_frequency"));
            row.createCell(8).setCellValue(WZJFDataList.get(i).get("time"));
        }


        // 摄像头






        // 导出为Excel
        String locDir = "C:\\ZhouDaiDatas\\";
        File file = new File(locDir);
        if(!file.exists()){
            file.mkdirs();
        }
        String locFle = "设备数据-" + (new SimpleDateFormat("yyyyMMddHHmm")).format(new Date()) + XLSX;
        String filePath = locDir + locFle;
        FileOutputStream out = null;
        try {
            System.out.println("开始生成文件");
            // 生成Excel的文件名
            out = new FileOutputStream(new File(filePath));
            wb.write(out);
            out.flush();
            out.close();
            System.out.println("文件生成成功");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("文件生成失败");
        }
    }

    // 生成表格样式
    private HSSFSheet createSheet(HSSFWorkbook wb, String sheetName, LinkedList<String> rowName, Integer endColumn){
        //1.1创建合并单元格对象
        CellRangeAddress callRangeAddress = new CellRangeAddress(0,1,0,endColumn);//起始行,结束行,起始列,结束列
        //1.2头标题样式
        HSSFCellStyle headStyle = createCellStyle(wb,(short)16);
        //1.3列标题样式
        HSSFCellStyle style = createCellStyle(wb,(short)12);
        // 1.4水平居中
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        // 1.5垂直居中
        style.setVerticalAlignment((HSSFCellStyle.VERTICAL_CENTER));
        //2.创建工作表
        HSSFSheet sheet = wb.createSheet(sheetName);
        //2.1加载合并单元格对象
        sheet.addMergedRegion(callRangeAddress);
        //设置默认列宽
        sheet.setDefaultColumnWidth(20);
        //3.创建行
        //3.1创建头标题行;并且设置头标题
        HSSFRow head=sheet.createRow(0);
        HSSFRow head2=sheet.createRow(1);
        head.setHeight((short) (20*30));
        head2.setHeight((short) (20*30));
        HSSFCell headCell= head.createCell(0);
        //加载单元格样式并赋值
        headCell.setCellStyle(headStyle);
        headCell.setCellValue(sheetName);
        // //3.2创建列标题;并且设置列标题
        HSSFRow row=sheet.createRow(2);
        row.setHeight((short) (20*25));
        HSSFCell cell=row.createCell(0);

        for(int i=0;i<rowName.size();i++){
            cell.setCellValue(rowName.get(i));
            cell.setCellStyle(style);
            cell = row.createCell(i+1);
        }

        return sheet;
    }



    //这个方法是创建表格样式的
    private static HSSFCellStyle createCellStyle(HSSFWorkbook workbook, short fontsize) {
        // TODO Auto-generated method stub
        HSSFCellStyle style = workbook.createCellStyle();
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER);//水平居中
        style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);//垂直居中
        //创建字体
        HSSFFont font = workbook.createFont();
        font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        font.setFontHeightInPoints(fontsize);
        //加载字体
        style.setFont(font);
        //设置行高
        return style;
    }

}
