package ai.raics.cloudsim;

import java.util.LinkedHashMap;
import java.util.Map;

public class ResultsAnalyzer {

    /**
     * Stores the makespan of each scheduling algorithm.
     */
    private final Map<String, Double> makespanResults;

    /**
     * Stores the total execution time of each algorithm.
     */
    private final Map<String, Double> executionTimeResults;

    /**
     * Stores the average waiting time of each algorithm.
     */
    private final Map<String, Double> waitingTimeResults;

    /**
     * Stores the load imbalance of each algorithm.
     */
    private final Map<String, Double> loadImbalanceResults;


    public ResultsAnalyzer() {

        makespanResults = new LinkedHashMap<>();
        executionTimeResults = new LinkedHashMap<>();
        waitingTimeResults = new LinkedHashMap<>();
        loadImbalanceResults = new LinkedHashMap<>();
    }


    /**
     * Adds a result for a scheduling algorithm.
     *
     * @param algorithmName name of the algorithm
     * @param makespan total simulation completion time
     * @param executionTime total execution time
     * @param waitingTime average waiting time
     * @param loadImbalance load imbalance value
     */
    public void addResult(
            String algorithmName,
            double makespan,
            double executionTime,
            double waitingTime,
            double loadImbalance) {

        makespanResults.put(algorithmName, makespan);

        executionTimeResults.put(
                algorithmName,
                executionTime
        );

        waitingTimeResults.put(
                algorithmName,
                waitingTime
        );

        loadImbalanceResults.put(
                algorithmName,
                loadImbalance
        );
    }


    /**
     * Prints all results in a table.
     */
    public void printResults() {

        System.out.println();
        System.out.println("==============================================================");
        System.out.println("                    SIMULATION RESULTS");
        System.out.println("==============================================================");

        System.out.printf(
                "%-20s %-12s %-15s %-15s %-15s%n",
                "Algorithm",
                "Makespan",
                "Exec.Time",
                "Avg.Waiting",
                "Load Imbalance"
        );

        System.out.println(
                "--------------------------------------------------------------"
        );

        for (String algorithm : makespanResults.keySet()) {

            System.out.printf(
                    "%-20s %-12.2f %-15.2f %-15.2f %-15.2f%n",
                    algorithm,
                    makespanResults.get(algorithm),
                    executionTimeResults.get(algorithm),
                    waitingTimeResults.get(algorithm),
                    loadImbalanceResults.get(algorithm)
            );
        }

        System.out.println(
                "=============================================================="
        );
    }


    /**
     * Returns the algorithm with the lowest makespan.
     */
    public String getBestMakespanAlgorithm() {

        return makespanResults
                .entrySet()
                .stream()
                .min(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse("N/A");
    }


    /**
     * Returns the lowest makespan.
     */
    public double getBestMakespan() {

        return makespanResults
                .values()
                .stream()
                .min(Double::compare)
                .orElse(Double.NaN);
    }


    /**
     * Prints the best algorithm according to makespan.
     */
    public void printBestAlgorithm() {

        String algorithm = getBestMakespanAlgorithm();
        double makespan = getBestMakespan();

        System.out.println();
        System.out.println("Best algorithm based on Makespan:");
        System.out.printf(
                "%s -> %.2f%n",
                algorithm,
                makespan
        );
    }
}