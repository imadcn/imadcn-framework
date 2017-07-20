package com.imadcn.framework.util;

import java.util.Date;

public class DigitCode {
	
	public static final String KEY = "3132333435363738393031323334353637383930" + "3132333435363738393031323334353637383930" + "3132333435363738393031323334353637383930" + "31323334";
	public static final String CARD_ID = "zzzzzz";
	public static int counter = 0;
	
	public static long string2Long(String value) {
		return Long.valueOf(value, 16);
	}
	
	public static long time() {
/*		return new Date().getTime();*/
/*		String timeStr = "2017-05-11 23:43:30";
		return DateFormatUtil.parse(timeStr, DateFormatUtil.DATE_TIME).getTime() + (counter++);*/
		return DateFormatUtil.parse(new Date(), DateFormatUtil.DATE_TIME).getTime() + (counter++);
	}
	
	public static long totp() {
		String totp = OathTOTP.generateTOTP(KEY, time() + "", "6", "HmacSHA512");
		return Long.valueOf(totp);
	}
	
	public static String encode(long totp) {
		StringBuilder sb = new StringBuilder();
		sb.append("13");
		sb.append(String.format("%010d", calc(totp)));
		sb.append(String.format("%06d", totp));
		return sb.toString();
	}
	
	public static String decode(String value) {
		// String prefix = value.substring(0, 2);
		String calc = value.substring(2, 12);
		String totp = value.substring(12);
		String subTotp = totp + totp.substring(0, 2);
		long v = Long.valueOf(calc) - Long.valueOf(subTotp);
		String code = String.valueOf(v);
		if (Long.valueOf(totp) % 2 == 0) {
			code = new StringBuilder(v + "").reverse().toString();
		}
		return String.format("%010d", Long.valueOf(code));
	}
	
	public static long calc(long totp) {
		long cardId = Long.parseLong(CARD_ID, 36);
		if (totp% 2 == 0) {
			cardId = Long.parseLong(new StringBuilder(cardId+"").reverse().toString());
		}
		return cardId + Long.valueOf(String.format("%06d", totp) + String.format("%06d", totp).substring(0, 2));
	}
	
	public static void main(String[] args) throws Exception{
		long sleep = 10;
		int counter = 0;
		for (int i = 0; i < 10; i++) {
			long totp = totp();
			System.out.println(String.format("%06d", totp));
			Thread.sleep(sleep);
			String encode = encode(totp);
			System.err.println("encode: " + encode.substring(0, 4) + " " + encode.substring(4, 8) + " " + encode.substring(8, 12) + " " + encode.substring(12));
			Thread.sleep(sleep);
			String decode = decode(encode);
			System.out.println("decode: " + decode);
			if (!decode.equals(String.format("%010d", Long.parseLong(CARD_ID, 36)))) {
				counter++;
			}
			System.out.println("-----------------------------------");
			Thread.sleep(sleep);
		}
		System.err.println("\n\n\n\n\n-----------------------------------------" + counter);
/*		String string = UIDUtil.uuid6();
		//String string = "zzzzzzz";
		System.out.println("normal: " + Long.parseLong(string, 36));
		System.out.println("upper : " + Long.parseLong(string.toUpperCase(), 36));
		System.out.println("lower : " + Long.parseLong(string.toLowerCase(), 36));
		System.out.println(Long.toString(7999999999L, 36));*/
	}

}
