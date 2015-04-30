package com.example.huma.almalzma;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
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


public class MainActivity extends AppCompatActivity {


    private ListView mSubjectsListView;
    private TextView mEmptyTextView;

    private FloatingActionButton mFab;

    public static ParseUser mCurrentUser;

    String mGrade;
    String[] mSubjects = {};
    private int mPreviousVisibleItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mSubjectsListView = (ListView) findViewById(R.id.subjects_list_view);
        mEmptyTextView = (TextView) findViewById(R.id.empty);
        mFab = (FloatingActionButton) findViewById(R.id.fab);

        mCurrentUser = ParseUser.getCurrentUser();
        if (mCurrentUser == null) {
            // show the signup or login screen
            Intent intent = new Intent(this, LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
        // do stuff with the user
        else {
            //get the user grade to show him the right subjects.
            mGrade = mCurrentUser.getString(ParseConstants.KEY_GRADE);
            mGrade += "2";

            Toast.makeText(this, mGrade, Toast.LENGTH_SHORT).show();

            //switch the user grade to show him the right subjects.
            switch (mGrade) {
                case "0_0_2": //prep
                    mSubjects = this.getResources().getStringArray(R.array.subjects_0_0_2);
                    break;
                case "n_1_2": //1
                    mSubjects = this.getResources().getStringArray(R.array.subjects_n_1_2);
                    break;
                case "n_2_2": //2
                    mSubjects = this.getResources().getStringArray(R.array.subjects_n_2_2);
                    break;
                case "n_3_2": //3
                    mSubjects = this.getResources().getStringArray(R.array.subjects_n_3_2);
                    break;
                case "n_4_2": //4
                    mSubjects = this.getResources().getStringArray(R.array.subjects_n_4_2);
                    break;
                default:
                    mEmptyTextView.setText(getString(R.string.unexpected_error));
            }

            mSubjectsListView.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, mSubjects));
            mSubjectsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    //pass the subject name and Grade to the SubjectActivity to use it making ParseObject.
                    Intent intent = new Intent(MainActivity.this, SubjectActivity.class);
                    intent.putExtra(Constants.KET_GRADE, mGrade);
                    intent.putExtra(Constants.KEY_SUBJECT_NAME, mSubjects[position]);
                    startActivity(intent);
                }
            });
        }

        //control the FloatingActionButton.
        mFab.hide(false);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mFab.show(true);
                mFab.setShowAnimation(AnimationUtils.loadAnimation(MainActivity.this, R.anim.show_from_bottom));
                mFab.setHideAnimation(AnimationUtils.loadAnimation(MainActivity.this, R.anim.hide_to_bottom));
            }
        }, 300);

        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "OK!!!", Toast.LENGTH_SHORT).show();
            }
        });

        //hide the button when scroll.
        mSubjectsListView.setOnScrollListener(scrollListener);

        mSubjectsListView.setEmptyView(mEmptyTextView);

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
