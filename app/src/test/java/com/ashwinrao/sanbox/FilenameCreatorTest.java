package com.ashwinrao.sanbox;

import com.ashwinrao.sanbox.util.FilenameCreator;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class FilenameCreatorTest {

    @Test
    @DisplayName("Should test that a filename is returned correctly from an item name")
    public void stringFilenameTest() {
        String item = "Gold Colored Socks";
        assertEquals("/GoldColoredSocks.jpeg", FilenameCreator.create(item));
    }

    @Test
    @DisplayName("Should expose flaw in removing apostrophes")
    public void apostropheFilenameTest() {
        String item = "Jane's Shirts";
        assertEquals("/JanesShirts.jpeg", FilenameCreator.create(item));
    }

    @Test
    @DisplayName("Should expose flaw in removing special characters")
    public void specialCharsFilenameTest() {
        String item = "The Best Shirts I've Ever Seen!!!";
        assertEquals("/TheBestShirtsIveEverSeen.jpeg", FilenameCreator.create(item));
    }

    @Test
    @DisplayName("Should retain numbers and letter combinations")
    public void lettersAndDigitFilenameTest() {
        String item = "Peter Piper's 007 Costume #amazing";
        assertEquals("/PeterPipers007Costumeamazing.jpeg", FilenameCreator.create(item));
    }

    @Test
    @DisplayName("Should test trim() String function for spaces at the beginning and end of input")
    public void trimFilenameTest() {
        String item = "  The Matrix DVD Set. ";
        assertEquals("/TheMatrixDVDSet.jpeg", FilenameCreator.create(item));
    }

}
