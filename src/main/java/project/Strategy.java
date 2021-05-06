package project;

import java.util.List;

public class Strategy {

    private final IStrategy strategy;

    /**
     * Main strategy object
     * @param strategy strategy to be executed
     */
    public Strategy(IStrategy strategy) {
        this.strategy = strategy;
    }

    /**
     * Executes current strategy
     * @param people RAM database
     * @return Result of strategy execution
     * @throws CustomException Raised when scanning ID, user inputs int out of the range
     */
    public StrategyResult executeStrategy(List<Person> people) throws CustomException {
        return strategy.execute(people);
    }
}
