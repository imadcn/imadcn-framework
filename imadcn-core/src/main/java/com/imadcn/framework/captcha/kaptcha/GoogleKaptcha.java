package com.imadcn.framework.captcha.kaptcha;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Properties;
import java.util.Random;

import javax.imageio.ImageIO;

import com.google.code.kaptcha.Constants;
import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.google.code.kaptcha.util.Config;
import com.imadcn.framework.captcha.ICaptcha;

public class GoogleKaptcha implements ICaptcha {
	
	private DefaultKaptcha defaultKaptcha;
	
	// simplified chinese [E4B880-E9BEA0]
	private final int CHINESE_START  = 0x4E00;
	private final int CHINESE_END    = 0x9FA5;
	private final int CHINESE_BUFFER = 0xFFFFFF;
	public static enum CaptchaType {CHINESE, LATIN};
	
	private final char[] CHARATERS = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 
									  'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z',
									  'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'};
	
	public BufferedImage getCaptcha(String text) {
		BufferedImage img = getDefaultKaptcha().createImage(text);
		return img;
	}
	
	public void save(int size, CaptchaType type) {
		try {
			String text = "";
			switch (type) {
			case CHINESE:
				text = randomChinese(size);
				break;
			case LATIN:
				text = randomLatin(size);
				break;
			}
			BufferedImage img = getCaptcha(text);
			String filePath = "f:\\captcha_" + System.currentTimeMillis() + ".jpg";
			File file = new File(filePath);
			ImageIO.write(img, "jpg", file);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 
	 * @return
	 */
	
	private DefaultKaptcha getDefaultKaptcha() {
		if (defaultKaptcha == null) {
			Config config = new Config(getDefaultProperties());
			defaultKaptcha = new DefaultKaptcha();
			defaultKaptcha.setConfig(config);
		}
		return defaultKaptcha;
	}
	
	public String randomLatin(int size) {
		if (size <= 0) {
			throw new IllegalArgumentException("size must be positive");
		}
		char[] chars = new char[size];
		for (int i = 0; i < chars.length; i++) {
			int random = randomInt(0, CHARATERS.length);
			chars[i] = CHARATERS[random];
		}
		String charaters = new String(chars);
		return charaters;
	}
	
	public String randomChinese(int size) {
		if (size <= 0) {
			throw new IllegalArgumentException("size must be positive");
		}
		char[] chars = new char[size];
		for (int i = 0; i < chars.length; i++) {
			int random = randomInt(CHINESE_START, CHINESE_END);
			chars[i] = (char) (random & CHINESE_BUFFER);
		}
		String charaters = new String(chars);
		return charaters;
	}
	
	public int randomInt(int min, int max) {
		if (min >= max) {
			return min;
		} else {
			return min + new Random().nextInt(max - min);
		}
	}
	
	public static void main(String[] args) {
		GoogleKaptcha kaptcha = new GoogleKaptcha();
		kaptcha.save(8, CaptchaType.LATIN);
	}
	
	private Properties getDefaultProperties() {
		Properties properties = new Properties();
		properties.put(Constants.KAPTCHA_BORDER, "no");
		properties.put(Constants.KAPTCHA_TEXTPRODUCER_FONT_COLOR, "red");
		properties.put(Constants.KAPTCHA_IMAGE_WIDTH, "400");
		properties.put(Constants.KAPTCHA_IMAGE_HEIGHT, "80");
		properties.put(Constants.KAPTCHA_TEXTPRODUCER_FONT_SIZE, "32");
		properties.put(Constants.KAPTCHA_NOISE_COLOR, "black");
		properties.put(Constants.KAPTCHA_TEXTPRODUCER_FONT_NAMES, "微软雅黑");
		return properties;
	}

}
