package com.example.boot.util.excle;


import cn.idev.excel.FastExcel;
import cn.idev.excel.read.listener.ReadListener;
import cn.idev.excel.write.metadata.style.WriteCellStyle;
import cn.idev.excel.write.metadata.style.WriteFont;
import cn.idev.excel.write.style.HorizontalCellStyleStrategy;
import cn.idev.excel.write.style.row.AbstractRowHeightStyleStrategy;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.poi.ss.formula.functions.T;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

/**
 * Excel工具类
 *
 * @author ljx
 * @version 1.0.0
 * @create 2024/9/3 下午3:37
 */
public class FastExcelUtil {

    public static final String SHEET_1 = "sheet1";


    public static <T> List<T> read(MultipartFile file, Class<T> clazz) throws IOException {
        return FastExcel.read(
                file.getInputStream(),
                clazz,
                null).sheet().doReadSync();
    }

    public static <T> List<T> read(MultipartFile file, Class<T> clazz, ReadListener<T> readListener) throws IOException {
        return FastExcel.read(
                file.getInputStream(),
                clazz,
                readListener).sheet().doReadSync();
    }

    public static <T> List<T> readWithListener(MultipartFile file, Class<T> clazz) throws IOException {
        ValidatingExcelListener<T> listener = ValidatingExcelListener.create();
        return FastExcel.read(
                file.getInputStream(),
                clazz,
                listener).sheet().doReadSync();
    }

    public static <T> List<T> read(MultipartFile file, String sheetName, Class<T> clazz) throws IOException {
        return FastExcel.read(
                file.getInputStream(),
                clazz,
                null).sheet(sheetName).doReadSync();
    }

    public static void create(String fileName, String sheetName, List<?> list, Class<T> clazz, HttpServletResponse response) throws IOException {
        HeaderUtil.setResponseHeaders(response, fileName);
        FastExcel.write(response.getOutputStream(), clazz).sheet(sheetName).doWrite(list);
    }

    public static void create(String fileName, List<?> list, Class<T> clazz, HttpServletResponse response) throws IOException {
        create(fileName, SHEET_1, list, clazz, response);
    }

    /**
     * 带样式的Excel导出（使用默认sheet名）
     *
     * @param fileName 文件名
     * @param list     数据列表
     * @param clazz    数据类型
     * @param response HTTP响应
     * @throws IOException IO异常
     */
    public static void createWithStyle(String fileName, List<?> list, Class<T> clazz, HttpServletResponse response) throws IOException {
        createWithStyle(fileName, SHEET_1, list, clazz, response);
    }

    /**
     * 带样式的Excel导出
     * 标题：宋体 9号 居中 加粗 固定行高20磅
     * 正文：宋体 9号 靠左 固定行高15磅
     *
     * @param fileName  文件名
     * @param sheetName sheet名称
     * @param list      数据列表
     * @param clazz     数据类型
     * @param response  HTTP响应
     * @throws IOException IO异常
     */
    public static void createWithStyle(String fileName, String sheetName, List<?> list, Class<T> clazz, HttpServletResponse response) throws IOException {
        HeaderUtil.setResponseHeaders(response, fileName);
        // 头的策略
        WriteCellStyle headWriteCellStyle = new WriteCellStyle();
        // 设置表头居中对齐
        headWriteCellStyle.setHorizontalAlignment(HorizontalAlignment.CENTER);
        headWriteCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        // 设置边框
        headWriteCellStyle.setBorderBottom(BorderStyle.THIN);
        headWriteCellStyle.setBorderLeft(BorderStyle.THIN);
        headWriteCellStyle.setBorderRight(BorderStyle.THIN);
        headWriteCellStyle.setBorderTop(BorderStyle.THIN);
        // 头字体策略
        WriteFont headFont = new WriteFont();
        headFont.setFontName("宋体");
        headFont.setFontHeightInPoints((short) 9);
        headFont.setBold(true);
        headWriteCellStyle.setWriteFont(headFont);
        // 内容的策略
        WriteCellStyle contentWriteCellStyle = new WriteCellStyle();
        // 设置内容靠左对齐
        contentWriteCellStyle.setHorizontalAlignment(HorizontalAlignment.LEFT);
        contentWriteCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        // 设置边框
        contentWriteCellStyle.setBorderBottom(BorderStyle.THIN);
        contentWriteCellStyle.setBorderLeft(BorderStyle.THIN);
        contentWriteCellStyle.setBorderRight(BorderStyle.THIN);
        contentWriteCellStyle.setBorderTop(BorderStyle.THIN);
        // 内容字体策略
        WriteFont contentFont = new WriteFont();
        contentFont.setFontName("宋体");
        contentFont.setFontHeightInPoints((short) 9);
        contentWriteCellStyle.setWriteFont(contentFont);
        // 设置头和内容策略
        HorizontalCellStyleStrategy horizontalCellStyleStrategy =
                new HorizontalCellStyleStrategy(headWriteCellStyle, contentWriteCellStyle);
        // 自定义行高策略
        AbstractRowHeightStyleStrategy rowHeightStyleStrategy = new AbstractRowHeightStyleStrategy() {
            @Override
            protected void setHeadColumnHeight(Row row, int relativeRowIndex) {
                // 设置表头行高为20磅
                row.setHeightInPoints(20);
            }

            @Override
            protected void setContentColumnHeight(Row row, int relativeRowIndex) {
                // 设置内容行高为15磅
                row.setHeightInPoints(15);
            }
        };
        FastExcel.write(response.getOutputStream(), clazz)
                .registerWriteHandler(horizontalCellStyleStrategy)
                .registerWriteHandler(rowHeightStyleStrategy)
                .head(clazz)
                .sheet(sheetName)
                .doWrite(list);
    }
}
