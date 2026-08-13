package ai.raics.cloudsim;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class PoissonTaskGenerator {

    private final Random random;

    public PoissonTaskGenerator() {
        this.random = new Random(SimulationConfig.RANDOM_SEED);
    }

    /**
     * Generates task arrival times using a Poisson process.
     *
     * @param numberOfTasks number of tasks to generate
     * @param lambda arrival rate
     * @return list of task arrival times
     */
    public List<Double> generateArrivalTimes(
            int numberOfTasks,
            double lambda) {

        List<Double> arrivalTimes = new ArrayList<>();

        double currentTime = 0.0;

        for (int i = 0; i < numberOfTasks; i++) {

            // Generate a uniform random number between 0 and 1
            double u = random.nextDouble();

            // Avoid log(0)
            if (u == 0.0) {
                u = Double.MIN_VALUE;
            }

            // Exponential inter-arrival time
            double interArrivalTime =
                    -Math.log(1.0 - u) / lambda;

            currentTime += interArrivalTime;

            arrivalTimes.add(currentTime);
        }

        return arrivalTimes;
    }


    /**
     * Generates 1000 task arrival times
     * using the specified lambda.
     */
    public List<Double> generateTasks(double lambda) {

        return generateArrivalTimes(
                SimulationConfig.NUMBER_OF_CLOUDLETS,
                lambda
        );
    }
}