package com.ffanxxy.phe.phe.PclComponents;

import java.io.Serial;
import java.io.Serializable;
import java.util.Arrays;
import java.util.stream.Collectors;

public class PCLC implements Serializable{

    @Serial
    private static final long serialVersionUID = 1L;

    private int[] Margin;
    private String name;
    private String describe;
    private ID id;

    public PCLC(String name) {
        Margin = new int[4];
        this.name = name;
        id = new ID(this);
    }
    public PCLC(String name,String describe) {
        Margin = new int[4];
        this.name = name;
        this.describe = describe;
        id = new ID(this);
    }

    public PCLC setName(String name) {
        this.name = name;
        return this;
    }
    public void setDescribe(String describe) {
        this.describe = describe;
    }


    public void setMargin(int[] Margin) {
        this.Margin = Margin;
    }
    public int[] getMargin() {
        return Margin;
    }
    public int getMargin(int index) {
        return Margin[index];
    }

    /**
     * 设置边距
     *
     * @param direction 边距方向 {@code 0:左 1:上 2:右 3:下}
     * @param Margin 边距大小
     */
    public void setMargin(int direction,int Margin) {
        this.Margin[direction] = Margin;
    }

    public String buildMargins() {
        // 使用流API将整数数组转换为字符串并连接起来
        return Arrays.stream(Margin)
                .mapToObj(String::valueOf)
                .collect(Collectors.joining(", "));
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return this.getClass().getSimpleName();
    }

    public String getID() {
        if(id == null) {
            return "NEW";
        } else {
            return id.getID();
        }
    }

    @Override
    public String toString() {
        return this.name;
    }
}


