package com.citywithincity.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.StreamCorruptedException;
import java.io.Writer;

import android.util.Base64;

public class IoUtil {
	
	 private static byte[] streamToBytes(InputStream in, int length) throws IOException {
       byte[] bytes = new byte[length];
       int count;
       int pos = 0;
       while (pos < length && ((count = in.read(bytes, pos, length - pos)) != -1)) {
           pos += count;
       }
       if (pos != length) {
           throw new IOException("Expected " + length + " bytes, read " + pos + " bytes");
       }
       return bytes;
   }
	 
	
	


	/**
	 * 閸愭瑥鍙哹ase64阎ㄥ嫭鏆熼幑锟? * @param base64
	 * 
	 * @param path
	 * @throws IOException
	 */
	public static void writeBase64(String base64, String path)
			throws IOException {
		write(path, Base64.decode(base64, 0));
	}
	
	@SuppressWarnings("unchecked")
	public static <T> T readObject(File file) throws StreamCorruptedException, FileNotFoundException, IOException, ClassNotFoundException{
		ObjectInputStream objectInputStream = null;
		try {
			objectInputStream = new ObjectInputStream(new FileInputStream(file));
			return((T)objectInputStream.readObject());
		} finally{
			close(objectInputStream);
		}
	}
	
	public static void writeObject(File file,Object data) throws FileNotFoundException, IOException{
		ObjectOutputStream objectOutputStream = null;
		try{
			objectOutputStream = new ObjectOutputStream(new FileOutputStream(file));
			objectOutputStream.writeObject(data);
		}finally{
			close(objectOutputStream);
		}
	}

	/**
	 * 镨囱褰嘼ase64
	 * 
	 * @param path
	 * @return
	 * @throws IOException
	 */
	public static String readBase64(String path) throws IOException {
		return Base64.encodeToString(readBytes(path), Base64.NO_WRAP);
	}

	public static String readBase64(File file) throws IOException {
		return Base64.encodeToString(readBytes(file), Base64.NO_WRAP);
	}


	public static byte[] readBytes(String path) throws IOException {
		File file = new File(path);
		return readBytes(file);
	}

	public static byte[] readBytes(File file) throws IOException {
		FileInputStream reader = new FileInputStream(file);
		byte[] buffer = new byte[(int) file.length()];
		reader.read(buffer);
		reader.close();
		return buffer;
	}

	/**
	 * 閸愭瑥鍙嗛弬锲︽
	 * 
	 * @param file
	 * @param content
	 * @throws IOException
	 */
	public static void write(String file, String content) throws IOException {
		FileWriter writer = new FileWriter(new File(file));
		writer.write(content);
		writer.close();
	}

	/**
	 * 閸愭瑥鍙嗛弬锲︽
	 * 
	 * @param file
	 * @param inputStream
	 * @param len
	 * @throws IOException
	 */
	public static void write(File file, InputStream inputStream, int len)
			throws IOException {
		FileOutputStream writer = new FileOutputStream(file);
		byte[] buffer = new byte[4096];
		do {
			int readed = inputStream.read(buffer, 0, Math.min(len, 4096));
			writer.write(buffer, 0, readed);
			len -= readed;
		} while (len > 0);
		writer.close();
	}

	public static void writeBytes(File file, byte[] content) throws IOException {
		FileOutputStream writer = null;
		try {
			writer = new FileOutputStream(file);
			writer.write(content);
		} finally {
			close(writer);
		}
	}

	public static void write(String file, byte[] content) throws IOException {
		writeBytes(new File(file), content);
	}

	/**
	 * 
	 * @param is
	 * @param os
	 * @throws IOException
	 */
	public static void copy(InputStream is, OutputStream os) throws IOException {
		byte[] buffer = new byte[1024 * 4];
		int len = -1;
		while ((len = is.read(buffer)) != -1) {
			os.write(buffer, 0, len);
		}
	}

	public static void copyAndCloseAll(InputStream is, OutputStream os)
			throws IOException {
		try {
			byte[] buffer = new byte[1024 * 4];
			int len = -1;
			while ((len = is.read(buffer)) != -1) {
				os.write(buffer, 0, len);
			}
		} finally {
			close(is);
			close(os);
		}

	}

	/**
	 * 
	 * @param reader
	 * @return
	 */
	public static String readAndClose(BufferedReader reader) {
		try {
			String line;
			StringBuilder sb = new StringBuilder();
			while ((line = reader.readLine()) != null) {
				sb.append(line);
			}
			return sb.toString();
		} catch (IOException e) {
			throw new RuntimeException(e);
		} finally {
			close(reader);
		}

	}
	public static void close(Writer w) {
		if (w != null)
			try {
				w.close();
			} catch (IOException e) {
			}
	}
	public static void close(InputStream inputStream) {
		if (inputStream != null)
			try {
				inputStream.close();
			} catch (IOException e) {
			}
	}

	public static void close(Reader reader) {
		if (reader != null)
			try {
				reader.close();
			} catch (IOException e) {
			}
	}

	public static void close(OutputStream outputStream) {
		if (outputStream != null)
			try {
				outputStream.close();
			} catch (IOException e) {
			}
	}

	public static void close(PrintWriter writer) {
		if (writer != null)
			writer.close();
	}

	public static final int BUFFER_SIZE = 4096;


	

	/**
	 * 从文件读取字符串
	 * 
	 * @param file
	 * @return
	 * @throws IOException 
	 */
	public static String readFromFile(File file) throws IOException {
		FileReader fr = null;
		StringBuffer sb = new StringBuffer();
		try {
			fr = new FileReader(file);
			char[] buffer = new char[BUFFER_SIZE];
			do {
				int readed = fr.read(buffer, 0, BUFFER_SIZE);
				if (readed <= 0) {
					break;
				}
				sb.append(buffer, 0, readed);
			} while (true);
			return sb.toString();
		} finally {
			close(fr);
		}
	}

	public static boolean writeToFile(byte[] content, File file) throws IOException {
		FileOutputStream fw = null;
		try {
			fw = new FileOutputStream(file);
			fw.write(content);
			fw.flush();
			return true;
		} finally {
			close(fw);
		}

	}

	public static void writeToFile(String content, File file) throws IOException {
		FileWriter fw = null;
		try {
			fw = new FileWriter(file);
			fw.write(content);
			fw.flush();
		}finally {
			close(fw);
		}

	}

}
