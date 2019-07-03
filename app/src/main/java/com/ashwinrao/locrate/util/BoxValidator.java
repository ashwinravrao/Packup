package com.ashwinrao.locrate.util;

import com.ashwinrao.locrate.data.model.Box;

public class BoxValidator {

    private Box box;

    public BoxValidator(Box box) {
        this.box = box;
    }

    public boolean validate() {
        return box.getName() != null;
    }
}
