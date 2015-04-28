package com.example.huma.almalzma.subject;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.Log;
import android.util.Patterns;
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
import com.example.huma.almalzma.parse.ParseConstants;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.mingle.widget.LoadingView;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

import java.util.List;

public class DataActivity extends AppCompatActivity {


    ListView mDataListView;
    TextView mEmptyTextView;
    LoadingView mLoadingView;

    private FloatingActionMenu mFloatingActionMenu;
    private FloatingActionButton mFab1, mFab2, mFab3;

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
                mLectureName = intent.getStringExtra(Constants.KEY_LECTURE_PREFIX);
                break;
            case Constants.KEY_SECTION_PREFIX:
                mFab1.setLabelText(getString(R.string.label_section_link));
                mLectureName = intent.getStringExtra(Constants.KEY_SECTION_PREFIX);
                break;
        }
        Log.d("KEY_PREFIX: ", mLectureName);

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
                        mDataItems[i] = dataItem.getString(ParseConstants.KEY_LINK);
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

        //findViewById
        mDataListView = (ListView) findViewById(R.id.data_list_view);
        mEmptyTextView = (TextView) findViewById(R.id.empty);
        mLoadingView = (LoadingView) findViewById(R.id.data_loading_view);

        mFloatingActionMenu = (FloatingActionMenu) findViewById(R.id.data_float_menu);
        mFab1 = (FloatingActionButton) findViewById(R.id.data_fab1);
        mFab2 = (FloatingActionButton) findViewById(R.id.data_fab2);
        mFab3 = (FloatingActionButton) findViewById(R.id.data_fab3);

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
        mFloatingActionMenu.hideMenuButton(false);
        mFloatingActionMenu.setClosedOnTouchOutside(true);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mFloatingActionMenu.showMenuButton(true);
                mFloatingActionMenu.setMenuButtonShowAnimation(AnimationUtils.loadAnimation(DataActivity.this, R.anim.show_from_bottom));
                mFloatingActionMenu.setMenuButtonHideAnimation(AnimationUtils.loadAnimation(DataActivity.this, R.anim.hide_to_bottom));
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
                case R.id.data_fab1:
                    showInputDialog();
                    break;
                case R.id.data_fab2:

                    break;
                case R.id.data_fab3:

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
                        if (checkLink(link) != null) {
                            link = checkLink(link);

                            ParseObject announcementsParseObject = new ParseObject(mLectureName);
                            announcementsParseObject.put(ParseConstants.KEY_TYPE, ParseConstants.KEY_LINK);
                            announcementsParseObject.put(ParseConstants.KEY_LINK, link);
                            announcementsParseObject.put(ParseConstants.KEY_CURRENT_USER, MainActivity.mCurrentUser);
                            announcementsParseObject.saveInBackground(saveCallback);
                        }
                    }
                }).show();
    }

    //make sure the user has enter wright Uri >> and add http:// to it if it don't.
    private String checkLink(String link) {
        if (Patterns.WEB_URL.matcher(link).matches()) {
            if (!link.startsWith("http://") && !link.startsWith("https://"))
                link = "http://" + link;
            return link;

        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(getString(R.string.wrong_uri_error_title))
                    .setMessage(R.string.wrong_uri_error_message)
                    .setPositiveButton(android.R.string.ok, null)
                    .create().show();
        }
        return null;
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
                mFloatingActionMenu.hideMenuButton(true);
            } else if (firstVisibleItem < mPreviousVisibleItem) {
                mFloatingActionMenu.showMenuButton(true);
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
