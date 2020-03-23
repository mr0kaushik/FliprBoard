package com.board.flipr.screens;


import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.board.flipr.FirebaseConstants;
import com.board.flipr.Interfaces.OnBoardClickListener;
import com.board.flipr.Model.User;
import com.board.flipr.Model.UserBoard;
import com.board.flipr.R;
import com.board.flipr.adapters.BoardAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.FirebaseNetworkException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class TeamFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener, OnCompleteListener<DocumentSnapshot> {


    TeamFragment(OnBoardClickListener onBoardClickListener) {
        this.boardClickListener = onBoardClickListener;
    }

    private OnBoardClickListener boardClickListener;

    private View fragmentView;
    private Context context;

    private RecyclerView rvTeamBoard;
    private SwipeRefreshLayout refreshLayout;

    private LinearLayout llNoBoard;
    private BoardAdapter adapter;
    private List<UserBoard> boards = new ArrayList<>();

    private FirebaseFirestore fireStore;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        fragmentView = inflater.inflate(R.layout.fragment_team, container, false);
        refreshLayout = fragmentView.findViewById(R.id.swipeRefreshLayout);
        rvTeamBoard = fragmentView.findViewById(R.id.rvTeamBoard);
        llNoBoard = fragmentView.findViewById(R.id.noBoardFound);
        refreshLayout.setOnRefreshListener(this);

        fireStore = FirebaseFirestore.getInstance();


        adapter = new BoardAdapter(context, boards, boardClickListener);
        rvTeamBoard.setLayoutManager(new LinearLayoutManager(context));
        rvTeamBoard.setHasFixedSize(true);
        rvTeamBoard.setItemAnimator(new DefaultItemAnimator());

        getBoards();

        return fragmentView;
    }

    private void getBoards() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            fireStore.collection(FirebaseConstants.USER_COLLECTION).document(user.getUid())
                    .get().addOnCompleteListener(this);
        }
    }

    private void setToRefresh(boolean refresh) {
        refreshLayout.setEnabled(refresh);
        refreshLayout.setRefreshing(refresh);
    }

    @Override
    public void onRefresh() {

    }

    @Override
    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
        if (task.isSuccessful()) {
            DocumentSnapshot snapshot = task.getResult();
            if (snapshot != null && snapshot.contains(FirebaseConstants.USER_BOARDS_ARRAY)) {
//                UserBoard[] boardIds = snapshot.get(FirebaseConstants.USER_BOARDS_ARRAY, UserBoard[].class);
//                if (boardIds != null) {
//                    adapter.addAllBoards(Arrays.asList(boardIds));
//                }
//                User user = snapshot.toObject(User.class);
            }
        } else {
            handleException(task.getException());
        }
    }

    private void handleException(Exception exception) {
        if (refreshLayout.isRefreshing()) {
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


    private void getErrorDialog(String title, String msg, Exception e) {
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(context, R.style.AlertDialogStyle);
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
