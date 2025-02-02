package com.ffanxxy.phe.phe;

import com.ffanxxy.phe.phe.Applicant.Logger;
import com.ffanxxy.phe.phe.PclComponents.CardWith;
import com.ffanxxy.phe.phe.PclComponents.components.Card;
import com.ffanxxy.phe.phe.Save.SLphe;
import javafx.animation.FadeTransition;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;
import javafx.util.Duration;

import javax.naming.ldap.Control;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import static com.ffanxxy.phe.phe.Applicant.Logger.regeisterLog;

public class MainApplication extends Application {

    private static Stage primaryStageStatic; // 静态变量保存Stage引用


    public static Stage getPrimaryStage() {
        return primaryStageStatic;
    }

    private static final String TAG = "MainApplication";
    public static ArrayList<CardWith> Assemblies = new ArrayList<>();
    public static String savePath = null;


    @Override
    public void start(Stage stage) throws IOException {
        primaryStageStatic = stage;

        // 加载主界面
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("mainform.fxml"));

        // 设置场景和样式表
        Scene scene = new Scene(fxmlLoader.load(), 1080, 640);
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("fillet.css")).toExternalForm());
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("style.css")).toExternalForm());
        scene.setFill(Color.TRANSPARENT); // 设置场景背景透明

        // 设置窗口为无边框和透明背景
        stage.initStyle(StageStyle.TRANSPARENT);

        stage.setTitle("PCL2 Homepage Editor");
        stage.setScene(scene);
        stage.show();

        regeisterLog("MainApplication.class");


        stage.setOnCloseRequest(event -> {
            Logger.separateLog();
            Logger.addLog("onCloseRequest","退出程序...");
            if(savePath == null) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("保存");
                alert.setHeaderText("是否需要保存?");
                alert.setContentText("确定吗");

                Optional<ButtonType> result = alert.showAndWait();

                if(result.isEmpty()) {
                    Logger.warning(TAG,"已取消选择!");
                    return;
                }
                if (result.get() == ButtonType.OK) {
                    File file = SLphe.chooseDirectory();
                    if (file == null) {
                        Logger.warning(TAG, "未选择保存路径!");
                        event.consume(); // 阻止关闭操作
                        return;
                    }
                    savePath = file.getPath();

                    // 异步保存操作
                    CompletableFuture.runAsync(() -> {
                        ArrayList<String> as = workBuild();
                        SLphe.save(savePath, Assemblies, as);
                        // 保存完成后关闭窗口
                        Platform.runLater(stage::close);
                    });
                    event.consume(); // 阻止立即关闭，等待保存完成
                }

            } else {
                ArrayList<String> as = workBuild();
                SLphe.save(savePath,Assemblies, as);
            }
        });


        // 注册日志
        regeisterLog(TAG);

    }


    public static ArrayList<String> workBuild() {
        ArrayList<String> as = new ArrayList<>();
        as.add(savePath);
        return as;
    }


    public static void main(String[] args) {
        launch();

        // 设置未处理异常处理器
        Thread.setDefaultUncaughtExceptionHandler((thread, throwable) -> {
            System.err.println("未处理的异常发生在线程 " + thread.getName());
            throwable.fillInStackTrace();
            Logger.errorLog("未处理的异常发生在线程 " + thread.getName(),thread);
            performCleanup();
            System.err.println("未处理异常处理器完成.");
        });

        // 注册关闭钩子
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("正在执行关闭钩子...");
            performCleanup();
            System.out.println("关闭钩子完成.");
        }));

        System.out.println("请等待退出...");

        // 模拟正常退出
        try {
            // 让程序运行一段时间
            Thread.sleep(20);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.err.println("主线程被中断.");
        }

        // 正常退出
        System.out.println("应用程序正常退出.");

    }

    private static void performCleanup() {
        // 执行清理操作
        System.out.println("执行清理操作...");

        Logger.spawnLog();
    }
}