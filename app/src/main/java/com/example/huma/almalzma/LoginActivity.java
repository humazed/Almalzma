package com.example.huma.almalzma;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.mingle.widget.LoadingView;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;


public class LoginActivity extends ActionBarActivity {

    EditText mNameEditText, mPasswordEditText;
    TextView mSignupTextView;
    Button mLoginButton;
    LoadingView mLoadingView;

    String mName, mPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mNameEditText = (EditText) findViewById(R.id.name_login);
        mPasswordEditText = (EditText) findViewById(R.id.password_login);
        mSignupTextView = (TextView) findViewById(R.id.signup_tv_login);
        mLoginButton = (Button) findViewById(R.id.button_login);
        mLoadingView = (LoadingView) findViewById(R.id.login_loading_view);

        //go to SignupActivity.
        mSignupTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, SignupActivity.class));
            }
        });

        //Start Login proses.
        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mName = mNameEditText.getText().toString().trim();
                mPassword = mPasswordEditText.getText().toString().trim();

                //if user leave any thing empty show him AlertDialog.
                if (mName.isEmpty() || mPassword.isEmpty()) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                    builder.setTitle(getString(R.string.error_title))
                            .setMessage(R.string.signup_error_message)
                            .setPositiveButton(android.R.string.ok, null)
                            .create().show();
                }
                //send the data to parse. and if Login information is correct
                //take the user to MainActivity.
                else {
                    mLoadingView.setVisibility(View.VISIBLE);

                    ParseUser.logInInBackground(mName, mPassword, new LogInCallback() {
                        public void done(ParseUser user, ParseException e) {
                            mLoadingView.setVisibility(View.INVISIBLE);

                            if (e == null) {
                                // Hooray! Let them use the app now.
                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                            } else {
                                // Sign up didn't succeed. Look at the ParseException to figure out what went wrong
                                AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                                builder.setTitle(getString(R.string.error_title))
                                        .setMessage(e.getMessage())
                                        .setPositiveButton(android.R.string.ok, null)
                                        .create().show();
                            }
                        }
                    });
                }
            }
        });

    }

}
