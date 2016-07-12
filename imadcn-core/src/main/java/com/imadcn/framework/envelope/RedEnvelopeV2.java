package com.imadcn.framework.envelope;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Random;


/**
 * 随机红包算法
 * @author yc
 * @since 2015年12月30日
 */
public class RedEnvelopeV2 {
	
	public static void main(String[] args) {
		int total = 1 * 10 * 10;
		int apart = 10;
		int envelope = 0;
		int claimedAmount = 0;
		for (int i = 0; i < apart; i++) {
			envelope = giveMeOne(total, claimedAmount, apart, i);
			System.out.println(new BigDecimal(envelope).divide(new BigDecimal(100), 2, RoundingMode.HALF_DOWN));
			claimedAmount += envelope;
		}
		System.out.println("total claimed:" + new BigDecimal(claimedAmount).divide(new BigDecimal(100), 2, RoundingMode.HALF_DOWN));
	}
	
	/**
	 * 产生一个随机红包
	 * @param envelopeAmount
	 * @param claimedAmount
	 * @param envelopeNum
	 * @param claimedNum
	 * @return
	 */
	public static int giveMeOne(int envelopeAmount, int claimedAmount, int envelopeNum, int claimedNum) {
		if (envelopeAmount <=0 || envelopeNum <= 0) {
			throw new IllegalArgumentException("envelopeAmount/envelopeNum must > 0, actually: " + envelopeAmount + "/" + envelopeNum);
		}
		if (envelopeAmount < envelopeNum) {
			throw new IllegalArgumentException("no enough money(envelopeAmount>=envelopeNum), actually: " + envelopeAmount + "/" + envelopeNum);
		}
		if ((envelopeAmount-claimedAmount) < (envelopeNum-claimedNum)) {
			throw new IllegalArgumentException("no enough money(remain), actually: " + envelopeAmount + "/" + envelopeNum);
		}
		Random random = new Random(System.currentTimeMillis());
		int remain = envelopeAmount - claimedAmount;
		int number = envelopeNum;
		int leastNeed = number - claimedNum;
		int each = random.nextInt(remain / (number - claimedNum) * 2) + 1;
		if ((remain - each) < leastNeed) {
			each = each - leastNeed;
			if (each <= 0) {
				each = 1;
			}
		}
		if (claimedNum == number - 1) {
			each = remain;
		}
		remain -= each;
		return each;
	}
	
	/*protected void precious() {
		Random random = new Random(System.currentTimeMillis());
		for (int i = 0; i < 10; i++) {
			int total  = 10 * (i + 1) * 100;
			int remain = total;
			int number = 10;
			int check = 0;
			for (int j = 0; j < number; j++) {
				try {
					int leastNeed = number - j;
					int each = random.nextInt(remain / (number - j) * 2) + 1;
					if ((remain - each) < leastNeed) {
						each = each - leastNeed;
						if (each <= 0) {
							each = 1;
						}
					}
					if (j == number - 1) {
						each = remain;
					}
					remain -= each;
					check += each;
					if (each <= 0) {
						throw new RuntimeException("each is less than zero:" + each);
					}
					System.out.println(new BigDecimal(each).divide(new BigDecimal(100), 2, RoundingMode.HALF_DOWN));
				} catch (Exception e) {
					e.printStackTrace();
					System.exit(0);
				}
			}
			if (check != total) {
				System.out.println("not equal  check:total" + check + ":" + total);
				break;
			} else {
				
				BigDecimal dectotal = new BigDecimal(total).divide(new BigDecimal(100), 2, RoundingMode.HALF_DOWN);
				BigDecimal deccheck = new BigDecimal(check).divide(new BigDecimal(100), 2, RoundingMode.HALF_DOWN);
				System.out.println("-----------[" + dectotal + "]元分[" + number + "]个，总分得[" + deccheck + "]元");
			}
		}
	}*/
}
