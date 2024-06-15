package calculator;

public class CalculatorController {
    private CalculatorModel model;
    private CalculatorView view;

    public CalculatorController(CalculatorModel model, CalculatorView view) {
        this.model = model;
        this.view = view;
    }

    public void processEquation() {
        try {
            String equation = view.getInput();
            double result = model.calculate(equation);
            view.displayResult(result);
        } catch (Exception e) {
            view.displayError(e.getMessage());
        }
    }
}
