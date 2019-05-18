package com.ashwinrao.boxray.util;

import com.ashwinrao.boxray.data.Box;

public class InputValidator {

    private Box box;

    public InputValidator(Box box) {
        this.box = box;
    }

    public boolean validate() {
        return box.getName() != null;
    }
}
