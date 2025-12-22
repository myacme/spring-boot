package com.example.boot.util;

import com.example.boot.bean.Variable;
import com.example.boot.util.sql.MybatisUtil;
import org.apache.poi.util.Units;
import org.apache.poi.xwpf.usermodel.*;
import org.apache.xmlbeans.XmlCursor;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author ljx
 * @version 1.0.0
 * @create 2024/4/8 14:15
 */
public class WordUtil {

    private static final Logger log = LoggerFactory.getLogger(WordUtil.class);


    public static void main(String[] args) {
        try {
            // 打开现有的Word文档
            XWPFDocument document = new XWPFDocument(Files.newInputStream(Paths.get("D:\\OneDriver\\Desktop\\test.docx")));
//            FileInputStream in = new FileInputStream("C:\\Users\\MyAcme\\Desktop\\picture.png");
            // 替换文档中的变量
            replaceText(document, "${A1}", "100-D0101");
//			replaceText(document, "${A2}", "---Replacement Text 2---");
//            replacePicture(document, "${A1}", in, "picture.png");
            // 保存文档
            FileOutputStream out = new FileOutputStream("D:\\OneDriver\\Desktop\\replace.docx");
            document.write(out);
            out.close();
            document.close();
            log.info("Word文档中的变量替换完成！");
        } catch (Exception e) {
            log.error("发生异常：" + e.getMessage());
            e.printStackTrace();
        }
    }


    /**
     * 循环word段落替换变量
     *
     * @param in
     * @param variables
     * @return
     */
    public static ByteArrayOutputStream substitutionVariable(InputStream in, List<Variable> variables) {
        try (XWPFDocument doc = new XWPFDocument(in);
             ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            //循环段落
            circularParagraph(doc, variables);
            //循环表格
            circularTable(doc, variables);
            //循环页眉
            circularHeader(doc, variables);
            // 保存文档
            doc.write(out);
            log.info("Word文档中的变量替换完成！");
            return out;
        } catch (Exception e) {
            log.error("发生异常：" + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 循环段落
     *
     * @param doc
     * @param variables
     * @throws Exception
     */
    private static void circularParagraph(XWPFDocument doc, List<Variable> variables) throws Exception {
        for (XWPFParagraph p : doc.getParagraphs()) {
            for (XWPFRun r : p.getRuns()) {
                String text = r.getText(0);
                if (StringUtils.isNotEmpty(text)) {
                    List<String> keys = MybatisUtil.extractVariable(text);
                    if (!CollectionUtils.isEmpty(keys)) {
                        for (String key : keys) {
                            Variable variable = searchVariable(key, variables);
                            replaceVariable(doc, null, p, r, text, variable);
                            text = r.getText(0);
                        }
                    }
                }
            }
        }
    }

    /**
     * 循环表格
     *
     * @param doc
     * @param variables
     * @throws Exception
     */
    private static void circularTable(XWPFDocument doc, List<Variable> variables) throws Exception {
        List<XWPFTable> tables = doc.getTables();
        for (XWPFTable table : tables) {
            if (!CollectionUtils.isEmpty(MybatisUtil.extractVariable(table.getText()))) {
                for (XWPFTableRow row : table.getRows()) {
                    for (XWPFTableCell tableCell : row.getTableCells()) {
                        if (!CollectionUtils.isEmpty(MybatisUtil.extractVariable(tableCell.getText()))) {
                            for (XWPFParagraph p : tableCell.getParagraphs()) {
                                for (XWPFRun r : p.getRuns()) {
                                    String rText = r.getText(0);
                                    if (StringUtils.isNotEmpty(rText)) {
                                        List<String> rkeys = MybatisUtil.extractVariable(rText);
                                        if (!CollectionUtils.isEmpty(rkeys)) {
                                            for (String rKey : rkeys) {
                                                Variable rVariable = searchVariable(rKey, variables);
                                                replaceVariable(doc, tableCell, p, r, rText, rVariable);
                                                rText = r.getText(0);
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }


    /**
     * 循环页眉
     *
     * @param doc
     * @param variables
     * @throws Exception
     */
    private static void circularHeader(XWPFDocument doc, List<Variable> variables) throws Exception {
        List<XWPFHeader> headers = doc.getHeaderList();
        for (XWPFHeader header : headers) {
            for (XWPFParagraph p : header.getParagraphs()) {
                for (XWPFRun r : p.getRuns()) {
                    String text = r.getText(0);
                    if (StringUtils.isNotEmpty(text)) {
                        List<String> keys = MybatisUtil.extractVariable(text);
                        if (!CollectionUtils.isEmpty(keys)) {
                            for (String key : keys) {
                                Variable variable = searchVariable(key, variables);
                                replaceVariable(doc, null, p, r, text, variable);
                                text = r.getText(0);
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * 循环页脚
     *
     * @param doc
     * @param variables
     * @throws Exception
     */
    private static void circularFooter(XWPFDocument doc, List<Variable> variables) throws Exception {
        List<XWPFFooter> footers = doc.getFooterList();
        for (XWPFFooter footer : footers) {
            for (XWPFParagraph p : footer.getParagraphs()) {
                for (XWPFRun r : p.getRuns()) {
                    String text = r.getText(0);
                    if (StringUtils.isNotEmpty(text)) {
                        List<String> keys = MybatisUtil.extractVariable(text);
                        if (!CollectionUtils.isEmpty(keys)) {
                            for (String key : keys) {
                                Variable variable = searchVariable(key, variables);
                                replaceVariable(doc, null, p, r, text, variable);
                                text = r.getText(0);
                            }
                        }
                    }
                }
            }
        }
    }


    private static void replaceVariable(XWPFDocument doc, XWPFTableCell tableCell, XWPFParagraph p, XWPFRun r, String text, Variable variable) throws Exception {
        if (variable != null) {
            String type = variable.getType();
            if ("值".equals(type)) {
                replaceText(r, text, variable);
            }
            if ("表格".equals(type)) {
                replaceTable(doc, tableCell, p, r, text, variable);
            }
            if ("图片".equals(type)) {
                replacePicture(r, text, variable);
            }
        }
    }


    /**
     * 查找变量
     *
     * @param key
     * @param variables
     * @return
     */
    public static Variable searchVariable(String key, List<Variable> variables) throws Exception {
        for (Variable variable : variables) {
            if (variable.getKey().equals(key)) {
                return variable;
            }
        }
        return null;
    }


    /**
     * 替换文本的辅助方法
     *
     * @param doc
     * @param findText
     * @param replaceText
     */
    private static void replaceText(XWPFDocument doc, String findText, String replaceText) throws Exception {
        List<XWPFTable> tables = doc.getTables();
        for (XWPFTable table : tables) {
            String text = table.getText();
            if (text != null && text.contains(findText)) {
                for (XWPFTableRow row : table.getRows()) {
                    for (XWPFTableCell tableCell : row.getTableCells()) {
                        for (XWPFParagraph p : tableCell.getParagraphs()) {
                            for (XWPFRun r : p.getRuns()) {
                                String text1 = r.getText(0);
                                if (text1 != null && text1.contains(findText)) {
                                    text1 = text1.replace(findText, replaceText);
                                    r.setText(text1, 0);
                                }
                            }
                        }
                    }
                }
            }
        }
        for (XWPFParagraph p : doc.getParagraphs()) {
            for (XWPFRun r : p.getRuns()) {
                String text = r.getText(0);
                if (text != null && text.contains(findText)) {
                    text = text.replace(findText, replaceText);
                    r.setText(text, 0);
                }
            }
        }
    }

    /**
     * 替换文本
     *
     * @param text
     * @param r
     * @param variable
     */
    private static void replaceText(XWPFRun r, String text, Variable variable) throws Exception {
        r.setText(text.replace(variable.getKey(), StringUtils.getString(variable.getValue())), 0);
    }

    /**
     * 替换表格
     *
     * @param doc
     * @param key
     * @param head
     * @param data
     */
    private static void replaceTable(XWPFDocument doc, String key, List<String> head, List<LinkedHashMap<String, Object>> data) throws Exception {
        for (XWPFParagraph p : doc.getParagraphs()) {
            for (XWPFRun r : p.getRuns()) {
                String text = r.getText(0);
                if (text != null) {
                    text = text.trim();
                    if (text.contains(key)) {
                        r.setText(text.replace(key, ""), 0);
                        XmlCursor cursor = p.getCTP().newCursor();
                        // 在指定游标位置插入表格
                        XWPFTable table = doc.insertNewTbl(cursor);
                        CTTblPr tablePr = table.getCTTbl().getTblPr();
                        CTTblWidth width = tablePr.addNewTblW();
                        //设置表格宽度
                        width.setW(BigInteger.valueOf(7000));
                        insertInfo(table, head, data);
                        return;
                    }
                }
            }
        }
    }

    /**
     * 替换表格
     *
     * @param doc
     * @param p
     * @param r
     * @param text
     * @param variable
     */
    private static void replaceTable(XWPFDocument doc, XWPFTableCell tableCell, XWPFParagraph p, XWPFRun r, String text, Variable variable) throws Exception {
        r.setText(text.replace(variable.getKey(), ""), 0);
        if (!CollectionUtils.isEmpty(variable.getData())) {
            XWPFTable table;
            XmlCursor cursor = p.getCTP().newCursor();
            // 在指定游标位置插入表格
            if (tableCell != null) {
                //表格内部插入表格
                table = tableCell.insertNewTbl(cursor);
                insertTableInfo(table, variable.getHead(), variable.getData());
            } else {
                //段落中插入表格
                table = doc.insertNewTbl(cursor);
                insertInfo(table, variable.getHead(), variable.getData());
            }
        }
    }

    /**
     * 替换图片
     *
     * @param r
     * @param variable
     * @throws Exception
     */
    private static void replacePicture(XWPFRun r, String text, Variable variable) throws Exception {
        r.setText(text.replace(variable.getKey(), ""), 0);
        if (variable.getPicture() != null) {
            int width = Units.pixelToEMU(300);
            int height = Units.pixelToEMU(200);
            r.addPicture(new ByteArrayInputStream(variable.getPicture().toByteArray()), XWPFDocument.PICTURE_TYPE_PNG, variable.getPictureName(), width, height);
        }
    }


    /**
     * 替换图片
     *
     * @param doc
     * @param key
     * @param inputStream
     * @param name
     * @throws Exception
     */
    private static void replacePicture(XWPFDocument doc, String key, InputStream inputStream, String name) throws Exception {
        for (XWPFParagraph p : doc.getParagraphs()) {
            for (XWPFRun r : p.getRuns()) {
                String text = r.getText(0);
                if (text != null) {
                    text = text.trim();
                    if (text.contains(key)) {
                        r.setText(text.replace(key, ""), 0);
                        int width = Units.pixelToEMU(300);
                        int height = Units.pixelToEMU(200);
                        r.addPicture(inputStream, XWPFDocument.PICTURE_TYPE_PNG, name, width, height);
                        return;
                    }
                }
            }
        }
    }


    /**
     * 填充表格数据
     *
     * @param table
     * @param head
     * @param data
     */
    private static void insertTableInfo(XWPFTable table, List<String> head, List<LinkedHashMap<String, Object>> data) throws Exception {
        //创建第一行
        XWPFTableRow row = table.createRow();
        //设置表格宽度
//        width.setW(BigInteger.valueOf(7000));
        //改变长度策略为自己调整 默认为auto
//        table.getCTTbl().addNewTblPr().addNewTblW().setType(STTblWidth.AUTO);
        //根据头创建表格head
        for (int col = 0; col < head.size(); col++) {
            // 第一行创建了多少列，后续增加的行自动增加列
            CTTcPr cPr = row.createCell().getCTTc().addNewTcPr();
            cPr.addNewVAlign().setVal(STVerticalJc.CENTER);
            //设置单元格高度为500
//			row.getCtRow().addNewTrPr().addNewTrHeight().setVal(BigInteger.valueOf(500));
            //可以用来设置单元格长度
//            cPr.addNewTcW().setType(STTblWidth.AUTO);
//            width.setW(BigInteger.valueOf(2000));
        }
        //循环给表格添加头信息
        for (int i = 0; i < head.size(); i++) {
            //往表格里面写入头信息
            row.getCell(i).setText(head.get(i));
        }
        //循环填充body列表
        for (LinkedHashMap<String, Object> datum : data) {
            row = table.createRow();
            //修改行高为500
//			row.getCtRow().addNewTrPr().addNewTrHeight().setVal(BigInteger.valueOf(500));
            Set<Map.Entry<String, Object>> entries = datum.entrySet();
            int i = 0;
            for (Map.Entry<String, Object> key : entries) {
                row.getCell(i).setText(String.valueOf(key.getValue()));
                i++;
            }
        }
    }

    /**
     * 填充表格数据
     *
     * @param table
     * @param head
     * @param data
     */
    private static void insertInfo(XWPFTable table, List<String> head, List<LinkedHashMap<String, Object>> data) throws Exception {
        //获取第一行
        XWPFTableRow row = table.getRow(0);
        //设置表格宽度
//        width.setW(BigInteger.valueOf(7000));
        //改变长度策略为自己调整 默认为auto
//        table.getCTTbl().addNewTblPr().addNewTblW().setType(STTblWidth.AUTO);
        //根据头创建表格head
        //默认会创建一列，即从第2列开始
        for (int col = 1; col < head.size(); col++) {
            // 第一行创建了多少列，后续增加的行自动增加列
            CTTcPr cPr = row.createCell().getCTTc().addNewTcPr();
            cPr.addNewVAlign().setVal(STVerticalJc.CENTER);
            //设置单元格高度为500
//			row.getCtRow().addNewTrPr().addNewTrHeight().setVal(BigInteger.valueOf(500));
            //可以用来设置单元格长度
//            cPr.addNewTcW().setType(STTblWidth.AUTO);
//            width.setW(BigInteger.valueOf(2000));
        }
        //循环给表格添加头信息
        for (int i = 0; i < head.size(); i++) {
            //往表格里面写入头信息
            row.getCell(i).setText(head.get(i));
        }
        //循环填充body列表
        for (LinkedHashMap<String, Object> datum : data) {
            row = table.createRow();
            //修改行高为500
//			row.getCtRow().addNewTrPr().addNewTrHeight().setVal(BigInteger.valueOf(500));
            Set<Map.Entry<String, Object>> entries = datum.entrySet();
            int i = 0;
            for (Map.Entry<String, Object> key : entries) {
                row.getCell(i).setText(String.valueOf(key.getValue()));
                i++;
            }
        }
    }

    /**
     * 合并单元格
     *
     * @param table   table
     * @param col     第几列
     * @param fromRow 开始行
     * @param toRow   结束行
     */
    public static void mergeCellsVertically(XWPFTable table, int col, int fromRow, int toRow) throws Exception {
        for (int rowIndex = fromRow; rowIndex <= toRow; rowIndex++) {
            XWPFTableCell cell = table.getRow(rowIndex).getCell(col);
            if (rowIndex == fromRow) {
                // 第一个合并的单元格设置为RESTART合并值
                cell.getCTTc().addNewTcPr().addNewVMerge().setVal(STMerge.RESTART);
            } else {
                // 连接(合并)第一个单元格的单元格设置为CONTINUE
                cell.getCTTc().addNewTcPr().addNewVMerge().setVal(STMerge.CONTINUE);
            }
        }
    }
}