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
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.board.flipr.Model.User;
import com.board.flipr.Utils.Helper;
import com.board.flipr.Utils.SharedPrefHelper;
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
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;

public class SignUpActivity extends AppCompatActivity implements OnSuccessListener<AuthResult>, OnFailureListener {


    private static final String TAG = "SignUpActivity";
    private TextInputEditText etName;
    private TextInputEditText etEmail;
    private TextInputEditText etPassword;
    private TextInputEditText etConfirmPassword;
    private MaterialButton btnSignUp;
    private boolean isForgetScreen = false;
    private TextInputLayout tilName;
    private TextInputLayout tilEmail;
    private TextInputLayout tilPassword;
    private TextInputLayout tilConfirmPassword;
    private SwipeRefreshLayout swipeRefreshLayout;

    private FirebaseAuth auth;

    private DocumentReference mReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        auth = FirebaseAuth.getInstance();

        isForgetScreen = getIntent().getBooleanExtra(LoginActivity.IS_FORGET_SCREEN, false);
        MaterialTextView tvTitle = findViewById(R.id.tvTitle);
        tvTitle.setText(getResources().getString((isForgetScreen) ? R.string.forget_password_plain_text : R.string.register));
        AppCompatImageView ivBack = findViewById(R.id.ivBack);
        ivBack.setVisibility(View.VISIBLE);

        swipeRefreshLayout = findViewById(R.id.swipeRefresh);
        etName = findViewById(R.id.etName);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        etConfirmPassword = findViewById(R.id.etConfirmPassword);
        btnSignUp = findViewById(R.id.btnSignUp);

        tilName = findViewById(R.id.tilName);
        tilEmail = findViewById(R.id.tilEmail);
        tilPassword = findViewById(R.id.tilPassword);
        tilConfirmPassword = findViewById(R.id.tilConfirmPassword);

        if (isForgetScreen) {
            tvTitle.setTextSize(22);
            tilName.setVisibility(View.GONE);
            tilPassword.setVisibility(View.GONE);
            tilConfirmPassword.setVisibility(View.GONE);
        }

        String str = getIntent().getStringExtra(LoginActivity.KEY_EMAIL_ADDRESS);
        if (str != null && str.length() > 0) {
            etEmail.setText(str);
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
                        if (isForgetScreen) {
                            //FORGET SCREEN
                            btnSignUp.setEnabled(true);
                        } else {
                            //REGISTER SCREEN
                            validate();
                        }
                        tilEmail.setErrorEnabled(false);
                    } else {
                        tilEmail.setErrorEnabled(true);
                        tilEmail.setError("Please enter correct email address");
                        btnSignUp.setEnabled(false);
                    }
                } else {
                    tilEmail.setErrorEnabled(true);
                    etEmail.setError("Email must not empty");
                    btnSignUp.setEnabled(false);
                }
            }
        });
        if (!isForgetScreen) {
            etName.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    if (s.length() >= 3) {
                        validate();
                        tilName.setErrorEnabled(false);
                    } else {
                        tilName.setErrorEnabled(true);
                        tilName.setError("Field must contain at least 3 letters");
                        btnSignUp.setEnabled(false);
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
                        if (etConfirmPassword.getText() != null && etConfirmPassword.getText().length() > 6 && etConfirmPassword.getText().toString().equals(s.toString())) {
                            validate();
                            tilPassword.setErrorEnabled(false);
                            tilConfirmPassword.setErrorEnabled(false);
                        } else {
//                            tilConfirmPassword.setErrorEnabled(true);
//                            tilConfirmPassword.setError("Passwords do not match");
                            btnSignUp.setEnabled(false);
                        }

                    } else {
                        tilPassword.setErrorEnabled(true);
                        tilPassword.setError("Field must contain at least 6 letters");
                        btnSignUp.setEnabled(false);
                    }
                }
            });

            etConfirmPassword.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    if (s != null && s.length() >= 6) {
                        if (etPassword.getText() != null && etPassword.getText().toString().equals(s.toString())) {
                            validate();
                            tilConfirmPassword.setErrorEnabled(false);
                        } else {
                            tilConfirmPassword.setErrorEnabled(true);
                            tilConfirmPassword.setError("Passwords do not match");
                            btnSignUp.setEnabled(false);
                        }
                    }
                }
            });
        }


        btnSignUp.setOnClickListener(v -> {
            if (isForgetScreen)
                setForgetPassword();
            else
                setRegister();
        });

        ((isForgetScreen) ? etEmail : etConfirmPassword).setOnKeyListener((v, keyCode, event) -> {
            if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN) {
                if (btnSignUp.isEnabled())
                    btnSignUp.callOnClick();
            }
            return false;
        });


        ivBack.setOnClickListener(v -> onBackPressed());


    }

    private void setForgetPassword() {
        setToRefresh(true);
        String email = Objects.requireNonNull(etEmail.getText()).toString();
        auth.sendPasswordResetEmail(email).addOnFailureListener(this).addOnSuccessListener(aVoid -> {
            //TODO Show DIALOG BOX
            setToRefresh(false);
            setForgetDialog();
        });
    }

    private void setForgetDialog() {
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(SignUpActivity.this, R.style.AlertDialogStyle);
        builder.setTitle(R.string.reset_password);
        builder.setMessage(R.string.reset_password_email_description);
        builder.setPositiveButton(R.string.go_to_login_page, (dialog, which) -> {
            String email = (etEmail.getText() != null) ? etEmail.getText().toString() : "";
            dialog.dismiss();
            gotoLoginPage(email);
        });
        builder.setCancelable(true);
        AlertDialog dialog = builder.create();
        dialog.show();

    }

    private void gotoLoginPage(String email) {
        Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
        intent.putExtra(LoginActivity.KEY_EMAIL_ADDRESS, email);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_right_out, R.anim.slide_right_in);
        finish();
    }


    private void validate() {
        String name = (etName.getText() != null) ? etName.getText().toString() : null;
        String email = (etEmail.getText() != null) ? etEmail.getText().toString() : null;
        String password = (etPassword.getText() != null) ? etPassword.getText().toString() : null;
        String confirmPassword = (etConfirmPassword.getText() != null) ? etConfirmPassword.getText().toString() : null;

        if (isValidEmail(email) && isValidName(name) && isValidPassword(password, confirmPassword)) {
            tilName.setErrorEnabled(false);
            tilEmail.setErrorEnabled(false);
            tilPassword.setErrorEnabled(false);
            tilConfirmPassword.setErrorEnabled(false);
            btnSignUp.setEnabled(true);
        } else {
            btnSignUp.setEnabled(false);
        }
    }


    public boolean isValidEmail(String email) {
        return email != null && email.length() > 0 && email.matches(Patterns.EMAIL_ADDRESS.pattern());
    }

    public boolean isValidName(String name) {
        return name != null && name.length() >= 3;
    }

    public boolean isValidPassword(String pass, String con) {
        return pass != null && con != null && pass.length() >= 6 && con.length() >= 6 && pass.equals(con);
    }


    private void setRegister() {
        setToRefresh(true);
        String email = Objects.requireNonNull(etEmail.getText()).toString();
        String password = Objects.requireNonNull(etConfirmPassword.getText()).toString();

        auth.createUserWithEmailAndPassword(email, password)
                .addOnSuccessListener(this).addOnFailureListener(this);
    }

    @Override
    public void onFailure(@NonNull Exception exception) {
        setToRefresh(false);
        try {
            throw exception;
        } catch (FirebaseAuthUserCollisionException e) {
            getErrorDialog(getString(R.string.error_title_user_already_exist), getString(R.string.error_msg_user_already_exist), e);
        } catch (FirebaseAuthInvalidUserException e) {
            getErrorDialog(getString(R.string.error_title_user_not_found), getString(R.string.error_msg_user_not_found), e);
        } catch (FirebaseAuthException e) {
            getErrorDialog(getString(R.string.sign_up_error), e.getMessage(), e);
        } catch (FirebaseNetworkException e) {
            getErrorDialog(getString(R.string.error_title_network), getString(R.string.error_msg_network), e);
        } catch (Exception e) {
            getErrorDialog(getString(R.string.login_error), getString(R.string.error_firebase_authentication), e);
        }
    }

    private void getErrorDialog(String title, String msg, Exception e) {
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(SignUpActivity.this, R.style.AlertDialogStyle);
        builder.setTitle(title);
        builder.setMessage(msg);

        if (e instanceof FirebaseAuthInvalidUserException) {
            builder.setPositiveButton(R.string.okay, null);

        } else if (e instanceof FirebaseAuthUserCollisionException) {
            builder.setPositiveButton(R.string.login_page, (dialog, which) -> {
                String email = (etEmail.getText() != null) ? etEmail.getText().toString() : "";
                dialog.dismiss();
                gotoLoginPage(email);

            });
            builder.setNegativeButton(R.string.use_another, null);
            etEmail.requestFocus();
        } else if (e instanceof FirebaseAuthException) {
            builder.setPositiveButton(R.string.ok, null);
        } else if (e instanceof FirebaseNetworkException) {
            builder.setPositiveButton(R.string.retry, (dialog, which) -> {
                dialog.dismiss();
                if (isForgetScreen) {
                    setForgetPassword();
                } else {
                    setRegister();
                }
            });
            builder.setNegativeButton(R.string.cancel, null);
        } else if (e != null) {
            builder.setPositiveButton(R.string.ok, null);
        }

        builder.setCancelable(true);
        AlertDialog dialog = builder.create();
        dialog.show();
    }


    @Override
    public void onSuccess(AuthResult authResult) {

        FirebaseUser firebaseUser = authResult.getUser();
        User user = new User();
        if (firebaseUser != null) {
            user.setId(firebaseUser.getUid());
            user.setName(Objects.requireNonNull(etName.getText()).toString());
            user.setEmail(Objects.requireNonNull(etEmail.getText()).toString());
            user.setColor(Helper.getRandomColor());
        }

        FirebaseFirestore fireStore = FirebaseFirestore.getInstance();
        fireStore.collection(FirebaseConstants.USER_COLLECTION).
                document(firebaseUser.getUid()).set(user).addOnSuccessListener(aVoid -> {
            setToRefresh(false);

            SharedPrefHelper.getInstance(SignUpActivity.this).setUserData(user);
            Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            overridePendingTransition(R.anim.enter, R.anim.exit);
            finish();
        }).addOnFailureListener(this::handleFireBaseException);


    }

    private void handleFireBaseException(Exception e) {
        e.printStackTrace();
    }


    private void setToRefresh(boolean refresh) {
        swipeRefreshLayout.setEnabled(refresh);
        swipeRefreshLayout.setRefreshing(refresh);
        btnSignUp.setEnabled(!refresh);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_right_out, R.anim.slide_right_in);
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

