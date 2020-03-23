package com.board.flipr.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.RecyclerView;

import com.board.flipr.Interfaces.CardItem;
import com.board.flipr.Interfaces.OnListCardClickListener;
import com.board.flipr.Model.BoardList;
import com.board.flipr.Model.BoardListCard;
import com.board.flipr.Model.Items.BoardListCardItem;
import com.board.flipr.R;
import com.google.android.material.textview.MaterialTextView;

import java.util.List;

public class BoardListCardItemAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    private Context context;
    private List<CardItem> cardItems;
    private OnListCardClickListener listCardClickListener;
    private BoardList boardList;

    BoardListCardItemAdapter(Context context, List<CardItem> cardItems, BoardList boardList, OnListCardClickListener listCardClickListener) {
        this.context = context;
        this.cardItems = cardItems;
        this.listCardClickListener = listCardClickListener;
        this.boardList = boardList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if (viewType == CardItem.TYPE_LIST_CARD) {
            View view = LayoutInflater.from(context).inflate(R.layout.card_item_layout, parent, false);
            return new BoardCardItemViewHolder(view);
        } else if (viewType == CardItem.TYPE_ADD_CARD) {
            View view = LayoutInflater.from(context).inflate(R.layout.add_card_item_layout, parent, false);
            return new AddCardViewHolder(view);
        }
        throw new RuntimeException("No match for " + viewType + ".");
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        CardItem item = cardItems.get(position);
        if (item != null) {

            if (holder instanceof AddCardViewHolder) {
                holder.itemView.setOnClickListener(v -> listCardClickListener.onAddCardClick(this, boardList));
            } else {


                BoardCardItemViewHolder cardHolder = (BoardCardItemViewHolder) holder;
                BoardListCard card = (BoardListCard) item;

                if (card.getTitle() != null && card.getTitle().length() > 0) {
                    cardHolder.tvTitle.setText(card.getTitle());
                    cardHolder.tvTitle.setVisibility(View.VISIBLE);
                }


                if (card.getDescription() != null && card.getDescription().length() > 0) {
                    cardHolder.tvDescription.setText(card.getDescription());
                    cardHolder.tvDescription.setVisibility(View.VISIBLE);
                }

                if (card.getDueTime() != null) {
                    cardHolder.tvDueTime.setText(card.getDueTime().toString());
                    cardHolder.tvDueTime.setVisibility(View.VISIBLE);
                }

                if (card.getAttachmentCount() > 0) {
                    cardHolder.tvAttachment.setText(card.getAttachmentCount());
                    cardHolder.tvAttachment.setVisibility(View.VISIBLE);
                }

                if (card.getCheckedString() != null && card.getCheckedString().length() > 0) {
                    cardHolder.tvCheckbox.setText(card.getCheckedString());
                    cardHolder.tvCheckbox.setVisibility(View.VISIBLE);
                }

                if (card.getColor() != null && card.getColor() > 0) {
                    cardHolder.ivFlag.setBackgroundColor(card.getColor());
                    cardHolder.ivFlag.setVisibility(View.VISIBLE);
                }

                holder.itemView.setOnClickListener(v -> listCardClickListener.onBoardListCardClick(card, boardList));
            }
        }


    }

    @Override
    public int getItemCount() {
        return cardItems != null ? cardItems.size() : 0;
    }

    @Override
    public int getItemViewType(int position) {
        if (cardItems != null && position < cardItems.size())
            return cardItems.get(position).getItemType();
        return 1;
    }

    public void addBoardListCard(BoardListCard boardListCard) {
        BoardListCardItem item = new BoardListCardItem(boardListCard);
        cardItems.add(cardItems.size() - 2, item);
        notifyDataSetChanged();
    }


    class BoardCardItemViewHolder extends RecyclerView.ViewHolder {
        MaterialTextView tvDueTime, tvTitle, tvDescription;
        AppCompatImageView ivFlag, ivMore;
        MaterialTextView tvAttachment;
        MaterialTextView tvCheckbox;


        BoardCardItemViewHolder(@NonNull View itemView) {
            super(itemView);

            tvDueTime = itemView.findViewById(R.id.tvDueTime);

            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvDescription = itemView.findViewById(R.id.tvDescription);
            ivFlag = itemView.findViewById(R.id.ivFlag);
            ivMore = itemView.findViewById(R.id.ivMore);

            tvAttachment = itemView.findViewById(R.id.tvAttachment);
            tvCheckbox = itemView.findViewById(R.id.tvCheckbox);

        }
    }

    class AddCardViewHolder extends RecyclerView.ViewHolder {

        AddCardViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

}
