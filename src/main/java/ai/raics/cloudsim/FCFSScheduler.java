package ai.raics.cloudsim;

import org.cloudsimplus.cloudlets.Cloudlet;
import org.cloudsimplus.vms.Vm;

import java.util.Comparator;
import java.util.List;

public class FCFSScheduler {

    /**
     * Schedules Cloudlets using
     * First Come, First Served (FCFS).
     *
     * Cloudlets are sorted according to
     * their submission/arrival time.
     */
    public void schedule(
            List<Cloudlet> cloudletList,
            List<Vm> vmList) {

        /*
         * ---------------------------------------------------------
         * 1. Check Cloudlet list
         * ---------------------------------------------------------
         */

        if (cloudletList == null ||
                cloudletList.isEmpty()) {

            System.out.println(
                    "FCFS: No Cloudlets to schedule."
            );

            return;
        }


        /*
         * ---------------------------------------------------------
         * 2. Check VM list
         * ---------------------------------------------------------
         */

        if (vmList == null ||
                vmList.isEmpty()) {

            System.out.println(
                    "FCFS: No VMs available."
            );

            return;
        }


        /*
         * ---------------------------------------------------------
         * 3. Sort Cloudlets by arrival/submission time
         * ---------------------------------------------------------
         *
         * The Cloudlet with the smallest submission delay
         * comes first.
         */

        cloudletList.sort(
                Comparator.comparingDouble(
                        Cloudlet::getSubmissionDelay
                )
        );


        /*
         * ---------------------------------------------------------
         * 4. Print FCFS information
         * ---------------------------------------------------------
         */

        System.out.println();
        System.out.println(
                "======================================"
        );

        System.out.println(
                "FCFS Scheduler"
        );

        System.out.println(
                "======================================"
        );

        System.out.println(
                "Cloudlets to schedule: "
                        + cloudletList.size()
        );

        System.out.println(
                "Available VMs: "
                        + vmList.size()
        );

        System.out.println(
                "--------------------------------------"
        );


        /*
         * ---------------------------------------------------------
         * 5. Assign Cloudlets to VMs
         * ---------------------------------------------------------
         *
         * FCFS order:
         *
         * First Cloudlet -> first VM
         * Second Cloudlet -> second VM
         * ...
         *
         * When all VMs are used, assignment starts again
         * from the first VM.
         */

        for (int i = 0;
             i < cloudletList.size();
             i++) {

            Cloudlet cloudlet =
                    cloudletList.get(i);

            Vm vm =
                    vmList.get(
                            i % vmList.size()
                    );


            /*
             * Assign the Cloudlet to the selected VM.
             */

            cloudlet.setVm(vm);


            /*
             * Print only the first 20 assignments.
             *
             * This prevents 1000 lines from being printed.
             */

            if (i < 20) {

                System.out.println(
                        "FCFS: Cloudlet "
                                + cloudlet.getId()
                                + " -> VM "
                                + vm.getId()
                                + " | Arrival = "
                                + String.format(
                                        "%.4f",
                                        cloudlet
                                                .getSubmissionDelay()
                                )
                );
            }
        }


        /*
         * ---------------------------------------------------------
         * 6. Indicate that remaining assignments exist
         * ---------------------------------------------------------
         */

        if (cloudletList.size() > 20) {

            System.out.println(
                    "... "
                    + (cloudletList.size() - 20)
                    + " more Cloudlets assigned."
            );
        }


        /*
         * ---------------------------------------------------------
         * 7. Finish
         * ---------------------------------------------------------
         */

        System.out.println(
                "--------------------------------------"
        );

        System.out.println(
                "FCFS scheduling completed."
        );

        System.out.println(
                "======================================"
        );
    }
}
