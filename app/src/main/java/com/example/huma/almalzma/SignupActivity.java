package com.example.huma.almalzma;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.huma.almalzma.parse.ParseConstants;
import com.mingle.widget.LoadingView;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;


public class SignupActivity extends AppCompatActivity {

    EditText mNameEditText, mEmailEditText, mPasswordEditText, mPasswordConfirmEditText;
    Button mSignupButton;
    TextView mDepartmentTextView, mGradeTextView;
    Spinner mDepartmentSpinner, mGradeSpinner;
    LoadingView mLoadingView;

    String mName, mEmail, mPassword, mPasswordConfirm;
    String mDepartment = "x", mGrade;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        mNameEditText = (EditText) findViewById(R.id.name_signup);
        mEmailEditText = (EditText) findViewById(R.id.email_signup);
        mPasswordEditText = (EditText) findViewById(R.id.password_signup);
        mPasswordConfirmEditText = (EditText) findViewById(R.id.password_confirm_signup);
        mDepartmentTextView = (TextView) findViewById(R.id.department_text_view);
        mGradeTextView = (TextView) findViewById(R.id.grade_text_view);
        mSignupButton = (Button) findViewById(R.id.button_signup);
        mGradeSpinner = (Spinner) findViewById(R.id.grade_spinner);
        mDepartmentSpinner = (Spinner) findViewById(R.id.department_spinner);
        mLoadingView = (LoadingView) findViewById(R.id.signup_loading_view);


        /*the construct of grade which passed to parse.com is as following
        * department_grade_  and then at main add the term
        * so the full grade will be department_grade_term
        * for prep I used "0"
        * and for the rest of departments used the first letter of them.*/
        //Let the user choose his Department.
        mDepartmentSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        mDepartment = "p0"; //prep
                        mGradeSpinner.setVisibility(View.INVISIBLE);
                        mGradeTextView.setVisibility(View.INVISIBLE);
                        break;
                    case 1:
                        mDepartment = "n";
                        mGradeSpinner.setVisibility(View.VISIBLE);
                        mGradeTextView.setVisibility(View.VISIBLE);
                        break;
                    case 2:
                        mDepartment = "p";
                        mGradeSpinner.setVisibility(View.VISIBLE);
                        mGradeTextView.setVisibility(View.VISIBLE);
                        Toast.makeText(SignupActivity.this, R.string.coming_soon_message, Toast.LENGTH_SHORT).show();
                        break;
                    case 3:
                        mDepartment = "e";
                        mGradeSpinner.setVisibility(View.VISIBLE);
                        mGradeTextView.setVisibility(View.VISIBLE);
                        Toast.makeText(SignupActivity.this, R.string.coming_soon_message, Toast.LENGTH_SHORT).show();
                        break;
                    case 4:
                        mDepartment = "a";
                        mGradeSpinner.setVisibility(View.VISIBLE);
                        mGradeTextView.setVisibility(View.VISIBLE);
                        Toast.makeText(SignupActivity.this, R.string.coming_soon_message, Toast.LENGTH_SHORT).show();
                        break;
                    case 5:
                        mDepartment = "c";
                        mGradeSpinner.setVisibility(View.VISIBLE);
                        mGradeTextView.setVisibility(View.VISIBLE);
                        Toast.makeText(SignupActivity.this, R.string.coming_soon_message, Toast.LENGTH_SHORT).show();
                        break;
                    case 6:
                        mDepartment = "m";
                        mGradeSpinner.setVisibility(View.VISIBLE);
                        mGradeTextView.setVisibility(View.VISIBLE);
                        Toast.makeText(SignupActivity.this, R.string.coming_soon_message, Toast.LENGTH_SHORT).show();
                        break;
                    case 7:
                        mDepartment = "u";
                        mGradeSpinner.setVisibility(View.VISIBLE);
                        mGradeTextView.setVisibility(View.VISIBLE);
                        Toast.makeText(SignupActivity.this, R.string.coming_soon_message, Toast.LENGTH_SHORT).show();
                        break;
                    default:    //error state case.
                        mDepartment = "error";
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                AlertDialog.Builder builder = new AlertDialog.Builder(SignupActivity.this);
                builder.setTitle(getString(R.string.generic_error_title))
                        .setMessage(R.string.department_select_message)
                        .setPositiveButton(android.R.string.ok, null)
                        .create().show();
            }
        });

        //let user choose the Grade.
        mGradeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        mGrade = "1";
                        break;
                    case 1:
                        mGrade = "2";
                        break;
                    case 2:
                        mGrade = "3";
                        break;
                    case 3:
                        mGrade = "4";
                        break;
                    default:
                        mGrade = "0"; //error.
                        break;
                }
            }


            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                AlertDialog.Builder builder = new AlertDialog.Builder(SignupActivity.this);
                builder.setTitle(getString(R.string.generic_error_title))
                        .setMessage(R.string.grade_select_message)
                        .setPositiveButton(android.R.string.ok, null)
                        .create().show();
            }
        });

        mSignupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mName = mNameEditText.getText().toString().trim();
                mEmail = mEmailEditText.getText().toString().trim();
                mPassword = mPasswordEditText.getText().toString().trim();
                mPasswordConfirm = mPasswordConfirmEditText.getText().toString().trim();
                if (mDepartment.equals("0")) mGrade = "0";

                //if user leave any thing empty show him AlertDialog.
                if (mName.isEmpty() || mEmail.isEmpty() || mPassword.isEmpty()) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(SignupActivity.this);
                    builder.setTitle(getString(R.string.generic_error_title))
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
                        user.put(ParseConstants.KEY_GRADE, mDepartment + "_" + mGrade + "_");

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
                                    builder.setTitle(getString(R.string.generic_error_title))
                                            .setMessage(e.getMessage())
                                            .setPositiveButton(android.R.string.ok, null)
                                            .create().show();
                                }
                            }
                        });
                    } else {
                        //password not matching >> show the user AlertDialog.
                        AlertDialog.Builder builder = new AlertDialog.Builder(SignupActivity.this);
                        builder.setTitle(getString(R.string.generic_error_title))
                                .setMessage(getString(R.string.password_not_match_error_message))
                                .setPositiveButton(android.R.string.ok, null)
                                .create().show();
                    }
                }
            }
        });

    }

}
