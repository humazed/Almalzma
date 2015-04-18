package com.example.huma.almalzma;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
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

import com.example.huma.almalzma.parse.ParseConstants;
import com.example.huma.almalzma.subject.SubjectActivity;
import com.github.clans.fab.FloatingActionButton;
import com.parse.ParseUser;


public class MainActivity extends ActionBarActivity {


    protected ParseUser mCurrentUser;

    int mGrade;
    String[] mSubjects;

    private int mPreviousVisibleItem;

    private ListView mSubjectsListView;
    private TextView mEmptyTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mSubjectsListView = (ListView) findViewById(R.id.subjects_list_view);
        mEmptyTextView = (TextView) findViewById(R.id.empty);

        mCurrentUser = ParseUser.getCurrentUser();
        if (mCurrentUser == null) {
            // show the signup or login screen
            startActivity(new Intent(this, LoginActivity.class));
        }
        // do stuff with the user
        else {
            //get the user grade to show him the right subjects.
            mGrade = mCurrentUser.getInt(ParseConstants.KEY_GRADE);

            //switch the user grade to show him the right subjects.
            switch (mGrade) {
                case 0: //prep
                    mSubjects = this.getResources().getStringArray(R.array.subjects_1_1);
                    break;
                case 1: //1
                    mSubjects = this.getResources().getStringArray(R.array.subjects_1_1);
                    break;
                case 2: //2

                    break;
                case 3: //3

                    break;
                case 4: //4

                    break;
            }
            mSubjectsListView.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, mSubjects));

            mSubjectsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    startActivity(new Intent(MainActivity.this, SubjectActivity.class));
                }
            });
        }

        //control the FloatingActionButton.
        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.hide(false);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                fab.show(true);
                fab.setShowAnimation(AnimationUtils.loadAnimation(MainActivity.this, R.anim.show_from_bottom));
                fab.setHideAnimation(AnimationUtils.loadAnimation(MainActivity.this, R.anim.hide_to_bottom));
            }
        }, 300);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "OK!!!", Toast.LENGTH_SHORT).show();
            }
        });

        mSubjectsListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (firstVisibleItem > mPreviousVisibleItem) {
                    fab.hide(true);
                } else if (firstVisibleItem < mPreviousVisibleItem) {
                    fab.show(true);
                }
                mPreviousVisibleItem = firstVisibleItem;
            }
        });

        mSubjectsListView.setEmptyView(mEmptyTextView);

    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_logout) {
            //Logout and take the use to LoginActivity
            ParseUser.logOut();
            mCurrentUser = ParseUser.getCurrentUser(); // this will now be null
            startActivity(new Intent(this, LoginActivity.class));
        }

        return super.onOptionsItemSelected(item);
    }
}
