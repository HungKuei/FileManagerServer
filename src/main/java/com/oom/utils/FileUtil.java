package com.oom.utils;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.util.UUID;

public class FileUtil {

	public static void uploadFile(byte[] file, String filePath, String fileName) throws Exception {
		File targetFile = new File(filePath);
		if (!targetFile.exists()) {
			targetFile.mkdirs();
		}
		FileOutputStream out = new FileOutputStream(filePath + "/"+ fileName);
		out.write(file);
		out.flush();
		out.close();
	}

	public static void downloadFile(String filePath, String fileName, HttpServletResponse response) throws Exception {
		// 判断文件是否存在
		File inFile = new File(filePath);
		if (!inFile.exists()) {
			PrintWriter writer = null;
			try {
				response.setContentType("text/html;charset=UTF-8");
				writer = response.getWriter();
				writer.write("<!doctype html><title>404 Not Found</title><h1 style=\"text-align: center\">404 Not Found</h1><hr/><p style=\"text-align: center\">File Server</p>");
				writer.flush();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return;
		}
		response.setContentType("application/force-download");
		response.addHeader("Content-Disposition", "attachment;filename="+URLEncoder.encode(fileName,"UTF-8"));
		FileInputStream is = null;
		OutputStream os = null;
		try {
			os = response.getOutputStream();
			is = new FileInputStream(inFile);
			byte[] buff = new byte[1024];
			int len;
			while ((len = is.read(buff)) != -1) {
				os.write(buff, 0, len);
			}
			os.flush();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				is.close();
				os.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public static String getFileNameNoEx(String filename) {
		if ((filename != null) && (filename.length() > 0)) {
			int dot = filename.lastIndexOf('.');
			if ((dot >-1) && (dot < (filename.length()))) {
				return filename.substring(0, dot);
			}
		}
		return filename;
	}

	public static boolean deleteFile(String filePath) {
		File file = new File(filePath);
		// 如果文件路径所对应的文件存在，并且是一个文件，则直接删除
		if (file.exists() && file.isFile()) {
			if (file.delete()) {
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}

	public static String renameToUUID(String fileName) {
		return UUID.randomUUID() + "." + fileName.substring(fileName.lastIndexOf(".") + 1);
	}
}
