package com.maseno.franklinesable.politicalapp;

import android.annotation.TargetApi;
import android.app.LoaderManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.RectF;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Base64;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.maseno.franklinesable.politicalapp.log_in_package.LogInFragment;
import com.maseno.franklinesable.politicalapp.sharedmethods.TypefaceHandler;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.BindView;
import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;

import static android.Manifest.permission.READ_CONTACTS;

public class Account_Settings extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final int REQUEST_READ_CONTACTS = 0;
    private static final int SELECT_PHOTO = 12;
    @BindView(R.id.firstNameField)
    EditText mFirstName;
    @BindView(R.id.lastNameField)
    EditText mLastName;
    private Toolbar toolbar;
    private AppDatabase dbHandler;
    private TypefaceHandler tp;
    @BindView(R.id.homeField)EditText
    mHome;
    @BindView(R.id.constField)EditText
            mConstituency;
    @BindView(R.id.setting_title)TextView
            mTitleView;
    @BindView(R.id.birthField)EditText
            mBirthdate;
    @BindView(R.id.photoField)EditText
            mPhoto;
    @BindView(R.id.genderField)EditText
            mGender;
    @BindView(R.id.usernameField)AutoCompleteTextView
            mUsername;
    @BindView(R.id.mPhotoButton)Button
            mUploadPhotoButton;
    @BindView(R.id.profilePic)CircleImageView
            profileImage;
    String  userCredentials[];
    private fetchUserData asyncFetch=null;
    private userUpdateTask mAuthTask;
    private ProgressDialog activityProgress;
    private Bitmap bitmap;
    File imageFromPickedGallery;
    Uri mCurrentPhotoPath;
    String realImagePath;
    String KEY_IMAGE="image", KEY_USER_ID="name";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account__settings);
        ButterKnife.bind(this);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        initializer();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        dbHandler = new AppDatabase(this);
        tp = new TypefaceHandler(this);

        setSupportActionBar(toolbar);
        setupActionBar();

        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!mFirstName.isEnabled()) {
                    mFirstName.setEnabled(true);
                    mLastName.setEnabled(true);
                    mHome.setEnabled(true);
                    mConstituency.setEnabled(true);
                    mGender.setEnabled(true);
                    fab.setImageResource(R.drawable.edit_user_52px);
                    Snackbar.make(view, "The fields are now editable", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                } else {
                    fab.setImageResource(R.drawable.no_edit_48px);
                    mFirstName.setEnabled(false);
                    mLastName.setEnabled(false);
                    mHome.setEnabled(false);
                    mConstituency.setEnabled(false);
                    mGender.setEnabled(false);
                }
            }
        });
        mTitleView.setTypeface(tp.setTp("Helvetica Neue Light (Open Type).ttf"));
        mFirstName.setTypeface(tp.setTp("Helvetica Neue Light (Open Type).ttf"));
        mLastName.setTypeface(tp.setTp("Helvetica Neue Light (Open Type).ttf"));
        mUsername.setTypeface(tp.setTp("Helvetica Neue Light (Open Type).ttf"));
        mHome.setTypeface(tp.setTp("Helvetica Neue Light (Open Type).ttf"));
        mConstituency.setTypeface(tp.setTp("Helvetica Neue Light (Open Type).ttf"));
        mGender.setTypeface(tp.setTp("Helvetica Neue Light (Open Type).ttf"));
        mBirthdate.setTypeface(tp.setTp("Helvetica Neue Light (Open Type).ttf"));
        mPhoto.setTypeface(tp.setTp("Helvetica Neue Light (Open Type).ttf"));

    }

    private void initializer() {
        Intent intent = getIntent();
        userCredentials = intent.getStringArrayExtra(LogInFragment.EXTRA_MESSAGE);

        if (userCredentials == null) {
            asyncFetch = new fetchUserData();
            asyncFetch.execute();
        } else {
            initialiseFields();
        }
        setResult(RESULT_OK);
    }

    public void initialiseFields() {
        toolbar.setTitle(getString(R.string.userAccount, userCredentials[1]));
        mFirstName.setText(userCredentials[1]);
        mLastName.setText(userCredentials[2]);
        mUsername.setText(userCredentials[3]);
        mHome.setText(userCredentials[4]);
        mTitleView.setText("From "+userCredentials[5]);
        mConstituency.setText(userCredentials[6]);
        mGender.setText(userCredentials[7]);
        mBirthdate.setText(userCredentials[8]);
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
            Snackbar.make(mUsername, R.string.permission_rationale, Snackbar.LENGTH_INDEFINITE)
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

    public void attemptSavingData(View v) {
        if (mAuthTask != null) {
            return;
        }
        mFirstName.setError(null);
        mLastName.setError(null);
        mHome.setError(null);
        mConstituency.setError(null);
        mUsername.setError(null);
        mGender.setError(null);
        mBirthdate.setError(null);

        String first_name = mFirstName.getText().toString();
        String last_name = mLastName.getText().toString();
        String home = mHome.getText().toString();
        String constituency = mConstituency.getText().toString();
        String email = mUsername.getText().toString();
        String gender = mGender.getText().toString();
        String birthdate = mBirthdate.getText().toString();

        boolean cancel = false;
        View focusView = null;

        if (TextUtils.isEmpty(first_name)) {
            mFirstName.setError(getString(R.string.error_invalid_general));
            focusView = mFirstName;
            cancel = true;
        }
        if (TextUtils.isEmpty(last_name)) {
            mLastName.setError(getString(R.string.error_invalid_general));
            focusView = mLastName;
            cancel = true;
        }
        if (TextUtils.isEmpty(email)) {
            mUsername.setError(getString(R.string.error_field_required));
            focusView = mUsername;
            cancel = true;
        } else if (!isEmailValid(email)) {
            mUsername.setError(getString(R.string.error_invalid_email));
            focusView = mUsername;
            cancel = true;
        }
        if (TextUtils.isEmpty(home)) {
            mHome.setError(getString(R.string.error_invalid_general));
            focusView = mHome;
            cancel = true;
        }
        if (TextUtils.isEmpty(constituency)) {
            mConstituency.setError(getString(R.string.error_invalid_general));
            focusView = mConstituency;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

            if ((networkInfo != null && networkInfo.isConnected())) {
                mAuthTask = new userUpdateTask(Integer.parseInt(userCredentials[0]), first_name, last_name, email, home, constituency, gender, birthdate);
                mAuthTask.execute((Void) null);

            } else {
                Snackbar.make(mUsername, "There's no internet connection!" + networkInfo.toString(), Snackbar.LENGTH_LONG)
                        .setAction("Goto Settings", new View.OnClickListener() {
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

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new CursorLoader(this,
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
                new ArrayAdapter<>(Account_Settings.this,
                        android.R.layout.simple_dropdown_item_1line, emailAddressCollection);

        mUsername.setAdapter(adapter);
    }

    //uploadImage();
    public void launchImagePicker(View v) {
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        try {
            imageFromPickedGallery = createImageFile();
        } catch (IOException e) {
            Toast.makeText(this, "Error initialising Image store!", Toast.LENGTH_LONG).show();

        }
        if (imageFromPickedGallery != null) {
            photoPickerIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(imageFromPickedGallery));
            photoPickerIntent.setType("image/*");
            photoPickerIntent.putExtra("return-data", true);
            startActivityForResult(Intent.createChooser(photoPickerIntent, "Select photo from:"), SELECT_PHOTO);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SELECT_PHOTO && resultCode == RESULT_OK) {
            mPhoto.setText(R.string.col);
            try {
                realImagePath = RealPathURI.getRealPathFromURI_API19(this, data.getData());
            } catch (Exception e) {
                try {
                    realImagePath = RealPathURI.getRealPathFromURI_API11to18(this, data.getData());
                } catch (Exception e2) {
                    // SDK < API11
                    realImagePath = RealPathURI.getRealPathFromURI_BelowAPI11(this, data.getData());
                }
            } finally {
                try {
                    Uri filePath = data.getData();
                    //Getting the Bitmap from Gallery
                    bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                    //Setting the Bitmap to ImageView
                    copyImageFile(new File(realImagePath), imageFromPickedGallery);
                    decodeThumbnail();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
            uploadImage();
        }
    }

    //method to convert Bitmap to base64 String.
    public String getStringImage(Bitmap bmp) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        return Base64.encodeToString(imageBytes, Base64.DEFAULT);
    }

    public void uploadImage() {
        final ProgressDialog loading = ProgressDialog.show(this, "Uploading...", "Please wait...", false, false);
        String url_upload_photo = "http://192.168.75.2/polits_server/upload_photo.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url_upload_photo,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        //Dismissing the progress dialog
                        loading.dismiss();
                        //Showing toast message of the response
                        Toast.makeText(Account_Settings.this, response, Toast.LENGTH_LONG).show();
                        mUploadPhotoButton.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.upload_success, 0);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                loading.dismiss();
                Toast.makeText(Account_Settings.this, error.getMessage(), Toast.LENGTH_LONG).show();

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                //Converting Bitmap to String
                String image = getStringImage(bitmap);

                //Getting Image Name
                //Creating parameters
                Map<String, String> params = new Hashtable<String, String>();

                //Adding parameters
                params.put(KEY_IMAGE, image);
                params.put(KEY_USER_ID, userCredentials[3]);

                //returning parameters
                return params;
            }
        };
        //Creating a Request Queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        //Adding request. to the queue
        requestQueue.add(stringRequest);
    }

    @Override
    protected void onResume() {
        dbHandler.open();
        super.onResume();
    }

    public void updateDate(View v) {

        DatePickerSettings newFragment = new DatePickerSettings();
        newFragment.show(getFragmentManager(), "datePicker");
    }

    public File createImageFile() throws IOException {

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmSS", Locale.US).format(new Date());
        final String imageFileName = "JPEG_" + timeStamp + "_";
        File imageFile = null;

        File storageDir = Environment.getExternalStorageDirectory();
        if (storageDir.exists() && storageDir.canWrite()) {

            final File capturedFolder = new File(storageDir.getAbsolutePath() + "/Political App/.Profile Images/");

            if (!capturedFolder.exists()) {
                capturedFolder.mkdirs();
            }
            if (capturedFolder.exists() && capturedFolder.canWrite()) {

                imageFile = File.createTempFile(imageFileName, ".jpg", capturedFolder);
                mCurrentPhotoPath = Uri.fromFile(imageFile.getAbsoluteFile());
            }
        } else {
            Toast.makeText(this, "Error, external storage not available", Toast.LENGTH_LONG).show();
        }
        return imageFile;
    }

    private void decodeThumbnail() throws FileNotFoundException {

        int targetW = profileImage.getWidth();
        int targetH = profileImage.getHeight();

        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(getContentResolver().openInputStream(mCurrentPhotoPath), null, bmOptions);
        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

        int scaleFactor = Math.min(photoW / targetW, photoH / targetH);

        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;
        bmOptions.inPurgeable = true;

        Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(mCurrentPhotoPath), null, bmOptions);
        profileImage.setImageBitmap(scaleCenterCrop(bitmap, targetW, targetH));
    }

    public Bitmap scaleCenterCrop(Bitmap source, int newHeight, int newWidth) {
        int sourceWidth = source.getWidth();
        int sourceHeight = source.getHeight();

// Compute the scaling factors to fit the new height and width, respectively.
// To cover the final image, the final scaling will be the bigger
// of these two.
        float xScale = (float) newWidth / sourceWidth;
        float yScale = (float) newHeight / sourceHeight;
        float scale = Math.max(xScale, yScale);

// Now get the size of the source bitmap when scaled
        float scaledWidth = scale * sourceWidth;
        float scaledHeight = scale * sourceHeight;

// Let's find out the upper left coordinates if the scaled bitmap
// should be centered in the new size give by the parameters
        float left = (newWidth - scaledWidth) / 2;
        float top = (newHeight - scaledHeight) / 2;

// The target rectangle for the new, scaled version of the source bitmap will now
// be
        RectF targetRect = new RectF(left, top, left + scaledWidth, top + scaledHeight);

// Finally, we create a new bitmap of the specified size and draw our new,
// scaled bitmap onto it.
        Bitmap dest = Bitmap.createBitmap(newWidth, newHeight, source.getConfig());
        Canvas canvas = new Canvas(dest);
        canvas.drawBitmap(source, null, targetRect, null);

        return dest;
    }

    private void copyImageFile(File sourceFile, File destFile) throws IOException {


        if (!sourceFile.exists()) {
            return;
        }
        FileChannel source = null;
        FileChannel destination = null;
        source = new FileInputStream(sourceFile).getChannel();
        destination = new FileOutputStream(destFile).getChannel();

        if (source != null) {
            destination.transferFrom(source, 0, source.size());
        }
        if (source != null) {
            source.close();
        }
        destination.close();
    }

    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */

    private void setupActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }


    private interface ProfileQuery {
        String[] PROJECTION = {
                ContactsContract.CommonDataKinds.Email.ADDRESS,
                ContactsContract.CommonDataKinds.Email.IS_PRIMARY,
        };

        int ADDRESS = 0;
        int IS_PRIMARY = 1;
    }

    public class userUpdateTask extends AsyncTask<Void, Void, Boolean> {

        private final int id;
        private final String nFirstName, nLastName, nHome, nConstituency, mEmail, nGender, nBirthdate;
        private ProgressDialog dialog;
        private Boolean updateState = false;

        userUpdateTask(int id, String nFirstName, String nLastName, String mEmail, String nHome, String nConstituency, String nGender, String nBirthdate) {
            this.id = id;
            this.nFirstName = nFirstName;
            this.nLastName = nLastName;
            this.nHome = nHome;
            this.nConstituency = nConstituency;
            this.mEmail = mEmail;
            this.nGender = nGender;
            this.nBirthdate = nBirthdate;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = ProgressDialog.show(Account_Settings.this, "Wait a second", "Updating user data...", false, false);
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.4\
            //uploadImage();
            OkHttpClient client = new OkHttpClient();
            RequestBody body = new FormBody.Builder()
                    .add("pid", Integer.toString(id))
                    .add("f_name", nFirstName)
                    .add("l_name", nLastName)
                    .add("username", mEmail)
                    .add("home", nHome)
                    .add("constituency", nConstituency)
                    .add("gender", nGender)
                    .add("birthdate", nBirthdate)
                    .build();
            //TODO: update the account new account here.

            String url_update_userdata = "http://192.168.75.2/polits_server/UpdateData.php";
            okhttp3.Request request = new okhttp3.Request.Builder().url(url_update_userdata).post(body).build();
            Call call = client.newCall(request);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    Toast.makeText(Account_Settings.this, "Connection error, server is down!", Toast.LENGTH_LONG).show();
                }

                @Override
                public void onResponse(Call call, okhttp3.Response response) throws IOException {
                    if (updateState = Boolean.valueOf(response.body().string())) {
                        dbHandler.open();
                        dbHandler.updateDb(1, nFirstName, nLastName, mEmail, nHome, nConstituency, nGender, nBirthdate, "no photo");
                    }
                }
            });

            return updateState;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mAuthTask = null;
            dialog.dismiss();
            if (updateState) {
                Toast.makeText(Account_Settings.this, "Account successfully updated!", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(Account_Settings.this, "Error doing update!", Toast.LENGTH_LONG).show();
            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            dialog.cancel();
        }
    }

    public class fetchUserData extends AsyncTask<Void, Cursor, Cursor> {
        @Override
        protected void onPreExecute() {
            activityProgress = new ProgressDialog(Account_Settings.this);
            activityProgress.setIndeterminate(false);
            activityProgress.setCancelable(false);
            activityProgress.setMessage("Loading account data...");
            activityProgress.show();
        }

        @Override
        protected Cursor doInBackground(Void... voids) {
            dbHandler.open();
            Cursor cursor = dbHandler.readDb();
            cursor.moveToFirst();
            return cursor;
        }

        @Override
        protected void onPostExecute(Cursor cursor) {
            userCredentials = new String[]{AppDatabase.table_Structure.KEY_ROW_ID, AppDatabase.table_Structure.KEY_F_NAME, AppDatabase.table_Structure.KEY_L_NAME, AppDatabase.table_Structure.KEY_USERNAME,
                    AppDatabase.table_Structure.KEY_HOME, AppDatabase.table_Structure.KEY_CONSTITUENCY, AppDatabase.table_Structure.KEY_GENDER, AppDatabase.table_Structure.KEY_BIRTHDATE, AppDatabase.table_Structure.KEY_IMAGEPATH};

            for (int i = 0; i < userCredentials.length; i++) {
                userCredentials[i] = cursor.getString(cursor.getColumnIndex(userCredentials[i]));
            }
            cursor.close();
            activityProgress.dismiss();
           initialiseFields();
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            asyncFetch = null;
            activityProgress.cancel();
        }
    }

    @Override
    protected void onPause() {
        dbHandler.close();
        super.onPause();
    }
}
