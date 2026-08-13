package ai.raics.cloudsim;

import org.cloudsimplus.brokers.DatacenterBroker;
import org.cloudsimplus.brokers.DatacenterBrokerSimple;
import org.cloudsimplus.cloudlets.Cloudlet;
import org.cloudsimplus.core.CloudSimPlus;
import org.cloudsimplus.datacenters.Datacenter;
import org.cloudsimplus.vms.Vm;

import java.util.List;

public class MainSimulation {

    public static void main(String[] args) {

        System.out.println("======================================");
        System.out.println("CloudSim Project Starting...");
        System.out.println("======================================");

        // --------------------------------
        // 1. Create CloudSim simulation
        // --------------------------------

        CloudSimPlus simulation = new CloudSimPlus();

        // --------------------------------
        // 2. Create Datacenter
        // --------------------------------

        Datacenter datacenter =
                DatacenterCreator.createDatacenter(simulation);

        // --------------------------------
        // 3. Create VMs
        // --------------------------------

        List<Vm> vmList =
                VmCreator.createVms();

        // --------------------------------
        // 4. Create Broker
        // --------------------------------

        DatacenterBroker broker =
                new DatacenterBrokerSimple(simulation);

        // --------------------------------
        // 5. Submit VMs to Broker
        // --------------------------------

        broker.submitVmList(vmList);

        // --------------------------------
        // 6. Generate Poisson arrivals
        // --------------------------------

        PoissonTaskGenerator poissonGenerator =
                new PoissonTaskGenerator();

        double lambda =
                SimulationConfig.LAMBDA_1;

        List<Double> arrivalTimes =
                poissonGenerator.generateTasks(lambda);

        // --------------------------------
        // 7. Create Cloudlets
        // --------------------------------

        CloudletGenerator cloudletGenerator =
                new CloudletGenerator();

        List<Cloudlet> cloudletList =
                cloudletGenerator.createCloudlets(arrivalTimes);

        // --------------------------------
        // 8. Submit Cloudlets to Broker
        // --------------------------------

        broker.submitCloudletList(cloudletList);

        // --------------------------------
        // 9. Print configuration
        // --------------------------------

        System.out.println();
        System.out.println("Simulation Configuration");
        System.out.println("--------------------------------------");

        System.out.println(
                "Number of Hosts: "
                + SimulationConfig.NUMBER_OF_HOSTS
        );

        System.out.println(
                "Number of VMs: "
                + vmList.size()
        );

        System.out.println(
                "Number of Cloudlets: "
                + cloudletList.size()
        );

        System.out.println(
                "Poisson Lambda: "
                + lambda
        );

        // --------------------------------
        // 10. Start simulation
        // --------------------------------

        System.out.println();
        System.out.println("Starting simulation...");

        simulation.start();

        // --------------------------------
        // 11. Get finished Cloudlets
        // --------------------------------

        List<Cloudlet> finishedCloudlets =
                broker.getCloudletFinishedList();

        // --------------------------------
        // 12. Print results
        // --------------------------------

        System.out.println();
        System.out.println("======================================");
        System.out.println("Simulation Finished");
        System.out.println("======================================");

        System.out.println(
                "Submitted Cloudlets: "
                + cloudletList.size()
        );

        System.out.println(
                "Finished Cloudlets: "
                + finishedCloudlets.size()
        );

        System.out.println(
                "Created VMs: "
                + broker.getVmCreatedList().size()
        );

        System.out.println("======================================");
    }
}
