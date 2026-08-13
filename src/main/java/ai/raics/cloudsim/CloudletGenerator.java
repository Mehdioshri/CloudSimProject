package ai.raics.cloudsim;

import org.cloudsimplus.cloudlets.Cloudlet;
import org.cloudsimplus.cloudlets.CloudletSimple;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class CloudletGenerator {

    private final Random random;

    public CloudletGenerator() {
        this.random = new Random(SimulationConfig.RANDOM_SEED);
    }

    /**
     * Creates CloudSim Plus Cloudlets.
     *
     * @param arrivalTimes arrival time of each task
     * @return list of generated Cloudlets
     */
    public List<Cloudlet> createCloudlets(
            List<Double> arrivalTimes) {

        List<Cloudlet> cloudletList = new ArrayList<>();

        for (int i = 0; i < arrivalTimes.size(); i++) {

            long taskLength = generateTaskLength();

            Cloudlet cloudlet = new CloudletSimple(
                    taskLength,
                    SimulationConfig.CLOUDLET_PES
            );

            cloudlet.setSubmissionDelay(
                    arrivalTimes.get(i)
            );

            cloudletList.add(cloudlet);
        }

        return cloudletList;
    }

    /**
     * Generates a random task length
     * between MIN_TASK_LENGTH and MAX_TASK_LENGTH.
     */
    private long generateTaskLength() {

        long min = SimulationConfig.MIN_TASK_LENGTH;
        long max = SimulationConfig.MAX_TASK_LENGTH;

        return min + random.nextLong(max - min + 1);
    }
}