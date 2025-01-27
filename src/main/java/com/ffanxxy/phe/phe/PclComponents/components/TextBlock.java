package com.ffanxxy.phe.phe.PclComponents.components;

import com.ffanxxy.phe.phe.PclComponents.ID;
import com.ffanxxy.phe.phe.PclComponents.PCLC;

public class TextBlock extends PCLC {

    public final ID ID;

    public TextBlock(String name, String describe) {
        super(name, describe);
        ID = new ID(this);
        setMargin(new int[]{0,0,0,4});
    }
    public TextBlock(String name) {
        super(name);
        ID = new ID(this);
        setMargin(new int[]{0,0,0,4});
    }

    public String getType() {
        return "TextBlock";
    }

    private boolean Synchronization = true;
    public boolean isSynchronization() {
        return Synchronization;
    }
    public void setSynchronization(boolean synchronization) {
        Synchronization = synchronization;
    }

    public String TextWrapping = "Wrap";
    public String FontSize = null;
    public String Foreground = null;

    public String Text;

    public void setText(String text) {
        Text = text;
    }

    public String getText() {
        return Text;
    }


}
