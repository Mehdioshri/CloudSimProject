package ai.raics.cloudsim;

import java.util.Objects;

public class Action {

    private final int vmId;

    public Action(int vmId) {
        this.vmId = vmId;
    }

    public int getVmId() {
        return vmId;
    }

    @Override
    public boolean equals(Object obj) {

        if (this == obj) {
            return true;
        }

        if (!(obj instanceof Action)) {
            return false;
        }

        Action other = (Action) obj;

        return vmId == other.vmId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(vmId);
    }

    @Override
    public String toString() {
        return "Action{vmId=" + vmId + "}";
    }
}
