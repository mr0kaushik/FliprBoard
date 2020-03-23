package com.board.flipr.Model.Items;

import com.board.flipr.Interfaces.ListItem;

public class BoardListAddItem implements ListItem {

    @Override
    public int getItemType() {
        return ListItem.TYPE_ADD_LIST;
    }
}
