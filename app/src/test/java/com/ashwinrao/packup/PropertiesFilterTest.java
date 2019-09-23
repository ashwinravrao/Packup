package com.ashwinrao.packup;

import com.ashwinrao.packup.data.model.Box;
import com.ashwinrao.packup.util.BoxPropertiesFilter;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

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
    public void successfulFilterOnId() {
        final CharSequence constraint = "1";
        actual = propfilter.filter(constraint, true, false, false);
        expected.clear();
        expected.add(boxes.get(0));
        assertEquals(expected, actual);
    }

    @Test
    public void unsuccessfulFilterOnId() {
        final CharSequence constraint = "2";
        actual = propfilter.filter(constraint, true, false, false);
        expected.clear();
        expected.add(boxes.get(0));
        assertNotEquals(expected, actual);
    }

    @Test
    public void successfulFilterOnName() {
        final CharSequence constraint = "itchen";
        actual = propfilter.filter(constraint, false, true, false);
        expected.clear();
        expected.add(boxes.get(2));
        assertEquals(expected, actual);
    }

    @Test
    public void unsuccessfulFilterOnName() {
        final CharSequence constraint = "itchen";
        actual = propfilter.filter(constraint, false, true, false);
        expected.clear();
        expected.add(boxes.get(3));
        assertNotEquals(expected, actual);
    }

    @Test
    public void successfulFilterOnDescription() {
        final CharSequence constraint = "ow Away";
        actual = propfilter.filter(constraint, false, false, true);
        expected.clear();
        expected.add(boxes.get(0));
        assertEquals(expected, actual);
    }

    @Test
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
