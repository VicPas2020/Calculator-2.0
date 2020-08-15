package ru.pashkov.calculator;

import java.util.ArrayList;
import java.util.Scanner;

public class Calculator {

    /**
     * Публичный класс для включения калькулятора
     * @param mode Переключатель режима работы калькулятора
     *             0-строго по условиям задачи
     *             1-расширенный функционал (пробуйте!)
     * @return  Результат вычисления в виде строки исходной нумерации
     */
   public String calculate(int mode) {

       String result = null;

       // Получаем строку из сканера
       System.out.println("input:");
       Scanner sc = new Scanner(System.in);
       while (sc.hasNext()) {

           String scannerLine = sc.nextLine();

           // создаем три объекта нумераций
           Numeration_1 num1 = new Numeration_1();
           Numeration_2 num2 = new Numeration_2();
           Numeration_3 num3 = new Numeration_3();

           // создаем ЛИСТ объектов нумераций
           ArrayList<AbstractDecimalNumeration> list = new ArrayList<>();

           list.add(num1);
           list.add(num2);
           list.add(num3);

           // проверка на формат калькулятора: блок+действие+блок.....
           boolean boo = AbstractDecimalNumeration.isCalculatorFormat(/*list,*/scannerLine, mode);
                if (!boo) throw new WrongFormatOfExpression("Wrong format of calculator");

           // Приводим строку в стандарнтный вид: каждый элемент разделен одним пробелом.
           String[] cleanList = AbstractDecimalNumeration.RefactoringOfInput(scannerLine);

           // определяем нумерацию (римский, арабский....)
           AbstractDecimalNumeration numerationFormat = AbstractDecimalNumeration.currentFormat(list, cleanList);

           // Конвертируем выражение в стандартную нумерацию 1,2,3.....9
           ArrayList<String> lineInStandardNumeration = numerationFormat.convertToStandard(cleanList);

           // Производим математические действия в стандартной нумерации
           ArrayList<String> preResult = Operations.calculateThis(lineInStandardNumeration, mode);

           // производим конвертацию результата в исходную нумераци.
           result = numerationFormat.convertToSource(preResult);

           // выводим результат в консоль
           System.out.println("output:\n" + result);
       }
       return result;
   }
}
