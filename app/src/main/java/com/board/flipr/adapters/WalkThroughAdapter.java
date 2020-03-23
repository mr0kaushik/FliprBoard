package com.board.flipr.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewpager.widget.PagerAdapter;

import com.board.flipr.R;
import com.google.android.material.textview.MaterialTextView;

public class WalkThroughAdapter extends PagerAdapter {

    private Context context;
    private int[] slideImages = {
            R.drawable.wt_1,
            R.drawable.wt_2,
            R.drawable.wt_1
    };
    private int[] slideHeading = {
            R.string.walk_through_heading_1,
            R.string.walk_through_heading_2,
            R.string.walk_through_heading_3
    };

    private int[] slidePrimaryText = {
            R.string.walk_through_primary_1,
            R.string.walk_through_primary_2,
            R.string.walk_through_primary_3
    };

    private int[] slideSecondaryText = {
            R.string.walk_through_secondary_1,
            R.string.walk_through_secondary_2,
            R.string.walk_through_secondary_3
    };

    public WalkThroughAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return slideHeading.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        assert layoutInflater != null;
        View view = layoutInflater.inflate(R.layout.walk_through_layout, container, false);

        AppCompatImageView imageView = view.findViewById(R.id.ivWalkThrough);
        MaterialTextView tvHeading = view.findViewById(R.id.tvTitle);
        MaterialTextView tvPrimary = view.findViewById(R.id.tvPrimary);
        MaterialTextView tvSecondary = view.findViewById(R.id.tvSecondary);

        imageView.setImageResource(slideImages[position]);
        tvHeading.setText(slideHeading[position]);
        tvPrimary.setText(slidePrimaryText[position]);
        tvSecondary.setText(slideSecondaryText[position]);

        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((ConstraintLayout) object);
    }


}
