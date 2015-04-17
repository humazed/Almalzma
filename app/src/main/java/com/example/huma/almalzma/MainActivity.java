package com.example.huma.almalzma;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;

import com.parse.ParseUser;


public class MainActivity extends ListActivity {


    protected ParseUser mCurrentUser;

    int mGrade;
    String[] mSubjects;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mCurrentUser = ParseUser.getCurrentUser();
        if (mCurrentUser == null) {
            // show the signup or login screen
            startActivity(new Intent(this, LoginActivity.class));
        }
        // do stuff with the user
        else {
            //get the user grade to show him the right subjects.
            mGrade = mCurrentUser.getInt("grade");

            //switch the user grade to show him the right subjects.
            switch (mGrade) {
                case 0: //prep
                    mSubjects = this.getResources().getStringArray(R.array.subjects_1_1);
                    break;
                case 1: //1
                    mSubjects = this.getResources().getStringArray(R.array.subjects_1_2);
                    break;
                case 2: //2

                    break;
                case 3: //3

                    break;
                case 4: //4

                    break;
            }
            setListAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, mSubjects));

        }


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
