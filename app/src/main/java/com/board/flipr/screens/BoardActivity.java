package com.board.flipr.screens;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.board.flipr.FirebaseConstants;
import com.board.flipr.Interfaces.ListItem;
import com.board.flipr.Interfaces.OnBoardListListener;
import com.board.flipr.Interfaces.OnListCardClickListener;
import com.board.flipr.Model.Board;
import com.board.flipr.Model.BoardList;
import com.board.flipr.Model.BoardListCard;
import com.board.flipr.Model.Items.BoardListAddItem;
import com.board.flipr.Model.Items.BoardListItem;
import com.board.flipr.Model.UserBoard;
import com.board.flipr.R;
import com.board.flipr.Utils.Helper;
import com.board.flipr.Utils.IntentConstants;
import com.board.flipr.adapters.BoardListCardItemAdapter;
import com.board.flipr.adapters.BoardListItemAdapter;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.FirebaseNetworkException;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.ArrayList;
import java.util.List;

public class BoardActivity extends AppCompatActivity implements OnBoardListListener, OnListCardClickListener, OnSuccessListener<DocumentSnapshot>, OnFailureListener {

    private RecyclerView rvBoardLists;
    private SwipeRefreshLayout swipeRefreshLayout;

    private List<ListItem> listItems = new ArrayList<>();
    private Board board;

    private BoardListItemAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board);
        MaterialToolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        UserBoard userBoard = (UserBoard) getIntent().getSerializableExtra(IntentConstants.KEY_USER_BOARD);
        if (userBoard != null) {
            toolbar.setTitle(userBoard.getTitle());
            getBoardList(userBoard.getBoard_id());
        }

        listItems = new ArrayList<>();
        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);
        rvBoardLists = findViewById(R.id.rvBoardLists);
        rvBoardLists.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        rvBoardLists.setHasFixedSize(true);
        rvBoardLists.setItemAnimator(new DefaultItemAnimator());
        adapter = new BoardListItemAdapter(this, listItems, board, this, this);
        rvBoardLists.setAdapter(adapter);
//        setBoardsList(board);


    }

    private void getBoardList(String board_id) {
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        firestore.collection(FirebaseConstants.BOARDS_COLLECTION).document(board_id)
                .get().addOnSuccessListener(this)
                .addOnFailureListener(this);
    }

    public void setBoardsList(Board board) {

        if (this.board != null && this.board.getBoardList() != null && this.board.getBoardList().size() > 0) {
            for (BoardList list : this.board.getBoardList()) {
                listItems.add(new BoardListItem(list));
            }
        }
        listItems.add(new BoardListAddItem());
        adapter.notifyDataSetChanged();

    }


    private void setToRefresh(boolean refresh) {
        swipeRefreshLayout.setEnabled(refresh);
        swipeRefreshLayout.setRefreshing(refresh);
    }


    @Override
    public void onBoardListClick(ListItem item) {

    }

    @Override
    public void onAddListClick(BoardListItemAdapter boardListItemAdapter, Board board) {
        if (board != null) {
            BoardList list = new BoardList("List Title", Helper.getRandomColor());
            board.addListIdToBoard(list);
            boardListItemAdapter.addBoardList(list);
        }
    }


    @Override
    public void onBoardListCardClick(BoardListCard card, BoardList boardList) {

    }

    @Override
    public void onAddCardClick(BoardListCardItemAdapter boardListCardItemAdapter, BoardList boardListItem) {
        BoardListCard card = new BoardListCard("Card Title", Helper.getRandomColor());
        boardListItem.addCardToList(card);
        boardListCardItemAdapter.addBoardListCard(card);
    }

    @Override
    public void onSuccess(DocumentSnapshot documentSnapshot) {
        if (documentSnapshot != null) {
            board = documentSnapshot.toObject(Board.class);
            setBoardsList(board);
            adapter.setBoard(board);
        }
    }


    public void handleException(Exception exception) {
        if (swipeRefreshLayout.isRefreshing()) {
            setToRefresh(false);
        }
        if (exception instanceof FirebaseFirestoreException) {
            exception.printStackTrace();
        } else if (exception instanceof FirebaseNetworkException) {
            getErrorDialog(getString(R.string.error_title_network), getString(R.string.error_msg_network), exception);
        } else {
            getErrorDialog(getString(R.string.login_error), getString(R.string.error_firebase_authentication), exception);
        }
    }

    @Override
    public void onFailure(@NonNull Exception exception) {
        handleException(exception);
    }

    private void getErrorDialog(String title, String msg, Exception e) {
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(BoardActivity.this, R.style.AlertDialogStyle);
        builder.setTitle(title);
        builder.setMessage(msg);

        if (e instanceof FirebaseAuthInvalidUserException) {
            builder.setPositiveButton(R.string.continue_text, (dialog, which) -> {
                dialog.dismiss();
            });
            builder.setNegativeButton(R.string.no_thanks, null);

        } else if (e instanceof FirebaseAuthInvalidCredentialsException) {
            builder.setPositiveButton(R.string.ok, null);
        } else if (e instanceof FirebaseAuthException) {
            builder.setPositiveButton(R.string.ok, null);
        } else if (e instanceof FirebaseNetworkException) {
            builder.setPositiveButton(R.string.retry, (dialog, which) -> {
                dialog.dismiss();
            });
            builder.setNegativeButton(R.string.cancel, null);
        } else if (e != null) {
            builder.setPositiveButton(R.string.ok, null);
        }

        builder.setCancelable(true);
        builder.show();
    }
}
