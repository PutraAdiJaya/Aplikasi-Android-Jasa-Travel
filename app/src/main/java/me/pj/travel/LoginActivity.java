package me.pj.travel;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import me.pj.travel.controller.ApiClient;
import me.pj.travel.controller.Preferences;
import me.pj.travel.interfece.ApiInterface;
import me.pj.travel.models.GsonAction;
import me.pj.travel.models.User;
import me.pj.travel.models.UserResponse;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends Activity implements LoaderCallbacks<Cursor> {

    /**
     * Id to identity READ_CONTACTS permission request.
     */
    private static final int REQUEST_READ_CONTACTS = 0;


    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
    private UserLoginTask mAuthTask = null;

    // UI references.
    private TextView mEmailView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;
    int MODE = 1;
    Button btnMode;
    EditText editTextConfirmPass, edNama, edAlamat, edHP;
    TextView txtJenisLogin;
    LinearLayout layRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // Set up the login form.
        mEmailView = findViewById(R.id.email);

        mPasswordView = (EditText) findViewById(R.id.password);


        Preferences.loadPreferences(this);
        Button mEmailSignInButton = (Button) findViewById(R.id.ok_proses_button);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (MODE == 1) {
                    attemptLogin();
                } else {
                    attemptRegister();
                }
            }
        });


        layRegister = findViewById(R.id.layRegister);
        btnMode = findViewById(R.id.email_sign_in_button);
        txtJenisLogin = findViewById(R.id.txtJenisLogin);
        editTextConfirmPass = findViewById(R.id.password2);
        edAlamat = findViewById(R.id.edAlamat);
        edHP = findViewById(R.id.edHP);
        edNama = findViewById(R.id.edNama);
        layRegister.setVisibility(View.GONE);
        btnMode.setText("Register");
        txtJenisLogin.setText("LOGIN");
        btnMode.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                gantiMode();
            }
        });
        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
     /*   mEmailView.setText("username");
        mPasswordView.setText("ada");
        edNama.setText("nama");
        edHP.setText("089231611351");
        edAlamat.setText("jalan surabaya malang");
        editTextConfirmPass.setText("ada");*/
        mEmailView.setText("");
        mPasswordView.setText("");
        edNama.setText("");
        edHP.setText("");
        edAlamat.setText("");
        editTextConfirmPass.setText("");

    }
    @Override
    protected void onResume() {
        super.onResume();
        if (Preferences.IS_LOGIN){
            startActivity(new Intent(this, MainActivity.class));

            Toast.makeText(LoginActivity.this, "Selamat datang kembali, " + Preferences.NAMA.toUpperCase() , Toast.LENGTH_LONG).show();
            finish();
        }
    }
    User CURRENT_DATA = new User();
/*VALIDASI INPUT*/
    private void attemptRegister() {
        if (mAuthTask != null) {
            return;
        }

        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);
        edNama.setError(null);
        edAlamat.setError(null);
        edHP.setError(null);
        editTextConfirmPass.setError(null);

        // Store values at the time of the login attempt.
        final String email = mEmailView.getText().toString();
        final String password = mPasswordView.getText().toString();
        String password2 = editTextConfirmPass.getText().toString();
        String nama = edNama.getText().toString();
        String alamat = edAlamat.getText().toString();
        String hp = edHP.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (TextUtils.isEmpty(password)) {
            mPasswordView.setError(getString(R.string.error_field_required));
            focusView = mPasswordView;
            cancel = true;
        }

        if (TextUtils.isEmpty(password2) || !password.equals(password2)) {
            mPasswordView.setError(getString(R.string.error_incorrect_password));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid password, if the user entered one.
        if (TextUtils.isEmpty(nama)) {
            edNama.setError(getString(R.string.error_field_required));
            focusView = edNama;
            cancel = true;
        }
        // Check for a valid password, if the user entered one.
        if (TextUtils.isEmpty(alamat)) {
            edAlamat.setError(getString(R.string.error_field_required));
            focusView = edAlamat;
            cancel = true;
        }
        // Check for a valid password, if the user entered one.
        if (TextUtils.isEmpty(hp)) {
            edHP.setError(getString(R.string.error_field_required));
            focusView = edHP;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
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
            CURRENT_DATA = new User();
            CURRENT_DATA.setAkses("USER");
            CURRENT_DATA.setPassword(password);
            CURRENT_DATA.setUsername(email);
            CURRENT_DATA.setAlamat(alamat);
            CURRENT_DATA.setNama(nama);
            CURRENT_DATA.setHp(hp);

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Yakin Untuk Proses Registrasi Data Anda?");
            // Set up the buttons
            builder.setPositiveButton("Tidak",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
            builder.setNegativeButton("Ya",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            showProgress(true);
                            mAuthTask = new UserLoginTask(email, password);
                            mAuthTask.execute((Void) null);
                        }
                    });

            builder.show();


        }

    }
/*GANTI MODE LOGIN ATAU REGISTER*/
    private void gantiMode() {
        if (txtJenisLogin.getText().equals("LOGIN")) {
            layRegister.setVisibility(View.VISIBLE);
            txtJenisLogin.setText("REGISTER");
            btnMode.setText("Login");
            MODE = 2;
        } else {
            layRegister.setVisibility(View.GONE);
            txtJenisLogin.setText("LOGIN");
            btnMode.setText("Register");
            MODE = 1;
        }
    }


    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */

    /*VALIDASI INPUT LOGIN*/
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
        if (TextUtils.isEmpty(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
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

            CURRENT_DATA = new User();
            CURRENT_DATA.setPassword(password);
            CURRENT_DATA.setUsername(email);

            showProgress(true);
            mAuthTask = new UserLoginTask(email, password);
            mAuthTask.execute((Void) null);
        }
    }
  /*TAMPIL PROGRESS  */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
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
                new ArrayAdapter<>(LoginActivity.this,
                        android.R.layout.simple_dropdown_item_1line, emailAddressCollection);

    }


    private interface ProfileQuery {
        String[] PROJECTION = {
                ContactsContract.CommonDataKinds.Email.ADDRESS,
                ContactsContract.CommonDataKinds.Email.IS_PRIMARY,
        };

        int ADDRESS = 0;
        int IS_PRIMARY = 1;
    }



    ApiInterface mApiInterface;
/*PROSES KIRIM REQUEST*/
    public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {

        private final String mEmail;
        private final String mPassword;

        UserLoginTask(String email, String password) {
            mEmail = email;
            mPassword = password;

            Preferences.resetUserPreferences(LoginActivity.this);

        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.


            if (MODE == 1) {
                dologin();
            } else {
                doRegister();
            }
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                return false;
            }

            return Preferences.IS_ACTION_OK;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mAuthTask = null;
            showProgress(false);
            if (success) {
                if (MODE == 1) {
                    showMain();
                } else {
                   mPasswordView.setText("");
                   mEmailView.setText("");
                    gantiMode();
                }
            } else {
               /* mPasswordView.setError(getString(R.string.error_incorrect_password));
                mPasswordView.requestFocus();*/
            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            showProgress(false);
        }
    }
/*DATA REQUEST*/
    private void doRegister() {
        mApiInterface = ApiClient.getClient().create(ApiInterface.class);


        RequestBody rbUsername =
                RequestBody.create(
                        okhttp3.MultipartBody.FORM, CURRENT_DATA.getUsername());

        RequestBody rbPassword =
                RequestBody.create(
                        okhttp3.MultipartBody.FORM, CURRENT_DATA.getPassword());

        RequestBody rbAkses =
                RequestBody.create(
                        okhttp3.MultipartBody.FORM, CURRENT_DATA.getAkses());

        RequestBody rbNama =
                RequestBody.create(
                        okhttp3.MultipartBody.FORM, CURRENT_DATA.getNama());


        RequestBody rbAlamat =
                RequestBody.create(
                        okhttp3.MultipartBody.FORM, CURRENT_DATA.getAlamat());


        RequestBody rbHP =
                RequestBody.create(
                        okhttp3.MultipartBody.FORM, CURRENT_DATA.getHp());


        Preferences.resetUserPreferences(LoginActivity.this);
        Call<GsonAction> apiCall = mApiInterface.postUser(rbNama, rbUsername, rbPassword, rbHP, rbAlamat, rbAkses);
        apiCall.enqueue(new Callback<GsonAction>() {
            @Override
            public void onResponse(Call<GsonAction> call, Response<GsonAction>
                    response) {

                GsonAction resJSON;
                resJSON = response.body();
                Toast.makeText(LoginActivity.this, response.body().getMessage() , Toast.LENGTH_LONG).show();
                Log.i("MyAPP", response.toString());
                Preferences.IS_ACTION_OK = response.body().getStatus().equals("OK");
                Preferences.IS_LOGIN = false;
                Preferences.saveUserPreferences(LoginActivity.this);

            }

            @Override
            public void onFailure(Call<GsonAction> call, Throwable t) {
                Log.e("Retrofit Get", t.toString());
                Toast.makeText(LoginActivity.this, "Proses Penyimpanan Gagal, Silahkan coba lagi", Toast.LENGTH_LONG).show();
            }
        });

    }
    /*DATA REQUEST LOGIN*/
    private void dologin() {

        mApiInterface = ApiClient.getClient().create(ApiInterface.class);

        RequestBody rbUsername =
                RequestBody.create(
                        okhttp3.MultipartBody.FORM, CURRENT_DATA.getUsername());

        RequestBody rbPassword =
                RequestBody.create(
                        okhttp3.MultipartBody.FORM, CURRENT_DATA.getPassword());


        Preferences.resetUserPreferences(LoginActivity.this);
        Call<UserResponse> apiCall = mApiInterface.loginUser(rbUsername, rbPassword);
        apiCall.enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse>
                    response) {
                UserResponse resJSON = response.body();
                Toast.makeText(LoginActivity.this,resJSON.getMessage() , Toast.LENGTH_LONG).show();

                Preferences.resetUserPreferences(LoginActivity.this);
                if (resJSON.getStatus().equals("OK")){
                    Preferences.ID = resJSON.getUsers().get(0).getIdUser();
                    Preferences.NAMA = resJSON.getUsers().get(0).getNama();
                    Preferences.USERNAME = resJSON.getUsers().get(0).getUsername();
                    Preferences.AKSES = resJSON.getUsers().get(0).getAkses();
                    Preferences.IS_ACTION_OK = response.body().getStatus().equals("OK");
                    Preferences.IS_LOGIN = response.body().getStatus().equals("OK");
                    Preferences.saveUserPreferences(LoginActivity.this);
                }
                Log.i("MyAPP", resJSON.toString());
            }

            @Override
            public void onFailure(Call<UserResponse> call, Throwable t) {
                Log.e("Retrofit Get", t.toString());
                Toast.makeText(LoginActivity.this, "Proses Login Gagal, Silahkan coba lagi", Toast.LENGTH_LONG).show();

            }
        });

    }
/*KONFITMASI CLOSE*/
    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Yakin Untuk Keluar dari Aplikasi?");
        // Set up the buttons
        builder.setPositiveButton("Tidak",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        builder.setNegativeButton("Ya",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        finish();
                    }
                });

        builder.show();
    }
/*TAMPIL MENU UTAMA*/
    private void showMain() {
        this.finish();
        startActivity(new Intent(this, MainActivity.class));
    }
}

