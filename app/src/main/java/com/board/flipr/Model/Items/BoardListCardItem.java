package com.board.flipr.Model.Items;

import com.board.flipr.Interfaces.CardItem;
import com.board.flipr.Interfaces.ListItem;
import com.board.flipr.Model.BoardListCard;

import java.io.Serializable;

public class BoardListCardItem implements CardItem, Serializable {

    private BoardListCard boardListCard;

    @Override
    public int getItemType() {
        return ListItem.TYPE_BOARD_LIST;
    }

    public BoardListCardItem(BoardListCard boardListCard) {
        this.boardListCard = boardListCard;
    }

    public BoardListCard getBoardListCard() {
        return boardListCard;
    }

    public void setBoardListCard(BoardListCard boardListCard) {
        this.boardListCard = boardListCard;
    }
}
