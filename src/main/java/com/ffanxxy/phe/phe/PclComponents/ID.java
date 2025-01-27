package com.ffanxxy.phe.phe.PclComponents;

import java.io.Serial;
import java.io.Serializable;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Objects;

public class ID implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private String ID;
    public static ArrayList<Object[]> all = new ArrayList<>();

    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    private static final int LENGTH = 8;
    private static final SecureRandom random = new SecureRandom();

    public ID(Object o) {
        reload(o);
    }

    public void reload(Object o) {
        ID = generateRandomString();
        while (containsInFirstDimension(all,ID)) {
            ID = generateRandomString();
        }
        all.add(new Object[]{ID,o});
    }

    public static String generateRandomString() {
        StringBuilder sb = new StringBuilder(LENGTH);
        for (int i = 0; i < LENGTH; i++) {
            int index = random.nextInt(CHARACTERS.length());
            sb.append(CHARACTERS.charAt(index));
        }
        return sb.toString();
    }

    /**
     * 检查给定的二维对象列表的第一个维度是否包含指定的字符串。
     *
     * @param list   要搜索的二维对象列表
     * @param target 要查找的字符串
     * @return 如果找到目标字符串则返回true，否则返回false。
     */
    private static boolean containsInFirstDimension(ArrayList<Object[]> list, String target) {
        if (list == null || target == null) {
            return false;
        }

        for (Object[] row : list) {
            // 确保行不为空并且至少有一个元素
            if (row != null && row.length > 0) {
                // 检查该元素是否是String类型，并且是否与目标字符串相等
                if (row[0] instanceof String && Objects.equals(target, row[0])) {
                    return true;
                }
            }
        }
        return false;
    }

    public String getID() {
        return ID;
    }

}
