package com.board.flipr.screens;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.board.flipr.Interfaces.OnBoardClickListener;
import com.board.flipr.Model.Board;
import com.board.flipr.Model.UserBoard;
import com.board.flipr.R;
import com.board.flipr.Utils.IntentConstants;
import com.board.flipr.adapters.SectionPagerAdapter;
import com.google.android.material.tabs.TabLayout;


public class BoardsFragment extends Fragment implements OnBoardClickListener {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private Context context;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View boardView = inflater.inflate(R.layout.fragment_boards, container, false);

        TabLayout tabLayout = boardView.findViewById(R.id.tabLayout);
        ViewPager viewPager = boardView.findViewById(R.id.viewPager);
        tabLayout.setupWithViewPager(viewPager);

        SectionPagerAdapter adapter = new SectionPagerAdapter(getChildFragmentManager());

        Fragment privateBoard = new PrivateFragment(this);
        Fragment teamFragment = new TeamFragment(this);
        adapter.addFragment(privateBoard);
        adapter.addFragment(teamFragment);
        viewPager.setAdapter(adapter);


        return boardView;

    }


    @Override
    public void onBoardClick(UserBoard board) {
        Intent intent = new Intent(context, BoardActivity.class);
        intent.putExtra(IntentConstants.KEY_BOARD_ID, board.getBoard_id());
        intent.putExtra(IntentConstants.KEY_USER_BOARD, board);
        startActivity(intent);
        if (getActivity() != null) {
            getActivity().overridePendingTransition(R.anim.enter, R.anim.exit);
        }
    }
}
