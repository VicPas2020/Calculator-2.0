package another_package;

import ru.pashkov.calculator.Calculator;


public class Main {



    public static void main(String[] args) {

        /**
 *  режимы работы калькулятора
 *  mode 0 - калькулятор работает в режиме, строго заданном в условии задания
 *  mode 1 - значительно расширяет возможности калькулятора [проверьте сами]
 */
        Calculator calc = new Calculator();
        calc.calculate(1);

    }
}

