package com.board.flipr;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.board.flipr.Model.Board;
import com.board.flipr.Model.UserBoard;
import com.board.flipr.Utils.IntentConstants;
import com.board.flipr.screens.BoardActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.android.material.textview.MaterialAutoCompleteTextView;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.FirebaseNetworkException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.Objects;

public class EditActivity extends AppCompatActivity implements View.OnClickListener, OnSuccessListener<DocumentReference>, OnFailureListener {

    private MaterialAutoCompleteTextView autoCompleteTextView;
    private Board.BoardType type = Board.BoardType.PRIVATE;

    private TextInputEditText etTitle;
    private TextInputLayout tilTitle, tilDropDown;

    private MaterialButton btnCancel, btnCreate;
    private ChipGroup colorGroup;
    private Integer selectedColor;
    private SwipeRefreshLayout swipeRefreshLayout;


    FirebaseFirestore fireStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        fireStore = FirebaseFirestore.getInstance();

        MaterialTextView tvTitle = findViewById(R.id.tvTitle);
        tvTitle.setText(getResources().getString(R.string.create_board));
        tvTitle.setTextSize(18);
        AppCompatImageView ivBack = findViewById(R.id.ivBack);
        ivBack.setVisibility(View.VISIBLE);

        ivBack.setOnClickListener(this);

        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);
        tilTitle = findViewById(R.id.tilTitle);
        tilDropDown = findViewById(R.id.tilDropDown);
        etTitle = findViewById(R.id.etTitle);
        autoCompleteTextView = findViewById(R.id.actvTypes);
        btnCreate = findViewById(R.id.btnCreate);
        btnCancel = findViewById(R.id.btnCancel);
        colorGroup = findViewById(R.id.colorLayout);

        btnCreate.setOnClickListener(this);
        btnCancel.setOnClickListener(this);

        etTitle.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                if (s.length() > 0) {
                    btnCreate.setEnabled(true);
                    tilTitle.setErrorEnabled(false);
                } else {
                    btnCreate.setEnabled(false);
                    tilTitle.setError("Title must not be empty");
                    tilTitle.setErrorEnabled(true);
                }
            }
        });

        setUpDropDown();
        setUpColors();

    }

    private void setUpColors() {

        int[] colors = getResources().getIntArray(R.array.MatColorArray);
        if (colors.length > 0) {
            selectedColor = colors[0];
        }
        final LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(60, 60);

        for (int i = 0; i < colors.length; i++) {
            Chip chip = new Chip(this);
            chip.setId(i);
            chip.setChipStartPadding(0);
            chip.setChipEndPadding(0);
            chip.setIconStartPadding(4);
            chip.setIconEndPadding(0);
            chip.setPadding(0, 8, 0, 0);
            chip.setCheckable(true);
            chip.setEnsureMinTouchTargetSize(false);
            chip.setLayoutParams(params);
            chip.setChipBackgroundColor(ColorStateList.valueOf(colors[i]));
            colorGroup.addView(chip);
        }


        colorGroup.setOnCheckedChangeListener((chipGroup, i) -> {
            Chip chip = chipGroup.findViewById(i);
            if (chip != null) {
                chip.setCheckedIconVisible(true);
                selectedColor = colors[i];
            }
        });

        if (colorGroup.getChildCount() > 0) {
            colorGroup.check(0);
        }

    }

    private void setUpDropDown() {
        String[] types = new String[]{"Private Board", "Team Board"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.drop_down_menu_item, types);
        autoCompleteTextView.setAdapter(adapter);
        autoCompleteTextView.setKeyListener(null);

        autoCompleteTextView.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                type = Board.BoardType.values()[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        autoCompleteTextView.setSelection(0);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnCancel:
            case R.id.ivBack:
                onBackPressed();
                break;
            case R.id.btnCreate:
                createBoard();
                break;
        }
    }

    private Board board = null;

    private void createBoard() {
        setToRefresh(true);
        board = new Board(type, selectedColor, Objects.requireNonNull(etTitle.getText()).toString());
        fireStore = FirebaseFirestore.getInstance();
        fireStore.collection(FirebaseConstants.BOARDS_COLLECTION)
                .add(board)
                .addOnSuccessListener(this)
                .addOnFailureListener(this);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_right_out, R.anim.slide_right_in);
    }

    @Override
    public void onSuccess(DocumentReference documentReference) {
        String board_id = documentReference.getId();
        if (board != null) {
            board.setBoardId(documentReference.getId());
            UserBoard userBoard = new UserBoard(board.getBoardType(), board.getBoardId(), board.getBoardColor(), board.getTitle());
            if (FirebaseAuth.getInstance().getCurrentUser() != null) {
                String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                DocumentReference reference = fireStore.collection(FirebaseConstants.USER_COLLECTION).document(userId);

                reference.update(FirebaseConstants.USER_BOARDS_ARRAY, FieldValue.arrayUnion(userBoard))
                        .addOnSuccessListener(aVoid -> {
                            setToRefresh(false);
                            Intent intent = new Intent(EditActivity.this, BoardActivity.class);
                            intent.putExtra(FirebaseConstants.KEY_BOARD_ID, board_id);
                            intent.putExtra(IntentConstants.KEY_USER_BOARD, userBoard);
                            startActivity(intent);
                            overridePendingTransition(R.anim.enter, R.anim.exit);
                            finish();
                        })
                        .addOnFailureListener(this::handleException);
            }
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
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(EditActivity.this, R.style.AlertDialogStyle);
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


    private void setToRefresh(boolean refresh) {
        swipeRefreshLayout.setEnabled(refresh);
        swipeRefreshLayout.setRefreshing(refresh);
        btnCreate.setEnabled(!refresh);
    }
}

