package ai.raics.cloudsim;

import org.cloudsimplus.brokers.DatacenterBrokerSimple;
import org.cloudsimplus.cloudlets.Cloudlet;
import org.cloudsimplus.core.CloudSimPlus;
import org.cloudsimplus.vms.Vm;

import java.util.ArrayList;
import java.util.List;

public class QLearningBroker extends DatacenterBrokerSimple {

    private final QLearningAgent qLearningAgent;

    public QLearningBroker(
            CloudSimPlus simulation,
            QLearningAgent qLearningAgent) {

        super(simulation);

        this.qLearningAgent = qLearningAgent;
    }

    /**
     * Selects a VM for a Cloudlet using Q-Learning.
     */
    @Override
    public Vm defaultVmMapper(Cloudlet cloudlet) {

        List<Vm> vmList = getVmCreatedList();

        if (vmList == null || vmList.isEmpty()) {
            return Vm.NULL;
        }

        /*
         * Create available actions.
         * Each action represents one VM.
         */
        List<Action> availableActions = new ArrayList<>();

        for (Vm vm : vmList) {
            availableActions.add(
                    new Action((int) vm.getId())
            );
        }

        /*
         * Build the State used by our Q-Learning algorithm.
         *
         * IMPORTANT:
         * Explicitly use ai.raics.cloudsim.State
         * to avoid conflict with SimEntity.State.
         */

        long taskLength =
                cloudlet.getLength();

        /*
         * At this stage we use a simple
         * normalized load estimation.
         *
         * A more accurate dynamic load will be
         * connected later.
         */
        double vmLoad = 0.0;

        double vmMips =
                vmList.get(0).getMips();

        int vmQueueLength = 0;

        double waitingTime =
                cloudlet.getSubmissionDelay();

        ai.raics.cloudsim.State state =
                new ai.raics.cloudsim.State(
                        taskLength,
                        vmLoad,
                        vmMips,
                        vmQueueLength,
                        waitingTime
                );

        /*
         * Ask Q-Learning agent to select a VM.
         */
        Action selectedAction =
                qLearningAgent.chooseAction(
                        state,
                        availableActions
                );

        /*
         * Find the VM corresponding
         * to the selected action.
         */
        for (Vm vm : vmList) {

            if (vm.getId() ==
                    selectedAction.getVmId()) {

                return vm;
            }
        }

        return Vm.NULL;
    }
}