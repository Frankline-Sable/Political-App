package com.maseno.franklinesable.politicalapp;

import android.annotation.TargetApi;
import android.app.DialogFragment;
import android.app.LoaderManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.ContactsContract;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
/*
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;*/

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.ButterKnife;
import butterknife.BindView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static android.Manifest.permission.READ_CONTACTS;

public class RegisterActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {
    private View mLoginFormView;
    private static final int REQUEST_READ_CONTACTS = 0;
    private String f_Name, l_Name, email, home, constituency, password, password2;

    private UserRegisterTask mAuthTask = null;
    public static String url_register = "http://192.168.137.1/iMeds/db_signup_papp.php";
    @BindView(R.id.editTextFirstName)
    EditText edit_FirstName;
    @BindView(R.id.lastName)
    EditText edit_LastName;
    @BindView(R.id.editTextUserName)
    AutoCompleteTextView usernameEmail;
    @BindView(R.id.editTextHome)
    EditText editTextHome;
    @BindView(R.id.editTextConstituency)
    EditText editTextConstituency;
    @BindView(R.id.editTextPassword)
    EditText passwordNew;
    @BindView(R.id.editTextConfirmPassword)
    EditText passwordConfirm;
    @BindView(R.id.sex_Spinner)
    Spinner sexSpinner;
    @BindView(R.id.dateButton)
    Button dateButton;
    @BindView(R.id.logo)
    TextView logoText;
/*
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);

        Window window=getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_SECURE);
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        logoText.setTypeface(Typeface.createFromAsset(getAssets(), "BRUSHSCI.TTF"));

        mLoginFormView = findViewById(R.id.login_form);

        populateAutoComplete();


        Button btnCreateAccount = (Button) findViewById(R.id.buttonCreateAccount);
        btnCreateAccount.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                attemptSignUp();
            }
        });

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.gender_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sexSpinner.setAdapter(adapter);

       /* mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d("meh", "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out
                    Log.d("meh", "onAuthStateChanged:signed_out");
                }
                // ...
            }
        };*/
    }


    private void populateAutoComplete() {
        if (!mayRequestContacts()) {
            return;
        }

        getLoaderManager().initLoader(0, null, this);
    }

    private boolean mayRequestContacts() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }
        if (checkSelfPermission(READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        if (shouldShowRequestPermissionRationale(READ_CONTACTS)) {
            Snackbar.make(usernameEmail, R.string.permission_rationale, Snackbar.LENGTH_INDEFINITE)
                    .setAction(android.R.string.ok, new View.OnClickListener() {
                        @Override
                        @TargetApi(Build.VERSION_CODES.M)
                        public void onClick(View v) {
                            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
                        }
                    });
        } else {
            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
        }
        return false;
    }

    /**
     * Callback received when a permissions request has been completed.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == REQUEST_READ_CONTACTS) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                populateAutoComplete();
            }
        }
    }

    private void attemptSignUp() {
        if (mAuthTask != null) {
            return;
        }

        // Reset errors.

        edit_FirstName.setError(null);
        edit_LastName.setError(null);
        usernameEmail.setError(null);
        editTextHome.setError(null);
        editTextConstituency.setError(null);
        passwordNew.setError(null);
        passwordConfirm.setError(null);

        // Store values at the time of the login attempt.

        f_Name = edit_FirstName.getText().toString();
        l_Name = edit_LastName.getText().toString();
        email = usernameEmail.getText().toString();
        home = editTextHome.getText().toString();
        constituency = editTextConstituency.getText().toString();
        password = passwordNew.getText().toString();
        password2 = passwordConfirm.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid email address.
        if (TextUtils.isEmpty(f_Name)) {
            edit_FirstName.setError(getString(R.string.error_invalid_general));
            focusView = edit_FirstName;
            cancel = true;
        }
        if (TextUtils.isEmpty(l_Name)) {
            edit_LastName.setError(getString(R.string.error_invalid_general));
            focusView = edit_LastName;
            cancel = true;
        }
        if (TextUtils.isEmpty(email)) {
            usernameEmail.setError(getString(R.string.error_field_required));
            focusView = usernameEmail;
            cancel = true;
        } else if (!isEmailValid(email)) {
            usernameEmail.setError(getString(R.string.error_invalid_email));
            focusView = usernameEmail;
            cancel = true;
        }
        if (TextUtils.isEmpty(home)) {
            editTextHome.setError(getString(R.string.error_invalid_general));
            focusView = editTextHome;
            cancel = true;
        }
        if (TextUtils.isEmpty(constituency)) {
            editTextConstituency.setError(getString(R.string.error_invalid_general));
            focusView = editTextConstituency;
            cancel = true;
        }
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            passwordNew.setError(getString(R.string.error_invalid_password));
            focusView = passwordNew;
            cancel = true;
        } else if (!password.equals(password2)) {
            passwordConfirm.setError(getString(R.string.error_invalid_password_match));
            focusView = passwordConfirm;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            ConnectivityManager connMgr = (ConnectivityManager)
                    getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

            if ((networkInfo != null && networkInfo.isConnected())) {
                // perform the user register attempt.
                mAuthTask = new RegisterActivity.UserRegisterTask(f_Name, l_Name, email, home, constituency, password, sexSpinner.getSelectedItem().toString(), formatBirthday());
                mAuthTask.execute((Void) null);

            } else {

                Snackbar.make(mLoginFormView, getString(R.string.internet_error) + networkInfo.toString(), Snackbar.LENGTH_LONG)
                        .setAction(getString(R.string.open_settings), new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                startActivity(new Intent(Settings.ACTION_WIRELESS_SETTINGS));
                            }
                        }).setActionTextColor(getResources().getColor(android.R.color.holo_orange_dark)).show();
            }
        }
    }

    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 4;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new CursorLoader(this,
                // Retrieve data rows for the device user's 'profile' contact.
                Uri.withAppendedPath(ContactsContract.Profile.CONTENT_URI,
                        ContactsContract.Contacts.Data.CONTENT_DIRECTORY), RegisterActivity.ProfileQuery.PROJECTION,

                // Select only email addresses.
                ContactsContract.Contacts.Data.MIMETYPE +
                        " = ?", new String[]{ContactsContract.CommonDataKinds.Email
                .CONTENT_ITEM_TYPE},

                // Show primary email addresses first. Note that there won't be
                // a primary email address if the user hasn't specified one.
                ContactsContract.Contacts.Data.IS_PRIMARY + " DESC");
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        List<String> emails = new ArrayList<>();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            emails.add(cursor.getString(RegisterActivity.ProfileQuery.ADDRESS));
            cursor.moveToNext();
        }

        addEmailsToAutoComplete(emails);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {

    }

    private void addEmailsToAutoComplete(List<String> emailAddressCollection) {
        //Create adapter to tell the AutoCompleteTextView what to show in its dropdown list.
        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(RegisterActivity.this,
                        android.R.layout.simple_dropdown_item_1line, emailAddressCollection);

        usernameEmail.setAdapter(adapter);
    }


    private interface ProfileQuery {
        String[] PROJECTION = {
                ContactsContract.CommonDataKinds.Email.ADDRESS,
                ContactsContract.CommonDataKinds.Email.IS_PRIMARY,
        };

        int ADDRESS = 0;
        int IS_PRIMARY = 1;
    }

    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public class UserRegisterTask extends AsyncTask<Void, Boolean, Boolean> {

        private final String mFirstName, mLastName, mEmail, mHome, mConsti, mPassword, mGender, mBirthDate;
        private Boolean registrationError= false;
        ProgressDialog progressDialog;

        UserRegisterTask(String mFirstName, String mLastName, String mEmail, String mHome, String mConsti, String mPassword, String mGender, String mBirthDate) {
            this.mFirstName = mFirstName;
            this.mLastName = mLastName;
            this.mEmail = mEmail;
            this.mHome = mHome;
            this.mConsti = mConsti;
            this.mPassword = mPassword;
            this.mGender = mGender;
            this.mBirthDate = mBirthDate;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(RegisterActivity.this);
            progressDialog.setIndeterminate(true);
            progressDialog.setMessage("Creating Account...");
            progressDialog.show();
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.
            OkHttpClient client = new OkHttpClient();
            RequestBody body = new FormBody.Builder()
                    .add("f_name", mFirstName)
                    .add("l_name", mLastName)
                    .add("username", mEmail)
                    .add("home", mHome)
                    .add("constituency", mConsti)
                    .add("password", mPassword)
                    .add("gender", mGender)
                    .add("birthdate", mBirthDate)
                    .build();

            Request request = new Request.Builder().url(url_register).post(body).build();
            Call call = client.newCall(request);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    Toast.makeText(RegisterActivity.this, getString(R.string.server_down), Toast.LENGTH_LONG).show();
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String resp = response.body().string();
                    if(resp.equalsIgnoreCase("failure"))
                        resp="false";
                            publishProgress(Boolean.valueOf(resp));
                }
            });// TODO: register the new account here.
            return null;
        }

        @Override
        protected void onProgressUpdate(Boolean... values) {
            if (values[0]) {
                progressDialog.dismiss();
                Toast.makeText(getApplicationContext(), R.string.account_sucess, Toast.LENGTH_LONG).show();
                setResult(RESULT_OK, null);
                mAuthTask=null;
                finish();
            }
            else {
                progressDialog.dismiss();
                usernameEmail.setError(getString(R.string.account_dupli));
                usernameEmail.requestFocus();
                Snackbar.make(usernameEmail.getRootView(), R.string.frgt_pass, Snackbar.LENGTH_LONG)
                        .setAction("Yes", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                startActivity(new Intent(RegisterActivity.this, Forgot_password.class));
                                finish();
                            }
                        }).setActionTextColor(getResources().getColor(android.R.color.holo_blue_light)).show();

                mAuthTask=null;
            }
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mAuthTask = null;
            progressDialog.dismiss();
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            progressDialog.cancel();
        }
    }

    public void showDatePickerDialog(View v) {

        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getFragmentManager(), "datePicker");
    }

    private String formatBirthday() {
        Calendar mCalendar = Calendar.getInstance();
        String dateSet = dateButton.getText().toString();
        SimpleDateFormat dateTimeFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);//2017-02-15

        if (dateSet.equalsIgnoreCase("birthday")) {
            return dateTimeFormat.format(mCalendar.getTime());
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-LLLL-yy", Locale.US);
        Date date = null;
        try {
            date = dateFormat.parse(dateSet);
        } catch (ParseException e) {
            Snackbar.make(dateButton, getString(R.string.date_prob) + e.getMessage(), Snackbar.LENGTH_SHORT).show();
        }
        mCalendar.setTime(date);
        return dateTimeFormat.format(mCalendar.getTime());
    }

    @Override
    protected void onStart() {
        super.onStart();
        //mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
      /*  if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }*/
    }
}