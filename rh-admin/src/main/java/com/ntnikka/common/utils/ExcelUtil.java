package com.ntnikka.common.utils;

import com.ntnikka.modules.orderManager.entity.TradeOrder;
import com.ntnikka.modules.pay.aliPay.utils.DateUtil;
import org.apache.poi.hssf.usermodel.*;

import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.util.List;

/**
 * @ClassName ExcelUtil
 * @Author Liuq
 * @Description todo
 * @Date 2018/9/27 12:53
 **/
public class ExcelUtil {

    public static final String[] title = {"序号", "商户ID", "商户名称", "支付方式", "商户订单号", "系统订单号", "银行订单号", "订单金额", "支付时间", "通知状态"};


    /**
     * 导出Excel
     *
     * @param sheetName sheet名称
     * @param title     标题
     * @param values    内容
     * @param wb        HSSFWorkbook对象
     * @return
     */
    public static HSSFWorkbook getHSSFWorkbook(String sheetName, String[] title, String[][] values, HSSFWorkbook wb) {

        // 第一步，创建一个HSSFWorkbook，对应一个Excel文件
        if (wb == null) {
            wb = new HSSFWorkbook();
        }

        // 第二步，在workbook中添加一个sheet,对应Excel文件中的sheet
        HSSFSheet sheet = wb.createSheet(sheetName);

        // 第三步，在sheet中添加表头第0行,注意老版本poi对Excel的行数列数有限制
        HSSFRow row = sheet.createRow(0);

        // 第四步，创建单元格，并设置值表头 设置表头居中
        HSSFCellStyle style = wb.createCellStyle();
//        style.setAlignment(); // 创建一个居中格式

        //声明列对象
        HSSFCell cell = null;

        //创建标题
        for (int i = 0; i < title.length; i++) {
            cell = row.createCell(i);
            cell.setCellValue(title[i]);
            cell.setCellStyle(style);
        }

        //创建内容
        for (int i = 0; i < values.length; i++) {
            row = sheet.createRow(i + 1);
            for (int j = 0; j < values[i].length; j++) {
                //将内容按顺序赋给对应的列对象
                row.createCell(j).setCellValue(values[i][j]);
            }
        }
        return wb;
    }

    public static String[][] getContent(List<TradeOrder> orderList){
        String[][] content = new String[orderList.size()][];
        for (int i = 0; i < orderList.size(); i++) {
            TradeOrder order = orderList.get(i);
            content[i] = new String[title.length];
            content[i][0] = order.getId().toString();
            content[i][1] = order.getMerchantId().toString();
            content[i][2] = order.getMerchantName();
            content[i][3] = order.getPayType().equals("Wap") ? "Wap支付" : "二维码支付";
            content[i][4] = order.getOrderId();
            content[i][5] = order.getSysTradeNo() == null ? "" : order.getSysTradeNo();
            content[i][6] = order.getTradeNo() == null ? "" : order.getTradeNo();
            content[i][7] = order.getOrderAmount().toString();
            content[i][8] = order.getPayTime() == null ? "" : DateUtil.Date2Str(order.getPayTime());
            content[i][9] = order.getNotifyStatus() == 0 ? "未通知" : "已通知";
        }
        return content;
    }

    //发送响应流方法
    public static void setResponseHeader(HttpServletResponse response, String fileName) {
        try {
            try {
                fileName = new String(fileName.getBytes("GBK"), "ISO8859-1");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            response.setContentType("application/vnd.ms-excel;charset=ISO8859-1");
            response.setHeader("Content-Disposition", "attachment;filename=" + fileName);
            response.addHeader("Pargam", "no-cache");
            response.addHeader("Cache-Control", "no-cache");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
