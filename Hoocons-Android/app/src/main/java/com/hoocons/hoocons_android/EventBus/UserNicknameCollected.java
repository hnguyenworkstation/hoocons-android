package com.hoocons.hoocons_android.EventBus;

/**
 * Created by hungnguyen on 9/5/17.
 */

public class UserNicknameCollected {
    private String nickname;

    public UserNicknameCollected(String nickname) {
        this.nickname = nickname;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
}
