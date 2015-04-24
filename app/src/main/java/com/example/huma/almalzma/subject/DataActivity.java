package com.example.huma.almalzma.subject;

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

import com.example.huma.almalzma.R;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;

public class DataActivity extends AppCompatActivity {


    ListView mDataListView;
    TextView mEmptyTextView;

    private FloatingActionMenu mFloatingActionMenu;
    private FloatingActionButton mFab1, mFab2, mFab3;

    private int mPreviousVisibleItem;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data);

        String[] weeks = getResources().getStringArray(R.array.weeks);

        //findViewById
        mDataListView = (ListView) findViewById(R.id.data_list_view);
        mEmptyTextView = (TextView) findViewById(R.id.empty);

        mFloatingActionMenu = (FloatingActionMenu) findViewById(R.id.data_float_menu);
        mFab1 = (FloatingActionButton) findViewById(R.id.data_fab1);
        mFab2 = (FloatingActionButton) findViewById(R.id.data_fab2);
        mFab3 = (FloatingActionButton) findViewById(R.id.data_fab3);

        mDataListView.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, weeks));
        mDataListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //TODO: view the data.
            }
        });
        mDataListView.setEmptyView(mEmptyTextView);
        mDataListView.setOnScrollListener(scrollListener);

        //control the FloatingActionMenu.
//        mFloatingActionMenu.hideMenuButton(false);
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

                    break;
                case R.id.data_fab2:

                    break;
                case R.id.data_fab3:

                    break;
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
