package com.board.flipr;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.viewpager.widget.ViewPager;

import com.board.flipr.Utils.SharedPrefHelper;
import com.board.flipr.adapters.WalkThroughAdapter;
import com.google.android.material.button.MaterialButton;

import ru.tinkoff.scrollingpagerindicator.ScrollingPagerIndicator;

public class WalkThroughActivity extends AppCompatActivity implements View.OnClickListener {

    private ViewPager viewPager;
    private AppCompatImageView ivLeft, ivRight;
    private int currentPage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_walk_through);


        viewPager = findViewById(R.id.viewPager);
        ivLeft = findViewById(R.id.ivLeft);
        ivRight = findViewById(R.id.ivRight);

        MaterialButton btnGotIt = findViewById(R.id.btnGotIt);

        ivLeft.setOnClickListener(this);
        ivRight.setOnClickListener(this);
        btnGotIt.setOnClickListener(this);


        WalkThroughAdapter adapter = new WalkThroughAdapter(this);
        viewPager.setAdapter(adapter);
        ScrollingPagerIndicator indicator = findViewById(R.id.vpIndicator);
        indicator.attachToPager(viewPager);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                currentPage = position;
                if (currentPage == 0) {
                    ivLeft.setVisibility(View.GONE);
                    ivRight.setVisibility(View.VISIBLE);
                } else if (currentPage == viewPager.getChildCount()) {
                    ivLeft.setVisibility(View.VISIBLE);
                    ivRight.setVisibility(View.GONE);
                } else {
                    ivLeft.setVisibility(View.VISIBLE);
                    ivRight.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


    }

    private void setVisited() {

        SharedPrefHelper.getInstance(getApplicationContext())
                .setVisitedWalkThrough(true);
        Intent intent = new Intent(WalkThroughActivity.this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ivLeft:
                if (currentPage > 0) {
                    viewPager.setCurrentItem(--currentPage);
                }
                break;
            case R.id.ivRight:
                if (viewPager.getAdapter() != null && currentPage < viewPager.getAdapter().getCount()) {
                    viewPager.setCurrentItem(++currentPage);
                }
                break;
            case R.id.btnGotIt:
                if (currentPage < viewPager.getChildCount()) {
                    viewPager.setCurrentItem(++currentPage);
                } else {
                    setVisited();
                }
                break;
        }
    }
}
