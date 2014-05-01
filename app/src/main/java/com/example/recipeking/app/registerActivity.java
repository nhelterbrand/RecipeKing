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
public class registerActivity extends Activity{
    // Progress Dialog
    private ProgressDialog pDialog;

    JSONParser jsonParser = new JSONParser();
    EditText inputName;
    EditText inputEmail;
    EditText inputPassword;
    // url to create new product
    private static String url_register_user = "http://web.engr.illinois.edu/~andatajunkies/android_connect/register_user.php";

    // JSON Node names
    private static final String TAG_SUCCESS = "success";

    private boolean emailValid;
    private boolean nameValid;
    private boolean passwordValid;
    Button btnRegisterUser;

    public static final String email = "emailKey";
    public static final String password = "passwordKey";
    SharedPreferences sharedpreferences;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);

        // Edit Text
        inputName = (EditText) findViewById(R.id.registerName);
        inputEmail = (EditText) findViewById(R.id.registerEmail);
        inputPassword = (EditText) findViewById(R.id.registerPassword);

        inputName.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                validateName(s.toString());
                updateRegisterButton();
            }
            @Override
            public void onTextChanged(CharSequence s, int a, int b, int c) {}
            @Override
            public void beforeTextChanged(CharSequence s, int a, int b, int c) {}
        });

        inputEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                validateEmail(s.toString());
                updateRegisterButton();
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
                updateRegisterButton();
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
                    onRegisterClicked();
                }
                return false;
            }
        });

        // Create button
        btnRegisterUser = (Button) findViewById(R.id.btnRegister);
        // button click event
        btnRegisterUser.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                // creating new product in background thread
                onRegisterClicked();
            }
        });


        Button btnToLoginPage = (Button)findViewById(R.id.btnLinkToLogin);

        btnToLoginPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), loginActivity.class);
                startActivity(i);
            }
        });
    }

    private void onRegisterClicked() {
        if (!isNetworkOn(getBaseContext())) {
            Toast.makeText(getBaseContext(), "No Network Connection", Toast.LENGTH_SHORT).show();
        } else {
            new RegisterUser().execute();
        }
    }

    public boolean isNetworkOn(Context context) {
        ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        return (networkInfo != null && networkInfo.isConnected());
    }

    private void updateRegisterButton() {
        if (nameValid && emailValid && passwordValid) {
            btnRegisterUser.setEnabled(true);
        }
        else btnRegisterUser.setEnabled(false);
    }

    private void validateName(String text) {
        nameValid = !(text.length() == 0);
    }

    private void validateEmail(String text) {
        emailValid = Patterns.EMAIL_ADDRESS.matcher(text).matches();
    }

    private void validatePassword(String text) {
        passwordValid = !(text.length() == 0);
    }

    /**
     * Background Async Task to Create new product
     * */
    @SuppressLint("NewApi")
    class RegisterUser extends AsyncTask<String, String, String> {



        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(registerActivity.this);
            pDialog.setMessage("Registering You..");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        /**
         * registering user
         * */
        protected String doInBackground(String... args) {
            String name = inputName.getText().toString();
            String rate = inputEmail.getText().toString();
            String description = inputPassword.getText().toString();


            // Building Parameters
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("name", name));
            params.add(new BasicNameValuePair("email", rate));
            params.add(new BasicNameValuePair("password", description));

            // getting JSON Object
            // Note that create product url accepts POST method
            JSONObject json = jsonParser.makeHttpRequest(url_register_user,
                    "POST", params);

            // check log cat fro response
            Log.d("Register Response", json.toString());

            // check for success tag
            try {
                int success = json.getInt(TAG_SUCCESS);

                if (success == 1) {
                    // successfully created product

                    //setting the shared preferences
                    sharedpreferences = getSharedPreferences(loginActivity.myprefs, Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedpreferences.edit();
                    String u = inputEmail.getText().toString();
                    String p = inputPassword.getText().toString();
                    editor.putString(email, u);
                    editor.putString(password, p);
                    editor.commit();

                    //starting menu activity
                    Intent i = new Intent(getApplicationContext(), MenuActivity.class);
                    startActivity(i);

                    // closing this screen
                    finish();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        /**
         * After completing background task Dismiss the progress dialog
         * **/
        protected void onPostExecute(String file_url) {
            // dismiss the dialog once done
            pDialog.dismiss();
        }

    }
}
