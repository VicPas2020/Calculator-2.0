package ru.pashkov.calculator;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Нумерация, случайно совпадающия со стандартной
 */
class Numeration_2 extends AbstractDecimalNumeration{

    private final static String [] LATIN  = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9"};

    @Override
    public ArrayList<String>  convertToStandard( String [] rawList) {
        return new ArrayList<>(Arrays.asList(rawList));
    }

    @Override
    public String convertToSource(ArrayList<String> in) {
        return in.get(0);
    }

    @Override
    public String [] getNumeration() {
        return LATIN;
    }
}
