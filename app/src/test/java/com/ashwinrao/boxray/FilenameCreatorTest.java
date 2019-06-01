package com.ashwinrao.boxray;

import com.ashwinrao.boxray.util.FilenameCreator;

import org.junit.After;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class FilenameCreatorTest {

    private FilenameCreator creator;

    @Test
    @DisplayName("Should test that a filename is returned correctly from an item name")
    public void stringFilenameTest() {
        String item = "Gold Colored Socks";
        creator = new FilenameCreator(item);
        assertEquals("GoldColoredSocks.jpeg", creator.create());
    }

    @Test
    @DisplayName("Should expose flaw in removing apostrophes")
    public void apostropheFilenameTest() {
        String item = "Jane's Shirts";
        creator = new FilenameCreator(item);
        assertEquals("JanesShirts.jpeg", creator.create());
    }

    @Test
    @DisplayName("Should expose flaw in removing special characters")
    public void specialCharsFilenameTest() {
        String item = "The Best Shirts I've Ever Seen!!!";
        creator = new FilenameCreator(item);
        assertEquals("TheBestShirtsIveEverSeen.jpeg", creator.create());
    }

    @Test
    @DisplayName("Should retain numbers and letter combinations")
    public void lettersAndDigitFilenameTest() {
        String item = "Peter Piper's 007 Costume #amazing";
        creator = new FilenameCreator(item);
        assertEquals("PeterPipers007Costumeamazing.jpeg", creator.create());
    }

    @Test
    @DisplayName("Should test trim() String function for spaces at the beginning and end of input")
    public void trimFilenameTest() {
        String item = "  The Matrix DVD Set. ";
        creator = new FilenameCreator(item);
        assertEquals("TheMatrixDVDSet.jpeg", creator.create());
    }

    @After
    public void tearDown() {
        creator = null;
    }

}
