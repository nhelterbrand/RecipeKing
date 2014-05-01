package com.example.recipeking.app;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.TextView;

/**
 * Created by nate on 4/30/14.
 */
public abstract class TextValidator implements TextWatcher {
    private final TextView textView;

    public TextValidator(TextView textView) {
        this.textView = textView;
    }

    public abstract boolean validate(TextView textView);

    @Override
    final public void afterTextChanged(Editable s) {
        validate(textView);
    }

    @Override
    final public void beforeTextChanged(CharSequence c, int a, int b, int d) {}
    @Override
    final public void onTextChanged(CharSequence c, int a, int b, int d) {}
}
