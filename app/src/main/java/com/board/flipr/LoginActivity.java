package com.board.flipr;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.FirebaseNetworkException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;


public class LoginActivity extends AppCompatActivity implements View.OnClickListener, OnSuccessListener<AuthResult>, OnFailureListener {


    public static final String IS_FORGET_SCREEN = "is_forget_password";
    public static final String KEY_EMAIL_ADDRESS = "email_address";
    private TextInputEditText etEmail;
    private TextInputEditText etPassword;
    private MaterialButton btnLogin, btnSignUp;
    private MaterialTextView tvForgetPassword;
    private MaterialTextView tvError;
    private FirebaseAuth mAuth;
    private TextInputLayout tilEmail;
    private TextInputLayout tilPassword;
    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mAuth = FirebaseAuth.getInstance();

        MaterialTextView tvTitle = findViewById(R.id.tvTitle);
        tvTitle.setText(getResources().getString(R.string.login));

        swipeRefreshLayout = findViewById(R.id.swipeRefresh);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        tilEmail = findViewById(R.id.tilEmail);
        tilPassword = findViewById(R.id.tilPassword);
        tvError = findViewById(R.id.tvError);
        tvForgetPassword = findViewById(R.id.tvForgetPassword);
        btnLogin = findViewById(R.id.btnLogin);
        btnSignUp = findViewById(R.id.btnSignUp);


        Intent intent = getIntent();
        if (intent != null) {
            String email = intent.getStringExtra(KEY_EMAIL_ADDRESS);
            if (email != null && email.length() > 0) {
                etEmail.setText(email);
            }
        }

        etEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s != null && s.length() > 0) {
                    if (s.toString().matches(Patterns.EMAIL_ADDRESS.pattern())) {
                        tilEmail.setErrorEnabled(false);
                        validate();

                    } else {
                        tilEmail.setErrorEnabled(true);
                        tilEmail.setError("Please enter correct email address");
                        btnLogin.setEnabled(false);
                    }
                } else {
                    tilEmail.setErrorEnabled(true);
                    btnLogin.setEnabled(false);
                }
            }
        });

        etPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s != null && s.length() >= 6) {
                    tilPassword.setErrorEnabled(false);
                    validate();
                } else {
                    tilPassword.setErrorEnabled(true);
                    tilPassword.setError("Field must contain at least 6 letters");
                    btnLogin.setEnabled(false);
                }
            }
        });

        etPassword.setOnKeyListener((v, keyCode, event) -> {
            if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN) {
                if (btnLogin.isEnabled())
                    btnLogin.callOnClick();
            }
            return false;
        });

        btnLogin.setOnClickListener(this);
        btnSignUp.setOnClickListener(this);
        tvForgetPassword.setOnClickListener(this);


    }

    private void validate() {
        String email = (etEmail.getText() != null) ? etEmail.getText().toString() : null;
        String password = (etPassword.getText() != null) ? etPassword.getText().toString() : null;

        if (isValidEmail(email) && isValidPassword(password)) {
            tilEmail.setErrorEnabled(false);
            tilPassword.setErrorEnabled(false);
            btnLogin.setEnabled(true);
        } else {
            btnLogin.setEnabled(false);
        }
    }

    public boolean isValidEmail(String email) {
        return email != null && email.length() > 0 && email.matches(Patterns.EMAIL_ADDRESS.pattern());
    }

    public boolean isValidPassword(String pass) {
        return pass != null && pass.length() >= 6;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnLogin:
                setLogin();
                break;
            case R.id.btnSignUp:
                setNext(false, null);
                break;
            case R.id.tvForgetPassword:
                setNext(true, null);
                break;
        }
    }

    private void setNext(boolean isForgetScreen, String email) {
        Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
        intent.putExtra(IS_FORGET_SCREEN, isForgetScreen);
        if (email != null && email.length() > 0) {
            intent.putExtra(KEY_EMAIL_ADDRESS, email);
        }
        startActivity(intent);
        overridePendingTransition(R.anim.enter, R.anim.exit);
    }


    private void setLogin() {
        String email = Objects.requireNonNull(etEmail.getText()).toString();
        String password = Objects.requireNonNull(etPassword.getText()).toString();
        setToRefresh(true);
        mAuth.signInWithEmailAndPassword(email, password).addOnSuccessListener(this).addOnFailureListener(this);
    }

    @Override
    public void onFailure(@NonNull Exception exception) {
        setToRefresh(false);
        try {
            throw exception;
        } catch (FirebaseAuthInvalidUserException e) {
            getErrorDialog(getString(R.string.error_title_user_not_found), getString(R.string.error_msg_user_not_found), e);
        } catch (FirebaseAuthInvalidCredentialsException e) {
            getErrorDialog(getString(R.string.error_title_invalid_credentials), getString(R.string.error_msg_invalid_credentials), e);
        } catch (FirebaseAuthException e) {
            getErrorDialog(getString(R.string.sign_up_error), e.getMessage(), e);
        } catch (FirebaseNetworkException e) {
            getErrorDialog(getString(R.string.error_title_network), getString(R.string.error_msg_network), e);
        } catch (Exception e) {
            getErrorDialog(getString(R.string.login_error), getString(R.string.error_firebase_authentication), e);
        }

    }

    @Override
    public void onSuccess(AuthResult authResult) {
        FirebaseUser user = authResult.getUser();
        setToRefresh(false);
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        overridePendingTransition(R.anim.enter, R.anim.exit);
        finish();
    }


    private void setToRefresh(boolean refresh) {
        swipeRefreshLayout.setEnabled(refresh);
        swipeRefreshLayout.setRefreshing(refresh);
        btnLogin.setEnabled(!refresh);
        tvForgetPassword.setOnClickListener(!refresh ? null : this);
        btnSignUp.setEnabled(!refresh);
    }

    private void getErrorDialog(String title, String msg, Exception e) {
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(LoginActivity.this, R.style.AlertDialogStyle);
        builder.setTitle(title);
        builder.setMessage(msg);
        String email = (etEmail.getText() != null) ? etEmail.getText().toString() : "";

        if (e instanceof FirebaseAuthInvalidUserException) {
            builder.setPositiveButton(R.string.continue_text, (dialog, which) -> {
                dialog.dismiss();
                setNext(false, email);
            });
            builder.setNegativeButton(R.string.no_thanks, null);

        } else if (e instanceof FirebaseAuthInvalidCredentialsException) {
            builder.setPositiveButton(R.string.ok, null);
            etPassword.requestFocus();
        } else if (e instanceof FirebaseAuthException) {
            builder.setPositiveButton(R.string.ok, null);
        } else if (e instanceof FirebaseNetworkException) {
            builder.setPositiveButton(R.string.retry, (dialog, which) -> {
                dialog.dismiss();
                setLogin();
            });
            builder.setNegativeButton(R.string.cancel, null);
        } else if (e != null) {
            builder.setPositiveButton(R.string.ok, null);
        }

        builder.setCancelable(true);
        builder.show();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (getCurrentFocus() != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            assert imm != null;
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
        return super.dispatchTouchEvent(ev);
    }
}

