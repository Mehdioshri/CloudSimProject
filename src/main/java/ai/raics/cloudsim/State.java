package ai.raics.cloudsim;

import java.util.Objects;

public class State {

    private final long taskLength;
    private final double vmLoad;
    private final double vmMips;
    private final int vmQueueLength;
    private final double waitingTime;

    public State(
            long taskLength,
            double vmLoad,
            double vmMips,
            int vmQueueLength,
            double waitingTime) {

        this.taskLength = taskLength;
        this.vmLoad = vmLoad;
        this.vmMips = vmMips;
        this.vmQueueLength = vmQueueLength;
        this.waitingTime = waitingTime;
    }

    public long getTaskLength() {
        return taskLength;
    }

    public double getVmLoad() {
        return vmLoad;
    }

    public double getVmMips() {
        return vmMips;
    }

    public int getVmQueueLength() {
        return vmQueueLength;
    }

    public double getWaitingTime() {
        return waitingTime;
    }

    @Override
    public boolean equals(Object obj) {

        if (this == obj) {
            return true;
        }

        if (!(obj instanceof State)) {
            return false;
        }

        State other = (State) obj;

        return taskLength == other.taskLength
                && Double.compare(vmLoad, other.vmLoad) == 0
                && Double.compare(vmMips, other.vmMips) == 0
                && vmQueueLength == other.vmQueueLength
                && Double.compare(waitingTime, other.waitingTime) == 0;
    }

    @Override
    public int hashCode() {

        return Objects.hash(
                taskLength,
                vmLoad,
                vmMips,
                vmQueueLength,
                waitingTime
        );
    }

    @Override
    public String toString() {

        return String.format(
                "State{taskLength=%d, vmLoad=%.2f, vmMips=%.2f, " +
                "queue=%d, waitingTime=%.2f}",
                taskLength,
                vmLoad,
                vmMips,
                vmQueueLength,
                waitingTime
        );
    }
}
