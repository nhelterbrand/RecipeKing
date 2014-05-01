package com.example.recipeking.app;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nate on 4/30/14.
 */
public class loginActivity extends Activity {
    // Progress Dialog
    private ProgressDialog pDialog;

    JSONParser jsonParser = new JSONParser();
    EditText inputEmail;
    EditText inputPassword;
    public TextView errorMessage;
    // url to create new product
    private static String url_login_user = "http://web.engr.illinois.edu/~andatajunkies/android_connect/login_user.php";

    // JSON Node names
    private static final String TAG_SUCCESS = "success";

    private boolean emailValid;
    private boolean passwordValid;
    Button btnLoginUser;

    public static final String email = "emailKey";
    public static final String password = "passwordKey";
    public static final String myprefs = "myprefs";

    SharedPreferences sharedpreferences;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        // Edit Text
        inputEmail = (EditText) findViewById(R.id.loginEmail);
        inputPassword = (EditText) findViewById(R.id.loginPassword);
        errorMessage = (TextView) findViewById(R.id.login_error);

        inputEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                validateEmail(s.toString());
                updateLoginButton();
            }
            @Override
            public void onTextChanged(CharSequence s, int a, int b, int c) {}
            @Override
            public void beforeTextChanged(CharSequence s, int a, int b, int c) {}
        });

        inputPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                validatePassword(s.toString());
                updateLoginButton();
            }
            @Override
            public void onTextChanged(CharSequence s, int a, int b, int c) {}
            @Override
            public void beforeTextChanged(CharSequence s, int a, int b, int c) {}
        });
        inputPassword.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actid, KeyEvent event) {
                boolean isValidKey = event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER;
                boolean isValidAction = actid == EditorInfo.IME_ACTION_DONE;

                if (isValidAction || isValidKey) {
                    onLoginClicked();
                }
                return false;
            }
        });

        //setting the shared preferences
        sharedpreferences = getSharedPreferences(loginActivity.myprefs, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        String u = inputEmail.getText().toString();
        String p = inputPassword.getText().toString();
        editor.putString(email, u);
        editor.putString(password, p);
        editor.commit();
        Log.d("shared preferences in login", sharedpreferences.getAll().toString());

        // Create button
        btnLoginUser = (Button) findViewById(R.id.btnLogin);

        // button click event
        btnLoginUser.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                // creating new product in background thread
                onLoginClicked();
            }
        });

        Button btnToRegisterPage = (Button)findViewById(R.id.btnLinkRegistration);

        btnToRegisterPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), registerActivity.class);
                startActivity(  i);
            }
        });
    }

    private void onLoginClicked() {
        if (!isNetworkOn(getBaseContext())) {
            Toast.makeText(getBaseContext(), "No Network Connection", Toast.LENGTH_SHORT).show();
        } else {
            new LoginUser().execute();
        }
    }

    public boolean isNetworkOn(Context context) {
        ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        return (networkInfo != null && networkInfo.isConnected());
    }

    private void updateLoginButton() {
        if (emailValid && passwordValid) {
            btnLoginUser.setEnabled(true);
        }
        else {
            btnLoginUser.setEnabled(false);
        }
    }

    private void validateEmail(String text) {
        emailValid = Patterns.EMAIL_ADDRESS.matcher(text).matches();
        if (emailValid) inputEmail.setError(null);
        else inputEmail.setError("Email is required and needs to be in email format");
    }

    private void validatePassword(String text) {
        passwordValid = !(text.length() == 0);
        if (passwordValid) inputPassword.setError(null);
        else inputPassword.setError("Password is required");
    }

    /**
     * Background Async Task to Create new product
     * */
    @SuppressLint("NewApi")
    class LoginUser extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(loginActivity.this);
            pDialog.setMessage("Logging You In..");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        /**
         * registering user
         * */
        protected String doInBackground(String... args) {
            String rate = inputEmail.getText().toString();
            String description = inputPassword.getText().toString();

            // Building Parameters
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("email", rate));
            params.add(new BasicNameValuePair("password", description));

            // getting JSON Object
            // Note that create product url accepts POST method
            JSONObject json = jsonParser.makeHttpRequest(url_login_user,
                    "POST", params);

            // check log cat fro response
            Log.d("Login Response", json.toString());

            // check for success tag
            int result = 0;
            try {
                result = json.getInt(TAG_SUCCESS);

                if (result == 1) {
                    //success

                    //starting menu activity
                    Intent i = new Intent(getApplicationContext(), MenuActivity.class);
                    startActivity(i);

                    // closing this screen
                    finish();
                }
                if (result == 2) {
                    Log.e("loginActivity", "incorrect password");
                    //print bad password prompt
                }
                if (result == 3) {
                    Log.e("loginActivity", "no email found");

                    //print bad input prompt
                }
                if (result == 4) {
                    Log.e("loginActivity", "bad input");

                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

            return String.valueOf(result);
        }

        /**
         * After completing background task Dismiss the progress dialog
         * **/
        protected void onPostExecute(String file_url) {
            // dismiss the dialog once done
            Log.d("onPostExecute", file_url);
            int returnNum = 0;
            try {
                returnNum = Integer.parseInt(file_url);
            } catch (Exception e) {
                Log.e("Error", e.toString());
            }

            if (returnNum > 1) {
                AlertDialog alertDialog = new AlertDialog.Builder(
                        loginActivity.this).create();

                alertDialog.setCancelable(true);

                // Setting Dialog Message
                switch(returnNum) {
                    case 2: alertDialog.setMessage("Incorrect Password");
                        break;
                    case 3: alertDialog.setMessage("Email Not Found");
                        break;
                    case 4: alertDialog.setMessage("Incorrect Input");
                        break;
                }

                // Showing Alert Message
                alertDialog.show();
            }
            pDialog.dismiss();
        }

    }
}
