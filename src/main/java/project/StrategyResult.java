package project;

public class StrategyResult {

    private final boolean result;
    private final String message;

    /**
     * Result from strategy
     * @param result success/failure
     * @param message result message
     */
    public StrategyResult(boolean result, String message) {
        this.result = result;
        this.message = message;
    }

    /**
     * Gets result
     * @return true if success
     */
    public boolean getResult() {
        return result;
    }

    /**
     * Gets message
     * @return message
     */
    public String getMessage() {
        return message;
    }
}
