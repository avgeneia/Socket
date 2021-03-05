package com.NettyBoot.Common;

public class CmmFunction {
	
	public boolean addEQ(String a, String b) {
		
		if(a == b) {
			return true;
		} else {
			return false;
		}
	}
	
	public boolean addLR(String a, String b) {
		
		if(Integer.parseInt(a) > Integer.parseInt(b)) {
			return true;
		} else {
			return false;
		}
	}
}
