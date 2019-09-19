package com.yunda.base.feiniao.report.utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ZipUtil {
	static Logger log = LoggerFactory.getLogger(ZipUtil.class);

	/**
	 * 对象转Byte数组
	 * 
	 * @param obj
	 * @return
	 */
	public static byte[] objectToByteArray(Object obj) {
		byte[] bytes = null;
		ByteArrayOutputStream byteArrayOutputStream = null;
		ObjectOutputStream objectOutputStream = null;
		try {
			byteArrayOutputStream = new ByteArrayOutputStream();
			objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
			objectOutputStream.writeObject(obj);
			objectOutputStream.flush();
			bytes = byteArrayOutputStream.toByteArray();

		} catch (IOException e) {
			log.error("objectToByteArray failed, " + e);
		} finally {
			if (objectOutputStream != null) {
				try {
					objectOutputStream.close();
				} catch (IOException e) {
					log.error("close objectOutputStream failed, " + e);
				}
			}
			if (byteArrayOutputStream != null) {
				try {
					byteArrayOutputStream.close();
				} catch (IOException e) {
					log.error("close byteArrayOutputStream failed, " + e);
				}
			}

		}
		return bytes;
	}

	/**
	 * Byte数组转对象
	 * 
	 * @param bytes
	 * @return
	 */
	public static Object byteArrayToObject(byte[] bytes) {
		Object obj = null;
		ByteArrayInputStream byteArrayInputStream = null;
		ObjectInputStream objectInputStream = null;
		try {
			byteArrayInputStream = new ByteArrayInputStream(bytes);
			objectInputStream = new ObjectInputStream(byteArrayInputStream);
			obj = objectInputStream.readObject();
		} catch (Exception e) {
			log.error("byteArrayToObject failed, " + e);
		} finally {
			if (byteArrayInputStream != null) {
				try {
					byteArrayInputStream.close();
				} catch (IOException e) {
					log.error("close byteArrayInputStream failed, " + e);
				}
			}
			if (objectInputStream != null) {
				try {
					objectInputStream.close();
				} catch (IOException e) {
					log.error("close objectInputStream failed, " + e);
				}
			}
		}
		return obj;
	}

	public static void zipFile(String path, File file, ZipOutputStream zos) throws IOException {
		try {
			// 读Object内容
			BufferedInputStream in = new BufferedInputStream(new FileInputStream(file));
			zos.putNextEntry(new ZipEntry(path + file.getName()));
			byte[] car = new byte[1024];
			int L = 0;
			while ((L = in.read(car)) != -1) {
				zos.write(car, 0, L);
			}

			if (in != null) {
				in.close();
			}

		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
	}

	public static boolean zipFile(String sourceFilePath,String zipFilePath,String fileName) {
		boolean flag = false;
		File sourceFile = new File(sourceFilePath);
		FileInputStream fis = null;
		BufferedInputStream bis = null;
		FileOutputStream fos = null;
		ZipOutputStream zos = null;
		
		if(!sourceFile.exists()){
			log.warn("待压缩的文件目录："+sourceFilePath+"不存在.");
		}else{
			try {
				File zipFile = new File(zipFilePath + "/" + fileName +".zip");
				if(zipFile.exists()){
					log.warn(zipFilePath + "目录下存在名字为:" + fileName +".zip" +"打包文件.");
				}else{
					File d = new File(zipFilePath);// 创建当前操作的文件夹
					if (!d.exists()) {
						d.mkdirs();
					}
					File[] sourceFiles = sourceFile.listFiles();
					if(null == sourceFiles || sourceFiles.length<1){
						log.warn("源文件目录：" + sourceFilePath + "里面不存在文件，无需压缩.");
					}else{
						fos = new FileOutputStream(zipFile);
						zos = new ZipOutputStream(new BufferedOutputStream(fos));
						byte[] bufs = new byte[1024*10];
						for(int i=0;i<sourceFiles.length;i++){
							//创建ZIP实体，并添加进压缩包
							ZipEntry zipEntry = new ZipEntry(sourceFiles[i].getName());
							zos.putNextEntry(zipEntry);
							//读取待压缩的文件并写进压缩包里
							fis = new FileInputStream(sourceFiles[i]);
							bis = new BufferedInputStream(fis, 1024*10);
							int read = 0;
							while((read=bis.read(bufs, 0, 1024*10)) != -1){
								zos.write(bufs,0,read);
							}
						}
						flag = true;
					}
				}
			} catch (FileNotFoundException e) {
//				e.printStackTrace();
				log.error(e.getMessage(), e);
				throw new RuntimeException(e);
			} catch (IOException e) {
//				e.printStackTrace();
				log.error(e.getMessage(), e);
				throw new RuntimeException(e);
			} finally{
				//关闭流
				try {
					if(null != bis) bis.close();
					if(null != zos) zos.close();
					if(null != fis) fis.close();
					if(null != fos) fos.close();
				} catch (IOException e) {
//					e.printStackTrace();
					log.error(e.getMessage(), e);
					throw new RuntimeException(e);
				}
			}
		}
		return flag;
	}
}
