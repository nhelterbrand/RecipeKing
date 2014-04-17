package com.example.recipeking.app;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.Button;

public class MenuActivity extends ActionBarActivity {

    Button btnViewProducts;
    Button btnNewProduct;
    Button btnSearchProduct;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        // Buttons
        btnViewProducts = (Button) findViewById(R.id.btnViewProducts);
        btnNewProduct = (Button) findViewById(R.id.btnCreateProduct);
        btnSearchProduct = (Button) findViewById(R.id.btnSearchRecipe);

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
    }

}
