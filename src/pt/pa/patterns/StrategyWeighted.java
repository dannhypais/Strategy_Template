package pt.pa.patterns;

import java.util.Map;

public class StrategyWeighted implements Strategy{
    @Override
    public double calculateAverage(Map<Course, Integer> record) {
        double total = 0.0, sum = 0.0;
        for(Course c : record.keySet()) {
            total += c.getEcts();
            double grade = record.get(c);
            sum += grade * c.getEcts();
        }
        return sum / total;
    }
}
