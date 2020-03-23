package com.board.flipr.Model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class BoardList implements Serializable {

    private String id;
    private String title;
    private Integer list_flag_color;
    private List<BoardListCard> boardCardsList;

    public BoardList() {
    }

    public BoardList(String title, Integer list_flag_color) {
        this.title = title;
        this.list_flag_color = list_flag_color;
    }

    public BoardList(String id, String title, Integer list_flag_color, List<BoardListCard> boardCardsList) {
        this.id = id;
        this.title = title;
        this.list_flag_color = list_flag_color;
        this.boardCardsList = boardCardsList;
    }

    public void addCardToList(BoardListCard card) {
        if (boardCardsList == null) {
            boardCardsList = new ArrayList<>();
        }
        boardCardsList.add(card);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getList_flag_color() {
        return list_flag_color;
    }

    public void setList_flag_color(Integer list_flag_color) {
        this.list_flag_color = list_flag_color;
    }

    public List<BoardListCard> getBoardCardsList() {
        return boardCardsList;
    }

    public void setBoardCardsList(List<BoardListCard> boardCardsList) {
        this.boardCardsList = boardCardsList;
    }

}
