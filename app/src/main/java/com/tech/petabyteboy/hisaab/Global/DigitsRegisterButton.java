package com.tech.petabyteboy.hisaab.Global;

import android.content.Context;
import android.util.AttributeSet;

import com.digits.sdk.android.DigitsAuthButton;
import com.tech.petabyteboy.hisaab.R;

/**
 * Created by petabyteboy on 14/07/16.
 */
public class DigitsRegisterButton extends DigitsAuthButton {
    public DigitsRegisterButton(Context c) {
        super(c);
        init();
    }
    public DigitsRegisterButton(Context c, AttributeSet attrs) {
        super(c, attrs);
        init();
    }

    public DigitsRegisterButton(Context c, AttributeSet attrs, int defStyle) {
        super(c, attrs, defStyle);
        init();
    }

    private void init() {
        if (isInEditMode()) {
            return;
        }

        setBackgroundResource(R.drawable.button_design);

        // Modifying the text here..
        setText(getResources().getString(R.string.btn_signup));

        setTextColor(getResources().getColor(R.color.caribbean_green));
    }
}