package com.board.flipr.Utils;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.ColorInt;
import androidx.annotation.Dimension;

import com.board.flipr.R;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.resources.TextAppearance;
import com.google.android.material.textfield.TextInputEditText;

public class CardViewWithEditable extends MaterialCardView {


    private float mCardRadius;
    private int mCardColor = Color.WHITE;
    private int mTextColor = Color.BLACK;
    private float mTextSize;
    private String mText = "";

    private TextAppearance textAppearance;

    private TextInputEditText editText;
    private Context context;
    private MaterialCardView cardView;

    private View view;

    public CardViewWithEditable(Context context) {
        super(context);
        init(context, null, 0);
    }

    public CardViewWithEditable(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0);
    }

    public CardViewWithEditable(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    public void init(Context context, AttributeSet attrs, int defStyleAtt) {
        LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (mInflater != null) {
            view = mInflater.inflate(R.layout.card_view_with_edit_text, this, true);
        }
        this.context = context;

        if (view != null) {
            editText = view.findViewById(R.id.editText);
            cardView = view.findViewById(R.id.cardView);
        }

        mCardRadius = dpToPx(8);
        mTextSize = dpToPx(16);

        if (attrs != null) {

            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CardViewWithEditable, defStyleAtt, 0);

            mCardRadius = a.getDimensionPixelSize(R.styleable.CardViewWithEditable_cardRadius, dpToPx(8));
            mCardColor = a.getColor(R.styleable.CardViewWithEditable_cardColor, Color.WHITE);
            mTextColor = a.getColor(R.styleable.CardViewWithEditable_textColor, Color.BLACK);
            mTextSize = a.getDimensionPixelSize(R.styleable.CardViewWithEditable_textSize, dpToPx(16));
            mText = a.getString(R.styleable.CardViewWithEditable_text);

            a.recycle();
        }
        init();

        invalidate();

    }

    private void init() {
        setText(mText);
        setTextColor(mTextColor);
        setTextSize(mTextSize);
        setCardColor(mCardColor);
        setCornerRadius(mCardRadius);
    }

    private void setText(String str){
        if(editText!=null){
            editText.setText(mText);
        }
    }



    public int dpToPx(float dp) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        return Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
    }

    public void setCardColor(@ColorInt int color) {
        mCardColor = color;
        if (cardView != null) {
            cardView.setCardBackgroundColor(color);
        }
        invalidate();
    }


    public void setTextColor(@ColorInt int color) {
        mTextColor = color;
        if (editText != null) {
            editText.setTextColor(color);
        }
        invalidate();
    }


    public void setCornerRadius(@Dimension float dp) {
        mCardRadius = dpToPx(dp);
        if (cardView != null) {
            cardView.setRadius(mCardRadius);
        }
        invalidate();
    }


    public void setTextSize(@Dimension float dp) {
        mTextSize = dpToPx(dp);
        if (editText != null) {
            editText.setTextSize(mTextSize);
        }
        invalidate();
    }

}