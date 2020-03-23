package com.board.flipr.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.board.flipr.Interfaces.OnBoardClickListener;
import com.board.flipr.Model.Board;
import com.board.flipr.Model.UserBoard;
import com.board.flipr.R;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.textview.MaterialTextView;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class BoardAdapter extends RecyclerView.Adapter<BoardAdapter.CardViewHolder> {

    private Context context;
    private List<UserBoard> boardList;
    private OnBoardClickListener boardClickListener;

    public BoardAdapter(Context context, List<UserBoard> boardList, OnBoardClickListener boardClickListener) {
        this.context = context;
        this.boardList = boardList;
        this.boardClickListener = boardClickListener;
    }

    @NonNull
    @Override
    public CardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.board_item_layout, parent, false);
        return new CardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CardViewHolder holder, int position) {
        UserBoard model = boardList.get(position);
        if (model != null) {

            holder.tvBoardTitle.setText(model.getTitle());

            holder.cvBoard.setCardBackgroundColor(model.getBoard_color());

            if (boardClickListener != null) {
                holder.itemView.setOnClickListener(v -> boardClickListener.onBoardClick(model));
            }

        }
    }


    @Override
    public int getItemCount() {
        return (boardList != null) ? boardList.size() : 0;
    }

    class CardViewHolder extends RecyclerView.ViewHolder {
        MaterialTextView tvBoardTitle;
        MaterialCardView cvBoard;


        CardViewHolder(@NonNull View itemView) {
            super(itemView);

            tvBoardTitle = itemView.findViewById(R.id.tvBoardTitle);
            cvBoard = itemView.findViewById(R.id.cvBoard);

        }
    }

    public void addAllBoards(List<UserBoard> boards){
        if(boardList==null){
            boardList = new ArrayList<>();
        }
        boardList.addAll(boards);
        notifyDataSetChanged();
    }


}
