package com.ffanxxy.phe.phe;

import com.ffanxxy.phe.phe.Applicant.ConstantTable.Illegal;
import com.ffanxxy.phe.phe.Applicant.Logger;
import com.ffanxxy.phe.phe.Applicant.ass;
import com.ffanxxy.phe.phe.Applicant.work;
import com.ffanxxy.phe.phe.PclComponents.CardWith;
import com.ffanxxy.phe.phe.PclComponents.PAssemblies;
import com.ffanxxy.phe.phe.PclComponents.PCLC;
import com.ffanxxy.phe.phe.PclComponents.components.Card;
import com.ffanxxy.phe.phe.PclComponents.components.TextBlock;
import com.ffanxxy.phe.phe.PclComponents.components.setting.CardSetting;
import com.ffanxxy.phe.phe.PclComponents.components.setting.TextBlockSetting;
import com.ffanxxy.phe.phe.Save.SLphe;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Callback;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;

import static com.ffanxxy.phe.phe.MainApplication.*;

public class AllController {

    private static final String AC_INFO = "AllController";
    private static PCLC chooseAss;
    private static CardWith chooseCardWith;

    @FXML
    public ImageView CloseB;


    @FXML
    private TextField Mup;
    @FXML
    private TextField Mdown;
    @FXML
    private TextField Mleft;
    @FXML
    private TextField Mright;
    @FXML
    private VBox layout;
    @FXML
    private VBox bar;

    private double xOffset = 0;
    private double yOffset = 0;

    @FXML
    private void AppClose() {
        Stage stage = MainApplication.getPrimaryStage(); // 获取Stage引用
        WindowEvent closeRequestEvent = new WindowEvent(stage, WindowEvent.WINDOW_CLOSE_REQUEST);
        stage.fireEvent(closeRequestEvent); // 手动触发onCloseRequest事件

        if (!closeRequestEvent.isConsumed()) {
            stage.close(); // 如果没有阻止关闭操作，则手动关闭窗口
        }
    }
    @FXML
    private void AppMax() {
        Stage stage = MainApplication.getPrimaryStage();
        stage.setMaximized(!stage.isMaximized());
    }
    @FXML
    private void AppMin() {
        Stage stage = MainApplication.getPrimaryStage();
        stage.setIconified(true);
    }

    private void handleMousePressed(MouseEvent event) {
        // 记录按下时的鼠标位置相对于窗口的位置
        xOffset = event.getSceneX();
        yOffset = event.getSceneY();
    }

    private void handleMouseDragged(MouseEvent event) {
        // 根据当前鼠标位置和按下时的位置计算新的窗口位置
        Stage stage = (Stage) ((VBox) event.getSource()).getScene().getWindow();
        stage.setX(event.getScreenX() - xOffset);
        stage.setY(event.getScreenY() - yOffset);
    }

    private void handleMouseReleased(MouseEvent event) {
        // 可选：在这里可以添加一些操作，例如重置状态
    }


    /**
     * 通过ID刷新chooseAss到ass
     */
    private void reloadChooseAssToAss() {
        if(chooseAss != null) {
            chooseCardWith.setCard(chooseCardWith.getCard().setComponet(chooseAss.getID(), chooseAss));
        }
        ass.set("AllController.initialize",
                chooseCardWith.getID(),
                chooseCardWith);
    }

    @FXML
    private void initialize() {
        TassName.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if(chooseAss != null) {
                chooseAss.setName(TassName.getText());
                reloadChooseAssToAss();
                Logger.info(AC_INFO,"已设置 " + chooseAss.getName() + " 的名称为" + chooseAss.getName());

                refreshComboBoxes();
                reloadMenu();
                resetTab();
                resetTab();
            }
        });

        bar.setOnMousePressed(this::handleMousePressed);
        bar.setOnMouseDragged(this::handleMouseDragged);
        bar.setOnMouseReleased(this::handleMouseReleased);

        layout.setStyle("-fx-background-color: #5286D8;");
        Logger.info(AC_INFO,"初始化圆角裁剪...");

        Rectangle clip = new Rectangle();
        clip.widthProperty().bind(layout.widthProperty());
        clip.heightProperty().bind(layout.heightProperty());
        clip.setArcWidth(15); // 调整为你希望的圆角大小
        clip.setArcHeight(15); // 调整为你希望的圆角大小

        // 应用裁剪
        layout.setClip(clip);


        Mup.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                if(chooseAss == null) return;
                chooseAss.setMargin(1,Integer.parseInt(Mup.getText()));
                Logger.info(AC_INFO,"已设置 " + chooseAss.getName() + " 的上边距为" + chooseAss.getMargin(1));
            }
        });
        Mdown.focusedProperty().addListener((observable,oldValue,newValue) -> {
            if (!newValue) {
                if(chooseAss == null) return;
                chooseAss.setMargin(3,Integer.parseInt(Mdown.getText()));
                Logger.info(AC_INFO,"已设置 " + chooseAss.getName() + " 的下边距为" + chooseAss.getMargin(3));
            }
        });
        Mleft.focusedProperty().addListener((observable,oldValue,newValue) -> {
            if (!newValue) {
                if(chooseAss == null) return;
                chooseAss.setMargin(0,Integer.parseInt(Mleft.getText()));
                Logger.info(AC_INFO,"已设置 " + chooseAss.getName() + " 的左边距为" + chooseAss.getMargin(0));
            }
        });
        Mright.focusedProperty().addListener((observable,oldValue,newValue) -> {
            if (!newValue) {
                if(chooseAss == null) return;
                chooseAss.setMargin(2,Integer.parseInt(Mdown.getText()));
                Logger.info(AC_INFO,"已设置 " + chooseAss.getName() + " 的右边距为" + chooseAss.getMargin(2));
            }
        });
    }

    @FXML
    private Label assName;
    @FXML
    private TextField TassName;
    
    @FXML
    private MenuItem AddCard;
    @FXML
    private AnchorPane AssList;
    @FXML
    private Label tips;
    @FXML
    private Menu CardMenu;
    /**
     * 所有MyCard的列表的Tab
     */
    @FXML
    private Tab CardsTab;
    /**
     * 所有MyCard的列表
     */
    @FXML
    private ListView<String> CardsList;
    @FXML
    private Label RightTips;

    @FXML
    public void AddMyCard() {
        Logger.info(AC_INFO,"正在添加卡片...");
        String name = AskForCreate();
        if(name != null) {
            CardWith cw = AddCard(name);
            if(cw != null) {
                tips.setText("添加成功");
            }else {
                errorInCreate("包含非法字符!");
                errorInCreate("包含非法字符!");
            }
        }else {
             errorInCreate("没有名字!");
        }

    }

    @FXML
    private TabPane Tabs;
    @FXML
    private Tab setAss;

    public void resetTab() {
        CardsList.getItems().setAll(PAssemblies.getAssembliesNames());

        // 自定义单元格工厂
        CardsList.setCellFactory(new Callback<ListView<String>, ListCell<String>>() {
            @Override
            public ListCell<String> call(ListView<String> param) {
                return new ListCell<String>() {
                    @Override
                    protected void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty || item == null) {
                            setText(null);
                        } else {
                            setText(item);

                            // 为每个单元格设置鼠标点击事件处理器
                            setOnMouseClicked(event -> {
                                if (!isEmpty()) {
                                    CardWith cw = PAssemblies.getAssembliesByName(item);
                                    Card card;
                                    if (cw != null) {
                                        card = cw.getCard();
                                    } else {
                                        return;
                                    }
                                    CardSetting cs = new CardSetting(card);

                                    // 提取参数
                                    Optional<String> result = cs.showAndWait();

                                    // 处理结果
                                    if (result.isPresent()) {
                                        String newName = result.get();
                                        Logger.info(AC_INFO,"已重命名卡片: " + newName);

                                        ass.set("reTab-OnMouseClicked",ass.indexOf(cw),
                                                cw.setCard(
                                                        cw.getCard().resetCardName(newName)
                                                )
                                        );

                                        chooseCardWith = cw;

                                        //处理Margin
                                        int[] margin = cs.getMargin();
                                        int[] stackMargin = cs.getStackMargin();
                                        if(margin != null) {
                                            chooseCardWith.getCard().setMargin(margin);
                                            Logger.info(AC_INFO,"已设置 " + chooseCardWith.getCard().getName() + " 的Margin为" + Arrays.toString(margin));
                                            reloadChooseAssToAss();
                                        }
                                        if(stackMargin != null) {
                                            chooseCardWith.getCard().setStackPanleMargin(stackMargin);
                                            Logger.info(AC_INFO,"已设置 " + chooseCardWith.getCard().getName() + " 的StackPanelMargin为" + Arrays.toString(stackMargin));
                                            reloadChooseAssToAss();
                                        }

                                        // 如果需要获取 ListView 的选中项
                                        String selectedItem = cs.getSelectedItem();
                                        if (selectedItem != null) {
                                            Logger.info(AC_INFO,"选择了物品:" + selectedItem);
                                            RightTips.setText(selectedItem);

                                            chooseAss = cw.getCard().getComponent(selectedItem);



                                            if(chooseAss == null) {
                                                Logger.err(AC_INFO,"没有选择到组件!");
                                                return; }

                                            Logger.info(AC_INFO,"选择的组件:" + chooseAss.getName());
                                            reloadTabs(chooseAss);

                                            Mup.setText(chooseAss.getMargin(1) + "");
                                            Mdown.setText(chooseAss.getMargin(3) + "");
                                            Mleft.setText(chooseAss.getMargin(0) + "");
                                            Mright.setText(chooseAss.getMargin(2) + "");

                                            Tabs.getSelectionModel().select(setAss); // 选择该Tab

                                        } else {
                                            Logger.info(AC_INFO,"未选择物品!");
                                        }

                                    } else {
                                        Logger.info(AC_INFO,"取消了设置卡片。");
                                    }

                                    // 刷新
                                    refreshComboBoxes();
                                    reloadMenu();
                                    resetTab();
                                }
                            });
                        }
                    }
                };
            }
        });
    }


    private void reloadTabs(PCLC pclc) {
        assName.setText(pclc.getName());
        TassName.setText(pclc.getName());
    }


    /**
     * 初始化添加一个菜单项到 CardMenu 中
     *
     * @param name      菜单项名称
     * @param cardWith  与菜单项关联的 CardWith
     */
    private void addMenu(String name, CardWith cardWith) {
        addMenu(name,cardWith,true);
    }

    /**
     * 初始化添加一个菜单项到 CardMenu 中
     *
     * @param name      菜单项名称
     * @param cardWith  与菜单项关联的 CardWith
     * @param shouldReload 是否需要刷新(加载文件时不要刷新!)
     *
     */
    private void addMenu(String name, CardWith cardWith,boolean shouldReload) {

        Menu card = new Menu(name);
        card.setUserData(cardWith); // 将 CardWith 关联到 Menu

//        Logger.info(AC_INFO,"当前卡片:" + cardWith.getCard().getName());

        // 添加菜单项
        MenuItem textItem = new MenuItem("添加文本框");
        MenuItem ImageItem = new MenuItem("添加图片框");


        textItem.setOnAction(event -> {

            // 从 Menu 获取关联的 CardWith
            CardWith associatedCardWith = (CardWith) card.getUserData();

            // 添加小组件并更新 Assemblies
            int index = ass.indexOf(associatedCardWith);
            if (index != -1) { // 确保找到了对应的 CardWith
                ass.set("addForCardMenu- textItem.setOnAction",index, addTextBlock(associatedCardWith));

                refreshComboBoxes();

                Debug();

            } else {
                System.err.println("Associated CardWith not found in Assemblies.");
            }
        });


        // 将菜单添加到 CardMenu 中
        CardMenu.getItems().add(card);

        card.getItems().addAll(textItem,ImageItem);

        Logger.addLog("AllController.addMenu","添加菜单:" + name);

        if(shouldReload) {
            // 刷新
            refreshComboBoxes();
            reloadMenu();
            resetTab();
        }
    }


    /**
     *
     * 发送警告
     *
     * @param text 警告文本
     */
    private void errorInCreate(String text) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("警告");
        alert.setHeaderText("在创建时出现错误");
        alert.setContentText(text);

        alert.showAndWait();
    }

    private String AskForCreate() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("新增卡片");
        dialog.setHeaderText("设置卡片名称...");
        dialog.setContentText("设置名称：");

        Optional<String> result = dialog.showAndWait();

        return result.orElse(null);

    }



    private CardWith addTextBlock(CardWith cardwith) {
        tips.setText("正在添加文本...");
        TextBlock tb = new TextBlock("");
        TextBlockSetting tbs = new TextBlockSetting(tb);
        Optional<ArrayList<String>> result = tbs.showAndWait();

        ArrayList<String> r = result.orElse(null);

        if (r != null) {
            tb.setName(r.get(0));
            tb.setText(r.get(1));
            tb.setSynchronization(Boolean.parseBoolean(r.get(2)));
            tips.setText("添加成功:" +  tb.getID());
        }else {
            errorInCreate("没有名字或字符!");
            tips.setText("");
            return cardwith;
        }


        Logger.info(AC_INFO,"创建了文本Textbox:" + tb.getText());


        cardwith.setCard(cardwith.getCard().addComponent(tb));


        return cardwith;
    }


    /**
     * <p>对于AddCard的创建方法的快速实现使用，允许被外部直接调用</p>
     * @param name 新 ComboBox 的名称
     * @return 创建的CardWith
     * <p><br>实现方法:{@link AllController#AddCard(String, boolean, CardWith)}</p>
     */
    public CardWith AddCard(String name) {
        return AddCard(name, true, null);
    }

    private boolean isClearingSelection = false;
    /**
     * 添加一个新的 ComboBox 到 AnchorPane 中
     *
     * @param name   新 ComboBox 的名称
     * @param normal 是否为加载模式，关系于是否创建Ass,是否刷新,是否使用{@code CardWith c} <p> 如果为true,则为创建模式;否则则为加载模式</p>
     * @param c      与 ComboBox 关联的 CardWith(没有时请输入null)
     * @return 创建的CardWith
     */
    private CardWith AddCard(String name, boolean normal, CardWith c) {

        if(Illegal.isStringIlleagal(name)) {
            return null;
        }
        if(name == null || name.isEmpty()) {
            return null;
        }

        int size = AssList.getChildren().size();

        // 创建 ComboBox
        ComboBox<String> comboBox = new ComboBox<>();
        comboBox.setPromptText(name);

        CardWith cardwith = Objects.requireNonNullElseGet(c, () -> new CardWith(new Card(name), comboBox));

        if(normal) {
            ass.add("AllController.AddCard-shouldAddAss", cardwith);
        } else {
            cardwith.setComboBox(comboBox);
            ass.set("AllController.AddCard-shouldAddAss", ass.indexOf(cardwith), cardwith);
        }

//        DebugForAssemblies();
        comboBox.setUserData(cardwith);

//        设置Combox里的items
        ObservableList<String> items = FXCollections.observableArrayList(ass.getCard(size-1).getComponentsName());
        comboBox.setItems(items);

        // 设置锚点以适应 AnchorPane 的大小变化
        AnchorPane.setTopAnchor(comboBox, 60+25.0 * size);
        AnchorPane.setLeftAnchor(comboBox, 0.0);
        AnchorPane.setRightAnchor(comboBox, 0.0); // 确保 ComboBox 不覆盖按钮

        resetTab();

        comboBox.valueProperty().addListener((observable, oldValue, newValue) -> {
            Logger.info(AC_INFO,"正在设置属性");

            if (isClearingSelection || newValue == null) {
                return;
            }

            // 从 ComboBox 获取关联的 CardWith
            CardWith associatedCardWith = (CardWith) comboBox.getUserData();

//            Logger.info(AC_INFO,"选择的物品： " + removeTags(newValue));

            if(associatedCardWith.getCard().getComponent(removeTags(newValue)) != null) {
                PCLC selected = associatedCardWith.getCard().getComponent(removeTags(newValue));


                Logger.info(AC_INFO,"选择组件的类型" + selected.getType() +
                        "\n 选择组件的名称（来自选择的物品）： " + selected.getName() +
                        "\n 选择组件的名称（来自选事件监听器）：" + newValue +
                        "\n 选择的组件: " + selected.getName());

                if (selected instanceof TextBlock) {
                    TextBlockSetting tbs = new TextBlockSetting((TextBlock) selected);
                    Optional<ArrayList<String>> result = tbs.showAndWait();

                    ArrayList<String> r = result.orElse(null);

                    if (r != null) {
                        boolean isSynchronization = Boolean.parseBoolean(r.get(2)); // CheckBox 状态
                        ((TextBlock) selected).setSynchronization(isSynchronization);

                        ((TextBlock) selected).setName(r.get(0));
                        ((TextBlock) selected).setText(r.get(1));
                    }
                }

//                Logger.info(AC_INFO,"选择的组件： " + selected);

                Logger.info(AC_INFO,"正在重置...");

                // 设置标志位以防止再次触发监听器
                isClearingSelection = true;
                Platform.runLater(() -> {
                            comboBox.getSelectionModel().clearSelection(); // 清除选择


                            Logger.info(AC_INFO,"正在刷新名称为:" + associatedCardWith.getCard().getName());
                            comboBox.setPromptText(associatedCardWith.getCard().getName());

                            refreshComboBoxes();
                        });
                isClearingSelection = false; // 恢复标志位


            }else {
                errorInCreate("没有找到组件!");
            }
        });

        // 将 ComboBox 添加到 AnchorPane
        AssList.getChildren().add(comboBox);

//        添加卡片
        addMenu(name,cardwith, normal);

//        DebugForAssemblies();

        if(normal) {
            // 刷新所有 ComboBox 以确保新添加的 Card 的 ComboBox 是最新的
            resetTab();
            reloadMenu();
            refreshComboBoxes();
        }

        Logger.addLog("AllController.AddCard-shouldAddAss",
                "创建组件完成:" + "卡片名称：" + cardwith.getCard().getName() + "\n卡片组件：" + cardwith.getCard().getComponentsName());

        return cardwith;
    }

    /**
     * 去除字符串中所有形如 <...> 的内容。
     *
     * @param input 包含特殊标记的原始字符串
     * @return 去除了特殊标记的新字符串
     */
    public static String removeTags(String input) {
        if (input == null || input.isEmpty()) {
            return input;
        }

        // 使用正则表达式匹配形如 <...> 的内容，并用空字符串替换它们
        return input.replaceAll("<[^>]*>", "");
    }

    /**
     * 刷新所有的 ComboBox 以匹配它们关联的 Card 中的组件名称。
     *
     */
    public static void refreshComboBoxes() {
        Logger.info(AC_INFO,"正在刷新ComboBox...");

        for (CardWith cardWith : ass.get()) {
            Card card = cardWith.getCard();
            ComboBox<String> comboBox = cardWith.getComboBox();

            comboBox.setPromptText(card.getName());

            // 保存当前选择
            String currentSelection = comboBox.getValue();

            // 获取card中所有组件的名字
            ArrayList<String> componentNames = card.getComponentsName();
            // 获取card中所有组件类型
            ArrayList<String> componentTypes = card.getComponentsType();

            // 组合两个列表
            ArrayList<String> ItemShow = combineLists(componentTypes, componentNames, "<", ">");

            // 创建一个新的 ObservableList 并设置给 ComboBox
            ObservableList<String> items = FXCollections.observableArrayList(ItemShow);
            comboBox.setItems(items);

            // 恢复之前的选中项
            if (currentSelection != null && !currentSelection.isEmpty()) {
                comboBox.setValue(currentSelection);
            }

        }

    }


    public void reloadMenu() {
        ArrayList<String> NewName = new ArrayList<>();
        for(CardWith cardWith : ass.get()) {
            NewName.add(cardWith.getCard().getName());
        }
        // 更新 Menu 列表
        updateMenuNames(CardMenu.getItems(), NewName); //NewName.size = Assemblies.size
        tips.setText("刷新成功");
    }
    /**
     * 更新 ObservableList<Menu> 中每个 Menu 的名称为 ArrayList<String> 中对应的项。
     *
     * @param menuList   已经存在的 Menu 列表
     * @param stringList 包含新名称的字符串列表（必须是 ArrayList<String>）
     */
    public void updateMenuNames(ObservableList<MenuItem> menuList, ArrayList<String> stringList) {

        // 验证列表大小是否匹配
        if (menuList.size() != stringList.size()) {
            throw new IllegalArgumentException("菜单列表和字符串列表的大小不匹配\n" + "Menu表数量：" + menuList.size() + "\n所有组件数：" + stringList.size());
        }

        // 遍历并更新每个 Menu 的名称
        for (int i = 0; i < menuList.size(); i++) {
            MenuItem menu = menuList.get(i);
            String newName = stringList.get(i);

            // 设置 Menu 的文本为新的名称
            menu.setText(newName);
        }
    }
    public void addMenus(ArrayList<CardWith> arrayList) {
            for (CardWith cw : arrayList) {
                String name = cw.getCard().getName();
                addMenu(name, cw, false);
            }
    }


    /**
     * Debug
     */
    protected void Debug() {
        for (CardWith cardWith : ass.get()) {
            Logger.info(AC_INFO,"=调试信息");
            Logger.info(AC_INFO,"=卡片名：" + cardWith.getCard().getName());
            Logger.info(AC_INFO,"=卡片组件：" + cardWith.getCard().getComponentsName());
            Logger.info(AC_INFO,"=选择栏名称：" + cardWith.getComboBox().getPromptText());
        }
    }

    /**
     * 将两个列表组合成一个
     * <p>组合方法:<br>
     * A: 1 2 3<br>
     * B: a b c<br>
     * 组合后:<br>
     * 1a 2b 3c</p>
     *
     * @param listA 前列表
     * @param listB 后列表
     * @param suffix 后缀
     * @return 合计列表
     */
    private static ArrayList<String> combineLists(ArrayList<String> listA, ArrayList<String> listB,String prefix,String suffix) {
        ArrayList<String> result = new ArrayList<>();
        int size = Math.min(listA.size(), listB.size()); // 确保不会超出任一列表的界限

        for (int i = 0; i < size; i++) {
            // 将A和B中相同索引位置的元素组合起来，
            result.add(prefix + listA.get(i) + suffix + listB.get(i));
        }

        return result;
    }

//    保存功能

    public SLphe slphe;
    @FXML
    public void Saveto() {
        if(work.getSavePath() == null) {
            Saveas();
        } else {
            ArrayList<String> as = workBuild();
            SLphe.save(work.getSavePath(),ass.get(), as);
        }

    }
    @FXML
    public void Saveas() {
        Logger.info(AC_INFO,"另存为...");
        slphe = new SLphe(Assemblies, workBuild());
        slphe.ReloadSavePath(true);

        File p = slphe.saveAsZip();

        if(p == null) {
            Logger.info(AC_INFO,"取消了另存为");
            return;
        }
        Logger.info(AC_INFO,"另存为:" + p.getPath());
        work.setSavePath(p.getPath());
    }

    @FXML
    public void OpenFile() {
        slphe = new SLphe(ass.get(), workBuild());
        File openFile = slphe.loadFromZip();

        if(openFile == null) return;

        ass.set("AllController.OpenFile-Save" , slphe.getAssemblies());  //此处Assemblies.size = 1
        String path = slphe.getWork().getFirst();

        if(!openFile.getPath().equals(path)) {
            if(YoNDialog("警告","文件路径与存储路径不匹配","文件路径与存储路径不匹配，是否更新为选择的路径?")) {
                work.setSavePath(openFile.getPath());
            }else {
                work.setSavePath(path);
            }
        }

        addCards(ass.get());

        resetTab();
        reloadMenu();
        refreshComboBoxes();
    }

    private void addCards(ArrayList<CardWith> cardWiths) {
        for(CardWith cw : cardWiths) {
            Logger.addLog("AC.addCards"," =Line" + cardWiths.indexOf(cw) + "== " + cw.getCard().getName());
            AddCard(cw.getCard().getName(),false,cw);
        }
    }

    private void DebugForAssemblies() {
        for(CardWith cw : ass.get()) {
            Logger.info(AC_INFO,"名称：" + cw.getCard().getName());
            Logger.addLog("AllController.DebugForAssemblies","<Line:" +
                    (ass.indexOf(cw) + 1) + ">  "
                    + cw.getCard().getName());
        }
        Logger.info(AC_INFO,"组件数量" + ass.size());
    }

//    工作区
    @FXML
    private void delAll() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("确定");
        alert.setHeaderText("是否需要删除");
        alert.setContentText("你真的要做出这样的决定吗？？？");

        Optional<ButtonType> result = alert.showAndWait();
        if(result.isPresent()) {
            if (result.get() == ButtonType.OK) {
                ass.set("del-all(User)", new ArrayList<CardWith>());
                CardMenu.getItems().setAll();
                AssList.getChildren().removeIf(n -> !(n instanceof Label));
                CardsList.getItems().setAll();
            }
        }
    }

    private boolean YoNDialog(String title,String HeaderText,String ContentText) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText(HeaderText);
        alert.setContentText(ContentText);

        Optional<ButtonType> result = alert.showAndWait();
        return result.filter(buttonType -> buttonType == ButtonType.OK).isPresent();
    }

    //
    @FXML
    private void reloadMargin() {
        int[] margin = new int[]{Integer.parseInt(Mup.getText()),Integer.parseInt(Mdown.getText()),Integer.parseInt(Mleft.getText()),Integer.parseInt(Mright.getText())};
        chooseAss.setMargin(margin);
        Logger.info(AC_INFO,"已设置 " + chooseAss.getName() + " 的Margin为" + Arrays.toString(margin));
    }

//    构建
    @FXML
    private void SaveXmal() {
        slphe = new SLphe(ass.get(),workBuild());
        slphe.spawn(slphe.build());
    }
}

