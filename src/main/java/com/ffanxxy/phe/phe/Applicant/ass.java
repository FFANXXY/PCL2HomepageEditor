package com.ffanxxy.phe.phe.Applicant;

import com.ffanxxy.phe.phe.PclComponents.CardWith;
import com.ffanxxy.phe.phe.PclComponents.components.Card;

import java.util.ArrayList;
import java.util.stream.Collectors;

import static com.ffanxxy.phe.phe.MainApplication.Assemblies;

/**
 * 申请操作控件库
 */
public class ass {
    public static void set(String methon , ArrayList<CardWith> cardWiths) {
        Assemblies = new ArrayList<>(cardWiths);

        Logger.addLog(methon,"设置全部组件，当前组件:" + ass.get());
        for(CardWith cw : ass.get()) {
            Logger.addLog((ass.indexOf(cw)+1) + "","组件:<Card>" + cw.getCard().getName());
        }
        Logger.separateLog();
    }

    /**
     * 设置ass内的卡片
     * @param methon 调用的方法
     * @param cardWith (用于定位)Card所属的CardWith
     * @param card 要设置的Card
     */
    public static void setCard(String methon,CardWith cardWith, Card card) {
        ass.set(methon,ass.indexOf(cardWith),cardWith.setCard(card));
    }
    public static void setCard(String methon,String id  , Card card) {
        for(CardWith cardWith : ass.get()) {
            if(cardWith.getID().equals(id)) {
                ass.setCard(methon,cardWith,card);
                return;
            }
        }
    }


    public static Card getCard(CardWith cardWith) {
        return ass.get(ass.indexOf(cardWith)).getCard();
    }
    public static Card getCard(int index) {
        return ass.get(index).getCard();
    }
    public static Card getCard(String id) {
        for(CardWith cw : ass.get() ) {
            if(cw.getID().equals(id)) {
                return cw.getCard();
            }
        }
        return null;
    }

    public static void set(String methon, int index,CardWith cardWith) {
        Assemblies.set(index,cardWith);
        Logger.addLog(methon,"设置组件:" +
                "第" + (index+1) +  "行" +
                ",设置为" +  cardWith.getCard());
    }

    public static void set(String methon, String id, CardWith cw) {
        for(CardWith cardWith : ass.get()) {
            if(cardWith.getID().equals(id)) {
                ass.set(methon,ass.indexOf(cardWith),cw);
                return;
            }
        }
    }

    public static void add(String methon , CardWith cardWith) {
        Logger.addLog(methon,"添加组件:" + "第" + (Assemblies.size()+1) +  "行" + "设置为" +  cardWith.getCard());
        Assemblies.add(cardWith);
    }

    public static int size() {
        return Assemblies.size();
    }

    public static int indexOf(CardWith cardWith) {
        return Assemblies.indexOf(cardWith);
    }

    public static CardWith get(int index) {
        return Assemblies.get(index);
    }

    public static ArrayList<CardWith> get() {
        return new ArrayList<>(Assemblies);
    }

}
