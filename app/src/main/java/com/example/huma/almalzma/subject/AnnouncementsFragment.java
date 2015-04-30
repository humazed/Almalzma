package com.example.huma.almalzma.subject;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
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

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.example.huma.almalzma.Constants;
import com.example.huma.almalzma.MainActivity;
import com.example.huma.almalzma.R;
import com.example.huma.almalzma.parse.ParseConstants;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.mingle.widget.LoadingView;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.SaveCallback;
import com.rey.material.widget.EditText;

import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class AnnouncementsFragment extends Fragment {

    private ListView mAnnouncementsListView;
    private TextView mEmptyTextView;
    private LoadingView mLoadingView;

    private FloatingActionMenu mFloatingActionMenu;
    private FloatingActionButton mFab1, mFab2, mFab3;

    //link dialog components.
    private EditText mLinkEditText;
    private EditText mDescriptionEditText;

    private int mPreviousVisibleItem;
    private String mSubjectName;
    private String mGrade;
    private String mAnnouncementName;
    private String[] mAnnouncements = {};
    private ParseObject mAnnouncementsParseObject;


    public AnnouncementsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onResume() {
        super.onResume();

        mLoadingView.setVisibility(View.VISIBLE);

        //get the prefix of parse class.
        Intent intent = getActivity().getIntent();
        mSubjectName = intent.getStringExtra(Constants.KEY_SUBJECT_NAME);
        mGrade = intent.getStringExtra(Constants.KET_GRADE);
        //ParseObject name. which format grade_subjectName_section
        mAnnouncementName = mGrade + "_" + mSubjectName + "_" + ParseConstants.OBJECT_ANNOUNCEMENTS;

        //retrieve all the quotes.
        ParseQuery<ParseObject> announcementsQuery = ParseQuery.getQuery(mAnnouncementName);
        announcementsQuery.addDescendingOrder(ParseConstants.KEY_CREATED_AT);
        announcementsQuery.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> list, ParseException e) {
                mLoadingView.setVisibility(View.INVISIBLE);
                if (e == null) {
                    //successful
                    mAnnouncements = new String[list.size()];
                    int i = 0;
                    for (ParseObject announcement : list) {
                        String s = announcement.getString(ParseConstants.KEY_TYPE);
                        switch (s) {
                            case ParseConstants.KEY_QUOTE:
                                mAnnouncements[i] = announcement.getString(ParseConstants.KEY_QUOTE_TEXT);
                                break;
                            case ParseConstants.KEY_ANNOUNCEMENT_IMPORTANT_LINK:
                                mAnnouncements[i] = announcement
                                        .getString(ParseConstants.KEY_ANNOUNCEMENT_IMPORTANT_LINK_DESCRIPTION);
                                break;
                        }
                        i++; 
                    }
                    mAnnouncementsListView.setAdapter(new ArrayAdapter<>(getActivity(),
                            android.R.layout.simple_list_item_1, mAnnouncements));
                } else {
                    //unsuccessful
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setTitle(getString(R.string.generic_error_title))
                            .setMessage(R.string.connection_error)
                            .setPositiveButton(android.R.string.ok, null)
                            .create().show();
                    Log.e("Error: ", e.getMessage());
                }
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_announcements, container, false);

        //findViewById
        mAnnouncementsListView = (ListView) view.findViewById(R.id.announcements_frag_list_view);
        mEmptyTextView = (TextView) view.findViewById(R.id.empty);
        mLoadingView = (LoadingView) view.findViewById(R.id.announcements_frag_loading_view);

        mFloatingActionMenu = (FloatingActionMenu) view.findViewById(R.id.announcements_frag_float_menu);
        mFab1 = (FloatingActionButton) view.findViewById(R.id.ann_fab1);
        mFab2 = (FloatingActionButton) view.findViewById(R.id.ann_fab2);
        mFab3 = (FloatingActionButton) view.findViewById(R.id.ann_fab3);


        mAnnouncementsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //TODO: add Dialog to let user edit or delete the Announcement it's owen.
//                switch ()
            }
        });
        mAnnouncementsListView.setEmptyView(mEmptyTextView);
        mAnnouncementsListView.setOnScrollListener(scrollListener);

        //control the FloatingActionMenu.
        mFloatingActionMenu.hideMenuButton(false);
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
                case R.id.ann_fab1: //doctor quote.
                    showInputDialog();
                    break;
                case R.id.ann_fab2:
                    showCustomView();
                    break;
                case R.id.ann_fab3:

                    break;
            }
        }

    };

    private void showInputDialog() {
        new MaterialDialog.Builder(getActivity())
                .title(R.string.doctor_quote_dialog_input)
                .content(R.string.doctor_quote_dialog_content)
                .inputType(InputType.TYPE_CLASS_TEXT |
                        InputType.TYPE_TEXT_VARIATION_PERSON_NAME |
                        InputType.TYPE_TEXT_FLAG_CAP_WORDS)
                .input(R.string.doctor_quote_dialog_hint, 0, false, new MaterialDialog.InputCallback() {
                    private String announcementName;

                    @Override
                    public void onInput(MaterialDialog dialog, CharSequence input) {
                        String quote = input.toString();
                        mAnnouncementsParseObject = new ParseObject(mAnnouncementName);
                        mAnnouncementsParseObject.put(ParseConstants.KEY_TYPE, ParseConstants.KEY_QUOTE);
                        mAnnouncementsParseObject.put(ParseConstants.KEY_QUOTE_TEXT, quote);
                        mAnnouncementsParseObject.put(ParseConstants.KEY_CURRENT_USER, MainActivity.mCurrentUser);
                        mAnnouncementsParseObject.saveInBackground(saveCallback);
                    }
                }).show();
    }

    boolean linkFlag, descriptionFlag;

    private void showCustomView() {
        MaterialDialog dialog = new MaterialDialog.Builder(getActivity())
                .title(R.string.important_link)
                .customView(R.layout.add_link_dialog, true)
                .positiveText(R.string.add)
                .negativeText(android.R.string.cancel)
                .callback(new MaterialDialog.ButtonCallback() {
                    @Override
                    public void onPositive(MaterialDialog dialog) {
                        //save link and description to parse.
                        mAnnouncementsParseObject = new ParseObject(mAnnouncementName);
                        String link = mLinkEditText.getText().toString();
                        String description = mDescriptionEditText.getText().toString();
                        mAnnouncementsParseObject.put(ParseConstants.KEY_TYPE, ParseConstants.KEY_ANNOUNCEMENT_IMPORTANT_LINK);
                        mAnnouncementsParseObject.put(ParseConstants.KEY_ANNOUNCEMENT_IMPORTANT_LINK, link);
                        mAnnouncementsParseObject.put(ParseConstants.KEY_ANNOUNCEMENT_IMPORTANT_LINK_DESCRIPTION, description);
                        mAnnouncementsParseObject.put(ParseConstants.KEY_CURRENT_USER, MainActivity.mCurrentUser);
                        mAnnouncementsParseObject.saveInBackground(saveCallback);
                    }

                    @Override
                    public void onNegative(MaterialDialog dialog) {
                    }
                }).build();

        final View positiveAction = dialog.getActionButton(DialogAction.POSITIVE);
        mLinkEditText = (EditText) dialog.getCustomView().findViewById(R.id.add_link_dialog_link);
        mDescriptionEditText = (EditText) dialog.getCustomView().findViewById(R.id.add_link_dialog_description);

        //it's only faction is to enable and disable the input Button.
        mLinkEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                linkFlag = (s.toString().trim().length() > 0);
                if (linkFlag && descriptionFlag) positiveAction.setEnabled(true);
                else positiveAction.setEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        mDescriptionEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                descriptionFlag = (s.toString().trim().length() > 0);
                if (linkFlag && descriptionFlag) positiveAction.setEnabled(true);
                else positiveAction.setEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        dialog.show();
        positiveAction.setEnabled(false); // disabled by default
    }

    private SaveCallback saveCallback = new SaveCallback() {
        @Override
        public void done(ParseException e) {
            if (e == null) {
                //successful
                Toast.makeText(getActivity(), getActivity().getString(R.string.add_success_message), Toast.LENGTH_LONG).show();
                onResume();
            } else {
                //unsuccessful show the user AlertDialog.
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle(getString(R.string.generic_error_title))
                        .setMessage(R.string.connection_error)
                        .setPositiveButton(android.R.string.ok, null)
                        .create().show();
                Log.e("Error: ", e.getMessage());
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
