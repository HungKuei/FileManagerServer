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

	public static byte[] getFileByte(String filePath, HttpServletResponse response) throws IOException {
		File file = new File(filePath);
		// 判断文件是否存在
		if (!file.exists()) {
			PrintWriter writer = null;
			try {
				response.setContentType("text/html;charset=UTF-8");
				writer = response.getWriter();
				writer.write("<!doctype html><title>404 Not Found</title><h1 style=\"text-align: center\">404 Not Found</h1><hr/><p style=\"text-align: center\">File Server</p>");
				writer.flush();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return null;
		}
		long fileSize = file.length();
		FileInputStream is = new FileInputStream(file);
		byte[] buffer = new byte[(int) fileSize];
		int offset = 0;
		int numRead = 0;
		while (offset < buffer.length
				&& (numRead = is.read(buffer, offset, buffer.length - offset)) >= 0) {
			offset += numRead;
		}
		// 确保所有数据均被读取
		if (offset != buffer.length) {
			throw new IOException("Could not completely read file "
					+ file.getName());
		}
		is.close();
		return buffer;
	}

	public static void downloadFile(byte[] data, String fileName, HttpServletResponse response) throws Exception {
		response.setContentType("application/force-download");
		response.addHeader("Content-Disposition", "attachment;filename="+URLEncoder.encode(fileName,"UTF-8"));
		OutputStream os = response.getOutputStream();
		os.write(data);
		os.flush();
		os.close();
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
