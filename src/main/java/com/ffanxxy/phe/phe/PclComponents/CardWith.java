package com.ffanxxy.phe.phe.PclComponents;

import com.ffanxxy.phe.phe.PclComponents.components.Card;
import javafx.scene.control.ComboBox;

import java.io.Serial;
import java.io.Serializable;

public class CardWith implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    public Card card;
    public transient ComboBox<String> comboBox;

    private final ID id;

    public CardWith(Card card, ComboBox<String> comboBox) {
        this.card = card;
        this.comboBox = comboBox;
        id = new ID(this);
    }

    public String getID() {
        return id.getID();
    }

    public Card getCard() {
        return card;
    }
    public ComboBox<String> getComboBox() {
        return comboBox;
    }

    public CardWith setCard(Card card) {
        this.card = card;
        return this;
    }
    public CardWith setComboBox(ComboBox<String> comboBox) {
        this.comboBox = comboBox;
        return this;
    }

}
