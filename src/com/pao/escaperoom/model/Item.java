package com.pao.escaperoom.model;

import javax.swing.text.StyledEditorKit;

public abstract class Item extends  GameObject implements Storable{

    public Item(String name, String description) {
        super(name, description);
    }

    public abstract String useOn(GameObject target);
}
