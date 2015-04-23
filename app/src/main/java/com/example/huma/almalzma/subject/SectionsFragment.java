package com.example.huma.almalzma.subject;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
public class SectionsFragment extends Fragment {

    ListView mSectionsListView;
    TextView mEmptyTextView;

    private FloatingActionMenu mFloatingActionMenu;
    private FloatingActionButton mFab1, mFab2, mFab3;

    public SectionsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sections, container, false);

        String[] weeks = getResources().getStringArray(R.array.weeks);

        mSectionsListView = (ListView) view.findViewById(R.id.sections_frag_list_view);
        mEmptyTextView = (TextView) view.findViewById(R.id.empty);

        mFloatingActionMenu = (FloatingActionMenu) view.findViewById(R.id.sections_frag_float_menu);
        mFab1 = (FloatingActionButton) view.findViewById(R.id.sec_fab1);
        mFab2 = (FloatingActionButton) view.findViewById(R.id.sec_fab2);
        mFab3 = (FloatingActionButton) view.findViewById(R.id.sec_fab3);

        mSectionsListView.setAdapter(new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, weeks));
        mSectionsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //start DataActivity.
                startActivity(new Intent(getActivity(), DataActivity.class));
            }
        });
        mSectionsListView.setEmptyView(mEmptyTextView);


        // Inflate the layout for this fragment
        return view;
    }

}
