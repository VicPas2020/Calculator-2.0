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
     * @return сконвертированную в другую нумерацию цифру
     */
    private  String convertNumberOneByOneFromLine(String rawList) {

            int result = 0;
            List<Integer> sum = new ArrayList<>();

            Map<String, Integer> map = new LinkedHashMap<>();
            map.put("I",1);
            map.put("V",5);
            map.put("X",10);
            map.put("L",50);
            map.put("C",100);
            map.put("D",500);
            map.put("M",1000);
            map.put("Z",5000);                      // нужна только для подсчета МММ в начале числа


            String last = "Z";                      // начальное положение нулевого числа для сравнения с первым
            String flip = "";                       // последнее значение в парах XI,XL и тп (5,50,500,90,900)
            int count   = 0;                        // подсчет повторений чисел кратных 1-10: I,X,C,M
            String can  = "DLV";                    // числа, которые не могут повторяться

            for (int i = 0; i <rawList.length() ; i++) {

                String  part = rawList.substring(i, i + 1);         // часть составного числа, например I из IX

                for (String key : map.keySet()) {

                    int curr = map.get(key);                        // текущее значение ключа для сравнения
                    int prev = map.get(last);                       // предыдущее значение ключа для сравнения
                    boolean rightOrder = curr < prev;               // правильный порядок цифр: текущая меньше предыдущей

                    boolean isRightKey      = key.equals(part);     // пренадлжежит ли число правильным ключам
                    boolean isKeyEqualsLast = key.equals(last);     // совпадает ли число с предыдущим (для пар IX,XL...)
                    boolean isKeyFlipNumber = flip.equals(key);     // была ли пара IX,XL... последней
                    boolean canRepeat       = can.contains(key);    // можно ли повторять данное число

                    if (map.containsKey(part) && !key.equals("Z")) {

                        if (isRightKey && !isKeyEqualsLast && rightOrder && !isKeyFlipNumber) {
                            last = key;
                            count = 0;
                            sum.add(curr);
                            break;
                        }
                        // пока оставлю этот метод, хотя не понятно что он делает
                        if (isRightKey && !isKeyEqualsLast && rightOrder && isKeyFlipNumber) {
                            throw new WrongFormatOfExpression("UNKNOWN ERROR in convertNumberOneByOneFromLine method");
                        }

                        if (isRightKey && isKeyEqualsLast  && count < 2 && !isKeyFlipNumber && !canRepeat) {
                            count++;
                            sum.add(curr);
                            break;
                        }
                        if (isRightKey && isKeyEqualsLast  && canRepeat) {
                            throw new WrongFormatOfExpression("'D-L-V' REPETITION IS PROHIBITED");
                        }
                        if (isRightKey && isKeyEqualsLast  && count == 2 && !isKeyFlipNumber) {
                            throw new WrongFormatOfExpression("WRONG REPETITION I-X-C-M");
                        }
                        if (isRightKey && isKeyEqualsLast  && isKeyFlipNumber) {
                            throw new WrongFormatOfExpression("WRONG BACKWARD PAIRING");
                        }
                        if (isRightKey && !isKeyEqualsLast && !rightOrder) {

                            if (    (last.equals("I") && key.equals("V")) ||
                                    (last.equals("I") && key.equals("X")) ||
                                    (last.equals("X") && key.equals("L")) ||
                                    (last.equals("X") && key.equals("C")) ||
                                    (last.equals("C") && key.equals("D")) ||
                                    (last.equals("C") && key.equals("M"))
                                )
                            {
                                count = 0;
                                sum.add(curr - prev * 2);
                                flip = key;
                                last = key;
                                break;
                            } else {
                                throw new WrongFormatOfExpression("WRONG BACKWARD PAIRING");
                            }
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
