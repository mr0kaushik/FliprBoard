package com.board.flipr.Model.Items;

import com.board.flipr.Interfaces.ListItem;
import com.board.flipr.Model.BoardList;

import java.io.Serializable;

public class BoardListItem implements ListItem, Serializable {

    private BoardList boardList;

    @Override
    public int getItemType() {
        return ListItem.TYPE_BOARD_LIST;
    }

    public BoardListItem(BoardList boardList) {
        this.boardList = boardList;
    }

    public BoardList getBoardList() {
        return boardList;
    }

    public void setBoardList(BoardList boardList) {
        this.boardList = boardList;
    }
}
