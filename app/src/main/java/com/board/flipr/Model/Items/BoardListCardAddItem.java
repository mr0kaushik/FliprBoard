package com.board.flipr.Model.Items;

import com.board.flipr.Interfaces.CardItem;

public class BoardListCardAddItem implements CardItem {

    @Override
    public int getItemType() {
        return CardItem.TYPE_ADD_CARD;
    }
}
