## Participante Strategy
```java
public interface Strategy {
    double calculateAverage(Map<Course, Integer> record);
    String gradesToString(Map<Course, Integer> record);
}
```
## Participantes:Concrete Strategy

```java
    public class StrategyArithmetic implements Strategy {
    @Override
    public double calculateAverage(Map<Course, Integer> record) {
        double sum = 0;
        for (Integer grade : record.values()) {
            sum += grade;
        }
        return sum / record.size();
    }

    @Override
    public String gradesToString(Map<Course, Integer> record) {
        StringBuilder sb = new StringBuilder(String.format("%6s | %10s | %50s | %8s | %5s \n",
                "Year",
                "Course ID",
                "Name",
                "Semester",
                "Grade"));

        for (Map.Entry<Course, Integer> entry : record.entrySet()) {
            Course c = entry.getKey();
            int grade = entry.getValue();

            String line = String.format("%6d | %10s | %50s | %8s | %5s \n",
                    c.getYear(),
                    c.getId(),
                    c.getName(),
                    c.getSemester(),
                    grade);

            sb.append(line);
        }
        return sb.toString();

    }
}
```

```java
    public class StrategyWeighted implements Strategy{
    @Override
    public double calculateAverage(Map<Course, Integer> record) {
        double total=0.0, sum=0.0;
        for(Course c: record.keySet()){
            total+=c.getEcts();
            double grade= record.get(c);
            sum+=grade*c.getEcts();
        }
        return sum/total;
    }

    @Override
    public String gradesToString(Map<Course, Integer> record) {
        StringBuilder sb = new StringBuilder(String.format("%6s | %10s | %50s | %8s | %5s | %4s\n",
                "Year",
                "Course ID",
                "Name",
                "Semester",
                "Grade",
                "ECTS"));

        for (Map.Entry<Course, Integer> entry : record.entrySet()) {
            Course c = entry.getKey();
            int grade = entry.getValue();

            String line = String.format("%6d | %10s | %50s | %8s | %5s |%4d \n",
                    c.getYear(),
                    c.getId(),
                    c.getName(),
                    c.getSemester(),
                    grade,
                    c.getEcts());

            sb.append(line);
        }
        return sb.toString();
    }
}
```

## Participante Context
```java
public class StudentRecord {

    private String studentId;
    private String studentName;
    Strategy strategy;

    private Map<Course, Integer> record;

    public StudentRecord(String studentId, String studentName,Strategy strategy) {
        this.studentId = studentId;
        this.studentName = studentName;
        this.strategy=strategy;
        this.record = new HashMap<>();
    }

    public void setStrategy(Strategy strategy) {
        this.strategy = strategy;
    }

    public String getStudentId() {
        return studentId;
    }

    public String getStudentName() {
        return studentName;
    }

    public double computeAverage() {
        return strategy.calculateAverage(record);

    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(String.format("Record of: %s | %s \n", studentId, studentName));
        sb.append(strategy.gradesToString(record));
        return sb.toString();
    }

    public void importFromFile(String filename) throws FileNotFoundException {
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;

            /* discard header*/
            br.readLine();

            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");

                int year = Integer.valueOf( values[0] );
                String name = values[1].trim();
                String id = values[2].trim();
                String semester = values[3].trim();
                int ects = Integer.valueOf( values[4] );

                int grade = Integer.valueOf( values[5] );

                Course c = new Course(id, year, name, semester, ects);

                record.put(c, grade);

            }
        } catch(IOException e) {
            System.err.println(e.getMessage());
        }
    }
}

```

## Main

```java
import pt.pa.patterns.StrategyArithmetic;
import pt.pa.patterns.StrategyWeighted;

public class Main {

    public static void main(String[] args) {

        StudentRecord record = new StudentRecord(
                "2018", "João Meireles",
                new StrategyArithmetic());
        try {
            record.importFromFile("record.csv");
        } catch (FileNotFoundException e) {
            System.err.println(e.getMessage());
        }

        /* Print record */
        System.out.println(record);

        /* Compute average */
        double average = record.computeAverage();
        System.out.println(String.format("Course average: %.2f", average));
        System.out.println("MUDAR DE ESTRATEGIA \n");
        record.setStrategy(new StrategyWeighted());
        System.out.println(record);
        average = record.computeAverage();
        System.out.println(String.format("Course average: %.2f", average));

    }
}
```