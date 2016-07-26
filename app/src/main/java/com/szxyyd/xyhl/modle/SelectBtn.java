package com.szxyyd.xyhl.modle;

import android.widget.Button;

/**
 * Created by jq on 2016/7/20.
 */
public class SelectBtn {
    private boolean isSelect = false;
    private String name;
    private Button view;
    private int id;
    private int tag;

    public SelectBtn() {
        super();
    }
    public boolean isSelect() {
        return isSelect;
    }
    public void setSelect(boolean select) {
        isSelect = select;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTag() {
        return tag;
    }

    public void setTag(int tag) {
        this.tag = tag;
    }

    public Button getView() {
        return view;
    }

    public void setView(Button view) {
        this.view = view;
    }
}
