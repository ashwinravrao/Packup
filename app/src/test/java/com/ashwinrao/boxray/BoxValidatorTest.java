package com.ashwinrao.boxray;

import com.ashwinrao.boxray.data.Box;
import com.ashwinrao.boxray.util.BoxValidator;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class BoxValidatorTest {

    private Box box;
    private BoxValidator validator;

    @Before
    public void setupBox() {
        box = new Box();
        validator = new BoxValidator(box);
    }

    @Test
    public void box_name_not_null() {
        box.setName("Andy");
        assertTrue(validator.validate());
    }

    @Test
    public void box_name_null() {
        box.setName(null);
        assertFalse(validator.validate());
    }
}
