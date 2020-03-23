package com.board.flipr.Model;

public class CheckBox {
    String text;
    Boolean isCheck;

    public CheckBox() {
    }

    public CheckBox(String text, Boolean isCheck) {
        this.text = text;
        this.isCheck = isCheck;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Boolean getCheck() {
        return isCheck;
    }

    public void setCheck(Boolean check) {
        isCheck = check;
    }
}
