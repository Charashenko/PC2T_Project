package project;

import java.util.List;

public class Strategy {

    private final IStrategy strategy;
    public Strategy(IStrategy strategy) {
        this.strategy = strategy;
    }

    public StrategyResult executeStrategy(List<Person> people) throws CustomException {
        return strategy.execute(people);
    }
}
