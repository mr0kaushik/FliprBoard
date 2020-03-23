package com.board.flipr;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.board.flipr.Model.User;
import com.board.flipr.Utils.SharedPrefHelper;
import com.board.flipr.screens.BoardsFragment;
import com.board.flipr.screens.DashboardFragment;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, OnSuccessListener<DocumentSnapshot>, OnFailureListener {

    private static final String TAG = "MainActivity";
    private static final long TIME_DELAY = 2000;
    boolean isPressedTwice = false;

    ActionBarDrawerToggle actionBarDrawerToggle;
    DrawerLayout drawerLayout;
    NavigationView navigationView;

    final Fragment boardsFragment = new BoardsFragment();
    final Fragment dashboardFragment = new DashboardFragment();
    final FragmentManager fm = getSupportFragmentManager();
    Fragment active;

    FirebaseAuth mAuth;
    FirebaseUser mUser;
    FirebaseFirestore fireStore;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.navigation_drawer);

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();

        fireStore = FirebaseFirestore.getInstance();

        active = boardsFragment;
        fm.beginTransaction().add(R.id.frameLayoutForBottomNavigation, boardsFragment, "boards").commit();
        fm.beginTransaction().add(R.id.frameLayoutForBottomNavigation, dashboardFragment, "dashboard").hide(dashboardFragment).commit();

        MaterialToolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Boards");

        //DRAWER LAYOUT
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        FloatingActionButton btnFab = findViewById(R.id.fabActionButton);

        btnFab.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, EditActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_top_in, R.anim.slide_top_out);
        });

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_nav_bar);
        bottomNavigationView.setOnNavigationItemSelectedListener(navListener);
        bottomNavigationView.setSelectedItemId(R.id.menu_boards);
//        Objects.requireNonNull(getSupportActionBar()).setTitle("Boards");
//        bottomNavigationView.setSelectedItemId();
//        bottomNavigationView.transform(btnFab);
//

        // GET USER DATA
        if (!SharedPrefHelper.getInstance(this).isUserLoggedIn()) {
            DocumentReference reference = fireStore.collection(FirebaseConstants.USER_COLLECTION).document(mUser.getUid());
            reference.get().addOnSuccessListener(this).addOnFailureListener(this);
        } else {
            User user = SharedPrefHelper.getInstance(this).getUserData();
            updateUserUI(user);
        }
    }

    private void updateUserUI(User user) {

        NavigationView navigationView = findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);
        CircleImageView cvProfile = headerView.findViewById(R.id.profilePic);
        MaterialTextView tvName = headerView.findViewById(R.id.tvName);
        MaterialTextView tvEmail = headerView.findViewById(R.id.tvEmail);


        if (user.getName() != null && user.getName().length() > 0) {
            tvName.setText(user.getName());
        }
        if (user.getEmail() != null && user.getEmail().length() > 0) {
            tvEmail.setText(user.getEmail());
        }
        if (user.getColor() != null) {
            cvProfile.setCircleBackgroundColor(user.getColor());
        }
    }

    BottomNavigationView.OnNavigationItemSelectedListener navListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
            switch (menuItem.getItemId()) {
                case R.id.menu_dashboard:
                    Objects.requireNonNull(getSupportActionBar()).setTitle("Dashboard");
                    fm.beginTransaction().hide(active).show(dashboardFragment).commit();
                    active = dashboardFragment;
                    return true;

                case R.id.menu_boards:
                    Objects.requireNonNull(getSupportActionBar()).setTitle("Boards");
                    fm.beginTransaction().hide(active).show(boardsFragment).commit();
                    active = boardsFragment;
                    return true;
            }
            return false;
        }
    };

    @Override
    public void onBackPressed() {

        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            if (isPressedTwice) {
                super.onBackPressed();
                return;
            }

            this.isPressedTwice = true;

            View view = findViewById(android.R.id.content);
            Snackbar snackbar = Snackbar.make(view, R.string.press_again, Snackbar.LENGTH_SHORT);
            snackbar.show();

            new Handler().postDelayed(() -> isPressedTwice = false, TIME_DELAY);
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

        switch (menuItem.getItemId()) {

            case R.id.menu_logout:
                logout();
                break;

            case R.id.menu_bugReport:
                report();
//                if (sendReport) {
//                    showSnackBar("Opening Email");
//                }
                break;

            case R.id.menu_about:
                showSnackBar("Will add later.");
                break;
        }
        drawerLayout.closeDrawer(GravityCompat.START);

        return true;
    }

    public void showSnackBar(String msg) {
        View view = findViewById(android.R.id.content);
        Snackbar snackbar = Snackbar.make(view, msg, Snackbar.LENGTH_SHORT);
        snackbar.show();
    }

    private void logout() {
        //TODO LOGOUT

        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(MainActivity.this, R.style.AlertDialogStyle);
        View view = LayoutInflater.from(MainActivity.this).inflate(R.layout.progress_dialog_layout, null);
        MaterialTextView tvTitle = view.findViewById(R.id.tvProgressTitle);
        tvTitle.setText(R.string.logging_off);
        builder.setView(view);
        final AlertDialog dialog = builder.create();
        dialog.show();

        new Handler().postDelayed(() -> {
            mAuth.signOut();
            dialog.dismiss();
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_right_out, R.anim.slide_right_in);
            finish();

        }, TIME_DELAY);


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item != null && item.getItemId() == android.R.id.home) {
            toggle();
        }
        return super.onOptionsItemSelected(item);
    }

    private void toggle() {
        if (drawerLayout.isDrawerVisible(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            drawerLayout.openDrawer(GravityCompat.START);
        }
    }

    private boolean report() {
        Intent Email = new Intent(Intent.ACTION_SEND);
        Email.setType("text/email");
        Email.putExtra(Intent.EXTRA_EMAIL, new String[]{getString(R.string.report_email_id)});
        Email.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.report_email_subject));
        Email.putExtra(Intent.EXTRA_TEXT, getString(R.string.report_email_text));
        startActivity(Intent.createChooser(Email, getString(R.string.report_title)));
        return true;
    }

    @Override
    public void onSuccess(DocumentSnapshot documentSnapshot) {
        User user = documentSnapshot.toObject(User.class);

        if (user != null) {
            SharedPrefHelper.getInstance(this).setUserData(user);
            updateUserUI(user);
        }
    }

    @Override
    public void onFailure(@NonNull Exception e) {
//        // TODO HANDLE EXCEPTION
        e.printStackTrace();
    }
}
