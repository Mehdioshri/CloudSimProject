package ai.raics.cloudsim;

import org.cloudsimplus.cloudlets.Cloudlet;
import org.cloudsimplus.vms.Vm;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MinMinScheduler {

    /**
     * Schedules Cloudlets using the Min-Min algorithm.
     *
     * Min-Min:
     * 1. For every unscheduled Cloudlet, find the VM
     *    where it has the earliest estimated finish time.
     * 2. Among those Cloudlet-VM pairs, select the one
     *    with the globally smallest finish time.
     * 3. Assign that Cloudlet to that VM.
     * 4. Update the estimated available time of the VM.
     * 5. Repeat until all Cloudlets are scheduled.
     */
    public void schedule(
            List<Cloudlet> cloudletList,
            List<Vm> vmList) {

        if (cloudletList == null || cloudletList.isEmpty()) {
            return;
        }

        if (vmList == null || vmList.isEmpty()) {
            return;
        }

        /*
         * Keep the estimated time at which
         * each VM becomes available.
         */
        Map<Vm, Double> vmAvailableTime =
                new HashMap<>();

        for (Vm vm : vmList) {
            vmAvailableTime.put(vm, 0.0);
        }

        /*
         * Cloudlets that still need to be scheduled.
         */
        List<Cloudlet> unscheduled =
                new ArrayList<>(cloudletList);

        /*
         * Sort according to arrival/submission time.
         *
         * This makes the input deterministic and allows
         * arrival times to be considered when evaluating
         * the estimated start time.
         */
        unscheduled.sort(
                Comparator.comparingDouble(
                        Cloudlet::getSubmissionDelay
                )
        );

        while (!unscheduled.isEmpty()) {

            Cloudlet selectedCloudlet = null;
            Vm selectedVm = null;

            double globalMinimumFinishTime =
                    Double.MAX_VALUE;

            /*
             * Examine every unscheduled Cloudlet.
             */
            for (Cloudlet cloudlet : unscheduled) {

                /*
                 * Arrival time of the Cloudlet.
                 */
                double arrivalTime =
                        cloudlet.getSubmissionDelay();

                /*
                 * For this Cloudlet find its best VM.
                 */
                double cloudletMinimumFinishTime =
                        Double.MAX_VALUE;

                Vm cloudletBestVm = null;

                for (Vm vm : vmList) {

                    double mips =
                            vm.getMips();

                    if (mips <= 0) {
                        continue;
                    }

                    /*
                     * Estimated execution time.
                     *
                     * Cloudlet length is in MI.
                     * VM MIPS is Million Instructions/second.
                     */
                    double executionTime =
                            cloudlet.getLength() / mips;

                    /*
                     * The VM can start the Cloudlet only
                     * after both:
                     *
                     * 1. The Cloudlet has arrived.
                     * 2. The VM has become available.
                     */
                    double startTime =
                            Math.max(
                                    arrivalTime,
                                    vmAvailableTime.get(vm)
                            );

                    /*
                     * Estimated finish time.
                     */
                    double finishTime =
                            startTime + executionTime;

                    /*
                     * Find the best VM for this Cloudlet.
                     */
                    if (finishTime <
                            cloudletMinimumFinishTime) {

                        cloudletMinimumFinishTime =
                                finishTime;

                        cloudletBestVm = vm;
                    }
                }

                /*
                 * Now compare this Cloudlet's best VM
                 * with the best choice found for all
                 * other Cloudlets.
                 */
                if (cloudletBestVm != null &&
                        cloudletMinimumFinishTime <
                                globalMinimumFinishTime) {

                    globalMinimumFinishTime =
                            cloudletMinimumFinishTime;

                    selectedCloudlet =
                            cloudlet;

                    selectedVm =
                            cloudletBestVm;
                }
            }

            /*
             * Assign the selected Cloudlet to the selected VM.
             */
            if (selectedCloudlet != null &&
                    selectedVm != null) {

                selectedCloudlet.setVm(selectedVm);

                /*
                 * Calculate the execution time again
                 * for updating the VM availability.
                 */
                double executionTime =
                        selectedCloudlet.getLength()
                                / selectedVm.getMips();

                double startTime =
                        Math.max(
                                selectedCloudlet
                                        .getSubmissionDelay(),
                                vmAvailableTime
                                        .get(selectedVm)
                        );

                double finishTime =
                        startTime + executionTime;

                /*
                 * The VM is now considered busy until
                 * this estimated finish time.
                 */
                vmAvailableTime.put(
                        selectedVm,
                        finishTime
                );

                /*
                 * Remove the Cloudlet from the
                 * unscheduled list.
                 */
                unscheduled.remove(
                        selectedCloudlet
                );
            } else {
                /*
                 * Safety condition.
                 */
                break;
            }
        }
    }
}
 