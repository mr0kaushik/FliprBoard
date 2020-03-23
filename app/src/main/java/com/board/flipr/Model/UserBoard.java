package com.board.flipr.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class UserBoard implements Serializable {

    @SerializedName("board_type")
    @Expose
    private Board.BoardType board_type;
    @SerializedName("board_id")
    @Expose
    private String board_id;

    @SerializedName("board_color")
    @Expose
    private Integer board_color;

    @SerializedName("board_title")
    @Expose
    private String title;

    public UserBoard() {
    }

    public Board.BoardType getBoardType() {
        return board_type;
    }

    public void setBoardType(Board.BoardType boardType) {
        this.board_type = boardType;
    }

    public String getBoard_id() {
        return board_id;
    }

    public void setBoard_id(String board_id) {
        this.board_id = board_id;
    }

    public Integer getBoard_color() {
        return board_color;
    }

    public void setBoard_color(Integer board_color) {
        this.board_color = board_color;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public UserBoard(Board.BoardType boardType, String board_id, Integer board_color, String title) {
        this.board_type = boardType;
        this.board_id = board_id;
        this.board_color = board_color;
        this.title = title;
    }


}
