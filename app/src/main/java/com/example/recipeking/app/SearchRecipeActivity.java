package com.example.recipeking.app;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.EditText;

/**
 * Created by nate on 4/15/14.
 */
@SuppressLint("NewApi")
public class SearchRecipeActivity extends Activity {

    private ProgressDialog pDialog;

    JSONParser jsonParser = new JSONParser();
    EditText searchName;
    Button btnSearch;
    String pid;

    //url to search for a recipe
    private static String url_search_product = "http://web.engr.illinois.edu/~jhudzik2/android_connect/search_product.php";

    // JSON Node names
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_PRODUCT = "product";
    private static final String TAG_PID = "pid";
    private static final String TAG_NAME = "name";
    private static final String TAG_rate = "rate";
    private static final String TAG_DESCRIPTION = "description";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_recipe);

        //initialize search button
        btnSearch = (Button) findViewById(R.id.searchButton);

        //get current intent and pid
        Intent i = getIntent();
        pid = i.getStringExtra(TAG_PID);

        // save button click event
        btnSearch.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // starting background task to update product
                new searchQuery().execute();
            }
        });
    }

    class searchQuery extends AsyncTask<String, String, String>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(SearchRecipeActivity.this);
            pDialog.setMessage("Searching for recipe...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... args) {

            // Check for success tag
            int success;
            String nameSearched = searchName.getText().toString();

            try {
                // Building Parameters
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair(TAG_PID, pid));
                params.add(new BasicNameValuePair(TAG_NAME, nameSearched));

                // getting product details by making HTTP request
                JSONObject json = jsonParser.makeHttpRequest(
                        url_search_product, "POST", params);

                // check your log for json response
                Log.d("Search for Recipe", json.toString());

                // json success tag
                success = json.getInt(TAG_SUCCESS);
                if (success == 1) {
                    // product successfully deleted
                    // notify previous activity by sending code 100
                    Intent i = getIntent();
                    // send result code 100 to notify about product deletion
                    setResult(100, i);
                    finish();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String file_url) {
            //dismiss dialog once recipe is searched for
            pDialog.dismiss();
        }
    }
}
