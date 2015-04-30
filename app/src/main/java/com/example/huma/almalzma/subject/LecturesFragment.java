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

import com.example.huma.almalzma.Constants;
import com.example.huma.almalzma.R;
import com.example.huma.almalzma.parse.ParseConstants;
import com.github.clans.fab.FloatingActionButton;

//import android.support.v4.app.Fragment;


/**
 * A simple {@link Fragment} subclass.
 */
public class LecturesFragment extends Fragment {

    ListView mLecturesListView;
    TextView mEmptyTextView;
    private FloatingActionButton mFab;

    private int mPreviousVisibleItem;

    private String mLectureName;


    public LecturesFragment() {
        // Required empty public constructor
    }


    @Override
    public void onResume() {
        super.onResume();

        //get the subject name to make a ParseObject with it.
        Intent intent = getActivity().getIntent();
        String mSubjectName = intent.getStringExtra(Constants.KEY_SUBJECT_NAME);
        String mGrade = intent.getStringExtra(Constants.KET_GRADE);
        //ParseObject name. which format grade_subjectName_section
        mLectureName = mGrade + "_" + mSubjectName + "_" + ParseConstants.OBJECT_LECTURES + "_";

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_lectures, container, false);

        final String[] weeks = getResources().getStringArray(R.array.weeks);

        //findViewById
        mLecturesListView = (ListView) view.findViewById(R.id.lectures_frag_list_view);
        mEmptyTextView = (TextView) view.findViewById(R.id.empty);

        mFab = (FloatingActionButton) view.findViewById(R.id.lec_fab);

        mLecturesListView.setAdapter(new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, weeks));
        mLecturesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String week = weeks[position].replaceAll("\\s+", "");
                //start DataActivity
                Intent intent = new Intent(getActivity(), DataActivity.class);
                intent.putExtra(Constants.KEY_LECTURE_PREFIX, mLectureName + week);
                intent.putExtra(Constants.KEY_FROM, Constants.KEY_LECTURE_PREFIX);
                startActivity(intent);
            }
        });
        mLecturesListView.setEmptyView(mEmptyTextView);


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
        mLecturesListView.setOnScrollListener(scrollListener);

        mLecturesListView.setEmptyView(mEmptyTextView);

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
