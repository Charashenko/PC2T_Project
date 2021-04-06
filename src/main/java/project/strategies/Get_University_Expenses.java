package project.strategies;

import project.*;

import java.util.List;

public class Get_University_Expenses implements IStrategy {

    @Override
    public StrategyResult execute(List<Person> people) throws CustomException {
        return new StrategyResult(true, String.format("Total university expenses are %.2f", University.getExpenses()));
    }
}
