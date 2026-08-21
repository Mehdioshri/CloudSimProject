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

        runBaseline();

        runFCFS();

        System.out.println();
        System.out.println("======================================");
        System.out.println("All simulations finished.");
        System.out.println("======================================");
    }


    private static void runBaseline() {

        System.out.println();
        System.out.println("======================================");
        System.out.println("RUNNING BASELINE");
        System.out.println("======================================");

        CloudSimPlus simulation =
                new CloudSimPlus();

        Datacenter datacenter =
                DatacenterCreator.createDatacenter(simulation);

        List<Vm> vmList =
                VmCreator.createVms();

        DatacenterBroker broker =
                new DatacenterBrokerSimple(simulation);

        broker.submitVmList(vmList);

        PoissonTaskGenerator poissonGenerator =
                new PoissonTaskGenerator();

        double lambda =
                SimulationConfig.LAMBDA_1;

        List<Double> arrivalTimes =
                poissonGenerator.generateTasks(lambda);

        CloudletGenerator cloudletGenerator =
                new CloudletGenerator();

        List<Cloudlet> cloudletList =
                cloudletGenerator.createCloudlets(
                        arrivalTimes
                );
               

        broker.submitCloudletList(
                cloudletList
        );

        System.out.println();
        System.out.println(
                "Starting BASELINE simulation..."
        );

        simulation.start();

        List<Cloudlet> finishedCloudlets =
                broker.getCloudletFinishedList();

        ResultsAnalyzer analyzer =
                new ResultsAnalyzer();

        analyzer.analyze(
                finishedCloudlets,
                vmList,
                "BASELINE",
                lambda
        );






        System.out.println(
                "BASELINE completed Cloudlets: "
                        + finishedCloudlets.size()
        );
    }


    private static void runFCFS() {

        System.out.println();
        System.out.println("======================================");
        System.out.println("RUNNING FCFS");
        System.out.println("======================================");

        CloudSimPlus simulation =
                new CloudSimPlus();

        Datacenter datacenter =
                DatacenterCreator.createDatacenter(simulation);

        List<Vm> vmList =
                VmCreator.createVms();

        DatacenterBroker broker =
                new DatacenterBrokerSimple(simulation);

        broker.submitVmList(vmList);

        PoissonTaskGenerator poissonGenerator =
                new PoissonTaskGenerator();

        double lambda =
                SimulationConfig.LAMBDA_1;

        List<Double> arrivalTimes =
                poissonGenerator.generateTasks(lambda);

        CloudletGenerator cloudletGenerator =
                new CloudletGenerator();

        List<Cloudlet> cloudletList =
                cloudletGenerator.createCloudlets(
                        arrivalTimes
                );


        // ======================================
        // FCFS SCHEDULER
        // ======================================

        FCFSScheduler fcfsScheduler =
                new FCFSScheduler();

        fcfsScheduler.schedule(
                cloudletList,
                vmList
        );


        // ======================================
        // Submit Cloudlets
        // ======================================

        broker.submitCloudletList(
                cloudletList
        );


        System.out.println();
        System.out.println(
                "Starting FCFS simulation..."
        );

        simulation.start();

        List<Cloudlet> finishedCloudlets =
                broker.getCloudletFinishedList();


        ResultsAnalyzer analyzer =
                new ResultsAnalyzer();

        analyzer.analyze(
                finishedCloudlets,
                vmList,
                "FCFS",
                lambda
        );


        System.out.println(
                "FCFS completed Cloudlets: "
                        + finishedCloudlets.size()
        );
    }


private static void runMinMin() {

    System.out.println();
    System.out.println("======================================");
    System.out.println("RUNNING MIN-MIN");
    System.out.println("======================================");

    // 1. Create a new CloudSim simulation
    CloudSimPlus simulation =
            new CloudSimPlus();

    // 2. Create Datacenter
    Datacenter datacenter =
            DatacenterCreator.createDatacenter(simulation);

    // 3. Create VMs
    List<Vm> vmList =
            VmCreator.createVms();

    // 4. Create Broker
    DatacenterBroker broker =
            new DatacenterBrokerSimple(simulation);

    // 5. Submit VMs to Broker
    broker.submitVmList(vmList);

    // 6. Generate Poisson task arrivals
    PoissonTaskGenerator poissonGenerator =
            new PoissonTaskGenerator();

    double lambda =
            SimulationConfig.LAMBDA_1;

    List<Double> arrivalTimes =
            poissonGenerator.generateTasks(lambda);

    // 7. Create Cloudlets
    CloudletGenerator cloudletGenerator =
            new CloudletGenerator();

    List<Cloudlet> cloudletList =
            cloudletGenerator.createCloudlets(
                    arrivalTimes
            );

    // ======================================
    // 8. MIN-MIN SCHEDULER
    // ======================================

    MinMinScheduler minMinScheduler =
            new MinMinScheduler();

    minMinScheduler.schedule(
            cloudletList,
            vmList
    );

    // ======================================
    // 9. Submit Cloudlets
    // ======================================

    broker.submitCloudletList(
            cloudletList
    );

    // ======================================
    // 10. Start simulation
    // ======================================

    System.out.println();
    System.out.println(
            "Starting MIN-MIN simulation..."
    );

    simulation.start();

    // ======================================
    // 11. Get finished Cloudlets
    // ======================================

    List<Cloudlet> finishedCloudlets =
            broker.getCloudletFinishedList();

    // ======================================
    // 12. Analyze results
    // ======================================

    ResultsAnalyzer analyzer =
            new ResultsAnalyzer();

    analyzer.analyze(
            finishedCloudlets,
            vmList,
            "Min-Min",
            lambda
    );

    // ======================================
    // 13. Print completion information
    // ======================================

    System.out.println(
            "Min-Min completed Cloudlets: "
                    + finishedCloudlets.size()
    );
}
}
