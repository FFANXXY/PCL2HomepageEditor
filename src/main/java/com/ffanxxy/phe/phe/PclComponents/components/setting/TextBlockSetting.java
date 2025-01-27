package com.ffanxxy.phe.phe.PclComponents.components.setting;

import com.ffanxxy.phe.phe.Applicant.ConstantTable.Illegal;
import com.ffanxxy.phe.phe.PclComponents.components.TextBlock;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;

import java.util.ArrayList;

public class TextBlockSetting extends Dialog<ArrayList<String>> {
    private TextBlock textBlock;

    public TextBlockSetting(TextBlock target) {
        this.textBlock = target;

        setTitle("设置属性");
        setHeaderText("输入Textblock属性:");

        // 设置对话框的结果转换器，以便我们可以返回 String 类型的结果和 CheckBox 的状态
        setResultConverter(dialogButton -> {
            if (dialogButton == ButtonType.OK) {
                TextField nameText = (TextField) getDialogPane().lookup("#nameField");
                TextField textField = (TextField) getDialogPane().lookup("#textField");
                CheckBox checkBox = (CheckBox) getDialogPane().lookup("#isSynchronization");

                ArrayList<String> result = new ArrayList<>();
                result.add(nameText.getText()); // 名称
                result.add(textField.getText()); // 字符
                result.add(Boolean.toString(checkBox.isSelected())); // CheckBox 状态

                return result;
            }
            return null;
        });

        // 创建对话框的内容
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField nameField = new TextField(textBlock.getName());
        nameField.setId("nameField"); // 为查找设置 ID

        TextField textField = new TextField(textBlock.getText());
        textField.setId("textField"); // 为查找设置 ID

        TextField MarginField = new TextField(textBlock.buildMargins());
        MarginField.setId("MarginField"); // 为查找设置 ID

        textField.textProperty().addListener((observable, oldValue, newValue) -> {
            if(Illegal.isStringIlleagal(newValue)) {
                textField.setText(oldValue);
                errInText();
            }
        });
        nameField.textProperty().addListener((observable, oldValue, newValue) -> {
            if(Illegal.isStringIlleagal(newValue)) {
                textField.setText(oldValue);
                errInText();
            }
        });

        CheckBox checkBox = new CheckBox("名称同步字符");
        checkBox.setId("isSynchronization"); // 为查找设置 ID
        checkBox.setSelected(textBlock.isSynchronization());

        // 监听 checkBox 的改变以同步或不同步 nameField 和 textField
        checkBox.selectedProperty().addListener((observable, oldValue, newValue) -> {
            nameField.setEditable(!newValue);
            if (newValue) {
                nameField.setText(textField.getText());
            }
        });

        // 监听 textField 的变化来同步 nameField 的内容，仅当 checkBox 选中时生效
        textField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (checkBox.isSelected()) {
                nameField.setText(newValue);
            }
        });

        // 初始化时处理同步逻辑
        if (textBlock.isSynchronization()) {
            nameField.setEditable(false);
            nameField.setText(textField.getText());
        }

        grid.add(new Label("名称:"), 0, 0);
        grid.add(new Label("字符:"), 0, 1);
        grid.add(new Label("ID: " + target.getID()), 0, 3);
        grid.add(new Label("间距: "), 0, 4);

        grid.add(nameField, 1, 0);
        grid.add(textField, 1, 1);
        grid.add(checkBox, 3, 0);
        grid.add(MarginField,1,4);

        getDialogPane().setContent(grid);
        getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
    }

    private void errInText() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("错误");
        alert.setHeaderText("输入错误");
        alert.setContentText("请输入合法的字符串(注意不要包括空格,*,&,%等特殊符号)");
        alert.showAndWait();
    }
}