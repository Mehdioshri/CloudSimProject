package ai.raics.cloudsim;

public class Reward {

    /*
     * Weight assigned to each objective.
     *
     * Lower load      -> better
     * Lower waiting   -> better
     * Lower queue     -> better
     */
    private static final double LOAD_WEIGHT = 0.4;
    private static final double WAITING_WEIGHT = 0.4;
    private static final double QUEUE_WEIGHT = 0.2;

    /**
     * Calculates the reward for assigning a task to a VM.
     *
     * Higher reward means a better VM selection.
     */
    public static double calculate(
            double vmLoad,
            double waitingTime,
            int queueLength) {

        double loadPenalty = vmLoad;

        double waitingPenalty = waitingTime;

        double queuePenalty = queueLength;

        double penalty =
                LOAD_WEIGHT * loadPenalty
                + WAITING_WEIGHT * waitingPenalty
                + QUEUE_WEIGHT * queuePenalty;

        return -penalty;
    }

    private Reward() {
        // Prevent object creation
    }
}