package com.NettyBoot.Common;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.log4j.Logger;




/**
 * 파일 관련 처리(생성, 삭제, 읽기, 쓰기, 백업 등)
 * 
 * @author jymoon
 *
 */
public class SingleFile {
	/** 메인 파일 인스턴스 */
	private File mainfile = null;	
	/** 파일 쓰기 스트림 */
	private FileOutputStream fos = null;	
	/** 파일 읽기 스트림 */
	private FileInputStream fis = null;		
	/** 백업 파일 인스턴스 */
	private File backupFile = null;		
	
	/** Logger*/
	static Logger logger = LogManager.GetConfiguredLogger(SingleFile.class);
	

	/**
	 * 생성자 1: 폴더 경로, 파일명, 백업파일 폴더를 가지고 객체 생성
	 * 
	 * @param folderPath : 폴더 경로
	 * @param fileName : 파일명
	 * @param backupFolder : 백업파일 폴더
	 */
	public SingleFile(String folderPath, String fileName)
	{
		File folder = new File(folderPath);
		
		if(!folder.exists()) {
			try {
				folder.mkdirs();
			}catch(Exception e) {
				logger.error(e);
			}
		}
		
		if(ProcessInfoGetter.isWindows()) {
			mainfile = new File(folderPath + "\\" + fileName);	
		} else {
			mainfile = new File(folderPath + "//" + fileName);
		}
		

	}

	/**
	 * 생성자 2: 파일 풀패스, 백업파일 풀패스를 가지고 객체 생성
	 * 
	 * @param fullFilePath : 파일 풀패스
	 * @param fullBackupfilePath : 백업파일 풀패스
	 */
	public SingleFile(String fullFilePath) {
		mainfile = new File(fullFilePath);

	}
	

	/**
	 * 파일 존재 여부 확인
	 * 
	 * @return : 파일이 존재하는지 여부
	 */
	public boolean isExist()
	{
		return mainfile.isFile();
	}
	
	/**
	 * 파일 길이 확인
	 * 
	 * @return : 파일 길이
	 */
	public long GetFileSize() {
		return mainfile.length();
	}
	
	/**
	 * 파일 생성
	 * 
	 * @return : 파일 생성 성공 여부
	 */
	public boolean Create()
	{
		try {
			return mainfile.createNewFile();
		} catch (IOException e) {
			logger.error(e);
			return false;
		}
	}
	
	/**
	 * 파일 삭제
	 * 
	 * @return : 파일 삭제 성공 여부
	 */
	public boolean DeleteIfExist()
	{
		/*try {
			Files.deleteIfExists(file.toPath());
			return true;
		} catch (IOException e) {
			logger.warn(e);
			return false;
		}*/
		return mainfile.delete();
	}
	
	/**
	 * 파일 후미에 버퍼 내용을 추가한다.(Append) 
	 * data의 offset 위치부터 len만큼의 내용이 파일 후미에 추가된다.
	 * 최초 호출시에는 fos(파일 출력 스트림)를 생성하면서, 기존 파일이 있는 경우 기존 파일 내용은 지워진다. 
	 * 
	 * @param data : 파일에 추가할 내용 버퍼
	 * @param offset : 파일에 쓸 data 버퍼의 첫번째 위치 
	 * @param len : 파일에 쓸 길이
	 */
	public void AppendWrite(byte[] data, int offset, int len)
	{
		if (len <= 0)
			return;
		
		try {
			if (fos == null)
			{
				fos = new FileOutputStream(mainfile);
			}

			fos.write(data, offset, len);
			logger.debug("written to file : " + len + "bytes");
		} catch (FileNotFoundException e) {
			logger.error(e);
		} catch (IOException e) {
			logger.error(e);
		}
	}
	
	/**
	 * 파일 내용을 읽어온다. 
	 * 읽어오는 위치는 기존에 읽었던 부분에서 buf의 size만큼 읽어오며,
	 * 파일의 남은 길이가 buf의 size보다 적은 경우는 파일의 남은 길이만큼 읽어온다. 
	 * 최초 호출시에는 fis(파일 입력 스트림)를 생성하면서, 파일 읽는 위치도 초기화된다. 
	 * 
	 * @param buf : 파일을 읽어들이는 버퍼
	 * @return : 실제로 읽어들인 바이트수
	 */
	public int ReadNext(byte[] buf)
	{
		try {
			if (fis == null)
			{
				fis = new FileInputStream(mainfile);
			}
			return fis.read(buf);
		} catch (FileNotFoundException e) {
			logger.error(e);
			return 0;
		} catch (IOException e) {
			logger.error(e);
			return 0;
		}		
	}
	
	/**
	 * 생성자에서 설정된 backupFile 경로에 파일(mainfile)을 백업(복사)한다.
	 * 
	 * @return : 백업 성공 여부
	 */
	public boolean BackupFile() {
		boolean ret = false;
		if (backupFile != null) {
			/*try {
				Files.deleteIfExists(backupFile.toPath());
			} catch (IOException e) {
				logger.warn(e);
			}
			
			try {
				Files.copy(file.toPath(), backupFile.toPath());
				ret = true;
			} catch (IOException e) {
				logger.warn(e);
			}*/
			backupFile.delete();
			byte[] buf = new byte[2048];
			if (fis != null) {
				try {
					fis.close();
				} catch(Exception e) {
					logger.error(e);
				}
				fis = null;
			}
			FileOutputStream fos_bak = null;
			try {
				fos_bak = new FileOutputStream(backupFile);
				while (true) {
					int readCnt = ReadNext(buf);
					if (readCnt <= 0) 
						break;
					fos_bak.write(buf, 0, readCnt);
				}
				fos_bak.close();
			} catch (FileNotFoundException e) {
				logger.error(e);
			} catch (IOException e) {
				logger.error(e);
			}
		} else {
			logger.warn("Backup path is not designated");
		}
		
		return ret;
	}
	
	/**
	 * 자원 해제(파일 작업을 위해 Open한 Stream들을 닫고 해제한다.)
	 */
	public void Close() {
		if (fis != null) try { fis.close(); } catch (IOException e) {} finally { fis = null; }
		if (fos != null) try { fos.close(); } catch (IOException e) {} finally { fos = null; }
	}
	
	
	/**
	 * 단위 테스트용 Main
	 * 
	 * @param args
	 */
//	public static void main(String[] args) {
//		SingleFile sf1 = new SingleFile("test.txt");
//		SingleFile sf2 = new SingleFile("test.txt");
//		SingleFile sf3 = new SingleFile("test1.txt");
//		SingleFile sf4 = new SingleFile("test2.txt");
//
//		if (sf3.isExist())
//		{
//			System.out.println("" + sf3.isExist() + " Delete." + sf3.DeleteIfExist());
//		}
//		
//		if (sf4.isExist())
//		{
//			System.out.println("" + sf4.isExist() + " Delete." + sf4.DeleteIfExist());
//		}
//	  
//		sf3.Create();
//		System.out.println("File Created");
//		sf4.Create();
//		System.out.println("File Created");
//	  
//		byte[] buf = new byte[1024];
//		int len;
//		do {
//			len = sf1.ReadNext(buf);
//			sf3.AppendWrite(buf, 0, len);
//			
//			len = sf2.ReadNext(buf);
//			sf4.AppendWrite(buf, 0, len);
//		} while (len > 0);
//	}
}
