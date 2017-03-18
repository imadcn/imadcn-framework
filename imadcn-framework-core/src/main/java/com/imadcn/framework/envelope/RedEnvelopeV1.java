package com.imadcn.framework.envelope;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.TreeSet;

/**
 * 随机红包工具
 * @author yangchao
 * @since 2015年4月17日
 */
public class RedEnvelopeV1 {
	
	/**
	 * 0元
	 */
	private static final BigDecimal ZERO 		= new BigDecimal("0");
	/**
	 * 1元
	 */
	private static final BigDecimal ONE 		= new BigDecimal("1");
	/**
	 * 0.01元
	 */
	private static final BigDecimal ONE_CENT 	= new BigDecimal("0.01");
	
	/**
	 * 100
	 */
	private static final BigDecimal ONE_HUNDRED = new BigDecimal("100");
	
	private static final Random RANDOM 		= new Random(System.currentTimeMillis());
	
	/**
	 * 获取给定的金额最多可生成多少个红包，每个最好一分钱
	 * @param money
	 * @return
	 */
	public static int maxEnvelopeSize(String money) {
		if (!isDouble(money) || new BigDecimal(money).compareTo(ZERO) <= 0) {
			throw new IllegalArgumentException("illegal money or it should >=0");
		}
		BigDecimal decMoney  = new BigDecimal(money);
		return decMoney.divide(ONE_CENT, new MathContext(0)).intValue();
	}
	
	/**
	 * 将一定数额的钱，随机分成N份，每份最少1分
	 * @param money 待分总金额，单位：<b>分</b>
	 * @param number 总份数
	 * @return
	 */
	public static Integer[] calculate(int money, int number) {
		BigDecimal decYuanMoney = new BigDecimal(money).divide(ONE_HUNDRED);
		BigDecimal[] envelopes = calculate(decYuanMoney.toString(), number);
		if (envelopes != null) {
			List<Integer> envelopeList = new ArrayList<Integer>();
			for (BigDecimal yuan : envelopes) {
				envelopeList.add(yuan.multiply(ONE_HUNDRED).intValue());
			}
			return envelopeList.toArray(new Integer[envelopeList.size()]);
		}
		return null;
	}
	
	/**
	 * 将一定数额的钱，随机分成N份，每份最少0.01元
	 * @param money 待分总金额，单位：<b>元</b>
	 * @param number 总份数
	 * @return
	 */
	public static BigDecimal[] calculate(double money, int number) {
		return calculate(String.valueOf(money), String.valueOf(number));
	}
	
	/**
	 * 将一定数额的钱，随机分成N份，每份最少0.01元
	 * @param money 待分总金额，单位：<b>元</b>
	 * @param number 总份数
	 * @return
	 */
	public static BigDecimal[] calculate(String money, int number) {
		return calculate(money, String.valueOf(number));
	}
	
	/**
	 * 将一定数额的钱，随机分成N份，每份最少0.01元
	 * @param money 待分总金额
	 * @param number 总份数
	 * @return
	 */
	public static BigDecimal[] calculate(final String money, final String number) {
		if (!isDouble(money) || new BigDecimal(money).compareTo(ZERO) <= 0) {
			throw new IllegalArgumentException("illegal money or it should >=0");
		}
		if (!isInteger(number) || new BigDecimal(number).compareTo(ZERO) <= 0) {
			throw new IllegalArgumentException("illegal number or it should >=0");
		}
		BigDecimal decMoney  = new BigDecimal(money);
		BigDecimal decNumber = new BigDecimal(number);
		if (decMoney.compareTo(decNumber.multiply(ONE_CENT)) < 0) { // 校验总金额够不够，每份最少一分钱
			// throw new IllegalArgumentException("the money is not enough to seperate to " + number + " parts");
			int finalParts = decMoney.divide(ONE_CENT, 0, RoundingMode.HALF_UP).intValue(); // 金额不够，则按每份0.01元生成
			decNumber = new BigDecimal(finalParts);
		}
		/**
		 * 思路：生成N个(0，1]之间的随机数，然后将其求和被Y元除得到一个比例C，用C乘以所有数，这样就得到了最终结果
		 */
		BigDecimal[] res = new BigDecimal[decNumber.intValue()];
		BigDecimal[] weight = getWeight(decMoney, decNumber); // 获取随机每份权重
		BigDecimal ratio = getRatio(weight); // 红包系数
		BigDecimal ratioAverage = decMoney.divide(ratio, 20, RoundingMode.HALF_UP); // 系数获取每份平均大小
		BigDecimal sum = new BigDecimal("0");
		BigDecimal maxOne = getPartMax(decMoney, decNumber); // 由于每份最少一份，所以要计算理论上最大的一份多少钱
		for (int i = 0; i < decNumber.intValue(); i++) {
			if (i == (decNumber.intValue()-1)) { // 最后一份直接：总和-已分
				res[i] = decMoney.subtract(sum);
				if (res[i].compareTo(ZERO) <= 0) { // 最后一个没钱
					res[i] = balanceIt(res[i], res); // 金额修正
				}
			} else {
				res[i] = ratioAverage.multiply(weight[i]).divide(ONE, 2, RoundingMode.HALF_UP); // 每份实际金额
				if (res[i].compareTo(ONE_CENT) < 0 || res[i].compareTo(maxOne) >= 0) { // 计算后的金额 <=0 或者 >=单份最大，则默认为0.01元
					res[i] = ONE_CENT;
				}
				sum = sum.add(res[i]);
			}
		}
		return validate(decMoney, decNumber, res) ? res : calculate(money, number); // 金额校验
	}
	
	/**
	 * 排序，从小到大
	 * @param numbers
	 * @return
	 */
	public static BigDecimal[] sort(BigDecimal[] numbers) {
		if (isNull((Object[])numbers)) {
			throw new IllegalArgumentException("illegal numbers[mustn't be null or empty]");
		}
		List<BigDecimal> toBeSorted = new ArrayList<BigDecimal>(Arrays.asList(numbers));
		Collections.sort(toBeSorted);
		return toBeSorted.toArray(new BigDecimal[toBeSorted.size()]);
	}
	
	/**
	 * 金额修正，由于计算金额的时候，有精度丢失，最后一个可能出现负数，因此要做金额修正
	 * <br>随机把多余0.01元的红包取出来去填补"负数"红包
	 * @param last
	 * @param res
	 * @return
	 */
	private static BigDecimal balanceIt(BigDecimal last, BigDecimal[] res) {
		Set<Integer> set = new TreeSet<Integer>();
		for (int i = 0; i < res.length; i++) {
			if (res[i].compareTo(ONE_CENT) > 0) {
				set.add(i);
			}
		}
		while(last.compareTo(ONE_CENT) < 0) {
			Integer[] index = set.toArray(new Integer[set.size()]);
			int randomI = index[RANDOM.nextInt(index.length)];
			if (res[randomI].add(last).compareTo(ONE_CENT) > 0) {
				res[randomI] = res[randomI].add(last).subtract(ONE_CENT);
				last = ONE_CENT;
			} else {
				set.remove(randomI);
				BigDecimal gap = res[randomI].subtract(ONE_CENT);
				res[randomI] = ONE_CENT;
				last = last.add(gap);
			}
		}
		return last;
	}
	
	/**
	 * 金额校验
	 * @return
	 */
	private static boolean validate(final BigDecimal money, final BigDecimal number, final BigDecimal[] res) {
		BigDecimal sum = new BigDecimal("0");
		for (BigDecimal digit : res) {
			if (digit.compareTo(ZERO) <= 0) {
				return false; // 每个至少一分钱
			}
			sum = sum.add(digit);
		}
		return money.compareTo(sum) == 0; // 总和正确
	} 
	
	/**
	 * 计算单份最大金额
	 * @param money
	 * @param number
	 * @return
	 */
	private static BigDecimal getPartMax(BigDecimal money, BigDecimal number) {
		BigDecimal leastNeeded = ONE_CENT.multiply(number);
		BigDecimal remain = money.subtract(leastNeeded);
		return remain.compareTo(ZERO) > 0 ? remain.add(ONE_CENT) : ONE_CENT;
	}
	
	/**
	 * 计算随机平均系数
	 * @param weight
	 * @return
	 */
	private static BigDecimal getRatio(BigDecimal[] weight) {
		BigDecimal ratio = new BigDecimal("0");
		for (BigDecimal w : weight) {
			ratio = ratio.add(w);
		}
		return ratio.compareTo(ZERO) <= 0 ? getRatio(weight) : ratio;
	}
	
	/**
	 * 获取每份权重
	 * @param money
	 * @param number
	 * @return
	 */
	private static BigDecimal[] getWeight(BigDecimal money, BigDecimal number) {
		BigDecimal[] weight = new BigDecimal[number.intValue()];
		for (int i = 0; i < number.intValue(); i++) {
			weight[i] = new BigDecimal(Math.random());
		}
		return weight;
	}

	/**
	 * 判断是否是double
	 * @param value
	 * @return
	 */
	private static boolean isDouble(String value) {
		try {
			Double.parseDouble(value);
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	
	/**
	 * 判断是否是Integer
	 * @param value
	 * @return
	 */
	private static boolean isInteger(String value) {
		try {
			Integer.parseInt(value);
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	
	/**
	 * 判断是否是null
	 * @param values
	 * @return
	 */
	private static boolean isNull(Object... values) {
		if (values != null && values.length > 0) {
			for (Object v : values) {
				if (v == null) {
					return true;
				}
			}
			return false;
		}
		return true;
	}
	
	public static void main(String[] args) {
		BigDecimal[] rsp = RedEnvelopeV1.calculate("83.13", "11");
		// BigDecimal[] sorted = sort(rsp);
		for (BigDecimal r : rsp) {
			System.err.println(r);
		}
	}
}