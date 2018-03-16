package com.citywithincity.ecard.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ValidateUtil {
    public final static boolean isNull(String str) {
        return str == null || "".equals(str.trim())
                || "null".equals(str.toLowerCase());
    }

    /**
     * 匹配汉字
     *
     * @param text
     * @return
     * @author jiqinlin
     */
    public final static boolean isChinese(String text) {
        return match(text, "^[\u4e00-\u9fa5]+$");
    }

	/**
	 * 正则表达式匹配
	 *
	 * @param text
	 *            待匹配的文本
	 * @param reg
	 *            正则表达式
	 * @return
	 * @author jiqinlin
	 */
	private final static boolean match(String text, String reg) {
		if (isNull(text) || isNull(reg))
			return false;
		return Pattern.compile(reg).matcher(text).matches();
	}
	/**
	 * 手机号码验证
	 *
	 * @param text
	 * @return
	 * @author jiqinlin
	 */
	public final static boolean isMobile(String text) {
		if (text.length() != 11)
			return false;
		return match(text,
				"^(((13[0-9]{1})|(14[0-9]{1})|(15[0-9]{1})|(17[0-9]{1})|(18[0-9]{1}))+\\d{8})$");
	}

	public static boolean isEmail(String email) {
		if (null == email || "".equals(email))
			return false;
		// Pattern p = Pattern.compile("\\w+@(\\w+.)+[a-z]{2,3}"); //简单匹配
		Pattern p = Pattern.compile("\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*");// 复杂匹配
		Matcher m = p.matcher(email);
		return m.matches();
	}

    public static boolean isPostCode(String postCode) {
        if(isNull(postCode)){
            return false;
        }
        return postCode.trim().length()==6;
    }
}
