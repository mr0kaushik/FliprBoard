package com.board.flipr.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.board.flipr.Interfaces.CardItem;
import com.board.flipr.Interfaces.ListItem;
import com.board.flipr.Interfaces.OnBoardListListener;
import com.board.flipr.Interfaces.OnListCardClickListener;
import com.board.flipr.Model.Board;
import com.board.flipr.Model.BoardList;
import com.board.flipr.Model.BoardListCard;
import com.board.flipr.Model.Items.BoardListCardAddItem;
import com.board.flipr.Model.Items.BoardListCardItem;
import com.board.flipr.Model.Items.BoardListItem;
import com.board.flipr.R;
import com.google.android.material.textview.MaterialTextView;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class BoardListItemAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    private Context context;
    private List<ListItem> listItems;
    private OnBoardListListener listListener;
    private OnListCardClickListener cardClickListener;
    private Board board;

    public BoardListItemAdapter(Context context, List<ListItem> listItems, Board board, OnBoardListListener listListener, OnListCardClickListener cardClickListener) {
        this.context = context;
        this.listItems = listItems;
        this.listListener = listListener;
        this.cardClickListener = cardClickListener;
        this.board = board;
    }

    public void setBoard(Board board) {
        this.board = board;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if (viewType == ListItem.TYPE_BOARD_LIST) {
            View view = LayoutInflater.from(context).inflate(R.layout.board_list_item_layout, parent, false);
            return new BoardListItemViewHolder(view);
        } else if (viewType == ListItem.TYPE_ADD_LIST) {
            View view = LayoutInflater.from(context).inflate(R.layout.add_card_item_layout, parent, false);
            return new AddListViewHolder(view);
        }
        throw new RuntimeException("No match for " + viewType + ".");
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        ListItem item = listItems.get(position);
        if (item != null) {

            if (holder instanceof AddListViewHolder) {
                holder.itemView.setOnClickListener(v -> {
                    listListener.onAddListClick(this, board);
                });
            } else {

                BoardListItemViewHolder listItemViewHolder = (BoardListItemViewHolder) holder;
                BoardListItem listItem = (BoardListItem) item;
                BoardList list = listItem.getBoardList();

                if (list.getTitle() != null && list.getTitle().length() > 0) {
                    listItemViewHolder.tvTitle.setText(list.getTitle());
                    listItemViewHolder.tvTitle.setVisibility(View.VISIBLE);
                }


                if (list.getList_flag_color() != null && list.getList_flag_color() > 0) {
                    listItemViewHolder.ivFlag.setBackgroundColor(list.getList_flag_color());
                    listItemViewHolder.ivFlag.setVisibility(View.VISIBLE);
                }

                if (list.getBoardCardsList() != null) {
                    setRecyclerView(listItemViewHolder.rvCards, list);
                }

            }
        }


    }

    private void setRecyclerView(RecyclerView recyclerView, BoardList boardList) {
        final LinearLayoutManager llm = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(llm);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setHasFixedSize(true);

        List<CardItem> items = new ArrayList<>();
        if (boardList != null && boardList.getBoardCardsList() != null && boardList.getBoardCardsList().size() > 0) {
            for (BoardListCard card : boardList.getBoardCardsList()) {
                items.add(new BoardListCardItem(card));
            }
        }

        items.add(new BoardListCardAddItem());
        BoardListCardItemAdapter adapter = new BoardListCardItemAdapter(context, items, boardList, cardClickListener);
        recyclerView.setAdapter(adapter);

    }

    public void addBoardList(BoardList boardList) {
        BoardListItem item = new BoardListItem(boardList);
        listItems.add(listItems.size() - 1, item);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return listItems != null ? listItems.size() : 0;
    }

    @Override
    public int getItemViewType(int position) {
        if (listItems != null && position < listItems.size())
            return listItems.get(position).getItemType();
        return 1;
    }


    class BoardListItemViewHolder extends RecyclerView.ViewHolder {
        MaterialTextView tvTitle;
        AppCompatImageView ivMore;
        CircleImageView ivFlag;
        RecyclerView rvCards;

        BoardListItemViewHolder(@NonNull View itemView) {
            super(itemView);


            tvTitle = itemView.findViewById(R.id.tvListTitle);
            ivFlag = itemView.findViewById(R.id.ivFlag);
            ivMore = itemView.findViewById(R.id.ivMore);
            rvCards = itemView.findViewById(R.id.rvCards);


        }
    }

    class AddListViewHolder extends RecyclerView.ViewHolder {

        AddListViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

}
