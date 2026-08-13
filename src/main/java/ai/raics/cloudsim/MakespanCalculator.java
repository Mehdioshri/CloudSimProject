package ai.raics.cloudsim;

import org.cloudsimplus.cloudlets.Cloudlet;

import java.util.List;

public class MakespanCalculator {

    /**
     * Calculates the makespan of a set of Cloudlets.
     *
     * Makespan = finish time of the last completed Cloudlet
     */
    public static double calculate(List<Cloudlet> cloudletList) {

        if (cloudletList == null || cloudletList.isEmpty()) {
            return 0.0;
        }

        double makespan = 0.0;

        for (Cloudlet cloudlet : cloudletList) {

            if (cloudlet.getFinishTime() > makespan) {
                makespan = cloudlet.getFinishTime();
            }
        }

        return makespan;
    }

    private MakespanCalculator() {
        // Prevent object creation
    }
}