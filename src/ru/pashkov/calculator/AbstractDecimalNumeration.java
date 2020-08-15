
package ru.pashkov.calculator;

import java.util.ArrayList;
import java.util.Arrays;

    /**
     * Класс для описания методов любой нумерации
     */
abstract class AbstractDecimalNumeration {

    /**
     * Возвращает текущую нумерацию
     * @return массив с нумерацией
     */
    abstract String [] getNumeration() ;

    /**
     * Конвертирует в нумерацию вычислений
     * @param rawList Исходный массив (input)
     * @return Массив исходный массив в стандартной нумерации
     */
    abstract  ArrayList<String> convertToStandard( String [] rawList);

    /**
     * Конвертирует в нумерацию ввода(input)
     * @param in Конечный результат в стандартном формате
     * @return конечный результат в формате ввода (input)
     */
    abstract  String convertToSource(ArrayList<String> in );

    /**
     * Определение текущей нумерации
     * @param listNUM Коллекция всех доступных нумераций
     * @param numbers массив исходных данных по которым нужно определить их нумерацию сравнением каждого.
     *                должны совпасть все.
     * @return  нумерацию выражения, если таковая находится
     */
    static AbstractDecimalNumeration currentFormat (ArrayList<AbstractDecimalNumeration> listNUM, String[] numbers) {

        AbstractDecimalNumeration actualNumeration = null;

        int StringSize = numbers.length;
        int NUM        = 0;
        int listSize   = listNUM.size();

        loop:
        for (AbstractDecimalNumeration ss : listNUM) {                  // берем НУМЕРАЦИЮ

            for (int i = 0; i < numbers.length; i = i + 2) {            // берем ЧИСЛО из массива - через одного.
                                                                        // пропуская знаки действий

                for (int j = 0; j < numbers[i].length(); /*empty*/) {   // берем цифру из числа

                    String temp = numbers[i].substring(j, j + 1);       // I....

                    if (Arrays.asList(ss.getNumeration()).contains(temp)) {

                        j++;                                            // инктемент цикла - если меняется цифра

                        if (numbers[i].length() == j && StringSize == i + 1) {
                            actualNumeration = ss;                      // присваиваем значение нумерации
                            break loop;
                        }                                               // следующая буква
                        else {
                            continue;
                        }
                    } else {
                        NUM++;                                           // инкремент цикла - если меняется нумерация
                        if (listSize != NUM) {continue loop;}
                        else {
                            throw new WrongFormatOfExpression("No suitable or Invalid Numeration");
                        }
                    }
                }
            }
        }
        return actualNumeration;
    }

    /**
     * Проверка на формат калькулятора : блок+дейсвие+блок
     * @param s Исходная строка из Сканера
     * @return true если формат правильный, false если не правильный.
     */
    static boolean isCalculatorFormat(String s, int mode){

        if (mode==0) {

            int min = 1;
            int max = 2;

            //String plus = "{" + min + "," + max + "}";

            String spaces  = "[\\s]*";
            String numbers = "[^-+*/\\s]+";// проверка на формат числа позже
            String signs   = "[-+*/]";

           // String numberAndSignsInPeriod = "([-+*/][\\s]*[^\\s-+*/]" + plus + "+[\\s]*)*"; //{1,2}

            return s.matches(
                      spaces  +
                            numbers +
                            spaces  +
                            signs   +
                            spaces  +
                            numbers +
                            spaces
                            //numberAndSignsInPeriod + //  для выражений с множеством действий
                            //spaces
            );

        } else {


            String spaces  = "[\\s]*";
            String numbers = "[^-+*/\\s]+";
            String signs   = "[-+*/]";
            String numberAndSignsInPeriod = "([-+*/][\\s]*[^-+*/\\s]+[\\s]*)*"; //{1,2}

            return s.matches(
                      spaces  +
                            numbers +
                            spaces  +
                            signs   +
                            spaces  +
                            numbers +
                            spaces  +
                            numberAndSignsInPeriod + //  для выражений с множеством действий
                            spaces);
        }
        }

    /**
     * Приведение строки в нужный вид
     * @param in Входящая необработанная строка со сканера
     * @return Подготовленный массив данных
     */
    static String [] RefactoringOfInput (String in) {

        StringBuilder sb        = new StringBuilder(in.trim());
        StringBuilder sb_trash  = new StringBuilder(sb);

        // Устанавливаем гарантированные пробелы между блоками и действиями по которым потом будем их резать
        String rawLineInRightFormst = "";
        int i;

        for (String s : Operations.getPrioritet()) {

            while (sb_trash.indexOf(s) > 0) {
                i = sb_trash.indexOf(s);
                sb.replace(i, i + 1, " " + s + " ");
                sb_trash.replace(i, i + 1, " _ ");
                rawLineInRightFormst = sb.toString();
            }
        }
        return  rawLineInRightFormst
                .replaceAll("\\s+", " ")
                .split("[\\s]+");
    }
}
