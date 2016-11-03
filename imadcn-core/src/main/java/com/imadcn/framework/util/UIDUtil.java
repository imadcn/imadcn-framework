package com.imadcn.framework.util;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/**
 * 获取唯一ID By timestamp
 * @author yang c
 * @since 2014-10-14
 * @version 1.0
 */
public class UIDUtil {
	private UIDUtil(){};
	
	private static String[] chars = new String[] { "a", "b", "c", "d", "e", "f",  
        "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s",  
        "t", "u", "v", "w", "x", "y", "z", "0", "1", "2", "3", "4", "5",  
        "6", "7", "8", "9", "A", "B", "C", "D", "E", "F", "G", "H", "I",  
        "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V",  
        "W", "X", "Y", "Z", "_", "-"};  

	/**
	 * 6个字节的短UID
	 * @return
	 */
	public static String uuid6() {  
		 return shortUid(6);
	} 

	/**
	 * 8个字节的短UID
	 * @return
	 */
	public static String uuid8() {  
		 return shortUid(8);
	} 
	
	/**
	 * 12个字节的短UID
	 * @return
	 */
	public static String uuid12Upper() {
		return (shortUid(8) + shortUid(4)).toUpperCase();
	}
	
	private static String shortUid(int size) {
		StringBuffer shortBuffer = new StringBuffer();  
		String uuid = UUID.randomUUID().toString().replace("-", "");  
		for (int i = 0; i < size; i++) {  
		    String str = uuid.substring(i * 4, i * 4 + 4);  
		    int x = Integer.parseInt(str, 16);  
		    shortBuffer.append(chars[x % 0x3E]);  
		}
		return shortBuffer.toString(); 
	}
	
	public static String[] uuid12Upper(int number) {
		if (number <= 0) {
			throw new IllegalArgumentException("the number must be more than 0");
		}
		Set<String> set = new HashSet<String>();
		while (set.size() < number) {
			String id = uuid12Upper();
			set.add(id);
		}
		return set.toArray(new String[set.size()]);
	}
	
	/**
	 * 标准UUID
	 * @return
	 */
	public static String uuid() {
		return UUID.randomUUID().toString();  
	}
	
	/**
	 * 不带"-"符号的UUID
	 * @return
	 */
	public static String noneDashUuid() {
		return UUID.randomUUID().toString().replace("-", "");
	}
	
	/**
	 * 获取一个用于msgsrv连接的ConnId,格式 xxxxxxxx.xxxxxxxxxx
	 * @return
	 */
	public static String connId() {
		String uuid = noneDashUuid().toLowerCase();
		int middle = uuid.length() / 2;
		return uuid.substring(0, middle) + "." + uuid.substring(middle);
	}
	
	/**
	 * 64位[0-9a-zA-Z][_-]UUID
	 * @return
	 */
	public static String uuid64Bit() {  
        StringBuffer r = new StringBuffer();  
        String uuid = UUID.randomUUID().toString().replace("-", "");  
        int index = 0;  
        int[] buff = new int[3];  
        int l = uuid.length();  
        for (int i = 0; i < l; i++) {  
            index = i % 3;  
            buff[index] = Integer.parseInt(""+uuid.charAt(i),16);  
            if (index == 2) {  
                r.append(chars[buff[0] << 2 | buff[1] >>> 2]);  
                r.append(chars[(buff[1]&3) << 4 | buff[2]]);  
            }  
        }  
        return r.toString();  
    }
	
	public static void main(String[] args) {
		String[] uuid = uuid12Upper(1000);
		for (int i = 0; i < uuid.length; i++) {
			System.out.println("[" + (i+1) + "] " + uuid[i]);
		}
		
	}
}