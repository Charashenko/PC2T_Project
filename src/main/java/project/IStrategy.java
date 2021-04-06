package project;

import java.util.List;

public interface IStrategy {

    StrategyResult execute(List<Person> people) throws CustomException;

}
