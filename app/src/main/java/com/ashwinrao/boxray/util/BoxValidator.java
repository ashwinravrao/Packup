package com.ashwinrao.boxray.util;

import com.ashwinrao.boxray.data.Box;

public class BoxValidator {

    private Box box;

    public BoxValidator(Box box) {
        this.box = box;
    }

    public boolean validate() {
        return box.getName() != null;
    }
}
