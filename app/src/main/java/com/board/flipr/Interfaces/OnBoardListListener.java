package com.board.flipr.Interfaces;

import com.board.flipr.Model.Board;
import com.board.flipr.adapters.BoardListItemAdapter;

public interface OnBoardListListener {
    void onBoardListClick(ListItem item);
    void onAddListClick(BoardListItemAdapter boardListItemAdapter, Board board);
}
