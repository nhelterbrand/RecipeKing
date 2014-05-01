package com.example.recipeking.app;

import android.view.View;
import android.widget.TextView;

/**
 * Created by nate on 4/30/14.
 */
public class FormValidator extends TextValidator implements View.OnFocusChangeListener {

    public FormValidator(TextView textView) {
        super(textView);
    }

    @Override
    public boolean validate(TextView textView) {
        if (textView.getText().toString().length() == 0) {
            textView.setError(textView.getHint() + " is required!");
            return false;
        } else {
            textView.setError(null);
            return true;
        }
    }

    public void onFocusChange(View textView, boolean hasFocus) {
        if (!hasFocus) {
            validate((TextView)textView);
        }
    }


}
