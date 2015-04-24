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
import android.widget.Toast;

import com.example.huma.almalzma.R;
import com.github.clans.fab.FloatingActionButton;


/**
 * A simple {@link Fragment} subclass.
 */
public class SectionsFragment extends Fragment {

    ListView mSectionsListView;
    TextView mEmptyTextView;

    private FloatingActionButton mFab;

    private int mPreviousVisibleItem;


    public SectionsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sections, container, false);

        String[] weeks = getResources().getStringArray(R.array.weeks);

        mSectionsListView = (ListView) view.findViewById(R.id.sections_frag_list_view);
        mEmptyTextView = (TextView) view.findViewById(R.id.empty);

        mFab = (FloatingActionButton) view.findViewById(R.id.sec_fab);

        mSectionsListView.setAdapter(new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, weeks));
        mSectionsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //start DataActivity
                startActivity(new Intent(getActivity(), DataActivity.class));
            }
        });
        mSectionsListView.setEmptyView(mEmptyTextView);

        //control the FloatingActionButton.
        mFab.hide(false);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mFab.show(true);
                mFab.setShowAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.show_from_bottom));
                mFab.setHideAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.hide_to_bottom));
            }
        }, 300);

        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "OK!!!", Toast.LENGTH_SHORT).show();
            }
        });

        //hide the button when scroll.
        mSectionsListView.setOnScrollListener(scrollListener);

        mSectionsListView.setEmptyView(mEmptyTextView);

        // Inflate the layout for this fragment
        return view;
    }

    //hide the button when scroll.
    AbsListView.OnScrollListener scrollListener = new AbsListView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {
        }

        @Override
        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            if (firstVisibleItem > mPreviousVisibleItem) {
                mFab.hide(true);
            } else if (firstVisibleItem < mPreviousVisibleItem) {
                mFab.show(true);
            }
            mPreviousVisibleItem = firstVisibleItem;
        }
    };

}
