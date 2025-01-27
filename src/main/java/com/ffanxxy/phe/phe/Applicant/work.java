package com.ffanxxy.phe.phe.Applicant;

import com.ffanxxy.phe.phe.MainApplication;

public class work {
    public static void setSavePath(String path) {
        MainApplication.savePath = path;
        Logger.addLog("work.setSavePath","set SavePath to : " + path);
    }
    public static String getSavePath() {
        return  MainApplication.savePath;
    }
}
