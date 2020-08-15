package ru.pashkov.calculator;

import java.util.ArrayList;
import java.util.Arrays;

/**
 *  Еще одна нумерация , которых может быть много
 */
class Numeration_3 extends AbstractDecimalNumeration{

    private final static String [] CUSTOM = {"O", "A", "B", "C", "D", "E", "F", "G", "H", "I"};

    @Override
    public  ArrayList<String> convertToStandard(String[] numbers) {
        return new ArrayList<>();
    }

    @Override
    public String convertToSource(ArrayList<String> in) {
        return "какое-то число";
    }

    @Override
    public String [] getNumeration() {
        return CUSTOM;
    }
}
