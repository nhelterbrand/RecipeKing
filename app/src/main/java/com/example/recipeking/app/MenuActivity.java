package com.example.recipeking.app;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.Button;

import java.util.prefs.Preferences;

public class MenuActivity extends Activity {

    Button btnViewProducts;
    Button btnNewProduct;
    Button btnSearchProduct;
    Button btnLogout;

    public static final String myprefs = "myprefs";
    public static final String email = "emailKey";
    public static final String password = "passwordKey";
    SharedPreferences shrdprefs;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        shrdprefs = getSharedPreferences(loginActivity.myprefs, Context.MODE_PRIVATE);
        if (!userLoggedIn()) {
            finish();
            startLoginActivity();
            return;
        }

        // Buttons
        btnViewProducts = (Button) findViewById(R.id.btnViewProducts);
        btnNewProduct = (Button) findViewById(R.id.btnCreateProduct);
        btnSearchProduct = (Button) findViewById(R.id.btnSearchRecipe);
        btnLogout = (Button) findViewById(R.id.btnLogout);

        // view products click event
        btnViewProducts.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                // Launching All products Activity
                Intent i = new Intent(getApplicationContext(), AllRecipesActivity.class);
                startActivity(i);

            }
        });

        // view products click event
        btnNewProduct.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                // Launching create new product activity
                Intent i = new Intent(getApplicationContext(), AddRecipesActivity.class);
                startActivity(i);

            }
        });

        // search products click event
        btnSearchProduct.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                //Launching search product activity
                Intent i = new Intent(getApplicationContext(), SearchRecipeActivity.class);
                startActivity(i);
            }
        });

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //logging out
                logout(view);
            }
        });
    }

    @Override
    public void onResume() {
        if (!userLoggedIn()) {
            finish();
            startLoginActivity();
            return;
        }
        super.onResume();
    }

    public boolean userLoggedIn() {
        Log.d("shared preferences", shrdprefs.getAll().toString());
        if (shrdprefs.contains(email)) {
            if (shrdprefs.contains(password)) {
                Log.d("MenuActivity", "User is already Logged in");
                return true;
            }
        }
        Log.d("MenuActivity", "User isn't logged in yet");
        return false;
    }

    private void startLoginActivity() {
        Intent intent = new Intent(getApplicationContext(), loginActivity.class);
        startActivity(intent);
    }

    private void logout(View view) {
        shrdprefs = getSharedPreferences(loginActivity.myprefs, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = shrdprefs.edit();
        editor.clear();
        editor.commit();
        MenuActivity.this.finish();
        startLoginActivity();
    }

    public boolean onCreateMenuOptions(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.activity_menu_actions, menu);

        return true;
    }

}
