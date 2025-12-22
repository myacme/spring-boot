package com.example.boot.util;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;


/**
 * @author ljx
 * @version 1.0.0
 * @create 2022/8/5 15:32
 */
public class ZipUtil {

	private static final Logger logger = LoggerFactory.getLogger(ZipUtil.class);

	/**
	 * @param files
	 * @param fileName
	 * @return
	 *
	 * @throws Exception
	 */
	public static InputStream zip(MultipartFile[] files, String fileName) throws Exception {
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		ZipOutputStream zipOutputStream = new ZipOutputStream(byteArrayOutputStream);
		try {
			for (MultipartFile file : files) {
				zipOutputStream.putNextEntry(new ZipEntry(file.getOriginalFilename()));
				zipOutputStream.write(file.getBytes());
			}
			//ZipOutputStream必须在转数组之前关闭，否则数据不完整
			zipOutputStream.close();
			return new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
		} catch (IOException e) {
			e.printStackTrace();
			throw new IOException("创建zip文件失败");
		} finally {
			try {
				zipOutputStream.close();
				byteArrayOutputStream.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * @param inputStream
	 * @return
	 *
	 * @throws Exception
	 */
	public static List<OutputStream> unzip(InputStream inputStream) throws Exception {
		List<OutputStream> list = new ArrayList<>();
		try (ZipInputStream zipInputStream = new ZipInputStream(inputStream)) {
			ZipEntry nextEntry = null;
			while ((nextEntry = zipInputStream.getNextEntry()) != null) {
				ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
				byte[] bytes = new byte[1024];
				int length;
				while ((length = zipInputStream.read(bytes)) > 0) {
					byteArrayOutputStream.write(bytes, 0, length);
				}
				list.add(byteArrayOutputStream);
			}
			return list;
		} catch (IOException e) {
			throw new IOException();
		}
	}

	/**
	 * 获取当前工程的路径
	 *
	 * @return String
	 */
	public static String getProjectPath() {
		String canonicalPath = null;
		try {
			canonicalPath = new File(".").getCanonicalPath() + File.separator;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return canonicalPath;
	}
}
