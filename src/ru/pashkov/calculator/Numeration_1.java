package ru.pashkov.calculator;

import java.util.*;

/**
 * Римская нумерация
 */
class Numeration_1 extends AbstractDecimalNumeration  {

    private final static String [] ROMAN  = {"N", "I", "V", "X", "L", "C", "D", "M" };

    @Override
    public String [] getNumeration() {
        return ROMAN;
    }

    @Override
    public String convertToSource(ArrayList<String> list) {

        StringBuilder result = new StringBuilder();
        StringBuilder numberSB = new StringBuilder(list.get(0));

        // ПРОВЕРКА НА ПРЕВЫШЕНИЕ ЧИСЛА 3999
        if (Integer.parseInt(numberSB.toString())>3999 || Integer.parseInt(numberSB.toString())<=0) {
            throw new WrongFormatOfExpression("OUT OF STANDART NUMERATION MAXIMUM '3999' OR MINIMUM '1'");

            } else {

                // делим на десятки индексом справа налево
                char [] ch  = numberSB.reverse().toString().toCharArray();

                int pow =1;
                for (int i = 0; i <ch.length ; i++) {

                    int digit = Integer.parseInt(String.valueOf(ch[i]));   // число в int
                    result.insert(0,convert(digit,i+1));     // добавляем в начало(0), так как строка ПЕРЕВЕРНУТА
                    pow = pow*10;
                }
        }
        return  result.toString();
    }

    private String convert(int digit, int standard) {

        String [][] mass = new String[10][5];

        mass[0][1] = "";
        mass[0][2] = "";
        mass[0][3] = "";
        mass[0][4] = "";

        mass[1][1] = "I";
        mass[2][1] = "II";
        mass[3][1] = "III";
        mass[4][1] = "IV";
        mass[5][1] = "V";
        mass[6][1] = "VI";
        mass[7][1] = "VII";
        mass[8][1] = "VIII";
        mass[9][1] = "IX";

        mass[1][2] = "X";
        mass[1][3] = "C";
        mass[1][4] = "M";

        mass[2][2] = "XX";
        mass[3][2] = "XXX";
        mass[4][2] = "XL";
        mass[5][2] = "L";
        mass[6][2] = "LX";
        mass[7][2] = "LXX";
        mass[8][2] = "LXXX";
        mass[9][2] = "XC";


        mass[2][3] = "CC";
        mass[3][3] = "CCC";
        mass[4][3] = "CD";
        mass[5][3] = "D";
        mass[6][3] = "DC";
        mass[7][3] = "DCC";
        mass[8][3] = "DCCC";
        mass[9][3] = "CM";

        mass[2][4] = "MM";
        mass[3][4] = "MMM";

        return mass[digit][standard];
    }

    /**
     * Конвертирует число цифра за цифрой: VIII - V(5),I(1),I(1),I(1).
     * @param rawList Входящее цифра в виде строки
     * @return цифру, сконвертированную в другую нумерацию
     */
    private  String convertNumberOneByOneFromLine(String rawList) {


        if (    rawList.contains("IIII") ||
                rawList.contains("XXXX") ||
                rawList.contains("CCCC") ||
                rawList.contains("MMMM") ||
                rawList.contains("VV")   ||
                rawList.contains("LL")   ||
                rawList.contains("DD")
        ) throw new WrongFormatOfExpression("Prohibited repetition's rule");



        int result = 0;                                 // результат работы метода
        List<Integer> sum = new ArrayList<>();

        Map<String, Integer> map = new LinkedHashMap<>();
        map.put("I",1);
        map.put("V",5);
        map.put("X",10);
        map.put("L",50);
        map.put("C",100);
        map.put("D",500);
        map.put("M",1000);
        map.put("Z",5000);                       // нужна только для подсчета МММ в начале числа (основа)


        String last = "Z";                       // начальное положение неиспользуемого числа для сравнения с первым (M)
        String flip = "Z";                       // первое значение в парах IX,XL,CM и тп (4,40,400,90,900)

        boolean isLastPare = false;              // была ли перед этим числом пара чисел типа IX, CM и тп



        for (int i = 0; i <rawList.length() ; i++) {

            String  part = rawList.substring(i, i + 1);         // часть составного числа, например I из IX


            for (String key : map.keySet()) {
                int curr  = map.get(key);                        // текущее значение ключа для сравнения
                int prev  = map.get(last);                       // предыдущее значение ключа для сравнения
                int prev2 = map.get(flip);                       // предыдущее значение ключа для сравнения


                boolean rightOrder = curr <= prev;               // правильный порядок цифр: текущая меньше предыдущей
                boolean isRightKey = key.equals(part);           // пренадлжежит ли число правильным ключам

                // 1 проверка на парные числа.
                boolean pare = ((last.equals("I") && key.equals("V")) ||  // 4
                                (last.equals("I") && key.equals("X")) ||    //9
                                (last.equals("X") && key.equals("L")) ||    //40
                                (last.equals("X") && key.equals("C")) ||    //90
                                (last.equals("C") && key.equals("D")) ||    //400
                                (last.equals("C") && key.equals("M"))       //900
                );


                if (map.containsKey(part) && !key.equals("Z")) {
                // одно общее исключение на весь блок, хотя можно и детализировать

                    //  следующее меньше предыдущего и перед этим НЕ было пары типа IX(9), XC(90) и тп
                    if (isRightKey && rightOrder && !isLastPare) {
                        last = key;
                        sum.add(curr);
                        isLastPare = false;
                        break;
                    }


                    // если перед этим БЫЛА пара, то новое число сравнивается с первым в той паре
                    if (isRightKey && rightOrder && isLastPare) {
                        if(curr < prev2) {
                        last = key;
                        sum.add(curr);
                        isLastPare = false;
                        break;
                        } else throw new WrongFormatOfExpression("invalid digit "+flip+last+"+"+key);
                    }


                    // пары  9  40  90 400 900
                    if (isRightKey && !rightOrder && pare && !isLastPare) {
                        sum.add(curr - prev * 2);
                        flip = last;             //  с ним будем сравнивать первую цифру после пары
                        last = key;
                        isLastPare = true;
                        break;
                    }
                    if (isRightKey && !rightOrder && !pare) {
                        throw new WrongFormatOfExpression("invalid pare "+last+key);
                    }
                    if (isRightKey && !rightOrder && pare && isLastPare) {
                        throw new WrongFormatOfExpression("invalid digit "+flip+last+"+"+key);
                    }

                 } else  {
                    throw new WrongFormatOfExpression("WRONG LETTER");
                }
            }
            result = sum.stream().mapToInt(d->d).sum();
        }
        return String.valueOf(result);
    }

    @Override
    public ArrayList<String> convertToStandard(String[] numbers) {

        ArrayList<String> convrtedListForOperations = new ArrayList<>();
        int StringSize = numbers.length;

        loop:
        for (int i = 0; i < numbers.length; i = i + 2) { // берем ЧИСЛО из массива - через одного.пропуская знаки действий

            for (int j = 0; j < numbers[i].length(); /*empty*/) { // берем цифру из числа

                String temp = numbers[i].substring(j, j + 1); // I....

                if (Arrays.asList(ROMAN).contains(temp)) {

                    j++; // инкремент цикила
                    if (numbers[i].length() == j && StringSize == i + 1) {
                        convrtedListForOperations.add(convertNumberOneByOneFromLine(numbers[i]));
                        break loop;
                    }   // следующая буква
                    else {
                        continue;
                    }
                }
            }
            convrtedListForOperations.add(convertNumberOneByOneFromLine(numbers[i]));
            convrtedListForOperations.add(numbers[i + 1]); //добавляем знак
        }
        return convrtedListForOperations;
    }
}
