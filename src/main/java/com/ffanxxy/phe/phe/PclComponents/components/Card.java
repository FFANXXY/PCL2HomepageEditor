package com.ffanxxy.phe.phe.PclComponents.components;

import com.ffanxxy.phe.phe.PclComponents.ID;
import com.ffanxxy.phe.phe.PclComponents.PCLC;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;

public class Card extends PCLC {

    public ArrayList<PCLC> components = new ArrayList<>();
    public boolean hasStackPanle = true;
    private int[] StackPanleMargin;

    public final ID ID;

    public Card(String name, String describe) {
        super(name, describe);
        ID = new ID(this);
        setMargin(new int[]{0, 0, 0, 15});
        this.setStackPanleMargin(new int[]{25,40,23,15});
    }

    public Card resetCardName(String name) {
        this.setName(name);
        return this;
    }


    public Card(String name) {
        super(name);
        ID = new ID(this);
        setMargin(new int[]{0, 0, 0, 15});
        this.setStackPanleMargin(new int[]{25,40,23,15});
    }


    public ArrayList<String> getComponentsType() {
        ArrayList<String> name = new ArrayList<>();
        for(PCLC component : components) {
            name.add(component.getType());
        }
        return name;
    }

    /**
     *  向Card中添加组件
     * @param component 组件
     */
    public Card addComponent(PCLC component) {
        if(!(component instanceof Card) && hasStackPanle) { //检测输入的是否是Card
            components.add(component);
        }else {
            throw new RuntimeException("Card can't stack a card OR it doesn't have a stack panle");
        }
        return this;
    }
/*
<local:MyCard Title="纯文本" Margin="0,0,0,15" CanSwap="True" IsSwaped="True">
    <StackPanel Margin="25,40,23,15">
        <TextBlock TextWrapping="Wrap" Margin="0,0,0,4"
                    Text="..."/>
    </StackPanel>
</local:MyCard>

       选自教学文档
       hasStackPanle 是一个布尔值，用于指示 Card 是否有一个 StackPanel。
 */
    public boolean isHasStackPanle() {
        return hasStackPanle;
    }

    /**
     *  设置StackPanel的边距
     * @param StackPanleMargin 边距
     */
    public void setStackPanleMargin(int[] StackPanleMargin) {
        this.StackPanleMargin = StackPanleMargin;
    }

    public void setStackPanle(boolean hasStackPanle) {
        this.hasStackPanle = hasStackPanle;
    }

    public ArrayList<PCLC> getComponents() {
        return components;
    }

    public ArrayList<String> getComponentsName() {
        ArrayList<String> names = new ArrayList<>();
        for(PCLC component : components) {
            names.add(component.getName());
        }
        return names;
    }

    public PCLC getComponent(PCLC component) {
        return components.get(components.indexOf(component));
    }
    public PCLC getComponent(String name) {
        for(PCLC component : components) {
            if(component.getName().equals(name)) {
                return component;
            } else {
                return null;
            }
        }
        return null;
    }
    public PCLC getComponent(ID id) {
        for(PCLC component : components) {
            if(component.getID().equals(id.getID())) {
                return component;
            } else {
                return null;
            }
        }
        return null;
    }

    public String getType() {
        return "Card";
    }

    public Card setComponet(PCLC component,PCLC To_component) {
        components.set(components.indexOf(component),To_component);
        return this;
    }
    public Card setComponet(String ID,PCLC To_component) {
        for(PCLC c : components) {
            if (c.getID().equals(ID)) {
                components.set(components.indexOf(c),To_component);
            }
        }
        return this;
    }


    public String buildStackPanelMargin() {
        return Arrays.stream(StackPanleMargin)
                .mapToObj(String::valueOf)
                .collect(Collectors.joining(", "));
    }
}
