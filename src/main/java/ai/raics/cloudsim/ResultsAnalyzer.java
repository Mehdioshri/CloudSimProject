package ai.raics.cloudsim;

import org.cloudsimplus.cloudlets.Cloudlet;
import org.cloudsimplus.vms.Vm;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ResultsAnalyzer {

    private final Map<String, Double> makespanResults;
    private final Map<String, Double> executionTimeResults;
    private final Map<String, Double> waitingTimeResults;
    private final Map<String, Double> loadImbalanceResults;

    public ResultsAnalyzer() {
        makespanResults = new LinkedHashMap<>();
        executionTimeResults = new LinkedHashMap<>();
        waitingTimeResults = new LinkedHashMap<>();
        loadImbalanceResults = new LinkedHashMap<>();
    }

    /**
     * Analyzes simulation results.
     */
    public void analyze(
            List<Cloudlet> finishedCloudlets,
            List<Vm> vmList,
            String algorithmName,
            double lambda) {

        if (finishedCloudlets == null || finishedCloudlets.isEmpty()) {

            System.out.println();
            System.out.println("No finished Cloudlets to analyze.");
            return;
        }

        /*
         * =========================================================
         * 1. MAKESPAN
         * =========================================================
         *
         * Makespan = finish time of the last Cloudlet.
         */

        double makespan = finishedCloudlets
                .stream()
                .mapToDouble(Cloudlet::getFinishTime)
                .max()
                .orElse(0.0);


        /*
         * =========================================================
         * 2. EXECUTION TIME
         * =========================================================
         *
         * Execution Time =
         * Finish Time - Start Time
         */

        double totalExecutionTime = 0.0;

        for (Cloudlet cloudlet : finishedCloudlets) {

            double startTime =
                    cloudlet.getStartTime();

            double finishTime =
                    cloudlet.getFinishTime();

            double executionTime =
                    Math.max(
                            0.0,
                            finishTime - startTime
                    );

            totalExecutionTime += executionTime;
        }

        double averageExecutionTime =
                totalExecutionTime /
                        finishedCloudlets.size();


        /*
         * =========================================================
         * 3. WAITING TIME
         * =========================================================
         *
         * Waiting Time =
         * Start Time - Submission Time
         *
         * Submission Time is represented by
         * Cloudlet.getSubmissionDelay().
         */

        double totalWaitingTime = 0.0;

        for (Cloudlet cloudlet : finishedCloudlets) {

            double submissionTime =
                    cloudlet.getSubmissionDelay();

            double startTime =
                    cloudlet.getStartTime();

            double waitingTime =
                    Math.max(
                            0.0,
                            startTime - submissionTime
                    );

            totalWaitingTime += waitingTime;
        }

        double averageWaitingTime =
                totalWaitingTime /
                        finishedCloudlets.size();


        /*
         * =========================================================
         * 4. VM WORKLOAD
         * =========================================================
         */

        Map<Long, Double> vmWorkload =
                new LinkedHashMap<>();

        for (Vm vm : vmList) {

            vmWorkload.put(
                    vm.getId(),
                    0.0
            );
        }


        /*
         * Calculate workload assigned to each VM.
         */

        for (Cloudlet cloudlet : finishedCloudlets) {

            Vm vm = cloudlet.getVm();

            if (vm != null) {

                long vmId =
                        vm.getId();

                double startTime =
                        cloudlet.getStartTime();

                double finishTime =
                        cloudlet.getFinishTime();

                double executionTime =
                        Math.max(
                                0.0,
                                finishTime - startTime
                        );

                vmWorkload.put(
                        vmId,
                        vmWorkload.getOrDefault(
                                vmId,
                                0.0
                        ) + executionTime
                );
            }
        }


        /*
         * =========================================================
         * 5. MAXIMUM VM LOAD
         * =========================================================
         */

        double maxLoad =
                vmWorkload
                        .values()
                        .stream()
                        .mapToDouble(
                                Double::doubleValue
                        )
                        .max()
                        .orElse(0.0);


        /*
         * =========================================================
         * 6. MINIMUM VM LOAD
         * =========================================================
         */

        double minLoad =
                vmWorkload
                        .values()
                        .stream()
                        .mapToDouble(
                                Double::doubleValue
                        )
                        .min()
                        .orElse(0.0);


        /*
         * =========================================================
         * 7. AVERAGE VM LOAD
         * =========================================================
         */

        double averageLoad =
                vmWorkload
                        .values()
                        .stream()
                        .mapToDouble(
                                Double::doubleValue
                        )
                        .average()
                        .orElse(0.0);


        /*
         * =========================================================
         * 8. LOAD IMBALANCE
         * =========================================================
         *
         * Load Imbalance =
         *
         * (Maximum Load - Minimum Load)
         * -----------------------------
         *       Average Load
         */

        double loadImbalance = 0.0;

        if (averageLoad > 0.0) {

            loadImbalance =
                    (maxLoad - minLoad)
                            / averageLoad;
        }


        /*
         * =========================================================
         * 9. AVERAGE VM UTILIZATION
         * =========================================================
         */

        double averageVmUtilization = 0.0;

        if (makespan > 0.0 && !vmList.isEmpty()) {

            double totalUtilization = 0.0;

            for (double workload :
                    vmWorkload.values()) {

                double utilization =
                        workload / makespan;

                totalUtilization += utilization;
            }

            averageVmUtilization =
                    totalUtilization /
                            vmList.size();
        }


        /*
         * =========================================================
         * 10. THROUGHPUT
         * =========================================================
         *
         * Throughput =
         * Completed Cloudlets / Makespan
         */

        double throughput = 0.0;

        if (makespan > 0.0) {

            throughput =
                    finishedCloudlets.size()
                            / makespan;
        }


        /*
         * =========================================================
         * 11. SAVE SUMMARY RESULTS
         * =========================================================
         */

        makespanResults.put(
                algorithmName,
                makespan
        );

        executionTimeResults.put(
                algorithmName,
                totalExecutionTime
        );

        waitingTimeResults.put(
                algorithmName,
                averageWaitingTime
        );

        loadImbalanceResults.put(
                algorithmName,
                loadImbalance
        );


        /*
         * =========================================================
         * 12. PRINT RESULTS
         * =========================================================
         */

        printSummary(
                algorithmName,
                lambda,
                finishedCloudlets.size(),
                makespan,
                totalExecutionTime,
                averageExecutionTime,
                averageWaitingTime,
                loadImbalance,
                averageVmUtilization,
                throughput
        );


        /*
         * =========================================================
         * 13. EXPORT CLOUDLET RESULTS
         * =========================================================
         */

        writeCloudletResultsCsv(
                finishedCloudlets,
                algorithmName,
                lambda
        );


        /*
         * =========================================================
         * 14. EXPORT SUMMARY RESULTS
         * =========================================================
         */

        writeSummaryCsv(
                algorithmName,
                lambda,
                finishedCloudlets.size(),
                makespan,
                totalExecutionTime,
                averageExecutionTime,
                averageWaitingTime,
                loadImbalance,
                averageVmUtilization,
                throughput
        );
    }


    /**
     * Prints simulation results.
     */
    private void printSummary(
            String algorithmName,
            double lambda,
            int numberOfCloudlets,
            double makespan,
            double totalExecutionTime,
            double averageExecutionTime,
            double averageWaitingTime,
            double loadImbalance,
            double averageVmUtilization,
            double throughput) {

        System.out.println();

        System.out.println(
                "=============================================================="
        );

        System.out.println(
                "                    RESULTS ANALYZER"
        );

        System.out.println(
                "=============================================================="
        );

        System.out.println(
                "Algorithm              : "
                        + algorithmName
        );

        System.out.println(
                "Poisson Lambda         : "
                        + lambda
        );

        System.out.println(
                "Completed Cloudlets    : "
                        + numberOfCloudlets
        );

        System.out.println(
                "--------------------------------------------------------------"
        );

        System.out.printf(
                "Makespan               : %.4f%n",
                makespan
        );

        System.out.printf(
                "Total Execution Time   : %.4f%n",
                totalExecutionTime
        );

        System.out.printf(
                "Average Execution Time : %.4f%n",
                averageExecutionTime
        );

        System.out.printf(
                "Average Waiting Time   : %.4f%n",
                averageWaitingTime
        );

        System.out.printf(
                "Load Imbalance         : %.4f%n",
                loadImbalance
        );

        System.out.printf(
                "Average VM Utilization : %.4f%n",
                averageVmUtilization
        );

        System.out.printf(
                "Throughput             : %.4f%n",
                throughput
        );

        System.out.println(
                "=============================================================="
        );
    }


    /**
     * Creates detailed CSV file for Cloudlets.
     */
    private void writeCloudletResultsCsv(
            List<Cloudlet> cloudlets,
            String algorithmName,
            double lambda) {

        String fileName =
                "cloudlet-results-"
                        + algorithmName
                        + "-lambda-"
                        + lambda
                        + ".csv";

        try (
                PrintWriter writer =
                        new PrintWriter(
                                new FileWriter(fileName)
                        )
        ) {

            writer.println(
                    "Algorithm,"
                            + "Lambda,"
                            + "CloudletId,"
                            + "Length,"
                            + "VmId,"
                            + "SubmissionTime,"
                            + "StartTime,"
                            + "FinishTime,"
                            + "ExecutionTime,"
                            + "WaitingTime"
            );


            for (Cloudlet cloudlet :
                    cloudlets) {

                long cloudletId =
                        cloudlet.getId();

                long length =
                        cloudlet.getLength();

                long vmId = -1;

                if (cloudlet.getVm() != null) {

                    vmId =
                            cloudlet.getVm().getId();
                }

                double submissionTime =
                        cloudlet.getSubmissionDelay();

                double startTime =
                        cloudlet.getStartTime();

                double finishTime =
                        cloudlet.getFinishTime();

                double executionTime =
                        Math.max(
                                0.0,
                                finishTime - startTime
                        );

                double waitingTime =
                        Math.max(
                                0.0,
                                startTime - submissionTime
                        );


                writer.printf(
                        "%s,%.4f,%d,%d,%d,%.4f,%.4f,%.4f,%.4f,%.4f%n",

                        algorithmName,
                        lambda,
                        cloudletId,
                        length,
                        vmId,
                        submissionTime,
                        startTime,
                        finishTime,
                        executionTime,
                        waitingTime
                );
            }

            System.out.println();

            System.out.println(
                    "Detailed CSV created: "
                            + fileName
            );

        } catch (IOException e) {

            System.out.println(
                    "Error writing Cloudlet CSV: "
                            + e.getMessage()
            );
        }
    }


    /**
     * Creates/updates summary CSV file.
     */
    private void writeSummaryCsv(
            String algorithmName,
            double lambda,
            int numberOfCloudlets,
            double makespan,
            double totalExecutionTime,
            double averageExecutionTime,
            double averageWaitingTime,
            double loadImbalance,
            double averageVmUtilization,
            double throughput) {

        String fileName =
                "summary-results.csv";

        boolean fileExists =
                new java.io.File(fileName).exists();

        try (
                PrintWriter writer =
                        new PrintWriter(
                                new FileWriter(
                                        fileName,
                                        true
                                )
                        )
        ) {

            if (!fileExists) {

                writer.println(
                        "Algorithm,"
                                + "Lambda,"
                                + "Cloudlets,"
                                + "Makespan,"
                                + "TotalExecutionTime,"
                                + "AverageExecutionTime,"
                                + "AverageWaitingTime,"
                                + "LoadImbalance,"
                                + "AverageVMUtilization,"
                                + "Throughput"
                );
            }


            writer.printf(
                    "%s,%.4f,%d,%.4f,%.4f,%.4f,%.4f,%.4f,%.4f,%.4f%n",

                    algorithmName,
                    lambda,
                    numberOfCloudlets,
                    makespan,
                    totalExecutionTime,
                    averageExecutionTime,
                    averageWaitingTime,
                    loadImbalance,
                    averageVmUtilization,
                    throughput
            );

            System.out.println(
                    "Summary CSV updated: "
                            + fileName
            );

        } catch (IOException e) {

            System.out.println(
                    "Error writing summary CSV: "
                            + e.getMessage()
            );
        }
    }


    /**
     * Returns the algorithm with the lowest Makespan.
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
     * Returns the lowest Makespan.
     */
    public double getBestMakespan() {

        return makespanResults
                .values()
                .stream()
                .min(Double::compare)
                .orElse(Double.NaN);
    }


    /**
     * Prints the best algorithm.
     */
    public void printBestAlgorithm() {

        String algorithm =
                getBestMakespanAlgorithm();

        double makespan =
                getBestMakespan();

        System.out.println();

        System.out.println(
                "Best algorithm based on Makespan:"
        );

        System.out.printf(
                "%s -> %.4f%n",
                algorithm,
                makespan
        );
    }
}