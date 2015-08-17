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
import com.example.huma.almalzma.Utility;
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

import butterknife.Bind;
import butterknife.ButterKnife;


public class AnnouncementsFragment extends Fragment {
    @Bind(R.id.announcements_list_view) ListView mAnnouncementsListView;
    @Bind(R.id.empty_text_view) TextView mEmptyTextView;
    @Bind(R.id.loading_view) LoadingView mLoadingView;

    @Bind(R.id.float_menu) FloatingActionMenu mFloatMenu;
    @Bind(R.id.quote_fab) FloatingActionButton mFab1;
    @Bind(R.id.link_fab) FloatingActionButton mFab2;
    @Bind(R.id.event_fab) FloatingActionButton mFab3;


    //TODO: define them with ButterKnife
    //link dialog components.
    private EditText mLinkEditText;
    private EditText mLinkDescriptionEditText;

    private String mSubjectName;
    private String mGrade;
    private String mAnnouncementName;
    private ParseObject mAnnouncementsParseObject;
    private String mLink;
    private List<ParseObject> mAnnouncementsObjectsList;


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
                mAnnouncementsObjectsList = list;
                if (e == null) {
                    //successful
                    //TODO: make class for Announcements.
                    String[] mAnnouncements = new String[list.size()];
                    for (int i = 0; i < list.size(); i++) {
                        ParseObject announcement = list.get(i);
                        String s = announcement.getString(ParseConstants.KEY_TYPE);
                        switch (s) {
                            case ParseConstants.KEY_QUOTE:
                                mAnnouncements[i] = announcement
                                        .getString(ParseConstants.KEY_QUOTE_TEXT);
                                break;
                            case ParseConstants.KEY_IMPORTANT_LINK:
                                mAnnouncements[i] = announcement
                                        .getString(ParseConstants.KEY_IMPORTANT_LINK_DESCRIPTION);
                                break;
                        }
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
        ButterKnife.bind(this, view);

        mAnnouncementsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //if type is important_link open the link.
            }
        });
        mAnnouncementsListView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                //TODO: add Dialog to let user edit or delete the Announcement it's owen.
                return false;
            }
        });
        mAnnouncementsListView.setEmptyView(mEmptyTextView);
        mAnnouncementsListView.setOnScrollListener(scrollListener);

        //control the FloatingActionMenu.
        mFloatMenu.hideMenuButton(false);
        mFloatMenu.setClosedOnTouchOutside(true);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mFloatMenu.showMenuButton(true);
                mFloatMenu.setMenuButtonShowAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.show_from_bottom));
                mFloatMenu.setMenuButtonHideAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.hide_to_bottom));
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
                case R.id.quote_fab: //doctor quote.
                    showQuoteDialog();
                    break;
                case R.id.link_fab:
                    showLinkDialog();
                    break;
                case R.id.event_fab:

                    break;
            }
        }

    };

    private void showQuoteDialog() {
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

    private void showLinkDialog() {
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
                        mLink = mLinkEditText.getText().toString();
                        mLink = Utility.validateLink(mLink);
                        String description = mLinkDescriptionEditText.getText().toString();
                        mAnnouncementsParseObject.put(ParseConstants.KEY_TYPE, ParseConstants.KEY_IMPORTANT_LINK);
                        mAnnouncementsParseObject.put(ParseConstants.KEY_IMPORTANT_LINK, mLink);
                        mAnnouncementsParseObject.put(ParseConstants.KEY_IMPORTANT_LINK_DESCRIPTION, description);
                        mAnnouncementsParseObject.put(ParseConstants.KEY_CURRENT_USER, MainActivity.mCurrentUser);
                        mAnnouncementsParseObject.saveInBackground(saveCallback);
                    }

                    @Override
                    public void onNegative(MaterialDialog dialog) {
                    }
                }).build();

        final View positiveAction = dialog.getActionButton(DialogAction.POSITIVE);
        mLinkEditText = (EditText) dialog.getCustomView().findViewById(R.id.link_edit_text);
        mLinkDescriptionEditText = (EditText) dialog.getCustomView().findViewById(R.id.link_description_edit_text);

        //it's only faction is to enable and disable the input Button.
        mLinkEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //check the link.
                if (Utility.validateLink(mLinkEditText.getText().toString()) == null) {
                    linkFlag = false;
                    mLinkEditText.setError(getActivity().getString(R.string.wrong_link_error_message));
                } else {
                    linkFlag = true;
                    mLinkEditText.setError(null);
                }

                //check if the link is correct and there is description.
                if (linkFlag && descriptionFlag) positiveAction.setEnabled(true);
                else positiveAction.setEnabled(false);


            }

            @Override
            public void afterTextChanged(Editable s) {
            }

        });

        mLinkDescriptionEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //check if the text field is Empty or not.
                descriptionFlag = (s.toString().trim().length() > 0);
                //check if the link is correct and there is description.
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
    private int previousVisibleItem;
    AbsListView.OnScrollListener scrollListener = new AbsListView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {
        }

        @Override
        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            if (firstVisibleItem > previousVisibleItem) {
                mFloatMenu.hideMenuButton(true);
            } else if (firstVisibleItem < previousVisibleItem) {
                mFloatMenu.showMenuButton(true);
            }
            previousVisibleItem = firstVisibleItem;
        }
    };

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}

