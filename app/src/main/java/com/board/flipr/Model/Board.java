package com.board.flipr.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class Board {

    private BoardType boardType;
    @SerializedName("board_id")
    @Expose
    private String board_id;

    @SerializedName("board_color")
    @Expose
    private Integer board_color;

    @SerializedName("board_title")
    @Expose
    private String title;

    @SerializedName("board_lists")
    @Expose
    private List<BoardList> board_list;


    public Board() {
    }

    public Board(BoardType boardType, Integer board_color, String title) {
        this.boardType = boardType;
        this.board_color = board_color;
        this.title = title;
        board_list = new ArrayList<>();
    }

    public Board(BoardType boardType, String board_id, Integer board_color, String title, List<BoardList> board_list) {
        this.boardType = boardType;
        this.board_id = board_id;
        this.board_color = board_color;
        this.title = title;
        this.board_list = board_list;
    }

    public BoardType getBoardType() {
        return boardType;
    }


    public String getBoardId() {
        return board_id;
    }

    public void setBoardId(String board_id) {
        this.board_id = board_id;
    }

    public Integer getBoardColor() {
        return board_color;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<BoardList> getBoardList() {
        return board_list;
    }


    public enum BoardType {
        PRIVATE, TEAM_BOARD;
    }

    public void addListIdToBoard(BoardList boardList){
        if (board_list == null){
            board_list = new ArrayList<>();
        }
        board_list.add(boardList);
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

    public List<BoardList> getBoard_list() {
        return board_list;
    }

    public void setBoard_list(List<BoardList> board_list) {
        this.board_list = board_list;
    }
}
