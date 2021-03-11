package com.NettyBoot.Handler;

import java.sql.SQLSyntaxErrorException;

public class MessageHandler {

	/** Logger */
	//private final static Logger logger = LogManager.getLogger(BusinessThread.class);

	public static int ErrHandler(Exception e) {
		
		int result = 0;
		
		if(e.getCause() instanceof SQLSyntaxErrorException) {
			
			SQLSyntaxErrorException se = (SQLSyntaxErrorException) e.getCause();
			
			result = se.getErrorCode();
			
		}
		
		return result;
	}
}
