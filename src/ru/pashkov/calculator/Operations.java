package ru.pashkov.calculator;

import java.util.ArrayList;

class Operations {

    /**
     * Приоритет математических операций согласно индексам массива чем ниже число, тем выше приоритет
     */
    private static String [] prioritet = {"*","/","-","+"};

    /**
     * НЕ используется. Можно сделать автоопределение математических
     * действий для проверки на формат isCalculatorFormat
     */
    public static  String actionsForCheckingFormat () {

        return  "\\*" + "/" + "\\+" + "-";
    }

    /**
     * Возвращает приоритет математических операций
     * @return Приоритет арефметических операций, когда в строке более одного действия
     */
    static String[] getPrioritet() {
        return prioritet;
    }

    /**
     * Производит вычисления в стандартной десятичной нумерации складывая по два значения и
     * подставляя результат на их место.
     * @param rawList Данные в подготовленном формате для прямых вычислений
     * @param mode переключатель 0- работа строго по условиям задачи, 1 - расширенная работа.
     *
     * @return Результат вычисления в форме одного значения в коллекции.
     */
    static ArrayList<String> calculateThis(ArrayList<String> rawList, int mode) {

        int signIndex;
        int intermediateResult;

        // производим подсчет ПОБЛОЧНО со заменой на результат по приоритету операций
        for (String s : Operations.getPrioritet()) {

            while (rawList.contains(s)) {  // ЕСЛИ ЗНАК ДЕЙСВИЯ ЕСТЬ .... А КАК ЕГО МОЖЕТ НЕ БЫТЬ????????????????

                signIndex = rawList.indexOf(s); // БЕРЕМ ЕГО ИНДЕКС в массиве

                int part_1 = Integer.parseInt(rawList.get(signIndex - 1));
                int part_2 = Integer.parseInt(rawList.get(signIndex + 1));

                // ограничения по условию задачи : только ччисла от 1 до 10
                if (mode==0) {
                    if(part_1>10 || part_2>10 || part_1<1 || part_2<1 ) {
                        throw new WrongFormatOfExpression("OUT OF LIMITATION NUMBER");
                    }
                }
                intermediateResult = Operations.operate(s, part_1, part_2);

                rawList.set(signIndex - 1, Integer.toString(intermediateResult));
                // удаление ДВАЖДЫ
                rawList.remove(signIndex);
                rawList.remove(signIndex);
            }
        }
        return rawList;
    }

    /**
     * Вычисляет результат одного выражение: число1_действие_число2
     * @param s Арефметический знак
     * @param left Левый блок выражения
     * @param right Правый блок выражения
     * @return Результат арефметического действия
     */
    private static int operate (String s, int left, int right) {

        switch (s) {
            case "*": return left * right;
            case "-": return left - right;
            case "/": return left / right;
            case "+": return left + right;
            default:
                throw new WrongFormatOfExpression("Неизвестное арифметическое действие");
        }
    }
}
