package com.imadcn.framework.util;

public class DigitCode {
	
	public static final String KEY = "3132333435363738393031323334353637383930";
	public static final long CARD_ID = 3636657574L;
	public static int counter = 0;
	
	public static long string2Long(String value) {
		return Long.valueOf(value, 16);
	}
	
	public static long time() {
/*		return new Date().getTime();*/
		String timeStr = "2017-05-11 23:43:30";
		return DateFormatUtil.parse(timeStr, DateFormatUtil.DATE_TIME).getTime() + (counter++);
	}
	
	public static long totp() {
		String totp = OathTOTP.generateTOTP(KEY, time() + "", "6", "HmacSHA1");
		return Long.valueOf(totp);
	}
	
	public static String encode(long totp) {
		StringBuilder sb = new StringBuilder();
		sb.append("18");
		sb.append(calc(totp));
		sb.append(totp);
		return sb.toString();
	}
	
	public static String decode(String value) {
		// String prefix = value.substring(0, 2);
		String calc = value.substring(2, 12);
		String totp = value.substring(12);
		String subTotp = totp + totp.substring(0, 2);
		long v = Long.valueOf(calc) - Long.valueOf(subTotp);
		if (Long.valueOf(totp) % 2 == 0) {
			return new StringBuilder(v + "").reverse().toString();
		}
		return v + "";
	}
	
	public static long calc(long totp) {
		long cardId = CARD_ID;
		if (totp% 2 == 0) {
			cardId = Long.valueOf(new StringBuilder(CARD_ID + "").reverse().toString());
		}
		return cardId + Long.valueOf(totp + String.valueOf(totp).substring(0, 2));
	}
	
	
	
	public static void main(String[] args) throws Exception{
		for (int i = 0; i < 5; i++) {
			long totp = totp();
			System.out.println(totp);
			Thread.sleep(100);
			String code = encode(totp);
			System.err.println("encode: " + code.substring(0, 4) + " " + code.substring(4, 8) + " " + code.substring(8, 12) + " " + code.substring(12));
			Thread.sleep(100);
			System.out.println("decode: " + decode(code));
			System.out.println("-----------------------------------");
			Thread.sleep(100);
		}
	}

}
