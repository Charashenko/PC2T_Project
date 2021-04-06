package project;

public class StrategyResult {

    private final boolean result;
    private final String message;

    public StrategyResult(boolean result, String message) {
        this.result = result;
        this.message = message;
    }

    public boolean getResult() {
        return result;
    }

    public String getMessage() {
        return message;
    }
}
