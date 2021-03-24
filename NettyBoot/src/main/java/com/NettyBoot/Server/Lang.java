package com.NettyBoot.Server;

public class Lang {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

//		typedef struct
//		{
//		      char    szExtrStaNo    [ 2]; /* 외부역번호  */
//		      char    szEqpNo        [ 1]; /* 장비번호    */ 
//		      char    szEqpType      [ 1]; /* 장비타입    */
//		      char    szCommSndSeq   [ 4]; /* 통신전송일련번호 */
//		      char    szSndDTime     [ 7]; /* 전송일시    */
//		      char    szCmdCode      [ 2]; /* 명령코드    */   
//		      char    szCmdCodeSndSeq[ 4]; /* 명령코드별 전송일련번호 */
//		      char    szCnqeCode     [ 1]; /* 결과코드 : 요청전문에서는 0x00 으로 설정한다. */ 
//		      char    szMsgSize      [ 4]; /* 자료길이 (STX, 메시지헤더, CRC, ETX를 제외한 DATA만의 길이) */ 
//		      char    szRecordCnt    [ 2]; /* 레코드 건수   */
//		      char    szRecordSize   [ 2]; /* 레코드 사이즈 */
//		      char    szFileYN       [ 1]; /* File 여부 (메시지:0x00, 파일:0x01) */
//		      char    szRetryYN      [ 1]; /* 재전송 여부 (신규전송:0x00, 재전송:0x01) */
//		      char    szEqpMode      [ 4]; /* 장비모드    */
//		      char    szEqpStat      [ 6]; /* 장비상태    */
//		      char    szRFU          [22]; /* RFU         */
//		} SMS_HEADER;
		
//		typedef struct
//		{
//		    char    IO_TEMPER       [ 1]; /* HEX,IO Board 온도   */
//		    char    RF_TEMPER       [ 1]; /* HEX,RF 단말기 온도   */
//		    char    RFU             [18]; /* HEX,RFU    */
//		} SOX017A_DATA;
		
		System.out.println("=============================");
		//G2543254303101017A00000000000:
		//EF09 1F 01 C0000000 20210319085500 7A01 00000000 00 14000000 0100 1400 00 30 32180000 100800000000 00000000000000000000000000000000000000000000 21 2D 000000000000000000000000000000000000
		String hexStr = "EF091F01C0000000202103190855007A010000000000140000000100140000303218000010080000000000000000000000000000000000000000000000000000212D000000000000000000000000000000000000";
		
		byte[] pp = hexStringToByteArray(hexStr);
		System.out.println(pp);
		
		System.out.println("=============================");
		String extrstano = byteArrayToHexString(pp, 0, 2);
		String eqpNo = byteArrayToHexString(pp, 2, 3);
		String eqpClassCd = byteArrayToHexString(pp, 3, 4);
		String tranSeq = byteArrayToHexString(pp, 4, 8);
		String sndDtime = byteArrayToHexString(pp, 8, 15);
		String CmdCode = byteArrayToHexString(pp, 15, 17);
		String szCmdCodeSndSeq = byteArrayToHexString(pp, 17, 21);
		String szCnqeCode = byteArrayToHexString(pp, 21, 22);
		String szMsgSize = byteArrayToHexString(pp, 22, 26);
		String szRecordCnt = byteArrayToHexString(pp, 26, 28);
		String szRecordSize = byteArrayToHexString(pp, 28, 30);
		String szFileYN = byteArrayToHexString(pp, 30, 31);
		String szRetryYN = byteArrayToHexString(pp, 31, 32);
		String szEqpMode = byteArrayToHexString(pp, 32, 36);
		String szEqpStat = byteArrayToHexString(pp, 36, 42);
		String IO_TEMPER = byteArrayToHexString(pp, 64, 65);
		String RF_TEMPER = byteArrayToHexString(pp, 65, 66);
		
		System.out.println("=============================");
		System.out.println("EXTR_STA_NO :: " + Integer.parseInt(hexToBigEndian(extrstano), 16));
		System.out.println("EQP_NO :: " + Integer.parseInt(hexToBigEndian(eqpNo), 16));
		System.out.println("EQP_CLASS_CD :: " + Integer.parseInt(hexToBigEndian(eqpClassCd), 16));
		System.out.println("TRAN_SEQ :: " + Integer.parseInt(hexToBigEndian(tranSeq), 16));
		System.out.println("SND_DTIME :: " + sndDtime);
		System.out.println("CMD_CODE :: " + hexToBigEndian(CmdCode));
		System.out.println("CMD_CODE_SEQ :: " + Integer.parseInt(hexToBigEndian(szCmdCodeSndSeq), 16));
		System.out.println("CNQE_CODE :: " + Integer.parseInt(hexToBigEndian(szCnqeCode), 16));
		System.out.println("MSG_SIZE :: " + Integer.parseInt(hexToBigEndian(szMsgSize), 16));
		System.out.println("RECORD_CNT :: " + Integer.parseInt(hexToBigEndian(szRecordCnt), 16));
		System.out.println("RECORD_SIZE :: " + Integer.parseInt(hexToBigEndian(szRecordSize), 16));
		System.out.println("FILE_YN :: " + Integer.parseInt(hexToBigEndian(szFileYN), 16));
		System.out.println("RETRY_YN :: " + (char)Integer.parseInt(hexToBigEndian(szRetryYN),16));
		System.out.println("EQP_MODE :: " + Integer.parseInt(hexToBigEndian(szEqpMode), 16));
		System.out.println("EQP_STAT :: " + Integer.parseInt(hexToBigEndian(szEqpStat), 16));
		System.out.println("IO_TEMPER :: " + Integer.parseInt(hexToBigEndian(IO_TEMPER), 16));
		System.out.println("RF_TEMPER :: " + Integer.parseInt(hexToBigEndian(RF_TEMPER), 16));
		
	}
	
	public static byte[] hexStringToByteArray(String s) {
	      
		int len = s.length();
		byte[] data = new byte[len / 2];
		for (int i = 0; i < len; i += 2) {
			data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
									+ Character.digit(s.charAt(i+1), 16));
       	}
       	return data;
	}
	   
	public static String byteArrayToHexString(byte[] bytes, int si, int len){
		
		StringBuilder sb = new StringBuilder(); 
  
		for(int i = si; i < len; i++) {
			sb.append(String.format("%02X", bytes[i]&0xff)); 
		} 
  
		return sb.toString(); 
	} 

	
	public static String hexToBigEndian(String a) {
		
		int n = a.length() / 2;
		String[] temp = new String[n];
		
		if(a.length() == 2) {
			return a;
		}
		
		int k = a.length();
		int j = a.length() - 2;
		for(int i = n; i > 0; i--) {
			temp[n-1] = a.substring(j, k);
			j = j - 2;
			k = k - 2;
			n--;
		}
		
		String result = "";
		for(int i = temp.length; i > 0; i--) {
			result += temp[i-1];
		}
		
		return result;
	}
	
	public static int toInt(byte[] hexbyte) {
		
		/* hexbyte에 16진수가 들어가 있음 */

		StringBuffer sb = new StringBuffer(hexbyte.length * 2);
		String hexaDecimal;

		/* Hex byte[] to  Hex String */
		for(int x = 0; x < hexbyte.length; x++)
		{
			hexaDecimal = "0" + Integer.toHexString(0xff & hexbyte[x]);
			sb.append( hexaDecimal.substring(hexaDecimal.length()-2));
		}

		/* Hex String  to   Decimal int */
		int decimal = Integer.parseInt(sb.toString(),16);
		
		return decimal;
	}
}
