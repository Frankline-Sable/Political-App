package com.maseno.franklinesable.politicalapp.log_in_package;

import android.annotation.TargetApi;
import android.app.Fragment;
import android.app.LoaderManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.provider.ContactsContract;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.text.Html;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
/*
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;*/
import com.maseno.franklinesable.politicalapp.Parse_JSON_Account;
import com.maseno.franklinesable.politicalapp.R;
import com.maseno.franklinesable.politicalapp.RegisterActivity;
import com.maseno.franklinesable.politicalapp.central_class_boss.Main_BossClass;
import com.maseno.franklinesable.politicalapp.sharedmethods.PreferencesHandler;
import com.maseno.franklinesable.politicalapp.sharedmethods.TypefaceHandler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static android.Manifest.permission.READ_CONTACTS;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link LogInFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link LogInFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LogInFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {
    public final static String EXTRA_MESSAGE = "com.maseno.franklinesable.politicalapp";
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final int REQUEST_READ_CONTACTS = 0;
    private static final int REQUEST_SIGNUP = 0;
    private static final String url_login = "http://192.168.75.2/polits_server/login.php";
    private static final String url_fetch_account = "http://192.168.75.2/polits_server/fetch_user_account.php";
    //private UserLoginTask mAuthTask = null;
    ImageView passVisibility;
    TextView logoText, signUpView, warningText;
    EditText mPasswordView;
    Button mEmailSignInButton;
    AutoCompleteTextView mEmailView;
    Boolean shoWPassword = false;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private OnFragmentInteractionListener mListener;
    private Context mContext;
    private fetchUserDataAsync ftData = null;
    private ProgressDialog dialog;
    private PreferencesHandler savePrefs;
    private UserLoginTask mAuthTask = null;
    private View mLoginFormView;
    private TypefaceHandler tp;
/*
    private FirebaseAuth mAuth;
    private  FirebaseAuth.AuthStateListener mAuthListener;
*/
    public LogInFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment LogInFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static LogInFragment newInstance(String param1, String param2) {
        LogInFragment fragment = new LogInFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
      /*  mAuth=FirebaseAuth.getInstance();
        mAuthListener=new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user=firebaseAuth.getCurrentUser();
                if (user != null) {
                    //User is signed in
                    Log.i("meh","onAuthstateChanged: signed in "+user.getUid());
                }else{
                    //user signed out
                    Log.i("meh","onAuthestateChanged: signed out");
                }
            }
        };*/

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_log_in, container, false);
        mEmailView = (AutoCompleteTextView) view.findViewById(R.id.email);
        mPasswordView = (EditText) view.findViewById((R.id.password));
        warningText = (TextView) view.findViewById(R.id.warningText);
        signUpView = (TextView) view.findViewById(R.id.signUp);
        mEmailSignInButton = (Button) view.findViewById(R.id.email_sign_in_button);
        logoText = (TextView) view.findViewById(R.id.logo);
        passVisibility = (ImageView) view.findViewById(R.id.visibilityButton);
        mLoginFormView = view.findViewById(R.id.login_form);

        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            /*throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");*/
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mContext = getActivity();

        tp = new TypefaceHandler(mContext);
        savePrefs = new PreferencesHandler(mContext);
        Window window = getActivity().getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_SECURE);
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        mEmailSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });


        logoText.setTypeface(tp.setTp("BRUSHSCI.TTF"));

        String signUp_Tint = "Or <font color='#0489d1'>Sign up</font>";
        signUpView.setText(Html.fromHtml(signUp_Tint));

        passVisibility.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                visibilityButton();
            }
        });
        mEmailSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });
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
        if (getActivity().checkSelfPermission(READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        if (shouldShowRequestPermissionRationale(READ_CONTACTS)) {
            Snackbar.make(mEmailView, R.string.permission_rationale, Snackbar.LENGTH_INDEFINITE)
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

    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {

        if (mAuthTask != null) {
            return;
        }

        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (TextUtils.isEmpty(password) || !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }
        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        } else if (!isEmailValid(email)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            ConnectivityManager connMgr = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

            if ((networkInfo != null && networkInfo.isConnected())) {
                mAuthTask = new UserLoginTask(email);
                mAuthTask.execute(url_login, email, password);

            } else {

                Snackbar.make(mLoginFormView, getString(R.string.internet_error), Snackbar.LENGTH_LONG)
                        .setAction(R.string.open_settings, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                startActivity(new Intent(Settings.ACTION_WIRELESS_SETTINGS));
                            }
                        }).setActionTextColor(getResources().getColor(android.R.color.holo_orange_dark)).show();
            }
        }
    }

    /**
     * Set up the {@link android.app.ActionBar}, if the API is available.
     */

    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 4;
    }

    /**
     * Shows the progress UI and hides the login form.
     */

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new CursorLoader(mContext,
                // Retrieve data rows for the device user's 'profile' contact.
                Uri.withAppendedPath(ContactsContract.Profile.CONTENT_URI,
                        ContactsContract.Contacts.Data.CONTENT_DIRECTORY), ProfileQuery.PROJECTION,

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
            emails.add(cursor.getString(ProfileQuery.ADDRESS));
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
                new ArrayAdapter<>(mContext,
                        android.R.layout.simple_dropdown_item_1line, emailAddressCollection);

        mEmailView.setAdapter(adapter);
    }

    private void sharedPrefLoggedInState() {
        savePrefs.SaveLoggedInState((savePrefs.getLoggedInState() + 1));
    }

    public void visibilityButton() {
        shoWPassword = !shoWPassword;

        passVisibility.setImageResource(shoWPassword ? R.drawable.ic_visibility_off_black_24dp : R.drawable.ic_visibility_black_24dp);
        warningText.setVisibility(shoWPassword ? View.VISIBLE : View.GONE);

        mPasswordView.setInputType(shoWPassword ? InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD : InputType.TYPE_TEXT_VARIATION_PASSWORD);


        // Create InputType
        int type = InputType.TYPE_CLASS_TEXT;
        if (shoWPassword) {
// Plain display when check is ON.
            type |= InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD;
        } else {
// Masked display when check is OFF.
            type |= InputType.TYPE_TEXT_VARIATION_PASSWORD;
        }
        mPasswordView.setInputType(type);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Front_EndActivity.fragmentDestroyed = true;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    private interface ProfileQuery {
        String[] PROJECTION = {
                ContactsContract.CommonDataKinds.Email.ADDRESS,
                ContactsContract.CommonDataKinds.Email.IS_PRIMARY,
        };

        int ADDRESS = 0;
    }

    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    private class UserLoginTask extends AsyncTask<String, Boolean, Void> {
        private final OkHttpClient client;
        private final String mEmail;
        private RequestBody formBody;

        UserLoginTask(String mEmail) {
            client = new OkHttpClient();
            this.mEmail = mEmail;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(mContext, R.style.AppTheme2_Theme);
            dialog.setMessage(getString(R.string.auth));
            dialog.setIndeterminate(false);
            dialog.setCancelable(false);
            dialog.show();
        }

        @Override
        protected Void doInBackground(String... userData) {
            // TODO: attempt authentication against a network service.
            formBody = new FormBody.Builder().add("username", userData[1]).add("password", userData[2]).build();
            Request request = new Request.Builder()
                    .url(userData[0])
                    .post(formBody)
                    .build();
            Call call = client.newCall(request);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    Snackbar.make(mLoginFormView, getString(R.string.server_down) + e.getMessage(), Snackbar.LENGTH_LONG).show();
                    dialog.dismiss();
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String resp = response.body().string();
                    if (response.isSuccessful()) {
                        if (!resp.equals("fail")) {
                            publishProgress(Boolean.valueOf(resp));
                        } else {
                            new android.os.Handler(Looper.getMainLooper()).post(new Runnable() {
                                @Override
                                public void run() {
                                    dialog.cancel();
                                    mEmailView.setError(getString(R.string.account_blind));
                                    mEmailView.requestFocus();
                                    Snackbar.make(mEmailView.getRootView(), R.string.sign_up, Snackbar.LENGTH_LONG)
                                            .setAction(R.string.yes, new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    startActivity(new Intent(mContext, RegisterActivity.class));
                                                    getActivity().finish();
                                                }
                                            }).setActionTextColor(getResources().getColor(android.R.color.holo_orange_dark)).show();
                                }
                            });
                        }
                    }
                }
            });
            return null;
        }

        @Override
        protected void onProgressUpdate(Boolean... values) {

            if (values[0]) {
                sharedPrefLoggedInState();
                dialog.setTitle(getString(R.string.login_success));
                dialog.setMessage(getString(R.string.fetch_info));
                ftData = new fetchUserDataAsync(mEmail);
                ftData.execute(url_fetch_account);
            } else {
                dialog.dismiss();
                mPasswordView.setError(getString(R.string.error_incorrect_password));
                mPasswordView.requestFocus();
            }
            mAuthTask = null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            mAuthTask = null;
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            dialog.cancel();
        }
    }

    private class fetchUserDataAsync extends AsyncTask<String, Void, String> {
        final OkHttpClient client;
        private final String mEmail;
        String userCredentials[];

        fetchUserDataAsync(String mEmail) {
            this.mEmail = mEmail;
            client = new OkHttpClient();
        }

        @Override
        protected String doInBackground(String... urls) {
            RequestBody formBody = new FormBody.Builder().add("username", mEmail).build();
            Request requestAccount = new Request.Builder().url(urls[0]).post(formBody).build();
            Call call2 = client.newCall(requestAccount);
            call2.enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    Snackbar.make(mLoginFormView, getString(R.string.account_error) + e.getMessage(), Snackbar.LENGTH_LONG).show();
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    Parse_JSON_Account pj = new Parse_JSON_Account(response.body().string());
                    pj.ParseJSON();
                    userCredentials = new String[]{Parse_JSON_Account.id[0], Parse_JSON_Account.f_name[0], Parse_JSON_Account.l_name[0], Parse_JSON_Account.username[0], Parse_JSON_Account.home[0], Parse_JSON_Account.constituency[0], Parse_JSON_Account.password[0], Parse_JSON_Account.gender[0], Parse_JSON_Account.birthdate[0]};
                    savePrefs.SaveAccountState(true);
                    publishProgress();
                    sharedPrefLoggedInState();
                }
            });
            return null;
        }

        @Override
        protected void onProgressUpdate(Void... values) {

            Intent launchBoss = new Intent(mContext, Main_BossClass.class);
            launchBoss.putExtra(EXTRA_MESSAGE, userCredentials);
            startActivityForResult(launchBoss, REQUEST_SIGNUP);
            ftData = null;
            getActivity().finish();
            dialog.dismiss();
        }

        @Override
        protected void onPostExecute(String s) {
            ftData = null;
        }

        @Override
        protected void onCancelled() {
            ftData = null;
            dialog.cancel();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
      //  mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
       /* if(mAuthListener!=null){
            mAuth.removeAuthStateListener(mAuthListener);
        }*/
    }
}
