package com.tck.daole.sidebar;

/**
 * kylin on 16/9/24.
 */

public class Games {
    private String name;
    private String pinyin;
    public String gameId;

    public Games(String name, String pinyin) {
        this.name = name;
        this.pinyin = pinyin;
    }


    public Games(String name, String pinyin,String gameId) {
        this.name = name;
        this.pinyin = pinyin;
        this.gameId = gameId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPinyin() {
        return pinyin;
    }

    public void setPinyin(String pinyin) {
        this.pinyin = pinyin;
    }
}
