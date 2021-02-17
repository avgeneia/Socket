package com.NettyBoot.Server;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.Arrays;

import org.apache.log4j.Logger;

import com.NettyBoot.Common.LogManager;
import com.NettyBoot.Common.ProcessInfoGetter;
import com.NettyBoot.Common.SingleFile;
import com.NettyBoot.Handler.ClientConnectionHandler;

/**
 * 파일 필터 구현(정규 표현식에 의한 파일 검색 기능) 
 * 
 * @author jymoon
 *
 */
class FilenameFilterImpl implements FilenameFilter {
	String filterStr = null;
	String skipStr = null;
	public FilenameFilterImpl(String filterStr, String skipStr) {
		if (filterStr.startsWith("*"))
			filterStr = "." + filterStr;
		if (skipStr.startsWith("*"))
			skipStr = "." + skipStr;
		
		this.filterStr = filterStr;
		this.skipStr = skipStr;
	}

	public boolean accept(File arg0, String arg1) {
		return (arg1.matches(filterStr) && !arg1.matches(skipStr));
	}
}

/**
 * ClientConnection을 구현한 클래스.
 * FileTransfer 서버에 연결된 개별 클라이언트에 대한 모든 처리를 담당
 * <ul><li> 메세지 송수신 </li>
 * <li> 파일 전송 및 백업</li>
 * <li> 파일 업로드 </li></ul>
 * 
 * @author jymoon
 *
 */
public class SyncSingleClient extends ClientConnectionHandler {
	/** 클라이언트->서버로 업로딩 중인 경우의 파일 작업 담당자 */
	SingleFile uploadingFile = null;
	/** 클라이언트->서버로 업로딩 중인 경우, 남아 있는 파일 사이즈 */
	int uploadFile_remainSize = 0;	
	
	/** 클라이언트로부터 오는 메세지 처리를 위한 버퍼 */
	byte[] rcvBuf = null;	
	
	/** Logger */
	static Logger logger = LogManager.GetConfiguredLogger(SyncSingleClient.class);
	
	/**
	 * 클래스의 인스턴스가 언제 소멸되는지를 확인하기 위한 디버그용 함수.
	 */
	public void finalize() {
        logger.debug("I'm SingleClientMgr...bye!!");
	}

	/**
	 * 패킷을 받았을 때의 처리(ClientConnection의 추상 메소드 구현).
	 */
	@Override
	public void OnReceivePacket(byte[] buf, int size) {
		if (rcvBuf == null) {
			rcvBuf = buf;
		} else {
			size += rcvBuf.length;
			rcvBuf = Arrays.copyOf(rcvBuf, rcvBuf.length + buf.length);
			System.arraycopy(buf, 0, rcvBuf, rcvBuf.length - buf.length, buf.length);
		}
		int offset = 0;
		try {
			SyncServerPacket rcvPack;
			do {
				int bufRemain = size - offset;
				if (uploadingFile != null) {
					if (uploadFile_remainSize <= bufRemain) {
						uploadingFile.AppendWrite(rcvBuf, offset, uploadFile_remainSize);
						offset += uploadFile_remainSize;
						
						// Send response
						byte[] sndBytes = "940|00011|Y".getBytes("utf-8"); //전문번호|사이즈|처리결과
						SendToClient(sndBytes, 0, sndBytes.length);
						
						logger.debug("SEND : " + new String(sndBytes));
						
						// release uploading file
						uploadFile_remainSize = 0;
						uploadingFile.Close();
						logger.info("filePath["+filePath+"] fileName["+fileName+"] Receive complete!!");
						
						// File backup
						//uploadingFile.BackupFile();
						
						uploadingFile = null;
					}
					else
					{
						uploadingFile.AppendWrite(rcvBuf, offset, bufRemain);
						offset += bufRemain;
						uploadFile_remainSize -= bufRemain;
					}
				} else {
					logger.debug("RECV : " + new String(rcvBuf) + " size["+size+"]");
					
					rcvPack = SyncServerPacket.ParseMessage(rcvBuf, offset);
					offset += Integer.parseInt(rcvPack.GetColumn(1));
					ProcessMsg(rcvPack);
				}
			} while (size > offset);
		} catch (StringIndexOutOfBoundsException e) {
			logger.warn("Incomplete packet. offset :" + offset + ", size : " + size);
		} catch (Exception e) {
			logger.error(e);
			offset = size;
		}
		if (size > offset) {
			rcvBuf = Arrays.copyOfRange(rcvBuf, offset, size);
		} else {
			rcvBuf = null;
		}
	}
	
	/**
	 * 수신된 패킷별 분기 처리
	 * 
	 * @param pack : 수신된 패킷
	 * @throws IOException 
	 * @throws  
	 */
	String filePath = "";
	String fileName = "";
	String fileSize = "";
	void ProcessMsg(SyncServerPacket pack) throws IOException {
		String msgNo = pack.GetColumn(0);
		SyncServerPacket retPack = null;
		String retMsgNo = "";
		
		switch(msgNo) {
		case "910" : 
			String osName = System.getProperty("os.name");
			
			filePath = pack.GetColumn(2);
			fileName = pack.GetColumn(3);
			fileSize = pack.GetColumn(4);
						
			retMsgNo = "920";
			retPack = new SyncServerPacket(4);
			retPack.SetColumn(0, retMsgNo);
			retPack.SetColumn(2, filePath);
			retPack.SetColumn(3, fileName);
			retPack.SetColumn(1, retPack.GetSize()); //컬럼모두 채우고 마지막에 사이즈 구하기
			
			
//			Random randomGenerator = new Random();
//			System.out.println(randomGenerator.nextInt(99999));
			
			SendToClient(retPack.GetBytes(), 0, retPack.GetBytes().length);
			logger.debug("SEND : " + retPack.GetString());
			break;
		case "930" :

			//파일 수신준비
			uploadingFile = new SingleFile(filePath, fileName);
			uploadFile_remainSize = Integer.parseInt(fileSize);
			uploadingFile.DeleteIfExist();
			
			break;
		default:
			logger.warn("Invalid Packet type : " + msgNo);
			break;
		}
		
//		if (retPack != null) {
//			SendToClient(retPack.GetBytes(), 0, Integer.parseInt(retPack.GetField(FIELDS.szsize)));
//		}
		
//		String STX = pack.GetColumn(0).substring(0, 1);
//		if (STX.equals("D")) {
//			String filePath = pack.GetColumn(1);
//			String fileName = pack.GetColumn(2);
//			String backupPath = pack.GetColumn(3);
//			String skipFile = pack.GetColumn(4);
//			logger.debug(filePath + "," + fileName + "," + backupPath + "," + skipFile);
//			SendFilesToClient(filePath, fileName, backupPath, skipFile);
//		}	
//		else if (STX.equals("U")) {
//			String filePath = pack.GetColumn(1);
//			String fileName = pack.GetColumn(2);
//			String backupPath = pack.GetColumn(3);
//			String fileSize = pack.GetColumn(4);
//			
//			uploadingFile = new SingleFile(filePath, fileName, backupPath);
//			uploadFile_remainSize = Integer.parseInt(fileSize);
//			uploadingFile.DeleteIfExist();
//			logger.debug(filePath + "," + fileName + "," + backupPath + "," + uploadFile_remainSize);
//		}
	}
	
	/**
	 * 검색 조건에 따라 폴더 검색을 하고, 검색된 파일들을 클라이언트에 전송
	 * 
	 * @param filePath : 검색할 폴더 경로
	 * @param fileName : 파일명(정규식 사용 가능)
	 * @param backupPath : 백업폴더 경로
	 * @param skipFile : 건너뛸 파일명(정규식 사용 가능)
	 * @throws IOException
	 */
	private void SendFilesToClient(String filePath, String fileName,
			String backupPath, String skipFile) throws IOException {
		int fileCount = 0;
		long fileTotalSize = 0;
		
		File dir = new File(filePath);
		File[] files = dir.listFiles(new FilenameFilterImpl(fileName, skipFile));
		
		// filePath가 존재하지 않는 경우, files는 null이 된다.
		if (files == null) {
			logger.error(filePath + " does not exist");
		} else {
			for (int i = 0; i < files.length; i++) {
				if (files[i].isFile()) {
					fileCount += 1;
					fileTotalSize += files[i].length();
					logger.debug("file name : " + files[i].getName() + ", size : " + files[i].length());
				}
			}
		}
		
		SyncServerPacket sndPack = new SyncServerPacket(1);
		sndPack.SetColumn(0, "D" + String.format("%3s", fileCount).replace(' ', '0') 
				+ String.format("%10s", fileTotalSize).replace(' ', '0'));
		byte[] sndBytes = sndPack.GetBytes();
		SendToClient(sndBytes, 0, sndBytes.length);
		
		if (files != null) {
			File backupFile = null;
			for (int i = 0; i < files.length; i++) {
				if (files[i].isFile()) {
					if (!backupPath.equals("")) {
						
						if(ProcessInfoGetter.isWindows()) {
							backupFile = new File(backupPath + "\\" + files[i].getName());	
						} else {
							backupFile = new File(backupPath + "//" + files[i].getName());
						}
						
					}
						
					SendAFileToClient(files[i], backupFile);
				}
			}
		}
	}
	
	/**
	 * 파일을 클라이언트로 전송
	 * (backupFile이 지정되어 있는 경우, 파일 백업 후 전송)
	 * 
	 * @param srcFile : 전송할 파일
	 * @param backupFile : 백업 폴더에 생성될 파일
	 * @throws IOException
	 */
	public void SendAFileToClient(File srcFile, File backupFile) throws IOException {
		logger.info("Transfer Start. file name : " + srcFile.getName() + ", size : " + srcFile.length());
		byte[] sndBytes = new byte[70];
		String strTmp = String.format("%-60s", srcFile.getName());
		System.arraycopy(strTmp.getBytes("utf-8"), 0, sndBytes, 0, 60);
		strTmp = String.format("%10s", srcFile.length()).replace(' ', '0');
		System.arraycopy(strTmp.getBytes("utf-8"), 0, sndBytes, 60, 10);
		SendToClient(sndBytes, 0, sndBytes.length);
		
		// 파일 백업
		if (backupFile != null) {
			SingleFile sf = new SingleFile(srcFile.getCanonicalPath(), backupFile.getCanonicalPath());
			sf.BackupFile();
			sf.Close();
		}					
		
		SendFileToClient(srcFile.getCanonicalPath());
		logger.info("Transfer Ended. file name : " + srcFile.getName() + ", size : " + srcFile.length());
	}
	
	/**
	 * 연결 종료시의 처리(ClientConnection의 추상 메소드 구현).
	 */
	@Override
	public void OnClose() {
		
	}
	
	/**
	 * 단위테스트용 main
	 * 
	 * @author Maxdh
	 *
	 */
//	public static void main(String[] args) {
//		
//		//System.out.println("여긴안오지?");
//		//SingleClientManager scm = new SingleClientManager();
//		//scm.SendFilesToClient(".", "*.*", "", "*Bin.*");
//		/*java.io.BufferedReader br = new java.io.BufferedReader(new java.io.InputStreamReader(System.in));
//		while (true) {
//			try {
//				System.out.print("Str : ");
//				String useStr = br.readLine();
//				System.out.print("RegEx : ");
//				String regEx = br.readLine();
//				
//				if (useStr.equals("exit")) 
//					break;
//				System.out.println(useStr.matches(regEx));
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
//			
//		}*/
//		SyncSingleClient scm = new SyncSingleClient();
//		String msgSnd = "D00000||*.*||";
//		  int msgLen;
//		try {
//			msgLen = msgSnd.getBytes("utf-8").length;
//			msgSnd = msgSnd.substring(0, 1) + String.format("%5s", msgLen).replace(' ', '0') 
//					  + msgSnd.substring(6);
//			for (int i = 0; i < 5; i++) {
//				byte[] snd = msgSnd.substring(0, 2).getBytes("utf-8");
//				scm.OnReceivePacket(snd, snd.length);
//				System.out.println(msgSnd.substring(0, 2));
//				snd = msgSnd.substring(2, 4).getBytes("utf-8");
//				scm.OnReceivePacket(snd, snd.length);
//				System.out.println(msgSnd.substring(2, 4));
//				snd = msgSnd.substring(4, 6).getBytes("utf-8");
//				scm.OnReceivePacket(snd, snd.length);
//				System.out.println(msgSnd.substring(4, 6));
//				snd = msgSnd.substring(6).getBytes("utf-8");
//				scm.OnReceivePacket(snd, snd.length);
//				System.out.println(msgSnd.substring(6));
//				
//				snd = msgSnd.getBytes("utf-8");
//				scm.OnReceivePacket(snd, snd.length);
//				System.out.println(msgSnd);
//			}
//		} catch (UnsupportedEncodingException e) {
//			e.printStackTrace();
//		}
//		  
//	}
}



