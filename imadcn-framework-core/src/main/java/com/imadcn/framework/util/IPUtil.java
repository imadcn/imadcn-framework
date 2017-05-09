package com.imadcn.framework.util;

import java.net.InetAddress;

/**
 * IP转换工具
 * @author iamdcn
 * @since 2017年5月3日
 */
public class IPUtil {

	private final static int INADDRSZ = 4;

	/**
     * 把IP地址转化为字节数组
     * @param ipAddr
     * @return byte[]
     */
    public static byte[] ipToBytesByInet(String ipAddr) {
        try {
            return InetAddress.getByName(ipAddr).getAddress();
        } catch (Exception e) {
            throw new IllegalArgumentException(ipAddr + " is invalid IP");
        }
    }

	/**
	 * 把IP地址转化为int
	 * 
	 * @param ipAddr
	 * @return int
	 */
	public static byte[] ipToBytesByReg(String ipAddr) {
		byte[] ret = new byte[4];
		try {
			String[] ipArr = ipAddr.split("\\.");
			ret[0] = (byte) (Integer.parseInt(ipArr[0]) & 0xFF);
			ret[1] = (byte) (Integer.parseInt(ipArr[1]) & 0xFF);
			ret[2] = (byte) (Integer.parseInt(ipArr[2]) & 0xFF);
			ret[3] = (byte) (Integer.parseInt(ipArr[3]) & 0xFF);
			return ret;
		} catch (Exception e) {
			throw new IllegalArgumentException(ipAddr + " is invalid IP");
		}

	}

	/**
	 * 字节数组转化为IP
	 * 
	 * @param bytes
	 * @return int
	 */
	public static String bytesToIp(byte[] bytes) {
		return new StringBuffer().append(bytes[0] & 0xFF).append('.').append(bytes[1] & 0xFF).append('.').append(bytes[2] & 0xFF).append('.').append(bytes[3] & 0xFF).toString();
	}

	/**
	 * 根据位运算把 byte[] -> int
	 * 
	 * @param bytes
	 * @return int
	 */
	public static int bytesToInt(byte[] bytes) {
		int addr = bytes[3] & 0xFF;
		addr |= ((bytes[2] << 8) & 0xFF00);
		addr |= ((bytes[1] << 16) & 0xFF0000);
		addr |= ((bytes[0] << 24) & 0xFF000000);
		return addr;
	}

	/**
	 * 把IP地址转化为int
	 * 
	 * @param ipAddr
	 * @return int
	 */
	public static int ipToInt(String ipAddr) {
		try {
			return bytesToInt(ipToBytesByInet(ipAddr));
		} catch (Exception e) {
			throw new IllegalArgumentException(ipAddr + " is invalid IP");
		}
	}

	/**
	 * ipInt -> byte[]
	 * 
	 * @param ipInt
	 * @return byte[]
	 */
	public static byte[] intToBytes(int ipInt) {
		byte[] ipAddr = new byte[INADDRSZ];
		ipAddr[0] = (byte) ((ipInt >>> 24) & 0xFF);
		ipAddr[1] = (byte) ((ipInt >>> 16) & 0xFF);
		ipAddr[2] = (byte) ((ipInt >>> 8) & 0xFF);
		ipAddr[3] = (byte) (ipInt & 0xFF);
		return ipAddr;
	}

	/**
	 * 把int->ip地址
	 * 
	 * @param ipInt
	 * @return String
	 */
	public static String intToIp(int ipInt) {
		return new StringBuilder().append(((ipInt >> 24) & 0xff)).append('.').append((ipInt >> 16) & 0xff).append('.').append((ipInt >> 8) & 0xff).append('.').append((ipInt & 0xff)).toString();
	}

	/**
	 * 把192.168.1.1/24 转化为int数组范围
	 * 
	 * @param ipAndMask
	 * @return int[]
	 */
	public static int[] getIPIntScope(String ipAndMask) {

		String[] ipArr = ipAndMask.split("/");
		if (ipArr.length != 2) {
			throw new IllegalArgumentException("invalid ipAndMask with: " + ipAndMask);
		}
		int netMask = Integer.valueOf(ipArr[1].trim());
		if (netMask < 0 || netMask > 31) {
			throw new IllegalArgumentException("invalid ipAndMask with: " + ipAndMask);
		}
		int ipInt = ipToInt(ipArr[0]);
		int netIP = ipInt & (0xFFFFFFFF << (32 - netMask));
		int hostScope = (0xFFFFFFFF >>> netMask);
		return new int[] { netIP, netIP + hostScope };

	}

	/**
	 * 把192.168.1.1/24 转化为IP数组范围
	 * 
	 * @param ipAndMask
	 * @return String[]
	 */
	public static String[] getIPAddrScope(String ipAndMask) {
		int[] ipIntArr = getIPIntScope(ipAndMask);
		return new String[] { intToIp(ipIntArr[0]), intToIp(ipIntArr[0]) };
	}

	/**
	 * 根据IP 子网掩码（192.168.1.1 255.255.255.0）转化为IP段
	 * 
	 * @param ipAddr
	 * @param mask
	 * @return int[]
	 */
	public static int[] getIPIntScope(String ipAddr, String mask) {

		int ipInt;
		int netMaskInt = 0, ipcount = 0;
		try {
			ipInt = ipToInt(ipAddr);
			if (null == mask || "".equals(mask)) {
				return new int[] { ipInt, ipInt };
			}
			netMaskInt = ipToInt(mask);
			ipcount = ipToInt("255.255.255.255") - netMaskInt;
			int netIP = ipInt & netMaskInt;
			int hostScope = netIP + ipcount;
			return new int[] { netIP, hostScope };
		} catch (Exception e) {
			throw new IllegalArgumentException("invalid ip scope express  ip:" + ipAddr + "  mask:" + mask);
		}

	}

	/**
	 * 根据IP 子网掩码（192.168.1.1 255.255.255.0）转化为IP段
	 * @param ipAddr
	 * @param mask
	 * @return String[]
	 */
	public static String[] getIPStrScope(String ipAddr, String mask) {
		int[] ipIntArr = getIPIntScope(ipAddr, mask);
		return new String[] { intToIp(ipIntArr[0]), intToIp(ipIntArr[0]) };
	}

	/**
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		/*String ipAddr = "192.168.8.1";

		byte[] bytearr = ipToBytesByInet(ipAddr);

		StringBuffer byteStr = new StringBuffer();

		for (byte b : bytearr) {
			if (byteStr.length() == 0) {
				byteStr.append(b);
			} else {
				byteStr.append("," + b);
			}
		}
		System.out.println("IP: " + ipAddr + " ByInet --> byte[]: [ " + byteStr + " ]");

		bytearr = ipToBytesByReg(ipAddr);
		byteStr = new StringBuffer();

		for (byte b : bytearr) {
			if (byteStr.length() == 0) {
				byteStr.append(b);
			} else {
				byteStr.append("," + b);
			}
		}
		System.out.println("IP: " + ipAddr + " ByReg  --> byte[]: [ " + byteStr + " ]");

		System.out.println("byte[]: " + byteStr + " --> IP: " + bytesToIp(bytearr));

		int ipInt = ipToInt(ipAddr);

		System.out.println("IP: " + ipAddr + "  --> int: " + ipInt);

		System.out.println("int: " + ipInt + " --> IP: " + intToIp(ipInt));

		String ipAndMask = "192.168.1.1/24";

		int[] ipscope = getIPIntScope(ipAndMask);
		System.out.println(ipAndMask + " --> int地址段：[ " + ipscope[0] + "," + ipscope[1] + " ]");

		System.out.println(ipAndMask + " --> IP 地址段：[ " + intToIp(ipscope[0]) + "," + intToIp(ipscope[1]) + " ]");

		String ipAddr1 = "192.168.1.1", ipMask1 = "255.255.255.0";

		int[] ipscope1 = getIPIntScope(ipAddr1, ipMask1);
		System.out.println(ipAddr1 + " , " + ipMask1 + "  --> int地址段 ：[ " + ipscope1[0] + "," + ipscope1[1] + " ]");

		System.out.println(ipAddr1 + " , " + ipMask1 + "  --> IP地址段 ：[ " + intToIp(ipscope1[0]) + "," + intToIp(ipscope1[1]) + " ]");
*/
		System.out.println(IPUtil.ipToInt("128.0.0.1"));
	}
}
