package ru.netology.graphics.image;

public class TextColorW implements TextColorSchema {
    // набор символов от темного к светлому
    private final char[] CHARS = {'#', '$', '@', '%', '*', '+', '-', ' '};

    @Override
    public char convert(int color) {
        // color принимает значения 0..255
        int index = color * CHARS.length / 256; // 0..CHARS.length-1
        if (index < 0) index = 0;
        if (index >= CHARS.length) index = CHARS.length - 1;
        return CHARS[index];
    }
}
