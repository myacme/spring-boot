package com.example.boot.util.md5; /**
 * FileName: dealExcle
 * <p>
 * Author:   liujixiang
 * <p>
 * Date:     2021/2/26 15:50
 * <p>
 * Description:
 * <p>
 * History:
 *
 * <author>          <time>          <version>          <desc>
 * <p>
 * 作者姓名           修改时间           版本号              描述
 */


import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author ljx

 * @create 2021/2/26

 * @since 1.0.0

 */

public class dealExcle {

	/**
	 * 处理excle文件数据
	 *
	 * @param file
	 * @author: Ljx
	 * @return: java.util.List<java.util.Map < java.lang.String, java.lang.Object>>
	 * @time: 2021/1/6 14:52
	 */
	public List<Map<String, Object>> dealExcle(MultipartFile file) {
		List<Map<String, Object>> sources = new ArrayList<>();
		try {
			//判断文件是否存在
			if (!file.isEmpty() && file.getSize() > 0) {
				//.是特殊字符，需要转义！！！！！
				String[] split = file.getOriginalFilename().split("\\.");
				Workbook wb;
				byte[] byteArr = file.getBytes();
				InputStream inputStream = new ByteArrayInputStream(byteArr);
				//根据文件后缀（xls/xlsx）进行判断
				if ("xls".equals(split[1])) {
					wb = new HSSFWorkbook(inputStream);
				} else if ("xlsx".equals(split[1])) {
					wb = new XSSFWorkbook(inputStream);
				} else {
					System.out.println("文件类型错误!");
					return null;
				}
				//获取所有的Sheet
				for (int sIndex = 0; sIndex < wb.getNumberOfSheets(); sIndex++) {
					Sheet sheet = wb.getSheetAt(sIndex);
					//获取字段名
					Row title = sheet.getRow(1);
					//跳过上面4行提示数据
					for (int rIndex = sheet.getFirstRowNum() + 4; rIndex <= sheet.getLastRowNum(); rIndex++) {
						Row row = sheet.getRow(rIndex);
						StringBuilder cellString = new StringBuilder();
						if (row != null) {
							//排除全是""的行
							for (int cIndex = row.getFirstCellNum(); cIndex < row.getLastCellNum(); cIndex++) {
								Cell cell = row.getCell(cIndex);
								if (cell != null) {
									cellString.append(cell.toString().trim());
								}
							}
							if (cellString != null && !"".equals(cellString.toString())) {
								Map<String, Object> source = new HashMap<>();
								//设置源数据类型

								//将每一行数据封装成一个Map
								for (int cIndex = row.getFirstCellNum(); cIndex < row.getLastCellNum(); cIndex++) {
									Cell titleCell = title.getCell(cIndex);
									Cell cell = row.getCell(cIndex);
									if (titleCell != null && cell != null) {
										//格式化数字类型数据
										if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
											double cellValue = cell.getNumericCellValue();
											source.put(titleCell.toString().trim(), new DecimalFormat("#").format(cellValue));
											continue;
										}
										source.put(titleCell.toString().trim(), cell.toString().trim());
									}
								}
								//将所有map封装在list里
								sources.add(source);
							}
						}
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
		return sources;
	}

}