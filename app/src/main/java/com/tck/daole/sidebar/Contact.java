package com.tck.daole.sidebar;

import java.io.Serializable;

/**
 * kylin on 16/9/24.
 */
public class Contact implements Serializable {
    private String mName;
    public String gameId;
    private int mType;

    public Contact(String name,String gameId, int type) {
        mName = name;
        mType = type;
        this.gameId = gameId;
    }

    public Contact(String name, int type) {
        mName = name;
        mType = type;
    }

    public String getmName() {
        return mName;
    }

    public int getmType() {
        return mType;
    }

}
