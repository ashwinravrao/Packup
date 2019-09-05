package com.ashwinrao.sanbox;

import com.ashwinrao.sanbox.data.model.Box;
import com.ashwinrao.sanbox.util.BoxPropertiesFilter;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class PropertiesFilterTest {

    private String[] names = {"Socks", "Shoes", "Kitchen Stuff", "Cleaning Products", "Phone Accessories"};
    private String[] descriptions = {"Throw away", "Move to storage", "Sell", "Goodwill", "Donate to church"};
    private List<Box> boxes = new ArrayList<>();
    private BoxPropertiesFilter propfilter;
    private List<Box> actual;
    private List<Box> expected;

    @Before
    public void setUp() {
        actual = new ArrayList<>();
        expected = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            Box box = new Box();
            box.setId(String.valueOf(i+1));
            box.setName(names[i]);
            box.setDescription(descriptions[i]);
            boxes.add(box);
        }
        propfilter = new BoxPropertiesFilter(boxes);
    }

    @Test
    @DisplayName("Should successfully filter on id alone")
    public void successfulFilterOnId() {
        final CharSequence constraint = "1";
        actual = propfilter.filter(constraint, true, false, false);
        expected.clear();
        expected.add(boxes.get(0));
        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Should fail to filter on id alone")
    public void unsuccessfulFilterOnId() {
        final CharSequence constraint = "2";
        actual = propfilter.filter(constraint, true, false, false);
        expected.clear();
        expected.add(boxes.get(0));
        assertNotEquals(expected, actual);
    }

    @Test
    @DisplayName("Should successfully filter on name alone")
    public void successfulFilterOnName() {
        final CharSequence constraint = "itchen";
        actual = propfilter.filter(constraint, false, true, false);
        expected.clear();
        expected.add(boxes.get(2));
        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Should fail to filter on name alone")
    public void unsuccessfulFilterOnName() {
        final CharSequence constraint = "itchen";
        actual = propfilter.filter(constraint, false, true, false);
        expected.clear();
        expected.add(boxes.get(3));
        assertNotEquals(expected, actual);
    }

    @Test
    @DisplayName("Should successfully filter on description alone")
    public void successfulFilterOnDescription() {
        final CharSequence constraint = "ow Away";
        actual = propfilter.filter(constraint, false, false, true);
        expected.clear();
        expected.add(boxes.get(0));
        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Should fail to filter on description alone")
    public void unsuccessfulFilterOnDescription() {
        final CharSequence constraint = "ow Away";
        actual = propfilter.filter(constraint, false, false, true);
        expected.clear();
        expected.add(boxes.get(3));
        assertNotEquals(expected, actual);
    }

    @After
    public void tearDown() {
        actual = null;
        expected = null;
        boxes = null;
        propfilter = null;
    }

}
