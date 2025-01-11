package pt.pa.patterns;

import java.util.Map;

public class StrategyArithmetic implements Strategy{
    @Override
    public double calculateAverage(Map<Course, Integer> record) {
        double sum = 0;
        for(Integer grade: record.values()){
            sum += grade;
        }
        return sum / record.size();
    }
}
