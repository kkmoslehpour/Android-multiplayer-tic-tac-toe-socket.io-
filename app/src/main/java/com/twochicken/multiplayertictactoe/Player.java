package com.twochicken.multiplayertictactoe;

public class Player {
    String name;
    String type;
    boolean isMyTurn;

    public Player(String name, String type, boolean isMyTurn) {
        this.name = name;
        this.type = type;
        this.isMyTurn = isMyTurn;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isMyTurn() {
        return isMyTurn;
    }

    public void setMyTurn(boolean myTurn) {
        isMyTurn = myTurn;
    }
}
