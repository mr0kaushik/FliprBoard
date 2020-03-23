package com.board.flipr.Interfaces;

import com.board.flipr.Model.BoardList;
import com.board.flipr.Model.BoardListCard;
import com.board.flipr.adapters.BoardListCardItemAdapter;

public interface OnListCardClickListener {
    void onBoardListCardClick(BoardListCard card, BoardList boardList);

    void onAddCardClick(BoardListCardItemAdapter boardListCardItemAdapter, BoardList boardListItem);
}
