package com.ffanxxy.phe.phe.Applicant;

import com.ffanxxy.phe.phe.Applicant.ConstantTable.SystemColor;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.lang.management.ThreadInfo;
import java.lang.management.ThreadMXBean;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

public class Logger {

    private static SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    public static Date date = new Date();
    public static ArrayList<String> logs = new ArrayList<>();

    private static final String GeneralMethod = "Logger";

    private static SimpleDateFormat LogFormat = new SimpleDateFormat("yyyy_MM_dd_HH-mm-ss");

    /**
     * 注册日志
     * @param method 注册的方法
     */
    public static void regeisterLog(String method) {
        addLog(GeneralMethod + "<Regeister> ","注册了日志在 " + formatter.format(date) + " 被 " + method + " 方法");
//        输出提示
        System.out.println(formatter.format(date) + " " + method + " ");
//        备份使用
        logs.add(formatter.format(date) + " " + method + " ");
        separateLog();
    }

    public static void separateLog() {
        logs.add("-------------------------");
    }

    public static void addLog(String method,String log) {
        logs.add("<" +  method + "> " + log);
    }


    public static void spawnLog() {
        // 定义文件路径和名称
        Path filePath = Paths.get("log/" + LogFormat.format(date) + ".log");

        // 确保目录存在
        try {
            Files.createDirectories(filePath.getParent());
        } catch (IOException e1) {
            System.err.println("创建目录时出错.");
            e1.fillInStackTrace();
        }

        // 构建要写入文件的内容
        StringBuilder contentBuilder = new StringBuilder();
        for (String str : logs) {
            contentBuilder.append(str).append("\n");
        }
        String content = contentBuilder.toString();

        try {
            // 使用 write() 方法直接写入字符串，并指定编码
            Files.writeString(filePath, content, StandardCharsets.UTF_8);
            System.out.println("内容已成功写入文件: " + filePath.getFileName());
        } catch (IOException e) {
            System.err.println("写入文件时出错.");
            e.fillInStackTrace();
        }
    }

    public static void errorLog(String method, Exception e) {

        addLog(method,"\n\n" + e.getMessage());
    }

    public static void errorLog(String method,Thread thread) {
        StringBuilder logBuilder = new StringBuilder();

        // 添加方法名
        logBuilder.append("Error in method: ").append(method).append("\n");

        // 获取线程信息
        if (thread != null) {
            logBuilder.append("Thread ID: ").append(thread.threadId()).append("\n");
            logBuilder.append("Thread Name: ").append(thread.getName()).append("\n");
            logBuilder.append("Thread State: ").append(thread.getState()).append("\n");

            // 如果线程有堆栈跟踪（例如，线程抛出了异常），则添加堆栈跟踪信息
            Throwable throwable = findThrowableFromThread(thread);
            if (throwable != null) {
                logBuilder.append("Exception in thread:\n");
                String stackTrace = getStackTraceAsString(throwable);
                logBuilder.append(stackTrace);
            } else {
                logBuilder.append("No exception found in the provided thread.\n");
            }
        } else {
            logBuilder.append("Thread information is null.\n");
        }

        // 调用 addLog 方法记录日志
        addLog(method, "\n\n" + logBuilder.toString() );
    }

    private static Throwable findThrowableFromThread(Thread thread) {
        // 这里假设你有一个方式来关联线程和它可能抛出的异常。
        // 通常情况下，异常应该作为参数传递给 errorLog 方法。
        // 如果你知道线程抛出了异常，可以通过某种机制获取异常对象。
        // 如果没有这样的机制，这个函数可能会返回 null。
        return null; // 默认实现，需要根据实际情况调整
    }

    private static String getStackTraceAsString(Throwable throwable) {
        StringBuilder stackTraceBuilder = new StringBuilder();
        for (StackTraceElement element : throwable.getStackTrace()) {
            stackTraceBuilder.append("\tat ").append(element).append("\n");
        }
        return stackTraceBuilder.toString();
    }

    /**
     * 补充日志
     * <p>直接定位到上一个同method处,并补充
     * </p>
     * @param method 方法
     */
    public static void SupplementLog(String method,String log) {
        int index = findPreviousIndexWithPrefix(logs, "<" + method + ">");
        if(index != -1) {
            logs.add(index+1, "<" + method + ">[补充于" +  formatter.format(date) + "] " + log );
            logs.add("<" + method + "> [补上述] " + formatter.format(date));
        } else {
            logs.add("<" + method + "> 似乎想补充什么，但找不到，它的遗言:" +  log );
        }
    }
    /**
     * 查找给定索引之前最近一个以指定前缀开头的元素的索引。
     *
     * @param list   要搜索的 ArrayList<String>
     * @param prefix 要查找的前缀
     * @return 最近一个匹配项的索引，如果没有找到则返回 -1
     */
    public static int findPreviousIndexWithPrefix(ArrayList<String> list,String prefix) {
        for(String str : list) {
            if(str.startsWith(prefix)) {
                return list.indexOf(str);
            }
        }
        return -1; // 如果没有找到匹配项
    }

    public static void info(String c,String message) {
        System.out.println(SystemColor.CYAN + "<" + c + "> " + SystemColor.LIGHT_CYAN +  message + SystemColor.RESET);
    }
    public static void warning(String c,String message) {
        System.out.println(SystemColor.YELLOW + "<" + c + "> " + SystemColor.LIGHT_YELLOW  +  message + SystemColor.RESET);
    }
    public static void err(String c,String message) {
        System.out.println(SystemColor.RED + "<" + c + "> " + SystemColor.LIGHT_RED  +  message + SystemColor.RESET);
    }
}
