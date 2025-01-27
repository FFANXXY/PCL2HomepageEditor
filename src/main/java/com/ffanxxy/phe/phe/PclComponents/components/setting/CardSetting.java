package com.ffanxxy.phe.phe.PclComponents.components.setting;

import com.ffanxxy.phe.phe.PclComponents.components.Card;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Stack;
import java.util.stream.Collectors;

public class CardSetting extends Dialog<String> {

    private String initialName;
    private ListView<String> listView;

    private String Margin;
    private String StackMargin;

    public CardSetting(Card card) {
        this.initialName = card.getName();

        setTitle("设置属性");
        setHeaderText("输入卡片属性:");

        // 设置对话框的结果转换器，以便我们可以返回 String 类型的结果
        setResultConverter(dialogButton -> {
            if (dialogButton == ButtonType.OK) {
                TextField nameField = (TextField) getDialogPane().lookup("#nameField");

                Margin = ((TextField) getDialogPane().lookup("#MarginField")).getText();
                StackMargin = ((TextField) getDialogPane().lookup("#StackMarginField")).getText();

                return nameField.getText();
            }
            return null;
        });

        // 创建对话框的内容
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        // 名称输入框
        TextField nameField = new TextField(initialName);
        nameField.setId("nameField"); // 为查找设置 ID

        // ListView 用于选择项目
        listView = new ListView<>();
        listView.getItems().addAll(card.getComponentsName()); // 添加初始项目列表

        TextField MarginField = new TextField();
        MarginField.setId("MarginField"); // 为查找设置 ID
        MarginField.setText(card.buildMargins());

        TextField StackMarginField = new TextField();
        StackMarginField.setId("StackMarginField"); // 为查找设置 ID
        StackMarginField.setText(card.buildStackPanelMargin());

        // 将组件添加到网格布局中
        grid.add(new Label("名称:"), 0, 0);
        grid.add(nameField, 1, 0);

        grid.add(new Label("选择项目:"), 0, 1);
        grid.add(listView, 1, 1);

        grid.add(new Label("间距:"), 2, 0);
        grid.add(MarginField, 3, 0);

        grid.add(new Label("堆叠栏间距:"), 2, 1);
        grid.add(StackMarginField, 3, 1);


        grid.add(new Label("ID:" + card.getID()), 1, 3);

        // 设置对话框内容
        getDialogPane().setContent(grid);
        getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
    }

    // 获取 ListView 的当前选中项
    public String getSelectedItem() {
        return listView.getSelectionModel().getSelectedItem();
    }

    public int[] getMargin() {
        if(Margin != null) {
            int[] margin = new int[4];
            List<String> strList = Arrays.stream(Margin.split(",")).toList();
            for (int i = 0; i < 4; i++) {
                margin[i] = Integer.parseInt(strList.get(i).replaceAll(" ",""));
            }
            return margin;
        } else {
            return null;
        }
    }
    public int[] getStackMargin() {
        if(StackMargin != null) {
            int[] margin = new int[4];
            List<String> strList = Arrays.stream(StackMargin.split(",")).toList();
            for (int i = 0; i < 4; i++) {
                margin[i] = Integer.parseInt(strList.get(i).replaceAll(" ",""));
            }
            return margin;
        } else {
            return null;
        }
    }
}
