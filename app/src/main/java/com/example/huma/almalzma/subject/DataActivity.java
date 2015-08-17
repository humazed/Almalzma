package com.example.huma.almalzma.subject;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

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

import java.io.File;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class DataActivity extends AppCompatActivity implements FolderSelectorDialog.FolderSelectCallback {
    @Bind(R.id.data_list_view) ListView mDataListView;
    @Bind(R.id.empty_text_view) TextView mEmptyTextView;
    @Bind(R.id.loading_view) LoadingView mLoadingView;
    @Bind(R.id.float_menu) FloatingActionMenu mFloatMenu;
    @Bind(R.id.quote_fab) FloatingActionButton mFab1;
    @Bind(R.id.link_fab) FloatingActionButton mFab2;
    @Bind(R.id.event_fab) FloatingActionButton mFab3;


    private int mPreviousVisibleItem;
    private String mLectureName;
    private String[] mDataItems = {};


    @Override
    protected void onResume() {
        super.onResume();
        Intent intent = getIntent();
        String from = intent.getStringExtra(Constants.KEY_FROM);
        switch (from) {
            case Constants.KEY_LECTURE_PREFIX:
                mFab1.setLabelText(getString(R.string.label_lecture_link));
                mFab2.setLabelText(getString(R.string.label_lecture_pdf_file));
                mEmptyTextView.setText(R.string.no_lectures_message);
                mLectureName = intent.getStringExtra(Constants.KEY_LECTURE_PREFIX);
                break;
            case Constants.KEY_SECTION_PREFIX:
                mFab1.setLabelText(getString(R.string.label_section_link));
                mFab2.setLabelText(getString(R.string.label_section_pdf_file));
                mEmptyTextView.setText(R.string.no_sections_message);
                mLectureName = intent.getStringExtra(Constants.KEY_SECTION_PREFIX);
                break;
        }
        Log.d("KEY_PREFIX-DataActivity", mLectureName);

        //retrieve all the quotes.
        ParseQuery<ParseObject> announcementsQuery = ParseQuery.getQuery(mLectureName);
        announcementsQuery.addDescendingOrder(ParseConstants.KEY_CREATED_AT);
        announcementsQuery.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> list, ParseException e) {
                mLoadingView.setVisibility(View.INVISIBLE);
                if (e == null) {
                    //successful
                    mDataItems = new String[list.size()];
                    int i = 0;
                    for (ParseObject dataItem : list) {
                        mDataItems[i] = dataItem.getString(ParseConstants.KEY_LECTURE_LINK);
                        i++;
                    }
                    mDataListView.setAdapter(new ArrayAdapter<>(DataActivity.this,
                            android.R.layout.simple_list_item_1, mDataItems));
                } else {
                    //unsuccessful
                    AlertDialog.Builder builder = new AlertDialog.Builder(DataActivity.this);
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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data);
        ButterKnife.bind(this);


        mDataListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //TODO: view the data.
                Uri uri = Uri.parse(mDataItems[position]);
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });
        mDataListView.setEmptyView(mEmptyTextView);
        mDataListView.setOnScrollListener(scrollListener);

        //control the FloatingActionMenu.
        mFloatMenu.hideMenuButton(false);
        mFloatMenu.setClosedOnTouchOutside(true);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mFloatMenu.showMenuButton(true);
                mFloatMenu.setMenuButtonShowAnimation(AnimationUtils.loadAnimation(DataActivity.this, R.anim.show_from_bottom));
                mFloatMenu.setMenuButtonHideAnimation(AnimationUtils.loadAnimation(DataActivity.this, R.anim.hide_to_bottom));
            }
        }, 300);


        mFab1.setOnClickListener(clickListener);
        mFab2.setOnClickListener(clickListener);
        mFab3.setOnClickListener(clickListener);


    }

    private View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            switch (v.getId()) {
                case R.id.quote_fab:
                    showInputDialog();
                    break;
                case R.id.link_fab:
                    new FolderSelectorDialog().show(DataActivity.this);

                    break;
                case R.id.event_fab:

                    break;
            }
        }

    };

    private void showInputDialog() {
        new MaterialDialog.Builder(this)
                .title(R.string.lecture_link_dialog_input)
                .content(R.string.lecture_link_dialog_content)
                .inputType(InputType.TYPE_CLASS_TEXT |
                        InputType.TYPE_TEXT_VARIATION_PERSON_NAME |
                        InputType.TYPE_TEXT_FLAG_CAP_WORDS)
                .input(R.string.lecture_link_dialog_hint, 0, false, new MaterialDialog.InputCallback() {
                    private String announcementName;

                    @Override
                    public void onInput(MaterialDialog dialog, CharSequence input) {
                        String link = input.toString();
                        if (Utility.validateLinkWithAlderDialog(link, DataActivity.this) != null) {
                            link = Utility.validateLinkWithAlderDialog(link, DataActivity.this);

                            ParseObject announcementsParseObject = new ParseObject(mLectureName);
                            announcementsParseObject.put(ParseConstants.KEY_TYPE, ParseConstants.KEY_LECTURE_LINK);
                            announcementsParseObject.put(ParseConstants.KEY_LECTURE_LINK, link);
                            announcementsParseObject.put(ParseConstants.KEY_CURRENT_USER, MainActivity.mCurrentUser);
                            announcementsParseObject.saveInBackground(saveCallback);
                        }
                    }
                }).show();
    }

    @Override
    public void onFolderSelection(File folder) {
        Toast.makeText(this, folder.getAbsolutePath(), Toast.LENGTH_LONG).show();
        Toast.makeText(this, R.string.coming_soon_message, Toast.LENGTH_LONG).show();
    }

    private SaveCallback saveCallback = new SaveCallback() {
        @Override
        public void done(ParseException e) {
            if (e == null) {
                //successful
                Toast.makeText(DataActivity.this, getString(R.string.add_success_message), Toast.LENGTH_LONG).show();
                onResume();
            } else {
                //unsuccessful show the user AlertDialog.
                AlertDialog.Builder builder = new AlertDialog.Builder(DataActivity.this);
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
                mFloatMenu.hideMenuButton(true);
            } else if (firstVisibleItem < mPreviousVisibleItem) {
                mFloatMenu.showMenuButton(true);
            }
            mPreviousVisibleItem = firstVisibleItem;
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_data, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}