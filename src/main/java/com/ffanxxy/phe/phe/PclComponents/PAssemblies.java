package com.ffanxxy.phe.phe.PclComponents;

import com.ffanxxy.phe.phe.MainApplication;

import java.util.ArrayList;

public class PAssemblies {
    public static ArrayList<String> getAssembliesNames() {
        ArrayList<String> names = new ArrayList<>();
        for(CardWith cardwith : MainApplication.Assemblies) {
            names.add(cardwith.getCard().getName());
        }
        return names;
    }
    public static CardWith getAssembliesByName(String name) {
        for(CardWith cardwith : MainApplication.Assemblies) {
            if(cardwith.getCard().getName().equals(name)) {
                return cardwith;
            }
        }
        return null;
    }
}
