package com.ffanxxy.phe.phe.Applicant.ConstantTable;

public class Illegal {
    public static String regex = "[ *%$/?&|<>#{}]";

    /**
     * 检测字符是否违法
     *
     * @param str 字符
     * @return false为合法
     */
    public static boolean isStringIlleagal(String str) {
        if(str == null) return false;
        return str.matches(".*" + regex + ".*");
    }
}
