package com.ashwinrao.locrate;

import com.ashwinrao.locrate.data.Box;
import com.ashwinrao.locrate.util.BoxValidator;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;

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
    @DisplayName("Should ensure name is not null")
    public void box_name_not_null() {
        box.setName("Andy");
        assertTrue(validator.validate());
    }

    @Test
    @DisplayName("Should ensure name is null")
    public void box_name_null() {
        box.setName(null);
        assertFalse(validator.validate());
    }

    @After
    public void tearDown() {
        box = null;
        validator = null;
    }
}
