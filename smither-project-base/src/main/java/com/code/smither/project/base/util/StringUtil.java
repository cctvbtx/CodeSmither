package com.code.smither.project.base.util;

@SuppressWarnings("unused")
public class StringUtil {
	
	/**
	 * 大写第一个字母
	 */
	public static String upperFirst(String s) {
		if (s == null || s.length() == 0) {
			return s;
		}
		StringBuffer sb = new StringBuffer();
		sb.append(s.substring(0, 1).toUpperCase());
		sb.append(s.substring(1, s.length()));
		return sb.toString();
	}

	/**
	 * 小写第一个字母
	 */
	public static String lowerFirst(String s) {
		if (s == null || s.length() == 0) {
			return s;
		}
		StringBuffer sb = new StringBuffer();
		sb.append(s.substring(0, 1).toLowerCase());
		sb.append(s.substring(1, s.length()));
		return sb.toString();
	}


	public static String camel(String orgin, String division) {
		if (division != null && division.length() > 0) {
			String[] divs = orgin.split(division);
			orgin = "";
			for (String div : divs) {
				orgin += upperFirst(div);
			}
		}
		return orgin;
	}

}
