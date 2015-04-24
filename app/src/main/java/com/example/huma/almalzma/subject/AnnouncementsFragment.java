package com.example.huma.almalzma.subject;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.huma.almalzma.R;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;


/**
 * A simple {@link Fragment} subclass.
 */
public class AnnouncementsFragment extends Fragment {

    ListView mAnnouncementsListView;
    TextView mEmptyTextView;

    private FloatingActionMenu mFloatingActionMenu;
    private FloatingActionButton mFab1, mFab2, mFab3;

    private int mPreviousVisibleItem;


    public AnnouncementsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_announcements, container, false);

        String[] weeks = getResources().getStringArray(R.array.weeks);

        //findViewById
        mAnnouncementsListView = (ListView) view.findViewById(R.id.announcements_frag_list_view);
        mEmptyTextView = (TextView) view.findViewById(R.id.empty);

        mFloatingActionMenu = (FloatingActionMenu) view.findViewById(R.id.announcements_frag_float_menu);
        mFab1 = (FloatingActionButton) view.findViewById(R.id.ann_fab1);
        mFab2 = (FloatingActionButton) view.findViewById(R.id.ann_fab2);
        mFab3 = (FloatingActionButton) view.findViewById(R.id.ann_fab3);

        mAnnouncementsListView.setAdapter(new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, weeks));
        mAnnouncementsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //start DataActivity
                startActivity(new Intent(getActivity(), DataActivity.class));
            }
        });
        mAnnouncementsListView.setEmptyView(mEmptyTextView);
        mAnnouncementsListView.setOnScrollListener(scrollListener);

        //control the FloatingActionMenu.
//        mFloatingActionMenu.hideMenuButton(false);
        mFloatingActionMenu.setClosedOnTouchOutside(true);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mFloatingActionMenu.showMenuButton(true);
                mFloatingActionMenu.setMenuButtonShowAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.show_from_bottom));
                mFloatingActionMenu.setMenuButtonHideAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.hide_to_bottom));
            }
        }, 300);


        mFab1.setOnClickListener(clickListener);
        mFab2.setOnClickListener(clickListener);
        mFab3.setOnClickListener(clickListener);


        // Inflate the layout for this fragment
        return view;
    }

    private View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            switch (v.getId()) {
                case R.id.ann_fab1:

                    break;
                case R.id.ann_fab2:

                    break;
                case R.id.ann_fab3:

                    break;
            }
        }

    };

    //hide the button when scroll.
    AbsListView.OnScrollListener scrollListener = new AbsListView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {
        }

        @Override
        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            if (firstVisibleItem > mPreviousVisibleItem) {
                mFloatingActionMenu.hideMenuButton(true);
            } else if (firstVisibleItem < mPreviousVisibleItem) {
                mFloatingActionMenu.showMenuButton(true);
            }
            mPreviousVisibleItem = firstVisibleItem;
        }
    };
}
