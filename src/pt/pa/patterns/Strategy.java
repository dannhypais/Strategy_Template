package pt.pa.patterns;

import java.util.Map;

public interface Strategy {
    double calculateAverage(Map<Course, Integer> record);
}
