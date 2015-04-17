package com.example.huma.almalzma.subject;


import android.os.Bundle;
import android.app.Fragment;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.example.huma.almalzma.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class SectionsFragment extends ListFragment {


    public SectionsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        String[] weeks = getResources().getStringArray(R.array.weeks);

        setListAdapter(new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, weeks));

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_sections, container, false);
    }


}
