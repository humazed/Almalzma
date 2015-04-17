package com.example.huma.almalzma;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.huma.almalzma.parse.ParseConstants;
import com.mingle.widget.LoadingView;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;


public class SignupActivity extends ActionBarActivity {

    EditText mNameEditText, mEmailEditText, mPasswordEditText, mPasswordConfirmEditText;
    Button mSignupButton;
    Spinner mSpinner;
    LoadingView mLoadingView;

    String mName, mEmail, mPassword, mPasswordConfirm;
    int mGrade;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        mNameEditText = (EditText) findViewById(R.id.name_signup);
        mEmailEditText = (EditText) findViewById(R.id.email_signup);
        mPasswordEditText = (EditText) findViewById(R.id.password_signup);
        mPasswordConfirmEditText = (EditText) findViewById(R.id.password_confirm_signup);
        mSignupButton = (Button) findViewById(R.id.button_signup);
        mSpinner = (Spinner) findViewById(R.id.spinner);
        mLoadingView = (LoadingView) findViewById(R.id.signup_loading_view);

        //Let the user choose his grade.
        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mGrade = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                AlertDialog.Builder builder = new AlertDialog.Builder(SignupActivity.this);
                builder.setTitle(getString(R.string.error_title))
                        .setMessage(R.string.select_message)
                        .setPositiveButton(android.R.string.ok, null)
                        .create().show();
            }
        });

        //Start Signup proses.
        mSignupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mName = mNameEditText.getText().toString().trim();
                mEmail = mEmailEditText.getText().toString().trim();
                mPassword = mPasswordEditText.getText().toString().trim();
                mPasswordConfirm = mPasswordConfirmEditText.getText().toString().trim();

                //if user leave any thing empty show him AlertDialog.
                if (mName.isEmpty() || mEmail.isEmpty() || mPassword.isEmpty()) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(SignupActivity.this);
                    builder.setTitle(getString(R.string.error_title))
                            .setMessage(R.string.signup_error_message)
                            .setPositiveButton(android.R.string.ok, null)
                            .create().show();
                }
                //Create new ParseUser after confirming the password.
                else {
                    //confirm password
                    if (mPassword.equals(mPasswordConfirm)) {
                        mLoadingView.setVisibility(View.VISIBLE);

                        //pass user data to parse.
                        ParseUser user = new ParseUser();
                        user.setUsername(mName);
                        user.setPassword(mPassword);
                        user.setEmail(mEmail);
                        user.put("pass", mPassword);
                        user.put(ParseConstants.KEY_GRADE, mGrade);

                        user.signUpInBackground(new SignUpCallback() {
                            public void done(ParseException e) {
                                mLoadingView.setVisibility(View.INVISIBLE);

                                if (e == null) {
                                    // Hooray! Let them use the app now.
                                    Intent intent = new Intent(SignupActivity.this, MainActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(intent);
                                } else {
                                    // Sign up didn't succeed. Look at the ParseException to figure out what went wrong.
                                    AlertDialog.Builder builder = new AlertDialog.Builder(SignupActivity.this);
                                    builder.setTitle(getString(R.string.error_title))
                                            .setMessage(e.getMessage())
                                            .setPositiveButton(android.R.string.ok, null)
                                            .create().show();
                                }
                            }
                        });
                    } else {
                        //password not matching >> show the user AlertDialog.
                        AlertDialog.Builder builder = new AlertDialog.Builder(SignupActivity.this);
                        builder.setTitle(getString(R.string.error_title))
                                .setMessage(getString(R.string.password_not_match_error_message))
                                .setPositiveButton(android.R.string.ok, null)
                                .create().show();
                    }
                }
            }
        });

    }

}
