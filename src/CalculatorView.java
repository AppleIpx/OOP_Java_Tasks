import java.util.Scanner;

public class CalculatorView {
    public String getInput() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Введите математическое уравнение:");
        return scanner.nextLine();
    }

    public void displayResult(double result) {
        System.out.println("Результат: " + result);
    }

    public void displayError(String message) {
        System.out.println("Ошибка: " + message);
    }
}
