package com.citywithincity.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.channels.FileChannel;

public class FileUtil {
	
	
	

	public static void delFolder(String folderPath) {
		try {
			delAllFile(folderPath); // 删除完里面所有内容
			String filePath = folderPath;
			filePath = filePath.toString();
			java.io.File myFilePath = new java.io.File(filePath);
			myFilePath.delete(); // 删除空文件夹
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// 删除指定文件夹下所有文件
	// param path 文件夹完整绝对路径
	public static boolean delAllFile(String path) {
		boolean flag = false;
		File file = new File(path);
		if (!file.exists()) {
			return flag;
		}
		if (!file.isDirectory()) {
			return flag;
		}
		String[] tempList = file.list();
		File temp = null;
		for (int i = 0; i < tempList.length; i++) {
			if (path.endsWith(File.separator)) {
				temp = new File(path + tempList[i]);
			} else {
				temp = new File(path + File.separator + tempList[i]);
			}
			if (temp.isFile()) {
				temp.delete();
			}
			if (temp.isDirectory()) {
				delAllFile(path + "/" + tempList[i]);// 先删除文件夹里面的文件
				delFolder(path + "/" + tempList[i]);// 再删除空文件夹
				flag = true;
			}
		}
		return flag;
	}
	
	
	public static boolean copyInputStream(InputStream is,File dest){
		FileOutputStream outputStream = null;
		try {
			outputStream = new FileOutputStream(dest);
			int count;
			byte[] buffer = new byte[1024];
			while((count = is.read(buffer)) > 0){
				outputStream.write(buffer,0,count);
			}
			
			return true;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally{
			try {
				is.close();
				if(outputStream!=null)outputStream.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		return false;
	}

	public static String toBase64(File file){
		try {
			return IoUtil.readBase64(file);
		} catch (IOException e) {
			return null;
		}
	}


	public static String toBase64(String file){
		try {
			return IoUtil.readBase64(file);
		} catch (IOException e) {
			return null;
		}
	}


	/**
	 * 拷贝文件
	 * @param s
	 * @param t
	 */
	public static boolean copyFile(File src, File dest) {

		FileInputStream fi = null;
		FileOutputStream fo = null;
		FileChannel in = null;
		FileChannel out = null;

		try {
			fi = new FileInputStream(src);
			fo = new FileOutputStream(dest);
			in = fi.getChannel();// 得到对应的文件通道
			out = fo.getChannel();// 得到对应的文件通道
			in.transferTo(0, in.size(), out);// 连接两个通道，并且从in通道读取，然后写入out通道

			return true;
		} catch (IOException e) {
			e.printStackTrace();

		} finally {
			try {
				if(fi!=null)fi.close();
				if(in!=null)in.close();
				if(fo!=null)fo.close();
				if(out!=null)out.close();

			} catch (IOException e) {

				e.printStackTrace();

			}

		}
		return false;

	}
	

	
	
}
